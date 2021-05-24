package org.romankukin.bankapi.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import org.romankukin.bankapi.dto.card.CardStatusUpdateRequest;
import org.romankukin.bankapi.dto.card.CardUpdateRequest;
import org.romankukin.bankapi.dto.client.ClientPhoneRequest;
import org.romankukin.bankapi.model.CardStatus;
import org.romankukin.bankapi.service.account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 1) Выпуск новой карты по счету 2) Просмотр списка карт 3) Внесение средств 4) Проверка баланса
 */
public class AccountHandler extends BankHandler implements HttpHandler {

  private final AccountService service;
  private static final Logger logger = LoggerFactory.getLogger(AccountHandler.class);

  public AccountHandler(AccountService service) {
    this.service = service;
  }

  private CardStatusUpdateRequest createCardUpdateStatus(HttpExchange exchange, CardStatus status)
      throws IOException {
    return new CardStatusUpdateRequest(extractObjectFromJson(exchange, CardUpdateRequest.class),
        status.getCode());
  }

  @Override
  protected String handlePost(HttpExchange ex, String path) throws IOException {
    switch (path) {
//      case "api/card/activate":
//        return service.updateCardStatus(createCardUpdateStatus(ex, CardStatus.ACTIVE));
//      case "api/card/close":
//        return service.updateCardStatus(createCardUpdateStatus(ex, CardStatus.CLOSED));
//      case "api/card/status":
//        return service.updateCardStatus(extractObjectFromJson(ex, CardStatusUpdateRequest.class));
//      case "api/card/deposit":
//        return service.depositCard(extractObjectFromJson(ex, CardBalanceUpdateRequest.class));
//      case "api/card/delete":
//        return service.deleteCard(extractObjectFromJson(ex, CardNumberDeleteRequest.class));
    }

    throw new IllegalArgumentException();
  }

  @Override
  protected String handleGet(HttpExchange exchange, String path) {
    //all needing nothing
//    if ("api/card/all".equals(path)) {
//      return service.getAllCards();
//    } else if ("api/card/all/status".equals(path)) {
//      return service.getAllCardsStatus();
//    }
//
//    //all needing params
//    Map<String, String> params = getParametersFromQuery(exchange);
//    logger.debug("PARAMS: {}", params);
//
//    switch (path) {
//      case "api/card":
//        return service.getCard(params.get("number"));
//      case "api/card/balance":
//        return service.getCardBalance(params.get("number"));
//      case "api/card/status":
//        return service.getCardStatus(params.get("number"));
//    }
    throw new IllegalArgumentException();
  }

  @Override
  String handleCreate(HttpExchange exchange) throws IOException {
    return service.addNewAccountToDatabase(extractObjectFromJson(exchange, ClientPhoneRequest.class));
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    routeRequest(exchange);
  }
}
