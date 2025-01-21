package com.example.sews.controller;

import com.example.sews.service.DeviceService;
import com.example.sews.dto.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    // 添加设备
    @PostMapping("/addDevice")
    public ResponseEntity<Device> addDevice(@RequestBody Device device) {
        Device createdDevice = deviceService.addDevice(device);
        return ResponseEntity.status(201).body(createdDevice);
    }

    // 查询所有设备
    @GetMapping("/getAllDevices")
    public ResponseEntity<List<Device>> getAllDevices() {
        List<Device> devices = deviceService.getAllDevices();
        return ResponseEntity.status(200).body(devices);
    }

    // 根据设备ID查询设备
    @GetMapping("/{deviceId}")
    public ResponseEntity<Device> getDeviceById(@PathVariable("deviceId") int deviceId) {
        Device device = deviceService.getDeviceById(deviceId);
        return device != null ? ResponseEntity.ok(device) : ResponseEntity.notFound().build();
    }
}
