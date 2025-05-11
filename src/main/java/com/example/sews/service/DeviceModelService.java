package com.example.sews.service;

import com.example.sews.dto.vo.AdminPermissionDto;
import com.example.sews.repo.DeviceModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeviceModelService {

    @Autowired
    private DeviceModelRepository DeviceModelRepository;

    // 获取管理员和模型的联合信息
    public List<AdminPermissionDto> getDeviceModelInfo() {
        return DeviceModelRepository.findDeviceModelInfo();
    }
}
