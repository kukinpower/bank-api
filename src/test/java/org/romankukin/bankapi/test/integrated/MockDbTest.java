package org.romankukin.bankapi.test.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    appInMem.initDatabase(DatabaseConnection.MOCK_DB_PATH);
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
  
  private static String getResponse(HttpURLConnection connection) throws IOException {
    return new BufferedReader(new InputStreamReader(connection.getInputStream()))
        .lines()
        .collect(Collectors.joining(System.lineSeparator()));

  }

  @Test
  void cardGetByNumberOkTest() throws IOException {
    String request = new ResponseMapperBicycle("number", "4000006080001109").toParams();
    URL url = new URL(createUrl("api/card", request));
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(GET);
    connection.setRequestProperty("Content-Type", "application/json");

    String response = getResponse(connection);

    assertTrue(response.matches(CARD_STATUS_REGEX), "Card status: " + response);
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

    assertTrue(response.matches(CARD_STATUS_REGEX), "Card status: " + response);
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

}
