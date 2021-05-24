package org.romankukin.bankapi.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.stream.Collectors;

public interface IntegratedTest {

  String PROTOCOL = "http://";
  String HOST = "localhost";
  String PORT = "8080";

  String POST = "POST";
  String GET = "GET";

  String CARD_STATUS = "(\"[A-Z]+\"|[0-9]+)";
  String DECIMAL = "[0-9]+[.,]?[0-9]*";
  String CARD_NUMBER = "\"number\"\\s*:\\s*\"[0-9]{16}\"";
  String ACCOUNT = "\"(account|accountId|accountNumber)\"\\s*:\\s*(\"[0-9]{20}\"|[0-9]+)";
  String CARD_BODY = "\\s*\\{\\s*" + CARD_NUMBER + ","
      + "\\s*\"pin\"\\s*:\\s*\"[0-9]{4}\","
      + "\\s*" + ACCOUNT + ","
      + "\\s*\"currency\"\\s*:\\s*\"[A-Z]{3}\","
      + "\\s*\"balance\"\\s*:\\s*" + DECIMAL + ","
      + "\\s*\"status\"\\s*:\\s*" + CARD_STATUS + "\\s*}\\s*";

  String CARD_REGEX = "^" + CARD_BODY + "$";

  String CARD_ARRAY_REGEX = "^\\[(" + CARD_BODY + ",?)*]$";

  String CARD_STATUS_ARRAY_REGEX = "^\\[(\\s*\\{\\s*" + CARD_NUMBER + ","
          + "\\s*\"descriptor\"\\s*:\\s*\"[A-Z]+\"\\s*}\\s*,?)*]$";

 String CARD_BALANCE_REGEX = "^[0-9]+[.,]?[0-9]*$";

 String CARD_STATUS_AND_NUMBER_REGEX = "^\\{\\s*" + CARD_NUMBER + ","
     + "\\s*\"status\"\\s*:\\s*" + CARD_STATUS + "\\s*}\\s*$";

 String CARD_DEPOSIT_REGEX = "^\\{\\s*" + CARD_NUMBER + ","
     + "\\s*\"amount\"\\s*:\\s*" + DECIMAL + "\\s*}\\s*$";

  default String createUrl(String path) {
    return PROTOCOL + HOST + ":" + PORT + "/" + path;
  }

  default String createUrl(String path, String requestParams) {
    return createUrl(path) + requestParams;
  }

  default String getResponse(HttpURLConnection connection) throws IOException {
    return new BufferedReader(new InputStreamReader(connection.getInputStream()))
        .lines()
        .collect(Collectors.joining(System.lineSeparator()));

  }
}
