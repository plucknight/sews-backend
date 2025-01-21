package com.example.sews.controller;

import com.example.sews.dto.WarningRules;
import com.example.sews.service.WarningRulesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warningRules")
public class WarningRulesController {

    @Autowired
    private WarningRulesService warningRulesService;

    // 获取所有预警规则
    @GetMapping("/getAllWarningRules")
    public ResponseEntity<List<WarningRules>> getAllWarningRules() {
        List<WarningRules> warningRules = warningRulesService.getAllWarningRules();
        return ResponseEntity.ok(warningRules);
    }

    // 获取单个预警规则信息
    @GetMapping("/{rule_id}")
    public ResponseEntity<WarningRules> getWarningRuleById(@PathVariable Integer rule_id) {
        WarningRules warningRule = warningRulesService.getWarningRuleById(rule_id);
        if (warningRule == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(warningRule);
    }

    // 添加新的预警规则
    @PostMapping("/addWarningRule")
    public ResponseEntity<WarningRules> createWarningRule(@RequestBody WarningRules warningRules) {
        WarningRules newWarningRule = warningRulesService.createWarningRule(warningRules);
        return ResponseEntity.status(201).body(newWarningRule);
    }

    // 更新预警规则信息
    @PostMapping("/update")
    public ResponseEntity<WarningRules> updateWarningRule( @RequestBody WarningRules warningRules) {
        WarningRules updatedWarningRule = warningRulesService.updateWarningRule(warningRules.getRuleId(), warningRules);
        if (updatedWarningRule == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedWarningRule);
    }

    // 删除预警规则
    @GetMapping("/delete/{rule_id}")
    public ResponseEntity<Void> deleteWarningRule(@PathVariable Integer rule_id) {
        boolean isDeleted = warningRulesService.deleteWarningRule(rule_id);
        if (!isDeleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
