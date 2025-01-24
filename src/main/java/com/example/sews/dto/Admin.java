package com.example.sews.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id; // 用户ID

    @Column(name = "username", unique = true, nullable = false)
    private String username; // 用户名

    @Column(name = "password", nullable = false)
    private String password; // 密码

    @Column(name = "role", nullable = false)
    private int role; // 角色

    @Column(name = "email")
    private String email; // 邮箱

    @Column(name = "phone")
    private String phone; // 电话

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt; // 创建时间

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt; // 更新时间
}
