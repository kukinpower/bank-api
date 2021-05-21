package org.romankukin.bankapi.exception;

public class ObjectNotCreatedException extends RuntimeException {

  private static final String MESSAGE = "Couldn't create an object: ";
  private static final String NOT_A_CAUSE = "SqlException was not a cause";
  private final String sqlExceptionTrace;

  public ObjectNotCreatedException(String message) {
    super(MESSAGE + message);
    sqlExceptionTrace = NOT_A_CAUSE;
  }

  public String getSqlExceptionTrace() {
    return sqlExceptionTrace;
  }

  public ObjectNotCreatedException(String message, String trace, Throwable cause) {
    super(message, cause);
    sqlExceptionTrace = trace;
  }
}
