package org.romankukin.bankapi.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HelloHandler implements HttpHandler {

  private String handleGetRequest(HttpExchange exchange) {
    return "<h1>hello!</h1>";
  }

  private void handleResponse(HttpExchange exchange, String response) throws IOException {
    exchange.sendResponseHeaders(200, response.length());
    OutputStream outputStream = exchange.getResponseBody();

    outputStream.write(response.getBytes(StandardCharsets.UTF_8));
    outputStream.flush();
    outputStream.close();
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String response = "";
    if ("GET".equals(exchange.getRequestMethod())) {
      response = handleGetRequest(exchange);
    }
    handleResponse(exchange, response);
  }
}
