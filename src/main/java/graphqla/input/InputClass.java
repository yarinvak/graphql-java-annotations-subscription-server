package graphqla.input;


import graphql.annotations.annotationTypes.GraphQLField;

import java.util.LinkedHashMap;

public class InputClass {
    @GraphQLField
    public String stringInput;

    @GraphQLField
    public int intInput;

    public InputClass(LinkedHashMap<String,Object> args) {
        this.stringInput = (String) args.get("stringInput");
        this.intInput = (int) args.get("intInput");
    }
}
