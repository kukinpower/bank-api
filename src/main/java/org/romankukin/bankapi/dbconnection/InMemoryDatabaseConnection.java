package org.romankukin.bankapi.dbconnection;

import java.io.IOException;
import java.util.Properties;

public class InMemoryDatabaseConnection extends DatabaseConnection {

  private String PROPERTIES = "InMemDb.properties";

  public InMemoryDatabaseConnection(String PROPERTIES) {
    this.PROPERTIES = PROPERTIES;
  }

  public InMemoryDatabaseConnection() {
  }

  @Override
  protected Properties extractProperties() {
    Properties properties = new Properties();
    try {
      properties.load(DatabaseConnection.class
          .getClassLoader()
          .getResourceAsStream(this.PROPERTIES));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return properties;
  }

}
