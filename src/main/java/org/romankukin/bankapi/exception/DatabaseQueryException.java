package org.romankukin.bankapi.exception;

public class DatabaseQueryException extends IllegalArgumentException {

  private static final String message = "Database query failure";

  public DatabaseQueryException() {
    super(message);
  }
}
