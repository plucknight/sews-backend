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
@Table(name = "warning_info")
//预警信息表
public class WarningInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "warning_id")
    private Integer warningId;

    @Column(name = "device_id")
    private Integer deviceId;


    @Column(name = "warning_level")
    private String warningLevel;

    @Column(name = "warning_message")
    private String warningMessage;

    @Column(name = "publish_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date publishDate;

    @Column(name = "data_time", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataTime;

}
