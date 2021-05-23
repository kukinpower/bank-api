package org.romankukin.bankapi.exception;

public class CouldNotStartServerException extends IllegalArgumentException {

  private static final String MESSAGE = "Couldn't start server";

  public CouldNotStartServerException() {
    super(MESSAGE);
  }
}
