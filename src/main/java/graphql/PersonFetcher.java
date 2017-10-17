package graphql;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.Arrays;
import java.util.List;

public class PersonFetcher implements DataFetcher<Person> {
    @Override
    public Person get(DataFetchingEnvironment environment) {
//        return Arrays.asList(new Human(), new Human());
//        return new Human(new Person());
        return (new Person());
    }
}
