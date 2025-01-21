package com.example.sews.service;

import com.example.sews.dto.WarningRules;
import com.example.sews.repo.WarningRulesRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WarningRulesService {

    private final WarningRulesRepository warningRulesRepository;

    public WarningRulesService(WarningRulesRepository warningRulesRepository) {
        this.warningRulesRepository = warningRulesRepository;
    }

    /**
     * 获取所有预警规则
     *
     * @return List<WarningRules> - 预警规则列表
     */
    public List<WarningRules> getAllWarningRules() {
        return warningRulesRepository.findAll();
    }

    /**
     * 根据规则ID获取单个预警规则信息
     *
     * @param ruleId 规则ID
     * @return WarningRules - 预警规则信息
     */
    public WarningRules getWarningRuleById(Integer ruleId) {
        Optional<WarningRules> warningRule = warningRulesRepository.findById(ruleId);
        return warningRule.orElse(null);
    }

    /**
     * 创建新的预警规则
     *
     * @param warningRules 预警规则信息
     * @return WarningRules - 新创建的预警规则信息
     */
    public WarningRules createWarningRule(WarningRules warningRules) {
        return warningRulesRepository.save(warningRules);
    }

    /**
     * 更新已存在的预警规则信息
     *
     * @param ruleId       规则ID
     * @param warningRules 更新后的预警规则信息
     * @return WarningRules - 更新后的预警规则信息
     */
    public WarningRules updateWarningRule(Integer ruleId, WarningRules warningRules) {
        Optional<WarningRules> existingRule = warningRulesRepository.findById(ruleId);
        if (existingRule.isPresent()) {
            WarningRules updatedRule = existingRule.get();
            updatedRule.setRuleName(warningRules.getRuleName());
            updatedRule.setDeviceType(warningRules.getDeviceType());
            updatedRule.setWarningLevel(warningRules.getWarningLevel());
            updatedRule.setThreshold(warningRules.getThreshold());
            updatedRule.setDescription(warningRules.getDescription());
            updatedRule.setIsActive(warningRules.getIsActive());
            updatedRule.setCreateTime(warningRules.getCreateTime());
            updatedRule.setUpdateTime(warningRules.getUpdateTime());
            return warningRulesRepository.save(updatedRule);
        }
        return null;  // 如果规则不存在，则返回 null
    }

    /**
     * 删除指定的预警规则
     *
     * @param ruleId 规则ID
     * @return boolean - 是否删除成功
     */
    public boolean deleteWarningRule(Integer ruleId) {
        Optional<WarningRules> warningRule = warningRulesRepository.findById(ruleId);
        if (warningRule.isPresent()) {
            warningRulesRepository.deleteById(ruleId);
            return true;
        }
        return false;
    }
}
