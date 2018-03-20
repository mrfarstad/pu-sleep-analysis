package tdt4140.gr1816.app.api;

import static org.junit.Assert.*;

import graphql.ExecutionResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.types.User;

public class UserMethodsTest extends ApiBaseCase {

  @Test
  public void testCreateUser() {
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
  public void testLoginAndAuth() {
    User createdUser = createUser();
    String query =
        ("mutation ($username: String! $password: String!){\n"
            + "  signinUser(auth: {username: $username, password: $password}) {\n"
            + "    token\n"
            + "  }\n"
            + "}");

    Map<String, Object> variables = new HashMap<>();
    variables.put("username", createdUser.getUsername());
    variables.put("password", createdUser.getPassword());

    ExecutionResult res = getGraph().execute(query, new Object(), variables);
    Map<String, Object> result = res.getData();
    @SuppressWarnings("unchecked")
    Map<String, Object> payload = (Map<String, Object>) result.get("signinUser");

    assertNotNull(payload);

    User u = GraphQLEndpoint.userRepository.findByUsername(createdUser.getUsername());
    assertEquals(u.getId(), createdUser.getId());
    AuthContext ctx = new AuthContext(u, null, null);

    ExecutionResult res2 = getGraph().execute("{viewer {username id}}", ctx);

    Map<String, Object> result2 = res2.getData();
    Map<String, Object> user = (Map<String, Object>) result2.get("viewer");
    assertEquals(user.get("username"), "test");

    User u2 = GraphQLEndpoint.userRepository.findById((String) user.get("id"));
    assertEquals(u.getId(), u2.getId());
  }

  @Test
  public void testDeleteUser() {
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
  public void testGetAllUsers() {
    User user = createUser();

    ExecutionResult res =
        executeQuery(
            "query allUsers {\n"
                + "  allUsers{"
                + "    username\n"
                + "    id\n"
                + "    age\n"
                + "  }\n"
                + "}\n"
                + "",
            forceAuth(user));
    Map<String, Object> result = res.getData();

    @SuppressWarnings("unchecked")
    ArrayList<Map> allUsers = (ArrayList<Map>) result.get("allUsers");

    assertEquals(allUsers.size(), 1);
    assertEquals(allUsers.get(0).get("id"), user.getId());
  }

  @Test
  public void testGetUserById() {
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
