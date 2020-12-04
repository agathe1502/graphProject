package models;

/**
 * Modèle qui représente une ville. Elle est utilisée pour transformer un objet lors d'un appel API
 */
public class CityModel {

    /**
     * L'id de la ville dans le graphe
     */
    private final int id;
    /**
     * Le nom de la ville
     */
    private final String name;
    /**
     * Le nombre d'habitants de la ville
     */
    private final int population;
    /**
     * La longitude de la ville
     */
    private final double lng;
    /**
     * La latitude de la ville
     */
    private final double lat;

    /**
     * Constructeur d'une ville
     * @param id son id
     * @param name son nom
     * @param population son nombre d'habitants
     * @param lng sa longitude
     * @param lat sa latitude
     */
    public CityModel(int id, String name, int population, double lng, double lat) {
        this.id = id;
        this.name = name;
        this.population = population;
        this.lng = lng;
        this.lat = lat;
    }

    /**
     * Récupère l'id
     * @return l'id
     */
    public int getId() {
        return id;
    }
    /**
     * Récupère le nom
     * @return le nom de la ville
     */
    public String getName() {
        return name;
    }

    /**
     * Récupère le nombre d'habitants
     * @return le nombre d'habitants de la ville
     */
    public int getPopulation() {
        return population;
    }

    /**
     * Récupère la longitude
     * @return la longitude de la ville
     */
    public double getLng() {
        return lng;
    }

    /**
     * Récupère la latitude
     * @return la latitude de la ville
     */
    public double getLat() {
        return lat;
    }
}
