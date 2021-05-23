package org.romankukin.bankapi.test;

public interface IntegratedTest {

  String PROTOCOL = "http://";
  String HOST = "localhost";
  String PORT = "8080";

  String POST = "POST";
  String GET = "GET";

  String CARD_REGEX =
      "^\\{\\s*\"number\"\\s*:\\s*\"[0-9]{16}\","
          + "\\s*\"pin\"\\s*:\\s*\"[0-9]{4}\","
          + "\\s*\"account\"\\s*:\\s*\"[0-9]{20}\","
          + "\\s*\"currency\"\\s*:\\s*\"[A-Z]{3}\","
          + "\\s*\"balance\"\\s*:\\s*[0-9]+[.,]?[0-9]*,"
          + "\\s*\"status\"\\s*:\\s*[0-9]+\\s*}\\s*$";

  default String createUrl(String path) {
    return PROTOCOL + HOST + ":" + PORT + "/" + path;
  }

  default String createUrl(String path, String requestParams) {
    return createUrl(path) + requestParams;
  }
}
