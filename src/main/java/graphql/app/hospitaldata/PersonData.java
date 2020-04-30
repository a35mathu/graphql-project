package graphql.app.hospitaldata;

import java.util.LinkedHashMap;
import java.util.Map;

public class PersonData {
//added for dataLoader implementation


     static Patient patient1 = new Patient(1, "Arushi", "Mathur", 5.2, new Nurse(7, "Leslie", "Blue"));
    //map of patient data --> key is id, value is patient
    public static Map<Integer, Patient> patientsData = new LinkedHashMap<>();
    static {
        patientsData.put(0, new Patient(0, "Abby", "Baker", 10.0, new Nurse(6, "Jennifer", "Anniston")));
        patientsData.put(1, new Patient(1, "Arushi", "Mathur", 5.2, new Nurse(7, "Leslie", "Blue")));
        patientsData.put(2, new Patient(2, "David", "Beckham", 10.0, new Nurse(8, "Daniel", "Hasslehoff")));
        patientsData.put(3, new Patient(3, "Jack", "Black", 10.0, new Nurse(9, "Tom", "Bowie")));
        patientsData.put(4, new Patient(4, "Thomas", "Beckley", 10.0, new Nurse(10, "Apple ", "Pie")));
        patientsData.put(5, new Patient(5, "Emma", "Thorn", 10.0, new Nurse(11, "Coconut", "Creme")));

    }

    public static Patient getPatientData(String id){
        int realID = Integer.parseInt(id);
        if(patientsData.get(realID) != null){
            System.out.println("GETTING HERE!!!");
            System.out.println(patientsData.get(realID).getFirstName());
            return patientsData.get(realID);
        }
        return null;
    }
    
}