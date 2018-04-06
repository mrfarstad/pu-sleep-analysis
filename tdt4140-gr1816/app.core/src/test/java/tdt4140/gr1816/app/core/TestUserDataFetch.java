package tdt4140.gr1816.app.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import org.junit.Test;
import tdt4140.gr1816.app.core.DataAccessRequest.DataAccessRequestStatus;

public class TestUserDataFetch {

  //  create mock
  private DataGetter test = mock(DataGetter.class);

  private UserDataFetch userDataFetch = new UserDataFetch(test);
  String resourceResponsePath = "src/test/resources/tdt4140/gr1816/app/core/";

  @Test
  public void testCreateUserQuery() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(Paths.get(resourceResponsePath + "createUserResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);

    assertEquals(test.getData("", null), response);

    // CreateUser should return a user:
    User user = userDataFetch.createUser("test", "test", true, "female", 22);
    assertTrue(user instanceof User);
    assertNotNull(user);
    assertTrue(user.getUsername().equals("test"));
    assertTrue(user.isDoctor());
    assertTrue(user.getGender().equals("female"));
    assertTrue(user.getAge() == 22);
  }

  @Test
  public void testDeleteUserQuery() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(Paths.get(resourceResponsePath + "deleteUserResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);

    assertEquals(test.getData("", null), response);

    // UserDataFetch should return a boolean (success)
    // WILL BE FIXED:
    /*boolean success = userDataFetch.deleteUser("test", "test");
    assertNotNull(success);
    assertTrue(success);*/
  }

  @Test
  public void testGetAllUsersQuery() {
    String response = "";
    try {
      response =
          new String(Files.readAllBytes(Paths.get(resourceResponsePath + "allUsersResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);

    assertEquals(test.getData("", null), response);
    List<User> users = userDataFetch.getAllUsers();
    assertTrue(users.size() > 0);
    for (User user : users) {
      assertTrue(user.getId() instanceof String);
      assertTrue(user.getUsername() instanceof String);
      assertTrue(new Boolean(user.isDoctor()) instanceof Boolean);
      assertTrue(user.getGender() instanceof String);
      assertTrue(user.getAge() > -1);
    }
  }

  @Test
  public void testGetCurrentUserQuery() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(Paths.get(resourceResponsePath + "currentUserResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);

    assertEquals(test.getData("", null), response);
    User user = userDataFetch.getCurrentUser();
    assertNotNull(user);
    assertTrue(user instanceof User);
    assertTrue(user.getUsername().equals("test"));
    assertTrue(user.isDoctor());
    assertTrue(user.getGender().equals("male"));
    assertTrue(user.getAge() == 22);
  }

  @Test
  public void testGetUserById() {
    String response = "";
    try {
      response =
          new String(Files.readAllBytes(Paths.get(resourceResponsePath + "allUsersResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);

    assertEquals(test.getData("", null), response);

    User user = userDataFetch.getUserById("5ab173c1c13edf146111e7bb");
    assertTrue(user instanceof User);
    assertNotNull(user);
    assertTrue(user.getUsername().equals("test"));
    assertTrue(user.isDoctor());
    assertTrue(user.getGender().equals("male"));
    assertTrue(user.getAge() == 22);
  }

  @Test
  public void testGetUserByUsername() {
    String response = "";
    try {
      response =
          new String(Files.readAllBytes(Paths.get(resourceResponsePath + "allUsersResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);

    assertEquals(test.getData("", null), response);

    User user = userDataFetch.getUserByUsername("test");
    assertTrue(user instanceof User);
    assertNotNull(user);
    assertTrue(user.getId().equals("5ab173c1c13edf146111e7bb"));
    assertTrue(user.getUsername().equals("test"));
    assertTrue(user.isDoctor());
    assertTrue(user.getGender().equals("male"));
    assertTrue(user.getAge() == 22);
  }

  @Test
  public void testSignIn() {
    String response = "";
    try {
      response =
          new String(Files.readAllBytes(Paths.get(resourceResponsePath + "signInResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);

    assertEquals(test.getData("", null), response);
    User user = userDataFetch.signIn("test", "test");
    assertTrue(userDataFetch.currentToken.equals("5ab173c1c13edf146111e7bb"));
    assertTrue(user.getId().equals("5ab173c1c13edf146111e7bb"));
    assertTrue(user.getUsername().equals("test"));
    assertTrue(user.isDoctor());
    assertTrue(user.getGender().equals("male"));
    assertTrue(user.getAge() == 22);
  }

  @Test
  public void testGetAccessRequestsToUser() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(
                  Paths.get(resourceResponsePath + "accessRequestsToUserResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);

    assertEquals(test.getData("", null), response);

    assertTrue(userDataFetch.getAccessRequestsToUser() instanceof List);
    List<DataAccessRequest> requests = userDataFetch.getAccessRequestsToUser();
    assertTrue(requests.size() > 0);
    for (DataAccessRequest request : requests) {
      assertTrue(request.getId() instanceof String);
      assertTrue(request.getDataOwner() instanceof User);
      assertTrue(request.getRequestedBy() instanceof User);
      assertTrue(request.getStatus() instanceof DataAccessRequestStatus);
      assertTrue(request.getStatusAsString() instanceof String);
    }
  }

  @Test
  public void testGetAccessRequestsByDoctor() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(
                  Paths.get(resourceResponsePath + "accessRequestsByDoctorResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);

    assertEquals(test.getData("", null), response);

    assertTrue(userDataFetch.getAccessRequestsByDoctor() instanceof List);
    List<DataAccessRequest> requests = userDataFetch.getAccessRequestsByDoctor();
    assertTrue(requests.size() > 0);
    for (DataAccessRequest request : requests) {
      assertTrue(request.getId() instanceof String);
      assertTrue(request.getDataOwner() instanceof User);
      assertTrue(request.getRequestedBy() instanceof User);
      assertTrue(request.getStatus() instanceof DataAccessRequestStatus);
      assertTrue(request.getStatusAsString() instanceof String);
    }
  }

  @Test
  public void testRequestDataAccess() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(
                  Paths.get(resourceResponsePath + "requestDataAccessResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), anyString())).thenReturn(response);

    userDataFetch.currentToken = "5ab173c1c13edf146111e7bb";
    assertEquals(test.getData("", "5ab173c1c13edf146111e7bb"), response);
    assertTrue(userDataFetch.requestDataAccess("5ab26b19c13edf233e48b451"));
  }

  @Test
  public void testAnswerDataAccessRequest() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(
                  Paths.get(resourceResponsePath + "answerDataAccessRequestResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);

    assertEquals(test.getData("", null), response);
    DataAccessRequest request =
        new DataAccessRequest(
            "5ab26cd7c13edf233e48b455",
            new User("5ab26cb8c13edf233e48b454", "martin	", "martin", false, "male", 22, true),
            new User("5ab173c1c13edf146111e7bb", "mathias", "mathias", true, "male", 22, true),
            "PENDING");
    assertTrue(userDataFetch.answerDataAccessRequest(request, "ACCEPTED"));
  }

  @Test
  public void testDataAccessRequest() {
    User testUser1 = new User("5a9e8503c13edf22f93825e7", "test", "test", true, "female", 22, true);
    User testUser2 = new User("5a9e85cac13edf22f93825e8", "testo", "testo", true, "male", 22, true);
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

  @Test
  public void testGetAllSleepData() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(Paths.get(resourceResponsePath + "allSleepDataResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);
    List<SleepData> sleepData = userDataFetch.getAllSleepData("");
    assertTrue(sleepData.size() > 0);
    for (SleepData sleep : sleepData) {
      assertTrue(sleep.getId() instanceof String);
      assertTrue(sleep.getUser() instanceof User);
      assertTrue(sleep.getDate() instanceof LocalDate);
      assertTrue(sleep.getDuration() > -1);
      assertTrue(sleep.getEfficiency() > -1);
    }
  }

  @Test
  public void testGetSleepDataByViewer() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(
                  Paths.get(resourceResponsePath + "sleepDataByViewerResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);
    List<SleepData> sleepData = userDataFetch.getSleepDataByViewer();
    assertTrue(sleepData.size() > 0);
    String viewerId = sleepData.get(0).getUser().getId();
    for (SleepData sleep : sleepData) {
      assertTrue(sleep.getId() instanceof String);
      assertTrue(sleep.getUser() instanceof User);
      assertTrue(sleep.getUser().getId().equals(viewerId));
      assertTrue(sleep.getDate() instanceof LocalDate);
      assertTrue(sleep.getDuration() > -1);
      assertTrue(sleep.getEfficiency() > -1);
    }
  }

  @Test
  public void testCreateSleepData() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(Paths.get(resourceResponsePath + "createSleepDataResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);
    SleepData sleep = userDataFetch.createSleepData("2018-03-20", 20, 95);
    assertTrue(sleep.getId() instanceof String);
    assertTrue(sleep.getUser() instanceof User);
    assertTrue(sleep.getDate() instanceof LocalDate);
    assertTrue(sleep.getDuration() > -1);
    assertTrue(sleep.getEfficiency() > -1);
  }

  @Test
  public void testDeleteSleepData() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(Paths.get(resourceResponsePath + "deleteSleepDataResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);
    Boolean deleteSleep = userDataFetch.deleteSleepData("");
    assertTrue(deleteSleep);
  }

  @Test
  public void testGetAllStepsData() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(Paths.get(resourceResponsePath + "allStepsDataResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);
    List<StepsData> stepsData = userDataFetch.getAllStepsData("");
    assertTrue(stepsData.size() > 0);
    for (StepsData steps : stepsData) {
      assertTrue(steps.getId() instanceof String);
      assertTrue(steps.getUser() instanceof User);
      assertTrue(steps.getDate() instanceof LocalDate);
      assertTrue(steps.getSteps() > -1);
    }
  }

  @Test
  public void testStepsDataByViewer() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(
                  Paths.get(resourceResponsePath + "stepsDataByViewerResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);
    List<StepsData> stepsData = userDataFetch.getStepsDataByViewer();
    assertTrue(stepsData.size() > 0);
    String viewerId = stepsData.get(0).getUser().getId();
    for (StepsData steps : stepsData) {
      assertTrue(steps.getId() instanceof String);
      assertTrue(steps.getUser().getId().equals(viewerId));
      assertTrue(steps.getUser() instanceof User);
      assertTrue(steps.getDate() instanceof LocalDate);
      assertTrue(steps.getSteps() > -1);
    }
  }

  @Test
  public void testCreateStepsData() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(Paths.get(resourceResponsePath + "createStepsDataResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);
    StepsData steps = userDataFetch.createStepsData("2018-03-20", 20);
    assertTrue(steps.getId() instanceof String);
    assertTrue(steps.getUser() instanceof User);
    assertTrue(steps.getDate() instanceof LocalDate);
    assertTrue(steps.getSteps() > -1);
  }

  @Test
  public void testDeleteStepsData() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(Paths.get(resourceResponsePath + "deleteStepsDataResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);
    Boolean deleteSteps = userDataFetch.deleteStepsData("");
    assertTrue(deleteSteps);
  }

  @Test
  public void testGetAllPulseData() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(Paths.get(resourceResponsePath + "allPulseDataResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);
    List<PulseData> pulseData = userDataFetch.getAllPulseData("");
    assertTrue(pulseData.size() > 0);
    for (PulseData pulse : pulseData) {
      assertTrue(pulse.getId() instanceof String);
      assertTrue(pulse.getUser() instanceof User);
      assertTrue(pulse.getDate() instanceof LocalDate);
      assertTrue(pulse.getRestHr() > -1);
    }
  }

  @Test
  public void testGetPulseDataByViewer() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(
                  Paths.get(resourceResponsePath + "pulseDataByViewerResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);
    List<PulseData> pulseData = userDataFetch.getPulseDataByViewer();
    assertTrue(pulseData.size() > 0);
    String viewerId = pulseData.get(0).getUser().getId();
    for (PulseData pulse : pulseData) {
      assertTrue(pulse.getId() instanceof String);
      assertTrue(pulse.getUser().getId().equals(viewerId));
      assertTrue(pulse.getUser() instanceof User);
      assertTrue(pulse.getDate() instanceof LocalDate);
      assertTrue(pulse.getRestHr() > -1);
    }
  }

  @Test
  public void testCreatePulseData() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(Paths.get(resourceResponsePath + "createPulseDataResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);
    PulseData pulse = userDataFetch.createPulseData("2018-03-20", 60);
    assertTrue(pulse.getId() instanceof String);
    assertTrue(pulse.getUser() instanceof User);
    assertTrue(pulse.getDate() instanceof LocalDate);
    assertTrue(pulse.getRestHr() > -1);
  }

  @Test
  public void testDeletePulseData() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(Paths.get(resourceResponsePath + "deletePulseDataResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);
    Boolean deletePulse = userDataFetch.deletePulseData("");
    assertTrue(deletePulse);
  }

  @Test
  public void testMessagesForMe() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(Paths.get(resourceResponsePath + "messagesForMeResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), isNull())).thenReturn(response);
    List<Message> messages = userDataFetch.messagesForMe();
    assertTrue(messages.size() > 0);
    String viewerId = messages.get(0).getTo().getId();
    for (Message message : messages) {
      assertTrue(message.getId() instanceof String);
      assertTrue(message.getFrom() instanceof User);
      assertTrue(message.getFrom().getId().equals("5ab24b5fc13edf233e48b42c"));
      assertTrue(message.getTo() instanceof User);
      assertTrue(message.getTo().getId().equals(viewerId));
      assertTrue(message.getSubject() instanceof String);
      assertTrue(message.getSubject().length() > 0);
      assertTrue(message.getMessage() instanceof String);
      assertTrue(message.getMessage().length() > 0);
    }
  }

  @Test
  public void testCreateMessage() {
    String response = "";
    try {
      response =
          new String(
              Files.readAllBytes(Paths.get(resourceResponsePath + "createMessageResponse.txt")));
    } catch (IOException e) {
      fail("Wrong filename for query");
    }
    when(test.getData(anyString(), anyString())).thenReturn(response);

    User to = new User("5ab24b72c13edf233e48b42d", "test", "test", true, "female", 22, true);
    userDataFetch.currentToken = "5ab24b5fc13edf233e48b42c";
    assertEquals(test.getData("", "5ab24b5fc13edf233e48b42c"), response);
    assertTrue(userDataFetch.createMessage(to.getId(), "subject", "message content"));
  }
}
