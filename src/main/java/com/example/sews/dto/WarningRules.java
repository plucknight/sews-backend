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
@Table(name = "warning_rules")
//预警规则
public class WarningRules {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rule_id")
    private Integer ruleId;  // 规则编号

    @Column(name = "rule_name", nullable = false)
    private String ruleName;  // 规则名称

    @Column(name = "device_type")
    private String deviceType;  // 适用设备类型

    @Column(name = "warning_level")
    private String warningLevel;  // 预警级别

    @Column(name = "threshold")
    private Double threshold;  // 阈值

    @Column(name = "description")
    private String description;  // 规则描述

    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;  // 是否启用该规则

    @Column(name = "create_time", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;  // 规则创建时间

    @Column(name = "update_time", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;  // 规则更新时间
}
