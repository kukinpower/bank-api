package org.romankukin.bankapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import org.romankukin.bankapi.controller.scheme.CardBalanceUpdateRequest;
import org.romankukin.bankapi.controller.scheme.CardStatusUpdateRequest;
import org.romankukin.bankapi.controller.scheme.CardUpdateRequest;
import org.romankukin.bankapi.model.CardStatus;
import org.romankukin.bankapi.service.CardService;

//1) Выпуск новой карты по счету
//2) Проcмотр списка карт
//3) Внесение вредств
//4) Проверка баланса
public class CardHandler implements HttpHandler, BankHandler {

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

  private void handleErrorResponse(HttpExchange exchange, Throwable throwable) throws IOException {
    String errorMessage = throwable.getMessage();
    exchange.sendResponseHeaders(400, errorMessage.length());
    OutputStream outputStream = exchange.getResponseBody();

    outputStream.write(errorMessage.getBytes(StandardCharsets.UTF_8));
    outputStream.flush();
    outputStream.close();
  }

  private String extractAllAfterLastSlash(String path) {
    return path.substring(path.lastIndexOf("/") + 1);
  }

//  protected static String extractBody(HttpExchange exchange) throws IOException {
//    InputStreamReader isr =  new InputStreamReader(exchange.getRequestBody(),"utf-8");
//    BufferedReader br = new BufferedReader(isr);
//
//    String response = null;
//    if ((response = br.readLine()) != null) {
//      return response;
//    }
//    throw new NoRequestBodyDetectedException();
//  }

  private CardStatusUpdateRequest extractCardStatusFromJson(HttpExchange httpExchange)
      throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper
        .readValue(httpExchange.getRequestBody(), CardStatusUpdateRequest.class);
  }

  private <T> T extractObjectFromJson(HttpExchange httpExchange, Class<T> classObject)
      throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper
        .readValue(httpExchange.getRequestBody(), classObject);
  }

  private CardStatusUpdateRequest createCardUpdateStatus(HttpExchange exchange, CardStatus status)
      throws IOException {
    return new CardStatusUpdateRequest(extractObjectFromJson(exchange, CardUpdateRequest.class),
        status.getCode());
  }

  private String handlePost(HttpExchange ex, String path)
      throws IOException, SQLException {

    switch (path) {
//      case "api/card":
//        return service.createNewCard(ex, extractObjectFromJson(ex, CardBalanceUpdateRequest.class))
      case "api/card/activate":
        return service.updateCardStatus(ex, createCardUpdateStatus(ex, CardStatus.ACTIVE));
      case "api/card/close":
        return service.updateCardStatus(ex, createCardUpdateStatus(ex, CardStatus.CLOSED));
      case "api/card/status":
        return service.updateCardStatus(ex, extractObjectFromJson(ex, CardStatusUpdateRequest.class));
      case "api/card/deposit":
        return service.deposit(ex, extractObjectFromJson(ex, CardBalanceUpdateRequest.class));
    }

    //all needing mapper

//    else if ("api/card".equals(path)) {
//      return service.createNewCard(exchange, objectFromJson);
//    }
    throw new IllegalArgumentException();
  }

  private String handleGet(HttpExchange exchange, String path)
      throws IOException, SQLException {
    //all needing nothing
    if ("api/card/all".equals(path)) {
      return service.getAllCards();
    }

    //all needing params
    Map<String, String> params = getParametersFromQuery(exchange);
    if ("api/card".equals(path)) {
      return service.getCard(exchange, params.get("number"));
    } else if ("api/card/balance".equals(path)) {
      return service.getCardBalance(exchange, params.get("number"));
    }

    throw new IllegalArgumentException();
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String path = exchange.getRequestURI().getPath();
    path = path.substring(path.indexOf("/") + 1);
    String response = null;
    try {
      if ("GET".equals(exchange.getRequestMethod())) {
        response = handleGet(exchange, path);
      } else if ("POST".equals(exchange.getRequestMethod())) {
        response = handlePost(exchange, path);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      handleErrorResponse(exchange, e);
    } catch (Exception e) {
      handleErrorResponse(exchange, e);
    }

    if (response == null) {
      handleErrorResponse(exchange, new IllegalArgumentException());
    } else {
      handleResponse(exchange, response);
    }
    exchange.close();
  }
}
