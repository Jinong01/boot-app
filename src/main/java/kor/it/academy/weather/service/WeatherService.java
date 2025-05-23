package kor.it.academy.weather.service;

import kor.it.academy.common.utils.APIConnUtils;
import kor.it.academy.common.vo.APIResponse;
import kor.it.academy.common.vo.CountryEnum;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final APIConnUtils apiConnUtils;

    @Value("${api.weather.key}")
    private String weatherAPIKey;
    private String goCodeUrl =  "http://api.openweathermap.org/geo/1.0/direct";
    private String weatherUrl = "https://api.openweathermap.org/data/2.5/weather";

    public Map<String , Object> getWeather(String city) throws Exception{
        Map<String , Object> resultMap = new HashMap<>();

        Map<String, Object> geoMap = this.getGeoData(city);
        double lat = (double) geoMap.get("lat");
        double lon = (double) geoMap.get("lon");

        Map<String ,Object> dataParam = new HashMap<>();
        dataParam.put("lat", lat);
        dataParam.put("lon", lon);
        dataParam.put("units", "metric");
        dataParam.put("lang","kr");
        dataParam.put("appid", weatherAPIKey);


        APIResponse response = apiConnUtils.toGetAPI(weatherUrl, dataParam);
        JSONObject jsonObj = new JSONObject(response.getData());
        JSONObject weatherObj = jsonObj.getJSONArray("weather").getJSONObject(0);

        //날씨 정보
        String description = weatherObj.getString("description");
        //날씨 아이콘
        String icon = weatherObj.getString("icon");
        //온도 모음 객체
        JSONObject mainObj = jsonObj.getJSONObject("main");
        //온도 가져오기
        BigDecimal temp = mainObj.getBigDecimal("temp");

        resultMap.put("temp", temp);
        resultMap.put("icon", String.format("https://openweathermap.org/img/wn/%s.png", icon));
        resultMap.put("condition", description);
        resultMap.put("cityName", geoMap.get("cityName"));
        resultMap.put("country", geoMap.get("country"));

        return resultMap;
    }

    private Map<String , Object> getGeoData(String city) throws Exception{
        Map<String , Object> geoMap = new HashMap<>();

        Map<String , Object> dataParam = new HashMap<>();
        dataParam.put("q", city);
        dataParam.put("appid", weatherAPIKey);
        dataParam.put("units", "metric");

        APIResponse response = apiConnUtils.toGetAPI(goCodeUrl, dataParam);

        if (response != null) {
            JSONObject jsonObj = new JSONArray(response.getData()).getJSONObject(0);

            String cityName = jsonObj.getJSONObject("local_names").getString("ko");
            String countryCode = jsonObj.getString("country");

            //BigDecimal은 자바에서 실수를 가장 정확하게 표현하는 것, 위도 경도는 소수점 아래로 길게 나오기 때문에 정확한 BigDecimal로 받는다
            geoMap.put("lat", jsonObj.getBigDecimal("lat").doubleValue());
            geoMap.put("lon", jsonObj.getBigDecimal("lon").doubleValue());
            geoMap.put("cityName", cityName);
            geoMap.put("country", CountryEnum.getCountryName(countryCode));
        }
        return geoMap;
    }
}
