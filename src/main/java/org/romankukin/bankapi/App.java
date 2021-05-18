package org.romankukin.bankapi;

import java.io.IOException;
import java.sql.SQLException;

class AppService {

  private void openConnection() throws IOException, SQLException {
//    connection = new FileDatabaseConnection().establishConnection();
  }

  private void createTable() throws SQLException {
//    try (PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLE)) {
//      preparedStatement.executeUpdate();
//    }
  }
}

public class App {
  private String DB_PATH = "default";
  private String JDBC_DB = "jdbc:h2:";



  public void stop() throws SQLException {
//    scanner.close();
//    connection.close();
  }

  public void run() {
//    try {
//      openConnection();
//      createTable();
//    } catch () {
//
//    }
  }
}
