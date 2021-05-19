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
import javax.sql.DataSource;
import org.h2.tools.RunScript;
import org.romankukin.bankapi.context.AppContext;
import org.romankukin.bankapi.controller.CardHandler;
import org.romankukin.bankapi.controller.HelloHandler;
import org.romankukin.bankapi.service.CardService;

public class ClientAPI {

  private static final int PORT = 8080;
  private static final int BACKLOG = 0;
  private static final Logger logger = Logger.getLogger(ClientAPI.class.getName());
  private HttpServer server;
  private static final AppContext appContext = new AppContext();

  private void mapHandlers() {
    server.createContext("/api/hello", new HelloHandler());
    server.createContext("/api/card/", new CardHandler((CardService) appContext.getBean("cardService")));
  }

  private String SCRIPT_PATH = "/Users/a19188182/development/bank-api/src/main/resources/script/create_table.sql";

  public void runServer() throws IOException, SQLException {
    try (Connection connection = ((DataSource) appContext.getBean("dataSource")).getConnection()) {
      RunScript.execute(connection, new FileReader(SCRIPT_PATH));

      server = HttpServer.create(new InetSocketAddress(PORT), BACKLOG);
      ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
      mapHandlers();
      server.setExecutor(threadPoolExecutor);
      server.start();
      logger.info("Server started on port " + PORT);
    }
  }

  public static void main(String[] args) throws IOException, SQLException {
    ClientAPI clientAPI = new ClientAPI();
    clientAPI.runServer();
  }
}
