package com.example.sews.controller;

import com.example.sews.dto.vo.AdminPermissionDto;
import com.example.sews.service.AdminDeviceModelService;
import com.example.sews.service.DeviceModelService;
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
    private AdminDeviceModelService adminDeviceModelService;

    @Autowired
    private DeviceModelService DeviceModelService;
    // 获取管理员和设备的联合信息
    @GetMapping("/list")
    public ResponseEntity<List<AdminPermissionDto>> getAdminDeviceInfo() {
        List<AdminPermissionDto> adminDeviceInfo = adminDeviceModelService.getAdminDeviceInfo();
        List<AdminPermissionDto> DeviceModelInfo = DeviceModelService.getDeviceModelInfo();
        adminDeviceInfo.addAll(DeviceModelInfo);
        return ResponseEntity.ok(adminDeviceInfo);
    }
}
