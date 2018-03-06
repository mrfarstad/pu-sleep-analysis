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
      connection.setRequestProperty("Authorization", "5a9c4c49c13edf723a9ce172");

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

  public static List<User> getAllUsers() {

    String query = "{\"query\":\"query{allUsers{id username isDoctor gender age}}\"}";
    String responseJson = UserDataFetch.getData(query);
    ObjectMapper mapper = new ObjectMapper();
    JsonFactory factory = mapper.getFactory();
    JsonParser parser;
    List<User> userList = null;
    try {
      parser = factory.createParser(responseJson);
      JsonNode root = mapper.readTree(parser);
      JsonNode allUsers = root.get("data").get("allUsers");
      userList = mapper.readerFor(new TypeReference<List<User>>() {}).readValue(allUsers);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return userList;
  }

  public static User getCurrentUser() {
    String query = "{\"query\":\"query{viewer{id username isDoctor gender age}}\"}";
    String responseJson = UserDataFetch.getData(query);
    ObjectMapper mapper = new ObjectMapper();
    JsonFactory factory = mapper.getFactory();
    JsonParser parser;
    User user = null;
    try {
      parser = factory.createParser(responseJson);
      JsonNode root = mapper.readTree(parser);
      JsonNode viewer = root.get("data").get("viewer");
      user = mapper.readerFor(new TypeReference<User>() {}).readValue(viewer);
    } catch (JsonParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return user;
  }

  public static void signIn() {
    String query =
        "{\"query\":\"mutation{signinUser(auth:{username:\\\"test\\\" password:\\\"test\\\"}){token}}\"}";
    String responseJson = UserDataFetch.getData(query);
  }

  public static void main(String[] args) {
    List<User> users = UserDataFetch.getAllUsers();
    UserDataFetch.signIn();
    User user = UserDataFetch.getCurrentUser();
  }
}
