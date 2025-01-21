package com.example.sews.controller;

import com.example.sews.dto.WeatherData;
import com.example.sews.service.WeatherDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherDataController {

    @Autowired
    private WeatherDataService weatherDataService;

    // 添加气象信息
    @PostMapping("/addWeatherData")
    public ResponseEntity<WeatherData> addWeatherData(@RequestBody WeatherData weatherData) {
        WeatherData createdWeatherData = weatherDataService.addWeatherData(weatherData);
        return ResponseEntity.status(201).body(createdWeatherData);
    }

    // 查询设备的气象信息
    @GetMapping("/device/{deviceId}")
    public ResponseEntity<List<WeatherData>> getWeatherDataByDeviceId(@PathVariable("deviceId") int deviceId) {
        List<WeatherData> weatherDataList = weatherDataService.getWeatherDataByDeviceId(deviceId);
        return ResponseEntity.ok(weatherDataList);
    }

    // 查询所有气象信息
    @GetMapping("/getAllWeatherData")
    public ResponseEntity<List<WeatherData>> getAllWeatherData() {
        List<WeatherData> allWeatherData = weatherDataService.getAllWeatherData();
        return ResponseEntity.ok(allWeatherData);
    }

    // 查询指定ID的气象信息
    @GetMapping("/{weatherId}")
    public ResponseEntity<WeatherData> getWeatherDataById(@PathVariable("weatherId") int weatherId) {
        WeatherData weatherData = weatherDataService.getWeatherDataById(weatherId);
        return weatherData != null ? ResponseEntity.ok(weatherData) : ResponseEntity.notFound().build();
    }
}
