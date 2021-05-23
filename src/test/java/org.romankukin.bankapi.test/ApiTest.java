package org.romankukin.bankapi.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.romankukin.bankapi.BankApp;

class JsonMapperBicycle {
  private Map<String, String> map;

  public JsonMapperBicycle() {
    this.map = new LinkedHashMap<>();
  }

  public JsonMapperBicycle(String... args) {
    this.map = new LinkedHashMap<>();
    for (int i = 0; i < args.length; i += 2) {
      map.put(args[i], args[i + 1]);
    }
  }

  public void put(String key, String value) {
    map.put(key, value);
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("{");

    int i = 0;
    for (Entry<String, String> entry : map.entrySet()) {
      stringBuilder.append("\"")
          .append(entry.getKey())
          .append("\"")
          .append(":")
          .append("\"")
          .append(entry.getValue())
          .append("\"");
      if (i + 1 < map.size()) {
        stringBuilder.append(",");
      }
    }
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
}

public class ApiTest {

  private static final String POST = "POST";
  private static final String GET = "GET";

  private static final BankApp app = new BankApp();
  static {
    try {
      app.runServer();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  @Test
  void testApiCardCreate() throws IOException {
    URL url = new URL("http://localhost:8080/api/card");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(POST);
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Connection", "keep-alive");
    connection.setDoOutput(true);

    String request = new JsonMapperBicycle("account", "12345123451234512345").toString();
    try (OutputStream outputStream = connection.getOutputStream()) {
      byte[] bytes = request.getBytes(StandardCharsets.UTF_8);
      outputStream.write(bytes, 0, bytes.length);
    }

    String response = new BufferedReader(new InputStreamReader(connection.getInputStream()))
        .lines()
        .collect(Collectors.joining(System.lineSeparator()));

    System.out.println(response);
  }

}
