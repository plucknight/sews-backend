package com.example.sews.service;

import com.example.sews.dto.*;
import com.example.sews.dto.vo.AdminDeviceModelInfoDTO;
import com.example.sews.repo.*;
import com.example.sews.utils.GeoUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    @Autowired
    private DeviceRepository deviceRepository;
    private final ObjectMapper objectMapper = new ObjectMapper(); // 用于转化为 JSON
    @Autowired
    AdminDeviceModelRepository adminDeviceModelRepository;

    @Autowired
    ModelInfoRepository modelInfoRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    AdminRepository adminRepository;

    public List<Integer> getDevicesIds() {
        return deviceRepository.findAll().stream()
                .map(Device::getDeviceId)
                .collect(Collectors.toList());
    }


    public List<AdminDeviceModelInfoDTO> getDevicesByAdminId(int adminId) {
        // 获取管理员关联的设备模型
        List<AdminDeviceModel> adminDeviceModels = adminDeviceModelRepository.findByAdminId(adminId);

        List<AdminDeviceModelInfoDTO> result = new ArrayList<>();

        for (AdminDeviceModel adm : adminDeviceModels) {
            String modelIdsStr = adm.getModelId();  // 例如 "1;2;3"
            List<Integer> modelIds = Arrays.stream(modelIdsStr.split(";"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            // 获取所有模型信息
            List<ModelInfo> modelInfos = modelInfoRepository.findByModelIdIn(modelIds);

            // 将模型信息封装成 JSON 格式字符串
            List<Map<String, Object>> modelInfoList = new ArrayList<>();
            for (ModelInfo model : modelInfos) {
                Map<String, Object> modelInfoMap = new HashMap<>();
                modelInfoMap.put("modelId", model.getModelId());
                modelInfoMap.put("modelName", model.getModelName());
                modelInfoMap.put("modelType", model.getModelType());
                modelInfoList.add(modelInfoMap);
            }

            try {
                // 将模型信息转化为 JSON 字符串
                String modelInfoJson = objectMapper.writeValueAsString(modelInfoList);

                // 查询设备的 Location 信息
                Optional<Device> deviceOptional = deviceRepository.findById(adm.getDeviceId());
                if (deviceOptional.isPresent()) {
                    Device device = deviceOptional.get();
                    Location location = locationRepository.findById(device.getLocationId()).orElse(null);

                    // 创建 DTO 并填充所有字段
                    AdminDeviceModelInfoDTO dto = new AdminDeviceModelInfoDTO();
                    dto.setDeviceId(device.getDeviceId());
                    dto.setDeviceName(device.getDeviceName());
                    dto.setTrapsId(device.getTrapsId());
                    dto.setLocation(device.getLocation());
                    dto.setLatitude(device.getLatitude());
                    dto.setLongitude(device.getLongitude());
                    dto.setCropType(device.getCropType());
                    dto.setAdminId(adm.getAdminId());
                    dto.setModelInfo(modelInfoJson);  // 将模型信息 JSON 字符串放入 modelInfo
                    dto.setInstallTime(device.getInstallTime());
                    dto.setStatus(device.getStatus());
                    dto.setDataTime(device.getDataTime());

                    // 设置 Location 信息（省、市、县）
                    if (location != null) {
                        dto.setProvince(location.getProvince());
                        dto.setCity(location.getCity());
                        dto.setCounty(location.getCounty());
                    }

                    // 设置管理员信息（假设从 AdminDeviceModel 中获取）
                    Optional<Admin> adminOptional = adminRepository.findById(adm.getAdminId());
                    if (adminOptional.isPresent()) {
                        Admin admin = adminOptional.get();
                        dto.setAdminName(admin.getUsername());
                        dto.setAdminPhone(admin.getPhone());
                    }

                    result.add(dto);
                }

            } catch (Exception e) {
                e.printStackTrace();  // 处理 JSON 转换异常，可以记录日志或抛出业务异常
            }
        }

        return result;
    }

    public AdminDeviceModelInfoDTO getDeviceByDeviceId(int deviceId) {
        // 1️⃣ 查询设备关联的 AdminDeviceModel 记录
        AdminDeviceModel adm = adminDeviceModelRepository.findByDeviceId(deviceId);
        System.out.println("adminDeviceModels = " + adm);
        AdminDeviceModelInfoDTO result = new AdminDeviceModelInfoDTO();


        // 2️⃣ 获取 modelId
        String modelIdsStr = adm.getModelId();  // 例如 "1;2;3"
        List<Integer> modelIds = Arrays.stream(modelIdsStr.split(";"))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        // 3️⃣ 查询所有模型信息
        List<ModelInfo> modelInfos = modelInfoRepository.findByModelIdIn(modelIds);

        // 4️⃣ 将模型信息转换为 JSON
        List<Map<String, Object>> modelInfoList = new ArrayList<>();
        for (ModelInfo model : modelInfos) {
            Map<String, Object> modelInfoMap = new HashMap<>();
            modelInfoMap.put("modelId", model.getModelId());
            modelInfoMap.put("modelName", model.getModelName());
            modelInfoMap.put("modelType", model.getModelType());
            modelInfoList.add(modelInfoMap);
        }

        String modelInfoJson = "";
        try {
            modelInfoJson = objectMapper.writeValueAsString(modelInfoList);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // 5️⃣ 查询设备信息
        Optional<Device> deviceOptional = deviceRepository.findById(adm.getDeviceId());
        AdminDeviceModelInfoDTO dto = new AdminDeviceModelInfoDTO();
        if (deviceOptional.isPresent()) {
            Device device = deviceOptional.get();
            Location location = locationRepository.findById(device.getLocationId()).orElse(null);

            // 6️⃣ 创建 DTO 并填充所有字段
            dto.setDeviceId(device.getDeviceId());
            dto.setDeviceName(device.getDeviceName());
            dto.setTrapsId(device.getTrapsId());
            dto.setLocation(device.getLocation());
            dto.setLatitude(device.getLatitude());
            dto.setLongitude(device.getLongitude());
            dto.setCropType(device.getCropType());
            dto.setAdminId(adm.getAdminId());
            dto.setModelInfo(modelInfoJson); // JSON 格式的模型信息
            dto.setInstallTime(device.getInstallTime());
            dto.setStatus(device.getStatus());
            dto.setDataTime(device.getDataTime());

            // 设置 Location 信息
            if (location != null) {
                dto.setProvince(location.getProvince());
                dto.setCity(location.getCity());
                dto.setCounty(location.getCounty());
            }

            // 7️⃣ 设置管理员信息
            Optional<Admin> adminOptional = adminRepository.findById(adm.getAdminId());
            if (adminOptional.isPresent()) {
                Admin admin = adminOptional.get();
                dto.setAdminName(admin.getUsername());
                dto.setAdminPhone(admin.getPhone());
            }

        }

        System.out.println("result = " + dto);
        return dto;
    }


    public Device addDevice(Device device) {
        try {
            // 获取省市区信息
            GeoUtils geoUtils = new GeoUtils();
            List<String> provinceCityDistrict = geoUtils.getProvinceCityDistrict(device.getLongitude(), device.getLatitude());

            Location location = null;
            // 尝试根据县区查找 Location
            Optional<Location> existingLocation = locationRepository.findByProvinceAndCityAndCounty(
                    provinceCityDistrict.get(0),
                    provinceCityDistrict.get(1),
                    provinceCityDistrict.get(2)
            );

            if (existingLocation.isPresent()) {
                // 已经存在，就直接用查询到的
                location = existingLocation.get();
            } else {
                // 不存在，就新建
                location = new Location();
                location.setProvince(provinceCityDistrict.get(0));
                location.setCity(provinceCityDistrict.get(1));
                location.setCounty(provinceCityDistrict.get(2));

                // 保存新位置
                location = locationRepository.save(location);
            }
            Integer locationId = location.getLocationId();

            device.setLocationId(locationId);
            return deviceRepository.save(device);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 查询所有设备
    public List<AdminDeviceModelInfoDTO> getAllDevices() {
        // 获取所有设备模型关联数据
        List<AdminDeviceModel> adminDeviceModels = adminDeviceModelRepository.findAll();
        List<AdminDeviceModelInfoDTO> result = new ArrayList<>();

        for (AdminDeviceModel adm : adminDeviceModels) {
            String modelIdsStr = adm.getModelId();  // 例如 "1;2;3"
            List<Integer> modelIds = Arrays.stream(modelIdsStr.split(";"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());

            // 获取所有模型信息
            List<ModelInfo> modelInfos = modelInfoRepository.findByModelIdIn(modelIds);

            // 将模型信息封装成 JSON 格式字符串
            List<Map<String, Object>> modelInfoList = new ArrayList<>();
            for (ModelInfo model : modelInfos) {
                Map<String, Object> modelInfoMap = new HashMap<>();
                modelInfoMap.put("modelId", model.getModelId());
                modelInfoMap.put("modelName", model.getModelName());
                modelInfoMap.put("modelType", model.getModelType());
                modelInfoList.add(modelInfoMap);
            }

            try {
                // 将模型信息转化为 JSON 字符串
                String modelInfoJson = objectMapper.writeValueAsString(modelInfoList);

                // 查询设备的 Location 信息
                Optional<Device> deviceOptional = deviceRepository.findById(adm.getDeviceId());
                if (deviceOptional.isPresent()) {
                    Device device = deviceOptional.get();
                    Location location = locationRepository.findById(device.getLocationId()).orElse(null);

                    // 创建 DTO 并填充所有字段
                    AdminDeviceModelInfoDTO dto = new AdminDeviceModelInfoDTO();
                    dto.setDeviceId(device.getDeviceId());
                    dto.setDeviceName(device.getDeviceName());
                    dto.setTrapsId(device.getTrapsId());
                    dto.setLocation(device.getLocation());
                    dto.setLatitude(device.getLatitude());
                    dto.setLongitude(device.getLongitude());
                    dto.setCropType(device.getCropType());
                    dto.setModelInfo(modelInfoJson);  // 将模型信息 JSON 字符串放入 modelInfo
                    dto.setInstallTime(device.getInstallTime());
                    dto.setStatus(device.getStatus());
                    dto.setDataTime(device.getDataTime());

                    // 设置 Location 信息（省、市、县）
                    if (location != null) {
                        dto.setProvince(location.getProvince());
                        dto.setCity(location.getCity());
                        dto.setCounty(location.getCounty());
                    }

                    // 设置管理员信息（假设从 AdminDeviceModel 中获取）
                    Optional<Admin> adminOptional = adminRepository.findById(adm.getAdminId());
                    if (adminOptional.isPresent()) {
                        Admin admin = adminOptional.get();
                        dto.setAdminName(admin.getUsername());
                        dto.setAdminPhone(admin.getPhone());
                    }

                    result.add(dto);
                }

            } catch (Exception e) {
                e.printStackTrace();  // 处理 JSON 转换异常，可以记录日志或抛出业务异常
            }
        }

        return result;
    }

    // 根据设备ID查询设备
    public Device getDeviceById(int deviceId) {
        Optional<Device> device = Optional.ofNullable(deviceRepository.findByDeviceId(deviceId));
        return device.orElse(null);
    }

    public void deleteDeviceById(int deviceId) {
        deviceRepository.deleteById(Integer.valueOf(deviceId));
    }
}
