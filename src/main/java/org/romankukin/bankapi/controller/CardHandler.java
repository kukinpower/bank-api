package org.romankukin.bankapi.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Map;
import org.romankukin.bankapi.controller.dto.AccountNumberRequest;
import org.romankukin.bankapi.controller.dto.CardBalanceUpdateRequest;
import org.romankukin.bankapi.controller.dto.CardStatusUpdateRequest;
import org.romankukin.bankapi.controller.dto.CardUpdateRequest;
import org.romankukin.bankapi.model.CardStatus;
import org.romankukin.bankapi.service.card.impl.CardServiceImpl;

//1) Выпуск новой карты по счету
//2) Проcмотр списка карт
//3) Внесение вредств
//4) Проверка баланса
public class CardHandler extends BankHandler implements HttpHandler {

  private final static Integer CARD_NUMBER_LENGTH = 16;
  private final CardServiceImpl service;

  public CardHandler(CardServiceImpl service) {
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

  private CardStatusUpdateRequest createCardUpdateStatus(HttpExchange exchange, CardStatus status)
      throws IOException {
    return new CardStatusUpdateRequest(extractObjectFromJson(exchange, CardUpdateRequest.class),
        status.getCode());
  }

  private String handlePost(HttpExchange ex, String path)
      throws IOException, SQLException {

    switch (path) {
      case "api/card":
        return service.addNewCardToDatabase(extractObjectFromJson(ex, AccountNumberRequest.class));
      case "api/card/activate":
        return service.updateCardStatus(createCardUpdateStatus(ex, CardStatus.ACTIVE));
      case "api/card/close":
        return service.updateCardStatus(createCardUpdateStatus(ex, CardStatus.CLOSED));
      case "api/card/status":
        return service.updateCardStatus(extractObjectFromJson(ex, CardStatusUpdateRequest.class));
      case "api/card/deposit":
        return service.deposit(extractObjectFromJson(ex, CardBalanceUpdateRequest.class));
    }

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

    switch (path) {
      case "api/card":
        return service.getCard(params.get("number"));
      case "api/card/balance":
        return service.getCardBalance(params.get("number"));
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
      if (response == null) {
        handleErrorResponse(exchange, new IllegalArgumentException());
      } else {
        handleResponse(exchange, response);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      handleErrorResponse(exchange, e);
    } catch (Exception e) {
      e.printStackTrace();
      handleErrorResponse(exchange, e);
    }
    exchange.close();
  }
}
