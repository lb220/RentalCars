import java.util.Comparator;

class CompareVehicleRating implements Comparator<Vehicle> {
    public int compare(Vehicle v1, Vehicle v2) {
        if(v1.rating > v2.rating) return 1;
        else if(v2.rating > v1.rating) return -1;
        else return 0;
    }
}

class CompareVehicleBestRated implements Comparator<VehicleRating>{
    public int compare(VehicleRating v1, VehicleRating v2){
        if(v1.rating < v2.rating) return 1;
        else if(v2.rating < v1.rating) return -1;
        else return 0;
    }
}