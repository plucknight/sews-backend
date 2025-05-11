package com.example.sews.repo;

import com.example.sews.dto.DeviceModel;
import com.example.sews.dto.vo.AdminPermissionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceModelRepository extends JpaRepository<DeviceModel, Long> {

    @Query("SELECT new com.example.sews.dto.vo.AdminPermissionDto(a.username, a.email,  m.modelName, m.modelType,m.modelVersion,m.dataTime) " +
            "FROM DeviceModel am " +
            "JOIN Admin a ON a.id = am.deviceId " +    // 联接 Admin 表
            "JOIN ModelInfo m ON m.modelId = am.id")  // 联接 ModelInfo 表
    List<AdminPermissionDto> findDeviceModelInfo();
}
