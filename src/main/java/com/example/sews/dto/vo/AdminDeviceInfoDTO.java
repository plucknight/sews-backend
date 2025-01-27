package com.example.sews.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDeviceInfoDTO {

    private String username;   // 管理员用户名
    private String email;      // 管理员邮箱
    private String permissionName; // 设备名称
    private String permission; // 设备类型
    private String description;   // 设备安装地点

}
