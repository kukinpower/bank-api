package org.romankukin.bankapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BankHandler {

  private static final Logger logger = LoggerFactory.getLogger(CardHandler.class);

  protected Map<String, String> getParametersFromQuery(HttpExchange exchange) {
    String query = exchange.getRequestURI().getQuery();
    Map<String, String> params = new LinkedHashMap<>();
    for (String param : query.split("&")) {
      String[] pair = param.split("=");
      params.put(pair[0], pair[1]);
    }
    return params;
  }

  protected <T> T extractObjectFromJson(HttpExchange httpExchange, Class<T> classObject)
      throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    T dto = objectMapper.readValue(httpExchange.getRequestBody(), classObject);
    logger.debug("PARAMS: {}", dto);
    return dto;
  }

}
