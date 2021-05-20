package org.romankukin.bankapi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;

public interface BankService {
  default String extractFieldFromRequestBody(HttpExchange exchange, JsonNode objectFromJson, String key) throws IOException {
    String value = objectFromJson.get(key).textValue();
    if ("number".equals(key) && value.length() != CardService.CARD_LENGTH) {
      throw new IllegalArgumentException("Bad number: " + value);
    }
    return value;
  }

}
