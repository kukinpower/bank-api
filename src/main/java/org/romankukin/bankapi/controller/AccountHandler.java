package org.romankukin.bankapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.Map;
import org.romankukin.bankapi.dto.client.ClientPhoneRequest;
import org.romankukin.bankapi.service.account.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AccountHandler extends BankHandler implements HttpHandler {

  private final AccountService service;
  private static final Logger logger = LoggerFactory.getLogger(AccountHandler.class);

  public AccountHandler(AccountService service) {
    super();
    this.service = service;
  }

  @Override
  protected String handlePost(HttpExchange ex, String path) throws IOException {
    throw new IllegalArgumentException();
  }

  @Override
  protected String handleGet(HttpExchange exchange, String path) throws JsonProcessingException {
    //all needing nothing
    if ("api/account/all".equals(path)) {
      return service.getAllAccounts();
    }

    //all needing params
    Map<String, String> params = getParametersFromQuery(exchange);
    logger.debug("PARAMS: {}", params);

    switch (path) {
      case "api/account":
        return service.getAccount(Integer.parseInt(params.get("id")));
    }
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
