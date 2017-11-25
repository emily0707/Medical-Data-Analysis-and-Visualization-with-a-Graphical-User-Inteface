/*
this class is used to conenct to sqlite database
 */
package Model;
import Util.SqliteConnection;
import Util.SqliteConnection;
import java.sql.*;
/**
 *
 * @author feiping
 */
public class BeadsModel {
    Connection conection;
   public BeadsModel () {
   conection = SqliteConnection.Connector();
   if (conection == null) {

   System.out.println("connection not successful");
    System.exit(1);}
  }
  
  public boolean isDbConnected() {
   try {
  return !conection.isClosed();
 } catch (SQLException e) {
  // TODO Auto-generated catch block
  e.printStackTrace();
  return false;
 }
}
}




        
    

