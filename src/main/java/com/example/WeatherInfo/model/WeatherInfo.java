package com.example.WeatherInfo.model;

import com.example.WeatherInfo.service.WeatherService;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WeatherInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long pinCode;
    private String weatherData;
    private String location;

    public Long getPinCode(){
        return this.pinCode;
    }

    public String getWeatherData(){
        return this.weatherData;
    }

    public String getLocation(){
        return this.location;
    }

    public WeatherInfo(Long pinCode, String weatherData, String location){
        this.pinCode = pinCode;
        this.weatherData = weatherData;
        this.location = location;
    }
}
