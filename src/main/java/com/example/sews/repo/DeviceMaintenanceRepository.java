package com.example.sews.repo;

import com.example.sews.dto.DeviceMaintenance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceMaintenanceRepository extends JpaRepository<DeviceMaintenance, Integer> {

    // 根据设备ID查询设备的维护记录
    List<DeviceMaintenance> findByDeviceId(Integer deviceId);
    // 根据设备ID和维护记录ID查询
    List<DeviceMaintenance> findByDeviceIdAndMaintenanceId(int deviceId, int maintenanceId);

}
