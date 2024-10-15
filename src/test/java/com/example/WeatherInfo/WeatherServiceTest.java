package com.example.WeatherInfo;

import com.example.WeatherInfo.model.PinCode;
import com.example.WeatherInfo.model.WeatherInfo;
import com.example.WeatherInfo.repository.WeatherInfoRepository;
import com.example.WeatherInfo.service.PinCodeService;
import com.example.WeatherInfo.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WeatherServiceTest {

    @Mock
    private WeatherInfoRepository weatherInfoRepository;

    @Mock
    private PinCodeService pinCodeService;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetWeatherAtPinCode_CachedData() throws Exception {
        // Arrange
        Long pinCode = 123456L;
        WeatherInfo weatherInfo = new WeatherInfo(pinCode, "Clear sky", "Location1");
        when(weatherInfoRepository.findByPinCode(pinCode)).thenReturn(Optional.of(weatherInfo));

        // Act
        String result = weatherService.getWeatherAtPinCode(pinCode);

        // Assert
        assertTrue(result.contains("Clear sky"));
        assertTrue(result.contains("Location1"));
        assertTrue(result.contains("cached data"));
        verify(weatherInfoRepository, times(1)).findByPinCode(pinCode);
        verify(pinCodeService, never()).getGeoCoordinates(any());
    }

    @Test
    void testGetWeatherAtPinCode_NewData() throws Exception {
        // Arrange
        Long pinCode = 654321L;
        ArrayList<PinCode> pinCodes = new ArrayList<>();
        pinCodes.add(new PinCode(654321L, "chennai", 44.34, 10.99, "Kanchipuram", "Tamil Nadu"));
        when(weatherInfoRepository.findByPinCode(pinCode)).thenReturn(Optional.empty());
        when(pinCodeService.getGeoCoordinates(pinCode)).thenReturn(pinCodes);

        // Act
        String result = weatherService.getWeatherAtPinCode(pinCode);

        // Assert
        assertNotNull(result);  // API response exists
        assertFalse(result.contains("cached data"));
        verify(weatherInfoRepository, times(1)).findByPinCode(pinCode);
        verify(weatherInfoRepository, times(1)).save(any(WeatherInfo.class));
    }

//    @Test
//    void testGetWeatherAtPinCode_MalformedURLException() throws Exception {
//        // Arrange
//        Long pinCode = 123456L;
//        when(weatherInfoRepository.findByPinCode(pinCode)).thenReturn(Optional.empty());
//        when(pinCodeService.getGeoCoordinates(pinCode)).thenReturn(new ArrayList<>());
//
//        // Act & Assert
//        assertThrows(MalformedURLException.class, () -> weatherService.getWeatherAtPinCode(pinCode));
//    }

//    @Test
//    void testGetWeatherAtPinCode_IOException() throws Exception {
//        // Arrange
//        Long pinCode = 123456L;
//        ArrayList<PinCode> pinCodes = new ArrayList<>();
//        pinCodes.add(new PinCode(654321L, "chennai", 44.34, 10.99, "Kanchipuram", "Tamil Nadu"));
//        when(weatherInfoRepository.findByPinCode(pinCode)).thenReturn(Optional.empty());
//        when(pinCodeService.getGeoCoordinates(pinCode)).thenReturn(pinCodes);
//
//        // Simulate IO Exception during the API call
//        doThrow(IOException.class).when(weatherInfoRepository).save(any(WeatherInfo.class));
//
//        // Act & Assert
//        assertThrows(IOException.class, () -> weatherService.getWeatherAtPinCode(pinCode));
//    }

    @Test
    void testExtractValue() {
        // Arrange
        String json = "{\"main\":\"Clear\",\"description\":\"clear sky\"}";

        // Act
        String mainWeather = WeatherService.extractValue(json, "\"main\":\"", "\"");
        String description = WeatherService.extractValue(json, "\"description\":\"", "\"");

        // Assert
        assertEquals("Clear", mainWeather);
        assertEquals("clear sky", description);
    }
}
