//package com.example.sews.service.scheduler;
//
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//public class ModelService {
//
//    @Value("${bp-model.url}")
//    private String modelUrl;
//    public class ModelInput {
//        private double avgTemperature;
//        private double totalRainfall;
//        private int actualPestCount;
//    }
//    public class ModelOutput {
//        private int predictedCount;
//        private String riskLevel; // 风险等级（如"高风险"）
//    }
//    // 调用BP模型API
//    public ModelOutput callBpModel(ModelInput input) {
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<ModelOutput> response = restTemplate.postForEntity(
//            modelUrl,
//            input,
//            ModelOutput.class
//        );
//        return response.getBody();
//    }
//}
