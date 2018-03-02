package graphqla;

import java.util.HashMap;

public class GraphQLRequest {
    private String query;
    private HashMap<String, Object> variables;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public HashMap<String, Object> getVariables() {
        return variables;
    }

    public void setVariables(HashMap<String, Object> variables) {
        this.variables = variables;
    }
}
