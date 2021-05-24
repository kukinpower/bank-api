package org.romankukin.bankapi.dto.card;

public class CardStatusDescriptor {

  private String number;
  private String descriptor;

  public CardStatusDescriptor() {
  }

  public CardStatusDescriptor(String number, String descriptor) {
    this.number = number;
    this.descriptor = descriptor;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getDescriptor() {
    return descriptor;
  }

  public void setDescriptor(String descriptor) {
    this.descriptor = descriptor;
  }
}
