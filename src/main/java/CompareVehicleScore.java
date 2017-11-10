import java.util.Comparator;

class CompareVehicleScore implements Comparator<VehicleScore> {
    public int compare(VehicleScore v1, VehicleScore v2) {
        if(v1.sumScores < v2.sumScores) return 1;
        else if(v2.sumScores < v1.sumScores) return -1;
        else return 0;
    }
}