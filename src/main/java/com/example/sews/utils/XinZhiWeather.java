package com.example.sews.utils;

import com.example.sews.dto.WeatherData;
import com.mathworks.toolbox.javabuilder.external.org.json.JSONArray;
import com.mathworks.toolbox.javabuilder.external.org.json.JSONException;
import com.mathworks.toolbox.javabuilder.external.org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SignatureException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class XinZhiWeather {

    private String TIANQI_DAILY_WEATHER_URL = "https://api.seniverse.com/v3/weather/daily.json";
    @Value("${xinzhi.api.secret}")
    private String TIANQI_API_SECRET_KEY ; //

    @Value("${xinzhi.api.userid}")
    private String TIANQI_API_USER_ID ; //

    /**
     * Generate HmacSHA1 signature with given data string and key
     * @param data
     * @param key
     * @return
     * @throws SignatureException
     */
    private String generateSignature(String data, String key) throws SignatureException {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(rawHmac); // 使用标准库
        } catch (Exception e) {
            throw new SignatureException("HMAC生成失败: " + e.getMessage());
        }
    }
    /**
     * Generate the URL to get diary weather
     * @param location
     * @param language
     * @param unit
     * @param start
     * @param days
     * @return
     */
    public String generateGetDiaryWeatherURL(
            String location,
            String language,
            String unit,
            String start,
            String days
    )  throws SignatureException, UnsupportedEncodingException {
        String timestamp = String.valueOf(new Date().getTime());
        String params = "ts=" + timestamp + "&ttl=1800&uid=" + TIANQI_API_USER_ID;
        String signature = URLEncoder.encode(generateSignature(params, TIANQI_API_SECRET_KEY), "UTF-8");
        return TIANQI_DAILY_WEATHER_URL + "?" + params + "&sig=" + signature + "&location=" + location + "&language=" + language + "&unit=" + unit + "&start=" + start + "&days=" + days;
    }
    public String sendGetRequest(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) { // 200
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } else {
            throw new Exception("GET请求失败，响应码：" + responseCode);
        }
    }
    private List<WeatherData> parseWeatherData(String jsonStr, String city) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonStr);
        JSONArray results = jsonObject.getJSONArray("results");
        JSONObject result = results.getJSONObject(0);
        JSONArray dailyArray = result.getJSONArray("daily");

        List<WeatherData> weatherDataList = new ArrayList<>();

        for (int i = 0; i < dailyArray.length(); i++) {
            JSONObject daily = dailyArray.getJSONObject(i);
            WeatherData weatherData = new WeatherData();

            weatherData.setLocationId(1); // Example: You can set this to an actual location ID if necessary
            weatherData.setDate(LocalDate.parse(daily.getString("date")));
            weatherData.setMaxTemp(Double.parseDouble(daily.getString("high")));
            weatherData.setMinTemp(Double.parseDouble(daily.getString("low")));
            weatherData.setPrecipitation(Double.parseDouble(daily.getString("rainfall")));
            weatherData.setHumidity(Double.parseDouble(daily.getString("humidity")));

            weatherDataList.add(weatherData);
        }

        return weatherDataList;
    }
    public List<WeatherData> requestWeatherForCities(List<String> cities) {
        List<WeatherData> weatherDataList = new ArrayList<>();
        try{
            for (String city : cities) {
                String url = generateGetDiaryWeatherURL(city, "zh-Hans", "c", "1", "15");
                String jsonResponse = sendGetRequest(url); // 发送 GET 请求获取 JSON 数据
                List<WeatherData> cityWeatherData = parseWeatherData(jsonResponse, city);
                weatherDataList.addAll(cityWeatherData);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        return weatherDataList;
    }
//    public static void main(String args[]){
//        XinZhiWeather demo = new XinZhiWeather();
//        try {
//            String url = demo.generateGetDiaryWeatherURL(
//                    "宝鸡",
//                    "zh-Hans",
//                    "c",//摄氏度
//                    "1",//开始日期
//                    "15"//请求天数
//            );
////            System.out.println("URL:" + url);
//
//            // 发送GET请求，拿到返回的JSON
//            String jsonResponse = demo.sendGetRequest(url);
//            JSONObject jsonObject = new JSONObject(jsonResponse);
//            JSONArray results = jsonObject.getJSONArray("results");
//
//            JSONObject result = results.getJSONObject(0); // 只取第一个城市
//            JSONObject location = result.getJSONObject("location");
//            JSONArray daily = result.getJSONArray("daily");
//
//            System.out.println("城市名称: " + location.getString("name"));
//
//            for (int i = 0; i < daily.length(); i++) {
//                JSONObject dayWeather = daily.getJSONObject(i);
//                System.out.println("日期: " + dayWeather.getString("date"));
//                System.out.println("最高温度: " + dayWeather.getString("high"));
//                System.out.println("最低温度: " + dayWeather.getString("low"));
//                System.out.println("降水量: " + dayWeather.getString("rainfall"));
//                System.out.println("湿度: " + dayWeather.getString("humidity"));
//            }
////            System.out.println("JSON数据: " + jsonResponse);
//        } catch (Exception e) {
//            System.out.println("Exception:" + e);
//        }

//    }
}