package graphql;

import graphql.annotations.GraphQLDataFetcher;
import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLName;

import java.util.List;

@GraphQLName("query")
public class Query {
//    @GraphQLField
//    @GraphQLDataFetcher(PersonFetcher.class)
//    public Person person(@GraphQLName("extension") String extension, @GraphQLName("others") List<List<List<Person>>> others, @GraphQLName("list") List<List<String>> strings) {
//        return null;
//    }
//
//    @GraphQLField
//    @GraphQLDataFetcher(PersonFetcher.class)
//    public Person person1(@GraphQLName("extension") String extension, @GraphQLName("other") Person other){
//        return null;
//    }
//
//    @GraphQLField
//    @GraphQLDataFetcher(PersonFetcher.class)
//    public Person person2(@GraphQLName("extension") String extension, @GraphQLName("other") Person other){
//        return null;
//    }

    @GraphQLField
    @GraphQLDataFetcher(PersonFetcher.class)
    public Person person3(@GraphQLName("inputs") List<InputClass> inputs){return null;}




}

