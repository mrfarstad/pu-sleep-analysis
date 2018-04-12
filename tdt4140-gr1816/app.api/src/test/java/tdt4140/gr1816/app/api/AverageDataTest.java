package tdt4140.gr1816.app.api;

import static org.junit.Assert.assertEquals;

import graphql.ExecutionResult;
import java.util.Map;
import org.junit.Test;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.types.User;

public class AverageDataTest extends ApiBaseCase {

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
