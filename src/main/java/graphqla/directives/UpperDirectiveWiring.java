package graphqla.directives;

import graphql.annotations.directives.AnnotationsDirectiveWiring;
import graphql.annotations.directives.AnnotationsWiringEnvironment;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetcherFactories;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLFieldDefinition;

public class UpperDirectiveWiring implements AnnotationsDirectiveWiring {
    @Override
    public GraphQLFieldDefinition onField(AnnotationsWiringEnvironment<GraphQLFieldDefinition> environment) {
        GraphQLFieldDefinition field = environment.getElement();
        DataFetcher dataFetcher = DataFetcherFactories.wrapDataFetcher(field.getDataFetcher(), (((dataFetchingEnvironment, value) -> {
            if (value instanceof String) {
                return ((String) value).toUpperCase();
            }
            return value;
        })));
        return field.transform(builder -> builder.dataFetcher(dataFetcher));
    }

    @Override
    public GraphQLArgument onArgument(AnnotationsWiringEnvironment<GraphQLArgument> environment) {
        GraphQLArgument argument = environment.getElement();
        if (argument.getType().getName().equals("String")) {
            argument = argument.transform(builder -> builder.description("This is a string argument"));
        }
        return argument;
    }
}
