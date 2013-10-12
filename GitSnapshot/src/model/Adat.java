/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author Apa
 */
public class Adat extends Object {
  
  private int id;
  private String neve;

  public Adat() {
    this.id = -1;
    this.neve = "";
  }

  public Adat(int id, String neve) {
    this.id = id;
    this.neve = neve;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNeve() {
    return neve;
  }

  public void setNeve(String neve) {
    this.neve = neve;
  }

  @Override
  public String toString() {
    return neve;
  }

  @Override
  public int hashCode() {
    return id;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Adat other = (Adat) obj;
    if (this.id != other.id) {
      return false;
    }
    return true;
  }
  
  
  
}
