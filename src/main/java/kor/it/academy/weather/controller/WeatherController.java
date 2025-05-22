package kor.it.academy.weather.controller;

import kor.it.academy.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weather/{city}")
    public ModelAndView getWeatherByCity(@PathVariable("city") String city) {
        ModelAndView view = new ModelAndView();
        view.setViewName("/views/weather/display");

        try {
            Map<String, Object> dataMap = weatherService.getWeather(city);
            Set<String> keys = dataMap.keySet();

            for (String key : keys) {
                view.addObject(key, dataMap.get(key));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }
}
