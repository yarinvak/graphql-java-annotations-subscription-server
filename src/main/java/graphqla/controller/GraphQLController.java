package graphqla.controller;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.annotations.processor.GraphQLAnnotations;
import graphql.schema.GraphQLSchema;
import graphqla.GraphQLRequest;
import graphqla.mutation.Mutation;
import graphqla.query.Query;
import graphqla.subscription.Subscription;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import static graphql.schema.GraphQLSchema.newSchema;

@RestController
public class GraphQLController {

    @CrossOrigin
    @RequestMapping(value = "/graphql", method = RequestMethod.POST)
    public Object index(@RequestBody GraphQLRequest graphQLRequest) {
        GraphQLSchema schema = getGraphQLSchema();
        GraphQL graphql = GraphQL.newGraphQL(schema).build();
        ExecutionResult result = graphql.execute(graphQLRequest.getQuery(), this, new HashMap<>());
        return result;
    }

    private GraphQLSchema getGraphQLSchema() {
        return newSchema().query(GraphQLAnnotations.object(Query.class))
                .mutation(GraphQLAnnotations.object(Mutation.class))
                .subscription(GraphQLAnnotations.object(Subscription.class))
                .build();
    }

}
