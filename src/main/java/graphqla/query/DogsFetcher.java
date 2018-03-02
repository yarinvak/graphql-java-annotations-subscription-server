package graphqla.query;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphqla.entities.Dog;

import java.util.Arrays;
import java.util.List;

public class DogsFetcher implements DataFetcher<List<Dog>> {
    @Override
    public List<Dog> get(DataFetchingEnvironment environment) {
        Object source = environment.getSource();
        return Arrays.asList(new Dog(), new Dog());
    }
}
