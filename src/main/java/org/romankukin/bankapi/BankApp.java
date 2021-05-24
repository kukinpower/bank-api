package org.romankukin.bankapi;

import com.sun.net.httpserver.HttpServer;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import javax.sql.DataSource;
import org.h2.tools.RunScript;
import org.romankukin.bankapi.context.AppContext;
import org.romankukin.bankapi.controller.CardHandler;
import org.romankukin.bankapi.controller.HelloHandler;
import org.romankukin.bankapi.dbconnection.DatabaseConnection;
import org.romankukin.bankapi.dbconnection.FileDatabaseConnection;
import org.romankukin.bankapi.exception.ConnectionNotEstablishedException;
import org.romankukin.bankapi.exception.CouldNotStartServerException;
import org.romankukin.bankapi.service.card.impl.CardServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BankApp {

  private static final int PORT = 8080;
  private static final int BACKLOG = 0;
  private static final Logger logger = LoggerFactory.getLogger(BankApp.class);
  private HttpServer server;
  private final AppContext appContext;
  private boolean serverIsRunning;

  public BankApp(DatabaseConnection databaseConnection) {
    appContext = new AppContext(databaseConnection);
    serverIsRunning = false;
  }

  private void mapHandlers() {
    server.createContext("/api/hello", new HelloHandler());
    server.createContext("/api/card",
        new CardHandler((CardServiceImpl) appContext.getBean("cardService")));
  }

  private void runScriptOnDatabase(String scriptPath) {
    try (Connection connection = ((DataSource) appContext.getBean("dataSource")).getConnection()) {
      RunScript.execute(connection, new FileReader(
          Objects.requireNonNull(getClass().getClassLoader().getResource(scriptPath)).getFile()));
      logger.debug("Database created");
    } catch (FileNotFoundException | SQLException e) {
      logger.error(e.getMessage());
      throw new ConnectionNotEstablishedException();
    }
  }

  public void initDatabase(String scriptPath) {
    runScriptOnDatabase(scriptPath);
  }

  public void mockDatabase(String scriptPath) {
    runScriptOnDatabase(scriptPath);
  }

  public boolean isRunning() {
    return serverIsRunning;
  }

  public void stop() {
    server.stop(0);
    serverIsRunning = false;
  }

  public void runServer() {
    try {
      server = HttpServer.create(new InetSocketAddress(PORT), BACKLOG);
      ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
      mapHandlers();
      server.setExecutor(threadPoolExecutor);
      server.start();
      serverIsRunning = true;
      logger.debug("Server started on port {}", PORT);
    } catch (IOException e) {
      throw new CouldNotStartServerException();
    }
  }

  public static void main(String[] args) {
    BankApp bankApp = new BankApp(new FileDatabaseConnection());
    bankApp.initDatabase(DatabaseConnection.CREATE_DB_PATH);
    bankApp.runServer();
  }
}
