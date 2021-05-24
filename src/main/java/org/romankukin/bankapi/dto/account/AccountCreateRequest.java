package org.romankukin.bankapi.dto.account;

import java.math.BigDecimal;
import java.util.Objects;

public class AccountCreateRequest {

  private String number;
  private BigDecimal balance;
  private String phone;

  public AccountCreateRequest() {
  }

  public AccountCreateRequest(String number, BigDecimal balance, String phone) {
    this.number = number;
    this.balance = balance;
    this.phone = phone;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AccountCreateRequest that = (AccountCreateRequest) o;
    return number.equals(that.number) && balance.equals(that.balance) && phone.equals(that.phone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, balance, phone);
  }

  @Override
  public String toString() {
    return "AccountCreateRequest{" +
        "number='" + number + '\'' +
        ", balance=" + balance +
        ", phone='" + phone + '\'' +
        '}';
  }
}
