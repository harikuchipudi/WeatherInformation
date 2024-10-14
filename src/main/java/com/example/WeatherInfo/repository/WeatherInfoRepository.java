package com.example.WeatherInfo.repository;

import com.example.WeatherInfo.model.PinCode;
import com.example.WeatherInfo.model.WeatherInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WeatherInfoRepository extends JpaRepository<WeatherInfo, Long> {
    Optional<WeatherInfo> findByPinCode(Long pinCode);
}



