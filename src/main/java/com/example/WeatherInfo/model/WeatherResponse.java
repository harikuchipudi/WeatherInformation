package com.example.WeatherInfo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    private List<Weather> weather;

    // Getter and setter for weather
    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    // Method to get the main weather description
    public String getMainWeatherDescription() {
        if (weather != null && !weather.isEmpty()) {
            return weather.get(0).getDescription();
        }
        return null; // Handle if there's no weather data available
    }

    // Inner Weather class to hold description
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        private String description;

        // Getter and setter for description
        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
