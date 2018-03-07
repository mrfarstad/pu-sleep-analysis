package tdt4140.gr1816.app.core;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;

public class UserDataFetch {

  protected String currentToken = null;

  public String createUserQuery =
      "{\"query\":\"mutation{createUser(authProvider:{username:\\\"test\\\" password:\\\"test\\\"} isDoctor: true gender:\\\"male\\\" age: 22){username}}\"}";
  public String deleteUserQuery =
      "{\"query\":\"mutation{deleteUser(auth:{username:\\\"test\\\" password:\\\"test\\\"})}\"}";
  public String signInQuery =
      "{\"query\":\"mutation{signinUser(auth:{username:\\\"test\\\" password:\\\"test\\\"}){token}}\"}";
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
  public User createUser() {
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

  public Boolean deleteUser() {
    this.dataGetter.getData(deleteUserQuery, null);
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

  public void signIn() {
    String responseJson = dataGetter.getData(signInQuery, null);
    ObjectMapper mapper = new ObjectMapper();
    JsonFactory factory = mapper.getFactory();
    JsonParser parser;
    String token = null;
    try {
      parser = factory.createParser(responseJson);
      JsonNode root = mapper.readTree(parser);
      JsonNode thirdJsonObject = root.get("data").get("signinUser").get("token");
      token = mapper.readerFor(new TypeReference<String>() {}).readValue(thirdJsonObject);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    this.currentToken = token;
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
    return getAllUsers().stream().filter(user -> user.getId().equals(id)).findFirst().get();
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
