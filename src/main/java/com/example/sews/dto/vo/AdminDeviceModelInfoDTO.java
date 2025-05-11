package com.example.sews.dto.vo;

        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminDeviceModelInfoDTO {

    private Integer deviceId;

    // 设备名称
    private String deviceName;

    // 设备类型
    private String trapsId;

    // 位置
    private String location;

    // 纬度
    private Double latitude;

    // 经度
    private Double longitude;

    // 作物类型
    private String cropType;

    // 管理员 ID
    private Integer adminId;

    // 管理员名称
    private String adminName;

    // 管理员电话
    private String adminPhone;

    private String modelInfo; // 模型类型
    private String modelId; // 模型类型
    // 安装时间
    private Date installTime;

    // 状态
    private String status;

    // 安装时间
    private Date dataTime;

    private String province;

    private String city;

    private String county;
}