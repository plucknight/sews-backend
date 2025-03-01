package com.example.sews.controller;

import com.example.sews.dto.AdminDevice;
import com.example.sews.dto.Device;
import com.example.sews.dto.vo.AdminDeviceInfoDTO;
import com.example.sews.service.AdminDeviceService;
import com.example.sews.service.DeviceService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private AdminDeviceService adminDeviceService;
    // 添加设备
    @PostMapping("/addDevice")
    public ResponseEntity<Device> addDevice(@RequestBody AdminDeviceInfoDTO adminDeviceInfoDTO) {
        try {
            // 1. 创建并保存设备信息到 devices 表
            Device device = new Device();
            BeanUtils.copyProperties(adminDeviceInfoDTO, device); // 将 DTO 数据复制到 Device 实体
            device.setDataTime(new Date()); // 设置默认的 data_time 字段
            Device savedDevice = deviceService.addDevice(device);

            // 2. 创建并保存管理员与设备的关联信息到 admin_device 表
            AdminDevice adminDevice = new AdminDevice();
            adminDevice.setAdminId(adminDeviceInfoDTO.getAdminId().intValue()); // 管理员 ID
            adminDevice.setDeviceId(savedDevice.getDeviceId()); // 设备 ID
            adminDevice.setCreatedAt(LocalDateTime.now()); // 设置创建时间
            adminDeviceService.addAdminDevice(adminDevice);
            // 3. 返回保存成功的设备信息
            return ResponseEntity.status(201).body(savedDevice);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // 返回错误响应
        }
    }

    // 查询所有设备
    @GetMapping("/getAllDevices")
    public ResponseEntity<List<com.example.sews.dto.vo.AdminDeviceInfoDTO>> getAllDevices() {
        List<com.example.sews.dto.vo.AdminDeviceInfoDTO> devices = deviceService.getAllDevices();
        return ResponseEntity.status(200).body(devices);
    }

    // 根据设备ID查询设备
    @GetMapping("/{deviceId}")
    public ResponseEntity<Device> getDeviceById(@PathVariable("deviceId") int deviceId) {
        Device device = deviceService.getDeviceById(deviceId);
        return device != null ? ResponseEntity.ok(device) : ResponseEntity.notFound().build();
    }
    @GetMapping("/deleteDevice/{deviceId}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> deleteDevice(@PathVariable Integer deviceId) {
        try {
            // 1. 从 admin_device 表中删除关联记录
            adminDeviceService.deleteAdminDevice(Long.valueOf(deviceId));
            // 2. 从 devices 表中删除设备记录
            deviceService.deleteDeviceById(deviceId);
            return ResponseEntity.ok("Device deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to delete device");
        }
    }
}
