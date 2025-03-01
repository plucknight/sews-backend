package com.example.sews.repo;

import com.example.sews.dto.Device;
import com.example.sews.dto.vo.AdminDeviceInfoDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
    // 通过设备ID查询
    Device findByDeviceId(Integer deviceId);

@Query("SELECT new com.example.sews.dto.vo.AdminDeviceInfoDTO(" +
        "d.deviceId,d.deviceName, d.trapsId, d.location, d.latitude, d.longitude, " +
        "d.cropType, a.id, a.username, a.phone, d.installTime, d.status,d.dataTime" +
        ") " +
        "FROM Device d " + // 主表 devices
        "LEFT JOIN AdminDevice ad ON ad.deviceId = d.deviceId " + // 左连接 admin_device
        "LEFT JOIN Admin a ON a.id = COALESCE(ad.adminId, 1)") // 左连接 admin，使用 COALESCE 处理空值
List<AdminDeviceInfoDTO> findAdminDeviceInfo();

}
