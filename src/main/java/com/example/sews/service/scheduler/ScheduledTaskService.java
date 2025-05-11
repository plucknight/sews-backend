package com.example.sews.service.scheduler;

import com.example.sews.config.BPModelLoader;
import com.example.sews.dto.Location;
import com.example.sews.dto.WeatherData;
import com.example.sews.service.DeviceService;
import com.example.sews.service.LocationService;
import com.example.sews.service.PredictionResultService;
import com.example.sews.service.WeatherDataService;
import com.example.sews.utils.XinZhiWeather;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ScheduledTaskService {

    @Autowired
    private PredictionResultService predictionResultService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    LocationService locationService;

    @Autowired
    BPModelLoader   bpModelLoader;

    @Autowired
    private WeatherDataService weatherDataService;
    // 定时调用预测方法，每天凌晨 8 点执行
    @Scheduled(cron = "0 0 8 * * ?")
    public void executeDailyPrediction() {
        List<Integer> devicesIds = deviceService.getDevicesIds();
        for(Integer deviceId : devicesIds) {
            double shortTermResult = bpModelLoader.predictShortTerm(1,deviceId);
            System.out.println("Prediction shortTermResult for device " + deviceId + ": " + shortTermResult);
        }
    }
    @Scheduled(cron = "0 30 9 * * ?")
    public void fetchDeviceImages() {
        // 1. 调用设备接口获取照片
        // 2. 存入照片数据库（设备号、拍摄日期、照片地址）
    }
    @Scheduled(cron = "0 40 9 * * ?")
    public void runImageRecognition() {
        // 1. 查询当天9:30存入的照片
        // 2. 对图片运行识别模型（调用YOLO等），返回昆虫种类与数量
        // 3. 存入害虫发生量数据库：设备号、日期、昆虫种类、实际数量
    }
    @Scheduled(cron = "0 0 23 * * ?")
    public void updateWeatherDataForCities() throws Exception {
        List<String> cities = locationService.findAll().stream()
                .map(Location::getCounty)
                .collect(Collectors.toList());
        // 获取所有的天气数据
        XinZhiWeather demo = new XinZhiWeather();
        List<WeatherData> weatherData = demo.requestWeatherForCities(cities);

        // 遍历每个天气数据并更新
        for (WeatherData data : weatherData) {
            // 查找当前日期和 locationId 是否已经有数据
            WeatherData existingData = weatherDataService.findByLocationIdAndDate(data.getLocationId(), data.getDate());

            if (existingData != null) {
                // 如果数据已经存在，更新现有记录
                existingData.setMaxTemp(data.getMaxTemp());
                existingData.setMinTemp(data.getMinTemp());
                existingData.setPrecipitation(data.getPrecipitation());
                existingData.setHumidity(data.getHumidity());

                weatherDataService.update(existingData); // 更新数据
            } else {
                // 如果没有找到，插入新的记录
                weatherDataService.save(data); // 保存新的数据
            }
        }
    }

    @Scheduled(cron = "0 0 10 * * ?")
    public void runPredictionIfReady() {
        // 1. 遍历设备，检查其最后一次换膜是否已达可预测时间（如已过20天）
        // 2. 对满足条件的设备：
        //    - 查询对应时间段的天气数据和历史虫口数据
        //    - 构造 BP 模型输入，调用模型预测
        //    - 将预测结果写入 "害虫发生量数据库"，预测日期 = 今日 + 5
    }
    @Scheduled(cron = "0 20 9 * * ?")
    public void updateFilmChangeStatus() {
        // 1. 判断哪些设备今天是换膜日（根据初次换膜日 + 5天倍数）
        // 2. 记录换膜（可以存一个日志表或更新设备字段）
    }
//    @Scheduled(cron = "0/1 * * * * ?") // 每秒执行一次
//    public void test() {
//        System.out.println("scheduleTest: " + LocalDateTime.now());
//    }
}
