
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.time.LocalDate;

public class HealthDataDao {
 /**
  * Creates a new health data entry in the database.
  *
  * @param healthData The health data object to be created
  * @return true if the health data was created successfully, false otherwise
  */

  public boolean createHealthData(HealthData healthData) {
    String sql = "INSERT INTO health_data (user_id, weight, height, steps, heart_rate, date) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection connection = DatabaseConnection.getCon();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setInt(1, healthData.getUserId());
        preparedStatement.setDouble(2, healthData.getWeight());
        preparedStatement.setDouble(3, healthData.getHeight());
        preparedStatement.setInt(4, healthData.getSteps());
        preparedStatement.setInt(5, healthData.getHeartRate());
        preparedStatement.setDate(6, java.sql.Date.valueOf(healthData.getDate()));  // Ensure this conversion is correct

        int updatedRows = preparedStatement.executeUpdate();
        if (updatedRows != 0) {
            System.out.println("Health data added successfully!");
            return true;
        }
    } catch (SQLException e) {
        System.err.println("SQL Error: " + e.getMessage());
        e.printStackTrace();
    }
    System.out.println("An error occurred while adding health data.");
    return false;
}


  
         

    /**
     * Retrieves a list of health data records for a user based on the user's ID.
     * 
     * @param userId The ID of the user
     * @return A list of health data records for the user
     */

     public List<HealthData> getHealthDataByUserId(int userId) {
        List<HealthData> healthDataList = new ArrayList<>();
        String sql = "SELECT id, user_id, weight, height, steps, heart_rate, date FROM health_data WHERE user_id = ?";
        try (Connection connection = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int fetchedUserId = resultSet.getInt("user_id");
                double weight = resultSet.getDouble("weight");
                double height = resultSet.getDouble("height");
                int steps = resultSet.getInt("steps");
                int heartRate = resultSet.getInt("heart_rate");
                Date date = resultSet.getDate("date");
    System.out.println("Your Health Data Fetched Directly From ResultSet - ID: " + id + ", User ID: " + fetchedUserId +
                                   ", Weight: " + weight + ", Height: " + height + ", Steps: " + steps +
                                   ", Heart Rate: " + heartRate + ", Date: " + (date != null ? date.toString() : "null"));
                
    
                // Create and add the health data object
                HealthData healthData = new HealthData(
                    id, fetchedUserId, weight, height, steps, heartRate,
                    date != null ? date.toString() : "No Date"
                );
                healthDataList.add(healthData);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return healthDataList;
    }
    
    

     /**
      * Updates an existing health data entry in the database.
      *
      * @param healthData The health data object to be updated
      * @return true if the health data was updated successfully, false otherwise
      */

      public boolean updateHealthData(HealthData healthData) {
        String sql = "UPDATE health_data SET weight = ?, height = ?, steps = ?, heart_rate = ?, date = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setDouble(1, healthData.getWeight());
            preparedStatement.setDouble(2, healthData.getHeight());
            preparedStatement.setInt(3, healthData.getSteps());
            preparedStatement.setInt(4, healthData.getHeartRate());
            preparedStatement.setString(5, healthData.getDate());
            preparedStatement.setInt(6, healthData.getId());
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows != 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
      }
    

    /**
     * Deletes a health data entry from the database.
     * 
     * @param id The ID of the health data entry to be deleted
     * @return true if the health data was deleted successfully, false otherwise
     */

      public boolean deleteHealthData(int id) {
          String sql = "DELETE FROM health_data WHERE id = ?";
          try (Connection connection = DatabaseConnection.getCon();
                PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
              preparedStatement.setInt(1, id);
              int updatedRows = preparedStatement.executeUpdate();
              if (updatedRows != 0) {
                  return true;
              }
          } catch (SQLException e) {
              e.printStackTrace();
              return false;
          }
          return false;
      }
    }