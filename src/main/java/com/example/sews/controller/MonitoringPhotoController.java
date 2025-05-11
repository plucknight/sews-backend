package com.example.sews.controller;

import com.example.sews.dto.Admin;
import com.example.sews.dto.MonitoringPhoto;
import com.example.sews.service.AdminService;
import com.example.sews.service.MonitoringPhotoService;
import com.example.sews.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/monitoringPhoto")
public class MonitoringPhotoController {

    @Autowired
    private MonitoringPhotoService monitoringPhotoService;

    @Autowired
    private AdminService adminService;

    @Autowired
    UserService userService;
    // 添加监测照片
    @PostMapping("/addMonitoringPhoto")
    public ResponseEntity<MonitoringPhoto> addMonitoringPhoto(@RequestBody MonitoringPhoto monitoringPhoto) {
        MonitoringPhoto createdMonitoringPhoto = monitoringPhotoService.addMonitoringPhoto(monitoringPhoto);
        return ResponseEntity.status(201).body(createdMonitoringPhoto);
    }

    // 查询指定用户的监测照片
    @GetMapping("/admin/{adminId}")
    public ResponseEntity<List<MonitoringPhoto>> getMonitoringPhotosByAdminId(@PathVariable Integer adminId) {
        List<MonitoringPhoto> monitoringPhotoList = monitoringPhotoService.getMonitoringPhotosByAdminId(adminId);
        return ResponseEntity.ok(monitoringPhotoList);
    }

    @PostMapping("/device")
    public ResponseEntity<List<MonitoringPhoto>> getMonitoringPhotosByAdminId(@RequestBody List<Integer> deviceIds) {
        List<MonitoringPhoto> monitoringPhotoList = monitoringPhotoService.getMonitoringPhotosByDeviceIds(deviceIds);
        return ResponseEntity.ok(monitoringPhotoList);
    }

    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<MonitoringPhoto>> getMonitoringPhotosByDeviceId(@PathVariable Integer deviceId) {
        List<MonitoringPhoto> monitoringPhoto = monitoringPhotoService.getMonitoringPhotosByDeviceId(deviceId);
        return ResponseEntity.ok(monitoringPhoto);
    }

    @GetMapping("/device/count/{userId}")
    public ResponseEntity<Integer> getMonitoringPhotosCountByAdminId(@PathVariable Integer userId) {
        int count = 0;
        Optional<Admin> admin = adminService.findById(userId);
        Integer role = admin.get().getRole();
        if (role == 1) {
            count=monitoringPhotoService.getAllMonitoringPhotos().size();
        }   else if(role == 2) {
            count= monitoringPhotoService.getMonitoringPhotosByAdminId(userId).size();
         }else if (role == 3) {
            List<Integer> deviceIds = userService.getDeviceIDsByUserId(userId);
            count= monitoringPhotoService.getMonitoringPhotosByDeviceIds(deviceIds).size();
        }
        return ResponseEntity.ok(count);
    }
    // 查询所有监测照片
    @GetMapping("/getAllMonitoringPhotos")
    public ResponseEntity<List<MonitoringPhoto>> getAllMonitoringPhotos() {
        List<MonitoringPhoto> allMonitoringPhotos = monitoringPhotoService.getAllMonitoringPhotos();
//        System.out.println("allMonitoringPhotos = " + allMonitoringPhotos);
        return ResponseEntity.ok(allMonitoringPhotos);
    }

    // 查询指定ID的监测照片
    @GetMapping("/{photoId}")
    public ResponseEntity<MonitoringPhoto> getMonitoringPhotoById(@PathVariable("photoId") int photoId) {
        MonitoringPhoto monitoringPhoto = monitoringPhotoService.getMonitoringPhotoById(photoId);
        return monitoringPhoto != null ? ResponseEntity.ok(monitoringPhoto) : ResponseEntity.notFound().build();
    }

    @GetMapping("/segment")
    public ResponseEntity<double[]> getSegmentPestSum() {
        double[] result = monitoringPhotoService.getSegmentPestAccumulationArray(1);
        return ResponseEntity.ok(result);
    }

}
