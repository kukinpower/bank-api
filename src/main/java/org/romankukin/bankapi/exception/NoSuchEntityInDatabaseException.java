package org.romankukin.bankapi.exception;

public class NoSuchEntityInDatabaseException extends IllegalArgumentException {

  public NoSuchEntityInDatabaseException() {
  }

  public NoSuchEntityInDatabaseException(String s) {
    super(s);
  }
}
