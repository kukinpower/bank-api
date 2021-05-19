package org.romankukin.bankapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.romankukin.bankapi.model.Card;
import org.romankukin.bankapi.model.CardStatus;
import org.romankukin.bankapi.model.Currency;
import org.romankukin.bankapi.service.CardService;

//1) Выпуск новой карты по счету
//2) Проcмотр списка карт
//3) Внесение вредств
//4) Проверка баланса
public class CardHandler implements HttpHandler {

//  private static final BankDao<Card, Integer> dao = new CardDao();
//  private BankService<Card, Integer> service;
  private final static Integer CARD_NUMBER_LENGTH = 16;
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

  private Card createCardFromMap() {
    Map<String, Object> fields = new HashMap<>();
    Card card = new Card(
        (String) fields.get("number"),
        (String) fields.get("pin"),
        (Integer) fields.get("account"),
        (Currency) fields.get("currency"),
        (BigDecimal) fields.get("balance"),
        (CardStatus) fields.get("status"));
    return card;
  }

  private Card extractCardFromBody(HttpExchange exchange) throws IOException {
    String response = "";
    InputStreamReader isr =  new InputStreamReader(exchange.getRequestBody(),"utf-8");
    BufferedReader br = new BufferedReader(isr);
    String value = null;
    while ((value = br.readLine()) != null) {
      System.out.println(value);
    }
    return null;//todo
  }

  private String handleRequest(HttpExchange exchange, String path) throws JsonProcessingException {
    if (path.matches("api/card/new/[0-9]+")) {
      return service.createNewCard(exchange, Integer.parseInt(path.substring(path.lastIndexOf("/") + 1)));
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
    String response = handleRequest(exchange, path.substring(path.indexOf("/") + 1));

//    if ("GET".equals(exchange.getRequestMethod())) {
//      response = handleGetRequest(exchange);
//    }
    handleResponse(exchange, response);
    exchange.close();
  }
}
