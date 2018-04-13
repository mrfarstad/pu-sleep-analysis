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
  
  private Map<String, Object> getAverageDataForUserResult(
	      String userId, String fromDate, String toDate, AuthContext context) {
	    String query =
	        "query {\n"
	            + "  getAverageDataForUser(userId: \""
	            + userId
	            + "\", fromDate: \""
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
	    Map<String, Object> message = (Map<String, Object>) result.get("getAverageDataForUser");
	    return message;
	  }
  
  private Map<String, Object> getAverageDataForUsersInAgeGroupResult(
	      String fromDate, String toDate, int fromAge, int toAge, AuthContext context) {
	    String query =
	            "query {\n"
	                + "  getAverageDataForUsersInAgeGroup(fromDate: \""
	                + fromDate
	                + "\", toDate: \""
	                + toDate
	                + "\", fromAge: "
	                + fromAge
	                + ", toAge: "
	                + toAge
	                + ") {\n"
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
	    Map<String, Object> message = (Map<String, Object>) result.get("getAverageDataForUsersInAgeGroup");
	    return message;
	  }
  
  private void verifyAverageDate(Map<String, Object> message, int sleepDuration, int steps, int restHr, int sleepEfficiency) {
	    int sleepDurationFromDb = getIntFromObject(message, "sleepDuration");
	    int stepsFromDb = getIntFromObject(message, "steps");
	    int restHrFromDb = getIntFromObject(message, "restHr");
	    int sleepEfficiencyFromDb = getIntFromObject(message, "sleepEfficiency");

	    assertEquals(sleepDuration, sleepDurationFromDb);
	    assertEquals(steps, stepsFromDb);
	    assertEquals(restHr, restHrFromDb);
	    assertEquals(sleepEfficiency, sleepEfficiencyFromDb);
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
    verifyAverageDate(message, 40, 1100, 65, 70);

    message = getMyAverageResult("2018-01-01", "2018-01-02", userContext);
    verifyAverageDate(message, 35, 1050, 62, 65);
    
    message = getAverageDataForUserResult(user.getId(), "2018-01-01", "2018-01-03", userContext);
    verifyAverageDate(message, 40, 1100, 65, 70);
    
    message = getAverageDataForUserResult(user.getId(), "2018-01-01", "2018-01-02", userContext);
    verifyAverageDate(message, 35, 1050, 62, 65);
    
    message = getAverageDataForUsersInAgeGroupResult("2018-01-01", "2018-01-03", 25, 31, userContext);
    verifyAverageDate(message, 40, 1100, 65, 70);
    
    message = getAverageDataForUsersInAgeGroupResult("2018-01-01", "2018-01-02", 25, 31, userContext);
    verifyAverageDate(message, 35, 1050, 62, 65);
  }
}
