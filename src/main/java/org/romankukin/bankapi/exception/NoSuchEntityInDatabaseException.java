package org.romankukin.bankapi.exception;

import java.io.FileNotFoundException;

public class NoSuchEntityInDatabaseException extends IllegalArgumentException {

  public NoSuchEntityInDatabaseException() {
  }

  public NoSuchEntityInDatabaseException(String s) {
    super(s);
  }
}
