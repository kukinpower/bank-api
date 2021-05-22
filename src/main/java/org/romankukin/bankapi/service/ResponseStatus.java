package org.romankukin.bankapi.service;

public enum ResponseStatus {
  OK(200), CREATED(201), BAD_REQUEST(400), NOT_FOUND(404);

  int code;

  ResponseStatus(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
