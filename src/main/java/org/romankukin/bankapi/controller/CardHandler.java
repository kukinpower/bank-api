package org.romankukin.bankapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import org.romankukin.bankapi.exception.NoRequestBodyDetectedException;
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

  private String extractAllAfterLastSlash(String path) {
    return path.substring(path.lastIndexOf("/") + 1);
  }

  protected static String extractBody(HttpExchange exchange) throws IOException {
    InputStreamReader isr =  new InputStreamReader(exchange.getRequestBody(),"utf-8");
    BufferedReader br = new BufferedReader(isr);

    String response = null;
    if ((response = br.readLine()) != null) {
      return response;
    }
    throw new NoRequestBodyDetectedException();
  }
  
  private String handlePost(HttpExchange exchange, String path)
      throws JsonProcessingException, SQLException {
    if (path.matches(String.format("api/card/new/[0-9]{%d}", CardService.ACCOUNT_LENGTH))) {
      return service.createNewCard(exchange, extractAllAfterLastSlash(path));
    }
    return "";
  }
  
  private String extractFieldFromRequestBody(HttpExchange exchange, JsonNode objectFromJson, String key) throws IOException {
    String value = objectFromJson.get(key).textValue();
    if ("number".equals(key) && value.length() != CardService.CARD_LENGTH) {
      throw new IllegalArgumentException("Bad number: " + value);
    }
    return value;
  }

  private String handleGet(HttpExchange exchange, String path)
      throws IOException, SQLException {
    
    //all needing nothing
    if ("api/card/all".equals(path)) {
      return service.getAllCards();
    }

    //all needing mapper
    JsonNode objectFromJson = new ObjectMapper().readTree(extractBody(exchange));
    String number = extractFieldFromRequestBody(exchange, objectFromJson,"number");
    
    if ("api/card".equals(path)) {
      return service.getCard(exchange, number);
    } else if ("api/card/balance".equals(path)) {
      return service.getCardBalance(exchange, number);
    } else if ("api/card/activate".equals(path)) {
      return service.activateCard(exchange, number);
    } else if ("api/card/close".equals(path)) {
      return service.closeCard(exchange, number);
    } else if ("api/card/deposit".equals(path)) {
      return service.deposit(exchange, number, extractFieldFromRequestBody(exchange, objectFromJson, "amount"));
    }

    throw new IllegalArgumentException();
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String path = exchange.getRequestURI().getPath();
    String response = null;
    try {
      response = handleGet(exchange, path.substring(path.indexOf("/") + 1));
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
