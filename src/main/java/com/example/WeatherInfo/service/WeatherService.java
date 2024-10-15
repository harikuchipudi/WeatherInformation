package com.example.WeatherInfo.service;

import com.example.WeatherInfo.model.PinCode;
import com.example.WeatherInfo.model.WeatherInfo;
import com.example.WeatherInfo.model.WeatherResponse;
import com.example.WeatherInfo.repository.WeatherInfoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;


@Service
public class WeatherService {

    @Autowired
    private WeatherInfoRepository weatherInfoRepository;

    @Autowired
    private PinCodeService pinCodeService;

    private static final HttpClient client = HttpClient.newHttpClient();


    public String getWeatherAtPinCode(Long pinCode) throws Exception {

        //checking if the weather data is already loaded in the database
        Optional<WeatherInfo> weatherInfoOptional = weatherInfoRepository.findByPinCode(pinCode);
        if(weatherInfoOptional.isPresent()){
            WeatherInfo weatherInfo = weatherInfoOptional.get();
            String result = String.valueOf(weatherInfo.getPinCode()) + "  " + weatherInfo.getLocation() + "  " + weatherInfo.getWeatherData() ;
            result += "------>>>>>>This is cached data<<<<<<--------";
            return result;
        }

        List<PinCode> pinCodes = pinCodeService.getGeoCoordinates(pinCode);

        String lon = "";
        String lat = "";
        Long pinCodeforObject = 1L;
        System.out.println("Number of locations: "+pinCodes.size());

        for(PinCode pinCode1 : pinCodes){
            lon = String.valueOf(pinCode1.getLongitude());
            lat = String.valueOf(pinCode1.getLatitude());
            pinCodeforObject = pinCode1.getPinCode();
        }

//        lon = "10.99";
//        lat = "44.34";


        String api_key = "a71a95829a797e45030fa0935d8a26e6";
        String url_string = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s", lat, lon, api_key);

        try {
            //url
            URL url = new URL(url_string);

            //connection object creation
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            //get request
            conn.setRequestMethod("GET");

            //opening a connection
            conn.connect();

            //get the response code
            int responsecode = conn.getResponseCode();

            String result = "";


            //read the body using scanner
            if (responsecode != 200)
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            else {
                Scanner sc = new Scanner(url.openStream());
                String inline = "";
                while (sc.hasNext()) {
                    inline += sc.nextLine();
                    result += inline;
                }
//            System.out.println("\nJSON data in string format");
//            System.out.println(inline);
                sc.close();
            }

            // Extract the weather main description
            String mainWeather = extractValue(result, "\"main\":\"", "\"");

            // Extract the weather description
            String weatherDescription = extractValue(result, "\"description\":\"", "\"");

            // Extract the location name and country
            String location = extractValue(result, "\"name\":\"", "\"");
            String country = extractValue(result, "\"country\":\"", "\"");

            WeatherInfo weatherInfo = new WeatherInfo(pinCodeforObject, weatherDescription, location);
            weatherInfoRepository.save(weatherInfo);

            return result;
        }
        catch (MalformedURLException e) {
            throw new MalformedURLException("Malformed URL: " + e.getMessage());
        } catch (IOException e) {
            throw new IOException("Error during API call: " + e.getMessage());
        } catch (Exception e) {
            throw new Exception("An error occurred while fetching weather data: " + e.getMessage());
        }
    }


    public static String extractValue(String json, String key, String delimiter) {
        int startIndex = json.indexOf(key) + key.length();
        int endIndex = json.indexOf(delimiter, startIndex);
        return json.substring(startIndex, endIndex);
    }
}

//