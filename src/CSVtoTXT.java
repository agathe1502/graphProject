import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

public class CSVtoTXT {

    private HashMap<Integer, double[]> coordinates;
    private ArrayList<String[]> listCity;
    private HashMap<Integer, Integer> populations;


    public void fileConversion(int dmax, int popmin, boolean createFile) throws IOException {
        coordinates = new HashMap<>();
        populations = new HashMap<>();

        System.out.println("File is converting ...");
        readFile(popmin);
        if (createFile) {
            writeFile(dmax, popmin);
            // writeFileVRP1();
            // writeFileVRP2(popmin);
        } else {
            createCoordinates();
        }


    }

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
            for (int j = 0; j < listCity.size(); j++) {
                double lon2 = Math.toRadians(Double.parseDouble(listCity.get(j)[4]));
                double lat2 = Math.toRadians(Double.parseDouble(listCity.get(j)[5]));

                double d = Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1);
                if (d <= 1.0) {
                    d = Math.acos(d) * 6378.137;
                } else {
                    d = 0;
                }
                if (i != j) {
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

    private void createCoordinates() {
        for (int i = 0; i < listCity.size(); i++) {
            double longitude = Double.parseDouble(listCity.get(i)[4]);
            double latitude = Double.parseDouble(listCity.get(i)[5]);
            coordinates.put(i, new double[]{longitude, latitude});
        }
    }

    public HashMap<Integer, double[]> getCoordinates() {
        return coordinates;
    }

    public HashMap<Integer, Integer> getPopulations() {
        return populations;
    }

}
