package org.romankukin.bankapi.exception;

public class ConnectionNotEstablishedException extends IllegalArgumentException {

  private static final String MESSAGE = "Couldn't establish connection with database";

  public ConnectionNotEstablishedException() {
    super(MESSAGE);
  }
}
