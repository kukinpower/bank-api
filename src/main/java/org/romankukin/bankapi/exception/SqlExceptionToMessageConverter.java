package org.romankukin.bankapi.exception;

import java.sql.SQLException;

public class SqlExceptionToMessageConverter {

  public SqlExceptionToMessageConverter() {
  }

  public String extractStackTraceFromSqlException(SQLException e) {
    StringBuilder stringBuilder = new StringBuilder();
    for (Throwable throwable : e) {
      stringBuilder.append(throwable.toString());
    }
    return stringBuilder.toString();
  }
}
