package org.romankukin.bankapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.romankukin.bankapi.dao.BankDao;
import org.romankukin.bankapi.dao.CardDao;
import org.romankukin.bankapi.model.Card;
import org.romankukin.bankapi.service.BankService;
import org.romankukin.bankapi.service.CardService;

//1) Выпуск новой карты по счету
//2) Проcмотр списка карт
//3) Внесение вредств
//4) Проверка баланса
public class CardHandler implements HttpHandler {

//  private static final BankDao<Card, Integer> dao = new CardDao();
//  private BankService<Card, Integer> service;
  private final CardService service;

  public CardHandler(CardService service) {
    this.service = service;
  }

  private void map() {

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
      case "new": return service.addNewCard(exchange);
      case "get": return service.getCard(exchange);
      case "all": return service.listAllCards(exchange);
      case "activate": return service.activateCard(exchange);
      case "close": return service.closeCard(exchange);
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
