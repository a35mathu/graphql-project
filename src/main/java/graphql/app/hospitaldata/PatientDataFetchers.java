package graphql.app.hospitaldata;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import graphql.schema.DataFetcher;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Context;

@Singleton
@Context
public class PatientDataFetchers implements GraphQLQueryResolver{

    private List<Patient> getPatientsDataViaBatchHTTPApi(List<String> keys) {
        List<Patient> list = keys.stream().map(id -> personData.getPatientData(id)).collect(Collectors.toList());
        return list;
    }

        BatchLoader<String, Patient> patientsBatchLoader = new BatchLoader<String, Patient>() {
            @Override
            public CompletionStage<List<Patient>> load(List<String> keys) {
                //
                // we use supplyAsync() of values here for maximum parellisation
                //
                return CompletableFuture.supplyAsync(() -> getPatientsDataViaBatchHTTPApi(keys));         
            }
        };

    private DataLoader<String, Patient> patientDataLoader() {
        return new DataLoader<>(patientsBatchLoader); 
    }  

    //used for dataloader implementation
    public DataFetcher<CompletableFuture<List<Object>>> getPatientData() {

        return environment -> {
            List<Object> keys = (environment.getArgument("id"));
            if(environment.getSource() instanceof Nurse){
                Nurse nurse = environment.getSource();
                List<Object> patientIDs = nurse.getPatientIDs();
                return registry.getDataLoader("patients").loadMany(patientIDs); 
            }
            return registry.getDataLoader("patients").loadMany(keys); 
        };
    }
    

    public DataFetcher<Nurse> getNurse() {
        return environment ->
        {
            Patient patient = environment.getSource(); //gets parent field's data
            return patient.getNurse();
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
    PersonData personData = new PersonData();
  
}