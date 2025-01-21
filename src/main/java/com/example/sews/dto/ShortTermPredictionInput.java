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
@Table(name = "short_term_prediction_input")
public class ShortTermPredictionInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "input_id")
    private Integer inputId;

    @Column(name = "device_id")
    private Integer deviceId;

    @Column(name = "temp_20_16_days")
    private Double temp20_16Days;

    @Column(name = "temp_15_11_days")
    private Double temp15_11Days;

    @Column(name = "temp_10_6_days")
    private Double temp10_6Days;

    @Column(name = "temp_5_1_days")
    private Double temp5_1Days;

    @Column(name = "precip_20_16_days")
    private Double precip20_16Days;

    @Column(name = "precip_15_11_days")
    private Double precip15_11Days;

    @Column(name = "precip_10_6_days")
    private Double precip10_6Days;

    @Column(name = "precip_5_1_days")
    private Double precip5_1Days;

    @Column(name = "accumulated_pest_20_16_days")
    private Double accumulatedPest20_16Days;

    @Column(name = "accumulated_pest_15_11_days")
    private Double accumulatedPest15_11Days;

    @Column(name = "accumulated_pest_10_6_days")
    private Double accumulatedPest10_6Days;

    @Column(name = "accumulated_pest_5_1_days")
    private Double accumulatedPest5_1Days;

    @Column(name = "forecast_date")
    @Temporal(TemporalType.DATE)
    private Date forecastDate;

    @Column(name = "data_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataTime;

}
