
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * The MedicineReminderManager class should have methods to add reminders, get reminders
 *  1. for a specific user, and
 *  2. get reminders that are DUE for a specific user.
 *
 *  You'll need to integrate this class with your application and database logic to
 *  1. store,
 *  2. update, and
 *  3. delete reminders as needed.
 */

 public class MedicineReminderManager {
    private List<MedicineReminder> reminders;

    public MedicineReminderManager() {
        this.reminders = new ArrayList<>();
    }

    public void addReminder(MedicineReminder reminder) {
        String sql = "INSERT INTO medicine_reminders (user_id, medicine_name, dosage, schedule, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, reminder.getUserId());
            preparedStatement.setString(2, reminder.getMedicineName());
            preparedStatement.setString(3, reminder.getDosage());
            preparedStatement.setString(4, reminder.getSchedule());
            preparedStatement.setDate(5, java.sql.Date.valueOf(reminder.getStartDate()));

            if (reminder.getEndDate() != null && !reminder.getEndDate().isEmpty()) {
                preparedStatement.setDate(6, java.sql.Date.valueOf(reminder.getEndDate()));
            } else {
                preparedStatement.setNull(6, java.sql.Types.DATE);
            }
    
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows > 0) {
                System.out.println("Medicine reminder added successfully!");
            } else {
                System.out.println("Failed to add the medicine reminder.");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    
    public List<MedicineReminder> getRemindersForUser(int userId) {
        List<MedicineReminder> userReminders = new ArrayList<>();
        String sql = "SELECT * FROM medicine_reminders WHERE user_id = ?";

        try (Connection connection = DatabaseConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    MedicineReminder reminder = new MedicineReminder(resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getString("medicine_name"),
                            resultSet.getString("dosage"),
                            resultSet.getString("schedule"),
                            resultSet.getDate("start_date").toString(),
                            resultSet.getDate("end_date") != null ? resultSet.getDate("end_date").toString() : null
                    );
                    userReminders.add(reminder);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userReminders;
    }

    public List<MedicineReminder> getDueReminders(int userId) {
        List<MedicineReminder> dueReminders = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        for (MedicineReminder reminder : reminders) {
            if (reminder.getUserId() == userId && isDue(reminder, now)) {
                dueReminders.add(reminder);
            }
        }

        return dueReminders;
    }
/**
 * @param reminder
 * @param now
 * @return
 */
private boolean isDue(MedicineReminder reminder, LocalDateTime now) {
    LocalDate startDate = LocalDate.parse(reminder.getStartDate(), DateTimeFormatter.ISO_LOCAL_DATE);
    LocalDate endDate = reminder.getEndDate() != null ? LocalDate.parse(reminder.getEndDate(), DateTimeFormatter.ISO_LOCAL_DATE) : null;
    
    LocalDateTime starDateTime = startDate.atStartOfDay();
    LocalDateTime endDateTime = endDate != null ? endDate.atTime(12, 59, 59) : null;
   
    return (starDateTime.isBefore(now) || starDateTime.isEqual(now)) &&
        (endDateTime == null || endDateTime.isAfter(now));
}

public boolean deleteReminder(int reminderId) {
    String sql = "DELETE FROM medicine_reminders WHERE id = ?";

    try (Connection connection = DatabaseConnection.getCon();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setInt(1, reminderId);
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
