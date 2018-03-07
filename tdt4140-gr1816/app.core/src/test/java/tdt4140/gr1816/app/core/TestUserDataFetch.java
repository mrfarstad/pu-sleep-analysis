package tdt4140.gr1816.app.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.Test;

public class TestUserDataFetch {

  private static String getAllUsersResponse =
      "{\"data\":{\"allUsers\":[{\"id\":\"5a9e8503c13edf22f93825e7\",\"username\":\"test\",\"isDoctor\":true,\"gender\":\"male\",\"age\":22},{\"id\":\"5a9e85cac13edf22f93825e8\",\"username\":\"test\",\"isDoctor\":true,\"gender\":\"male\",\"age\":22}]}}";
  private static String getCurrentUserResponse =
      "{\"data\":{\"viewer\":{\"id\":\"5a9e8503c13edf22f93825e7\",\"username\":\"test\",\"isDoctor\":true,\"gender\":\"male\",\"age\":22}}}";
  private static String getAccessRequestsToUserResponse =
      "{\"data\":{\"dataAccessRequestsForMe\":[{\"requestedBy\":{\"username\":\"boye\",\"isDoctor\":true,\"gender\":\"male\",\"age\":33},\"status\":\"PENDING\"}]}}";
  private static String getAccessRequestsByDoctorResponse =
      "{\"data\":{\"myDataAccessRequests\":[{\"dataOwner\":{\"username\":\"mathiawa\",\"isDoctor\":false,\"gender\":\"male\",\"age\":21},\"status\":\"PENDING\"},{\"dataOwner\":{\"username\":\"doctor\",\"isDoctor\":true,\"gender\":\"male\",\"age\":40},\"status\":\"PENDING\"}]}}";

  //  create mock
  private DataGetter test = mock(DataGetter.class);

  private UserDataFetch userDataFetch = new UserDataFetch(test);

  @Test
  public void testGetAllUsersQuery() {
    // define what the mock class returns when methods are called
    // this mock the http response from the API
    when(test.getData(userDataFetch.allUsersQuery)).thenReturn(getAllUsersResponse);

    // test mock class
    assertEquals(test.getData(userDataFetch.allUsersQuery), getAllUsersResponse);

    // UserDataFetch.getAllUsers() should return List<User>
    assertTrue(userDataFetch.getAllUsers() instanceof List);
    List<User> users = userDataFetch.getAllUsers();
    if (!users.isEmpty()) {
      assertTrue(users.get(0) instanceof User);
    }
  }

  @Test
  public void testGetCurrentUserQuery() {
    when(test.getData(userDataFetch.currentUserQuery)).thenReturn(getCurrentUserResponse);

    assertEquals(test.getData(userDataFetch.currentUserQuery), getCurrentUserResponse);
    assertTrue(userDataFetch.getCurrentUser() instanceof User);
  }

  @Test
  public void testGetAccessRequestsToUser() {
    when(test.getData(userDataFetch.accessRequestsToUserQuery))
        .thenReturn(getAccessRequestsToUserResponse);

    assertEquals(
        test.getData(userDataFetch.accessRequestsToUserQuery), getAccessRequestsToUserResponse);

    assertTrue(userDataFetch.getAccessRequestsToUser() instanceof List);
    List<DataAccessRequest> requests = userDataFetch.getAccessRequestsToUser();
    if (!requests.isEmpty()) {
      assertTrue(requests.get(0) instanceof DataAccessRequest);
    }
  }

  @Test
  public void testGetAccessRequestsByDoctor() {
    when(test.getData(userDataFetch.accessRequestsByDoctorQuery))
        .thenReturn(getAccessRequestsByDoctorResponse);
    assertEquals(
        test.getData(userDataFetch.accessRequestsByDoctorQuery), getAccessRequestsByDoctorResponse);

    assertTrue(userDataFetch.getAccessRequestsByDoctor() instanceof List);
    List<DataAccessRequest> requests = userDataFetch.getAccessRequestsByDoctor();
    if (!requests.isEmpty()) {
      assertTrue(requests.get(0) instanceof DataAccessRequest);
    }
  }

  @Test
  public void testGetUserById() {
    when(test.getData(userDataFetch.allUsersQuery)).thenReturn(getAllUsersResponse);

    // test mock class
    assertEquals(test.getData(userDataFetch.allUsersQuery), getAllUsersResponse);

    User user = userDataFetch.getUserById("5a9e8503c13edf22f93825e7");
    assertTrue(user instanceof User);
    assertNotNull(user);
    assertTrue(user.getUsername().equals("test"));
    assertTrue(user.isDoctor());
    assertTrue(user.getGender().equals("male"));
    assertTrue(user.getAge() == 22);
  }
}
