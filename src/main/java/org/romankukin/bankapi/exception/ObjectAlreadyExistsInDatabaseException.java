package org.romankukin.bankapi.exception;

import java.sql.SQLException;

public class ObjectAlreadyExistsInDatabaseException extends RuntimeException {

  private static final String MESSAGE = "Object already exists in database: ";
  private final String sqlExceptionTrace;

  public String getSqlExceptionTrace() {
    return sqlExceptionTrace;
  }

  public ObjectAlreadyExistsInDatabaseException(String message, String trace, SQLException cause) {
    super(MESSAGE + message, cause);
    this.sqlExceptionTrace = trace;
  }
}
