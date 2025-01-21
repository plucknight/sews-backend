package com.example.sews.repo;

import com.example.sews.dto.PeakDayPredictionInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PeakDayPredictionInputRepository extends JpaRepository<PeakDayPredictionInput, Integer> {
    // 可以根据需要添加查询方法，例如根据设备查询高峰日预测记录
    PeakDayPredictionInput findByDeviceId(Integer deviceId);
}
