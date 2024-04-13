

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * In this basic version of the
 * RecommendationSystem class, complete the generateRecommendations to take a
 * HealthData object as input and generates recommendations based on the user's heart rate and step count.
 * You can also expand this class to include more health data analysis and generate more specific
 * recommendations based on the user's unique health profile
 * NOTE:
 * To integrate this class into your application, you'll need to pass the HealthData object to the generateRecommendations method
 * and store the generated recommendations in the recommendations table in the database.
 */

public class RecommendationSystem {
    private static final int MIN_HEART_RATE = 60;
    private static final int MAX_HEART_RATE = 100;
    private static final int MIN_STEPS = 10000;
    private static final double CALORIE_INTAKE = 2000;
    private static final double CALORIE_BURNED = 1000;

    /**
     * @param healthData
     * @return
     */
    public List<String> generateRecommendations(HealthData healthData) {
        List<String> recommendations = new ArrayList<>();
                String sql = "SELECT recommendation_text FROM recommendations WHERE user_id = ?";
                try (Connection connection = DatabaseConnection.getCon();
                     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    int userId = 123; // Replace 123 with the actual user ID
                    preparedStatement.setInt(1, userId);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        recommendations.add(resultSet.getString("recommendation_text"));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }

        //        // Analyze heart rate
                int heartRate = healthData.getHeartRate();
        if (heartRate < MIN_HEART_RATE) {
            recommendations.add("Your heart rate is lower than the recommended range. " +
                    "Consider increasing your physical activity to improve your cardiovascular health.");
        }
//
//
//        // Analyze steps
        int steps = healthData.getSteps();
        if (steps < MIN_STEPS) {
            recommendations.add("You're not reaching the recommended daily step count. " +
                    "Try to incorporate more walking or other physical activities into your daily routine.");
        }
//
//        // Add more health data analysis and recommendations as needed

        return recommendations;
    }

    public boolean saveRecommendation(int patientId, String recommendation) {
        String sql = "INSERT INTO recommendations (user_id, recommendation_text, date) VALUES (?, ?, ?)";
        LocalDate currentDate = LocalDate.now();  // Get the current date
        try (Connection connection = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, patientId);
            preparedStatement.setString(2, recommendation);
            preparedStatement.setDate(3, java.sql.Date.valueOf(currentDate));  // Convert LocalDate to java.sql.Date
            int updatedRows = preparedStatement.executeUpdate();
            return updatedRows != 0;
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    

    public List<String> getRecommendationsForUser(int userId) {
        List<String> recommendations = new ArrayList<>();
        String sql = "SELECT recommendation_text FROM recommendations WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                recommendations.add(resultSet.getString("recommendation_text"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return recommendations;
    }
}    

