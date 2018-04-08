package tdt4140.gr1816.app.core;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class UserDataFetch {

  protected String currentToken = null;
  private DataGetter dataGetter;
  public static UserDataFetch userDataFetch = new UserDataFetch(new DataGetter());

  public UserDataFetch(DataGetter dataGetter) {
    this.dataGetter = dataGetter;
  }

  /*
   * Generic method for querying the API
   * params:
   * 	fileName - Name of the file containing the GraphQL query
   * 	levelNodes - A list of subnodes that will be traversed in the JSON to fetch the correct data
   * 	typeReference - The type of the data that will be returned from the subnode
   * 	variables - Values will be placed next to the given key
   */

  private <T> T getGenericData(
      String fileName,
      List<String> levelNodes,
      TypeReference<T> typeReference,
      Map<String, String> variables) {
    T data = null;
    String resourcePath = "../app.core/src/main/resources/tdt4140/gr1816/app/core/";
    try {
      ObjectMapper mapper = new ObjectMapper();
      ObjectNode obj = mapper.createObjectNode();
      String query = new String(Files.readAllBytes(Paths.get(resourcePath + fileName)));
      obj.put("query", query);
      if (variables != null) {
        JsonNode variableJson = mapper.valueToTree(variables);
        obj.put("variables", variableJson.toString());
      }
      String responseJson = dataGetter.getData(obj.toString(), this.currentToken);
      JsonFactory factory = mapper.getFactory();
      JsonParser parser = factory.createParser(responseJson);
      JsonNode root = mapper.readTree(parser);
      JsonNode jsonObject = root.get("data");
      for (String node : levelNodes) {
        jsonObject = jsonObject.get(node);
      }
      data = mapper.readerFor(typeReference).readValue(jsonObject);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return data;
  }

  public void logOut() {
    this.currentToken = null;
  }

  public List<User> getAllUsers() {
    return getGenericData(
        "allUsersQuery.txt", Arrays.asList("allUsers"), new TypeReference<List<User>>() {}, null);
  }

  public User createUser(
      String username, String password, Boolean isDoctor, String gender, Integer age) {
    Map<String, String> variables = new HashMap<>();
    variables.put("username", username);
    variables.put("password", password);
    variables.put("isDoctor", isDoctor.toString());
    variables.put("gender", gender);
    variables.put("age", age.toString());
    return getGenericData(
        "createUserQuery.txt",
        Arrays.asList("createUser"),
        new TypeReference<User>() {},
        variables);
  }

  public boolean editUser(
      String newUsername, String newPassword, Integer newAge, String newGender) {
    Map<String, String> variables = new HashMap<>();
    variables.put("username", getCurrentUser().getUsername());
    variables.put("newUsername", newUsername);
    variables.put("newPassword", newPassword);
    variables.put("newAge", newAge.toString());
    variables.put("newGender", newGender);
    return getGenericData(
        "editUserQuery.txt", Arrays.asList("editUser"), new TypeReference<Boolean>() {}, variables);
  }

  public Boolean deleteUser(String username, String password, boolean isDoctor) {

    boolean deleteData = true;

    // Rid steps Data
    List<StepsData> stepsData = getStepsDataByViewer();
    for (StepsData entry : stepsData) {
      boolean success = deleteStepsData(entry.getId());
      if (!success) {
        deleteData = false;
      }
    }
    // Rid pulse Data
    List<PulseData> pulseData = getPulseDataByViewer();
    for (PulseData entry : pulseData) {
      boolean success = deletePulseData(entry.getId());
      if (!success) {
        deleteData = false;
      }
    }
    // Rid Sleep Data
    List<SleepData> sleepData = getSleepDataByViewer();
    for (SleepData entry : sleepData) {
      boolean success = deleteSleepData(entry.getId());
      if (!success) {
        deleteData = false;
      }
    }

    // Rid accessRequests
    List<DataAccessRequest> requests;
    if (isDoctor) {
      requests = getAccessRequestsByDoctor();
    } else {
      requests = getAccessRequestsToUser();
    }
    for (DataAccessRequest request : requests) {
      boolean success = deleteDataAccessRequest(request.getId());
      if (!success) {
        deleteData = false;
      }
    }

    // Rid User Credentials
    Map<String, String> variables = new HashMap<>();
    variables.put("username", username);
    variables.put("password", password);
    boolean deleteCredentials =
        getGenericData(
            "deleteUserQuery.txt",
            Arrays.asList("deleteUser"),
            new TypeReference<Boolean>() {},
            variables);
    return (deleteCredentials && deleteData);
  }

  public boolean forgotPassword(String username) {
    Map<String, String> variables = new HashMap<>();
    variables.put("username", username);
    return getGenericData(
        "forgotPasswordQuery.txt",
        Arrays.asList("forgotPassword"),
        new TypeReference<Boolean>() {},
        variables);
  }

  public User getUserById(String id) {
    try {
      return getAllUsers().stream().filter(user -> user.getId().equals(id)).findFirst().get();
    } catch (NoSuchElementException e) {
      return null;
    }
  }

  public User getUserByUsername(String name) {
    try {
      return getAllUsers()
          .stream()
          .filter(user -> user.getUsername().equals(name))
          .findFirst()
          .get();
    } catch (NoSuchElementException e) {
      return null;
    }
  }

  public User signIn(String username, String password) {
    Map<String, String> variables = new HashMap<>();
    variables.put("username", username);
    variables.put("password", password);
    User viewer =
        getGenericData(
            "signInQuery.txt",
            Arrays.asList("signinUser", "user"),
            new TypeReference<User>() {},
            variables);
    this.currentToken = viewer.getId();

    return viewer;
  }

  public User getCurrentUser() {
    return getGenericData(
        "currentUserQuery.txt", Arrays.asList("viewer"), new TypeReference<User>() {}, null);
  }

  public List<SleepData> getAllSleepData(String userId) {
    Map<String, String> variables = new HashMap<>();
    variables.put("userId", userId);
    return getGenericData(
        "allSleepDataQuery.txt",
        Arrays.asList("allSleepData"),
        new TypeReference<List<SleepData>>() {},
        variables);
  }

  public List<SleepData> getSleepDataByViewer() {
    return getGenericData(
        "sleepDataByViewerQuery.txt",
        Arrays.asList("sleepDataByViewer"),
        new TypeReference<List<SleepData>>() {},
        null);
  }

  public List<DataAccessRequest> getAccessRequestsToUser() {
    return getGenericData(
        "accessRequestsToUserQuery.txt",
        Arrays.asList("dataAccessRequestsForMe"),
        new TypeReference<List<DataAccessRequest>>() {},
        null);
  }

  public List<DataAccessRequest> getAccessRequestsByDoctor() {
    return getGenericData(
        "accessRequestsByDoctorQuery.txt",
        Arrays.asList("myDataAccessRequests"),
        new TypeReference<List<DataAccessRequest>>() {},
        null);
  }

  public SleepData createSleepData(String date, Integer duration, Integer efficiency) {
    Map<String, String> variables = new HashMap<>();
    variables.put("date", date);
    variables.put("duration", duration.toString());
    variables.put("efficiency", efficiency.toString());
    return getGenericData(
        "createSleepDataQuery.txt",
        Arrays.asList("createSleepData"),
        new TypeReference<SleepData>() {},
        variables);
  }

  public Boolean deleteSleepData(String sleepId) {
    Map<String, String> variables = new HashMap<>();
    variables.put("sleep", sleepId);
    return getGenericData(
        "deleteSleepDataQuery.txt",
        Arrays.asList("deleteSleepData"),
        new TypeReference<Boolean>() {},
        variables);
  }

  public Boolean setIsGatheringData(Boolean isGatheringData) {
    Map<String, String> variables = new HashMap<>();
    variables.put("isGatheringData", isGatheringData.toString());
    return getGenericData(
        "setIsGatheringDataQuery.txt",
        Arrays.asList("setIsGatheringData"),
        new TypeReference<Boolean>() {},
        variables);
  }

  public List<StepsData> getAllStepsData(String userId) {
    Map<String, String> variables = new HashMap<>();
    variables.put("userId", userId);
    return getGenericData(
        "allStepsDataQuery.txt",
        Arrays.asList("allStepsData"),
        new TypeReference<List<StepsData>>() {},
        variables);
  }

  public List<StepsData> getStepsDataByViewer() {
    return getGenericData(
        "stepsDataByViewerQuery.txt",
        Arrays.asList("stepsDataByViewer"),
        new TypeReference<List<StepsData>>() {},
        null);
  }

  public StepsData createStepsData(String date, Integer steps) {
    Map<String, String> variables = new HashMap<>();
    variables.put("date", date);
    variables.put("steps", steps.toString());
    return getGenericData(
        "createStepsDataQuery.txt",
        Arrays.asList("createStepsData"),
        new TypeReference<StepsData>() {},
        variables);
  }

  public Boolean deleteStepsData(String stepsId) {
    Map<String, String> variables = new HashMap<>();
    variables.put("steps", stepsId);
    return getGenericData(
        "deleteStepsDataQuery.txt",
        Arrays.asList("deleteStepsData"),
        new TypeReference<Boolean>() {},
        variables);
  }

  public List<PulseData> getAllPulseData(String userId) {
    Map<String, String> variables = new HashMap<>();
    variables.put("userId", userId);
    return getGenericData(
        "allPulseDataQuery.txt",
        Arrays.asList("allPulseData"),
        new TypeReference<List<PulseData>>() {},
        variables);
  }

  public List<PulseData> getPulseDataByViewer() {
    return getGenericData(
        "pulseDataByViewerQuery.txt",
        Arrays.asList("pulseDataByViewer"),
        new TypeReference<List<PulseData>>() {},
        null);
  }

  public PulseData createPulseData(String date, Integer restHr) {
    Map<String, String> variables = new HashMap<>();
    variables.put("date", date);
    variables.put("restHr", restHr.toString());
    return getGenericData(
        "createPulseDataQuery.txt",
        Arrays.asList("createPulseData"),
        new TypeReference<PulseData>() {},
        variables);
  }

  public Boolean deletePulseData(String pulseId) {
    Map<String, String> variables = new HashMap<>();
    variables.put("pulse", pulseId);
    return getGenericData(
        "deletePulseDataQuery.txt",
        Arrays.asList("deletePulseData"),
        new TypeReference<Boolean>() {},
        variables);
  }
  public int getAverage(String dataType) {
	  if (dataType.equals("steps")) {
		  return 6048;
	  } else if (dataType.equals("pulse")) {
		  return 124;
	  }else if (dataType.equals("sleep")) {
		  return 8;
	  }
	  return 0;
  }

  public int getAverage(String dataType) {
    if (dataType.equals("steps")) {
      return 6048;
    } else if (dataType.equals("pulse")) {
      return 124;
    } else if (dataType.equals("sleep")) {
      return 8;
    }
    return 0;
  }

  public boolean requestDataAccess(User patient) {
    return requestDataAccess(patient.getId());
  }

  public boolean requestDataAccess(String patientId) {
    Map<String, String> variables = new HashMap<>();
    variables.put("dataOwnerId", patientId);
    DataAccessRequest accessRequest =
        getGenericData(
            "requestDataAccessQuery.txt",
            Arrays.asList("requestDataAccess"),
            new TypeReference<DataAccessRequest>() {},
            variables);
    if (accessRequest.getRequestedBy().getId().equals(this.currentToken)
        && (accessRequest.getDataOwner().getId().equals(patientId))) {
      return true;
    } else {
      return false;
    }
  }

  public boolean answerDataAccessRequest(DataAccessRequest request, String answer) {
    Map<String, String> variables = new HashMap<>();
    variables.put("dataAccessRequestId", request.getId());
    variables.put("status", answer);
    DataAccessRequest accessRequest =
        getGenericData(
            "answerDataAccessRequestQuery.txt",
            Arrays.asList("answerDataAccessRequest"),
            new TypeReference<DataAccessRequest>() {},
            variables);
    if (accessRequest.getStatusAsString().equals(answer)) {
      return true;
    } else {
      return false;
    }
  }

  public List<Message> messagesForMe() {
    return getGenericData(
        "messagesForMeQuery.txt",
        Arrays.asList("messagesForMe"),
        new TypeReference<List<Message>>() {},
        null);
  }

  public List<Message> messagesByMe() {
    return getGenericData(
        "messagesByMeQuery.txt",
        Arrays.asList("messagesByMe"),
        new TypeReference<List<Message>>() {},
        null);
  }

  public Boolean createMessage(String toId, String subject, String message) {
    Map<String, String> variables = new HashMap<>();
    variables.put("toId", toId);
    variables.put("subject", subject);
    variables.put("message", message);
    Message msg =
        getGenericData(
            "createMessageQuery.txt",
            Arrays.asList("createMessage"),
            new TypeReference<Message>() {},
            variables);
    if (msg.getFrom().getId().equals(this.currentToken) && (msg.getTo().getId().equals(toId))) {
      return true;
    } else {
      return false;
    }
  }

  public boolean deleteDataAccessRequest(String requestID) {
    Map<String, String> variables = new HashMap<>();
    variables.put("dataAccessRequestId", requestID);
    return getGenericData(
        "deleteDataAccessRequestQuery.txt",
        Arrays.asList("deleteDataAccessRequest"),
        new TypeReference<Boolean>() {},
        variables);
  }

  public AverageData getMyAverageData(String fromDate, String toDate) {
    Map<String, String> variables = new HashMap<>();
    variables.put("fromDate", fromDate);
    variables.put("toDate", toDate);
    return getGenericData(
        "getMyAverageDataQuery.txt",
        Arrays.asList("getMyAverageData"),
        new TypeReference<AverageData>() {},
        variables);
  }

  public AverageData getAverageData(String fromDate, String toDate) {
    Map<String, String> variables = new HashMap<>();
    variables.put("fromDate", fromDate);
    variables.put("toDate", toDate);
    return getGenericData(
        "getAverageDataQuery.txt",
        Arrays.asList("getAverageData"),
        new TypeReference<AverageData>() {},
        variables);
  }

  public AverageData getAverageDataForUser(String userId, String fromDate, String toDate) {
    Map<String, String> variables = new HashMap<>();
    variables.put("userId", userId);
    variables.put("fromDate", fromDate);
    variables.put("toDate", toDate);
    return getGenericData(
        "getAverageDataForUserQuery.txt",
        Arrays.asList("getAverageDataForUser"),
        new TypeReference<AverageData>() {},
        variables);
  }

  public AverageData getAverageDataForUsersInAgeGroup(
      String fromDate, String toDate, int fromAge, int toAge) {
    Map<String, String> variables = new HashMap<>();
    variables.put("fromDate", fromDate);
    variables.put("toDate", toDate);
    variables.put("fromAge", new Integer(fromAge).toString());
    variables.put("toAge", new Integer(toAge).toString());
    return getGenericData(
        "getAverageDataForUsersInAgeGroupQuery.txt",
        Arrays.asList("getAverageDataForUsersInAgeGroup"),
        new TypeReference<AverageData>() {},
        variables);
  }
}
