import com.google.gson.Gson;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.lang.Character;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


@Path("/")
public class Main implements ServletContextListener{
    private static HashMap<String, String> sippMapType;
    private static HashMap<String, String> sippMapDoors;
    private static HashMap<String, String> sippMapTrans;
    private static HashMap<String, String> sippMapFuel;
    private static List<Vehicle> list;

    public void contextInitialized(ServletContextEvent arg0) {
        // Read JSON form url
        String json = "";
        try {
            json = readUrl("http://www.rentalcars.com/js/vehicles.json ");
        }
        catch (Exception e){
            System.out.println(e);
        }
        Gson gson = new Gson();
        Search search = gson.fromJson(json, Search.class);
        list = search.Search.VehicleList;

        // Add SIPP code mappings
        mapSIPP();
    }

    @Path("/getVehiclesPrice")
    @GET
    public String getVehiclesPrice() {
        Gson gson = new Gson();
        return gson.toJson(getVehiclePrices(list));
    }

    @Path("/getVehiclesSIPP")
    @GET
    public String getVehiclesSIPP() {
        Gson gson = new Gson();
        return gson.toJson(getVehicleSIPPs(list));
    }

    @Path("/getVehiclesRating")
    @GET
    public String getVehiclesScore() {
        Gson gson = new Gson();
        return gson.toJson(getVehicleRatings(list));
    }

    @Path("/getVehiclesScore")
    @GET
    public String getVehiclesRating() {
        Gson gson = new Gson();
        return gson.toJson(getVehicleScores(list));
    }

    // Get list of all vehicles in ascending price order
    public static ArrayList<VehiclePrice> getVehiclePrices(List<Vehicle> list){
        Gson gson = new Gson();
        ArrayList<VehiclePrice> priceComparisons = new ArrayList<VehiclePrice>();
        // Sort the list by price
        for(int i = 0; i < list.size(); i++)
            priceComparisons.add(new VehiclePrice(list.get(i).name, list.get(i).price));

        Collections.sort(priceComparisons, new CompareVehiclePrice());
        return priceComparisons;
    }


    // Map the SIPP values
    public static void mapSIPP(){
        sippMapType = new HashMap<String, String>();
        sippMapDoors = new HashMap<String, String>();
        sippMapTrans = new HashMap<String, String>();
        sippMapFuel = new HashMap<String, String>();

        // Car type
        sippMapType.put("M", "Mini");
        sippMapType.put("E","Economy");
        sippMapType.put("C","Compact");
        sippMapType.put("I","Intermediate");
        sippMapType.put("S","Standard");
        sippMapType.put("F","Full Size");
        sippMapType.put("P","Premium");
        sippMapType.put("L","Luxury");
        sippMapType.put("X","Special");

        // Doors (car type)
        sippMapDoors.put("B","2 doors");
        sippMapDoors.put("C","4 doors");
        sippMapDoors.put("D","5 doors");
        sippMapDoors.put("W","Estate");
        sippMapDoors.put("T","Convertible");
        sippMapDoors.put("F","SUV");
        sippMapDoors.put("P","Pick Up");
        sippMapDoors.put("V","Passenger Van");

        // Transmission
        sippMapTrans.put("M","Manual");
        sippMapTrans.put("A","Automatic");

        // Fuel and AC
        sippMapFuel.put("N","Petrol/no AC");
        sippMapFuel.put("R","Petrol/AC");
    }

    // Get list of specifications of all vehicles based on their SIPP values
    public static ArrayList<VehicleSIPP> getVehicleSIPPs(List<Vehicle> list){
        ArrayList<VehicleSIPP> vehicleSIPPS = new ArrayList<VehicleSIPP>();
        for(int i = 0; i < list.size(); i++){

            // Get sipp
            String sipp = list.get(i).sipp;

            // Get name
            String name = list.get(i).name;

            // Get sipp characters
            String type = Character.toString(sipp.charAt(0));
            String doors = Character.toString(sipp.charAt(1));
            String trans = Character.toString(sipp.charAt(2));
            String fuelAC = Character.toString(sipp.charAt(3));

            // Get sipp mapping
            type = sippMapType.get(type);
            doors = sippMapDoors.get(doors);
            trans = sippMapTrans.get(trans);
            fuelAC = sippMapFuel.get(fuelAC);

            // Get fuel and AC
            String fuel = fuelAC.substring(0, 6);
            String AC = fuelAC.substring(7, fuelAC.length());

            // Vehicle Type
            VehicleSIPP vehicle = new VehicleSIPP(name, sipp, type, doors, trans, fuel, AC);

            vehicleSIPPS.add(vehicle);
        }

        return vehicleSIPPS;
    }

    // Get list of suppliers based on their highest ratings for a vehicle
    public static ArrayList<VehicleRating> getVehicleRatings(List<Vehicle> vehicles){
        ArrayList<VehicleRating> ratingList = new ArrayList<VehicleRating>();
        for (String type : sippMapType.keySet()) {
            // Get matching vehicles
            ArrayList<Vehicle> matchingVehicles = getCarType(type, vehicles);
            Collections.sort(matchingVehicles, new CompareVehicleRating());

            if(matchingVehicles.size() > 0) {
                Vehicle v = matchingVehicles.get(0); // Get top supplier
                // Create vehicle rating obj
                VehicleRating topRated = new VehicleRating(v.name, sippMapType.get(type), v.supplier, v.rating);
                ratingList.add(topRated);
            }

            Collections.sort(ratingList, new CompareVehicleBestRated());
        }
        return ratingList;
    }

    // Get all cars of a given a SIPP vehicle type key
    public static ArrayList<Vehicle> getCarType(String type, List<Vehicle> vehicles){
        // List of vehciles of the given car type
        ArrayList<Vehicle> matchingVehicles = new ArrayList<Vehicle>();

        for(int i = 0; i < vehicles.size(); i++){
            Vehicle vehicle = vehicles.get(i);
            String carType = vehicles.get(i).sipp.substring(0, 1);
            if(carType.equals(type)) {
                matchingVehicles.add(vehicle);
            }
        }
        return matchingVehicles;
    }

    /*
     * Give each vehicle a score based on tramission/aircon, a sum of scores (score+rating), and return list of sum of
     * scores in descending order.
     */
    public static ArrayList<VehicleScore> getVehicleScores(List<Vehicle> vehicles){
        ArrayList<VehicleScore> vehicleScores = new ArrayList<VehicleScore>();
        for(int i = 0; i < vehicles.size(); i++){
            Vehicle v = vehicles.get(i);
            int score = getScore(v.sipp);
            vehicleScores.add(new VehicleScore(v.name, score, v.rating));
        }
        Collections.sort(vehicleScores, new CompareVehicleScore());
        return vehicleScores;
    }

    // Get a score given a SIPPs keys
    public static int getScore(String sipp){
        String transmission = Character.toString(sipp.charAt(2));
        String fuelAC = Character.toString(sipp.charAt(3));
        int score = 0;
        if(transmission.equals("M"))
            score += 1;
        else if(transmission.equals("A"))
            score += 5;
        if(fuelAC.equals("R"))
            score += 2;
        return score;
    }

    // Read in json file form url
    public static String readUrl(String jsonUrl) throws Exception {
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        char[] chars = new char[2048];
        int read;
        try {
            URL url = new URL(jsonUrl);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
        } finally {
            if (reader != null) reader.close();
        }
        return buffer.toString();
    }

    public static void setVehicleList(ArrayList<Vehicle> vehicles){
        list = vehicles;
    }
}