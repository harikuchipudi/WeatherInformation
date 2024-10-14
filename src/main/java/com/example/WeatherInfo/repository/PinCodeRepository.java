package com.example.WeatherInfo.repository;

import com.example.WeatherInfo.model.PinCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PinCodeRepository extends JpaRepository<PinCode, Long> {
    Optional<PinCode> findByPinCode(Long pinCode);
}
