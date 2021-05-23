package org.romankukin.bankapi.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.romankukin.bankapi.BankApp;
import org.romankukin.bankapi.service.ResponseStatus;
import org.romankukin.bankapi.test.mapper.ResponseMapperBicycle;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
