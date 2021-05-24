package org.romankukin.bankapi.test.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.romankukin.bankapi.BankApp;
import org.romankukin.bankapi.dbconnection.DatabaseConnection;
import org.romankukin.bankapi.dbconnection.InMemoryDatabaseConnection;
import org.romankukin.bankapi.service.ResponseStatus;
import org.romankukin.bankapi.test.IntegratedTest;
import org.romankukin.bankapi.test.mapper.ResponseMapperBicycle;

public class MockDbTest implements IntegratedTest {

  private static BankApp appInMem;

  @BeforeAll
  static void startServer() {
    appInMem = new BankApp(new InMemoryDatabaseConnection());
    appInMem.initDatabase(DatabaseConnection.CREATE_DB_PATH);
    appInMem.mockDatabase(DatabaseConnection.MOCK_DB_PATH);
    appInMem.runServer();
  }

  @AfterAll
  static void stopServer() {
    if (appInMem.isRunning()) {
      appInMem.stop();
    }
  }

  @Test
  void cardGetByNumberBadRequestTest() throws IOException {
    String request = new ResponseMapperBicycle("number", "some-text").toParams();
    URL url = new URL(createUrl("api/card", request));
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(GET);
    connection.setRequestProperty("Content-Type", "application/json");

    assertEquals(ResponseStatus.BAD_REQUEST.getCode(), connection.getResponseCode());
  }

  @Test
  void cardGetByNumberOkTest() throws IOException {
    String request = new ResponseMapperBicycle("number", "4000006080001109").toParams();
    URL url = new URL(createUrl("api/card", request));
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(GET);
    connection.setRequestProperty("Content-Type", "application/json");

    String response = getResponse(connection);

    assertTrue(response.matches(CARD_REGEX), "Card status: " + response);
    assertEquals(ResponseStatus.OK.getCode(), connection.getResponseCode());
  }

  @Test
  void cardStatusByNumberTest() throws IOException {
    String request = new ResponseMapperBicycle("number", "4000006080001109").toParams();
    URL url = new URL(createUrl("api/card/status", request));
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(GET);
    connection.setRequestProperty("Content-Type", "application/json");

    String response = getResponse(connection);

    assertTrue(response.matches(CARD_STATUS_AND_NUMBER_REGEX), "Card status: " + response);
    assertEquals(ResponseStatus.OK.getCode(), connection.getResponseCode());
  }

  @Test
  void cardBalanceByNumberTest() throws IOException {
    String request = new ResponseMapperBicycle("number", "4000006080001109").toParams();
    URL url = new URL(createUrl("api/card/balance", request));
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(GET);
    connection.setRequestProperty("Content-Type", "application/json");
    
    String response = getResponse(connection);

    assertTrue(response.matches(CARD_BALANCE_REGEX), "Card balance: " + response);
    assertEquals(ResponseStatus.OK.getCode(), connection.getResponseCode());
  }

  @Test
  void cardAllTest() throws IOException {
    URL url = new URL(createUrl("api/card/all"));
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(GET);
    connection.setRequestProperty("Content-Type", "application/json");

    String response = getResponse(connection);

    assertTrue(response.matches(CARD_ARRAY_REGEX), "Cards: " + response);
    assertEquals(ResponseStatus.OK.getCode(), connection.getResponseCode());
  }

  @Test
  void cardAllStatusTest() throws IOException {
    URL url = new URL(createUrl("api/card/all/status"));
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(GET);
    connection.setRequestProperty("Content-Type", "application/json");

    String response = getResponse(connection);

    assertTrue(response.matches(CARD_STATUS_ARRAY_REGEX), "Cards status: " + response);
    assertEquals(ResponseStatus.OK.getCode(), connection.getResponseCode());
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

  @Test
  void cardDepositTest() throws IOException {
    URL url = new URL(createUrl("api/card/deposit"));
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(POST);
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setDoOutput(true);

    String request = new ResponseMapperBicycle(
        "number", "4000000967827322",
        "amount", BigDecimal.valueOf(143.99))
        .toJson();

    try (OutputStream outputStream = connection.getOutputStream()) {
      byte[] bytes = request.getBytes(StandardCharsets.UTF_8);
      outputStream.write(bytes, 0, bytes.length);
    }

    String response = new BufferedReader(new InputStreamReader(connection.getInputStream()))
        .lines()
        .collect(Collectors.joining(System.lineSeparator()));

    assertTrue(response.matches(CARD_DEPOSIT_REGEX), "Card and amount: " + response);
    assertEquals(ResponseStatus.OK.getCode(), connection.getResponseCode());
  }


}
