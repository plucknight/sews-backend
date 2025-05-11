package com.example.sews.service;

import com.example.sews.dto.User;
import com.example.sews.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: myh
 * @CreateTime: 2025-04-01  22:19
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    public List<Integer> getDeviceIDsByUserId(Integer userId) {
        String deviceIdStr = userRepository.getDeviceIDByUserId(userId);
        if (deviceIdStr == null || deviceIdStr.isEmpty()) {
            return List.of(); // 返回空数组 []
        }
        // 按分号拆分并转换为整数列表
        return Arrays.stream(deviceIdStr.split(";"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    // 添加设备ID
    public boolean addDeviceIDForUser(Integer userId, Integer deviceId) {
        String deviceIdStr = userRepository.getDeviceIDByUserId(userId);

        if (deviceIdStr == null || deviceIdStr.isEmpty()) {
            // 如果之前没有设备ID，则直接用当前deviceId作为唯一值
            userRepository.updateDeviceID(userId, String.valueOf(deviceId));
        } else {
            // 避免重复添加
            List<String> deviceList = new ArrayList<>(Arrays.asList(deviceIdStr.split(";")));
            if (!deviceList.contains(String.valueOf(deviceId))) {
                deviceList.add(String.valueOf(deviceId));
                String newDeviceIdStr = String.join(";", deviceList);
                userRepository.updateDeviceID(userId, newDeviceIdStr);
            }
        }
        return true;
    }
    // 删除设备ID
    public boolean deleteDeviceIDForUser(Integer userId, Integer deviceId) {
        String deviceIdStr = userRepository.getDeviceIDByUserId(userId);
        if (deviceIdStr != null && !deviceIdStr.isEmpty()) {
            // 将设备ID从列表中删除
            List<Integer> deviceIds = Arrays.stream(deviceIdStr.split(";"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            deviceIds.remove(deviceId);
            // 更新数据库中的设备ID列表
            String updatedDeviceIdStr = deviceIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(";"));
            userRepository.updateDeviceID(userId, updatedDeviceIdStr);
        }
        return true;
    }

    // 修改设备ID（替换特定设备ID）
    public boolean updateDeviceIDForUser(Integer userId, List<Integer> newDeviceIds) {
        // 更新设备 ID 列表
        String deviceId =  newDeviceIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(";"));
        System.out.println("updatedDeviceIdStr = " + deviceId);
        return userRepository.updateDeviceID(userId, deviceId)>0;
    }

    public ResponseEntity<List<User>> findByAdminId(Integer adminId) {
        return ResponseEntity.ok(userRepository.findByAdminId(adminId));
    }
}
