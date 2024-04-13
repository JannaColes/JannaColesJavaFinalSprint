import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
   
    public boolean createUser(User user) {
        // insert user into database 
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        String sql = "INSERT INTO users (first_name, last_name, email, password, is_doctor) " +
                "VALUES (?, ?, ?, ?, ?)";

                try (Connection connection = DatabaseConnection.getCon();
                     PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setString(1, user.getFirstName());
                    preparedStatement.setString(2, user.getLastName());
                    preparedStatement.setString(3, user.getEmail());
                    preparedStatement.setString(4, hashedPassword);
                    preparedStatement.setBoolean(5, user.isDoctor());
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
    
    public User getUserById(int id) { //get user by id from database 
        User user = null;
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection connection = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    user = new User(resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getBoolean("is_doctor"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public User getUserByEmail(String email) { // get user by email from database 
        User user = null;
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection connection = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    user = new User(resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getBoolean("is_doctor"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;

    }


    public boolean updateUser(User user) {
        // update user in the database
        String sql = "UPDATE users SET first_name = ?, last_name = ?, email = ?, password = ?, is_doctor = ? WHERE id = ?";
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        
        try (Connection connection = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setBoolean(5, user.isDoctor());
            preparedStatement.setInt(6, user.getId());
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
    public boolean deleteUser(int id) { // delete user from the database 
       String sql = "DELETE FROM users WHERE id = ?";
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

    public boolean verifyPassword (String email, String password)
    {
        String sql = "SELECT password FROM users WHERE email = ?";

        try (Connection connection = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String hashedPassword = resultSet.getString("password");
                    return BCrypt.checkpw(password, hashedPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
 public List<User> getPatientsByDoctorId(int doctorId) {
        List<User> patients = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE doctor_id = ? AND is_doctor = false";


        try (Connection connection = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, doctorId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User user = new User(resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getBoolean("is_doctor"));
                    patients.add(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }





 public boolean assignPatientToDoctor(int patientId, int doctorId) {
       String sql = "UPDATE users SET doctor_id = ? WHERE id = ? AND is_doctor = false";

       try (Connection connection = DatabaseConnection.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
           preparedStatement.setInt(1, doctorId);
           preparedStatement.setInt(2, patientId);
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
