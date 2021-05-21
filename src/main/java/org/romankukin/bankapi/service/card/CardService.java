package org.romankukin.bankapi.service.card;

import com.fasterxml.jackson.databind.JsonNode;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import org.romankukin.bankapi.service.card.impl.CardServiceImpl;

public interface CardService {
  default String extractFieldFromRequestBody(HttpExchange exchange, JsonNode objectFromJson, String key) throws IOException {
    String value = objectFromJson.get(key).textValue();
    if ("number".equals(key) && value.length() != CardServiceImpl.CARD_LENGTH) {
      throw new IllegalArgumentException("Bad number: " + value);
    }
    return value;
  }

}
