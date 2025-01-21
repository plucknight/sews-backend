package com.example.sews.repo;

import com.example.sews.dto.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeatherDataRepository extends JpaRepository<WeatherData, Integer> {

    // 根据设备ID查询该设备的所有气象信息
    List<WeatherData> findByDeviceId(Integer deviceId);
}
