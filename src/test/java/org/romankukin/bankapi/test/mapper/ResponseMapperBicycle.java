package org.romankukin.bankapi.test.mapper;

import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import org.romankukin.bankapi.test.IntegratedTest;

public class ResponseMapperBicycle {

  private Map<String, Object> map;

  public ResponseMapperBicycle() {
    this.map = new LinkedHashMap<>();
  }

  public ResponseMapperBicycle(Object... args) {
    this.map = new LinkedHashMap<>();
    for (int i = 0; i < args.length; i += 2) {
      map.put((String) args[i], args[i + 1]);
    }
  }

  public ResponseMapperBicycle(Map<String, Object> map) {
    this.map = map;
  }

  public void put(String key, String value) {
    map.put(key, value);
  }

  public String toJson() {
    StringBuilder stringBuilder = new StringBuilder("{");

    int i = 0;
    for (Entry<String, Object> entry : map.entrySet()) {
      stringBuilder.append("\"")
          .append(entry.getKey())
          .append("\"")
          .append(":");
      if (entry.getValue() instanceof BigDecimal) {
        stringBuilder.append(((BigDecimal) entry.getValue()).toPlainString());
      } else if (entry.getValue() instanceof Integer) {
        stringBuilder.append(entry.getValue());
      } else {
        stringBuilder.append("\"")
            .append(entry.getValue())
            .append("\"");
      }
      if (i + 1 < map.size()) {
        stringBuilder.append(",");
      }
      i++;
    }
    stringBuilder.append("}");
    return stringBuilder.toString();
  }

  public String toParams() {
    StringBuilder stringBuilder = new StringBuilder("?");

    int i = 0;
    for (Entry<String, Object> entry : map.entrySet()) {
      stringBuilder.append(URLEncoder.encode(entry.getKey()))
          .append("=")
          .append(URLEncoder.encode((String) entry.getValue()));
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
