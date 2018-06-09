package graphqla.directives;

import graphql.annotations.directives.AnnotationsDirectiveWiring;
import graphql.annotations.directives.AnnotationsWiringEnvironment;
import graphql.annotations.directives.BasicDirectiveInfo;
import graphql.introspection.Introspection;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetcherFactories;
import graphql.schema.GraphQLFieldDefinition;

import java.util.Collections;
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
    public List<Introspection.DirectiveLocation> getValidLocations() {
        return Collections.singletonList(Introspection.DirectiveLocation.FIELD);
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
        };
    }
}
