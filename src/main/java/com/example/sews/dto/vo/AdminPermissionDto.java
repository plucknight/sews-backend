package com.example.sews.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: myh
 * @CreateTime: 2025-01-24  23:49
 * @Description:
 * @Version: 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminPermissionDto {

        private String username;   // 管理员用户名
        private String email;      // 管理员邮箱
        private String permissionName; // 权限名称
        private String permissionType; // 权限类型
        private String description;   // 描述
        private Date dateTime; // 时间

}
