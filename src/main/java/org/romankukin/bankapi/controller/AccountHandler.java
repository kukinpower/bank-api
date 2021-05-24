package org.romankukin.bankapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import org.romankukin.bankapi.dto.client.ClientPhoneRequest;
import org.romankukin.bankapi.service.account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountHandler extends BankHandler implements HttpHandler {

  private final AccountService service;
  private static final Logger logger = LoggerFactory.getLogger(AccountHandler.class);

  public AccountHandler(AccountService service) {
    this.service = service;
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
  protected String handleGet(HttpExchange exchange, String path) throws JsonProcessingException {
    //all needing nothing
    if ("api/account/all".equals(path)) {
      return service.getAllAccounts();
    }
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
