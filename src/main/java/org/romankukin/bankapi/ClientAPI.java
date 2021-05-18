package org.romankukin.bankapi;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;
import org.romankukin.bankapi.controller.CardHandler;
import org.romankukin.bankapi.controller.HelloHandler;
import org.romankukin.bankapi.dao.CardDao;
import org.romankukin.bankapi.dbconnection.DatabaseConnection;
import org.romankukin.bankapi.dbconnection.FileDatabaseConnection;
import org.romankukin.bankapi.model.Card;
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

  public void runServer() throws IOException {
    server = HttpServer.create(new InetSocketAddress(PORT), BACKLOG);
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    mapHandlers();
    server.setExecutor(threadPoolExecutor);
    server.start();
    logger.info("Server started on port " + PORT);
  }

  public static void main(String[] args) throws IOException {
    ClientAPI clientAPI = new ClientAPI();
    clientAPI.openDatabaseConnection();
    clientAPI.runServer();
  }
}
