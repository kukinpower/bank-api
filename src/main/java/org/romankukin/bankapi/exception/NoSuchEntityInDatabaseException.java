package org.romankukin.bankapi.exception;

import java.io.FileNotFoundException;

public class NoSuchEntityInDatabaseException extends FileNotFoundException {

  public NoSuchEntityInDatabaseException() {
  }

  public NoSuchEntityInDatabaseException(String s) {
    super(s);
  }
}
