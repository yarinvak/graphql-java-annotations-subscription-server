package graphqla.subscription;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class DogSubFetcher implements DataFetcher {
    private final static DogUpdatePublisher dogUpdatePublisher = new DogUpdatePublisher();

    @Override
    public Object get(DataFetchingEnvironment environment) {
        return dogUpdatePublisher.getPublisher();
    }
}
