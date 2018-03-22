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

  public <T> T getGenericData(
      String fileName,
      List<String> levelNodes,
      TypeReference<T> typeReference,
      Map<String, String> variables) {
    T data = null;
    String resourcePath = "src/main/resources/tdt4140/gr1816/app/core/";
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

  public Boolean deleteUser(String username, String password) {
    Map<String, String> variables = new HashMap<>();
    variables.put("username", username);
    variables.put("password", password);
    return getGenericData(
        "deleteUserQuery.txt",
        Arrays.asList("deleteUser"),
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

  public List<SleepData> getAllSleepData() {
    return getGenericData(
        "allSleepDataQuery.txt",
        Arrays.asList("allSleepData"),
        new TypeReference<List<SleepData>>() {},
        null);
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

  public List<StepsData> getAllStepsData() {
    return getGenericData(
        "allStepsDataQuery.txt",
        Arrays.asList("allStepsData"),
        new TypeReference<List<StepsData>>() {},
        null);
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

  public List<PulseData> getAllPulseData() {
    return getGenericData(
        "allPulseDataQuery.txt",
        Arrays.asList("allPulseData"),
        new TypeReference<List<PulseData>>() {},
        null);
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
}
