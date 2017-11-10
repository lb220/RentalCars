import java.util.ArrayList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestMain {
    static ArrayList<Vehicle> list;
    static ArrayList<Vehicle> listEmpty;
    static Vehicle v1;
    static Vehicle v2;
    static Vehicle v3;

    @BeforeAll
    static void setUp(){
        Main.mapSIPP();
        listEmpty = new ArrayList<Vehicle>();
        list = new ArrayList<Vehicle>();
        v1 = new Vehicle("ECAR", "VW Golf", 123.45f, "Enterprise", 5f);
        v2 = new Vehicle("ECMN", "Citroen C4", 123.23f, "Enterprise", 3f);
        v3 = new Vehicle("MBMN", "Ford Mondeo", 155.67f, "RentalCars", 10f);
        list.add(v1);
        list.add(v2);
        list.add(v3);
    }

    // Test multiple vehicles are correctly sorted in ascending price order
    @Test
    public void testVehiclesPriceHighest() {
        ArrayList<VehiclePrice> sortedByPrice = Main.getVehiclePrices(list);
        assertEquals(sortedByPrice.get(0).price, list.get(1).price);
        assertEquals(sortedByPrice.get(1).price, list.get(0).price);
        assertEquals(sortedByPrice.get(2).price, list.get(2).price);
    }

    // Test empty vehicle list returns an empty list of vehicle prices
    @Test
    public void testVehiclesPriceHighestEmpty() {
        ArrayList<VehiclePrice> sortedByPrice = Main.getVehiclePrices(listEmpty);
        assertEquals(sortedByPrice.size(), 0);
    }

    // Test get vehicle car type given SIPPs value
    @Test
    public void testVehicleType(){
        ArrayList<VehicleSIPP> sipps = Main.getVehicleSIPPs(list);
        assertEquals(sipps.get(0).type, "Economy");

    }

    // Test get vehicle doors given SIPPs value
    @Test
    public void testVehicleDoors(){
        ArrayList<VehicleSIPP> sipps = Main.getVehicleSIPPs(list);
        assertEquals(sipps.get(1).doors, "4 doors");
    }

    // Test get vehicle transmission given SIPPs value
    @Test
    public void testVehicleTransmission(){
        ArrayList<VehicleSIPP> sipps = Main.getVehicleSIPPs(list);
        assertEquals(sipps.get(2).transmission, "Manual");
    }

    // Test get vehicle fuel given SIPPs value
    @Test
    public void testVehicleFuel(){
        ArrayList<VehicleSIPP> sipps = Main.getVehicleSIPPs(list);
        assertEquals(sipps.get(0).fuel, "Petrol");

    }

    // Test get vehicle air-conditioning given SIPPs value
    @Test
    public void testVehicleAC(){
        ArrayList<VehicleSIPP> sipps = Main.getVehicleSIPPs(list);
        assertEquals(sipps.get(0).airCon, "AC");
    }

    // Test empty vehicle list returns en empty list of vehicle sipps
    @Test
    public void testVehicleSIPPempty(){
        ArrayList<VehicleSIPP> sipps = Main.getVehicleSIPPs(listEmpty);
        assertEquals(sipps.size(), 0);
    }

    // Test get size of rated vehicles list
    @Test
    public void testVehicleRatingSize(){
        ArrayList<VehicleRating> rating = Main.getVehicleRatings(list);
        assertEquals(rating.size(), 2);
    }

    // Test get vehicles by rating in descending order
    @Test
    public void testVehicleRating(){
        ArrayList<VehicleRating> rating = Main.getVehicleRatings(list);
        assertEquals(rating.get(0).name, list.get(2).name);
        assertEquals(rating.get(1).name, list.get(1).name);
    }

    // Test empty vehicle list returns an empty list of vehicle ratings
    @Test
    public void testVehicleRatingEmpty(){
        ArrayList<VehicleRating> rating = Main.getVehicleRatings(listEmpty);
        assertEquals(rating.size(), 0);
    }

    // Test vehicles are given the correct scores
    @Test
    public void testVehicleScores(){
        ArrayList<VehicleScore> scores = Main.getVehicleScores(list);
        assertEquals(scores.get(0).score, 7);
        assertEquals(scores.get(1).score, 1);
        assertEquals(scores.get(2).score, 1);
    }

    // Test vehicles are given the ordering based on their scores + rating
    @Test
    public void testVehicleSumScores(){
        ArrayList<VehicleScore> scores = Main.getVehicleScores(list);
        assertEquals(scores.get(0).name, "VW Golf");
        assertEquals(scores.get(1).name, "Ford Mondeo");
        assertEquals(scores.get(2).name, "Citroen C4");
    }

    // Test vehicles are given the correct scores
    @Test
    public void testVehicleSumScoresOrder(){
        ArrayList<VehicleScore> scores = Main.getVehicleScores(list);
        assertEquals(scores.get(0).sumScores, 12);
        assertEquals(scores.get(1).sumScores, 11);
        assertEquals(scores.get(2).sumScores, 4);
    }

    // Test empty vehicle list returns empty vehicle score list
    @Test
    public void testVehicleScoreEmpty(){
        ArrayList<VehicleScore> scores = Main.getVehicleScores(listEmpty);
        assertEquals(scores.size(), 0);
    }

    // Test get score given SIPP value
    @Test
    public void testScoreSipp(){
        int score1 = Main.getScore("PBAN");
        int score2 = Main.getScore("XLMR");
        assertEquals(score1, 5);
        assertEquals(score2, 3);
    }

    // Test get all vehicles in a list of a given type
    @Test
    public void getVehicleGivenType(){
        ArrayList<Vehicle> vehicles = Main.getCarType("E", list);
        assertEquals(vehicles.size(), 2);
        assertTrue(vehicles.contains(v1));
        assertTrue(vehicles.contains(v2));
    }


}