public class VehicleScore{
    String name;
    float score;
    float rating;
    float sumScores;

    VehicleScore(String name, float score, float rating){
        this.name = name;
        this.score = score;
        this.rating = rating;
        this.sumScores = score + rating;
    }
}