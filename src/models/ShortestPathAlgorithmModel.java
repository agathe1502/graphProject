package models;

import java.util.List;

/**
 * Modèle qui représente le résultat d'un algorithme de plus court chemin. Elle est utilisée pour transformer un objet lors d'un appel API
 */
public class ShortestPathAlgorithmModel {

    /**
     * La distance déterminée par l'algorithme
     */
    private final double distance;
    /**
     * Le temps mis pour faire la simulation de l'algorithme
     */
    private final double time;
    /**
     * Le plus court chemin déterminée -> Ensemble des villes
     */
    private final List<CityModel> path;

    /**
     * Constructeur
     * @param distance la distance
     * @param time le temps de simulation
     * @param path le chemin trouvé
     */
    public ShortestPathAlgorithmModel(double distance, double time, List<CityModel> path) {
        this.distance = distance;
        this.time = time;
        this.path = path;
    }

    /**
     * Retourne la distance
     * @return la distance
     */
    public double getDistance() {
        return distance;
    }

    /**
     * Retourne le temps
     * @return le temps
     */
    public double getTime() {
        return time;
    }

    /**
     * Retourne le chemin
     * @return le chemin
     */
    public List<CityModel> getPath() {
        return path;
    }
}