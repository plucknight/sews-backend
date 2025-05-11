package com.example.sews.repo;

import com.example.sews.dto.WarningInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WarningInfoRepository extends JpaRepository<WarningInfo, Integer> {
    // 根据设备ID查询预警信息
    List<WarningInfo> findByDeviceId(Integer deviceId);
    List<WarningInfo> findByDeviceIdIn(List<Integer> deviceIds);
}
