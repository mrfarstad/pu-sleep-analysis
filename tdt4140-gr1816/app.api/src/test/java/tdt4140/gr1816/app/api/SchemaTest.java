package tdt4140.gr1816.app.api;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import java.util.Map;
import org.junit.Test;

public class SchemaTest {

  @Test
  public void SchemaWillCompile() {
    GraphQLSchema schema = GraphQLEndpoint.buildSchema();

    GraphQL graphQL = GraphQL.newGraphQL(schema).build();
    Map<String, Object> result = graphQL.execute("{}").getData();
  }

  @Test
  public void viewerIsNullUnAuthorized() {
    GraphQLSchema schema = GraphQLEndpoint.buildSchema();

    GraphQL graphQL = GraphQL.newGraphQL(schema).build();
    ExecutionResult res =
        graphQL.execute("{\n" + "  allUsers {\n" + "    id\n" + "    username\n" + "  }\n" + "}");
    Map<String, Object> result = res.getData();
  }
}
