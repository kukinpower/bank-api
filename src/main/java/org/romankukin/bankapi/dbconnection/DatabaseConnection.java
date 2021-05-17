package org.romankukin.bankapi.dbconnection;

import org.romankukin.bankapi.exception.ConnectionNotEstablishedException;

import java.sql.*;
import java.util.Properties;

public abstract class DatabaseConnection {

  protected abstract Properties extractProperties();

  public Connection establishConnection() {
    try {
      Properties properties = extractProperties();
      return DriverManager.getConnection(properties.getProperty("url"), properties);
    } catch (SQLException e) {
      StringBuilder stringBuilder = new StringBuilder();
      for (Throwable throwable : e) {
        stringBuilder.append(throwable.toString()); //todo
      }
      throw new ConnectionNotEstablishedException(stringBuilder.toString(),
          "Could't connect to database", e);
    } catch (NullPointerException e) {
      throw new ConnectionNotEstablishedException("Bad database properties file", e);
    }
  }
}
