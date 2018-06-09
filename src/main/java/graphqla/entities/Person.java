package graphqla.entities;

import graphql.annotations.annotationTypes.GraphQLDataFetcher;
import graphql.annotations.annotationTypes.GraphQLDirectives;
import graphql.annotations.annotationTypes.GraphQLField;
import graphql.annotations.annotationTypes.GraphQLName;
import graphqla.directives.UpperDirectiveInfo;
import graphqla.query.DogsFetcher;

import java.util.LinkedHashMap;
import java.util.List;

@GraphQLName("Person")
public class Person {
//    @GraphQLField
//    public @GraphQLID int id(){return 5;}
//
//    @GraphQLField
//    private @GraphQLID String idString;

    @GraphQLField
    @GraphQLDirectives({UpperDirectiveInfo.class})
    public String name() {
        return "my person";
    }

    @GraphQLField
    @GraphQLDataFetcher(DogsFetcher.class)
    public List<Dog> dogs() {
        return null;
    }

    @GraphQLField
    public String x(@GraphQLName("argument") @GraphQLDirectives({UpperDirectiveInfo.class}) String argument) {
        return argument;
    }

//    @GraphQLField
//    private @GraphQLID String id;
//
//    @GraphQLField
//    private @GraphQLID int id2;

//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public int getId2() {
//        return id2;
//    }
//
//    public void setId2(int id2) {
//        this.id2 = id2;
//    }

    public Person() {
//        this.idString="asdkfhaksdjfhasdf";
//            this.id = "asdfasdf";
//            this.id2=5;
    }

    public Person(LinkedHashMap<String, Object> args) {

    }
//
//    public String getIdString() {
//        return idString;
//    }
//
//    public void setIdString(String idString) {
//        this.idString = idString;
//    }
    //    public Person(LinkedHashMap<String, Object> map) {
//        this.name = (String) map.get("name");
//        this.dogs = (List<Dog>)map.get("dogs");
//    }

}

