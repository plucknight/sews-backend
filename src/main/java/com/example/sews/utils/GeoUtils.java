package com.example.sews.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具类：通过经纬度查询省市区
 */
//@Component
public class GeoUtils {

//    @Value("${amap.api.key}")
    private String apiKey="";

    private static final String AMAP_REVERSE_GEOCODE_URL = "https://restapi.amap.com/v3/geocode/regeo";

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 根据经纬度查询省市区信息
     * @param longitude 经度
     * @param latitude 纬度
     * @return 地址信息（格式："省 市 区"），如果查询失败返回 null
     */
    public List<String> getProvinceCityDistrict(double longitude, double latitude) {
        try {
            String url = String.format("%s?key=%s&location=%s,%s", AMAP_REVERSE_GEOCODE_URL, apiKey, longitude, latitude);
            String response = restTemplate.getForObject(url, String.class);

            JsonNode root = objectMapper.readTree(response);
            if (root.path("status").asText().equals("1")) {
                JsonNode addressComponent = root.path("regeocode").path("addressComponent");
                String province = addressComponent.path("province").asText("");
                String city = addressComponent.path("city").isMissingNode() || addressComponent.path("city").asText().isEmpty()
                        ? province // 直辖市没有 city，用 province 填充
                        : addressComponent.path("city").asText("");
                String district = addressComponent.path("district").asText("");
                return new ArrayList<>() {{
                    add(province);
                    add(city);
                    add(district);
                }};
            } else {
                System.err.println("高德API调用失败：" + root.path("info").asText());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        GeoUtils geoUtils = new GeoUtils();
        System.out.println(geoUtils.getProvinceCityDistrict(80.397477, 39.908692));
    }
}
