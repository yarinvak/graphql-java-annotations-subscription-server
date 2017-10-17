package graphql;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class DogsFetcher implements DataFetcher<List<Dog>> {
    @Override
    public List<Dog> get(DataFetchingEnvironment environment) {
        Object source = environment.getSource();
        return Arrays.asList(new Dog(), new Dog());
    }
}
