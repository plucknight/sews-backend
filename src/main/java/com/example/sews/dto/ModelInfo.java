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
@Table(name = "model_info")
public class ModelInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "model_id")
    private Integer modelId;  // 模型编号

    @Column(name = "model_name", nullable = false)
    private String modelName;  // 模型名称

    @Column(name = "model_type", nullable = false)
    private String modelType;  // 模型类型

    @Column(name = "model_version")
    private String modelVersion;  // 模型版本

    @Column(name = "model_file_path")
    private String modelFilePath;  // 模型文件路径


    @Column(name = "data_time", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataTime;  // 数据记录时间
}
