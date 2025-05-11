package com.example.sews.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pest_record")
public class PestRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "device_id", length = 50, nullable = false)
    private String deviceId;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "pest_type", length = 50, nullable = false)
    private String pestType;

    @Column(name = "actual_count")
    private Integer actualCount;

    @Column(name = "predicted_count")
    private Integer predictedCount;
}