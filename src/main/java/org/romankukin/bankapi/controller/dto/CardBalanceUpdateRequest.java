package org.romankukin.bankapi.controller.dto;

import java.math.BigDecimal;

public class CardBalanceUpdateRequest {

  private String number;
  private BigDecimal amount;

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public void setAmount(BigDecimal amount) {
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "CardBalanceUpdateRequest{" +
        "number='" + number + '\'' +
        ", amount=" + amount +
        '}';
  }
}
