package com.example.sews.service;

import com.example.sews.dto.ModelInfo;
import com.example.sews.repo.ModelInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ModelInfoService {

    private final ModelInfoRepository modelInfoRepository;

    public ModelInfoService(ModelInfoRepository modelInfoRepository) {
        this.modelInfoRepository = modelInfoRepository;
    }

    /**
     * 获取所有模型信息
     *
     * @return List<ModelInfo> - 模型信息列表
     */
    public List<ModelInfo> getAllModels() {
        return modelInfoRepository.findAll();
    }

    /**
     * 根据模型ID获取单个模型信息
     *
     * @param modelId 模型ID
     * @return ModelInfo - 模型信息
     */
    public ModelInfo getModelById(Integer modelId) {
        Optional<ModelInfo> modelInfo = modelInfoRepository.findById(modelId);
        return modelInfo.orElse(null);
    }

    /**
     * 创建一个新的模型信息
     *
     * @param modelInfo 模型信息
     * @return ModelInfo - 新创建的模型信息
     */
    public ModelInfo createModel(ModelInfo modelInfo) {
        return modelInfoRepository.save(modelInfo);
    }

    /**
     * 更新模型信息
     *
     * @param modelId   模型ID
     * @param modelInfo 更新后的模型信息
     * @return ModelInfo - 更新后的模型信息
     */
    public ModelInfo updateModel(Integer modelId, ModelInfo modelInfo) {
        Optional<ModelInfo> existingModel = modelInfoRepository.findById(modelId);
        if (existingModel.isPresent()) {
            ModelInfo updatedModel = existingModel.get();
            updatedModel.setModelName(modelInfo.getModelName());
            updatedModel.setModelType(modelInfo.getModelType());
            updatedModel.setModelVersion(modelInfo.getModelVersion());
            updatedModel.setModelFilePath(modelInfo.getModelFilePath());
            updatedModel.setDataTime(modelInfo.getDataTime());
            return modelInfoRepository.save(updatedModel);
        }
        return null;  // 如果模型不存在，则返回 null
    }

    /**
     * 删除模型信息
     *
     * @param modelId 模型ID
     * @return boolean - 是否删除成功
     */
    public boolean deleteModel(Integer modelId) {
        Optional<ModelInfo> modelInfo = modelInfoRepository.findById(modelId);
        if (modelInfo.isPresent()) {
            modelInfoRepository.deleteById(modelId);
            return true;
        }
        return false;
    }
}
