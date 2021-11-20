package com.example.track;

public class Cordenadas_ll {
    private Double latitude;
    private Double longitude;
    private String data;

    public Cordenadas_ll(Double latitude, String data, Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
        this.data = data;
    }

    public Double getLatitude(){
        return latitude;
    }


    public Double getLongitude(){
        return longitude;
    }

    public String getData(){
        return data;
    }
}
