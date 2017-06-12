package Model;

import helpers.DoughnutChart;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;


public class PrestandaMeasurement {
    private Integer measuramentId;
    private LocalDateTime dateTime;
    private double totalTimeSec;
    private double perceivedTimeAvailableBikesSec;
    private double dbProcedureSec;
    private double readFromDbJdbcSec;
    private double gsonToJsonSec;
    private double executeSec;
    private double gsonFromJsonSec;
    private double readOneBike;
    private String comment;
    private double totalSizeDataMb;

    public PrestandaMeasurement() {

    }

    public PrestandaMeasurement(Integer measuramentId, LocalDateTime dateTime, double totalTimeSec, double perceivedTimeAvailableBikesSec, double dbProcedureSec, double readFromDbJdbcSec, double gsonToJsonSec, double executeSec, double gsonFromJsonSec, double readOneBike, String comment, double totalSizeDataMb) {
        this.measuramentId = measuramentId;
        this.dateTime = dateTime;
        this.totalTimeSec = totalTimeSec;
        this.perceivedTimeAvailableBikesSec = perceivedTimeAvailableBikesSec;
        this.dbProcedureSec = dbProcedureSec;
        this.readFromDbJdbcSec = readFromDbJdbcSec;
        this.gsonToJsonSec = gsonFromJsonSec;
        this.executeSec = executeSec;
        this.gsonFromJsonSec = gsonFromJsonSec;
        this.readOneBike = readOneBike;
        this.comment = comment;
        this.totalSizeDataMb = totalSizeDataMb;

    }

    public Integer getMeasuramentId() {

        return measuramentId;
    }

    public void setMeasuramentId(Integer measuramentId) {
        this.measuramentId = measuramentId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getTotalTimeSec() {
        return roundDouble(totalTimeSec);
    }

    public void setTotalTimeSec(double totalTimeSec) {
        this.totalTimeSec = totalTimeSec;
    }

    public double getPerceivedTimeAvailableBikesSec() {
        return roundDouble(perceivedTimeAvailableBikesSec);
    }

    public void setPerceivedTimeAvailableBikesSec(double perceivedTimeAvailableBikesSec) {
        this.perceivedTimeAvailableBikesSec = perceivedTimeAvailableBikesSec;
    }

    public double getDbProcedureSec() {
        return roundDouble(dbProcedureSec);
    }

    public void setDbProcedureSec(double dbProcedureSec) {
        this.dbProcedureSec = dbProcedureSec;
    }

    public double getReadFromDbJdbcSec() {
        return roundDouble(readFromDbJdbcSec);
    }

    public void setReadFromDbJdbcSec(double readFromDbJdbcSec) {
        this.readFromDbJdbcSec = readFromDbJdbcSec;
    }

    public double getGsonToJsonSec() {
        return roundDouble(gsonToJsonSec);
    }

    public void setGsonToJsonSec(double gsonToJsonSec) {
        this.gsonToJsonSec = gsonToJsonSec;
    }

    public double getExecuteSec() {
        return roundDouble(executeSec);
    }

    public void setExecuteSec(double executeSec) {
        this.executeSec = executeSec;
    }

    public double getGsonFromJsonSec() {
        return roundDouble(gsonFromJsonSec);
    }

    public void setGsonFromJsonSec(double gsonFromJsonSec) {
        this.gsonFromJsonSec = gsonFromJsonSec;
    }

    public double getReadOneBike() {
        return roundDouble(readOneBike);
    }

    public void setReadOneBike(double readOneBike) {
        this.readOneBike = readOneBike;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getTotalSizeDataMb() {
        return roundDouble(totalSizeDataMb);
    }

    public void setTotalSizeDataMb(double totalSizeDataMb) {
        this.totalSizeDataMb = totalSizeDataMb;
    }


    public double roundDouble(double value) {
        double bd =  new BigDecimal(value).setScale(4, RoundingMode.HALF_UP).doubleValue();
        String ds = Double.toString(bd);
        ds = ds.replace("E","");
        bd = new BigDecimal(ds).setScale(4, RoundingMode.HALF_UP).doubleValue();
        return bd;
    }
}


/**
 * Created by Goloconda on 2017-04-05.
 *
public class PrestandaMeasurement {
    private Integer measuramentId;
    private LocalDateTime dateTime;
    private double totalTimeSec;
    private double perceivedTimeAvailableBikesSec;
    private double dbProcedureSec;
    private double readFromDbJdbcSec;
    private double gsonToJsonSec;
    private double executeSec;
    private double gsonFromJsonSec;
    private double readOneBike;
    private String comment;
    private double totalSizeDataMb;

    public PrestandaMeasurement() {

    }

    public PrestandaMeasurement(Integer measuramentId, LocalDateTime dateTime, double totalTimeSec, double perceivedTimeAvailableBikesSec, double dbProcedureSec, double readFromDbJdbcSec, double gsonToJsonSec, double executeSec, double gsonFromJsonSec, double readOneBike, String comment, double totalSizeDataMb) {
        this.measuramentId = measuramentId;
        this.dateTime = dateTime;
        this.totalTimeSec = totalTimeSec;
        this.perceivedTimeAvailableBikesSec = perceivedTimeAvailableBikesSec;
       this.dbProcedureSec = dbProcedureSec;
        this.readFromDbJdbcSec = readFromDbJdbcSec;
        this.gsonToJsonSec = gsonFromJsonSec;
        this.executeSec = executeSec;
        this.gsonFromJsonSec = gsonFromJsonSec;
        this.readOneBike = readOneBike;
        this.comment = comment;
        this.totalSizeDataMb = totalSizeDataMb;
    }

    public Integer getMeasuramentId() {
        return measuramentId;
    }

    public void setMeasuramentId(Integer measuramentId) {
        this.measuramentId = measuramentId;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getTotalTimeSec() {
        return totalTimeSec;
    }

    public void setTotalTimeSec(double totalTimeSec) {
        this.totalTimeSec = totalTimeSec;
    }

    public double getPerceivedTimeAvailableBikesSec() {
        return perceivedTimeAvailableBikesSec;
    }

    public void setPerceivedTimeAvailableBikesSec(double perceivedTimeAvailableBikesSec) {
        this.perceivedTimeAvailableBikesSec = perceivedTimeAvailableBikesSec;
    }

    public double getDbProcedureSec() {
        return dbProcedureSec;
    }

    public void setDbProcedureSec(double dbProcedureSec) {
        this.dbProcedureSec = dbProcedureSec;
    }

    public double getReadFromDbJdbcSec() {
        return readFromDbJdbcSec;
    }

    public void setReadFromDbJdbcSec(double readFromDbJdbcSec) {
        this.readFromDbJdbcSec = readFromDbJdbcSec;
    }

    public double getGsonToJsonSec() {
        return gsonToJsonSec;
    }

    public void setGsonToJsonSec(double gsonToJsonSec) {
        this.gsonToJsonSec = gsonToJsonSec;
    }

    public double getExecuteSec() {
        return executeSec;
    }

    public void setExecuteSec(double executeSec) {
        this.executeSec = executeSec;
    }

    public double getGsonFromJsonSec() {
        return gsonFromJsonSec;
    }

    public void setGsonFromJsonSec(double gsonFromJsonSec) {
        this.gsonFromJsonSec = gsonFromJsonSec;
    }

    public double getReadOneBike() {
        return readOneBike;
    }

    public void setReadOneBike(double readOneBike) {
        this.readOneBike = readOneBike;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getTotalSizeDataMb() {
        return totalSizeDataMb;
    }

    public void setTotalSizeDataMb(double totalSizeDataMb) {
        this.totalSizeDataMb = totalSizeDataMb;
    }
}
 */
