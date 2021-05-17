package org.romankukin.bankapi;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;
import org.romankukin.bankapi.controller.CardHandler;
import org.romankukin.bankapi.controller.HelloHandler;

//1) Выпуск новой карты по счету
//2) Проcмотр списка карт
//3) Внесение вредств
//4) Проверка баланса
public class ClientAPI {

  private static final int PORT = 8080;
  private static final int BACKLOG = 1;
  private static final Logger logger = Logger.getLogger(ClientAPI.class.getName());
  private HttpServer server;

  private void mapHandlers() {
    server.createContext("/hello", new HelloHandler());
    server.createContext("/card/", new CardHandler());
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
    clientAPI.runServer();
  }
}
