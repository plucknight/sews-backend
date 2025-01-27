package com.example.sews.service;

import com.example.sews.repo.DeviceRepository;
import com.example.sews.dto.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;

    // 添加设备
    public Device addDevice(Device device) {
        return deviceRepository.save(device);
    }

    // 查询所有设备
    public List<Device> getAllDevices() {
        return deviceRepository.findAdminDeviceInfo();
    }

    // 根据设备ID查询设备
    public Device getDeviceById(int deviceId) {
        System.out.println(deviceId);
        Optional<Device> device = Optional.ofNullable(deviceRepository.findByDeviceId(Integer.valueOf(deviceId)));
        return device.orElse(null);
    }
}
