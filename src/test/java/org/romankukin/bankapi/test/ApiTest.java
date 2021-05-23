package org.romankukin.bankapi.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.romankukin.bankapi.BankApp;
import org.romankukin.bankapi.dbconnection.DatabaseConnection;
import org.romankukin.bankapi.dbconnection.FileDatabaseConnection;
import org.romankukin.bankapi.dbconnection.InMemoryDatabaseConnection;
import org.romankukin.bankapi.service.ResponseStatus;
import org.romankukin.bankapi.test.mapper.ResponseMapperBicycle;

public class ApiTest {

  private static final String POST = "POST";
  private static final String GET = "GET";
  private static final String CARD_REGEX =
      "^\\{\\s*\"number\"\\s*:\\s*\"[0-9]{16}\","
          + "\\s*\"pin\"\\s*:\\s*\"[0-9]{4}\","
          + "\\s*\"account\"\\s*:\\s*\"[0-9]{20}\","
          + "\\s*\"currency\"\\s*:\\s*\"[A-Z]{3}\","
          + "\\s*\"balance\"\\s*:\\s*[0-9]+[.,]?[0-9]*,"
          + "\\s*\"status\"\\s*:\\s*[0-9]+\\s*}\\s*$";

  private static final BankApp app = new BankApp(new FileDatabaseConnection());

  static {
    app.initDatabase(DatabaseConnection.SCRIPT_PATH);
    app.runServer();
  }

  @Test
  void testApiCardCreate() throws IOException {
    URL url = new URL("http://localhost:8080/api/card");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(POST);
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Connection", "keep-alive");
    connection.setDoOutput(true);

    String request = new ResponseMapperBicycle("account", "12345123451234512345").toJson();
    try (OutputStream outputStream = connection.getOutputStream()) {
      byte[] bytes = request.getBytes(StandardCharsets.UTF_8);
      outputStream.write(bytes, 0, bytes.length);
    }

    String response = new BufferedReader(new InputStreamReader(connection.getInputStream()))
        .lines()
        .collect(Collectors.joining(System.lineSeparator()));

    assertTrue(response.matches(CARD_REGEX), "Card: " + response);
    assertEquals(ResponseStatus.CREATED.getCode(), connection.getResponseCode());
  }

  @Test
  void testInMemDbCreation() throws IOException {
    if (app.isRunning()) {
      app.stop();
    }
    BankApp appInMem = new BankApp(new InMemoryDatabaseConnection());
    appInMem.initDatabase(DatabaseConnection.MOCK_DB_PATH);
    appInMem.runServer();

    URL url = new URL("http://localhost:8080/api/card");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(POST);
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Connection", "keep-alive");
    connection.setDoOutput(true);

    String request = new ResponseMapperBicycle("account", "12345123451234512345").toJson();
    try (OutputStream outputStream = connection.getOutputStream()) {
      byte[] bytes = request.getBytes(StandardCharsets.UTF_8);
      outputStream.write(bytes, 0, bytes.length);
    }

    String response = new BufferedReader(new InputStreamReader(connection.getInputStream()))
        .lines()
        .collect(Collectors.joining(System.lineSeparator()));

    assertTrue(response.matches(CARD_REGEX), "Card: " + response);
    assertEquals(ResponseStatus.CREATED.getCode(), connection.getResponseCode());
  }
}
