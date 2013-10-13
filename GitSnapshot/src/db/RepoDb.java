/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import gui.MainFrame;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Adat;

/**
 *
 * @author Apa
 */
public class RepoDb extends Adat {

    private String zipPrefix = "";
    private String issueId = "";
    private String branch = "";
    static PreparedStatement stmSelect1 = null;
    static PreparedStatement stmSelect2 = null;
    static PreparedStatement stmSelectAll = null;
    static PreparedStatement stmInsert = null;
    static PreparedStatement stmUpdate = null;

    public RepoDb() {
    }

    public RepoDb(int id, String neve) {
        super(id, neve);
    }

    public RepoDb(ResultSet rs) throws SQLException {
        setId(rs.getInt("id"));
        setNeve(rs.getString("neve"));
        this.zipPrefix = rs.getString("zipprefix");
        this.issueId = rs.getString("issueid");
        this.branch = rs.getString("branch");
    }
    /**
     * Get the value of zipPrefix
     *
     * @return the value of zipPrefix
     */
    public String getZipPrefix() {
        return zipPrefix;
    }

    /**
     * Set the value of zipPrefix
     *
     * @param zipPrefix new value of zipPrefix
     */
    public void setZipPrefix(String zipPrefix) {
        this.zipPrefix = zipPrefix;
    }

    /**
     * Get the value of issueId
     *
     * @return the value of issueId
     */
    public String getIssueId() {
        return issueId;
    }

    /**
     * Set the value of issueId
     *
     * @param issueId new value of issueId
     */
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    /**
     *
     * @return
     */
    public String getBranch() {
        return branch;
    }

    /**
     *
     * @param branch
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }

    
    
  private static void Init() throws SQLException {
    if (stmSelect1 == null) {
      stmSelect1 = Database.getDb().prepareStatement("select id, neve, zipprefix, issueid, branch from repository where id = ?");
      stmSelect2 = Database.getDb().prepareStatement("select id, neve, zipprefix, issueid, branch from repository where neve = ?");
      stmSelectAll = Database.getDb().prepareStatement("select id, neve, zipprefix, issueid, branch from repository order by neve");
      stmInsert = Database.getDb().prepareStatement("insert into repository (neve, zipprefix, issueid, branch) values (?, ?, ?, ?)");
      stmUpdate = Database.getDb().prepareStatement("update repository set neve = ?, zipprefix = ?, issueid = ?, branch = ? where id = ?");
    }
  }
    
  /**
   *
   * @return
   * @throws SQLException
   */
  public static ResultSet fetchAll() throws SQLException {
    Init();
    
    ResultSet rs = stmSelectAll.executeQuery();
    return (rs);
  }

  /**
   *
   * @return
   * @throws SQLException
   */
  public static ResultSet fetchByNeve(String neve) throws SQLException {
    Init();
    
    stmSelect2.setString(1, neve);
    ResultSet rs = stmSelect2.executeQuery();
    return (rs);
  }
  
  /**
   *
   * @return
   * @throws SQLException
   */
  public int update() throws SQLException {
    Init();
    
    stmUpdate.setString(1, this.getNeve());
    stmUpdate.setString(2, this.zipPrefix);
    stmUpdate.setString(3, this.issueId);
    stmUpdate.setString(4, this.branch);
    stmUpdate.setInt(5, this.getId());
    
    return (stmUpdate.executeUpdate());
  }
  
  /**
   *
   * @return
   * @throws SQLException
   */
  public int insert() throws SQLException {
    Init();
    
    stmInsert.setString(1, this.getNeve());
    stmInsert.setString(2, this.zipPrefix);
    stmInsert.setString(3, this.issueId);
    stmInsert.setString(4, this.branch);
    int rv = stmInsert.executeUpdate(); 
    if (rv > 0)
    {
        ResultSet rs = stmInsert.getGeneratedKeys();
        if (rs.next()) {
            this.setId(rs.getInt(1));
        }
    }
    return (rv);
  }
}
