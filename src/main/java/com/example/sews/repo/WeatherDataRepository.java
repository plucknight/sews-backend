package com.example.sews.repo;

import com.example.sews.dto.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Integer> {

    // 根据设备ID查询该设备的所有气象信息
    List<WeatherData> findByLocationId(Integer locationId);

    @Query("SELECT AVG((w.maxTemp + w.minTemp) / 2), AVG(w.precipitation) " +
            "FROM WeatherData w WHERE w.date BETWEEN :start AND :end")
    List<Object[]> findAvgTempAndPrecipitationBetween(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);
    WeatherData findByLocationIdAndDate(Integer locationId, LocalDate date);
}
