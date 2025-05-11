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
@Table(name = "devices")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deviceId")
    private Integer deviceId;

    @Column(name = "location_id", nullable = false)
    private Integer locationId;

    @Column(name = "device_name", nullable = false)
    private String deviceName;

    @Column(name = "traps_id", nullable = false)
    private String trapsId;

    @Column(name = "location", nullable = false)
    private String location;

    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "crop_type", nullable = false)
    private String cropType;

    @Column(name = "install_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date installTime;

    @Column(name = "status")
    private String status;

    @Column(name = "data_time", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataTime;

}
