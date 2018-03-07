package tdt4140.gr1816.app.core;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class UserDataFetch {

  public static String getData(String query) {
    String url = "http://localhost:8080/graphql";
    String charset = java.nio.charset.StandardCharsets.UTF_8.name();

    URLConnection connection;
    String responseJson = null;
    try {
      connection = new URL(url).openConnection();
      connection.setDoOutput(true); // Triggers POST.
      connection.setRequestProperty("Accept-Charset", charset);
      connection.setRequestProperty(
          "Content-Type", "application/x-www-form-urlencoded;charset=" + charset);
      connection.setRequestProperty("Authorization", "Bearer 5a9fed68a7cb8c1f64fb59b1");

      try (OutputStream output = connection.getOutputStream()) {
        output.write(query.getBytes(charset));
      }

      InputStream response = connection.getInputStream();
      try (Scanner scanner = new Scanner(response)) {
        responseJson = scanner.useDelimiter("\\A").next();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return responseJson;
  }

  public static void signIn() {
    String query =
        "{\"query\":\"mutation{signinUser(auth:{username:\\\"test\\\" password:\\\"test\\\"}){token}}\"}";
    UserDataFetch.getData(query);
  }

  public static <T> T accessRequests(Class<T> cls, String query, String jsonNodeName) {
    String responseJson = UserDataFetch.getData(query);
    ObjectMapper mapper = new ObjectMapper();
    JsonFactory factory = mapper.getFactory();
    JsonParser parser;
    T requests = null;
    try {
      parser = factory.createParser(responseJson);
      JsonNode root = mapper.readTree(parser);
      JsonNode thirdJsonObject = root.get("data").get(jsonNodeName);
      requests = mapper.readerFor(new TypeReference<T>() {}).readValue(thirdJsonObject);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return requests;
  }

  public static List<User> getAllUsers() {
    String responseJson =
        UserDataFetch.getData("{\"query\":\"query{allUsers{id username isDoctor gender age}}\"}");
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

  public static User getCurrentUser() {
    String responseJson =
        UserDataFetch.getData("{\"query\":\"query{viewer{id username isDoctor gender age}}\"}");
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

  public static User getUserByID(String id) {
    return getAllUsers()
        .stream()
        .filter(user -> user.getId() == id)
        .collect(Collectors.toList())
        .get(0);
  }

  public static List<DataAccessRequest> getAccessRequestsToUser() {
    String responseJson =
        UserDataFetch.getData(
            "{\"query\":\"query{dataAccessRequestsForMe{requestedBy{username isDoctor gender age}status}}\"}");
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

  public static List<DataAccessRequest> getAccessRequestsByDoctor() {
    String responseJson =
        UserDataFetch.getData(
            "{\"query\":\"query{myDataAccessRequests{dataOwner{username isDoctor gender age}, status}}\"}");
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

  public static void main(String[] args) {
    System.out.println(UserDataFetch.getAllUsers());
    UserDataFetch.signIn();
    System.out.println(UserDataFetch.getCurrentUser());
    System.out.println(UserDataFetch.getAccessRequestsToUser());
    System.out.println(UserDataFetch.getAccessRequestsByDoctor());
  }
}
