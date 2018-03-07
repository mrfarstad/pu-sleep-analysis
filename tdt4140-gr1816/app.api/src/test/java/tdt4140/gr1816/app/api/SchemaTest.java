package tdt4140.gr1816.app.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;

public class SchemaTest {

  @Before
  public void executedBeforeEach() {
    GraphQLEndpoint.mongo.drop();
  }

  private GraphQL getGraph() {
    GraphQLSchema schema = GraphQLEndpoint.buildSchema();

    return GraphQL.newGraphQL(schema).build();
  }

  private ExecutionResult executeQuery(String query) {
    return getGraph().execute(query);
  }

  @Test
  public void SchemaWillCompile() {
    getGraph();
  }

  @Test
  public void viewerIsNullUnAuthorized() {
    GraphQL graphQL = getGraph();
    ExecutionResult res = graphQL.execute("{viewer {id}}");
    Map<String, Object> result = res.getData();
    assertNull(result.get("viewer"));
  }

  @Test
  public void createUser() {
    ExecutionResult res =
        executeQuery(
            "mutation createUser {\n"
                + "  createUser(authProvider: {username: \"test\", password: \"test\"}, isDoctor: true, gender: \"male\", age: 22) {\n"
                + "    username\n"
                + "    id\n"
                + "    age\n"
                + "  }\n"
                + "}\n"
                + "");
    Map<String, Object> result = res.getData();

    @SuppressWarnings("unchecked")
    Map<String, Object> user = (Map<String, Object>) result.get("createUser");

    assertNotNull(user);

    assertEquals(user.get("username"), "test");
    assertEquals(user.get("age"), 22);
    assertNotNull(user.get("id"));
  }

  @Test
  public void login() {
    createUser();
    ExecutionResult res =
        executeQuery(
            "mutation {\n"
                + "  signinUser(auth: {username: \"test\", password: \"test\"}) {\n"
                + "    token\n"
                + "  }\n"
                + "}");
    Map<String, Object> result = res.getData();
    @SuppressWarnings("unchecked")
    Map<String, Object> payload = (Map<String, Object>) result.get("signinUser");

    assertNotNull(payload);
    assertNotNull(payload.get("token"));
  }
}
