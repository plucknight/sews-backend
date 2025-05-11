package com.example.sews.service;

import com.example.sews.dto.vo.DevicePredictionDto;
import com.example.sews.repo.DeviceRepository;
import com.example.sews.repo.PredictionResultRepository;
import com.example.sews.utils.bputils.BPModelInstance;
import com.example.sews.utils.bputils.BPUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mathworks.toolbox.javabuilder.MWCharArray;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.*;

@Service
public class PredictionResultService {
    @Autowired
    private PredictionResultRepository predictionResultRepository;

    @Autowired
    private ObjectMapper objectMapper;  // Jackson 序列化 JSON

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private WeatherDataService weatherAnalysisService;

    @Autowired
    private MonitoringPhotoService pestAnalysisService;

    // 预测短期虫口数量的结果
    public double predictShortTerm(int deviceId) {
        // 获取天气数据的 8 个参数（4 段温度 + 4 段降水量）
        double[] weatherData = weatherAnalysisService.getSegmentAveragesArray();

        // 获取虫口数据的 4 个参数（4 段虫口发生量）
        double[] pestData = pestAnalysisService.getSegmentPestAccumulationArray(deviceId);

        // 将这 12 个参数传递给预测方法
        return BPUtil.predictShortTerm(
                weatherData[0], weatherData[1], weatherData[2], weatherData[3], // temp_20_16, temp_15_11, temp_10_6, temp_5_1
                weatherData[4], weatherData[5], weatherData[6], weatherData[7], // rain_20_16, rain_15_11, rain_10_6, rain_5_1
                pestData[0], pestData[1], pestData[2], pestData[3]  // insects_20_16, insects_15_11, insects_10_6, insects_5_1
        );
    }

    

    // 获取所有预测结果
    public List<DevicePredictionDto> getAllPredictionResults() {
        // 查询所有设备基本信息
        List<DevicePredictionDto> deviceList = deviceRepository.findAllDevicesBaseInfo();
        if (deviceList.isEmpty()) {
            return Collections.emptyList();
        }

        // 查询所有设备的预测结果
        List<Object[]> predictions = predictionResultRepository.findAllPredictions();

        // 按 deviceId 分组预测数据
        Map<Integer, List<Map<String, Object>>> predictionMap = new HashMap<>();
        for (Object[] obj : predictions) {
            Integer deviceId = (Integer) obj[0];

            Map<String, Object> modelInfo = new HashMap<>();
            modelInfo.put("modelId", obj[1]);
            modelInfo.put("modelName", obj[2]);
            modelInfo.put("modelType", obj[3]);
            modelInfo.put("forecastValue", obj[4]);

            predictionMap.computeIfAbsent(deviceId, k -> new ArrayList<>()).add(modelInfo);
        }

        // 遍历设备，填充预测信息
        for (DevicePredictionDto dto : deviceList) {
            Integer deviceId = dto.getDeviceId();
            List<Map<String, Object>> predictionList = predictionMap.getOrDefault(deviceId, new ArrayList<>());

            try {
                dto.setPrediction(objectMapper.writeValueAsString(predictionList));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            // 计算防治建议
            dto.setSuggestion(generateSuggestion(predictionList));
        }

        return deviceList;
    }


    public List<DevicePredictionDto> getAllPredictionResultsByDeviceIds(List<Integer> deviceIds) {
        if (deviceIds == null || deviceIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 查询设备基本信息
        List<DevicePredictionDto> deviceList = deviceRepository.findDeviceBaseInfoByDeviceIds(deviceIds);
        if (deviceList.isEmpty()) {
            return Collections.emptyList();
        }

        // 查询所有设备的预测结果
        List<Object[]> predictions = predictionResultRepository.findPredictionsByDeviceIds(deviceIds);

        // 按 deviceId 分组预测数据
        Map<Integer, List<Map<String, Object>>> predictionMap = new HashMap<>();
        for (Object[] obj : predictions) {
            Integer deviceId = (Integer) obj[0];

            Map<String, Object> modelInfo = new HashMap<>();
            modelInfo.put("modelId", obj[1]);
            modelInfo.put("modelName", obj[2]);
            modelInfo.put("modelType", obj[3]);
            modelInfo.put("forecastValue", obj[4]);

            predictionMap.computeIfAbsent(deviceId, k -> new ArrayList<>()).add(modelInfo);
        }

        // 遍历设备，填充预测信息
        for (DevicePredictionDto dto : deviceList) {
            Integer deviceId = dto.getDeviceId();
            List<Map<String, Object>> predictionList = predictionMap.getOrDefault(deviceId, new ArrayList<>());

            try {
                dto.setPrediction(objectMapper.writeValueAsString(predictionList));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            // 计算防治建议
            dto.setSuggestion(generateSuggestion(predictionList));
        }

        return deviceList;
    }

    private String generateSuggestion(List<Map<String, Object>> predictions) {
        int maxValue = 0;
        for (Map<String, Object> p : predictions) {
            int value = p.get("forecastValue") != null ? Integer.parseInt(p.get("forecastValue").toString().replaceAll("\\D+", "")) : 0;
            maxValue = Math.max(maxValue, value);
        }
        return maxValue > 100 ? "建议加强防治措施，如喷洒农药" : "建议常规监测，无需额外防治";
    }

    // 删除预测结果
    public void deletePredictionResult(Integer forecastId) {
        predictionResultRepository.deleteById(forecastId);
    }
}
