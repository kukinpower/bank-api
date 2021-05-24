package org.romankukin.bankapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import org.romankukin.bankapi.dto.card.AccountNumberRequest;
import org.romankukin.bankapi.exception.NoSuchEntityInDatabaseException;
import org.romankukin.bankapi.service.ResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BankHandler {

  protected static final String BAD_REQUEST_MESSAGE = "Bad request";
  private static final Logger logger = LoggerFactory.getLogger(CardHandler.class);

  abstract String handlePost(HttpExchange ex, String path) throws IOException;
  abstract String handleGet(HttpExchange exchange, String path) throws IOException;

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

  protected void handleResponse(HttpExchange exchange, ResponseStatus status, String response) throws IOException {
    exchange.sendResponseHeaders(status.getCode(), response.length());
    OutputStream outputStream = exchange.getResponseBody();

    outputStream.write(response.getBytes(StandardCharsets.UTF_8));
    outputStream.flush();
    outputStream.close();
  }

  protected void handleErrorResponse(HttpExchange exchange, Throwable throwable) throws IOException {
    String errorMessage = throwable.getMessage();
    if (throwable instanceof NoSuchEntityInDatabaseException) {
      exchange.sendResponseHeaders(ResponseStatus.NOT_FOUND.getCode(), errorMessage.length());
    } else {
      errorMessage = BAD_REQUEST_MESSAGE;
      exchange.sendResponseHeaders(ResponseStatus.BAD_REQUEST.getCode(), errorMessage.length());
    }
    OutputStream outputStream = exchange.getResponseBody();

    outputStream.write(errorMessage.getBytes(StandardCharsets.UTF_8));
    outputStream.flush();
    outputStream.close();
  }

  abstract String handleCreate(HttpExchange exchange) throws IOException;

  protected static final String CREATE_PATH_REGEX = "^api/[a-zA-Z]+$";

  protected void routeRequest(HttpExchange exchange) throws IOException {
    String path = exchange.getRequestURI().getPath();
    path = path.substring(path.indexOf("/") + 1);
    String response = null;
    try {
      if ("GET".equals(exchange.getRequestMethod())) {
        logger.debug("GET {}", path);
        response = handleGet(exchange, path);
      } else if ("POST".equals(exchange.getRequestMethod())) {
        logger.debug("POST {}", path);
        if (path.matches(CREATE_PATH_REGEX)) {
          response = handleCreate(exchange);
          handleResponse(exchange, ResponseStatus.CREATED, response);
          exchange.close();
          return;
        } else {
          response = handlePost(exchange, path);
        }
      }
      if (response == null) {
        handleErrorResponse(exchange, new IllegalArgumentException());
      } else {
        handleResponse(exchange, ResponseStatus.OK, response);
      }
    } catch (Exception e) {
      logger.debug(e.getMessage());
      handleErrorResponse(exchange, e);
    }
    exchange.close();
  }

}
