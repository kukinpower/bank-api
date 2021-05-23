package org.romankukin.bankapi;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import javax.sql.DataSource;
import org.h2.tools.RunScript;
import org.romankukin.bankapi.context.AppContext;
import org.romankukin.bankapi.controller.CardHandler;
import org.romankukin.bankapi.controller.HelloHandler;
import org.romankukin.bankapi.service.card.impl.CardServiceImpl;

public class BankApp {

  private static final int PORT = 8080;
  private static final int BACKLOG = 0;
  private static final Logger logger = LoggerFactory.getLogger(BankApp.class);
  private HttpServer server;
  private static final AppContext appContext = new AppContext();

  private void mapHandlers() {
    server.createContext("/api/hello", new HelloHandler());
    server.createContext("/api/card",
        new CardHandler((CardServiceImpl) appContext.getBean("cardService")));
  }

  private String SCRIPT_PATH = "/Users/romankukin/development/bank-api/src/main/resources/script/create_table.sql";

  private void initDatabase() throws FileNotFoundException, SQLException {
    try (Connection connection = ((DataSource) appContext.getBean("dataSource")).getConnection()) {
      RunScript.execute(connection, new FileReader(SCRIPT_PATH));
      logger.debug("Database created");
    }
  }

  public void runServer() throws IOException, SQLException {
    initDatabase();
    server = HttpServer.create(new InetSocketAddress(PORT), BACKLOG);
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    mapHandlers();
    server.setExecutor(threadPoolExecutor);
    server.start();
    logger.debug("Server started on port {}", PORT);
  }

  public static void main(String[] args) throws IOException, SQLException {
    BankApp bankApp = new BankApp();
    bankApp.runServer();
  }
}
