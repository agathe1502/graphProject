package models;

import java.util.List;

/**
 * Modèle qui représente l'algorithme du VRP1. Elle est utilisée pour transformer un objet lors d'un appel API
 */
public class VRP1Model {

    /**
     * Les grandes villes de France
     */
    private final List<CityModel> bigCities;
    /**
     * Les moyennes trouvées pour accéder aux grandes villes de France
     */
    private final List<Double> averages;
    /**
     * Le résultat de la simulation : ville où doit habiter le VRP
     */
    private final CityModel cityToLive;
    /**
     * La moyenne pour accéder aux grandes villes en habitant dans la cityToLive
     */
    private final Double average;

    /**
     * Constructeur
     * @param bigCities les grandes villes
     * @param averages les moyennes aux grandes villes
     * @param cityToLive la ville à habiter
     * @param average la moyenne pour accéder aux grandes villes
     */
    public VRP1Model(List<CityModel> bigCities, List<Double> averages, CityModel cityToLive, double average) {
        this.bigCities = bigCities;
        this.averages = averages;
        this.cityToLive = cityToLive;
        this.average = average;
    }

    /**
     * Retourner les grandes villes
     * @return les grandes villes
     */
    public List<CityModel> getBigCities() {
        return bigCities;
    }

    /**
     * Retourne les moyennes
     * @return les moyennes aux grandes villes
     */
    public List<Double> getAverages() {
        return averages;
    }

    /**
     * Retourne la ville à habiter
     * @return la ville à habiter
     */
    public CityModel getCityToLive() {
        return cityToLive;
    }

    /**
     * Retourne la moyenne aux grandes villes en habitant à la ville du résultat
     * @return la moyenne aux grandes villes en habitant à la ville du résultat
     */
    public Double getAverage() {
        return average;
    }
}
