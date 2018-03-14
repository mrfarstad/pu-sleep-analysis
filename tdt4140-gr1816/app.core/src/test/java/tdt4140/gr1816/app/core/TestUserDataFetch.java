package tdt4140.gr1816.app.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.Test;
import tdt4140.gr1816.app.core.DataAccessRequest.DataAccessRequestStatus;

public class TestUserDataFetch {

  private static String createUserTestQuery =
      "{\"query\":\"mutation{createUser(authProvider:{username:\\\"test\\\" password:\\\"test\\\"} isDoctor: true gender:\\\"male\\\" age: 22){username}}\"}";
  /*private static String deleteUserQuery =
   "{\"query\":\"mutation{deleteUser(auth:{username:\\\"test\\\" password:\\\"test\\\"})}\"}";
  */
  private static String signInTestQuery =
      "{\"query\":\"mutation{signinUser(auth:{username:\\\"test\\\" password:\\\"test\\\"}){token}}\"}";
  /*
    private static String currentUserQuery = "{\"query\":\"query{viewer{id username isDoctor gender age}}\"}";
    private static String accessRequestsToUserQuery =
  	  "{\"query\":\"query{dataAccessRequestsForMe{requestedBy{username isDoctor gender age}status}}\"}";
    private static String accessRequestsByDoctorQuery =
  	  "{\"query\":\"query{myDataAccessRequests{dataOwner{username isDoctor gender age}status}}\"}";
  */
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
  private static String signInResponse =
      "{\"data\":{\"signinUser\":{\"token\":\"5aa82961c40c0b35eb3e8663\",\"user\":{\"id\":\"5aa82961c40c0b35eb3e8663\",\"username\":\"test\",\"isDoctor\":true,\"gender\":\"male\",\"age\":22}}}}";

  //  create mock
  private DataGetter test = mock(DataGetter.class);

  private UserDataFetch userDataFetch = new UserDataFetch(test);

  @Test
  public void testCreateUserQuery() {
    when(test.getData(createUserTestQuery, null)).thenReturn(createUserResponse);

    assertEquals(test.getData(createUserTestQuery, null), createUserResponse);

    // CreateUser should return a user:
    User user = userDataFetch.createUser("test", "test", true, "male", 22);
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
    boolean success = userDataFetch.deleteUser("test", "test");
    assertNotNull(success);
    assertTrue(success);
  }

  @Test
  public void testGetAllUsersQuery() {
    // define what the mock class returns when methods are called
    // this mocks the http response from the API
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

  @Test
  public void testSignIn() {
    when(test.getData(anyString(), isNull())).thenReturn(signInResponse);
    User testUser = userDataFetch.signIn("test", "test");
    assertTrue(testUser instanceof User);
  }

  @Test
  public void testDataAccessRequest() {
    User testUser1 = new User("5a9e8503c13edf22f93825e7", "test", "test", true, "female", 22);
    User testUser2 = new User("5a9e85cac13edf22f93825e8", "testo", "testo", true, "male", 22);
    DataAccessRequest request = new DataAccessRequest(null, testUser1, testUser2, "ACCEPTED");
    assertTrue(request.toString() instanceof String);
    assertNull(request.getId());
    assertTrue(request.getDataOwner() instanceof User);
    assertTrue(request.getRequestedBy() instanceof User);
    assertTrue(
        DataAccessRequest.statusFromString(request.getStatusAsString())
            instanceof DataAccessRequestStatus);
    assertTrue(DataAccessRequest.statusFromString("PENDING") instanceof DataAccessRequestStatus);
    assertTrue(DataAccessRequest.statusFromString("REJECTED") instanceof DataAccessRequestStatus);
    assertTrue(DataAccessRequest.statusFromString("") instanceof DataAccessRequestStatus);
    assertTrue(request.getStatusAsString() instanceof String);
    assertTrue(request.getStatus() instanceof DataAccessRequestStatus);
    assertTrue(DataAccessRequest.statusToString(DataAccessRequestStatus.PENDING) instanceof String);
    assertTrue(
        DataAccessRequest.statusToString(DataAccessRequestStatus.REJECTED) instanceof String);
  }
}
