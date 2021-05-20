package org.romankukin.bankapi.controller.scheme;

public class CardStatusUpdateRequest {

  private String number;
  private int status;

  public CardStatusUpdateRequest(CardUpdateRequest cardUpdate, int status) {
    this.number = cardUpdate.getNumber();
    this.status = status;
  }

  public CardStatusUpdateRequest() {
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }
}
