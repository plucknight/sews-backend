package com.example.sews.controller;

import com.example.sews.dto.DeviceMaintenance;
import com.example.sews.service.DeviceMaintenanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/devicesMaintenance")
public class DeviceMaintenanceController {

    @Autowired
    private DeviceMaintenanceService deviceMaintenanceService;

    // 添加设备维护记录
    @PostMapping("/addDeviceMaintenance")
    public ResponseEntity<DeviceMaintenance> addDeviceMaintenance(@RequestBody DeviceMaintenance deviceMaintenance) {
        DeviceMaintenance createdRecord = deviceMaintenanceService.addDeviceMaintenance(deviceMaintenance);
        return ResponseEntity.status(201).body(createdRecord);
    }

    // 根据设备ID查询维护记录
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<DeviceMaintenance>> getDeviceMaintenanceRecordsByDeviceId(@PathVariable("deviceId") int deviceId) {
        List<DeviceMaintenance> records = deviceMaintenanceService.getDeviceMaintenanceRecordsByDeviceId(Integer.valueOf(deviceId));
        return ResponseEntity.ok(records);
    }

    // 根据维护记录ID查询维护记录
    @GetMapping("/{maintenanceId}")
    public ResponseEntity<DeviceMaintenance> getDeviceMaintenanceById(@PathVariable("maintenanceId") int maintenanceId) {
        DeviceMaintenance record = deviceMaintenanceService.getDeviceMaintenanceById(maintenanceId);
        return record != null ? ResponseEntity.ok(record) : ResponseEntity.notFound().build();
    }

    // 查询所有设备的维护记录
    @GetMapping("/getMaintenanceRecords")
    public ResponseEntity<List<DeviceMaintenance>> getAllDeviceMaintenanceRecords() {
        List<DeviceMaintenance> records = deviceMaintenanceService.getAllDeviceMaintenanceRecords();
        return ResponseEntity.ok(records);
    }
    @GetMapping("/device/{deviceId}/maintenance/{maintenanceId}")
    public ResponseEntity<List<DeviceMaintenance>> getDeviceMaintenanceByDeviceIdAndMaintenanceId(
            @PathVariable("deviceId") int deviceId,
            @PathVariable("maintenanceId") int maintenanceId) {
        List<DeviceMaintenance> record = deviceMaintenanceService.getDeviceMaintenanceByDeviceIdAndMaintenanceId(deviceId, maintenanceId);
        return record != null ? ResponseEntity.ok(record) : ResponseEntity.notFound().build();
    }
}
