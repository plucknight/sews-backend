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
@Table(name = "device_maintenance")
public class DeviceMaintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "maintenance_id")
    private Integer maintenanceId;

    @Column(name = "device_id")
    private Integer deviceId;

    @Column(name = "photo_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date photoTime;

    @Column(name = "film_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date filmTime;

    @Column(name = "weather_data_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date weatherDataTime;

    @Column(name = "status", columnDefinition = "VARCHAR(50) DEFAULT 'normal'")
    private String status;

    @Column(name = "data_time", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataTime;

}
