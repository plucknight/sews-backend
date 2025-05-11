package com.example.sews.service;

import com.example.sews.dto.AdminDeviceModel;
import com.example.sews.dto.vo.AdminPermissionDto;
import com.example.sews.repo.AdminDeviceModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminDeviceModelService {

    @Autowired
    private AdminDeviceModelRepository adminDeviceModelRepository;

    // 获取管理员和设备的联合信息
    public List<AdminPermissionDto> getAdminDeviceInfo() {
        return adminDeviceModelRepository.findAdminDeviceInfo();
    }

    public void deleteAdminDevice(Long id) {
        adminDeviceModelRepository.deleteById(id);
    }
    public void addAdminDevice(AdminDeviceModel adminDevice) {
        adminDeviceModelRepository.save(adminDevice);
    }
    public Integer getDevicesCount(Integer adminId) {
        return adminDeviceModelRepository.findDeviceIdsByAdminId(adminId).size();
    }
}
