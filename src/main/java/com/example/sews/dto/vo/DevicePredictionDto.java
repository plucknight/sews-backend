package com.example.sews.dto.vo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: myh
 * @CreateTime: 2025-02-19  18:47
 * @Description: TODO
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevicePredictionDto {
    private Integer deviceId;
    private String deviceName;
    private String location;
    private Double latitude;
    private Double longitude;
    private String peekDayForecastValue;
    private String shortTermForecastValue;
    private String suggestion;
}
