package tdt4140.gr1816.app.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import graphql.ExecutionResult;
import java.util.ArrayList;
import java.util.Map;
import org.junit.Test;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.types.User;

public class DataTest extends ApiBaseCase {

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
    ArrayList<Map> sleepDataList = (ArrayList<Map>) result1.get("sleepDataBetweenDates");

    assertEquals(sleepDataList.size(), 1);
    assertTrue(sleepDataList.get(0).get("id") != null);
    assertTrue(sleepDataList.get(0).get("id") instanceof String);

    query = "mutation {\n" + "  deleteSleepData(" + "sleepId: " + "\"" + "%s" + "\"" + ")" + "}";

    String ID = (String) sleepDataList.get(0).get("id");
    ExecutionResult res2 = executeQuery(String.format(query, ID), context);
    Map<String, Object> result2 = res2.getData();

    boolean success = (boolean) result2.get("deleteSleepData");
    assertTrue(success);
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
    ArrayList<Map> stepsDataList = (ArrayList<Map>) result1.get("stepsDataBetweenDates");

    assertEquals(stepsDataList.size(), 1);
    assertTrue(stepsDataList.get(0).get("id") != null);
    assertTrue(stepsDataList.get(0).get("id") instanceof String);

    query = "mutation {\n" + "  deleteStepsData(" + "stepsId: " + "\"" + "%s" + "\"" + ")" + "}";

    String ID = (String) stepsDataList.get(0).get("id");
    ExecutionResult res2 = executeQuery(String.format(query, ID), context);
    Map<String, Object> result2 = res2.getData();

    boolean success = (boolean) result2.get("deleteStepsData");
    assertTrue(success);
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

    query = "mutation {\n" + "  deletePulseData(" + "pulseId: " + "\"" + "%s" + "\"" + ")" + "}";

    String ID = (String) pulseDataList.get(0).get("id");
    ExecutionResult res2 = executeQuery(String.format(query, ID), context);
    Map<String, Object> result2 = res2.getData();

    boolean success = (boolean) result2.get("deletePulseData");
    assertTrue(success);
  }
}
