package models;

import java.util.List;

public class ShortestPathAlgorithmModel {

    private final double distance;
    private final double time;
    private final List<CityModel> path;

    public ShortestPathAlgorithmModel(double distance, double time, List<CityModel> path) {
        this.distance = distance;
        this.time = time;
        this.path = path;
    }

    public double getDistance() {
        return distance;
    }

    public double getTime() {
        return time;
    }

    public List<CityModel> getPath() {
        return path;
    }
}