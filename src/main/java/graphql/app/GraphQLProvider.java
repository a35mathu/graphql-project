package graphql.app;

import graphql.app.hospitaldata.PatientDataFetchers;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentation;

import java.io.IOException;
import java.net.URL;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import org.dataloader.DataLoaderRegistry;

import graphql.GraphQL;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.micronaut.context.annotation.Context;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Singleton //only has one object
@Context
public class GraphQLProvider {

    // public GraphQLProvider(DataLoaderRegistry registry){
    //     this.registry = registry;
    // }


    @PostConstruct //complete after dependency injection
    // with micronaut dependency injection occurs at compile time instead of runtime
    public void init() throws IOException {
        this.registry = patientDataFetchers.getDataLoaderRegistry();
        System.out.println("reached init");
        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8); //convert schema to a string
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        DataLoaderDispatcherInstrumentation dispatcherInstrumentation = new DataLoaderDispatcherInstrumentation(registry);

        this.graphql = GraphQL.newGraphQL(graphQLSchema).instrumentation(dispatcherInstrumentation).build();
    }


    public GraphQLSchema buildSchema(String sdl){

        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runTimeWiring = buildWiring(); //look into what this is for documentation
        SchemaGenerator schemaGenerator = new SchemaGenerator(); //help execute the build of schema
        return schemaGenerator.makeExecutableSchema(typeRegistry, runTimeWiring);
    }


    //datafetcher created for which field
    public RuntimeWiring buildWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type(newTypeWiring("Query")
                        .dataFetcher("patients", patientDataFetchers.getPatients())) //getPatients for patients
                // .type("Query", builder -> builder.dataFetcher("patients", patientDataFetchers.getPatients()))
                .type(newTypeWiring("Patient")
                        .dataFetcher("nurse", patientDataFetchers.getNurse())) //getNurse for nurses
                    .build();

    }

    /**
     * @return the graphql
     */
    //unused
    public GraphQL getGraphql() {
        return graphql;
    }

    private GraphQL graphql;    
    private DataLoaderRegistry registry;

    // public PatientDataFetchers patientDataFetchers = new PatientDataFetchers();
    @Inject //like autowired
    private PatientDataFetchers patientDataFetchers;   
}