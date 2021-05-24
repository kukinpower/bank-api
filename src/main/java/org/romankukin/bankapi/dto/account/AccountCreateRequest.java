package org.romankukin.bankapi.dto.account;

import java.util.Objects;

public class AccountCreateRequest {

  private String number;
  private String phone;

  public AccountCreateRequest() {
  }

  public AccountCreateRequest(String number, String phone) {
    this.number = number;
    this.phone = phone;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
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
    return number.equals(that.number) && phone.equals(that.phone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number, phone);
  }

  @Override
  public String toString() {
    return "AccountCreateRequest{" +
        "number='" + number + '\'' +
        ", phone='" + phone + '\'' +
        '}';
  }
}
