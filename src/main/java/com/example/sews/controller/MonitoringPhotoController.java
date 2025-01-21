package com.example.sews.controller;

import com.example.sews.dto.MonitoringPhoto;
import com.example.sews.service.MonitoringPhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/monitoringPhoto")
public class MonitoringPhotoController {

    @Autowired
    private MonitoringPhotoService monitoringPhotoService;

    // 添加监测照片
    @PostMapping("/addMonitoringPhoto")
    public ResponseEntity<MonitoringPhoto> addMonitoringPhoto(@RequestBody MonitoringPhoto monitoringPhoto) {
        MonitoringPhoto createdMonitoringPhoto = monitoringPhotoService.addMonitoringPhoto(monitoringPhoto);
        return ResponseEntity.status(201).body(createdMonitoringPhoto);
    }

    // 查询指定设备的监测照片
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<MonitoringPhoto>> getMonitoringPhotosByDeviceId(@PathVariable("deviceId") int deviceId) {
        List<MonitoringPhoto> monitoringPhotoList = monitoringPhotoService.getMonitoringPhotosByDeviceId(deviceId);
        return ResponseEntity.ok(monitoringPhotoList);
    }

    // 查询所有监测照片
    @GetMapping("/getAllMonitoringPhotos")
    public ResponseEntity<List<MonitoringPhoto>> getAllMonitoringPhotos() {
        List<MonitoringPhoto> allMonitoringPhotos = monitoringPhotoService.getAllMonitoringPhotos();
        System.out.println("allMonitoringPhotos = " + allMonitoringPhotos);
        return ResponseEntity.ok(allMonitoringPhotos);
    }

    // 查询指定ID的监测照片
    @GetMapping("/{photoId}")
    public ResponseEntity<MonitoringPhoto> getMonitoringPhotoById(@PathVariable("photoId") int photoId) {
        MonitoringPhoto monitoringPhoto = monitoringPhotoService.getMonitoringPhotoById(photoId);
        return monitoringPhoto != null ? ResponseEntity.ok(monitoringPhoto) : ResponseEntity.notFound().build();
    }
}
