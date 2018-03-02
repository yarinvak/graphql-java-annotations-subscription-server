package graphqla.subscription;

import graphql.annotations.annotationTypes.GraphQLDataFetcher;
import graphql.annotations.annotationTypes.GraphQLField;

public class Subscription {
    @GraphQLField
    @GraphQLDataFetcher(DogSubFetcher.class)
    public DogUpdate dogUpdates(){return null;}
}
