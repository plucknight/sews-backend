package com.example.sews.repo;

import com.example.sews.dto.MonitoringPhoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
@Repository
public interface MonitoringPhotoRepository extends JpaRepository<MonitoringPhoto, Integer> {

    // 根据设备ID查询该设备的所有监测照片
    List<MonitoringPhoto> findByDeviceIdIn(List<Integer> deviceIds);
    List<MonitoringPhoto> findByDeviceId(Integer deviceId);
    List<MonitoringPhoto> findByDeviceIdInAndDataTimeBetween(List<Integer> deviceIds, Date startTime, Date endTime);

    @Query("SELECT SUM(p.pestCount) FROM MonitoringPhoto p " +
            "WHERE p.photoDate BETWEEN :startDate AND :endDate AND p.deviceId = :deviceId")
    Double sumPestCountBetweenDates(@Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate,
                                    @Param("deviceId") Integer deviceId);
}
