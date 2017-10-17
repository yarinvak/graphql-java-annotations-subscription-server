package graphql;

import graphql.annotations.GraphQLAnnotations;
import graphql.execution.ExecutionStrategy;
import graphql.execution.batched.BatchedExecutionStrategy;
import graphql.schema.GraphQLSchema;
import org.eclipse.jetty.client.HttpResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;

import static graphql.schema.GraphQLSchema.newSchema;

@RestController
public class GraphQLController {

    @RequestMapping(value="/graphql", method= RequestMethod.POST)
    public Object index(@RequestBody GraphQLRequest graphQLRequest) {
        GraphQLSchema schema = newSchema().query(GraphQLAnnotations.object(Query.class))
                .mutation(GraphQLAnnotations.object(Mutation.class))
                .build();
        GraphQL graphql = GraphQL.newGraphQL(schema).build();
        ExecutionResult result = graphql.execute(graphQLRequest.getQuery(), this, new HashMap<>());
        return result;
    }

}
