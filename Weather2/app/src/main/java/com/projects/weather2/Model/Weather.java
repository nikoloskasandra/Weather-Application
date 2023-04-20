package com.projects.weather2.Model;

public class Weather {

    private String time;
    private String temperatureInCelsius;
    private String getTemperatureInFahrenheit;
    private String weatherIcon;
    private String condition;

    public Weather(){}

    public Weather(String time, String temperatureInCelsius, String getTemperatureInFahrenheit, String weatherIcon, String condition) {
        this.time = time;
        this.temperatureInCelsius = temperatureInCelsius;
        this.getTemperatureInFahrenheit = getTemperatureInFahrenheit;
        this.weatherIcon = weatherIcon;
        this.condition = condition;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperatureInCelsius() {
        return temperatureInCelsius;
    }

    public void setTemperatureInCelsius(String temperatureInCelsius) {
        this.temperatureInCelsius = temperatureInCelsius;
    }

    public String getGetTemperatureInFahrenheit() {
        return getTemperatureInFahrenheit;
    }

    public void setGetTemperatureInFahrenheit(String getTemperatureInFahrenheit) {
        this.getTemperatureInFahrenheit = getTemperatureInFahrenheit;
    }

    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
