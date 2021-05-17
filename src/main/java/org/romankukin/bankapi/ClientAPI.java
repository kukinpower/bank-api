package org.romankukin.bankapi;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.logging.Logger;
import org.romankukin.bankapi.controller.HelloHandler;

//1) Выпуск новой карты по счету
//2) Проcмотр списка карт
//3) Внесение вредств
//4) Проверка баланса
public class ClientAPI {

  private static Logger logger = Logger.getLogger(ClientAPI.class.getName());

  public static void main(String[] args) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(8080), 1);
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    server.createContext("/hello", new HelloHandler());

    server.setExecutor(threadPoolExecutor);
    server.start();
    logger.info("Server started on port 8001");
  }

}
