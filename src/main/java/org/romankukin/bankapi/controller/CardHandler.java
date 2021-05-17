package org.romankukin.bankapi.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class CardHandler implements HttpHandler {

  private String getCard(HttpExchange exchange) {
    return "card: some card";
  }

  private String getAllCards(HttpExchange exchange) {
    return "card1, card2";
  }

  private String addNewCard(HttpExchange exchange) {
    return "new card added";
  }

  private void handleResponse(HttpExchange exchange, String response) throws IOException {
    exchange.sendResponseHeaders(200, response.length());
    OutputStream outputStream = exchange.getResponseBody();

    outputStream.write(response.getBytes(StandardCharsets.UTF_8));
    outputStream.flush();
    outputStream.close();
  }

  private String handleRequest(HttpExchange exchange, String path) {
    switch (path) {
      case "new": return addNewCard(exchange);
      case "get": return getCard(exchange);
      case "all": return getAllCards(exchange);
      default: return "";
    }
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String path = exchange.getRequestURI().getPath();
    String response = handleRequest(exchange, path.substring(path.lastIndexOf("/") + 1));

//    if ("GET".equals(exchange.getRequestMethod())) {
//      response = handleGetRequest(exchange);
//    }
    handleResponse(exchange, response);
  }
}
