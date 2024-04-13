import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;


public class HealthMonitoringApp {
    private static final UserDao userDao = new UserDao();
    private static final MedicineReminderManager medicineReminderManager = new MedicineReminderManager();
    private static final DoctorPortalDao doctorPortalDao = new DoctorPortalDao();
    private static final HealthDataDao healthDataDao = new HealthDataDao();
    private static final RecommendationSystem recommendationSystem = new RecommendationSystem();
    private static final Scanner scanner = new Scanner(System.in);
    private static User currentUser;
    /**
     * Test the following functionalities within the Main Application
     *  1. Register a new user
     *  2. Log in the user
     *  3. Add health data
     *  4. Generate recommendations
     *  5. Add a medicine reminder
     *  6. Get reminders for a specific user
     *  7. Get due reminders for a specific user
     *  8. test doctor portal
     */
    public static void main(String[] args) {
       boolean running = true;

       while (running) {
        System.out.println("\nWelcome to the Health Monitoring System!");
        System.out.println("1. Register a new user");
        System.out.println("2. Login user");
        System.out.println("3. Exit");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1 -> registerUser();
              
            case 2 -> loginUser();
               
            case 3 -> {
                System.out.println("Exiting the Health Monitoring System. Goodbye!");
                running = false;
            }
               
            default ->
                System.out.println("Invalid choice. Please try again.");
        }
        if (currentUser != null) {
            if (currentUser.isDoctor()) {
                displayDoctorMenu();
            } else {
                displayPatientMenu();
            }
        }
       }
    }


    private static void registerUser() {
        System.out.println("\nRegister a new user");
        System.out.print("Enter your first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter your last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
        System.out.print("Are you a doctor? (yes/no): ");
        String doctorInput = scanner.nextLine();
        boolean isDoctor = doctorInput.trim().equalsIgnoreCase("yes") || doctorInput.trim().equalsIgnoreCase("y");
    
        User newUser = new User(0, firstName, lastName, email, password, isDoctor);
        boolean success = userDao.createUser(newUser);
    
        if (success) {
            System.out.println("User registered successfully!");
        } else {
            System.out.println("An error occurred. Please try again.");
        }
    }
    


    private static void loginUser() {
        System.out.println("\nLogin user");
        System.out.print("Enter your email: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();
    
        User user = userDao.getUserByEmail(email);
        if (user != null && userDao.verifyPassword(email, password)) {
            System.out.println("Login successful!");
            currentUser = user;
            displayUserMenu(); // Display the appropriate menu based on the user type
        } else {
            System.out.println("Incorrect email or password. Please try again.");
            currentUser = null; // Important to ensure that no session is created
        }
    }
    
    private static void displayUserMenu() {
        if (currentUser == null) {
            return; // Do not proceed if no user is logged in
        }
    
        if (currentUser.isDoctor()) {
            displayDoctorMenu();
        } else {
            displayPatientMenu();
        }
    }
    
    /**
     * 
     */
    private static void displayDoctorMenu() {
        boolean doctorMenuActive = true;
        while (doctorMenuActive) {
            System.out.println("\nDoctor Menu");
            System.out.println("1. View patients");
            System.out.println("2. Assign patient to doctor");
            System.out.println("3. Remove patient from doctor");
            System.out.println("4. Access patient health data");
            System.out.println("5. Send health recommendations");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            int doctorChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            switch (doctorChoice) {
                case 1 -> viewPatientsList();
                case 2 -> assignPatientToDoctor();
                case 3 -> removePatientFromDoctor();
                case 4 -> accessPatientHealthData();
                case 5 -> sendHealthRecommendations();
                case 6 -> {
                    System.out.println("Exiting the Doctor Menu.");
                    doctorMenuActive = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    


    private static void displayPatientMenu() {
        boolean patientMenuActive = true;
        while (patientMenuActive) {
            System.out.println("\nPatient Menu");
            System.out.println("1. View health data");
            System.out.println("2. Add Health Data");
            System.out.println("3. View Recommendations");
            System.out.println("4. Get reminders");
            System.out.println("5. Get due reminders");
            System.out.println("6. Add medicine reminder");
            System.out.println("7. Delete medicine reminder");
            System.out.println("8. View doctor details");
            System.out.println("9. Exit");
            System.out.print("Enter your choice: ");
            int patientChoice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
    
            switch (patientChoice) {
                case 1 -> viewHealthData();
                case 2 -> addHealthData();
                case 3 -> viewRecommendations();
                case 4 -> getRemindersForUser();
                case 5 -> getDueRemindersForUser();
                case 6 -> addMedicineReminder();
                case 7 -> deleteMedicineReminder();
                case 8 -> viewDoctorDetails();
                case 9 -> {
                    System.out.println("Exiting the Patient Menu.");
                    patientMenuActive = false;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    
    
        
        private static void addHealthData() {
            System.out.println("Add health data");
        
            System.out.print("Enter weight: ");
            double weight = scanner.nextDouble();
            System.out.print("Enter height: ");
            double height = scanner.nextDouble();
            System.out.print("Enter steps: ");
            int steps = scanner.nextInt();
            System.out.print("Enter heart rate: ");
            int heartRate = scanner.nextInt();
            scanner.nextLine();  // Clear the scanner buffer after reading numbers
        
            System.out.print("Enter date (yyyy-mm-dd): ");
            String dateString = scanner.nextLine().trim();
        
            System.out.println("You entered date: '" + dateString + "'");
            if (!dateString.isEmpty()) {
                try {
                    LocalDate date = LocalDate.parse(dateString);  // Parse the date
                    java.sql.Date sqlDate = java.sql.Date.valueOf(date);  // Convert to sql date
                    HealthData healthData = new HealthData(0, currentUser.getId(), weight, height, steps, heartRate, sqlDate);
                    boolean success = healthDataDao.createHealthData(healthData);
        
                    if (success) {
                        System.out.println("Health data added successfully!");
                    } else {
                        System.out.println("An error occurred while adding health data.");
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format. Please enter the date in yyyy-mm-dd format.");
                }
            } else {
                System.out.println("Date field cannot be empty. Please enter a valid date.");
            }
        }
        
        
            
        private static void viewRecommendations() {
            if (currentUser == null || currentUser.isDoctor()) {
                System.out.println("You do not have access to this feature.");
                return;
            }
            System.out.println("View recommendations");

            List<String> recommendations = recommendationSystem.getRecommendationsForUser(currentUser.getId());
            if (recommendations.isEmpty()) {
                System.out.println("No recommendations available.");
            } else {
                for (String recommendation : recommendations) {
                    System.out.println(recommendation);
                }
            }
        }

        private static void addMedicineReminder() {
            System.out.println("Add a medicine reminder");

        System.out.print("Enter the user ID: ");
        int userId = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Enter the medicine name: ");
        String medicineName = scanner.nextLine();
        System.out.print("Enter the dosage: ");
        String dosage = scanner.nextLine();
        System.out.print("Enter the schedule: ");
        String schedule = scanner.nextLine();
        System.out.print("Enter the start date (yyyy-mm-dd): ");
        String startDate = scanner.next();
        System.out.print("Enter the end date (yyyy-mm-dd): ");
        String endDate = scanner.next();

        if (endDate.isEmpty()) {
            endDate = null;
        }

        try {
            MedicineReminder reminder = new MedicineReminder(0, userId, medicineName, dosage, schedule, startDate, endDate);
            MedicineReminder newReminder = reminder; // Initialize newReminder variable
            medicineReminderManager.addReminder(newReminder);
            System.out.println("Medicine reminder added successfully!");
        }  catch (Exception e) {
            System.out.println("An error occurred. Please try again.");
        }
    }

    private static void getRemindersForUser() {
        int userId;
        if (currentUser.isDoctor()) {
            System.out.print("Enter the user ID: ");
            userId = scanner.nextInt();
        } else {
            userId = currentUser.getId();
        }

        List<MedicineReminder> reminders = medicineReminderManager.getRemindersForUser(userId);
        if (reminders.isEmpty()) {
            System.out.println("No reminders available.");
        } else {
            for (MedicineReminder reminder : reminders) {
                System.out.println("Reminder: " + reminder.getMedicineName());
            }
        }
    }

    private static void getDueRemindersForUser() {
        int userId;
        if (currentUser.isDoctor()) {
            System.out.print("Enter the user ID: ");
            userId = scanner.nextInt();
        } else {
            userId = currentUser.getId();
        }

        List<MedicineReminder> dueReminders = medicineReminderManager.getDueReminders(userId);
        if (dueReminders.isEmpty()) {
            System.out.println("No due reminders available.");
        } else {
            for (MedicineReminder reminder : dueReminders) {
                System.out.println("Due reminder: " + reminder.getMedicineName());
            }
        }
    }

    private static void viewPatientsList() {
        if (currentUser == null || !currentUser.isDoctor()) {
            System.out.println("You do not have access to this feature.");
            return;
        }

        System.out.println("View patients " + currentUser.getLastName() + "...");
        try {
            List<User> patients =
                    doctorPortalDao.getPatientsByDoctorId(currentUser.getId());
            if (patients.isEmpty()) {
                System.out.println("No patients available.");
            } else {
                System.out.println("List of Patients: ");
                for (User patient : patients) {
                    System.out.println("ID: " + patient.getId() + ", Name: " + patient.getFirstName() + " " + patient.getLastName() + ", Email: " + patient.getEmail());
                }
                }
            } catch (Exception e) {
                System.out.println("An error occurred. Please try again.");
            }
        }

    private static void assignPatientToDoctor() {
        System.out.println("Assign patient to doctor " + currentUser.getLastName() + "...");
        System.out.print("Enter the patient ID: ");
        int patientId = scanner.nextInt();
        boolean success = doctorPortalDao.assignPatientToDoctor(patientId, currentUser.getId());
        if (success) {
            System.out.println("Patient assigned to doctor successfully!");
        } else {
            System.out.println("An error occurred. Please try again.");
        }
        }

        private static void removePatientFromDoctor() {
            System.out.println("Remove a patient from Doctor " + currentUser.getLastName());
            System.out.print("Enter the patient ID: ");
            int patientId = scanner.nextInt();
            boolean success = doctorPortalDao.removePatientFromDoctor(patientId, currentUser.getId());
            if (success) {
                System.out.println("Patient removed from doctor successfully!");
            } else {
                System.out.println("An error occurred. Please try again.");
            }
        }

        private static void accessPatientHealthData() {
            System.out.println("Enter patient Id to view health data: ");
            int patientId = scanner.nextInt();
            scanner.nextLine();

            List<HealthData> healthDataList = healthDataDao.getHealthDataByUserId(patientId);
            if (healthDataList.isEmpty()) {
                System.out.println("No health data available.");
            } else {
                for (HealthData healthData : healthDataList) {
                    char[] data = new char[0]; // Initialize the data variable with an empty array
                    System.out.println(data);
                }
            }
        }

        private static void sendHealthRecommendations() {
            System.out.println("Enter patient ID to send recommendations: ");
            int patientId = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            System.out.println("Enter the recommendation: ");
            String recommendation = scanner.nextLine();
        
            boolean success = recommendationSystem.saveRecommendation(patientId, recommendation);
            if (success) {
                System.out.println("Recommendation sent successfully!");
            } else {
                System.out.println("An error occurred. Please try again.");
            }
        }

        /**
         * 
         */
        private static void viewHealthData() {
    if (currentUser == null) {
        System.out.println("You must be logged in to view this information.");
        return;
    }

    List<HealthData> healthDataList = healthDataDao.getHealthDataByUserId(currentUser.getId());
    if (healthDataList.isEmpty()) {
        System.out.println("No health data available.");
    } else {
        System.out.println("Your Health Data:");
        
    }
}

        
        
        
        

private static void viewDoctorDetails() {
    // Assuming the current user is a patient and you want to fetch their assigned doctor
    Doctor doctor = doctorPortalDao.getDoctorByPatientId(currentUser.getId());
    if (doctor == null) {
        System.out.println("No doctor assigned to you.");
    } else {
        System.out.println("Your Doctor Details:");
        System.out.println("Name: " + doctor.getFirstName());
        System.out.println("Specialization: " + doctor.getSpecialization());
        // Add more details as needed
    }
}


        private static void deleteMedicineReminder() {
            System.out.println("Enter the reminder ID to delete: ");
            int reminderId = scanner.nextInt();
            boolean success = medicineReminderManager.deleteReminder(reminderId);
            if (success) {
                System.out.println("Reminder deleted successfully!");
            } else {
                System.out.println("An error occurred. Please try again.");
            }
        }
    }