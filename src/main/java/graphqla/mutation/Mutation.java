package graphqla.mutation;


import graphql.annotations.annotationTypes.GraphQLDataFetcher;
import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;

@GraphQLName("mutation")
public class Mutation {
    @GraphQLField
    @GraphQLDataFetcher(UpdateResolver.class)
    public String update(){return null;}
}
