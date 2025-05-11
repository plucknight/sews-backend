package com.example.sews.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceUpdateRequest {
    private Integer userId;
    private List<Integer> newDeviceIds;

}