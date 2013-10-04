/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Apa
 */
public class Database {

  private static Connection db = null;

  /**
   *
   * @return
   */
  public static Connection getDb() {
    if (db == null)
    {
      // create a database db
      java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("res/Bundle"); // NOI18N
      String dbName = bundle.getString("database");
      try {
        db = DriverManager.getConnection("jdbc:sqlite:" + dbName);
        /// memory db: "jdbc:sqlite::memory:"
      } catch (SQLException e) {
        // if the error message is "out of memory", 
        // it probably means no database file is found
        System.err.println(e.getMessage());
      }
    }
    return db;
  }

    /**
     *
     */
    public Database(){

  }
  
    /**
     *
     * @return
     * @throws SQLException
     */
    public static Statement createStatement () throws SQLException
  {
    return (getDb().createStatement());
  }
  
    /**
     *
     * @param statement
     * @return
     * @throws SQLException
     */
    public static PreparedStatement createPreparedStatement (String statement) throws SQLException
  {
    return (getDb().prepareStatement(statement));
  }
}
