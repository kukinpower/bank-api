package org.romankukin.bankapi.dbconnection;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class FileDatabaseConnection extends DatabaseConnection {

  private String PROPERTIES = "FileDb.properties";
  private final static String RESOURCES = "src/main/resources/";

  public FileDatabaseConnection(String PROPERTIES) {
    this.PROPERTIES = PROPERTIES;
  }

  public FileDatabaseConnection() {
  }

  @Override
  protected Properties extractProperties() {
    Properties properties = new Properties();
    try {
      properties.load(DatabaseConnection.class
          .getClassLoader()
          .getResourceAsStream(this.PROPERTIES));

      properties.setProperty("url", properties.getProperty("db") +
          new File(RESOURCES).getAbsoluteFile() +
          "/" +
          properties.getProperty("url"));

      properties.remove("db");

    } catch (IOException e) {
      e.printStackTrace();
    }
    return properties;
  }

}
