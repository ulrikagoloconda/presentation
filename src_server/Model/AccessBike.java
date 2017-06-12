package Model;

import helpers.PCRelated;

import java.io.ByteArrayInputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Ulrika Goloconda Fahlén
 * @version 1.0
 * @since 2016-09-16
 */
public class AccessBike {
    public static boolean returnBike(int bikeID, int userID) {
        DBType dataBase = null;
        Connection conn = null;
        boolean returnBool = false;
        if (helpers.PCRelated.isThisNiklasPC()) {
            dataBase = DBType.Niklas;
        } else {
            dataBase = DBType.Ulrika;
        }
        try {
            conn = DBUtil.getConnection(dataBase);
            String sql = "CALL return_bike(?,?, ?)";
            CallableStatement cs = conn.prepareCall(sql);
            cs.setInt(1, bikeID);
            cs.setInt(2, userID);
            cs.registerOutParameter(3, Types.BOOLEAN);
            ResultSet rs = cs.executeQuery();
            returnBool = cs.getBoolean(3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnBool;
    }


    public static Bike insertNewBike(Bike newBike) {
        DBType dataBase = null;
        Connection conn = null;
        if (helpers.PCRelated.isThisNiklasPC()) {
            dataBase = DBType.Niklas;
        } else {
            dataBase = DBType.Ulrika;
        }
        try {
            conn = DBUtil.getConnection(dataBase);
            String sql = "CALL insert_bike(?,?,?,?,?,?,?)";
            CallableStatement cs = conn.prepareCall(sql);
            cs.setString(1, newBike.getBrandName());
            cs.setString(2, newBike.getType());
            cs.setInt(3, newBike.getModelYear());
            cs.setString(4, newBike.getColor());
            cs.setInt(5, newBike.getSize());
            ByteArrayInputStream bais = newBike.getImageStream();
            cs.setBinaryStream(6, bais);
            if(newBike.getState()==null){
                Random random = new Random();
                int stat = random.nextInt(6 - 1) + 1;
                cs.setInt(7,stat);
            }else {
                cs.setInt(7, newBike.getState());
            }
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                newBike.setBikeID(rs.getInt("bikeID"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return newBike;
    }

    public static Bikes selectAvailableBikes() {
        ArrayList<Bike> availableBikes = new ArrayList<>();
        Bikes bikes = new Bikes();
        DBType dataBase = null;
        Connection conn = null;
        if (PCRelated.isThisNiklasPC()) {
            dataBase = DBType.Niklas;
        } else {
            dataBase = DBType.Ulrika;
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DBUtil.getConnection(dataBase);
            conn.setAutoCommit(false);
            String sql = "CALL search_available_bikes(?)";
            CallableStatement ps = conn.prepareCall(sql);
            ResultSet rs = ps.executeQuery();
            ps.registerOutParameter(1, Types.BIGINT);
            int i = 0;
            while (rs.next()) {
                i++;
                Bike b = new Bike();
                b.setAvailable(true);
                b.setBikeID(rs.getInt("bikeID"));
                b.setColor(rs.getString("color"));
                b.setSize(rs.getInt("size"));
                b.setModelYear(rs.getInt("modelyear"));
                Blob blob = rs.getBlob("image");
                byte[] bytes = blob.getBytes(1, (int) blob.length());
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                b.setImageStream(bis);
                b.setType(rs.getString("typeName"));
                b.setBrandName(rs.getString("brandname"));
                b.setImageFileName(rs.getString("imageFileName"));
               /* LocalDate actualReturn = rs.getDate("dayOfActualReturn").toLocalDate();
                LocalDate dayOfRent = rs.getDate("dayOfRent").toLocalDate();*/
                availableBikes.add(b);

            }
            long totalTimeSearchAvailableBikes = (ps.getLong(1));
            Long totalLong = new Long(totalTimeSearchAvailableBikes);
            double serachTotal = totalLong.doubleValue();
            bikes.setBikes(availableBikes);
            PrestandaMeasurement prestandaMeasurement = new PrestandaMeasurement();
            prestandaMeasurement.setDbProcedureSec(serachTotal/1000.0);
            bikes.setPrestandaMeasurement(prestandaMeasurement);
            conn.commit();

        } catch (Exception e) {
            try {
                e.printStackTrace();
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bikes;
    }


    public static ArrayList<Bike> getAllBikes() {
        ArrayList<Bike> allBikes = new ArrayList<>();
        DBType dataBase = null;
        Connection conn = null;
        if (helpers.PCRelated.isThisNiklasPC()) {
            dataBase = DBType.Niklas;
        } else {
            dataBase = DBType.Ulrika;
        }
        try {
            conn = DBUtil.getConnection(dataBase);
            String sql = "CALL get_all_bikes()";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Bike b = new Bike();
                b.setBikeID(rs.getInt("bikeID"));
                b.setColor(rs.getString("color"));
                b.setSize(rs.getInt("size"));
                b.setModelYear(rs.getInt("modelyear"));
                b.setType(rs.getString("typeName"));
                b.setBrandName(rs.getString("brandname"));
                allBikes.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allBikes;
    }

    public static boolean deleteBike(int bikeID) {
        boolean returnBool;
        DBType dataBase = null;
        Connection conn = null;
        if (helpers.PCRelated.isThisNiklasPC()) {
            dataBase = DBType.Niklas;
        } else {
            dataBase = DBType.Ulrika;
        }
        try {
            conn = DBUtil.getConnection(dataBase);
            conn.setAutoCommit(false);
            String sql = "CALL delete_bike(?, ?)";
            CallableStatement ps = conn.prepareCall(sql);
            ps.setInt(1, bikeID);
            ps.registerOutParameter(2, Types.BOOLEAN);
            ps.executeQuery();
            conn.commit();
            returnBool = ps.getBoolean(2);
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            returnBool = false;
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnBool;
    }


    public static Bike executeBikeLoan(int bikeID, int userID) {
        DBType dataBase = null;
        Connection conn = null;
        Date dayOfReturn = null;
       Bike returnBike = null;
        if (helpers.PCRelated.isThisNiklasPC()) {
            dataBase = DBType.Niklas;
        } else {
            dataBase = DBType.Ulrika;
        }
        try {
            conn = DBUtil.getConnection(dataBase);
            conn.setAutoCommit(false);
            String sql = "CALL execute_bike_loan(?,?,?,?)";
            CallableStatement cs = conn.prepareCall(sql);
            cs.setInt(1, userID);
            cs.setInt(2, bikeID);
            long minDay = LocalDate.of(2016, 1, 1).toEpochDay();
            long maxDay = LocalDate.of(2017, 05, 07).toEpochDay();
            long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
            LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
            Date date = Date.valueOf(randomDate);
            cs.setDate(3, date);
            cs.registerOutParameter(4, Types.DATE);
            cs.executeQuery();
            dayOfReturn = cs.getDate(4);
            returnBike = getBikeByID(bikeID);
            if(dayOfReturn!=null) {
                returnBike.setDayOfReturn(dayOfReturn.toLocalDate());
            } else {
                System.out.println("Lånet genomfördes inte");
            }
            conn.commit();
            return returnBike;

        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return returnBike;
    }

    public static Map<String, Integer> getSearchValue(String text) {
        Map<String, Integer> mapToReturn = new HashMap<>();
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
            String sql = "CALL search_by_string(?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, text);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int i = rs.getInt("bikeID");

                String s1 = rs.getString("color");
                String s2 = rs.getString("brandname");
                String s3 = rs.getString("typeName");
                String s4 = s2 + " " + s1 + " " + s3;
                mapToReturn.put(s4, i);
            }
            rs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return mapToReturn;

    }

    public static Bike getBikeByID(int bikeID) {
        Bike b = new Bike();

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
            String sql = "Call get_bike_returnedDate_from_ID(?, ?)";
            CallableStatement cs = conn.prepareCall(sql);
            cs.setInt(1, bikeID);
            cs.registerOutParameter(2, Types.BOOLEAN);
            ResultSet rs = cs.executeQuery();
            b.setAvailable(cs.getBoolean(2));

            if (rs.next()) {
                b.setBrandName(rs.getString("brandname"));
                Blob blob = rs.getBlob("image");
                byte[] bytes = blob.getBytes(1, (int) blob.length());
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
              //  BufferedImage theImage = ImageIO.read(new ByteArrayInputStream(bytes));
               // b.setBufferedImage(theImage);
                b.setImageStream(bis);
                b.setColor(rs.getString("color"));
                b.setType(rs.getString("typeName"));
                b.setModelYear(rs.getInt("modelyear"));
                b.setBikeID(bikeID);
                b.setSize(rs.getInt("size"));
            } else {
                rs.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }


    public static ArrayList<Bike> getCurrentBikesByUserID(int userID) {
        ArrayList<Bike> bikeList = new ArrayList<>();
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
            String sql = "CALL users_current_bikes(?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Bike tempBike = new Bike();
                tempBike.setBikeID(rs.getInt("bikeid"));
                tempBike.setModelYear(rs.getInt("modelyear"));
                tempBike.setColor(rs.getString("color"));
                Blob blob = rs.getBlob("image");
                byte[] bytes = blob.getBytes(1, (int) blob.length());
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
               tempBike.setImageStream(bis);
                tempBike.setSize(rs.getInt("size"));
                tempBike.setType(rs.getString("typeName"));
                tempBike.setBrandName(rs.getString("brandname"));
                tempBike.setDayOfRent(rs.getDate("dayOfRent").toLocalDate());
                tempBike.setDayOfReturn(rs.getDate("dayOfReturn").toLocalDate());
                bikeList.add(tempBike);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bikeList;
    }

    public static int getTotalNumOfBikes() {
        DBType dataBase = null;
        Connection conn = null;
        int returnInt = 0;
        if (helpers.PCRelated.isThisNiklasPC()) {
            dataBase = DBType.Niklas;
        } else {
            dataBase = DBType.Ulrika;
        }
        try {
            conn = DBUtil.getConnection(dataBase);
            String sql = "CALL get_num_of_total_bikes()";
            PreparedStatement ps = conn.prepareCall(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                returnInt = rs.getInt("totalBikes");
                return returnInt;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnInt;
    }

    public static int getNumOfCurrentAvailableBikes() {
        DBType dataBase = null;
        Connection conn = null;
        int returnInt = 0;
        if (helpers.PCRelated.isThisNiklasPC()) {
            dataBase = DBType.Niklas;
        } else {
            dataBase = DBType.Ulrika;
        }
        try {
            conn = DBUtil.getConnection(dataBase);
            String sql = "CALL get_num_of_curr_available_bikes()";
            PreparedStatement ps = conn.prepareCall(sql);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                returnInt = rs.getInt("count");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return returnInt;
    }

    public static Bikes getNextAvailableBikes(int nextfromInt, int numberOfBikesRead) {
        Bikes bikes = new Bikes();
        PrestandaMeasurement pm = new PrestandaMeasurement();
        bikes.setPrestandaMeasurement(pm);
        ArrayList<Bike> bikeList = new ArrayList<>();
        DBType dataBase = null;
        Connection conn = null;
        Date dayOfReturn = null;

        if (helpers.PCRelated.isThisNiklasPC()) {
            dataBase = DBType.Niklas;
        } else {
            dataBase = DBType.Ulrika;
        }
        try {
            conn = DBUtil.getConnection(dataBase);
            String sql = "CALL search_next_available_bikes(?,?,?,?)";
            CallableStatement ps = conn.prepareCall(sql);
            ps.setInt(1,nextfromInt );
            ps.setInt(2,numberOfBikesRead);
            ps.registerOutParameter(3, Types.INTEGER);
            ps.registerOutParameter(4, Types.INTEGER);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Bike tempBike = new Bike();
                tempBike.setBikeID(rs.getInt("bikeid"));
                tempBike.setModelYear(rs.getInt("modelyear"));
                tempBike.setColor(rs.getString("color"));
                Blob blob = rs.getBlob("image");
                byte[] bytes = blob.getBytes(1, (int) blob.length());
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                tempBike.setImageStream(bis);
                tempBike.setSize(rs.getInt("size"));
                tempBike.setType(rs.getString("typeName"));
                tempBike.setBrandName(rs.getString("brandname"));
                boolean isAvailable = rs.getBoolean("available");
                tempBike.setAvailable(isAvailable);
                bikeList.add(tempBike);
            }
           bikes.setLasID( ps.getInt(3));
            double timeDB = ps.getDouble(4)/1000.0;

            bikes.getPrestandaMeasurement().setDbProcedureSec(timeDB);

        } catch (Exception e) {
            e.printStackTrace();
        }
        bikes.setBikes(bikeList);
        bikes.setTenNextfromInt(nextfromInt + numberOfBikesRead);
        return bikes;
    }

    public static Bikes getNextAvailableBikes(Integer tenNextfromInt, Integer numberOfBikesRead, int userID) {
        Bikes bikes = new Bikes();
        PrestandaMeasurement pm = new PrestandaMeasurement();
        bikes.setPrestandaMeasurement(pm);
        ArrayList<Bike> bikeList = new ArrayList<>();
        DBType dataBase = null;
        Connection conn = null;
        Date dayOfReturn = null;

        if (helpers.PCRelated.isThisNiklasPC()) {
            dataBase = DBType.Niklas;
        } else {
            dataBase = DBType.Ulrika;
        }
        try {
            conn = DBUtil.getConnection(dataBase);
            System.out.println(userID);
            String sql = "CALL search_next_available_bikes_previous_choise(?,?,?,?,?)";
            CallableStatement ps = conn.prepareCall(sql);
            ps.setInt(1,tenNextfromInt );
            ps.setInt(2,userID);
            ps.setInt(3,numberOfBikesRead);
            ps.registerOutParameter(4, Types.INTEGER);
            ps.registerOutParameter(5, Types.INTEGER);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Bike tempBike = new Bike();
                tempBike.setBikeID(rs.getInt("bikeid"));
                tempBike.setModelYear(rs.getInt("modelyear"));
                tempBike.setColor(rs.getString("color"));
                Blob blob = rs.getBlob("image");
                byte[] bytes = blob.getBytes(1, (int) blob.length());
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                tempBike.setImageStream(bis);
                tempBike.setSize(rs.getInt("size"));
                tempBike.setType(rs.getString("typeName"));
                tempBike.setBrandName(rs.getString("brandname"));
                boolean isAvailable = rs.getBoolean("available");
                tempBike.setAvailable(isAvailable);
                bikeList.add(tempBike);
            }
            bikes.setLasID( ps.getInt(4));
            double timeDB = ps.getDouble(5)/1000.0;

            bikes.getPrestandaMeasurement().setDbProcedureSec(timeDB);

        } catch (Exception e) {
            e.printStackTrace();
        }
        bikes.setBikes(bikeList);
        bikes.setTenNextfromInt(tenNextfromInt + numberOfBikesRead);
        for(Bike b : bikes.getBikes()){
            System.out.println(b.getColor());
        }
        return bikes;
    }

    public static Bikes getNextAvailableBikesNotPrevious(Integer tenNextfromInt, Integer numberOfBikesRead, int userID) {
        Bikes bikes = new Bikes();
        PrestandaMeasurement pm = new PrestandaMeasurement();
        bikes.setPrestandaMeasurement(pm);
        ArrayList<Bike> bikeList = new ArrayList<>();
        DBType dataBase = null;
        Connection conn = null;
        Date dayOfReturn = null;

        if (helpers.PCRelated.isThisNiklasPC()) {
            dataBase = DBType.Niklas;
        } else {
            dataBase = DBType.Ulrika;
        }
        try {
            conn = DBUtil.getConnection(dataBase);
            System.out.println(userID);
            String sql = "CALL search_next_available_bikes_not_previous_choise(?,?,?,?,?)";
            CallableStatement ps = conn.prepareCall(sql);
            ps.setInt(1,tenNextfromInt );
            ps.setInt(2,userID);
            ps.setInt(3,numberOfBikesRead);
            ps.registerOutParameter(4, Types.INTEGER);
            ps.registerOutParameter(5, Types.INTEGER);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Bike tempBike = new Bike();
                tempBike.setBikeID(rs.getInt("bikeid"));
                tempBike.setModelYear(rs.getInt("modelyear"));
                tempBike.setColor(rs.getString("color"));
                Blob blob = rs.getBlob("image");
                byte[] bytes = blob.getBytes(1, (int) blob.length());
                ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                tempBike.setImageStream(bis);
                tempBike.setSize(rs.getInt("size"));
                tempBike.setType(rs.getString("typeName"));
                tempBike.setBrandName(rs.getString("brandname"));
                boolean isAvailable = rs.getBoolean("available");
                tempBike.setAvailable(isAvailable);
                bikeList.add(tempBike);
            }
            bikes.setLasID( ps.getInt(4));
            double timeDB = ps.getDouble(5)/1000.0;

            bikes.getPrestandaMeasurement().setDbProcedureSec(timeDB);

        } catch (Exception e) {
            e.printStackTrace();
        }
        bikes.setBikes(bikeList);
        bikes.setTenNextfromInt(tenNextfromInt + numberOfBikesRead);
        return bikes;
    }
}




