package com.example.sews.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "peak_day_prediction_input")
public class PeakDayPredictionInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "input_id")
    private Integer inputId;


    @Column(name = "device_id")
    private Integer deviceId;



    @Column(name = "avg_temp_nov_early")
    private Double avgTempNovEarly;

    @Column(name = "total_precip_nov_early")
    private Double totalPrecipNovEarly;

    @Column(name = "total_precip_nov_mid")
    private Double totalPrecipNovMid;

    @Column(name = "avg_radiation_nov_early")
    private Double avgRadiationNovEarly;

    @Column(name = "avg_radiation_jan_early")
    private Double avgRadiationJanEarly;

    @Column(name = "forecast_date")
    @Temporal(TemporalType.DATE)
    private Date forecastDate;

    @Column(name = "data_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataTime;

}
