package tdt4140.gr1816.app.api;

import static org.junit.Assert.assertEquals;

import graphql.ExecutionResult;
import java.util.Arrays;
import java.util.Map;
import org.junit.Test;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.types.User;

public class AverageDataTest extends ApiBaseCase {

  @Test
  public void testLoginAndAuth() {
    createUser();
    User user = GraphQLEndpoint.userRepository.findByUsername("test");
    AuthContext context = forceAuth(user);

    final String date = "2018-07-22";
    final String toDate = "2018-07-26";
    final int steps = 8000;
    final int sleepDur = 432;
    final int sleepEff = 98;
    final int pulse = 56;
    String stepQuery =
        "mutation {\n"
            + "  createStepsData(date: \""
            + date
            + "\", steps: "
            + steps
            + ") {\n"
            + "    id\n"
            + "    user {\n"
            + "      id\n"
            + "    }\n"
            + "  }\n"
            + "}";

    String sleepQuery =
        "mutation {\n"
            + "  createSleepData(date: \""
            + date
            + "\", duration: "
            + sleepDur
            + ", efficiency: "
            + sleepEff
            + ") {\n"
            + "    id\n"
            + "    user {\n"
            + "      id\n"
            + "    }\n"
            + "  }\n"
            + "}";
    String pulseQuery =
        "mutation {\n"
            + "  createPulseData(date: \""
            + date
            + "\", restHr: "
            + pulse
            + ") {\n"
            + "    id\n"
            + "    user {\n"
            + "      id\n"
            + "    }\n"
            + "  }\n"
            + "}";
    String[] queries = new String[] {pulseQuery, sleepQuery, stepQuery};
    for (String query : queries) {
      executeQuery(query, context);
    }

    String checkQuery =
        "{\n"
            + "  getAverageData(fromDate: \""
            + date
            + "\", toDate: \""
            + toDate
            + "\") {\n"
            + "    steps\n"
            + "    restHr\n"
            + "    sleepEfficiency\n"
            + "    sleepDuration\n"
            + "  }\n"
            + "  getMyAverageData(fromDate: \""
            + date
            + "\", toDate: \""
            + toDate
            + "\") {\n"
            + "    steps\n"
            + "    restHr\n"
            + "    sleepEfficiency\n"
            + "    sleepDuration\n"
            + "  }\n"
            + "}\n";

    ExecutionResult res = executeQuery(checkQuery, context);
    Map<String, Object> result = res.getData();
    // Avg for user and for group should be equal
    // Days without a count, should not count
    for (String type : Arrays.asList("getMyAverageData", "getAverageData")) {
      Map<String, Object> avgData = (Map<String, Object>) result.get(type);
      assertEquals(steps, avgData.get("steps"));
      assertEquals(sleepDur, avgData.get("sleepDuration"));
      assertEquals(sleepEff, avgData.get("sleepEfficiency"));
      assertEquals(pulse, avgData.get("restHr"));
    }
  }

  private Map<String, Object> getMyAverageResult(
      String fromDate, String toDate, AuthContext context) {
    String query =
        "query {\n"
            + "  getMyAverageData(fromDate: \""
            + fromDate
            + "\", toDate: \""
            + toDate
            + "\") {\n"
            + "sleepDuration\n"
            + "steps\n"
            + "sleepEfficiency\n"
            + "restHr\n"
            + "ageGroup\n"
            + "  }\n"
            + "}";

    ExecutionResult res = executeQuery(query, context);
    Map<String, Object> result = res.getData();

    @SuppressWarnings("unchecked")
    Map<String, Object> message = (Map<String, Object>) result.get("getMyAverageData");
    return message;
  }

  private int getIntFromObject(Map<String, Object> obj, String field) {
    return (int) obj.get(field);
  }

  @Test
  public void testAverageData() {
    // This test tests that the data actually changes as the date interval is limited
    createUser();
    User user = GraphQLEndpoint.userRepository.findByUsername("test");
    AuthContext userContext = forceAuth(user);

    createSleepData(user.getId(), "2018-01-01", 30, 60);
    createSleepData(user.getId(), "2018-01-02", 40, 70);
    createSleepData(user.getId(), "2018-01-03", 50, 80);

    createStepsData(user.getId(), "2018-01-01", 1000);
    createStepsData(user.getId(), "2018-01-02", 1100);
    createStepsData(user.getId(), "2018-01-03", 1200);

    createPulseData(user.getId(), "2018-01-01", 60);
    createPulseData(user.getId(), "2018-01-02", 65);
    createPulseData(user.getId(), "2018-01-03", 70);

    Map<String, Object> message = getMyAverageResult("2018-01-01", "2018-01-03", userContext);

    int sleepDuration = getIntFromObject(message, "sleepDuration");
    int steps = getIntFromObject(message, "steps");
    int restHr = getIntFromObject(message, "restHr");
    int sleepEfficiency = getIntFromObject(message, "sleepEfficiency");

    assertEquals(sleepDuration, 40);
    assertEquals(steps, 1100);
    assertEquals(restHr, 65);
    assertEquals(sleepEfficiency, 70);

    message = getMyAverageResult("2018-01-01", "2018-01-02", userContext);

    sleepDuration = getIntFromObject(message, "sleepDuration");
    steps = getIntFromObject(message, "steps");
    restHr = getIntFromObject(message, "restHr");
    sleepEfficiency = getIntFromObject(message, "sleepEfficiency");

    assertEquals(sleepDuration, 35);
    assertEquals(steps, 1050);
    assertEquals(restHr, 62);
    assertEquals(sleepEfficiency, 65);
  }
}
