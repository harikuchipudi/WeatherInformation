package com.example.WeatherInfo;

import com.example.WeatherInfo.model.PinCode;
import com.example.WeatherInfo.repository.PinCodeRepository;
import com.example.WeatherInfo.service.PinCodeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class PinCodeServiceTest {

    @Mock
    private PinCodeRepository pinCodeRepository;

    @InjectMocks
    private PinCodeService pinCodeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    private HttpClient httpClient;

    @Test
    public void testGetGeoCoordinates_WhenPinCodeExists() throws Exception {
        // Arrange
        Long pinCode = 123456L;
        PinCode existingPinCode = new PinCode(pinCode, "Area", 10.0, 20.0, "District", "State");
        when(pinCodeRepository.findByPinCode(pinCode)).thenReturn(Optional.of(existingPinCode));

        // Act
        List<PinCode> result = pinCodeService.getGeoCoordinates(pinCode);

        // Assert
        assertEquals(1, result.size());
        assertEquals(existingPinCode, result.get(0));
        verify(pinCodeRepository, times(1)).findByPinCode(pinCode);
        verifyNoMoreInteractions(pinCodeRepository);
    }

    @Test
    public void testGetGeoCoordinates_WhenPinCodeDoesNotExist() throws Exception {
        // Arrange
        Long pinCode = 123456L;
        when(pinCodeRepository.findByPinCode(pinCode)).thenReturn(Optional.empty());

        // Mock the HTTP response and JSON parsing
        String jsonResponse = "[{\"pincode\": 123456, \"area\": \"Area\", \"lat\": 10.0, \"lng\": 20.0, \"district\": \"District\", \"state\": \"State\"}]";
        mockStatic(HttpClient.class);
        HttpResponse mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonResponse);

        // Act
        List<PinCode> result = pinCodeService.getGeoCoordinates(pinCode);

        // Assert
        assertEquals(1, result.size());
        assertEquals(pinCode, result.get(0).getPinCode());
        verify(pinCodeRepository, times(1)).findByPinCode(pinCode);
        verify(pinCodeRepository, times(1)).save(any(PinCode.class));
    }

    @Test
    public void testSendApiRequest_WhenSuccessful() throws IOException, InterruptedException {
        // Arrange
        Long pinCode = 123456L;
        String expectedResponse = "{\"pincode\": 123456, \"area\": \"Area\", \"lat\": 10.0, \"lng\": 20.0, \"district\": \"District\", \"state\": \"State\"}";

        // Mock the HTTP client and response
        HttpResponse mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(expectedResponse);

        // Act
        String responseBody = pinCodeService.sendApiRequest(pinCode);

        // Assert
        assertNotNull(responseBody);
        assertEquals(expectedResponse, responseBody);
    }

    @Test
    public void testSendApiRequest_WhenApiFails() throws IOException, InterruptedException {
        // Arrange
        Long pinCode = 123456L;

        // Mock the HTTP client to throw an IOException
        doThrow(new IOException("Failed to fetch data from API")).when(pinCodeService).sendApiRequest(pinCode);

        // Act & Assert
        assertThrows(IOException.class, () -> pinCodeService.sendApiRequest(pinCode));
    }

    @Test
    public void testParseApiResponse_WhenSuccessful() throws JsonProcessingException {
        // Arrange
        String jsonResponse = "[{\"pincode\": 123456, \"area\": \"Area\", \"lat\": 10.0, \"lng\": 20.0, \"district\": \"District\", \"state\": \"State\"}]";

        // Act
        List<PinCode> pinCodes = pinCodeService.parseApiResponse(jsonResponse);

        // Assert
        assertNotNull(pinCodes);
        assertEquals(1, pinCodes.size());
        assertEquals(123456L, pinCodes.get(0).getPinCode());
    }

    @Test
    public void testParseApiResponse_WhenJsonParsingFails() {
        // Arrange
        String invalidJson = "invalid json";

        // Act & Assert
        assertThrows(JsonProcessingException.class, () -> pinCodeService.parseApiResponse(invalidJson));
    }
}
