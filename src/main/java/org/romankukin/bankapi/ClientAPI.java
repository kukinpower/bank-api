package org.romankukin.bankapi;

import com.sun.net.httpserver.HttpServer;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;
import org.h2.tools.RunScript;
import org.romankukin.bankapi.controller.CardHandler;
import org.romankukin.bankapi.controller.HelloHandler;
import org.romankukin.bankapi.dao.CardDao;
import org.romankukin.bankapi.dbconnection.DatabaseConnection;
import org.romankukin.bankapi.dbconnection.FileDatabaseConnection;
import org.romankukin.bankapi.service.CardService;

public class ClientAPI {

  private static final int PORT = 8080;
  private static final int BACKLOG = 1;
  private static final Logger logger = Logger.getLogger(ClientAPI.class.getName());
  private HttpServer server;
  private Connection connection;

  private void mapHandlers() {
    server.createContext("/api/hello", new HelloHandler());
    server.createContext("/api/card/", new CardHandler(new CardService(new CardDao(connection))));
  }

  public void openDatabaseConnection() {
    DatabaseConnection databaseConnection = new FileDatabaseConnection();
    connection = databaseConnection.establishConnection();
  }

  private String SCRIPT_PATH = "/Users/a19188182/development/bank-api/src/main/resources/script/create_table.sql";

  public void runServer() throws IOException, SQLException {

    RunScript.execute(connection, new FileReader(SCRIPT_PATH));

    server = HttpServer.create(new InetSocketAddress(PORT), BACKLOG);
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    mapHandlers();
    server.setExecutor(threadPoolExecutor);
    server.start();
    logger.info("Server started on port " + PORT);
  }

  public void stop() throws SQLException {
    connection.close();
  }

  public static void main(String[] args) throws IOException, SQLException {
    ClientAPI clientAPI = new ClientAPI();
    clientAPI.openDatabaseConnection();
    clientAPI.runServer();
//    clientAPI.stop();
  }
}
