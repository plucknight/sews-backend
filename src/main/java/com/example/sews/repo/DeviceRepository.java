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
//    @Query("SELECT new com.example.sews.dto.Device(d.deviceId, d.deviceName, d.trapsId, d.location, " +
//            "d.latitude, d.longitude, d.cropType, a.username, a.id, a.phone, d.installTime, d.status, d.dataTime) " +
//            "FROM AdminDevice ad " +
//            "JOIN Device d ON d.deviceId = ad.deviceId " +  // 联接设备表
//            "JOIN Admin a ON a.id = ad.adminId")  // 联接管理员表
//    List<Device> findAdminDeviceInfo();
@Query("SELECT new com.example.sews.dto.Device(d.deviceId, d.deviceName, d.trapsId, d.location, " +
        "d.latitude, d.longitude, d.cropType, " +
        "COALESCE(a.username, '默认管理员'), " +
        "COALESCE(a.id, 1) , " + // 如果没有对应的管理员，默认绑定adminId=1
        "COALESCE(a.phone, '无电话') , " +
        "d.installTime, d.status, d.dataTime) " +
        "FROM Device d " +
        "LEFT JOIN AdminDevice ad ON ad.deviceId = d.deviceId " +  // 使用LEFT JOIN，确保所有设备都会返回
        "LEFT JOIN Admin a ON a.id = COALESCE(ad.adminId, 1)")  // 如果AdminDevice没有记录，绑定默认adminId=1
List<Device> findAdminDeviceInfo();

}
