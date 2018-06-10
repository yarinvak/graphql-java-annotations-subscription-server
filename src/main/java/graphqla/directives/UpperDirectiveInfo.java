package graphqla.directives;

import graphql.annotations.directives.AnnotationsDirectiveWiring;
import graphql.annotations.directives.AnnotationsWiringEnvironment;
import graphql.annotations.directives.BasicDirectiveInfo;
import graphql.annotations.directives.DirectiveArgument;
import graphql.introspection.Introspection;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetcherFactories;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLFieldDefinition;

import java.util.Arrays;
import java.util.List;

public class UpperDirectiveInfo extends BasicDirectiveInfo {
    @Override
    public String getName() {
        return "upper";
    }

    @Override
    public String getDescription() {
        return "makes the string upper case";
    }

    @Override
    public DirectiveArgument[] getArguments() {
        return new DirectiveArgument[]{new DirectiveArgument("isUpper", "1", "indicates if is upper", Integer.class)};
    }

    @Override
    public List<Introspection.DirectiveLocation> getValidLocations() {
        return Arrays.asList(Introspection.DirectiveLocation.FIELD, Introspection.DirectiveLocation.ARGUMENT_DEFINITION);
    }

    @Override
    public AnnotationsDirectiveWiring getSchemaDirectiveWiring() {
        return new AnnotationsDirectiveWiring() {
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
        };
    }
}
