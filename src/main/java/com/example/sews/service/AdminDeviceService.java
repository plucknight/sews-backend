package com.example.sews.service;

import com.example.sews.dto.vo.AdminDeviceInfoDTO;
import com.example.sews.repo.AdminDeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminDeviceService {

    @Autowired
    private AdminDeviceRepository adminDeviceRepository;

    // 获取管理员和设备的联合信息
    public List<AdminDeviceInfoDTO> getAdminDeviceInfo() {
        return adminDeviceRepository.findAdminDeviceInfo();
    }
}
