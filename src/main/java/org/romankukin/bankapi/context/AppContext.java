package org.romankukin.bankapi.context;

import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.romankukin.bankapi.dao.card.impl.CardDaoImpl;
import org.romankukin.bankapi.dbconnection.FileDatabaseConnection;
import org.romankukin.bankapi.service.card.impl.CardServiceImpl;

public class AppContext {

  public static final String DATA_SOURCE = "dataSource";
  public static final String CARD_DAO = "cardDao";
  public static final String CARD_SERVICE = "cardService";

  private static final Map<String, Object> beans = new HashMap<>();

  public AppContext() {
    beans.put(DATA_SOURCE, new FileDatabaseConnection().createDataSource());
    beans.put(CARD_DAO, new CardDaoImpl((DataSource) beans.get(DATA_SOURCE)));
    beans.put(CARD_SERVICE, new CardServiceImpl((CardDaoImpl) beans.get(CARD_DAO)));
  }

  public Object getBean(String beanName) {
    return beans.get(beanName);
  }
}
