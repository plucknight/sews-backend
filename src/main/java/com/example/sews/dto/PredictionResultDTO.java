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
@Table(name = "prediction_results") // 映射到 prediction_results 表
public class PredictionResultDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "forecast_id")
    private Integer forecastId;  // 预测结果编号

    @Column(name = "device_id")
    private Integer deviceId;    // 设备编号

    @Column(name = "model_id")
    private Integer modelId;     // 模型编号

    @Column(name = "start_date")
    private Date startDate;      // 预测开始日期

    @Column(name = "end_date")
    private Date endDate;        // 预测结束日期（高峰日BP预测模型为NULL）

    @Column(name = "forecast_value")
    private String forecastValue; // 预测结果（例如："50虫口/板"）

    @Column(name = "data_time")
    private Date dataTime;       // 数据记录时间
}
