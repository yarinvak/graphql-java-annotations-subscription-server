package graphql;

import graphql.annotations.GraphQLDataFetcher;
import graphql.annotations.GraphQLField;
import graphql.annotations.GraphQLName;

@GraphQLName("mutation")
public class Mutation {
    @GraphQLField
    @GraphQLDataFetcher(UpdateResolver.class)
    public String update(){return null;}
}
