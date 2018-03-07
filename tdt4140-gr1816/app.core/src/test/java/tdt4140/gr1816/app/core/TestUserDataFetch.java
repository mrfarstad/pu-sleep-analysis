package tdt4140.gr1816.app.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

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
    // define what the mock class returns when methods are called
    when(test.getData(UserDataFetch.allUsersQuery)).thenReturn(getAllUsersResponse);

    // test mock class
    assertEquals(test.getData(UserDataFetch.allUsersQuery), getAllUsersResponse);
    // UserDataFetch.getAllUsers() should return List<User>
    assertTrue(test.getAllUsers() instanceof List);
    List<User> users = test.getAllUsers();
    if (!users.isEmpty()) {
      assertTrue(users.get(0) instanceof User);
    }
  }
}
