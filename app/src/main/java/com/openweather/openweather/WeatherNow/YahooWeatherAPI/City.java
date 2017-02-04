package com.openweather.openweather.WeatherNow.YahooWeatherAPI;

/**
 * Created by andy6804tw on 2017/2/4.
 */

public class City {

    private String cityName ;

    public City(){
        cityName = "";
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
