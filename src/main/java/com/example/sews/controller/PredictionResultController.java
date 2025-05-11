package com.example.sews.controller;

import com.example.sews.dto.vo.DevicePredictionDto;
import com.example.sews.service.PredictionResultService;
import com.example.sews.utils.bputils.BPModelInstance;
import com.mathworks.toolbox.javabuilder.MWCharArray;
import com.mathworks.toolbox.javabuilder.MWNumericArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.List;

@RestController
@RequestMapping("/prediction-results")
public class PredictionResultController {

    @Autowired
    private PredictionResultService predictionResultService;

    // 获取所有预测结果
    @GetMapping("/getAll")
    public ResponseEntity<List<DevicePredictionDto>> getAllPredictionResults() {
        List<DevicePredictionDto> results = predictionResultService.getAllPredictionResults();
        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    //    // 根据设备ID获取预测结果
    @GetMapping("/predict/{deviceId}")
    public ResponseEntity<Double> getPrediction(@PathVariable int deviceId) {

        double result = predictionResultService.predictShortTerm(deviceId);
        return ResponseEntity.ok(result);
    }




//    // 根据模型ID获取预测结果
//    @GetMapping("/model/{modelId}")
//    public ResponseEntity<List<PredictionResultDTO>> getPredictionResultsByModelId(@PathVariable Integer modelId) {
//        List<PredictionResultDTO> results = predictionResultService.getPredictionResultsByModelId(modelId);
//        return new ResponseEntity<>(results, HttpStatus.OK);
//    }
//
//    // 根据预测ID获取预测结果
//    @GetMapping("/{forecastId}")
//    public ResponseEntity<PredictionResultDTO> getPredictionResultById(@PathVariable Integer forecastId) {
//        Optional<PredictionResultDTO> result = predictionResultService.getPredictionResultById(forecastId);
//        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    // 创建/更新预测结果
//    @PostMapping
//    public ResponseEntity<PredictionResultDTO> createPredictionResult(@RequestBody PredictionResultDTO predictionResultDTO) {
//        PredictionResultDTO savedResult = predictionResultService.savePredictionResult(predictionResultDTO);
//        return new ResponseEntity<>(savedResult, HttpStatus.CREATED);
//    }

    // 删除预测结果
    @PostMapping("/delete")
    public ResponseEntity<Void> deletePredictionResult(@RequestBody Integer forecastId) {
        predictionResultService.deletePredictionResult(forecastId);
        return ResponseEntity.noContent().build();
    }
}
