import java.sql.Date;

public class HealthData {
    private int id;
    private int userId;
    private double weight;
    private double height;
    private int steps;
    private int heartRate;
    private String date;
   


    // Constructor, getters, and setters

    /**
     * @param id
     * @param userId
     * @param weight
     * @param height
     * @param steps
     * @param heartRate
     * @param date2
    
     */
    public HealthData(int id, int userId, double weight, double height, int steps, int heartRate, Date date2) {
        this.id = id;
        this.userId = userId;
        this.weight = weight;
        this.height = height;
        this.steps = steps;
        this.heartRate = heartRate;
        this.date = date2.toString();
        

    }

    public HealthData(int id2, int userId2, double weight2, double height2, int steps2, int heartRate2, String date2) {
        
    }

    /**
     * @param int1
     * @param userId2
     * @param double1
     * @param double2
     * @param int2
     * @param int3
     * @param date2
     */
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

   


    @Override
    public String toString() {
        return "model.HealthData[" +
                "id=" + id +
                ", userId=" + userId +
                ", weight=" + weight +
                ", height=" + height +
                ", steps=" + steps +
                ", heartRate=" + heartRate +
                ", date='" + date + '\'' +
            
                ']';
    }
}
