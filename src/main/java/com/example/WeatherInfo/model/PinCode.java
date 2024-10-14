package com.example.WeatherInfo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Entity
public class PinCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    private Long pinCode;
    private String location;
    private double longitude;
    private double latitude;
    private String district;
    private String state;

    public Long getPinCode(){
        return this.pinCode;
    }

    public double getLongitude(){
        return this.longitude;
    }

    public double getLatitude(){
        return this.latitude;
    }

    public String getLocation(){
        return this.location;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public void setLatitude(Long latitude){
        this.latitude = latitude;
    }

    public void setLongitude(Long longitude){
        this.longitude = longitude;
    }

    public PinCode(Long pinCode, String location, double latitude, double longitude, String district, String state){
        this.pinCode = pinCode;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.district = district;
        this.state = state;
    }

}
