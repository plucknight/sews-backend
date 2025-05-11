package com.example.sews.service;

import com.example.sews.dto.MonitoringPhoto;
import com.example.sews.repo.AdminDeviceModelRepository;
import com.example.sews.repo.MonitoringPhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MonitoringPhotoService {

    @Autowired
    private MonitoringPhotoRepository monitoringPhotoRepository;
    @Autowired
    private AdminDeviceModelRepository adminDeviceModelRepository;

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
     * 根据adminId查询的所有监测照片记录
     * @param adminId adminId
     * @return 该adminId的所有监测照片记录
     */
    public List<MonitoringPhoto> getMonitoringPhotosByAdminId(int adminId) {
        // 1. 获取该管理员所有关联的设备ID
        List<Integer> deviceIds = adminDeviceModelRepository.findDeviceIdsByAdminId(adminId);

        // 2. 如果没有找到设备，返回空列表
        if (deviceIds.isEmpty()) {
            return Collections.emptyList();
        }

        // 3. 查询所有设备对应的照片
        return monitoringPhotoRepository.findByDeviceIdIn(deviceIds);
    }
    public List<MonitoringPhoto> getMonitoringPhotosByDeviceIds(List<Integer>  deviceIds) {
        return monitoringPhotoRepository.findByDeviceIdIn(deviceIds);
    }
    public List<MonitoringPhoto> getMonitoringPhotosByDeviceId(Integer deviceId) {
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

    public double[] getSegmentPestAccumulationArray(int deviceId) {
        LocalDate today = LocalDate.of(2025, 4, 10); // 或 LocalDate.now()

        double[] result = new double[4];

        result[0] = getSumPests(today.minusDays(20), today.minusDays(16), deviceId); // 前20~16天
        result[1] = getSumPests(today.minusDays(15), today.minusDays(11), deviceId); // 前15~11天
        result[2] = getSumPests(today.minusDays(10), today.minusDays(6), deviceId);  // 前10~6天
        result[3] = getSumPests(today.minusDays(5), today.minusDays(1), deviceId);   // 前5~1天

        return result;
    }

    private double getSumPests(LocalDate start, LocalDate end, int deviceId) {
        Double sum = monitoringPhotoRepository.sumPestCountBetweenDates(start, end, deviceId);
        return sum != null ? sum : 0.0;
    }
}
