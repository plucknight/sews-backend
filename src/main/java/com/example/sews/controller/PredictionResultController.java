package com.example.sews.controller;

import com.example.sews.dto.PredictionResultDTO;
import com.example.sews.service.PredictionResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/prediction-results")
public class PredictionResultController {

    private final PredictionResultService predictionResultService;

    @Autowired
    public PredictionResultController(PredictionResultService predictionResultService) {
        this.predictionResultService = predictionResultService;
    }

    // 获取所有预测结果
    @GetMapping
    public ResponseEntity<List<PredictionResultDTO>> getAllPredictionResults() {
        List<PredictionResultDTO> results = predictionResultService.getAllPredictionResults();
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    // 根据设备ID获取预测结果
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<PredictionResultDTO>> getPredictionResultsByDeviceId(@PathVariable Integer deviceId) {
        List<PredictionResultDTO> results = predictionResultService.getPredictionResultsByDeviceId(deviceId);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    // 根据模型ID获取预测结果
    @GetMapping("/model/{modelId}")
    public ResponseEntity<List<PredictionResultDTO>> getPredictionResultsByModelId(@PathVariable Integer modelId) {
        List<PredictionResultDTO> results = predictionResultService.getPredictionResultsByModelId(modelId);
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    // 根据预测ID获取预测结果
    @GetMapping("/{forecastId}")
    public ResponseEntity<PredictionResultDTO> getPredictionResultById(@PathVariable Integer forecastId) {
        Optional<PredictionResultDTO> result = predictionResultService.getPredictionResultById(forecastId);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 创建/更新预测结果
    @PostMapping
    public ResponseEntity<PredictionResultDTO> createPredictionResult(@RequestBody PredictionResultDTO predictionResultDTO) {
        PredictionResultDTO savedResult = predictionResultService.savePredictionResult(predictionResultDTO);
        return new ResponseEntity<>(savedResult, HttpStatus.CREATED);
    }

    // 删除预测结果
    @PostMapping("/delete")
    public ResponseEntity<Void> deletePredictionResult(@RequestBody Integer forecastId) {
        predictionResultService.deletePredictionResult(forecastId);
        return ResponseEntity.noContent().build();
    }
}
