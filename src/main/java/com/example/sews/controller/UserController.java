package com.example.sews.controller;

import com.example.sews.dto.Admin;
import com.example.sews.dto.MonitoringPhoto;
import com.example.sews.dto.User;
import com.example.sews.dto.WarningInfo;
import com.example.sews.dto.anno.LogOperation;
import com.example.sews.dto.vo.AdminDeviceModelInfoDTO;
import com.example.sews.dto.vo.DevicePredictionDto;
import com.example.sews.dto.vo.DeviceUpdateRequest;
import com.example.sews.repo.MonitoringPhotoRepository;
import com.example.sews.service.*;
import com.example.sews.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @Author: myh
 * @CreateTime: 2025-04-01  22:33
 * @Description: TODO
 * @Version: 1.0
 */

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    AdminService adminService;

    @Autowired
    private PredictionResultService predictionResultService;

    @Autowired
    private WarningInfoService warningInfoService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private MonitoringPhotoRepository monitoringPhotoRepository;
    // 根据 adminId 获取其所有用户及分配设备信息
    @GetMapping("/device/{adminId}")
    public ResponseEntity<List<User>> getUsersByAdminId(@PathVariable Integer adminId) {
        return userService.findByAdminId(adminId);
    }

    @GetMapping("/getDeviceList/{userId}")
    public ResponseEntity<List<AdminDeviceModelInfoDTO>> getDevicesByUserId(@PathVariable Integer userId) {
        List<AdminDeviceModelInfoDTO> list=new ArrayList<>();
        List<Integer> deviceIDsByUserId = userService.getDeviceIDsByUserId(userId);
        for(Integer deviceId : deviceIDsByUserId){
            AdminDeviceModelInfoDTO devicesByAdminId = deviceService.getDeviceByDeviceId(deviceId);
            list.add(devicesByAdminId);
        }
        return ResponseEntity.status(200).body(list);
    }
//    @GetMapping("/device/{userId}")
//    public ResponseEntity<List<Integer>> UserDevice(@PathVariable Integer userId) {
//        List<Integer> deviceIds = userService.getDeviceIDsByUserId(userId);
//        System.out.println("deviceIds = " + deviceIds);
////        List<AdminDeviceModelInfoDTO> res = new ArrayList<>();
////        for (Integer deviceId : deviceIds) {
////            List<AdminDeviceModelInfoDTO> list = deviceService.getDeviceByDeviceId(deviceId);
////            res.addAll(list);
////        }
//        return ResponseEntity.ok(deviceIds);
//    }
    // 添加设备ID
@LogOperation("为用户添加设备")
    @PostMapping("/deviceIds/add")
    public ResponseEntity<String> addDeviceId(@RequestParam Integer userId,
                            @RequestParam Integer deviceId) {
        boolean flag = userService.addDeviceIDForUser(userId, deviceId);
        return flag ? ResponseEntity.ok("添加成功") : ResponseEntity.badRequest().body("添加失败");
    }

    // 删除设备ID
    @LogOperation("删除设备")
    @PostMapping("/deviceIds/delete")
    public ResponseEntity<String> deleteDeviceId(@RequestParam Integer userId,
                               @RequestParam Integer deviceId) {
        boolean flag =userService.deleteDeviceIDForUser(userId, deviceId);
        return flag ? ResponseEntity.ok("添加成功") : ResponseEntity.badRequest().body("添加失败");
    }

    // 修改设备ID
    @LogOperation("修改设备")
    @PostMapping("/deviceIds/update")
    public ResponseEntity<String> updateDeviceId(@RequestBody DeviceUpdateRequest request) {
        boolean flag = userService.updateDeviceIDForUser(request.getUserId(), request.getNewDeviceIds());
        System.out.println("flag = " + flag);
        return flag ? ResponseEntity.ok("设备更新成功") : ResponseEntity.badRequest().body("设备更新失败");
    }
    //预警
    @GetMapping("/warningInfo/{userId}")
    public ResponseEntity<List<WarningInfo>> UserWarningInfo(@PathVariable Integer userId) {
        List<Integer> deviceIds = userService.getDeviceIDsByUserId(userId);

        List<WarningInfo> res = new ArrayList<>();
        for (Integer deviceId : deviceIds) {
            List<WarningInfo> list = warningInfoService.getWarningInfoByDevice(deviceId);
            res.addAll(list);
        }
        return ResponseEntity.ok(res);
    }

    //预测结果
    @GetMapping("/prediction/{userId}")
    public ResponseEntity<List<DevicePredictionDto>> UserDevicePrediction(@PathVariable Integer userId) {
        List<Integer> deviceIds = userService.getDeviceIDsByUserId(userId);
        List<DevicePredictionDto> res = predictionResultService.getAllPredictionResultsByDeviceIds(deviceIds);
        return ResponseEntity.ok(res);
    }

    //图片
    @GetMapping("/photo/{userId}")
    public ResponseEntity<List<MonitoringPhoto>> UserPhoto(@PathVariable Integer userId) {
        List<Integer> deviceIds = userService.getDeviceIDsByUserId(userId);
        List<MonitoringPhoto> monitoringPhotoList = monitoringPhotoRepository.findByDeviceIdIn(deviceIds);
        return ResponseEntity.ok(monitoringPhotoList);
    }
    @LogOperation("用户下载")
    @GetMapping("/photo/download")
    public ResponseEntity<byte[]> downloadPhotosByTimeRange(
            @RequestParam Integer userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<Integer> deviceIds;
        if(userId==1){
            deviceIds=deviceService.getDevicesIds();
        }else{
           deviceIds = userService.getDeviceIDsByUserId(userId);
        }

        System.out.println("deviceIds = " + deviceIds);
        Date start = java.sql.Timestamp.valueOf(startTime);
        Date end = java.sql.Timestamp.valueOf(endTime);
        List<MonitoringPhoto> photos = monitoringPhotoRepository.findByDeviceIdInAndDataTimeBetween(deviceIds, start, end);

        System.out.println("photos = " + photos);

        StringBuilder csv = new StringBuilder();
        csv.append("photoId,deviceId,photoDate,dataTime,pestCount,pestType,photoPath\n");
        for (MonitoringPhoto p : photos) {

            csv.append(String.format("%s,%s,%s,%s,%s,%s,%s\n",
                    p.getPhotoId() != null ? p.getPhotoId() : "",
                    p.getDeviceId() != null ? p.getDeviceId() : "",
                    p.getPhotoDate() != null ? p.getPhotoDate() : "",
                    p.getDataTime() != null ? p.getDataTime() : "",
                    p.getPestCount() != null ? p.getPestCount() : "",
                    p.getPestType() != null ? p.getPestType() : "",
                    p.getPhotoPath() != null ? p.getPhotoPath() : ""));
        }
        byte[] csvBytes = csv.toString().getBytes(StandardCharsets.UTF_8);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);  // 设置为字节流
        headers.setContentDisposition(ContentDisposition.attachment().filename("monitoring_photos.csv").build());


        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/id")
    public ResponseEntity<Admin> getAdminById(HttpServletRequest request)  {
        String token = request.getHeader("token");
        String adminId;
        try{
            Claims claims = JwtUtil.parseJWT(token);
            adminId = claims.getSubject();
        }catch (Exception e){
            throw new RuntimeException("Invalid token");
        }
        Optional<Admin> admin = adminService.findById(Integer.valueOf(adminId));
        return admin.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
