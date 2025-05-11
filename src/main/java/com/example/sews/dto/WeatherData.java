package com.example.sews.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "weather_data")
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "location_id", nullable = false)
    private Integer locationId;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "max_temp", nullable = false)
    private Double maxTemp;

    @Column(name = "min_temp", nullable = false)
    private Double minTemp;

    @Column(name = "precipitation", nullable = false)
    private Double precipitation;

    @Column(name = "humidity", nullable = false)
    private Double humidity;
}