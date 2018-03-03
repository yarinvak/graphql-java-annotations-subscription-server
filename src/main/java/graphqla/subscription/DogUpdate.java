package graphqla.subscription;

import graphql.annotations.annotationTypes.GraphQLField;

import java.time.ZonedDateTime;
import java.util.Date;

public class DogUpdate {
    @GraphQLField
    String name;

    public DogUpdate(String name) {
        this.name = name;
    }
}
