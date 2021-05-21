package org.romankukin.bankapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BankHandler {
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
    return objectMapper
        .readValue(httpExchange.getRequestBody(), classObject);
  }

}
