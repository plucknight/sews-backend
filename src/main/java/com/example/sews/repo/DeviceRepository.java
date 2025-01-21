package com.example.sews.repo;

import com.example.sews.dto.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
    // 通过设备ID查询
    Device findByDeviceId(Integer deviceId);
}
