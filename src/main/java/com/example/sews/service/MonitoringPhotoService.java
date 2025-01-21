package com.example.sews.service;

import com.example.sews.dto.MonitoringPhoto;
import com.example.sews.repo.MonitoringPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MonitoringPhotoService {

    @Autowired
    private MonitoringPhotoRepository monitoringPhotoRepository;

    /**
     * 添加监测照片记录
     * @param monitoringPhoto 监测照片
     * @return 创建的监测照片记录
     */
    public MonitoringPhoto addMonitoringPhoto(MonitoringPhoto monitoringPhoto) {
        // 保存监测照片记录到数据库
        return monitoringPhotoRepository.save(monitoringPhoto);
    }

    /**
     * 根据设备ID查询该设备的所有监测照片记录
     * @param deviceId 设备ID
     * @return 该设备的所有监测照片记录
     */
    public List<MonitoringPhoto> getMonitoringPhotosByDeviceId(int deviceId) {
        // 根据设备ID查询该设备的所有监测照片记录
        return monitoringPhotoRepository.findByDeviceId(deviceId);
    }

    /**
     * 查询所有监测照片记录
     * @return 所有监测照片记录
     */
    public List<MonitoringPhoto> getAllMonitoringPhotos() {
        // 查询并返回所有监测照片记录
        return monitoringPhotoRepository.findAll();
    }

    /**
     * 根据监测照片ID查询监测照片
     * @param photoId 监测照片ID
     * @return 监测照片记录
     */
    public MonitoringPhoto getMonitoringPhotoById(int photoId) {
        // 根据ID查询监测照片
        Optional<MonitoringPhoto> monitoringPhoto = monitoringPhotoRepository.findById(photoId);
        return monitoringPhoto.orElse(null);
    }
}
