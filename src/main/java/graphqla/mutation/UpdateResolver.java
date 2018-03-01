package graphqla.mutation;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class UpdateResolver implements DataFetcher<String>{

    @Override
    public String get(DataFetchingEnvironment environment) {
        return "asdfasdfa";
    }
}
