package com.example.sews.controller;

import com.example.sews.dto.PeakDayPredictionInput;
import com.example.sews.service.PredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prediction")
public class PredictionController {

    @Autowired
    private PredictionService predictionService;

    // 短期发生量预测
    @GetMapping("/short-term")
    public ResponseEntity<String> predictShortTerm(@RequestParam Integer modelId,@RequestParam Integer deviceId) {
        // 调用服务进行短期发生量预测
        String result = predictionService.calculateShortTermPestCount(modelId,deviceId);
        return ResponseEntity.ok(result);
    }

    // 高峰日预测
    @PostMapping("/peak-day")
    public ResponseEntity<String> predictPeakDay(@RequestBody PeakDayPredictionInput input) {
        // 调用服务进行高峰日预测
        String result = predictionService.calculatePeakDay(input);
        return ResponseEntity.ok(result);
    }

}
