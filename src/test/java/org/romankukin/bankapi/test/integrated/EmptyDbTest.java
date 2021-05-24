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

public class EmptyDbTest implements IntegratedTest {

  private BankApp appInMem;

  @BeforeEach
  void startServer() {
    appInMem = new BankApp(new InMemoryDatabaseConnection());
    appInMem.initDatabase(DatabaseConnection.CREATE_DB_PATH);
    appInMem.runServer();
  }

  @AfterEach
  void stopServer() {
    if (appInMem.isRunning()) {
      appInMem.stop();
    }
  }

  @Test
  void testGetCardByNumberOnEmptyDatabase() throws IOException {
    String request = new ResponseMapperBicycle("number", "4000006080001109").toParams();
    URL url = new URL(createUrl("api/card", request));
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod(GET);
    connection.setRequestProperty("Content-Type", "application/json");
    connection.setRequestProperty("Connection", "keep-alive");

    assertEquals(ResponseStatus.NOT_FOUND.getCode(), connection.getResponseCode());
  }
}
