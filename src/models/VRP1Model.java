package models;

import java.util.List;

public class VRP1Model {

    private final List<CityModel> bigCities;
    private final List<Double> averages;
    private final CityModel cityToLive;
    private final Double average;

    public VRP1Model(List<CityModel> bigCities, List<Double> averages, CityModel cityToLive, double average) {
        this.bigCities = bigCities;
        this.averages = averages;
        this.cityToLive = cityToLive;
        this.average = average;
    }

    public List<CityModel> getBigCities() {
        return bigCities;
    }

    public List<Double> getAverages() {
        return averages;
    }

    public CityModel getCityToLive() {
        return cityToLive;
    }

    public Double getAverage() {
        return average;
    }
}
