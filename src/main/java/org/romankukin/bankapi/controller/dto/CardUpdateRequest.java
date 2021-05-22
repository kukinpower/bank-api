package org.romankukin.bankapi.controller.dto;

public class CardUpdateRequest {

  public CardUpdateRequest() {
  }

  private String Number;

  public String getNumber() {
    return Number;
  }

  public void setNumber(String number) {
    Number = number;
  }

  @Override
  public String toString() {
    return "CardUpdateRequest{" +
        "Number='" + Number + '\'' +
        '}';
  }
}
