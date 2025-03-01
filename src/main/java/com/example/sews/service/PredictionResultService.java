package com.example.sews.service;

import com.example.sews.dto.vo.DevicePredictionDto;
import com.example.sews.repo.PredictionResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PredictionResultService {
@Autowired
    private  PredictionResultRepository predictionResultRepository;


    // 获取所有预测结果
    public List<DevicePredictionDto> getAllPredictionResults() {
        List<Object[]> results = predictionResultRepository.findAllDeviceWithPredictionResult();
        return results.stream()
                .map(row -> new DevicePredictionDto(
                        (Integer) row[0],
                        (String) row[1],
                        (String) row[2],
                        (Double) row[3],
                        (Double) row[4],
                        (String) row[5],
                        (String) row[6],
                        (String) row[7]
                ))
                .collect(Collectors.toList());
    }

    // 根据设备ID获取预测结果
//    public List<PredictionResultDTO> getPredictionResultsByDeviceId(Integer deviceId) {
//        return predictionResultRepository.findByDeviceId(deviceId);
//    }
//
//    // 根据模型ID获取预测结果
//    public List<PredictionResultDTO> getPredictionResultsByModelId(Integer modelId) {
//        return predictionResultRepository.findByModelId(modelId);
//    }

    // 根据预测ID获取预测结果
//    public Optional<PredictionResultDTO> getPredictionResultById(Integer forecastId) {
//        return predictionResultRepository.findById(forecastId);
//    }
//
//    // 保存或更新预测结果
//    public PredictionResultDTO savePredictionResult(PredictionResultDTO predictionResultDTO) {
//        return predictionResultRepository.save(predictionResultDTO);
//    }

    // 删除预测结果
    public void deletePredictionResult(Integer forecastId) {
        predictionResultRepository.deleteById(forecastId);
    }
}
