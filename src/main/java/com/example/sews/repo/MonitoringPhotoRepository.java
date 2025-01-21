package com.example.sews.repo;

import com.example.sews.dto.MonitoringPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonitoringPhotoRepository extends JpaRepository<MonitoringPhoto, Integer> {

    // 根据设备ID查询该设备的所有监测照片
    List<MonitoringPhoto> findByDeviceId(Integer deviceId);
}
