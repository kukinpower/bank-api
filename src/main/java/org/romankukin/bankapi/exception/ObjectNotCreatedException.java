package org.romankukin.bankapi.exception;

public class ObjectNotCreatedException extends IllegalArgumentException {

  private static final String MESSAGE = "Couldn't create an object: ";

  public ObjectNotCreatedException(String message) {
    super(MESSAGE + message);
  }

  public ObjectNotCreatedException(String message, Throwable cause) {
    super(MESSAGE + message, cause);
  }
}
