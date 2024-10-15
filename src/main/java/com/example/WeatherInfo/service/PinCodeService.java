package com.example.WeatherInfo.service;

import com.example.WeatherInfo.model.PinCode;
import com.example.WeatherInfo.repository.PinCodeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class PinCodeService {

    @Autowired
    private PinCodeRepository pinCodeRepository;

    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String API_URL = "https://india-pincode-with-latitude-and-longitude.p.rapidapi.com/api/v1/pincode/";
    private static final String API_KEY = "c16aa61ca3msh2021cca5becb6b3p11825ajsnb2396dc4b40f";

    public List<PinCode> getGeoCoordinates(Long pinCode) throws Exception {
        Optional<PinCode> pinCodeOptional = pinCodeRepository.findByPinCode(pinCode);

        if (pinCodeOptional.isPresent()) {
            return Collections.singletonList(pinCodeOptional.get());
        } else {
            return fetchPinCodeFromApi(pinCode);
        }
    }

    private List<PinCode> fetchPinCodeFromApi(Long pinCode) throws IOException {
        String responseBody = null;
        try {
            responseBody = sendApiRequest(pinCode);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return parseApiResponse(responseBody);
    }

    public String sendApiRequest(Long pinCode) throws IOException, InterruptedException {
        String url = API_URL + pinCode;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-rapidapi-key", API_KEY)
                .header("x-rapidapi-host", "india-pincode-with-latitude-and-longitude.p.rapidapi.com")
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Failed to fetch data from API, status code: " + response.statusCode());
        }

        return response.body();
    }

    public List<PinCode> parseApiResponse(String responseBody) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> pincodeInfoList = objectMapper.readValue(responseBody, new TypeReference<List<Map<String, Object>>>() {});

        List<PinCode> pinCodes = new ArrayList<>();
        for (Map<String, Object> pincodeInfo : pincodeInfoList) {
            Long currentPincode = ((Number) pincodeInfo.get("pincode")).longValue();
            String area = (String) pincodeInfo.get("area");
            Double lat = (Double) pincodeInfo.get("lat");
            Double lng = (Double) pincodeInfo.get("lng");
            String district = (String) pincodeInfo.get("district");
            String state = (String) pincodeInfo.get("state");

            PinCode pinCodeObj = new PinCode(currentPincode, area, lat, lng, district, state);
            pinCodeRepository.save(pinCodeObj);
            pinCodes.add(pinCodeObj);
        }
        return pinCodes;
    }
}
