package tdt4140.gr1816.app.api;

import static org.junit.Assert.assertEquals;
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
  public void testDataAccessRequestsFromUserPerspective() {
    // "Setup"
    createUser();
    createDoctor();
    User user = GraphQLEndpoint.userRepository.findByUsername("test");
    User doctor = GraphQLEndpoint.userRepository.findByUsername("doctor");

    AuthContext userContext = forceAuth(user);
    AuthContext doctorContext = forceAuth(doctor);

    // Preps database by creating a request
    ExecutionResult tempresult =
        executeQuery(
            "mutation {requestDataAccess(dataOwnerId: \"" + user.getId() + "\") {id status} }",
            doctorContext);

    String query =
        "{\n"
            + "  dataAccessRequestsForMe {\n"
            + "    id\n"
            + "    dataOwner {\n"
            + "      id\n"
            + "    }\n"
            + "    requestedBy {\n"
            + "      id\n"
            + "    }\n"
            + "    status\n"
            + "  }\n"
            + "}";

    ExecutionResult res = executeQuery(query, userContext);

    Map<String, Object> result = res.getData();

    @SuppressWarnings("unchecked")
    ArrayList<Map> dataAccessRequestsForMe = (ArrayList<Map>) result.get("dataAccessRequestsForMe");

    assertEquals(dataAccessRequestsForMe.size(), 1);
    assertEquals(dataAccessRequestsForMe.get(0).get("status"), "PENDING");

    String requestId = (String) dataAccessRequestsForMe.get(0).get("id");

    ExecutionResult deleteRes =
        executeQuery(
            "mutation {deleteDataAccessRequest(dataAccessRequestId: \"" + requestId + "\")}",
            userContext);

    Map<String, Object> deleteResult = deleteRes.getData();

    assertTrue((Boolean) deleteResult.get("deleteDataAccessRequest"));
  }

  @Test
  public void testDataAccessRequestsFromDoctorsPerspective() {
    // "Setup"
    createUser();
    createDoctor();
    User user = GraphQLEndpoint.userRepository.findByUsername("test");
    User doctor = GraphQLEndpoint.userRepository.findByUsername("doctor");

    AuthContext userContext = forceAuth(user);
    AuthContext doctorContext = forceAuth(doctor);
    // Preps database by creating a request
    ExecutionResult tempresult =
        executeQuery(
            "mutation {requestDataAccess(dataOwnerId: \"" + user.getId() + "\") {id status} }",
            doctorContext);

    String query =
        "{\n"
            + "  myDataAccessRequests {\n"
            + "    id\n"
            + "    dataOwner {\n"
            + "      id\n"
            + "    }\n"
            + "    requestedBy {\n"
            + "      id\n"
            + "    }\n"
            + "    status\n"
            + "  }\n"
            + "}";
    ExecutionResult res = executeQuery(query, doctorContext);

    Map<String, Object> result = res.getData();

    @SuppressWarnings("unchecked")
    ArrayList<Map> dataAccessRequestsFromDoc = (ArrayList<Map>) result.get("myDataAccessRequests");
    assertEquals(dataAccessRequestsFromDoc.size(), 1);
    assertEquals(dataAccessRequestsFromDoc.get(0).get("status"), "PENDING");
  }

  @Test
  public void testAnswerDataAccessRequest() {
    // "Setup"
    createUser();
    createDoctor();
    User user = GraphQLEndpoint.userRepository.findByUsername("test");
    User doctor = GraphQLEndpoint.userRepository.findByUsername("doctor");

    AuthContext userContext = forceAuth(user);
    AuthContext doctorContext = forceAuth(doctor);
    // Preps database by creating a request
    ExecutionResult tempres =
        executeQuery(
            "mutation {requestDataAccess(dataOwnerId: \""
                + user.getId()
                + "\") {id dataOwner{id} requestedBy{id} status} }",
            doctorContext);

    Map<String, Object> tempresult = tempres.getData();

    @SuppressWarnings("unchecked")
    Map<String, Object> request = (Map<String, Object>) tempresult.get("requestDataAccess");

    String ans = "ACCEPTED";
    String answerQuery =
        "mutation {\n"
            + "  answerDataAccessRequest(dataAccessRequestId: \""
            + request.get("id")
            + "\", status: "
            + ans
            + ") {\n"
            + "    status\n"
            + "  }\n"
            + "}";
    ExecutionResult res = executeQuery(answerQuery, userContext);
    Map<String, Object> result = res.getData();

    @SuppressWarnings("unchecked")
    Map<String, Object> answeredRequest =
        (Map<String, Object>) result.get("answerDataAccessRequest");

    assertNotNull(answeredRequest);
    assertEquals(answeredRequest.get("status"), "ACCEPTED");

    ans = "REJECTED";
    answerQuery =
        "mutation {\n"
            + "  answerDataAccessRequest(dataAccessRequestId: \""
            + request.get("id")
            + "\", status: "
            + ans
            + ") {\n"
            + "    status\n"
            + "  }\n"
            + "}";
    res = executeQuery(answerQuery, userContext);
    result = res.getData();
    @SuppressWarnings("unchecked")
    Map<String, Object> newAnsweredRequest =
        (Map<String, Object>) result.get("answerDataAccessRequest");
    assertEquals(newAnsweredRequest.get("status"), "REJECTED");
  }

  @Test
  public void testSleepData() {
    createUser();
    User user = GraphQLEndpoint.userRepository.findByUsername("test");
    AuthContext context = forceAuth(user);

    String query =
        "mutation{\n"
            + "  createSleepData(\n"
            + "    date: \"2018-01-01\"\n"
            + "    duration: 200\n"
            + "    efficiency: 20\n"
            + "  ){\n"
            + "    id\n"
            + "    efficiency\n"
            + "    user{\n"
            + "      id\n"
            + "    }\n"
            + "  }\n"
            + "}";
    ExecutionResult res = executeQuery(query, context);
    Map<String, Object> result = res.getData();

    @SuppressWarnings("unchecked")
    Map<String, Object> sleepData = (Map<String, Object>) result.get("createSleepData");

    assertNotNull(sleepData);

    Map<String, Object> fetchedUser = (Map<String, Object>) sleepData.get("user");

    assertEquals(fetchedUser.get("id"), user.getId());

    String query1 =
        "query {\n"
            + "  sleepDataBetweenDates(userId: \""
            + user.getId()
            + "\", startDate: \"2018-01-01\", endDate: \"2018-01-02\") {\n"
            + "    id\n"
            + "  }\n"
            + "}";
    ExecutionResult res1 = executeQuery(query1, context);
    Map<String, Object> result1 = res1.getData();

    @SuppressWarnings("unchecked")
    ArrayList<Map> pulseDataList = (ArrayList<Map>) result1.get("sleepDataBetweenDates");

    assertEquals(pulseDataList.size(), 1);
    assertTrue(pulseDataList.get(0).get("id") != null);
    assertTrue(pulseDataList.get(0).get("id") instanceof String);
  }

  @Test
  public void testStepsData() {
    createUser();
    User user = GraphQLEndpoint.userRepository.findByUsername("test");
    AuthContext context = forceAuth(user);

    String query =
        "mutation {\n"
            + "  createStepsData(date: \"2018-07-22\", steps: 7000) {\n"
            + "    id\n"
            + "    user {\n"
            + "      id\n"
            + "    }\n"
            + "  }\n"
            + "}";
    ExecutionResult res = executeQuery(query, context);
    Map<String, Object> result = res.getData();

    @SuppressWarnings("unchecked")
    Map<String, Object> stepsData = (Map<String, Object>) result.get("createStepsData");

    assertNotNull(stepsData);

    Map<String, Object> fetchedUser = (Map<String, Object>) stepsData.get("user");

    assertEquals(fetchedUser.get("id"), user.getId());

    String query1 =
        "query {\n"
            + "  stepsDataBetweenDates(userId: \""
            + user.getId()
            + "\", startDate: \"2018-07-22\", endDate: \"2018-07-23\") {\n"
            + "    id\n"
            + "  }\n"
            + "}";
    ExecutionResult res1 = executeQuery(query1, context);
    Map<String, Object> result1 = res1.getData();

    @SuppressWarnings("unchecked")
    ArrayList<Map> pulseDataList = (ArrayList<Map>) result1.get("stepsDataBetweenDates");

    assertEquals(pulseDataList.size(), 1);
    assertTrue(pulseDataList.get(0).get("id") != null);
    assertTrue(pulseDataList.get(0).get("id") instanceof String);
  }

  @Test
  public void testPulseData() {
    createUser();
    User user = GraphQLEndpoint.userRepository.findByUsername("test");
    AuthContext context = forceAuth(user);

    String query =
        "mutation {\n"
            + "  createPulseData(date: \"2018-07-22\", restHr: 65) {\n"
            + "    id\n"
            + "    user {\n"
            + "      id\n"
            + "    }\n"
            + "  }\n"
            + "}";
    ExecutionResult res = executeQuery(query, context);
    Map<String, Object> result = res.getData();

    @SuppressWarnings("unchecked")
    Map<String, Object> pulseData = (Map<String, Object>) result.get("createPulseData");

    assertNotNull(pulseData);

    Map<String, Object> fetchedUser = (Map<String, Object>) pulseData.get("user");

    assertEquals(fetchedUser.get("id"), user.getId());

    String query1 =
        "query {\n"
            + "  pulseDataBetweenDates(userId: \""
            + user.getId()
            + "\", startDate: \"2018-07-01\", endDate: \"2018-08-02\") {\n"
            + "    id\n"
            + "  }\n"
            + "}";
    ExecutionResult res1 = executeQuery(query1, context);
    Map<String, Object> result1 = res1.getData();

    @SuppressWarnings("unchecked")
    ArrayList<Map> pulseDataList = (ArrayList<Map>) result1.get("pulseDataBetweenDates");

    assertEquals(pulseDataList.size(), 1);
    assertTrue(pulseDataList.get(0).get("id") != null);
    assertTrue(pulseDataList.get(0).get("id") instanceof String);
  }

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
