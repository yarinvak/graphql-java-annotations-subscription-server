package graphqla.query;

import graphql.execution.batched.Batched;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphqla.entities.Dog;

import java.util.ArrayList;
import java.util.List;

public class NameFetcher implements DataFetcher<List<String>> {

    @Override
    @Batched
    public List<String> get(DataFetchingEnvironment environment) {
        List<Dog> source = environment.getSource();
        List<String> names = new ArrayList<>();
        final int[] i = {0};
        source.forEach(dog->{
            names.add("A"+ i[0]);
            i[0]++;
        });
        return names;
    }
}
