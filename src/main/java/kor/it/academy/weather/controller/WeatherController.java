package kor.it.academy.weather.controller;

import kor.it.academy.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weather")
    public ModelAndView getWeatherByCity() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/views/weather/display");

        try {
            String defaultCity = "서울";
            Map<String, Object> dataMap = weatherService.getWeather(defaultCity);
            Set<String> keys = dataMap.keySet();

            for (String key : keys) {
                view.addObject(key, dataMap.get(key));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @GetMapping("/api/wx/{cityName}")
    @ResponseBody
    public Map<String , Object> apiWeatherByCity(@PathVariable("cityName") String cityName) {

        Map<String, Object> dataMap = new HashMap<>();

        try {
             dataMap = weatherService.getWeather(cityName);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dataMap;
    }
}
