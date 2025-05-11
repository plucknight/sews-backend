package com.example.sews.repo;

import com.example.sews.dto.Device;
import com.example.sews.dto.vo.DevicePredictionDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {
    // 通过设备ID查询
    Device findByLocationId(Integer locationId);

    Device findByDeviceId(Integer deviceId);
    @Query("SELECT new com.example.sews.dto.vo.DevicePredictionDto(d.deviceId, d.deviceName, d.location, d.latitude, d.longitude,d.deviceName,d.deviceName) " +
            "FROM Device d")
    List<DevicePredictionDto> findAllDevicesBaseInfo();
    @Query("SELECT new com.example.sews.dto.vo.DevicePredictionDto(d.deviceId, d.deviceName, d.location, d.latitude, d.longitude,d.deviceName,d.deviceName) " +
            "FROM Device d WHERE d.deviceId IN (:deviceIds)")
    List<DevicePredictionDto> findDeviceBaseInfoByDeviceIds( List<Integer> deviceIds);

//    @Query("SELECT new com.example.sews.dto.vo.AdminDeviceModelInfoDTO(" +
//            "d.deviceId, d.deviceName, d.trapsId, d.location, d.latitude, d.longitude, " +
//            "d.cropType, a.id, a.username, a.phone,m.modelName,m.modelType, d.installTime, d.status, d.dataTime, " +
//            "loc.province, loc.city, loc.county) " +  // 使用loc而不是d获取省市县
//            "FROM Device d " +
//            "LEFT JOIN AdminDeviceModel ad ON ad.deviceId = d.deviceId " +
//            "LEFT JOIN Admin a ON a.id = COALESCE(ad.adminId, 1) " +
//            "LEFT JOIN Location loc ON loc.locationId = d.locationId " +
//            "LEFT JOIN ModelInfo m ON m.modelId = ad.modelId")
//    List<AdminDeviceModelInfoDTO> findAdminDeviceInfo();

//    @Query("SELECT new com.example.sews.dto.vo.AdminDeviceModelInfoDTO(" +
//            "d.deviceId, d.deviceName, d.trapsId, d.location, d.latitude, d.longitude, " +
//            "d.cropType, a.id, a.username, a.phone, m.modelName, m.modelType, " +  // 添加 modelName 和 modelType
//            "d.installTime, d.status, d.dataTime, " +
//            "loc.province, loc.city, loc.county) " +
//            "FROM Device d " +
//            "LEFT JOIN AdminDeviceModel ad ON ad.deviceId = d.deviceId " +
//            "LEFT JOIN Admin a ON a.id = ad.adminId " +
//            "LEFT JOIN Location loc ON loc.locationId = d.locationId " +
//            "LEFT JOIN ModelInfo m ON m.modelId = ad.modelId " +  // 关联 ModelInfo 表
//            "WHERE a.id = :adminId")  // 添加 adminId 过滤条件
//    List<AdminDeviceModelInfoDTO> getDevicesByAdminId( int adminId);

}
