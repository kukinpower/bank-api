package org.romankukin.bankapi.test.integrated;

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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.romankukin.bankapi.BankApp;
import org.romankukin.bankapi.dbconnection.DatabaseConnection;
import org.romankukin.bankapi.dbconnection.FileDatabaseConnection;
import org.romankukin.bankapi.service.ResponseStatus;
import org.romankukin.bankapi.test.IntegratedTest;
import org.romankukin.bankapi.test.mapper.ResponseMapperBicycle;

public class FileDbTest implements IntegratedTest {

  private static BankApp app;

  @BeforeAll
  private static void init() {
    app = new BankApp(new FileDatabaseConnection());
    app.initDatabase(DatabaseConnection.CREATE_DB_PATH);
    app.runServer();
  }

  @AfterAll
  private static void stop() {
    if (app.isRunning()) {
      app.stop();
    }
  }

  @Test
  void cardCreateTest() throws IOException {
    URL url = new URL(createUrl("api/card"));
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(POST);
    connection.setRequestProperty("Content-Type", "application/json");
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
