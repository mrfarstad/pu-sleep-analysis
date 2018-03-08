package tdt4140.gr1816.app.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class DataGetter {

  public String getData(String query, String token) {
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
      if (token != null) {
        connection.setRequestProperty("Authorization", "Bearer " + token);
      }

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
}
