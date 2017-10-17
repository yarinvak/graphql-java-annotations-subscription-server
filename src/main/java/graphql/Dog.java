package graphql;

import graphql.annotations.GraphQLBatched;
import graphql.annotations.GraphQLDataFetcher;
import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLName;

import java.util.LinkedHashMap;
import java.util.List;

@GraphQLName("dog")
public class Dog {
    @GraphQLField
//    @GraphQLDataFetcher(NameFetcher.class)
    public String name;

    public Dog() {
        this.name="dog";
    }

    public Dog(LinkedHashMap<String, Object> args){
        this.name = (String) args.get("name");
    }

}
