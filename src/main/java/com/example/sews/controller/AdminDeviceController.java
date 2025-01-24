package com.example.sews.controller;

import com.example.sews.dto.vo.AdminDeviceInfoDTO;
import com.example.sews.service.AdminDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin-device")
public class AdminDeviceController {

    @Autowired
    private AdminDeviceService adminDeviceService;

    // 获取管理员和设备的联合信息
    @GetMapping("/list")
    public ResponseEntity<List<AdminDeviceInfoDTO>> getAdminDeviceInfo() {
        List<AdminDeviceInfoDTO> adminDeviceInfo = adminDeviceService.getAdminDeviceInfo();
        return ResponseEntity.ok(adminDeviceInfo);
    }
}
