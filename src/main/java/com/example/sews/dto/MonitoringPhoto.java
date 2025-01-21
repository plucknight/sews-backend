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
@Table(name = "monitoring_photos")
public class MonitoringPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_id")
    private Integer photoId;

    @Column(name = "device_id")
    private Integer deviceId;

    @Column(name = "photo_date")
    @Temporal(TemporalType.DATE)
    private Date photoDate;

    @Column(name = "pest_count")
    private Integer pestCount;

    @Column(name = "pest_type")
    private String pestType;

    @Column(name = "photo_path", nullable = false)
    private String photoPath;

    @Column(name = "data_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataTime;

}
