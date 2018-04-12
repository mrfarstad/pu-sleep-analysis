package tdt4140.gr1816.app.api;

import static org.junit.Assert.assertEquals;

import graphql.ExecutionResult;
import java.util.Arrays;
import java.util.Map;
import org.junit.Test;
import tdt4140.gr1816.app.api.auth.AuthContext;
import tdt4140.gr1816.app.api.types.User;

public class AvgDataTestCase extends ApiBaseCase {
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
}
