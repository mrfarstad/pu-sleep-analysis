package tdt4140.gr1816.app.core;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

public class UserDataFetch {

  protected String currentToken = null;

  public String deleteUserQuery =
      "{\"query\":\"mutation{deleteUser(auth:{username:\\\"test\\\" password:\\\"test\\\"})}\"}";

  public String allUsersQuery = "{\"query\":\"query{allUsers{id username isDoctor gender age}}\"}";
  public String currentUserQuery = "{\"query\":\"query{viewer{id username isDoctor gender age}}\"}";
  public String accessRequestsToUserQuery =
      "{\"query\":\"query{dataAccessRequestsForMe{requestedBy{username isDoctor gender age}status}}\"}";
  public String accessRequestsByDoctorQuery =
      "{\"query\":\"query{myDataAccessRequests{dataOwner{username isDoctor gender age}status}}\"}";

  private DataGetter dataGetter;

  public static UserDataFetch userDataFetch = new UserDataFetch(new DataGetter());

  public UserDataFetch(DataGetter dataGetter) {
    this.dataGetter = dataGetter;
  }

  // Returns user
  public User createUser(
      String username, String password, boolean isDoctor, String gender, int age) {
    String createUserQuery =
        "{\"query\":\"mutation{createUser(authProvider:{username:\\\""
            + username
            + "\\\" password:\\\""
            + password
            + "\\\"} isDoctor: "
            + isDoctor
            + " gender:\\\""
            + gender
            + "\\\" age: "
            + age
            + "){username}}\"}";
    String responseJson = dataGetter.getData(createUserQuery, null);
    ObjectMapper mapper = new ObjectMapper();
    JsonFactory factory = mapper.getFactory();
    JsonParser parser;
    User createdUser = null;
    try {
      parser = factory.createParser(responseJson);
      JsonNode root = mapper.readTree(parser);
      JsonNode thirdJsonObject = root.get("data").get("createUser");
      createdUser = mapper.readerFor(new TypeReference<User>() {}).readValue(thirdJsonObject);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return createdUser;
  }

  public Boolean deleteUser(String username, String password) {
    String query =
        "{\"query\":\"mutation{deleteUser(auth:{username:\\\""
            + username
            + "\\\" password:\\\""
            + password
            + "\\\"})}\"}";
    this.dataGetter.getData(query, null);
    String responseJson = dataGetter.getData(deleteUserQuery, null);
    ObjectMapper mapper = new ObjectMapper();
    JsonFactory factory = mapper.getFactory();
    JsonParser parser;
    Boolean success = false;
    try {
      parser = factory.createParser(responseJson);
      JsonNode root = mapper.readTree(parser);
      JsonNode thirdJsonObject = root.get("data").get("deleteUser");
      success = mapper.readerFor(new TypeReference<Boolean>() {}).readValue(thirdJsonObject);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return success;
  }

  public User signIn(String username, String password) {
    String signInQuery =
        "{\"query\":\"mutation{signinUser(auth:{username:\\\""
            + username
            + "\\\" password:\\\""
            + password
            + "\\\"}){token user{id username isDoctor gender age}}}\"}";
    String responseJson = dataGetter.getData(signInQuery, null);
    ObjectMapper mapper = new ObjectMapper();
    JsonFactory factory = mapper.getFactory();
    JsonParser parser;
    String token = null;
    User user = null;
    try {
      parser = factory.createParser(responseJson);
      JsonNode root = mapper.readTree(parser);
      JsonNode thirdJsonObject = root.get("data").get("signinUser").get("token");
      JsonNode fouthJsonObject = root.get("data").get("signinUser").get("user");
      token = mapper.readerFor(new TypeReference<String>() {}).readValue(thirdJsonObject);
      user = mapper.readerFor(new TypeReference<User>() {}).readValue(fouthJsonObject);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.currentToken = token;
    return user;
  }

  public List<User> getAllUsers() {
    String responseJson = dataGetter.getData(allUsersQuery, null);
    ObjectMapper mapper = new ObjectMapper();
    JsonFactory factory = mapper.getFactory();
    JsonParser parser;
    List<User> requests = null;
    try {
      parser = factory.createParser(responseJson);
      JsonNode root = mapper.readTree(parser);
      JsonNode thirdJsonObject = root.get("data").get("allUsers");
      requests = mapper.readerFor(new TypeReference<List<User>>() {}).readValue(thirdJsonObject);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return requests;
  }

  public User getCurrentUser() {
    String responseJson = dataGetter.getData(currentUserQuery, this.currentToken);
    ObjectMapper mapper = new ObjectMapper();
    JsonFactory factory = mapper.getFactory();
    JsonParser parser;
    User requests = null;
    try {
      parser = factory.createParser(responseJson);
      JsonNode root = mapper.readTree(parser);
      JsonNode thirdJsonObject = root.get("data").get("viewer");
      requests = mapper.readerFor(new TypeReference<User>() {}).readValue(thirdJsonObject);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return requests;
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

  public List<DataAccessRequest> getAccessRequestsToUser() {
    String responseJson = dataGetter.getData(accessRequestsToUserQuery, this.currentToken);
    ObjectMapper mapper = new ObjectMapper();
    JsonFactory factory = mapper.getFactory();
    JsonParser parser;
    List<DataAccessRequest> requests = null;
    try {
      parser = factory.createParser(responseJson);
      JsonNode root = mapper.readTree(parser);
      JsonNode thirdJsonObject = root.get("data").get("dataAccessRequestsForMe");
      requests =
          mapper
              .readerFor(new TypeReference<List<DataAccessRequest>>() {})
              .readValue(thirdJsonObject);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return requests;
  }

  public boolean requestDataAccess(User patient) {
    String mutation =
        "{\"query\":\"mutation{requestDataAccess(dataOwnerId: \\\""
            + patient.getId()
            + "\\\"){id dataOwner { id username isDoctor gender age }requestedBy { id username isDoctor gender age }status }}\"}";
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

  public List<DataAccessRequest> getAccessRequestsByDoctor() {
    String responseJson = dataGetter.getData(accessRequestsByDoctorQuery, this.currentToken);
    ObjectMapper mapper = new ObjectMapper();
    JsonFactory factory = mapper.getFactory();
    JsonParser parser;
    List<DataAccessRequest> requests = null;
    try {
      parser = factory.createParser(responseJson);
      JsonNode root = mapper.readTree(parser);
      JsonNode thirdJsonObject = root.get("data").get("myDataAccessRequests");
      requests =
          mapper
              .readerFor(new TypeReference<List<DataAccessRequest>>() {})
              .readValue(thirdJsonObject);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return requests;
  }
}
