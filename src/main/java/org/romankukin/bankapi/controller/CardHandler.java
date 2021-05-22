package org.romankukin.bankapi.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.romankukin.bankapi.dto.AccountNumberRequest;
import org.romankukin.bankapi.dto.CardBalanceUpdateRequest;
import org.romankukin.bankapi.dto.CardNumberDeleteRequest;
import org.romankukin.bankapi.dto.CardStatusUpdateRequest;
import org.romankukin.bankapi.dto.CardUpdateRequest;
import org.romankukin.bankapi.exception.NoSuchEntityInDatabaseException;
import org.romankukin.bankapi.model.CardStatus;
import org.romankukin.bankapi.service.card.impl.CardServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 1) Выпуск новой карты по счету 2) Просмотр списка карт 3) Внесение средств 4) Проверка баланса
 */
public class CardHandler extends BankHandler implements HttpHandler {

  private final CardServiceImpl service;
  private static final Logger logger = LoggerFactory.getLogger(CardHandler.class);

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

  private static final String BAD_REQUEST = "Bad request";

  private void handleErrorResponse(HttpExchange exchange, Throwable throwable) throws IOException {
    String errorMessage = throwable.getMessage();
    if (throwable instanceof NoSuchEntityInDatabaseException) {
      exchange.sendResponseHeaders(404, errorMessage.length());
    } else if (throwable instanceof IOException) {
      errorMessage = BAD_REQUEST;
      exchange.sendResponseHeaders(400, errorMessage.length());
    } else {
      exchange.sendResponseHeaders(400, errorMessage.length());
    }
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
      throws IOException {
    logger.debug("POST {}", path);

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
        return service.depositCard(extractObjectFromJson(ex, CardBalanceUpdateRequest.class));
      case "api/card/delete":
        return service.deleteCard(extractObjectFromJson(ex, CardNumberDeleteRequest.class));
    }

    throw new IllegalArgumentException();
  }

  private String handleGet(HttpExchange exchange, String path) throws IOException {
    logger.debug("GET {}", path);
    //all needing nothing
    if ("api/card/all".equals(path)) {
      return service.getAllCards();
    }

    //all needing params
    Map<String, String> params = getParametersFromQuery(exchange);
    logger.debug("PARAMS: {}", params);

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
    } catch (Exception e) {
      logger.debug(e.getMessage());
      handleErrorResponse(exchange, e);
    }
    exchange.close();
  }
}
