package com.example.sews.repo;

import com.example.sews.dto.ShortTermPredictionInput;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShortTermPredictionInputRepository extends JpaRepository<ShortTermPredictionInput, Integer> {
    // 可以根据需要添加查询方法，例如根据设备查询短期预测记录
    ShortTermPredictionInput findByDeviceId(Integer deviceId);
}
