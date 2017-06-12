;import Interfaces.DBAccess;
import Model.*;
import REST.RestRoot;
import com.google.gson.Gson;
import helpers.AuthHelper;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

//Denna klass ska bara anv�ndas f�r att testk�ra metoder
public class Main {
	private static DBAccess dbAccess = new DBAccessImpl();
private static ByteArrayInputStream stream;
	public static void main(String[] args) {
		System.out.println("Obs, k�rs fr�n main och inte som server ");

		Bikes bikes = AccessBike.getNextAvailableBikes(0,10,19);
		for(Bike b : bikes.getBikes()){
			System.out.println(b.getColor());
		}

		AccessBike.getNextAvailableBikesNotPrevious(0,10,19);

		/*ArrayList<Bike> bikes = new ArrayList<>();
		for (int i = 1; i <=7; i++){
			bikes.add(AccessBike.getBikeByID(i));
		}

		Random random = new Random();
		for (int i = 0; i < 100; i++ ) {
			Bike b = bikes.get(random.nextInt(bikes.size()));
			b.getImageStream().reset();
			System.out.println(AccessBike.insertNewBike(b));
		}





		//AccessBike.insertNewBike()


		//addBikesChild();
	/*
	barn_sport 39429 rosa
	Herr_sport 39003 gul Cresent
	Herr_city 39431 Grön Hammarby
	Dam_city 39425 vit Monark
	Dam_sport 39430 Röd Scott
	MTB_unisex 39124 svart Crecent
	Barn 38667 blå Yosmite

	 */

//rentBIkes();
		/*long minDay = LocalDate.of(1945, 1, 1).toEpochDay();
		long maxDay = LocalDate.of(2017, 05, 07).toEpochDay();
		long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
		LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
		System.out.println(randomDate);
		for(int i = 0; i<30; i++) {
			Random rand = new Random();
			int j = rand.nextInt(105);
			System.out.println(j+43);
			ArrayList<Bike> list = bikes.getBikes();
			int k = rand.nextInt(list.size());
			System.out.println(list.get(k).getBikeID() + " " + j );
			System.out.println(AccessBike.executeBikeLoan(list.get(k).getBikeID(),j+43));

		}*/

		//System.out.println(gc.get(gc.YEAR) + "-" + (gc.get(gc.MONTH) + 1) + "-" + gc.get(gc.DAY_OF_MONTH));
		//43-150

		/*(int i = 0; i < 50; i++){
			Random rand = new Random();
			String s = generateString(rand, "abcdefghijklmnopqrstuvwxyzåäö", 7);
			GregorianCalendar gc = new GregorianCalendar();
			int year = randBetween(1945, 2000);
			Year year1 = Year.of(year);
			boolean isok = AccessUser.insertNewUser("Användare", "Användare", 1,year1,"användare@hotmail.com", 0704455345,s, "Other", s);
			System.out.println(isok);
		}
*/

		PrestandaMeasurement pm = new PrestandaMeasurement();
		String salt = AuthHelper.generateValidationToken();
		//String fname, String lname, int memberlevel, Year year, String email, int phone, String username, String gender, String passw
		Year yeartry= Year.of(1985);
		//boolean isok = AccessUser.insertNewUser("Användare", "Användare", 1,year,"användare@hotmail.com", 0704455345,"användare6", "Female", "användare");
		//System.out.println(isok);


		//Bikes bikes = AccessBike.getNextAvailableBikes(0,20);
Bike bike2 = AccessBike.getBikeByID(39427);
stream = bike2.getImageStream();
/*for(int i = 0; i < 300; i++) {
	//addBikesChild();
}
		/*Bikes bikes = AccessBike.getNextAvailableBikes(0, 40);
		System.out.println(bikes.getBikes().size() + " storlek på listan " + bikes.getLasID());

		//Bike bi = AccessBike.getBikeByID(38589);
		//AccessBike.returnBike(38593,19);
		//AccessBike.executeBikeLoan(38593,19);
		//AccessBike.returnBike(38593,19);
		//Bike bi = AccessBike.getBikeByID(38593);
		//System.out.println(bi.getImageStream() + " " + bi.isAvailable());
		long millisStart = Calendar.getInstance().getTimeInMillis();
		//AccessBike.executeBikeLoan(38593,19);
		long millisStop = Calendar.getInstance().getTimeInMillis();
		System.out.println("Tid det tar att utföra lån " + (millisStop - millisStart));
		//Bike bi2 = AccessBike.getBikeByID(38593);
		//System.out.println(bi2.getImageStream() + " " + bi2.isAvailable());




		//Integer measuramentId, LocalDateTime dateTime, double totalTimeSec, double perceivedTimeAvailableBikesSec,
		// double dbProcedureSec, double readFromDbJdbcSec, double gsonToJsonSec, double executeSec,
		// double gsonFromJsonSec, double readOneBike, String comment, double totalSizeDataMb) {

		/*pm.setDateTime(null);
		pm.setTotalTimeSec(2.433333333);
		pm.setPerceivedTimeAvailableBikesSec(3.4);
		pm.setDbProcedureSec(4.4);
		pm.setReadFromDbJdbcSec(5.4);
		pm.setGsonToJsonSec(6.4);
		pm.setExecuteSec(7.4);
		pm.setGsonFromJsonSec(8.4);
		pm.setComment("Testar");
		pm.setTotalSizeDataMb(10.4);

		double d = 28786.079999999998;
		String str = String.format("%1.2f", d);
		Year year = Year.now();

		int j = 37889;
		int k = 37889 +700;

		for(int i = j; i<k; i++ ) {
			deletBikes(i);
		}

		//AccessUser.UpdateUser("DBUser", "DBUser", 10,"dbuser@hotmail.com",07000000,"DBUser", "Female","DBUser");
		//AccessUser.insertNewUser("DBUser", "DBUser", 10,year,"dbuser@hotmail.com",07000000,"DBUser", "Annat","DBUser");
		//AccessBike.selectAvailableBikes();
		//AcccesPrestandaMesaurment.insertMesaurment(pm);

		/*double bd = new BigDecimal(1.490514650355E12).setScale(4, RoundingMode.HALF_UP).doubleValue();
		String ds = Double.toString(bd);
		ds = ds.replace("E","");
		bd = new BigDecimal(ds).setScale(4, RoundingMode.HALF_UP).doubleValue();


		/*d = Double.valueOf(str);
		System.out.println(d);
		//AcccesPrestandaMesaurment.insertMesaurment(pm);
		//AccessBike.executeBikeLoan(21,19);

	/*RestRoot restRoot = new RestRoot();
		BikeUser user = new BikeUser();
		user.setUserName("Demo123");
		user.setPassw("Demo123");
		Gson g = new Gson();
		long millisStart = Calendar.getInstance().getTimeInMillis();
		System.out.println("start " + millisStart);
		AccessBike.selectAvailableBikes();
		long millisStop = Calendar.getInstance().getTimeInMillis();
		System.out.println("läsa från databas available bikes " + (millisStop-millisStart));*/
		//restRoot.loginBikeUser(g.toJson(user));
		//String json = "{"sessionToken":"9tact15mmvehr8t6g0mhblh580","userID":19}"
		/*BikeUser user = new BikeUser();
		user.setUserID(19);
		user.setSessionToken("9tact15mmvehr8t6g0mhblh580");
		Gson g = new Gson();
		String s = g.toJson(user);
		long millisStart = Calendar.getInstance().getTimeInMillis();
		restRoot.getAvailableBikes(s);
		long millisStop = Calendar.getInstance().getTimeInMillis();
		System.out.println("Tidsåtgång: " + (millisStop - millisStart));
		//AccessUser.UpdateUser("golo","golo",10,"gologologolo@golo.com",400,"Ulrika", "Golo");
	//	Bike b = AccessBike.getBikeByID(17);
		//stream = b.getImageStream();

	/*	for(int i = 0; i < 50; i++) {
			addBikesChild();
		}*/

	/*	for(int i = 37887; i < 37887+2; i++){
			deletBikes(i);
			/*
			27256
			18756

			18741  st = 69223 millisekunder

		}*/
		//System.out.println("Tidsåtgång: " + (millisStop - millisStart) + " millisekunder" );
		/*
		17729st
		18730
		28731st
		1000st = 7421 millisec
		10 000st = 48813 millisec

		10 000 = 87347 millisekunder med korrekt bild
		com.mysql.jdbc.exceptions.jdbc4.CommunicationsException: The driver was unable to create a connection due to an inability to establish the client portion of a socket.

This is usually caused by a limit on the number of sockets imposed by the operating system. This limit is usually configurable.

For Unix-based platforms, see the manual page for the 'ulimit' command. Kernel or system reconfiguration may also be required.
		 */

	}


	public static String generateString(Random rng, String characters, int length)
	{
		char[] text = new char[length];
		for (int i = 0; i < length; i++)
		{
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		}
		return new String(text);
	}

	public static void addBikesChild(){
		Random random = new Random();
		int randomNumber = random.nextInt(6 - 1) + 1;
		Bike bike = new Bike();
		bike.setBrandName("Yosemite");
		bike.setType("Barn_sport");
		bike.setModelYear(2012);
		bike.setSize(20);
		bike.setState(randomNumber);
		bike.setColor("Grå");
		stream.reset();
		bike.setImageStream(stream);
		AccessBike.insertNewBike(bike);
	}

	public static void deletBikes(int i){
		AccessBike.deleteBike(i);
	}
public void genarteYear() {

}
	public static int randBetween(int start, int end) {
		return start + (int)Math.round(Math.random() * (end - start));
	}
public static void  rentBIkes() {

	Bikes bikes = AccessBike.selectAvailableBikes();
	ArrayList<Bike> list = bikes.getBikes();
	System.out.println(list.size());
	ArrayList<Integer> idList = new ArrayList<>();
	ArrayList<Bike> remove = new ArrayList<>();
	for(int i = 38592; i < 38890; i++){
		idList.add(i);
	}
	for (int i = 0; i < list.size(); i++){
		if(idList.contains(list.get(i).getBikeID())){
			remove.add(list.get(i));
		}
	}

	list.removeAll(remove);
		/*long minDay = LocalDate.of(1945, 1, 1).toEpochDay();
		long maxDay = LocalDate.of(2017, 05, 07).toEpochDay();
		long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
		LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
		System.out.println(randomDate);*/
	System.out.println(list.size() + " liste efter " + list.get(0).getColor());
	for (int i = 0; i < 1; i++) {
		Random rand = new Random();
		int j = rand.nextInt(105);
		System.out.println(j + 43);

		int k = rand.nextInt(list.size());
		System.out.println(list.get(k).getBikeID() + " " + j);
		System.out.println(AccessBike.executeBikeLoan(list.get(k).getBikeID(), j + 43));

	}
}

}
