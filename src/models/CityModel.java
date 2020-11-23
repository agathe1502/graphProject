package models;

public class CityModel {

    private final int id;
    private final String name;
    private final int population;
    private final double lng;
    private final double lat;

    public CityModel(int id, String name, int population, double lng, double lat) {
        this.id = id;
        this.name = name;
        this.population = population;
        this.lng = lng;
        this.lat = lat;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    public double getLng() {
        return lng;
    }

    public double getLat() {
        return lat;
    }
}
