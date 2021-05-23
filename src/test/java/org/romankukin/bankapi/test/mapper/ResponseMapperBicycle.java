package org.romankukin.bankapi.test.mapper;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ResponseMapperBicycle {

  private Map<String, String> map;

  public ResponseMapperBicycle() {
    this.map = new LinkedHashMap<>();
  }

  public ResponseMapperBicycle(String... args) {
    this.map = new LinkedHashMap<>();
    for (int i = 0; i < args.length; i += 2) {
      map.put(args[i], args[i + 1]);
    }
  }

  public ResponseMapperBicycle(Map<String, String> map) {
    this.map = map;
  }

  public void put(String key, String value) {
    map.put(key, value);
  }

  public String toJson() {
    StringBuilder stringBuilder = new StringBuilder("{");

    int i = 0;
    for (Entry<String, String> entry : map.entrySet()) {
      stringBuilder.append("\"")
          .append(entry.getKey())
          .append("\"")
          .append(":")
          .append("\"")
          .append(entry.getValue())
          .append("\"");
      if (i + 1 < map.size()) {
        stringBuilder.append(",");
      }
    }
    stringBuilder.append("}");
    return stringBuilder.toString();
  }

  public String toParams() {
    StringBuilder stringBuilder = new StringBuilder();

    int i = 0;
    for (Entry<String, String> entry : map.entrySet()) {
      stringBuilder.append(URLEncoder.encode(entry.getKey()))
          .append("=")
          .append(URLEncoder.encode(entry.getValue()));
      if (i + 1 < map.size()) {
        stringBuilder.append("&");
      }
    }
    return stringBuilder.toString();
  }

  @Override
  public String toString() {
    return toJson();
  }
}
