import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;


public class UserDaoExample {
    public static boolean createUser(User user) {
        boolean bool = false;
        /* insert user into database */
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        // Prepare the SQL query
        String query = "INSERT INTO users (first_name, last_name, email, password, is_doctor) " +
                "VALUES (?, ?, ?, ?, ?)";

        // Database logic to insert data using PREPARED Statement
        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, hashedPassword);
            statement.setBoolean(5, user.isDoctor());
            int updatedRows = statement.executeUpdate();
            if (updatedRows != 0) {
                bool = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bool;
    }

    public User getUserById(int id) { /* get user by id from database */
        int user_id = 0;
        String firstName = null;
        String lastName = null;
        String email = null;
        String password = null;
        boolean is_doctor = false;

        // Prepare the SQL query
        String query = "SELECT * FROM users WHERE id = ?";

        // Database logic to get data by ID Using Prepared Statement
        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                user_id = rs.getInt("id");
                firstName = rs.getString("first_name");
                lastName = rs.getString("last_name");
                email = rs.getString("email");
                password = rs.getString("password");
                is_doctor = rs.getBoolean("is_doctor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new User(user_id, firstName, lastName, email, password, is_doctor);
    }

    public static User getUserByEmail(String email) { /* get user by email from database */
        int id = 0;
        String firstName = null;
        String lastName = null;
        String user_email = null;
        String password = null;
        boolean is_doctor = false;

        // Prepare the SQL query
        String query = "SELECT * FROM users WHERE email = ?";

        // Database logic to get data by ID Using Prepared Statement
        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getInt("id");
                firstName = rs.getString("first_name");
                lastName = rs.getString("last_name");
                user_email = rs.getString("email");
                password = rs.getString("password");
                is_doctor = rs.getBoolean("is_doctor");
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return new User(id, firstName, lastName, user_email, password, is_doctor);
    }


    public boolean updateUser(User user) {
        boolean bool = false;
        // Prepare the SQL query
        String query = "UPDATE users " +
                "SET first_name = ?, last_name = ?, email = ?, password = ?, is_doctor = ? " +
                "WHERE id = ?";
        String hashedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        // Database logic to get update user Using Prepared Statement
        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, user.getFirstName());
            statement.setString(2, user.getLastName());
            statement.setString(3, user.getEmail());
            statement.setString(4, hashedPassword);
            statement.setBoolean(5, user.isDoctor());
            statement.setInt(6, user.getId());
            int updatedRows = statement.executeUpdate();
            if (updatedRows != 0) {
                bool = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bool;
    }

    public boolean deleteUser(int id) { /* delete user from the database */
        boolean bool = false;
        // Prepare the SQL query
        String query = "DELETE FROM users WHERE id = ?";

        // Database logic to delete user
        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, id);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated != 0){
                bool = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bool;
    }

    public static boolean verifyPassword (String email, String password){
        boolean bool = false;
        String query = "SELECT password FROM users WHERE email = ?";
        //Implement logic to retrieve password using the Bcrypt
        try {
            Connection con = DatabaseConnection.getCon();
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            String hashedPassword = null;
            while (rs.next()) {
                hashedPassword = rs.getString("password");
            }
            if (BCrypt.checkpw(password, hashedPassword)) {
                bool = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bool;
    }

}