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

  public String deleteUserQuery =
      "{\"query\":\"mutation{deleteUser(auth:{username:\\\"test\\\" password:\\\"test\\\"})}\"}";

  public String allUsersQuery = "{\"query\":\"query{allUsers{id username isDoctor gender age}}\"}";
  public String currentUserQuery = "{\"query\":\"query{viewer{id username isDoctor gender age}}\"}";
  public String accessRequestsToUserQuery =
      "{\"query\":\"query{dataAccessRequestsForMe{id dataOwner{id username isDoctor gender age} requestedBy{id username isDoctor gender age}status}}\"}";
  public String accessRequestsByDoctorQuery =
      "{\"query\":\"query{myDataAccessRequests{id dataOwner{id username isDoctor gender age} requestedBy{id username isDoctor gender age}status}}\"}";

  private DataGetter dataGetter;

  public static UserDataFetch userDataFetch = new UserDataFetch(new DataGetter());

  public UserDataFetch(DataGetter dataGetter) {
    this.dataGetter = dataGetter;
  }

  // Returns user

  public boolean requestDataAccess(User patient) {
    String mutation =
        "{\"query\":\"mutation{requestDataAccess(dataOwnerId: \\\""
            + patient.getId()
            + "\\\"){id dataOwner { id username isDoctor gender age }requestedBy { id username isDoctor gender age } status }}\"}";
    String responseJson = dataGetter.getData(mutation, this.currentToken);

    ObjectMapper mapper = new ObjectMapper();
    JsonFactory factory = mapper.getFactory();
    JsonParser parser;
    DataAccessRequest accessRequest = null;
    try {
      parser = factory.createParser(responseJson);
      JsonNode root = mapper.readTree(parser);
      JsonNode thirdJsonObject = root.get("data").get("requestDataAccess");
      accessRequest =
          mapper.readerFor(new TypeReference<DataAccessRequest>() {}).readValue(thirdJsonObject);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (accessRequest.getRequestedBy().getId().equals(this.currentToken)
        && (accessRequest.getDataOwner().getId().equals(patient.getId()))) {
      return true;
    } else {
      return false;
    }
  }

  public boolean answerDataAccessRequest(DataAccessRequest request, String answer) {
    String mutation =
        "{\"query\":\"mutation{answerDataAccessRequest(dataAccessRequestId: \\\""
            + request.getId()
            + "\\\", status: "
            + answer
            + "){id dataOwner { id username isDoctor gender age }requestedBy { id username isDoctor gender age }status }}\"}";

    String responseJson = dataGetter.getData(mutation, this.currentToken);

    ObjectMapper mapper = new ObjectMapper();
    JsonFactory factory = mapper.getFactory();
    JsonParser parser;
    DataAccessRequest accessRequest = null;
    try {
      parser = factory.createParser(responseJson);
      JsonNode root = mapper.readTree(parser);
      JsonNode thirdJsonObject = root.get("data").get("answerDataAccessRequest");
      accessRequest =
          mapper.readerFor(new TypeReference<DataAccessRequest>() {}).readValue(thirdJsonObject);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (accessRequest.getStatusAsString().equals(answer)) {
      return true;
    } else {
      return false;
    }
  }

  public <T> T getGenericData(
      String fileName,
      List<String> levelNodes,
      TypeReference<T> typeReference,
      Map<String, String> variables) {
    T sleepData = null;
    String resourcePath = "../app.core/src/main/resources/tdt4140/gr1816/app/core/";
    try {
      ObjectMapper mapper = new ObjectMapper();
      ObjectNode obj = mapper.createObjectNode();
      String allSleepDataQuery = new String(Files.readAllBytes(Paths.get(resourcePath + fileName)));
      obj.put("query", allSleepDataQuery);
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
      sleepData = mapper.readerFor(typeReference).readValue(jsonObject);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return sleepData;
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

  public static void main(String[] args) {
    UserDataFetch data = new UserDataFetch(new DataGetter());
    //    data.signIn("test", "test");
    System.out.println(data.createUser("test", "test", true, "female", 20));
    //    System.out.println(data.getAllUsers());
    System.out.println(data.signIn("test", "test"));
    System.out.println(data.currentToken);
    System.out.println(data.getCurrentUser());
    //    System.out.println(data.getAccessRequestsToUser());
    System.out.println(data.getAccessRequestsByDoctor());
    //    List<SleepData> sleep = data.getAllSleepData();
    //    List<StepsData> steps = data.getAllStepsData();
    //    List<PulseData> pulse = data.getAllPulseData();
    //    System.out.println(sleep);
    //    System.out.println(steps);
    //    System.out.println(pulse);
    //    List<SleepData> sleepViewer = data.getSleepDataByViewer();
    //    List<StepsData> stepsViewer = data.getStepsDataByViewer();
    //    List<PulseData> pulseViewer = data.getPulseDataByViewer();
    //    System.out.println(sleepViewer);
    //    System.out.println(stepsViewer);
    //    System.out.println(pulseViewer);
    //    SleepData sleepCreate = data.createSleepData("2018-03-20", 10, 10);
    //    StepsData stepsCreate = data.createStepsData("2018-03-20", 10);
    //    PulseData pulseCreate = data.createPulseData("2018-03-20", 10);
    //    System.out.println(sleepCreate);
    //    System.out.println(stepsCreate);
    //    System.out.println(pulseCreate);
    //    Boolean sleepDelete = data.deleteSleepData(sleepCreate.getId());
    //    Boolean stepsDelete = data.deleteStepsData(stepsCreate.getId());
    //    Boolean pulseDelete = data.deletePulseData(pulseCreate.getId());
    //    System.out.println(sleepDelete);
    //    System.out.println(stepsDelete);
    //    System.out.println(pulseDelete);
  }
}
