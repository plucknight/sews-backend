package com.example.sews.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceModelInfoDTO {

    private String username;   // 管理员用户名
    private String email;      // 管理员邮箱
    private String modelName;  // 模型名称
    private String modelType;  // 模型类型
    private String modelVersion; // 模型版本

}
