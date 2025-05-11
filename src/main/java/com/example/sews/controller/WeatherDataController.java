package com.example.sews.controller;

import com.example.sews.dto.WeatherData;
import com.example.sews.dto.vo.WeatherDataDto;
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
    @GetMapping("/location/{locationId}")
    public ResponseEntity<List<WeatherData>> getWeatherDataByLocationId(@PathVariable("locationId") int locationId) {
        List<WeatherData> weatherDataList = weatherDataService.getWeatherDataByLocationId(locationId);
        return ResponseEntity.ok(weatherDataList);
    }

    // 查询所有气象信息
    @GetMapping("/getAllWeatherData")
    public ResponseEntity<List<WeatherDataDto>> getAllWeatherData() {
        List<WeatherDataDto> allWeatherData = weatherDataService.getAllWeatherDataWithLocation();
        return ResponseEntity.ok(allWeatherData);
    }

    @GetMapping("/getWeatherDataByCounty/{county}")
    public ResponseEntity<List<WeatherDataDto>> getWeatherDataByCounty(@PathVariable String county) {
        List<WeatherDataDto> allWeatherData = weatherDataService.getWeatherDataByCounty(county);
        return ResponseEntity.ok(allWeatherData);
    }

    // 查询指定ID的气象信息
    @GetMapping("/{weatherId}")
    public ResponseEntity<WeatherData> getWeatherDataById(@PathVariable("weatherId") int weatherId) {
        WeatherData weatherData = weatherDataService.getWeatherDataById(weatherId);
        return weatherData != null ? ResponseEntity.ok(weatherData) : ResponseEntity.notFound().build();
    }

    @GetMapping("/array")
    public ResponseEntity<double[]> getArray() {
        return ResponseEntity.ok(weatherDataService.getSegmentAveragesArray());
    }
}
