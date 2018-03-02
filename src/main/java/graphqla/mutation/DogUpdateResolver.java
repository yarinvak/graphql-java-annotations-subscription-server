package graphqla.mutation;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphqla.subscription.DogUpdate;
import graphqla.subscription.DogUpdatePublisher;
import graphqla.subscription.ObservableList;

public class DogUpdateResolver implements DataFetcher {

    @Override
    public Object get(DataFetchingEnvironment environment) {
        ObservableList<DogUpdate> observableList = DogUpdatePublisher.observableList;
        observableList.add(new DogUpdate("nanana1"));
        return null;
    }
}
