package tdt4140.gr1816.app.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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

  @Test
  public void testGatheringData() {
    User testUser = createUser();
    assertTrue(GraphQLEndpoint.userRepository.setIsGatheringData(testUser, true));
    assertFalse(GraphQLEndpoint.userRepository.setIsGatheringData(testUser, false));
  }

  @Test
  public void testForgotPassword() {
    User testUser = null;
    assertFalse(GraphQLEndpoint.userRepository.forgotPassword(testUser));
    testUser = createUser();
    String oldPassword = testUser.getPassword();
    boolean success = GraphQLEndpoint.userRepository.forgotPassword(testUser);
    assertTrue(success);

    User newUser = GraphQLEndpoint.userRepository.findById(testUser.getId());
    String newPassword = newUser.getPassword();

    assertFalse(oldPassword.equals(newPassword));
  }
  /*
  @Test
  public void testEditUser() {
    User testUser = createUser();
    boolean success = editUser(testUser.getUsername());
    User newUser = GraphQLEndpoint.userRepository.findById(testUser.getId());
    AuthContext auth = forceAuth(newUser.getUsername());

    assertNotNull(success);
    assertTrue(success);

    ExecutionResult res = getGraph().execute("{viewer {username id age gender}}", auth);

    Map<String, Object> result = res.getData();
    Map<String, Object> fetchedUser = (Map<String, Object>) result.get("viewer");

    assertEquals(fetchedUser.get("id"), testUser.getId());
    assertEquals(fetchedUser.get("username"), "NewTest");
    assertEquals(fetchedUser.get("age"), 34);
    assertEquals(fetchedUser.get("gender"), "female");

    assertFalse(editUser(testUser.getUsername()));

    success =
        GraphQLEndpoint.userRepository.editUser(
            (String) fetchedUser.get("username"), "null", "null", 35, "null");
    assertTrue(success);
    User finalUser = GraphQLEndpoint.userRepository.findById((String) fetchedUser.get("id"));
    assertEquals(finalUser.getUsername(), fetchedUser.get("username"));
    assertTrue((int) fetchedUser.get("age") == (finalUser.getAge() - 1));

    success =
        GraphQLEndpoint.userRepository.editUser(
            (String) fetchedUser.get("username"), "DONE", "null", 0, "null");

    assertTrue(success);
  }*/

  @Test
  public void testMessage() {
    createUser();
    createDoctor();
    User user = GraphQLEndpoint.userRepository.findByUsername("test");
    User doctor = GraphQLEndpoint.userRepository.findByUsername("doctor");
    AuthContext context = forceAuth(doctor);

    String query =
        "mutation {\n"
            + "  createMessage(toId: \""
            + user.getId()
            + "\" subject: \"summon studass\" message: \"studass pls notice me\") {\n"
            + "    id\n"
            + "    to {\n"
            + "      id\n"
            + "    }\n"
            + "    from {\n"
            + "      id\n"
            + "    }\n"
            + "    subject\n"
            + "    message\n"
            + "    date\n"
            + "  }\n"
            + "}";
    ExecutionResult res = executeQuery(query, context);
    Map<String, Object> result = res.getData();

    @SuppressWarnings("unchecked")
    Map<String, Object> message = (Map<String, Object>) result.get("createMessage");

    assertNotNull(message);

    String fetchedSubject = (String) message.get("subject");
    String fetchedMessage = (String) message.get("message");
    String fetchedDate = (String) message.get("date");
    Map<String, Object> fetchedUser = (Map<String, Object>) message.get("to");
    Map<String, Object> fetchedDoctor = (Map<String, Object>) message.get("from");

    assertEquals(fetchedUser.get("id"), user.getId());
    assertEquals(fetchedDoctor.get("id"), doctor.getId());
    assertEquals(fetchedSubject, "summon studass");
    assertEquals(fetchedMessage, "studass pls notice me");
    assertTrue(fetchedDate instanceof String);

    query =
        "{\n"
            + "  messagesByMe {\n"
            + "    id\n"
            + "    to {\n"
            + "      id\n"
            + "      username\n"
            + "    }\n"
            + "    from {\n"
            + "      id\n"
            + "      username\n"
            + "    }\n"
            + "    subject\n"
            + "    message\n"
            + "  }\n"
            + "}";

    res = executeQuery(query, context);
    result = res.getData();

    @SuppressWarnings("unchecked")
    ArrayList<Map> messagesByMe = (ArrayList<Map>) result.get("messagesByMe");

    assertEquals(messagesByMe.size(), 1);
    assertEquals(messagesByMe.get(0).get("subject"), "summon studass");
    assertEquals(messagesByMe.get(0).get("message"), "studass pls notice me");
  }
}
