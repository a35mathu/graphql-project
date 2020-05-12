package graphql.app.hospitaldata;

import java.util.ArrayList;
import java.util.List;

//data class
public final class Nurse {
    private int id;
    private String firstName;
    private String lastName;
    private List<Patient> patients;
   
    public Nurse(int id, String firstName, String lastName, List<Patient> patients) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patients = patients;
    }

    public List<Object> getPatientIDs(){
        List<Object> patientIDs = new ArrayList<>();
        this.patients.forEach(patient -> patientIDs.add(String.valueOf(patient.getId())));

        return patientIDs;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }
}