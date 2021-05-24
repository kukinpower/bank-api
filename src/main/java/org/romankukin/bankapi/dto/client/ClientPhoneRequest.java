package org.romankukin.bankapi.dto.client;

import java.util.Objects;

public class ClientPhoneRequest {
  private String phone;

  public ClientPhoneRequest() {
  }

  public ClientPhoneRequest(String phone) {
    this.phone = phone;
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
    ClientPhoneRequest that = (ClientPhoneRequest) o;
    return phone.equals(that.phone);
  }

  @Override
  public int hashCode() {
    return Objects.hash(phone);
  }

  @Override
  public String toString() {
    return "ClientPhoneRequest{" +
        "phone='" + phone + '\'' +
        '}';
  }
}
