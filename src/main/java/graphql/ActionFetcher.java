package graphql;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class ActionFetcher implements DataFetcher<String> {
    @Override
    public String get(DataFetchingEnvironment environment) {
        return "blabla";
    }
}
