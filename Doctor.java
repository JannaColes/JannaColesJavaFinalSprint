
public class Doctor extends User {
    private String medicalLicenseNumber;
    private String specialization;

    // Updated constructor
    public Doctor(int id, String firstName, String lastName, String email, String password, boolean isDoctor, String medicalLicenseNumber, String specialization) {
        super(id, firstName, lastName, email, password, isDoctor);  // Assuming User has a constructor that includes the password
        this.medicalLicenseNumber = medicalLicenseNumber;
        this.specialization = specialization;
    }

    // Getters and setters
    public String getMedicalLicenseNumber() {
        return medicalLicenseNumber;
    }

    public void setMedicalLicenseNumber(String medicalLicenseNumber) {
        this.medicalLicenseNumber = medicalLicenseNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    // Optionally override the toString method for debugging/output purposes
    @Override
    public String toString() {
        return super.toString() + ", Medical License: " + medicalLicenseNumber + ", Specialization: " + specialization;
    }
}