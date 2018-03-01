package graphqla.entities;


import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;

import java.util.LinkedHashMap;

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
