package org.romankukin.bankapi.controller;

import com.sun.net.httpserver.HttpExchange;
import java.util.LinkedHashMap;
import java.util.Map;

public interface BankHandler {
  default Map<String, String> getParametersFromQuery(HttpExchange exchange) {
    String query = exchange.getRequestURI().getQuery();
    Map<String, String> params = new LinkedHashMap<>();
    for (String param : query.split("&")) {
      String[] pair = param.split("=");
      params.put(pair[0], pair[1]);
    }
    return params;
  }
}
