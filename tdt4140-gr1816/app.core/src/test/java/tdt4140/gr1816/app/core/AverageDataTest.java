package tdt4140.gr1816.app.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class AverageDataTest extends CoreBaseTest {

  @Test
  public void testGetMyAverageData() {

    String response = getResponse("getMyAverageDataResponse.txt");
    when(test.getData(anyString(), anyString())).thenReturn(response);

    User user = createUser();
    userDataFetch.currentToken = user.getId();
    assertEquals(test.getData("", user.getId()), response);

    String fromDate = "2018-01-01";
    String toDate = "2018-04-04";

    AverageData averageData = userDataFetch.getMyAverageData(fromDate, toDate);
    assertTrue(averageData.getSleepDuration() >= 0);
    assertTrue(averageData.getSleepEfficiency() >= 0);
    assertTrue(averageData.getSteps() >= 0);
    assertTrue(averageData.getRestHr() >= 0);
    assertTrue(Integer.parseInt(averageData.getAgeGroup()) > 0);
  }

  @Test
  public void testGetAverageData() {
    String response = getResponse("getAverageDataResponse.txt");
    when(test.getData(anyString(), anyString())).thenReturn(response);

    User user = createUser();
    userDataFetch.currentToken = user.getId();
    assertEquals(test.getData("", user.getId()), response);

    String fromDate = "2018-01-01";
    String toDate = "2018-04-04";

    AverageData averageData = userDataFetch.getAverageData(fromDate, toDate);
    assertTrue(averageData.getSleepDuration() >= 0);
    assertTrue(averageData.getSleepEfficiency() >= 0);
    assertTrue(averageData.getSteps() >= 0);
    assertTrue(averageData.getRestHr() >= 0);
    assertTrue(Integer.parseInt(averageData.getAgeGroup()) > 0);
  }

  @Test
  public void testGetAverageDataForUser() {
    String response = getResponse("getAverageDataForUserResponse.txt");
    when(test.getData(anyString(), anyString())).thenReturn(response);

    User user = createUser();
    userDataFetch.currentToken = user.getId();
    assertEquals(test.getData("", user.getId()), response);

    String fromDate = "2017-01-01";
    String toDate = "2018-04-04";

    AverageData averageData = userDataFetch.getAverageDataForUser(user.getId(), fromDate, toDate);
    assertTrue(averageData.getSleepDuration() >= 0);
    assertTrue(averageData.getSleepEfficiency() >= 0);
    assertTrue(averageData.getSteps() >= 0);
    assertTrue(averageData.getRestHr() >= 0);
    assertTrue(Integer.parseInt(averageData.getAgeGroup()) > 0);
  }

  @Test
  public void testGetAverageDataForUsersInAgeGroup() {
    String response = getResponse("getAverageDataForUsersInAgeGroupResponse.txt");
    when(test.getData(anyString(), anyString())).thenReturn(response);

    User user = createUser();
    userDataFetch.currentToken = user.getId();
    assertEquals(test.getData("", user.getId()), response);

    String fromDate = "2017-01-01";
    String toDate = "2018-04-04";
    int fromAge = 20;
    int toAge = 30;

    AverageData averageData =
        userDataFetch.getAverageDataForUsersInAgeGroup(fromDate, toDate, fromAge, toAge);
    assertTrue(averageData.getSleepDuration() >= 0);
    assertTrue(averageData.getSleepEfficiency() >= 0);
    assertTrue(averageData.getSteps() >= 0);
    assertTrue(averageData.getRestHr() >= 0);
    assertTrue(Integer.parseInt(averageData.getAgeGroup()) > 0);
  }
}
