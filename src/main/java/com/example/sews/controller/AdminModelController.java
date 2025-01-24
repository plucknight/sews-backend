package com.example.sews.controller;

import com.example.sews.dto.vo.AdminModelInfoDTO;
import com.example.sews.service.AdminModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin-model")
public class AdminModelController {

    @Autowired
    private AdminModelService adminModelService;

    // 获取管理员和模型的联合信息
    @GetMapping("/list")
    public ResponseEntity<List<AdminModelInfoDTO>> getAdminModelInfo() {
        List<AdminModelInfoDTO> adminModelInfo = adminModelService.getAdminModelInfo();
        return ResponseEntity.ok(adminModelInfo);
    }
}
