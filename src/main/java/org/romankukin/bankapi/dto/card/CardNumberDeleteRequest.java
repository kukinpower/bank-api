package org.romankukin.bankapi.dto.card;

public class CardNumberDeleteRequest {

  private String number;

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  @Override
  public String toString() {
    return "CardNumberDeleteRequest{" +
        "number='" + number + '\'' +
        '}';
  }
}
