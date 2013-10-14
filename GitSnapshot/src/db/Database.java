/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;


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
      String dbName = System.getProperty("user.home") + File.separator + bundle.getString("database");
      try {
        File dbFile = new File(dbName);
        if (!dbFile.exists()) {
          URL inputUrl = Connection.class.getResource("/res/GitSnapshot.db");
          
          File dest = new File(dbName);
            try {            
                FileUtils.copyURLToFile(inputUrl, dest);
            } catch (IOException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
