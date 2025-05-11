package com.example.sews.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @Author: myh
 * @CreateTime: 2025-04-29  15:29
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherDataDto {
    private Integer id;
    private LocalDate date;
    private Double maxTemp;
    private Double minTemp;
    private Double precipitation;
    private Double humidity;

    private Integer locationId;
    private String province;
    private String city;
    private String county;
}
