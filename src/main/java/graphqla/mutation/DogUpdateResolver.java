package graphqla.mutation;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphqla.subscription.DogUpdate;
import graphqla.subscription.DogUpdatePublisher;
import graphqla.subscription.ObservableList;

public class DogUpdateResolver implements DataFetcher {

    @Override
    public Object get(DataFetchingEnvironment environment) {
        String newName = environment.getArgument("newName");
        // save the dog with the new name here

        notifyUpdate(newName);
        return null;
    }

    private void notifyUpdate(String newName) {
        ObservableList<DogUpdate> observableList = DogUpdatePublisher.observableList;
        observableList.add(new DogUpdate(newName));
    }
}
