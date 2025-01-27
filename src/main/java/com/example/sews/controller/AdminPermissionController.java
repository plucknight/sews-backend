package com.example.sews.controller;

import com.example.sews.dto.vo.AdminPermissionDto;
import com.example.sews.service.AdminDeviceService;
import com.example.sews.service.AdminModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/adminPermission")
public class AdminPermissionController {

    @Autowired
    private AdminDeviceService adminDeviceService;

    @Autowired
    private AdminModelService adminModelService;
    // 获取管理员和设备的联合信息
    @GetMapping("/list")
    public ResponseEntity<List<AdminPermissionDto>> getAdminDeviceInfo() {
        List<AdminPermissionDto> adminDeviceInfo = adminDeviceService.getAdminDeviceInfo();
        List<AdminPermissionDto> adminModelInfo = adminModelService.getAdminModelInfo();
        adminDeviceInfo.addAll(adminModelInfo);
        return ResponseEntity.ok(adminDeviceInfo);
    }
}
