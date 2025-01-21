package com.example.sews.controller;

import com.example.sews.dto.ModelInfo;
import com.example.sews.service.ModelInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/models")
public class ModelController {

    @Autowired
    private ModelInfoService modelInfoService;


    // 获取所有模型信息
    @GetMapping("/getAllModels")
    public ResponseEntity<List<ModelInfo>> getAllModels() {
        List<ModelInfo> models = modelInfoService.getAllModels();
        return ResponseEntity.ok(models);
    }

    // 获取单个模型信息
    @GetMapping("/{model_id}")
    public ResponseEntity<ModelInfo> getModelById(@PathVariable Integer model_id) {
        ModelInfo model = modelInfoService.getModelById(model_id);
        if (model == null) {
            return ResponseEntity.notFound().build();
        }
        System.out.println("ResponseEntity.ok(model) = " + ResponseEntity.ok(model));
        return ResponseEntity.ok(model);
    }

    // 添加新的模型信息
    @PostMapping("/addModel" )
    public ResponseEntity<ModelInfo> createModel(@RequestBody ModelInfo modelInfo) {
        System.out.println("modelInfo = " + modelInfo);
        ModelInfo newModel = modelInfoService.createModel(modelInfo);
        return ResponseEntity.status(201).body(newModel);
    }

    // 更新模型信息
    @PostMapping("/update")
    public ResponseEntity<ModelInfo> updateModel(@PathVariable Integer model_id, @RequestBody ModelInfo modelInfo) {
        ModelInfo updatedModel = modelInfoService.updateModel(model_id, modelInfo);
        if (updatedModel == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedModel);
    }

    // 删除模型信息
    @GetMapping("/delete/{model_id}")
    public ResponseEntity<Void> deleteModel(@PathVariable Integer model_id) {
        System.out.println("model_id = " + model_id);
        boolean isDeleted = modelInfoService.deleteModel(model_id);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
