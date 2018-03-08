package tdt4140.gr1816.app.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.Test;

public class TestUserDataFetch {

  private static String createUserResponse =
      "{\"data\":{\"createUser\":{\"id\":\"5aa117f0c40c0b0451c261c2\",\"username\":\"test\",\"isDoctor\":true,\"gender\":\"male\",\"age\":33}}}";
  private static String deleteUserResponse = "{\"data\":{\"deleteUser\":true}}";
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
  public void testCreateUserQuery() {
    when(test.getData(userDataFetch.createUserQuery, null)).thenReturn(createUserResponse);

    assertEquals(test.getData(userDataFetch.createUserQuery, null), createUserResponse);

    // CreateUser should return a user:
    User user = userDataFetch.createUser();
    assertTrue(user instanceof User);
    assertNotNull(user);
    assertTrue(user.getUsername().equals("test"));
    assertTrue(user.isDoctor());
    assertTrue(user.getGender().equals("male"));
    assertTrue(user.getAge() == 33);
  }

  @Test
  public void testDeleteUserQuery() {
    when(test.getData(userDataFetch.deleteUserQuery, null)).thenReturn(deleteUserResponse);

    assertEquals(test.getData(userDataFetch.deleteUserQuery, null), deleteUserResponse);

    // UserDataFetch should return a boolean (success)
    boolean success = userDataFetch.deleteUser();
    assertNotNull(success);
    assertTrue(success);
  }

  @Test
  public void testGetAllUsersQuery() {
    // define what the mock class returns when methods are called
    // this mock the http response from the API
    when(test.getData(userDataFetch.allUsersQuery, null)).thenReturn(getAllUsersResponse);

    // test mock class
    assertEquals(test.getData(userDataFetch.allUsersQuery, null), getAllUsersResponse);

    // UserDataFetch.getAllUsers() should return List<User>
    assertTrue(userDataFetch.getAllUsers() instanceof List);
    List<User> users = userDataFetch.getAllUsers();
    if (!users.isEmpty()) {
      assertTrue(users.get(0) instanceof User);
    }
  }

  @Test
  public void testGetCurrentUserQuery() {
    when(test.getData(userDataFetch.currentUserQuery, userDataFetch.currentToken))
        .thenReturn(getCurrentUserResponse);

    assertEquals(
        test.getData(userDataFetch.currentUserQuery, userDataFetch.currentToken),
        getCurrentUserResponse);
    assertTrue(userDataFetch.getCurrentUser() instanceof User);
  }

  @Test
  public void testGetAccessRequestsToUser() {
    when(test.getData(userDataFetch.accessRequestsToUserQuery, userDataFetch.currentToken))
        .thenReturn(getAccessRequestsToUserResponse);

    assertEquals(
        test.getData(userDataFetch.accessRequestsToUserQuery, userDataFetch.currentToken),
        getAccessRequestsToUserResponse);

    assertTrue(userDataFetch.getAccessRequestsToUser() instanceof List);
    List<DataAccessRequest> requests = userDataFetch.getAccessRequestsToUser();
    if (!requests.isEmpty()) {
      assertTrue(requests.get(0) instanceof DataAccessRequest);
    }
  }

  @Test
  public void testGetAccessRequestsByDoctor() {
    when(test.getData(userDataFetch.accessRequestsByDoctorQuery, userDataFetch.currentToken))
        .thenReturn(getAccessRequestsByDoctorResponse);
    assertEquals(
        test.getData(userDataFetch.accessRequestsByDoctorQuery, userDataFetch.currentToken),
        getAccessRequestsByDoctorResponse);

    assertTrue(userDataFetch.getAccessRequestsByDoctor() instanceof List);
    List<DataAccessRequest> requests = userDataFetch.getAccessRequestsByDoctor();
    if (!requests.isEmpty()) {
      assertTrue(requests.get(0) instanceof DataAccessRequest);
    }
  }

  @Test
  public void testGetUserById() {
    when(test.getData(userDataFetch.allUsersQuery, null)).thenReturn(getAllUsersResponse);

    // test mock class
    assertEquals(test.getData(userDataFetch.allUsersQuery, null), getAllUsersResponse);

    User user = userDataFetch.getUserById("5a9e8503c13edf22f93825e7");
    assertTrue(user instanceof User);
    assertNotNull(user);
    assertTrue(user.getUsername().equals("test"));
    assertTrue(user.isDoctor());
    assertTrue(user.getGender().equals("male"));
    assertTrue(user.getAge() == 22);
  }
}
