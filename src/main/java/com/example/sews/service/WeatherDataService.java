package com.example.sews.service;

import com.example.sews.dto.WeatherData;
import com.example.sews.repo.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeatherDataService {

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    /**
     * 添加新的气象信息记录
     * @param weatherData 气象信息
     * @return 创建的气象信息记录
     */
    public WeatherData addWeatherData(WeatherData weatherData) {
        // 保存气象信息记录到数据库
        return weatherDataRepository.save(weatherData);
    }

    /**
     * 根据设备ID查询该设备的气象信息记录
     * @param deviceId 设备ID
     * @return 该设备的气象信息记录
     */
    public List<WeatherData> getWeatherDataByDeviceId(int deviceId) {
        // 根据设备ID查询该设备的所有气象信息记录
        return weatherDataRepository.findByDeviceId(deviceId);
    }

    /**
     * 查询所有气象信息记录
     * @return 所有气象信息记录
     */
    public List<WeatherData> getAllWeatherData() {
        // 查询并返回所有气象信息记录
        return weatherDataRepository.findAll();
    }

    /**
     * 根据气象信息ID查询气象信息
     * @param weatherId 气象信息ID
     * @return 气象信息记录
     */
    public WeatherData getWeatherDataById(int weatherId) {
        // 根据ID查询气象信息
        Optional<WeatherData> weatherData = weatherDataRepository.findById(weatherId);
        return weatherData.orElse(null);
    }
}
