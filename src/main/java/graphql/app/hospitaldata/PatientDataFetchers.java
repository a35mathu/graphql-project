package graphql.app.hospitaldata;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;

import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;
import graphql.schema.DataFetcher;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Context;

@Singleton
@Context
public class PatientDataFetchers implements GraphQLQueryResolver{

    // List<Patient> patients = new ArrayList<Patient>(
    //     Arrays.asList(
    //     new Patient(0, "Abby", "Baker", 10.0, new Nurse(6, "Jennifer", "Anniston")),
    //     new Patient(1, "Arushi", "Mathur", 5.2, new Nurse(7, "Leslie", "Blue")),  
    //     new Patient(2, "David", "Beckham", 10.0, new Nurse(8, "Daniel", "Hasslehoff")),  
    //     new Patient(3, "Jack", "Black", 10.0, new Nurse(9, "Tom", "Bowie")),  
    //     new Patient(4, "Thomas", "Beckley", 10.0, new Nurse(10, "Apple ", "Pie")),  
    //     new Patient(5, "Emma", "Thorn", 10.0, new Nurse(11, "Coconut", "Creme"))  
    // ));

    private List<Patient> getPatientsDataViaBatchHTTPApi(List<String> keys) {
        List<Patient> list = keys.stream().map(PersonData::getPatientData).collect(Collectors.toList());
        return list;
    }

    // private BatchLoader<String, Object> patientsBatchLoader = keys -> {//new BatchLoader<String, Object>(){
    //         // @Override public CompletionStage<List<Object>> load(List<String> keys){
    //             //build this method
    //             CompletableFuture<List<Object>> patientsPromise = CompletableFuture.supplyAsync(() -> getPatientsDataViaBatchHTTPApi(keys));
    //         // }
    //         return patientsPromise;
    //     };

        BatchLoader<String, Patient> patientsBatchLoader = new BatchLoader<String, Patient>() {
            @Override
            public CompletionStage<List<Patient>> load(List<String> keys) {
                //
                // we use supplyAsync() of values here for maximum parellisation
                //
                // return CompletableFuture.supplyAsync(() -> getPatientsDataViaBatchHTTPApi(keys));
                return CompletableFuture.supplyAsync(() -> {
                    // List<Patient> list = new ArrayList<>();
                    // list.addAll(PersonData.patientsData.values());
                    List<Patient> list = keys.stream().map(id -> PersonData.getPatientData(id)).collect(Collectors.toList());
                    
                    // System.out.println(PersonData.getPatientData(keys.get(0)));
                    // list.add(PersonData.getPatientData(keys.get(0)));
                    return list;
                });                
            }
        };

        // private DataLoader<String, Object> patientDataLoader = new DataLoader<String, Object>(patientsBatchLoader); 
        private DataLoader<String, Patient> patientDataLoader() {
            return new DataLoader<>(patientsBatchLoader); 
        }  


        //used for dataloader implementation
        public DataFetcher<CompletableFuture<List<Object>>> getPatientData() {
    
            return environment -> {
                List<Object> keys = (environment.getArgument("id"));
                // keys.add(id);
                // keys.add("2"); //get two patients
                for(int i = 0; i < keys.size(); i++){

                }
                return registry.getDataLoader("patients").loadMany(keys); 
            };
        }
        

    // public DataFetcher getPatients() {
    //     return environment ->
    //         {
    //             String argument = environment.getArgument("lastName");
    //             if(argument != null && !argument.isEmpty()){
    //             return patients.stream().filter(fn -> fn.getLastName().contains(argument)).collect(Collectors.toList());

    //             }
    //             return patients;
    //         };
    // }

    public DataFetcher getNurse() {
        return environment ->
        {
            Patient patient = environment.getSource(); //gets parent field's data
            return patient.getNurse();
            // return patients.getNurse();
        };
    }

    //registers all data loaders that will need to be dispatched together
    //here we only have one, but we can have multiple
    public PatientDataFetchers() {
        this.registry = new DataLoaderRegistry();
        registry.register("patients", patientDataLoader());
    }

    @Bean
    public DataLoaderRegistry getDataLoaderRegistry() {
        return registry;
    }
 
    DataLoaderRegistry registry;
  
}