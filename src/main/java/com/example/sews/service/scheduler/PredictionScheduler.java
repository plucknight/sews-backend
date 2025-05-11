//package com.example.sews.service.scheduler;
//
//import com.example.sews.dto.Device;
//import com.example.sews.service.PredictionService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Configuration
//@EnableScheduling
//public class PredictionScheduler {
//
//    @Autowired
//    private PredictionService predictionService;
//
//    // 首次换膜后20天启动，每5天执行一次（示例：3月10日换膜 → 3月30日首次预测）
//    @Scheduled(initialDelayString = "#{20 * 24 * 60 * 60 * 1000}", fixedRate = 5 * 24 * 60 * 60 * 1000)
//    public void runBpPrediction() {
//        List<Device> devices = deviceRepository.findAll();
//        devices.forEach(device -> {
//            LocalDate predictDate = LocalDate.now().plusDays(5); // 预测5天后（如4月4日）
//            predictionService.predict(device, predictDate);
//        });
//    }
//}