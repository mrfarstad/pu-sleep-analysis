package tdt4140.gr1816.app.core;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UserDataFetch {

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

  public void signIn() {
    dataGetter.getData(signInQuery);
  }

  public List<User> getAllUsers() {
    String responseJson = dataGetter.getData(allUsersQuery);
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

  public User getUserById(String id) {
    return getAllUsers()
        .stream()
        .filter(user -> user.getId() == id)
        .collect(Collectors.toList())
        .get(0);
  }

  public User getCurrentUser() {
    String responseJson = dataGetter.getData(currentUserQuery);
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

  public User getUserByID(String id) {
    return getAllUsers()
        .stream()
        .filter(user -> user.getId() == id)
        .collect(Collectors.toList())
        .get(0);
  }

  public List<DataAccessRequest> getAccessRequestsToUser() {
    String responseJson = dataGetter.getData(accessRequestsToUserQuery);
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
    String responseJson = dataGetter.getData(accessRequestsByDoctorQuery);
    ObjectMapper mapper = new ObjectMapper();
    JsonFactory factory = mapper.getFactory();
    JsonParser parser;
    List<DataAccessRequest> requests = null;
    try {
      parser = factory.createParser(responseJson);
      JsonNode root = mapper.readTree(parser);
      JsonNode thirdJsonObject = root.get("data").get("myDataAccessRequests");
      System.out.println(requests);
      requests =
          mapper
              .readerFor(new TypeReference<List<DataAccessRequest>>() {})
              .readValue(thirdJsonObject);
      System.out.println(requests);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return requests;
  }

  public static void main(String[] args) {
    UserDataFetch userDataFetch = new UserDataFetch(new DataGetter());
    System.out.println(userDataFetch.getAllUsers());
    userDataFetch.signIn();
    System.out.println(userDataFetch.getCurrentUser());
    System.out.println(userDataFetch.getAccessRequestsToUser());
    System.out.println(userDataFetch.getAccessRequestsByDoctor());
  }
}
