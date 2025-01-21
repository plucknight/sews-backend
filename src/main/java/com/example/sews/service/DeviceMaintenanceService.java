package com.example.sews.service;

import com.example.sews.dto.DeviceMaintenance;
import com.example.sews.repo.DeviceMaintenanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceMaintenanceService {

    @Autowired
    private DeviceMaintenanceRepository deviceMaintenanceRepository;

    /**
     * 添加设备维护记录
     * @param deviceMaintenance 设备维护记录
     * @return 创建的设备维护记录
     */
    public DeviceMaintenance addDeviceMaintenance(DeviceMaintenance deviceMaintenance) {
        // 保存设备维护记录到数据库
        return deviceMaintenanceRepository.save(deviceMaintenance);
    }

    /**
     * 查询设备的所有维护记录
     * @param deviceId 设备ID
     * @return 设备的维护记录列表
     */
    public List<DeviceMaintenance> getDeviceMaintenanceRecordsByDeviceId(int deviceId) {
        // 根据设备ID查询该设备的所有维护记录
        return deviceMaintenanceRepository.findByDeviceId(deviceId);
    }

    /**
     * 根据维护记录ID查询设备维护记录
     * @param maintenanceId 维护记录ID
     * @return 设备维护记录
     */
    public DeviceMaintenance getDeviceMaintenanceById(int maintenanceId) {
        // 查找并返回维护记录
        Optional<DeviceMaintenance> record = deviceMaintenanceRepository.findById(maintenanceId);
        return record.orElse(null);
    }

    /**
     * 查询所有设备的维护记录
     * @return 所有设备的维护记录列表
     */
    public List<DeviceMaintenance> getAllDeviceMaintenanceRecords() {
        // 查询并返回所有设备的维护记录
        return deviceMaintenanceRepository.findAll();
    }

    // 根据设备ID和维护记录ID查询
    public List<DeviceMaintenance> getDeviceMaintenanceByDeviceIdAndMaintenanceId(int deviceId, int maintenanceId) {
        return deviceMaintenanceRepository.findByDeviceIdAndMaintenanceId(deviceId, maintenanceId);
         // 如果没有找到记录，返回null
    }
}
