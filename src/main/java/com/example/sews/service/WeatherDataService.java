package com.example.sews.service;

import com.example.sews.dto.Location;
import com.example.sews.dto.WeatherData;
import com.example.sews.dto.vo.WeatherDataDto;
import com.example.sews.repo.LocationRepository;
import com.example.sews.repo.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class WeatherDataService {

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    LocationRepository locationRepository;
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
     * @param locationId 设备ID
     * @return 该设备的气象信息记录
     */
    public List<WeatherData> getWeatherDataByLocationId(int locationId) {
        // 根据设备ID查询该设备的所有气象信息记录
        return weatherDataRepository.findByLocationId(locationId);
    }
    public List<WeatherDataDto> getWeatherDataByCounty(String  county) {
        Location location = locationRepository.findByCounty(county);
        if (location == null) {
            return Collections.emptyList(); // 或者抛出自定义异常
        }

        // 查找该 Location 下的所有天气数据
        List<WeatherData> weatherDataList = weatherDataRepository.findByLocationId(location.getLocationId());

        // 转换为 DTO
        List<WeatherDataDto> dtoList = new ArrayList<>();
        for (WeatherData wd : weatherDataList) {
            WeatherDataDto dto = new WeatherDataDto();
            dto.setId(wd.getId());
            dto.setDate(wd.getDate());
            dto.setMaxTemp(wd.getMaxTemp());
            dto.setMinTemp(wd.getMinTemp());
            dto.setPrecipitation(wd.getPrecipitation());
            dto.setHumidity(wd.getHumidity());
            dto.setLocationId(wd.getLocationId());

            dto.setProvince(location.getProvince());
            dto.setCity(location.getCity());
            dto.setCounty(location.getCounty());

            dtoList.add(dto);
        }

        return dtoList;
    }

    /**
     * 查询所有气象信息记录
     * @return 所有气象信息记录
     */
    public List<WeatherDataDto> getAllWeatherDataWithLocation() {
        List<WeatherData> weatherDataList = weatherDataRepository.findAll();
        List<WeatherDataDto> dtoList = new ArrayList<>();

        for (WeatherData wd : weatherDataList) {
            WeatherDataDto dto = new WeatherDataDto();
            dto.setId(wd.getId());
            dto.setDate(wd.getDate());
            dto.setMaxTemp(wd.getMaxTemp());
            dto.setMinTemp(wd.getMinTemp());
            dto.setPrecipitation(wd.getPrecipitation());
            dto.setHumidity(wd.getHumidity());
            dto.setLocationId(wd.getLocationId());
            Location location=locationRepository.findById(wd.getLocationId()).get();
            dto.setProvince(location.getProvince());
            dto.setCity(location.getCity());
            dto.setCounty(location.getCounty());

            dtoList.add(dto);
        }

        return dtoList;
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

    public double[] getSegmentAveragesArray() {
        LocalDate today = LocalDate.of(2025, 4, 10);  // 或 LocalDate.now()

        double[] result = new double[8];

        // 计算平均气温
        result[0] = getAvgTemp(today.minusDays(20), today.minusDays(16)); // temp_20_16
        result[1] = getAvgTemp(today.minusDays(15), today.minusDays(11)); // temp_15_11
        result[2] = getAvgTemp(today.minusDays(10), today.minusDays(6));  // temp_10_6
        result[3] = getAvgTemp(today.minusDays(5), today.minusDays(1));   // temp_5_1

        // 计算平均降水
        result[4] = getAvgRain(today.minusDays(20), today.minusDays(16)); // rain_20_16
        result[5] = getAvgRain(today.minusDays(15), today.minusDays(11)); // rain_15_11
        result[6] = getAvgRain(today.minusDays(10), today.minusDays(6));  // rain_10_6
        result[7] = getAvgRain(today.minusDays(5), today.minusDays(1));   // rain_5_1

        return result;
    }

    private double getAvgTemp(LocalDate start, LocalDate end) {
        List<Object[]> list = weatherDataRepository.findAvgTempAndPrecipitationBetween(start, end);
        if (!list.isEmpty() && list.get(0)[0] != null) {
            return ((Number) list.get(0)[0]).doubleValue();
        }
        return 0.0;
    }

    private double getAvgRain(LocalDate start, LocalDate end) {
        List<Object[]> list = weatherDataRepository.findAvgTempAndPrecipitationBetween(start, end);
        if (!list.isEmpty() && list.get(0)[1] != null) {
            return ((Number) list.get(0)[1]).doubleValue();
        }
        return 0.0;
    }

    public void update(WeatherData existingData) {
        weatherDataRepository.save(existingData);
    }

    public void save(WeatherData data) {
        weatherDataRepository.save(data);
    }

    public WeatherData findByLocationIdAndDate(Integer locationId, LocalDate date) {
        return weatherDataRepository.findByLocationIdAndDate(locationId, date);
    }
}
