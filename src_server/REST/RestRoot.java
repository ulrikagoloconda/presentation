
package REST;

import Interfaces.DBAccess;
import Model.*;
import com.google.gson.Gson;
import helpers.AuthHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

//Klassen är en samling av endpoints som försörjer en klient med data. Programmet skapar, vid inloggning, en
//session med tillhörande session_token som sparas i databasen. Varje gång som kilenten efterfågar information efter
//inloggningen, måste användarens id och ett aktuellt session_token medfölja förfrågningen. Detta val har fått
//konsekvensen att alla metoder utom en är en POST, eftersom vi inte vill skicka id och aktuellt session_token i url.
//För att visa att vi kan göra även GET har ändå en metod genomförts med en GET-metod.
@Path("/resources")
public class RestRoot {
    private DBAccess dbAccess = new DBAccessImpl();


    //Metoden loggar in användaren i programmet och sakapar en randomiserad sträng, ett session_token, som sedan returneras och
    //sparas på klienten.
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String loginBikeUser(String json) {
        BikeUser currentUser = null;
        Gson gson = new Gson();
        BikeUser user;
        user = gson.fromJson(json, BikeUser.class);
        user.setUserID(0);
        try {
            currentUser = dbAccess.logIn(user.getUserName(), user.getPassw());
            if (currentUser.getUserID() > 0) {
                ArrayList<Bike> currentBikes = dbAccess.getUsersCurrentBikes(currentUser.getUserID());
                currentUser.setCurrentBikeLoans(currentBikes);
                currentUser.setTotalBikeLoans(dbAccess.getUsersTotalLoan(currentUser.getUserID()));
       /* System.out.println("getottalloans: " + currentUser.getTotalBikeLoans() +
            "get current. bikeloans: " + currentUser.getCurrentBikeLoans() + "phone : " + currentUser.getPhone());*/
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        MainViewInformaiton mvi = new MainViewInformaiton();
        mvi.setCurrentUser(currentUser);

        int total = dbAccess.getTotalNumOfbikes();
        int available = dbAccess.getNumOfCurrentAvailableBikes();
        mvi.setTotalBikes(total);
        mvi.setAvailableBikes(available);

        if (dbAccess.isSessionOpen(currentUser.getUserID())) {
            mvi.getCurrentUser().setSessionToken(dbAccess.readSessionToken(currentUser.getUserID()));
        } else {
            String s = AuthHelper.generateValidationToken();
            dbAccess.startSession(s, currentUser.getUserID());
            mvi.getCurrentUser().setSessionToken(s);
        }
        String jsonUser = gson.toJson(mvi);
        return jsonUser;
    }


    //Metoden skapar en ny användare. Efter registrerandet kan användaren logga in, först då skapas en ny session.
    @POST
    @Path("/newUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String newBikeUser(String json) {
        Gson gson = new Gson();
        BikeUser newUser;
        newUser = gson.fromJson(json, BikeUser.class);
        boolean isNewUserOK = false;
        try {
            isNewUserOK = dbAccess.insertNewUser(
                    newUser.getfName(), newUser.getUserName(), newUser.getMemberLevel(), newUser.getBirthYear(), newUser.getEmail(), newUser.getPhone(), newUser.getUserName(), newUser.getGender(), newUser.getPassw());


        } catch (Exception e) {
            e.printStackTrace();
        }

        String jsonUser = gson.toJson(isNewUserOK);
        return jsonUser;
    }

    //Metoden uppdaterar en användare i databasen.
    @POST
    @Path("/alterUser")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String updateBikeUser(String json) {
        Gson gson = new Gson();
        MainViewInformaiton mvi;
        mvi = gson.fromJson(json, MainViewInformaiton.class);
        boolean isUpdateUserOK = false;
        BikeUser user = mvi.getOldUser();

        String clientToken = dbAccess.readSessionToken(user.getUserID());

        if (mvi.getOldUser().getSessionToken().equals(clientToken)) {
            try {
                String gender = mvi.getAlteredUser().getGender();
                if (gender.equals("Kvinna")) {
                    gender = "Female";
                } else if (gender.equals("Man")) {
                    gender = "Male";
                } else {
                    gender = "Other";
                }
                isUpdateUserOK = dbAccess.UpdateUser(mvi.getAlteredUser().getfName(),
                        mvi.getAlteredUser().getlName(),
                        mvi.getAlteredUser().getMemberLevel(),
                        mvi.getAlteredUser().getEmail(),
                        mvi.getAlteredUser().getPhone(),
                        mvi.getOldUser().getUserName(),
                        gender,
                        mvi.getAlteredUser().getPassw());

            } catch (Exception e) {
                e.printStackTrace();
            }
            String jsonUser = gson.toJson(isUpdateUserOK);
            return jsonUser;
        }
        return null;
    }

    //Metoden returnerar en lista på alla cyklar som för tillfället är lediga
    @POST
    @Path("/availableBikes")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getAvailableBikes(String json) {
        try {
            Gson gson = new Gson();
            BikeUser user = gson.fromJson(json, BikeUser.class);
            String clientToken = dbAccess.readSessionToken(user.getUserID());
            if (user.getSessionToken().equals(clientToken)) {
                long millisStartdb = Calendar.getInstance().getTimeInMillis();
                Bikes bikes = dbAccess.selectAvailableBikes();
                user.getMesaurment().setDbProcedureSec(bikes.getPrestandaMeasurement().getDbProcedureSec());
                System.out.println("");
                long millisStoptdb = Calendar.getInstance().getTimeInMillis();
                Long milliLong = new Long(millisStoptdb - millisStartdb);
                user.getMesaurment().setReadFromDbJdbcSec(milliLong.doubleValue() / 1000.0);
                Bikes bikeCollection = new Bikes();
                bikeCollection.setPrestandaMeasurement(user.getMesaurment());
                bikeCollection.setBikes(bikes.getBikes());
                long millisStart = Calendar.getInstance().getTimeInMillis();
                String returnJson = gson.toJson(bikeCollection);
                long millisStop = Calendar.getInstance().getTimeInMillis();
                return returnJson;
            } else {
                return null;

            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Metoden tar emot en sträng som sedan används för att göra en wild card sökning i databasen
    @POST
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getSearchResults(String json) {
        Gson gson = new Gson();
        MainViewInformaiton mvi = gson.fromJson(json, MainViewInformaiton.class);
        String clientToken = dbAccess.readSessionToken(mvi.getCurrentUser().getUserID());
        if (mvi.getCurrentUser().getSessionToken().equals(clientToken)) {
            Map<String, Integer> searchMap = dbAccess.getSearchValue(mvi.getSearchValue());
            Bikes bikes = new Bikes();
            bikes.setSearchResults(searchMap);
            Gson gson1 = new Gson();
            String returnJson = gson1.toJson(bikes);
            return returnJson;
        } else {
            return null;
        }
    }

    //Metoden stänger en session genom att sätta en sluttid för det aktuella användares session i databasen
    @POST
    @Path("/closeSession")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String closeSession(String json) {
        try {
            Gson gson = new Gson();
            BikeUser user = gson.fromJson(json, BikeUser.class);
            String clientToken = dbAccess.readSessionToken(user.getUserID());
            if (user.getSessionToken().equals(clientToken)) {
                dbAccess.closeSession(user.getUserID());
                user.setSessionToken("-1");
                Gson gson1 = new Gson();
                String returnUser = gson1.toJson(user);
                return returnUser;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //Metoden returnerar en specifik cykel.
    @POST
    @Path("/getBike")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getSingleBike(String json) {
        try {
            Gson gson = new Gson();
            MainViewInformaiton mvi = gson.fromJson(json, MainViewInformaiton.class);
            String clientToken = dbAccess.readSessionToken(mvi.getCurrentUser().getUserID());
            if (mvi.getCurrentUser().getSessionToken().equals(clientToken)) {
                Bike returnBike = dbAccess.getBikeByID(mvi.getSingleBikeID());
                System.out.println("I rest root get gingle bike " + returnBike.isAvailable());
                Gson gson1 = new Gson();
                String returnJson = gson1.toJson(returnBike);
                return returnJson;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //Metoden utför ett lån genom att registrera lånet i databasen
    @POST
    @Path("/executeRental")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String executeBikeRent(String json) {
        Gson gson = new Gson();
        MainViewInformaiton mvi = gson.fromJson(json, MainViewInformaiton.class);
        String clientToken = dbAccess.readSessionToken(mvi.getCurrentUser().getUserID());
        if (mvi.getCurrentUser().getSessionToken().equals(clientToken)) {
            Bike returnBike = dbAccess.executeBikeLoan(mvi.getBikeToRentID(), mvi.getCurrentUser().getUserID());
            Gson gson1 = new Gson();
            String returnJson = gson1.toJson(returnBike);
            return returnJson;
        } else {
            return null;
        }
    }

    //Klassens enda GET-metod tar bort en cykel från databasen
    @GET
    @Path("/removeBike/{userID}/{sessionToken}/{bikeID}")
    @Produces(MediaType.TEXT_PLAIN)
    public String removeBike(@PathParam("userID") String userID, @PathParam("sessionToken") String token, @PathParam("bikeID") String bikeID) {
        String returnString = "Du har inte rätt access";
        try {
            int userIDInt = Integer.parseInt(userID);
            int bikeIDInt = Integer.parseInt(bikeID);
            String clientToken = dbAccess.readSessionToken(userIDInt);
            if (token.equals(clientToken)) {
                dbAccess.deleteBike(bikeIDInt);
                if (dbAccess.deleteBike(bikeIDInt)) {
                    returnString = "Cykel med cykelID " + bikeID + " har raderats från databasen";
                }
            } else {
                return returnString;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnString;
    }


    //Metoden lägger till en ny cykel i databasen
    @POST
    @Path("/newBike")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String addBikeToDB(String json) {
        Gson gson = new Gson();
        MainViewInformaiton mvi = gson.fromJson(json, MainViewInformaiton.class);
        String clientToken = dbAccess.readSessionToken(mvi.getCurrentUser().getUserID());
        if (mvi.getCurrentUser().getSessionToken().equals(clientToken)) {
            System.out.println("Här körs add bike i restroot ");
            Bike returnBike = dbAccess.insertNewBike(mvi.getNewBike());
            Gson gson1 = new Gson();
            String returnJson = gson1.toJson(returnBike);
            return returnJson;
        } else {
            return null;
        }
    }

    //Metoden returnerar alla cyklar som finns i databasen, för att få tillgång till denna måste användarens id vara satt
// till 10, dvs: Metoden är bara för administratörer
    @POST
    @Path("/getAllBikes")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getAllBikes(String json) {
        Gson gson = new Gson();
        BikeUser user = gson.fromJson(json, BikeUser.class);
        String clientToken = dbAccess.readSessionToken(user.getUserID());
        if (user.getSessionToken().equals(clientToken) && user.getMemberLevel() == 10) {
            Bikes bikes = new Bikes();
            bikes.setBikes(dbAccess.getAllBikes());
            Gson gson1 = new Gson();
            String returnJson = gson1.toJson(bikes);
            return returnJson;
        } else {
            return null;
        }

    }

    //Metoden lämnar tillbaka en cykel efter lån, dvs lånet registreras som avslutat i databasen
    @POST
    @Path("/returnBike")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String returnBike(String json) {
        try {
            Gson gson = new Gson();
            MainViewInformaiton mvi = gson.fromJson(json, MainViewInformaiton.class);
            String clientToken = dbAccess.readSessionToken(mvi.getCurrentUser().getUserID());

            if (mvi.getCurrentUser().getSessionToken().equals(clientToken)) {
                boolean returnOk = AccessBike.returnBike(mvi.getBikeToReturnID(), mvi.getCurrentUser().getUserID());
                System.out.println("I restroot return bike " + returnOk);
                Gson gson1 = new Gson();
                String returnJson = gson1.toJson(returnOk);
                return returnJson;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //Metod hämtar ny information från databasen för att kunna uppdatera gui.
    @POST
    @Path("/fetchUpdate")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String fetchUpdatedInfo(String json) {
        Gson gson = new Gson();
        BikeUser user = gson.fromJson(json, BikeUser.class);
        String clientToken = dbAccess.readSessionToken(user.getUserID());
        if (user.getSessionToken().equals(clientToken)) {
            MainViewInformaiton mvi = new MainViewInformaiton();
            user.setCurrentBikeLoans(AccessBike.getCurrentBikesByUserID(user.getUserID()));
            user.setTotalBikeLoans(AccessRentbridge.getUsersTotalLoan(user.getUserID()));
            mvi.setTotalBikes(AccessBike.getTotalNumOfBikes());
            mvi.setAvailableBikes(AccessBike.getNumOfCurrentAvailableBikes());
            user.setSessionToken(null);
            mvi.setCurrentUser(user);
            Gson gson1 = new Gson();
            String returnJson = gson1.toJson(mvi);
            return returnJson;
        } else {
            return null;
        }
    }

    @POST
    @Path("/prestandaMeasurment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String prestandaMesaurment(String json) {
        int mesaurmentID = 0;
        try {
            Gson gson = new Gson();
            BikeUser user = gson.fromJson(json, BikeUser.class);
            String clientToken = dbAccess.readSessionToken(user.getUserID());
            if (user.getSessionToken().equals(clientToken)) {
                mesaurmentID = dbAccess.insertPrestandaMesaurment(user.getMesaurment());
                System.out.println("Här körs insert Measrumet ");
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return mesaurmentID + "";
    }


    @POST
    @Path("/nextTenAvailableBikes")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getnextTenAvailableBikes(String json) {
        try {
            Gson gson = new Gson();
            MainViewInformaiton mvi = gson.fromJson(json, MainViewInformaiton.class);
            String clientToken = dbAccess.readSessionToken(mvi.getCurrentUser().getUserID());
            if (mvi.getCurrentUser().getSessionToken().equals(clientToken)) {
                long millistart = Calendar.getInstance().getTimeInMillis();
                Bikes returnBikes = dbAccess.getNextAvailableBikes(mvi.getBikes().getTenNextfromInt(), mvi.getBikes().getNumberOfBikesRead());
                long millistop = Calendar.getInstance().getTimeInMillis();
                double readFromDB = (millistop - millistart) / 1000.0;
                mvi.getBikes().getPrestandaMeasurement().setReadFromDbJdbcSec(readFromDB);
                Gson gson1 = new Gson();
                mvi.getBikes().getPrestandaMeasurement().setDbProcedureSec(returnBikes.getPrestandaMeasurement().getDbProcedureSec());
                returnBikes.setPrestandaMeasurement(mvi.getBikes().getPrestandaMeasurement());
                String returnJson = gson1.toJson(returnBikes);
                return returnJson;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @POST
    @Path("/nextTenAvailableBikespreviousChoise")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getnextTenAvailableBikesPreviousChoise(String json) {
        System.out.println(" i nextTenAvailableBikespreviousChoise ");
        try {
            Gson gson = new Gson();
            MainViewInformaiton mvi = gson.fromJson(json, MainViewInformaiton.class);
            String clientToken = dbAccess.readSessionToken(mvi.getCurrentUser().getUserID());
            if (mvi.getCurrentUser().getSessionToken().equals(clientToken)) {
                long millistart = Calendar.getInstance().getTimeInMillis();
                Bikes returnBikes = dbAccess.getNextAvailableBikes(mvi.getBikes().getTenNextfromInt(), mvi.getBikes().getNumberOfBikesRead(), mvi.getCurrentUser().getUserID());
                long millistop = Calendar.getInstance().getTimeInMillis();
                double readFromDB = (millistop - millistart) / 1000.0;
                mvi.getBikes().getPrestandaMeasurement().setReadFromDbJdbcSec(readFromDB);
                Gson gson1 = new Gson();
                mvi.getBikes().getPrestandaMeasurement().setDbProcedureSec(returnBikes.getPrestandaMeasurement().getDbProcedureSec());
                returnBikes.setPrestandaMeasurement(mvi.getBikes().getPrestandaMeasurement());
                String returnJson = gson1.toJson(returnBikes);
                return returnJson;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    @POST
    @Path("/nextTenAvailableBikesNotpreviousChoise")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public String getnextTenAvailableBikesNotPreviousChoise(String json) {
        System.out.println(" i nextTenAvailableBikespreviousChoise NOT");
        try {
            Gson gson = new Gson();
            MainViewInformaiton mvi = gson.fromJson(json, MainViewInformaiton.class);
            String clientToken = dbAccess.readSessionToken(mvi.getCurrentUser().getUserID());
            if (mvi.getCurrentUser().getSessionToken().equals(clientToken)) {
                long millistart = Calendar.getInstance().getTimeInMillis();
                Bikes returnBikes = dbAccess.getNextAvailableBikesNotPrevious(mvi.getBikes().getTenNextfromInt(), mvi.getBikes().getNumberOfBikesRead(), mvi.getCurrentUser().getUserID());
                long millistop = Calendar.getInstance().getTimeInMillis();
                double readFromDB = (millistop - millistart) / 1000.0;
                mvi.getBikes().getPrestandaMeasurement().setReadFromDbJdbcSec(readFromDB);
                Gson gson1 = new Gson();
                mvi.getBikes().getPrestandaMeasurement().setDbProcedureSec(returnBikes.getPrestandaMeasurement().getDbProcedureSec());
                returnBikes.setPrestandaMeasurement(mvi.getBikes().getPrestandaMeasurement());
                String returnJson = gson1.toJson(returnBikes);
                return returnJson;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

