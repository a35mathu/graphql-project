package graphql.app;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

//graphql endpoint
@Controller("/graphql")
public class GraphqlEndpoint {

    @Get
    public HttpResponse<?> graphQlGet(HttpRequest<?> request) throws IOException {
        System.out.println("Welcome to GraphQL :)");
        String queryParam = request.getParameters().get("query");
        
        //used to allow dynamic values to be added to query, unused for demo purposes
        String variablesParam = request.getParameters().get("variables");
        if (queryParam == null) {
            queryParam = "";
        }
        Map<String, Object> variables = new LinkedHashMap<>();
        if (variablesParam != null) {
            variables = objectMapper.readValue(variablesParam, new TypeReference<Map<String, Object>>() {
            });
        }
        return executeGraphqlQuery(queryParam, variables); 
    }

    HttpResponse<?> executeGraphqlQuery(String queryParam, Map<String, Object> variables)
            throws JsonProcessingException {

        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                .query(queryParam)
                // .variables(variables)
                .build();

                ExecutionResult executionResult = graphQLProvider.getGraphql().execute(executionInput);

        return handleNormalResponse(executionResult);
    }

    HttpResponse<?> handleNormalResponse(ExecutionResult executionResult) throws JsonProcessingException {
        Map<String, Object> toSpecs = executionResult.toSpecification();
        String body = objectMapper.writeValueAsString(toSpecs);
    return HttpResponse
        .ok()
        .characterEncoding(Charsets.UTF_8)
        .contentType(MediaType.APPLICATION_JSON)
        .body(body);
}

private ObjectMapper objectMapper = new ObjectMapper();
@Inject
private GraphQLProvider graphQLProvider;

}




