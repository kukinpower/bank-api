package org.romankukin.bankapi.dbconnection;

import javax.sql.DataSource;
import org.h2.jdbcx.JdbcDataSource;

import java.util.Properties;

public abstract class DatabaseConnection {

  protected abstract Properties extractProperties();

  public DataSource createDataSource() {
    Properties properties = extractProperties();
    JdbcDataSource jdbcDataSource = new JdbcDataSource();
    jdbcDataSource.setURL(properties.getProperty("url"));
    jdbcDataSource.setUser(properties.getProperty("user"));
    jdbcDataSource.setPassword(properties.getProperty("password"));
    return jdbcDataSource;
//    } catch (SQLException e) {
//      StringBuilder stringBuilder = new StringBuilder();
//      for (Throwable throwable : e) {
//        stringBuilder.append(throwable.toString()); //todo
//      }
//      throw new ConnectionNotEstablishedException(stringBuilder.toString(),
//          "Could't connect to database", e);
//    } catch (NullPointerException e) {
//      throw new ConnectionNotEstablishedException("Bad database properties file", e);
//    }
  }
}
