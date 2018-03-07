package tdt4140.gr1816.app.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.types.User;

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

    User u = GraphQLEndpoint.userRepository.findByUsername("test");
    AuthContext ctx = new AuthContext(u, null, null);

    ExecutionResult res2 = getGraph().execute("{viewer {username id}}", ctx);

    Map<String, Object> result2 = res2.getData();
    Map<String, Object> user = (Map<String, Object>) result2.get("viewer");
    assertEquals(user.get("username"), "test");

    User u2 = GraphQLEndpoint.userRepository.findById((String) user.get("id"));
    assertEquals(u.getId(), u2.getId());
  }

  @Test
  public void noUserShouldExist() {
    User u2 = GraphQLEndpoint.userRepository.findByUsername("test");
    assertNull(u2);
  }

  @Test
  public void deleteUser() {
    createUser();
    User user = GraphQLEndpoint.userRepository.findByUsername("test");
    assertNotNull(user);
    ExecutionResult res =
        executeQuery(
            "  mutation {\n"
                + "    deleteUser(auth: {\n"
                + "      username: \"test\"\n"
                + "      password: \"test\"\n"
                + "    })\n"
                + "  }\n"
                + "");
    user = GraphQLEndpoint.userRepository.findByUsername("test");
    assertNull(user);
  }

  @Test
  public void getUserById() {
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
    assertNotNull(user.get("id"));
  }
}
