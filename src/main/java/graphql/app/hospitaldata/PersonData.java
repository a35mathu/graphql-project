package graphql.app.hospitaldata;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class PersonData {
//added for dataLoader implementation

      Patient patient0 = new Patient(0, "Abby", "Baker", 11, 10.0, null);
      Patient patient1 = new Patient(1, "Arushi", "Mathur", 20, 5.2, null);
      Patient patient2 = new Patient(2, "David", "Beckham", 44, 10.0, null);
      Patient patient3 = new Patient(3, "Jack", "Black", 50, 10.0, null);
      Patient patient4 = new Patient(4, "Thomas", "Beckley", 21, 10.0, null);
      Patient patient5 = new Patient(5, "Emma", "Thorn", 93, 10.0, null);


      private List<Patient> patientsList1 = Arrays.asList(patient0, patient1, patient2);
      private List<Patient> patientsList2 = Arrays.asList(patient3, patient4, patient5);

     Nurse nurse1 = new Nurse(6, "Jennifer", "Anniston", patientsList1);
     Nurse nurse2 = new Nurse(7, "Leslie", "Blue", patientsList2);

     {
        patient0.setNurse(nurse1);
        patient1.setNurse(nurse1);
        patient2.setNurse(nurse1);
        patient3.setNurse(nurse2);
        patient4.setNurse(nurse2);
        patient5.setNurse(nurse2);
    }

public Map<Integer, Patient> patientsData = new LinkedHashMap<>();
{
    patientsData.put(0, patient0);
    patientsData.put(1, patient1);
    patientsData.put(2, patient2);
    patientsData.put(3, patient3);
    patientsData.put(4, patient4);
    patientsData.put(5, patient5);
}
 
    public Patient getPatientData(String id){
        int realID = Integer.parseInt(id);
        if(patientsData.get(realID) != null){
            System.out.println("GETTING HERE!!!");
            System.out.println(patientsData.get(realID).getFirstName());
            return patientsData.get(realID);
        }
        return null;
    }
    
}