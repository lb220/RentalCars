import java.util.Comparator;

class CompareVehiclePrice implements Comparator<VehiclePrice> {
    public int compare(VehiclePrice v1, VehiclePrice v2) {
        if(v1.price > v2.price) return 1;
        else if(v2.price > v1.price) return -1;
        else return 0;
    }
}