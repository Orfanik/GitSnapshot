/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import db.RepoDb;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author U292156
 */
public class Repo extends RepoDb {

    public Repo() {
    }

    public Repo(int id, String neve) {
        super(id, neve);
    }

    public Repo(ResultSet rs) throws SQLException {
        super(rs);
    }
    
}
