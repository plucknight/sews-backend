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
@Table(name = "peak_day_prediction_input")
public class PeakDayPredictionInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "input_id")
    private Integer inputId;

    @Column(name = "model_id")
    private Integer modelId;

    @Column(name = "input1")
    private Double input1;

    @Column(name = "input2")
    private Double input2;

    @Column(name = "input3")
    private Double input3;

    @Column(name = "input4")
    private Double input4;

    @Column(name = "input5")
    private Double input5;

    @Column(name = "forecast_date")
    @Temporal(TemporalType.DATE)
    private LocalDate forecastDate;

    @Column(name = "data_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataTime;

}
