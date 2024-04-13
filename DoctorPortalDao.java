import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DoctorPortalDao {
    private UserDao userDao;
    private HealthDataDao healthDataDao;

   
    public DoctorPortalDao() {
        userDao = new UserDao();
        healthDataDao = new HealthDataDao();
    }


    /**
     * Retrieves a doctor based on the provided doctor ID.
     * @param doctorId The ID of the doctor
     * @return The doctor object
     */
    
    public Doctor getDoctorById(int doctorId) {
        String sql = "SELECT * FROM users WHERE id = ? AND is_doctor = true";
        Doctor doctor = null;
        try (Connection connection = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, doctorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                doctor = new Doctor(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getBoolean("is_doctor"),
                        resultSet.getString("medical_license_number"),
                        resultSet.getString("specialization")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctor;
        
    }

    /**
     * Retrieves a doctor based on the provided patient ID.
     * 
     * @param patientId  The ID of the patient
     * @return The doctor associated with the patient
     */

     public Doctor getDoctorByPatientId(int patientId) {
        String sql = """
            SELECT u.id, u.first_name, u.last_name, u.email, u.password, d.medical_license_number, d.specialization 
            FROM users u 
            INNER JOIN doctor_patient dp ON u.id = dp.doctor_id 
            INNER JOIN doctors d ON u.id = d.user_id 
            WHERE dp.patient_id = ?
            """;
    
        try (Connection connection = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, patientId);
    
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Doctor doctor = new Doctor(
                        resultSet.getInt("id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),  // Ensure this is securely handled
                        true,  // Assuming the fetched user must be a doctor
                        resultSet.getString("medical_license_number"),
                        resultSet.getString("specialization")
                    );
                    return doctor;
                } else {
                    System.out.println("No doctor assigned to this patient.");
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
    
    
    
                            

/**
 * Retrieves a list of patients associated with a doctor based on the doctor's ID.
 * 
 * @param doctorId The ID of the doctor
 * @return A list of patients associated with the doctor
 */

    public List<User> getPatientsByDoctorId(int doctorId) {
        List<User> patients = new ArrayList<>();
        String sql = "SELECT u.* FROM users u INNER JOIN doctor_patient dp ON u.id = dp.patient_id WHERE dp.doctor_id = ?";
        
        try (Connection connection = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, doctorId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    User patient = new User(resultSet.getInt("id"),
                            resultSet.getString("first_name"),
                            resultSet.getString("last_name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getBoolean("is_doctor"));
                    patients.add(patient);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } 
        return patients;
    }

    /**
     * Retrieves a list of health data records for a patient based on the patient's ID.
     * 
     * @param patientId The ID of the patient
     * @return A list of health data records for the patient
     */

    public List<HealthData> getHealthDataByPatientId(int patientId) {
        return healthDataDao.getHealthDataByUserId(patientId);
    }

    /**
     * Inserts a patient into the doctor_patient table to establish a connection between a doctor and a patient.
     * 
     * @param patientId the ID of the patient to be assigned to a doctor
     * @param doctorId the ID of the doctor to be assigned to the patient
     * @return true if the patient was successfully assigned to the doctor, if not it would be false
     */
    
     public boolean assignPatientToDoctor(int patientId, int doctorId) {
        String sql = "INSERT INTO doctor_patient (doctor_id, patient_id) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setInt(2, patientId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

   /**
    * Removes a patient from the doctor_patient table to remove the connection between a doctor and a patient.
    *
    * @param patientId The ID of the patient to be removed from the doctor's list of patients
    * @param doctorId The ID of the doctor to remove the patient from
    * @return true if the patient was successfully removed from the doctor's list of patients, if not it would be false
    */

    public boolean removePatientFromDoctor(int patientId, int doctorId) {
        String sql = "DELETE FROM doctor_patient WHERE doctor_id = ? AND patient_id = ?";
        try (Connection connection = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, doctorId);
            preparedStatement.setInt(2, patientId);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

