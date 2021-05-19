package org.romankukin.bankapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import org.romankukin.bankapi.service.CardService;

//1) Выпуск новой карты по счету
//2) Проcмотр списка карт
//3) Внесение вредств
//4) Проверка баланса
public class CardHandler implements HttpHandler {

  private final static Integer CARD_NUMBER_LENGTH = 16;
  private final CardService service;

  public CardHandler(CardService service) {
    this.service = service;
  }

  private void handleResponse(HttpExchange exchange, String response) throws IOException {
    exchange.sendResponseHeaders(200, response.length());
    OutputStream outputStream = exchange.getResponseBody();

    outputStream.write(response.getBytes(StandardCharsets.UTF_8));
    outputStream.flush();
    outputStream.close();
  }

  private String handleRequest(HttpExchange exchange, String path)
      throws JsonProcessingException, SQLException {
    if (path.matches(String.format("api/card/new/[0-9]{%d}", CardService.ACCOUNT_LENGTH))) {
      return service.createNewCard(exchange, path.substring(path.lastIndexOf("/") + 1));
    } else if (path.matches("api/card/get/all")) {
      return service.getAllCards();
    } else if (path.matches("api/card/get/4000002698974233")) {
      return service.getCard(exchange);
    }
    switch (path) {
      case "get": return service.getCard(exchange);
      case "all": return service.listAllCards(exchange);
      case "activate": return service.activateCard(exchange);
      case "close": return service.closeCard(exchange);
      default: {
        return "";
      }
    }
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String path = exchange.getRequestURI().getPath();
    String response = null;
    try {
      response = handleRequest(exchange, path.substring(path.indexOf("/") + 1));
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

//    if ("GET".equals(exchange.getRequestMethod())) {
//      response = handleGetRequest(exchange);
//    }
    handleResponse(exchange, response);
    exchange.close();
  }
}
