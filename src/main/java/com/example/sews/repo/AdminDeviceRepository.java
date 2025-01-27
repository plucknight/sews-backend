package com.example.sews.repo;

import com.example.sews.dto.AdminDevice;
import com.example.sews.dto.vo.AdminDeviceInfoDTO;
import com.example.sews.dto.vo.AdminPermissionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminDeviceRepository extends JpaRepository<AdminDevice, Long> {

    // 自定义查询：联合查询 admin、devices 和 admin_device 表，返回管理员和设备的主要信息
    @Query("SELECT new com.example.sews.dto.vo.AdminPermissionDto(a.username, a.email, d.deviceName, d.cropType, d.location ,d.dataTime) " +
            "FROM AdminDevice ad " +
            "JOIN Admin a ON a.id = ad.adminId " +
            "JOIN Device d ON d.deviceId = ad.deviceId")
    List<AdminPermissionDto> findAdminDeviceInfo();
}
