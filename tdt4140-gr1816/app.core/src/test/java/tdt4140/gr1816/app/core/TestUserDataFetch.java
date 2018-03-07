package tdt4140.gr1816.app.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import tdt4140.gr1816.app.core.UserDataFetch.*;

public class TestUserDataFetch {

  private static String getCurrentUserResponse =
      "{\"data\":{\"allUsers\":[{\"id\":\"5a9e8503c13edf22f93825e7\",\"username\":\"test\",\"isDoctor\":true,\"gender\":\"male\",\"age\":22},{\"id\":\"5a9e85cac13edf22f93825e8\",\"username\":\"test\",\"isDoctor\":true,\"gender\":\"male\",\"age\":22}]}}";
  private static String getAllUsersResponse =
      "{\"data\":{\"viewer\":{\"id\":\"5a9e8503c13edf22f93825e7\",\"username\":\"test\",\"isDoctor\":true,\"gender\":\"male\",\"age\":22}}}";
  private static String getAccessRequestsToUserResponse =
      "{\"data\":{\"dataAccessRequestsForMe\":[]}}";
  private static String getAccessRequestsByDoctorResponse =
      "{\"data\":{\"myDataAccessRequests\":[]}}";

  @Test
  public void testGetAllUsersQuery() {

    //  create mock
    UserDataFetch test = mock(UserDataFetch.class);

    // define return value for method getUniqueId()
    when(test.getData()).thenReturn(getAllUsersResponse);

    // use mock in test....
    assertEquals(test.getUniqueId(), 43);

    assertEquals();
  }
}
