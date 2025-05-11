package com.example.sews.controller;

import com.example.sews.dto.Admin;
import com.example.sews.dto.AdminDeviceModel;
import com.example.sews.dto.Device;
import com.example.sews.dto.vo.AdminDeviceModelInfoDTO;
import com.example.sews.service.AdminDeviceModelService;
import com.example.sews.service.AdminService;
import com.example.sews.service.DeviceService;
import com.example.sews.service.UserService;
import com.example.sews.utils.GeoUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;

    @Autowired
    UserService userService;

    @Autowired
    private AdminDeviceModelService adminDeviceModelService;
    // 添加设备

    @PostMapping("/addDevice")
    public ResponseEntity<Device> addDevice(@RequestBody AdminDeviceModelInfoDTO AdminDeviceModelInfoDTO) {
        try {
            // 1. 创建并保存设备信息到 devices 表
            Device device = new Device();
            BeanUtils.copyProperties(AdminDeviceModelInfoDTO, device); // 将 DTO 数据复制到 Device 实体
            device.setDataTime(new Date()); // 设置默认的 data_time 字段
            Device savedDevice = deviceService.addDevice(device);

            // 2. 创建并保存管理员与设备的关联信息到 admin_device 表
            AdminDeviceModel adminDeviceModel = new AdminDeviceModel();
            adminDeviceModel.setAdminId(AdminDeviceModelInfoDTO.getAdminId().intValue()); // 管理员 ID
            adminDeviceModel.setDeviceId(savedDevice.getDeviceId()); // 设备 ID
            adminDeviceModel.setModelId(AdminDeviceModelInfoDTO.getModelId()); // 设备 ID
            adminDeviceModel.setCreatedAt(LocalDateTime.now()); // 设置创建时间
            adminDeviceModelService.addAdminDevice(adminDeviceModel);
            // 3. 返回保存成功的设备信息
            return ResponseEntity.status(201).body(savedDevice);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null); // 返回错误响应
        }
    }

    // 查询所有设备
    @GetMapping("/getAllDevices")
    public ResponseEntity<List<com.example.sews.dto.vo.AdminDeviceModelInfoDTO>> getAllDevices() {
        List<com.example.sews.dto.vo.AdminDeviceModelInfoDTO> devices = deviceService.getAllDevices();
        return ResponseEntity.status(200).body(devices);
    }

    @GetMapping("/getDevices/{adminId}")
    public ResponseEntity<List<AdminDeviceModelInfoDTO>> deviceByAdminId(@PathVariable Integer adminId) {
        List<AdminDeviceModelInfoDTO> devicesByAdminId = deviceService.getDevicesByAdminId(adminId);
        return ResponseEntity.status(200).body(devicesByAdminId);
    }

    // 根据设备ID查询设备
    @GetMapping("/{deviceId}")
    public ResponseEntity<AdminDeviceModelInfoDTO> getDeviceById(@PathVariable Integer deviceId) {
        AdminDeviceModelInfoDTO device = deviceService.getDeviceByDeviceId(deviceId);
        return device != null ? ResponseEntity.ok(device) : ResponseEntity.notFound().build();
    }

    @GetMapping("/deleteDevice/{deviceId}")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<String> deleteDevice(@PathVariable Integer deviceId) {
        try {
            // 1. 从 admin_device 表中删除关联记录
            adminDeviceModelService.deleteAdminDevice(Long.valueOf(deviceId));
            // 2. 从 devices 表中删除设备记录
            deviceService.deleteDeviceById(deviceId);
            return ResponseEntity.ok("Device deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to delete device");
        }
    }

    @Autowired
    private AdminService adminService;

    @GetMapping("/deviceCount/{userId}")
    public ResponseEntity<Integer> deviceCount(@PathVariable Integer userId) {
        try {
            int count = 0;
            Optional<Admin> admin = adminService.findById(userId);
            Integer role = admin.get().getRole();
            if (role == 1) {
                count = deviceService.getAllDevices().size();
            } else if(role == 2) {
                count = adminDeviceModelService.getDevicesCount(userId);
            }else if (role == 3) {
                 count = userService.getDeviceIDsByUserId(userId).size();
            }
            return ResponseEntity.ok(count);
        } catch (
                Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(0);
        }
    }


}
