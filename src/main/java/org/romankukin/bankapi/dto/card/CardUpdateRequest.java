package org.romankukin.bankapi.dto.card;

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
