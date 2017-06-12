package Model;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.util.ArrayList;

/**
 * @author Ulrika Goloconda Fahlén
 * @version 1.0
 * @since 2016-09-22
 */
public class AccessRentbridge {

  public static ArrayList<Bike> getUsersCurrnetBikes(int userID) {
    ArrayList<Bike> bikes = new ArrayList<>();
    DBType dataBase = null;
    Connection conn = null;
    Date dayOfReturn = null;
    String returnSring = "";
    if (helpers.PCRelated.isThisNiklasPC()) {
      dataBase = DBType.Niklas;
    } else {
      dataBase = DBType.Ulrika;
    }
    try {
      conn = DBUtil.getConnection(dataBase);
      conn.setAutoCommit(false);
      String sql = "CALL users_current_bikes(?)";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1, userID);
      ResultSet rs = ps.executeQuery();
      Bike b;
      while (rs.next()) {
        b = new Bike();
        if (rs.getDate("dayOfActualReturn") == null && rs.getDate("dayOfRent") != null) {
          b.setAvailable(false);
        } else {
          b.setAvailable(true);
        }
        b.setBrandName(rs.getString("brandname"));
        Blob blob = rs.getBlob("image");
        byte[] bytes = blob.getBytes(1, (int) blob.length());
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        b.setImageStream(bis);
        b.setColor(rs.getString("color"));
        b.setType(rs.getString("typeName"));
        b.setModelYear(rs.getInt("modelyear"));
        b.setBikeID(rs.getInt("bikeID"));
        b.setSize(rs.getInt("size"));
        b.setDayOfReturn(rs.getDate("dayOfRent").toLocalDate());
       bikes.add(b);
      }
      conn.commit();

    } catch (Exception e) {
      try {
        conn.rollback();
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
      e.printStackTrace();
    }
    return bikes;
  }

  public static ArrayList<Integer> getUsersTotalLoan(int userID) {
    ArrayList<Integer> bikes = new ArrayList<>();
    DBType dataBase = null;
    Connection conn = null;
    Date dayOfReturn = null;
    String returnSring = "";
    if (helpers.PCRelated.isThisNiklasPC()) {
      dataBase = DBType.Niklas;
    } else {
      dataBase = DBType.Ulrika;
    }
    try {
      conn = DBUtil.getConnection(dataBase);
      conn.setAutoCommit(false);
      String sql = "CALL users_total_loan(?)";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1, userID);
      ResultSet rs = ps.executeQuery();
      while (rs.next()) {
        bikes.add(rs.getInt("bikeID"));
      }
      conn.commit();
    } catch (Exception e) {
      try {
        conn.rollback();
      } catch (SQLException e1) {
        e1.printStackTrace();
      }
      e.printStackTrace();
    }
    return bikes;
  }
}
