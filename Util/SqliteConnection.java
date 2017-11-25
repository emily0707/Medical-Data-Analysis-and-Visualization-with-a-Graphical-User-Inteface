package Util;
import java.sql.*;
/*
this calss is use to set up conenction to a sqlite database
 */

/**
 *
 * @author feiping
 */

public class SqliteConnection {
  public static Connection Connector() {
 try {
  Class.forName("org.sqlite.JDBC");
  Connection conn =DriverManager.getConnection("jdbc:sqlite:experiments.sqlite"); // open or create experiments.sqlite database file
  return conn;
 } catch (Exception e) {
  System.out.println(e);
  return null;
  // TODO: handle exception
 }
}
}