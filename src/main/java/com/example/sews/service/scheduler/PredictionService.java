//package com.example.sews.service.scheduler;
//
//import com.example.sews.dto.Device;
//import com.example.sews.dto.PestRecord;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//public class PredictionService {
//
//    @Autowired
//    private WeatherRepository weatherRepo;
//    @Autowired
//    private PestRecordRepository pestRepo;
//    @Autowired
//    private ModelService modelService;
//
//    public void predict(Device device, LocalDate targetDate) {
//        // 1. 获取时间窗口数据（示例：3月10-14日 → 窗口期前20-16天）
//        List<LocalDate> windowDates = calculateWindowDates(device.getFirstMembraneDate(), targetDate);
//
//        // 2. 查询天气数据
//        List<Weather> weatherData = weatherRepo.findByDateBetween(
//            windowDates.get(0),
//            windowDates.get(1)
//        );
//
//        // 3. 计算气象指标
//        double avgTemp = weatherData.stream()
//            .mapToDouble(w -> (w.getMaxTemp() + w.getMinTemp()) / 2)
//            .average().orElse(0);
//
//        double totalRain = weatherData.stream()
//            .mapToDouble(Weather::getPrecipitation)
//            .sum();
//
//        // 4. 查询实际虫量（根据窗口期结束日期获取）
//        LocalDate pestRecordDate = windowDates.get(1).plusDays(1); // 如3月15日
//        PestRecord pestData = pestRepo.findByDeviceIdAndRecordDate(
//            device.getDeviceId(),
//            pestRecordDate
//        );
//
//        // 5. 调用BP模型（伪代码，需对接实际模型）
//        ModelInput input = new ModelInput(avgTemp, totalRain, pestData.getActualCount());
//        ModelService.ModelOutput output = modelService.callBpModel(input);
//
//        // 6. 存储预测结果
//        PestRecord prediction = new PestRecord();
//        prediction.setDeviceId(device.getDeviceId());
//        prediction.setRecordDate(targetDate);          // 预测目标日期（如4月4日）
//        prediction.setPredictedCount(output.getCount());
//        pestRepo.save(prediction);
//
//        // 7. 触发可视化更新（WebSocket或消息队列）
//        eventPublisher.publishPredictionEvent(output);
//    }
//
//    // 计算时间窗口（根据首次换膜日期和目标预测日期）
//    private List<LocalDate> calculateWindowDates(LocalDate firstMembraneDate, LocalDate targetDate) {
//        // 实现窗口期逻辑（如前20-16天、前15-11天等）
//        // 返回[startDate, endDate]
//    }
//}