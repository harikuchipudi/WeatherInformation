package com.example.WeatherInfo.service;

import com.example.WeatherInfo.model.PinCode;
import com.example.WeatherInfo.repository.PinCodeRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
public class PinCodeService {

    @Autowired
    private PinCodeRepository pinCodeRepository;

    private static final HttpClient client = HttpClient.newHttpClient();

    public ArrayList<PinCode> getGeoCoordinates(Long pinCode) {

        String url = "https://india-pincode-with-latitude-and-longitude.p.rapidapi.com/api/v1/pincode/";
        url += String.valueOf(pinCode);

        ArrayList<PinCode> pinCodes = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("x-rapidapi-key", "c16aa61ca3msh2021cca5becb6b3p11825ajsnb2396dc4b40f")
                .header("x-rapidapi-host", "india-pincode-with-latitude-and-longitude.p.rapidapi.com")
                .GET()
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> pincodeInfoList = objectMapper.readValue(responseBody, new TypeReference<List<Map<String, Object>>>() {
            });
            for (Map<String, Object> pincodeInfo : pincodeInfoList) {
                Long currentPincode = ((Number) pincodeInfo.get("pincode")).longValue();
                String area = (String) pincodeInfo.get("area");
                Double lat = (Double) pincodeInfo.get("lat");
                Double lng = (Double) pincodeInfo.get("lng");
                String district = (String) pincodeInfo.get("district");
                String state = (String) pincodeInfo.get("state");

                // Create a PinCode object
                PinCode pinCodeObj = new PinCode(currentPincode, area, lat, lng, district, state);
                pinCodeRepository.save(pinCodeObj);

                // Add the PinCode object to the list
                pinCodes.add(pinCodeObj);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return pinCodes;
    }
}

//    // Asynchronously send the request and handle the response
//    CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, HttpResponse.BodyHandlers.ofString());
//
//    ObjectMapper objectMapper = new ObjectMapper();
//
////         Process the response when it arrives
//        response.thenAccept(res -> {
////            System.out.println("Status Code: " + res.statusCode());
////            System.out.println("Response Body: " + res.body());
//
//        String responseBody = res.body().toString();
//
//        try {
//            List<Map<String, Object>> pincodeInfoList = objectMapper.readValue(responseBody, new TypeReference<List<Map<String, Object>>>() {});
//            for (Map<String, Object> pincodeInfo : pincodeInfoList) {
//                Long currentPincode = ((Number) pincodeInfo.get("pincode")).longValue();
//                String area = (String) pincodeInfo.get("area");
//                Double lat = (Double) pincodeInfo.get("lat");
//                Double lng = (Double) pincodeInfo.get("lng");
//                String district = (String) pincodeInfo.get("district");
//                String state = (String) pincodeInfo.get("state");
//
//                // Create a PinCode object
//                PinCode pinCodeObj = new PinCode(currentPincode, area, lat, lng, district, state);
//
//                // Add the PinCode object to the list
//                pinCodes.add(pinCodeObj);
//            }
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    });
//
//
//}
