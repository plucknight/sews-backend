package com.example.sews.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "operate_log")
public class OperateLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id; // 日志ID

    @Column(name = "admin_id")
    private Integer adminId; // 操作员ID，外键，指向`admin`表

    @Column(name = "action", nullable = false)
    private String action; // 操作

    @Column(name = "description")
    private String description; // 操作描述

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 操作时间
}
