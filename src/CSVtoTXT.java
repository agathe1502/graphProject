import models.CityModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CSVtoTXT {

    /**
     * Hashmap pour les coordonnées
     */
    private HashMap<Integer, double[]> coordinates;
    /**
     * Liste des villes
     */
    private ArrayList<String[]> listCity;
    /**
     * Hashmap pour la population
     */
    private HashMap<Integer, Integer> populations;

    /**
     * Méthode qui permet la conversion d'un fichier CVS à un fichier TXT
     * - Lecture du fichier CSV
     * - Si le fichier n'existe pas, création du fichier TXT
     * - Sinon on remplit la hashmap des coordonnées
     *
     * @param dmax distance maximum entre 2 villes pour créer une liaison
     * @param popmin population minimum que doit avoir une ville pour la garder
     * @param createFile true si le fichier est déjà créé, sinon faux
     */
    public void fileConversion(int dmax, int popmin, boolean createFile) {
        coordinates = new HashMap<>();
        populations = new HashMap<>();

        try{
            System.out.println("File is converting ...");
            readFile(popmin);
            if (createFile) {
                writeFile(dmax, popmin);
                // writeFileVRP1();
                // writeFileVRP2(popmin);
            } else {
                createCoordinates();
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }



    }

    /**
     * Méthode qui permet d'écrire le fichier TXT pour le VRP1
     * Prend en compte une condition sur la population des villes afin de créer un arc
     * @throws IOException
     */
    private void writeFileVRP1() throws IOException {
        String filename = "File_VRP1.txt";
        FileWriter fw = new FileWriter("src/files/" + filename);

        //Write into file
        fw.write("File name : " + filename + "\n");
        fw.write("Number of vertex : " + listCity.size() + "\n");

        fw.write("--- Vertex --- " + "\n");
        for (int i = 0; i < listCity.size(); i++) {
            fw.write(i + " " + listCity.get(i)[1] + "\n");
        }

        fw.write("--- Edges --- " + "\n");
        int nbEdge = 0;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);

        for (int i = 0; i < listCity.size(); i++) {
            double longitude = Double.parseDouble(listCity.get(i)[4]);
            double latitude = Double.parseDouble(listCity.get(i)[5]);
            double lon1 = Math.toRadians(longitude);
            double lat1 = Math.toRadians(latitude);
            coordinates.put(i, new double[]{longitude, latitude});
            for (int j = i + 1; j < listCity.size(); j++) {
                double lon2 = Math.toRadians(Double.parseDouble(listCity.get(j)[4]));
                double lat2 = Math.toRadians(Double.parseDouble(listCity.get(j)[5]));

                double d = Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);
                if (d <= 1.0) {
                    d = Math.acos(d) * 6378.137;
                } else {
                    d = 0;
                }

                if (populations.get(i) > 200000 || populations.get(j) > 200000) {
                    fw.write(nbEdge + " " + i + " " + j + " " + df.format(d) + "\n");
                    nbEdge++;
                }
            }
        }
        fw.flush();
        fw.close();

        System.out.println("Number of vertex : " + listCity.size());
        System.out.println("Number of Edge : " + nbEdge);

    }

    /**
     * Méthode qui permet d'écrire le fichier TXT pour le VRP2
     * Tous les arcs entre toutes les villes sont créés, il n'y a aucune condition sur leur création
     * @param popmin population minimum qu'une ville doit posséder
     * @throws IOException
     */
    private void writeFileVRP2(int popmin) throws IOException {
        String filename = "File_VRP2_" + popmin + ".txt";
        FileWriter fw = new FileWriter("src/files/" + filename);

        //Write into file
        fw.write("File name : " + filename + "\n");
        fw.write("Number of vertex : " + listCity.size() + "\n");

        fw.write("--- Vertex --- " + "\n");
        for (int i = 0; i < listCity.size(); i++) {
            fw.write(i + " " + listCity.get(i)[1] + "\n");
        }

        fw.write("--- Edges --- " + "\n");
        int nbEdge = 0;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);

        for (int i = 0; i < listCity.size(); i++) {
            double longitude = Double.parseDouble(listCity.get(i)[4]);
            double latitude = Double.parseDouble(listCity.get(i)[5]);
            double lon1 = Math.toRadians(longitude);
            double lat1 = Math.toRadians(latitude);
            coordinates.put(i, new double[]{longitude, latitude});
            for (int j = i + 1 ; j < listCity.size(); j++) {
                double lon2 = Math.toRadians(Double.parseDouble(listCity.get(j)[4]));
                double lat2 = Math.toRadians(Double.parseDouble(listCity.get(j)[5]));

                double d = Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);
                if (d <= 1.0) {
                    d = Math.acos(d) * 6378.137;
                } else {
                    d = 0;
                }
                fw.write(nbEdge + " " + i + " " + j + " " + df.format(d) + "\n");
                nbEdge++;
            }
        }
        fw.flush();
        fw.close();

        System.out.println("Number of vertex : " + listCity.size());
        System.out.println("Number of Edge : " + nbEdge);
    }

    /**
     * Méthode qui permet d'écrire le fichier TXT de manière générale pour les autres algorithmes
     * Les arcs sont écrits si leurs valeurs sont inférieurs a dmax
     * @param dmax
     * @param popmin
     * @throws IOException
     */
    public void writeFile(int dmax, int popmin) throws IOException {

        String filename = "Communes_" + dmax + "_" + popmin + ".txt";
        FileWriter fw = new FileWriter("src/files/" + filename);

        //Write into file
        fw.write("File name : " + filename + "\n");
        fw.write("Number of vertex : " + listCity.size() + "\n");

        fw.write("--- Vertex --- " + "\n");
        for (int i = 0; i < listCity.size(); i++) {
            fw.write(i + " " + listCity.get(i)[1] + "\n");
        }

        fw.write("--- Edges --- " + "\n");
        int nbEdge = 0;
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.HALF_UP);

        for (int i = 0; i < listCity.size(); i++) {
            double longitude = Double.parseDouble(listCity.get(i)[4]);
            double latitude = Double.parseDouble(listCity.get(i)[5]);
            double lon1 = Math.toRadians(longitude);
            double lat1 = Math.toRadians(latitude);
            coordinates.put(i, new double[]{longitude, latitude});
            for (int j = i + 1; j < listCity.size(); j++) {
                double lon2 = Math.toRadians(Double.parseDouble(listCity.get(j)[4]));
                double lat2 = Math.toRadians(Double.parseDouble(listCity.get(j)[5]));

                double d = Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);
                if (d <= 1.0) {
                    d = Math.acos(d) * 6378.137;
                } else {
                    d = 0;
                }

                if (d < dmax) {
                    fw.write(nbEdge + " " + i + " " + j + " " + df.format(d) + "\n");
                    nbEdge++;
                }
            }
        }
        fw.flush();
        fw.close();

        System.out.println("Number of vertex : " + listCity.size());
        System.out.println("Number of Edge : " + nbEdge);

    }

    /**
     * Méthode qui permet de lire le fichier CSV
     * Elle trie les villes suivant leur population (popmin) et les ajoute à la liste
     * @param popmin population minimum requise pour garder une ville
     * @throws IOException
     */
    private void readFile(int popmin) throws IOException {
        String[] city;
        listCity = new ArrayList<>();

        String line = "";


        BufferedReader br = new BufferedReader(new FileReader("CommunesFrance.csv"));

        try {
            // Skip the first line
            br.readLine();
            int loop = 0;

            while ((line = br.readLine()) != null) {
                city = line.split(";");
                int popCity = Integer.parseInt(city[3]);
                populations.put(loop, popCity);
                // Sorting by population
                if (popCity >= popmin) {
                    listCity.add(city);
                }
                loop++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        br.close();


    }

    /**
     * Méthode qui permet de remplir le tableau des coordonnées pour chaque ville de la liste
     */
    private void createCoordinates() {
        for (int i = 0; i < listCity.size(); i++) {
            double longitude = Double.parseDouble(listCity.get(i)[4]);
            double latitude = Double.parseDouble(listCity.get(i)[5]);
            coordinates.put(i, new double[]{longitude, latitude});
        }
    }

    /**
     * Retourne la hashmap contenant les coordonnées (longitude et latitude)
     * @return hashmap des coordonnées
     */
    public HashMap<Integer, double[]> getCoordinates() {
        return coordinates;
    }

    /**
     * Retourne la hashmap contenant les population de chaque ville
     * @return hashmap des populations des villes
     */
    public HashMap<Integer, Integer> getPopulations() {
        return populations;
    }

    public List<CityModel> getListCitiesModel(){
        List<CityModel> citiesModel = new ArrayList<>();
        for (int i = 0; i < listCity.size(); i++){
            citiesModel.add(new CityModel(i,listCity.get(i)[1],Integer.parseInt(listCity.get(i)[3]),Double.parseDouble(listCity.get(i)[4]),Double.parseDouble(listCity.get(i)[5])));
        }
        return citiesModel;
    }

}
