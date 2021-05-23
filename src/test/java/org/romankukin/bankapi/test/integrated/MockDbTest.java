package org.romankukin.bankapi.test.integrated;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.romankukin.bankapi.BankApp;
import org.romankukin.bankapi.dbconnection.DatabaseConnection;
import org.romankukin.bankapi.dbconnection.InMemoryDatabaseConnection;
import org.romankukin.bankapi.service.ResponseStatus;
import org.romankukin.bankapi.test.IntegratedTest;
import org.romankukin.bankapi.test.mapper.ResponseMapperBicycle;

public class MockDbTest implements IntegratedTest {

  private BankApp appInMem;

  @BeforeEach
  void startServer() {
    appInMem = new BankApp(new InMemoryDatabaseConnection());
    appInMem.initDatabase(DatabaseConnection.MOCK_DB_PATH);
    appInMem.runServer();
  }

  @AfterEach
  void stopServer() {
    if (appInMem.isRunning()) {
      appInMem.stop();
    }
  }

  @Test
  void testGetCardByNumberOn() throws IOException {
    String request = new ResponseMapperBicycle("number", "some-text").toParams();
    URL url = new URL(createUrl("api/card", request));
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(GET);
    connection.setRequestProperty("Content-Type", "application/json");

    assertEquals(ResponseStatus.BAD_REQUEST.getCode(), connection.getResponseCode());
  }
}
