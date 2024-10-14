package com.example.WeatherInfo.controller;

import com.example.WeatherInfo.model.PinCode;
import com.example.WeatherInfo.service.PinCodeService;
import com.example.WeatherInfo.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/weather")
public class WeatherInfoController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private PinCodeService pinCodeService;

    @GetMapping("/hello")
    public ResponseEntity<String> Hello(){
        return ResponseEntity.ok("Hello world");
    }

//    @GetMapping("/coord")
//    public ResponseEntity<ArrayList<PinCode>> getCoordinates(@RequestParam Long pin) throws Exception {
//        return ResponseEntity.ok(pinCodeService.getGeoCoordinates(pin));
//    }

    @GetMapping("/forecast")
    public ResponseEntity<String> getWeatherForecast(@RequestParam Long pinCode) throws Exception {
        return ResponseEntity.ok(weatherService.getWeatherAtPinCode(pinCode));
    }

}
