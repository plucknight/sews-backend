package com.example.sews.controller;

import com.example.sews.dto.Admin;
import com.example.sews.dto.WarningInfo;
import com.example.sews.service.AdminService;
import com.example.sews.service.UserService;
import com.example.sews.service.WarningInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/warning")
public class WarningInfoController {

    @Autowired
    private WarningInfoService warningInfoService;
    @Autowired
    private AdminService adminService;

    @Autowired
    UserService userService;

    // 创建预警信息
    @PostMapping("/addWarningInfo")
    public ResponseEntity<String> createWarningInfo(@RequestBody WarningInfo warningInfo) {
        boolean created = warningInfoService.createWarningInfo(warningInfo);
        if (created) {
            return ResponseEntity.ok("预警信息创建成功");
        } else {
            return ResponseEntity.status(400).body("预警信息创建失败");
        }
    }

    // 查询设备的预警信息
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<WarningInfo>> getWarningInfoByDevice(@PathVariable Integer deviceId) {
        List<WarningInfo> warnings = warningInfoService.getWarningInfoByDevice(deviceId);
        return ResponseEntity.ok(warnings);
    }

    // 更新预警信息
    @PostMapping("/update")
    public ResponseEntity<String> updateWarningInfo(@RequestBody WarningInfo warningInfo) {
        boolean updated = warningInfoService.updateWarningInfo(warningInfo.getWarningId(), warningInfo);
        if (updated) {
            return ResponseEntity.ok("预警信息更新成功");
        } else {
            return ResponseEntity.status(400).body("预警信息更新失败");
        }
    }

    // 删除预警信息
    @GetMapping("/delete/{warningId}")
    public ResponseEntity<String> deleteWarningInfo(@PathVariable Integer warningId) {
        boolean deleted = warningInfoService.deleteWarningInfo(warningId);
        if (deleted) {
            return ResponseEntity.ok("预警信息删除成功");
        } else {
            return ResponseEntity.status(400).body("预警信息删除失败");
        }
    }

    @GetMapping("/getAllWarningInfo")
    public ResponseEntity<List<WarningInfo>> getAllWarningInfo() {
        List<WarningInfo> warnings = warningInfoService.getAllWarningInfo();
        return ResponseEntity.ok(warnings);
    }

    @GetMapping("/getWarningInfo/{adminId}")
    public ResponseEntity<List<WarningInfo>> getWarningInfo(@PathVariable Integer adminId) {
        List<WarningInfo> warnings = warningInfoService.findWarningsByAdminId(adminId);
        return ResponseEntity.ok(warnings);
    }


    @GetMapping("/getWarningInfo/count/{userId}")
    public ResponseEntity<Integer> getWarningInfoCount(@PathVariable Integer userId) {
        int count = 0;
        Optional<Admin> admin = adminService.findById(userId);
        Integer role = admin.get().getRole();
        if (role == 1) {
            count = warningInfoService.getAllWarningInfo().size();
        } else if (role == 2) {
            count = warningInfoService.findWarningsByAdminId(userId).size();
        } else if (role == 3) {
            List<Integer> deviceIds = userService.getDeviceIDsByUserId(userId);
            for (Integer deviceId : deviceIds) {
                count += warningInfoService.getWarningInfoByDevice(deviceId).size();
            }
        }
        return ResponseEntity.ok(count);
    }
}
