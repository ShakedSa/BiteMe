package JDBC;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import Config.ReadPropertyFile;
import Entities.BranchManager;
import Entities.CEO;
import Entities.Customer;
import Entities.EmployerHR;
import Entities.Product;
import Entities.ServerResponse;
import Entities.Supplier;
import Entities.User;
import Entities.W4CCard;
import Enums.BranchName;
import Enums.Status;
import Enums.UserType;

/**
 * MySQL Connection class. Using a single connector to the db.
 * 
 * @author Aviel Malayev
 * @author Natali Krief
 * @author Michael Ben Israel
 * @author Eden Ben Abu
 * @author Shaked Sabag
 * @version November 2021 (1.0)
 */
public class mysqlConnection {

	/**
	 * Default arguments for connection received from config file.
	 */
	public static String arg0 = ReadPropertyFile.getInstance().getProp("jdbcScheme");
	public static String arg1 = ReadPropertyFile.getInstance().getProp("jdbcId");
	public static String arg2 = ReadPropertyFile.getInstance().getProp("jdbcPass");
	private static Connection conn;

	/**
	 * Set connection
	 * 
	 * @param args
	 */
	public static void setConnection(String[] args) {
		arg0 = args[0];
		arg1 = args[1];
		arg2 = args[2];
		try {
			if (conn == null) {
				conn = DriverManager.getConnection(arg0, arg1, arg2);
			}
		} catch (SQLException ex) {
			System.out.println("SQLExcetion: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	/**
	 * Login query, checking for matching tuples of userName and password. returns
	 * null object if the user is not found.
	 * 
	 * @param String userName
	 * @param String password
	 * 
	 * @return ServerResponse serverResponse
	 */
	public static ServerResponse login(String userName, String password) {
		ServerResponse serverResponse = new ServerResponse("User");
		PreparedStatement stmt;
		User user = null;
		try {
			String query = "SELECT * FROM bitemedb.users WHERE UserName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, userName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt(12) == 1) {
					serverResponse.setMsg("Already Logged In");
					serverResponse.setServerResponse(null);
					return serverResponse;
				}
				if(!rs.getString(2).equals(password)) {
					serverResponse.setMsg("Wrong Password");
					serverResponse.setServerResponse(null);
					return serverResponse;
				}
				if(rs.getString(8).equals("User")) {
					serverResponse.setMsg("Not Authorized");
					serverResponse.setServerResponse(null);
					return serverResponse;
				}
				String firstName = rs.getString(3);
				String lastName = rs.getString(4);
				String id = rs.getString(5);
				String email = rs.getString(6);
				String phoneNumber = rs.getString(7);
				UserType userType = UserType.valueOf(rs.getString(8));
				String role = rs.getString(9);
				String organization = rs.getString(10);
				BranchName branch = BranchName.valueOf(rs.getString(11));
				File avatar = null;
				Status status = Status.valueOf(rs.getString(13));
				/** If the user is customer or business customer get his w4c card info. */
				switch (userType) {
				case Customer:
				case BusinessCustomer:
					query = "SELECT * FROM bitemedb.customers WHERE UserName = ?";
					stmt = conn.prepareStatement(query);
					stmt.setString(1, userName);
					rs = stmt.executeQuery();
					int cusID = 0;
					float refBalance = 0;
					if (rs.next()) {
						cusID = rs.getInt(1);
						refBalance = rs.getFloat(3);
					}
					W4CCard w4cCard = getW4CCard(cusID);
					user = new Customer(userName, password, firstName, lastName, id, email, phoneNumber, userType,
							organization, branch, role, status, avatar, w4cCard, refBalance);
					stmt.close();
					break;
				case Supplier:
					/** get restaurant's info of the supplier */
					query = "SELECT * FROM bitemedb.suppliers WHERE UserName = ?";
					stmt = conn.prepareStatement(query);
					stmt.setString(1, userName);
					rs = stmt.executeQuery();
					String restaurantName = "";
					int monthlyComission = 12;
					if (rs.next()) {
						restaurantName = rs.getString(1);
						monthlyComission = rs.getInt(3);
					}
					ArrayList<Product> menu = getMenu(restaurantName);
					user = new Supplier(userName, password, firstName, lastName, id, email, phoneNumber, userType,
							organization, branch, role, status, avatar, restaurantName, menu, monthlyComission, branch);
					stmt.close();
					break;
				case BranchManager:
					user = new BranchManager(userName, password, firstName, lastName, id, email, phoneNumber, userType,
							organization, branch, role, status, avatar);
					break;
				case CEO:
					user = new CEO(userName, password, firstName, lastName, id, email, phoneNumber, userType,
							organization, branch, role, status, avatar);
					break;
				case EmployerHR:
					query = "SELECT * FROM bitemedb.employers_hr WHERE UserName = ?";
					stmt = conn.prepareStatement(query);
					stmt.setString(1, userName);
					rs = stmt.executeQuery();
					String employerID = "";
					if (rs.next()) {
						employerID = rs.getString(2);
					}
					user = new EmployerHR(userName, password, firstName, lastName, id, email, phoneNumber, userType,
							organization, branch, role, status, avatar, employerID);
					stmt.close();
					break;
				}
				/** Updating the user logged in status */
				if (!updateIsLoggedIn(userName, 1)) {
					serverResponse.setMsg("Internal Error");
					serverResponse.setServerResponse(null);
					return serverResponse;
				}
			}
			else {
					serverResponse.setMsg("not found");
					serverResponse.setServerResponse(null);
					return serverResponse;
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		serverResponse.setMsg("Success");
		serverResponse.setServerResponse(user);
		return serverResponse;
	}

	/**
	 * Query to get all the restaurants in the db.
	 * 
	 * @return ServerResponse serverResponse
	 */
	public static ServerResponse getRestaurants() {
		ServerResponse serverResponse = new ServerResponse("Restaurants");
		PreparedStatement stmt;
		HashMap<String, File> names = new HashMap<>();
		try {
			String query = "SELECT RestaurantName, Image FROM bitemedb.suppliers";
			stmt = conn.prepareStatement(query);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				names.put(rs.getString(1), null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		serverResponse.setMsg("Success");
		serverResponse.setServerResponse(names);
		return serverResponse;
	}

	/**
	 * Query to get the w4ccard of a certain customer.
	 * 
	 * @param customerID
	 * 
	 * @return W4CCard
	 */
	private static W4CCard getW4CCard(int customerID) {
		try {
			String query = "SELECT * FROM bitemedb.w4ccards WHERE CustomerID = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, customerID);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int w4cID = rs.getInt(1);
				String employerID = rs.getString(3);
				String qrCode = rs.getString(4);
				String creditCardNumber = rs.getString(5);
				float monthlyBudget = rs.getFloat(6);
				float dailyBudget = rs.getFloat(7);
				float balance = rs.getFloat(8);
				return new W4CCard(w4cID, employerID, qrCode, creditCardNumber, monthlyBudget, balance, dailyBudget);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Query to update the user logged in status.
	 * 
	 * @param userName
	 * @param isLoggedIn
	 * 
	 * @return boolean
	 */
	private static boolean updateIsLoggedIn(String userName, int isLoggedIn) {
		try {
			String query = "UPDATE bitemedb.users SET IsLoggedIn = ? WHERE UserName = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, isLoggedIn);
			stmt.setString(2, userName);
			stmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Query to get all the products of a given restaurant.
	 * 
	 * @param restaurantName
	 * 
	 * @return ArrayList<Product>
	 */
	private static ArrayList<Product> getMenu(String restaurantName) {
		ArrayList<Product> menu = new ArrayList<>();
		try {
			String query = "SELECT * FROM bitemedb.products WHERE RestaurantName = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, restaurantName);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				menu.add(new Product(restaurantName, rs.getString(3), rs.getString(2), null, rs.getInt(4),
						rs.getString(5)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return menu;
	}

	/**
	 * Logout query, setting user's IsLoggedIn field to 0 in the db indicates that
	 * he can login again.
	 * 
	 * @param String userName
	 * 
	 * @return String msg
	 */
	public static String logout(String userName) {
		if (!updateIsLoggedIn(userName, 0)) {
			return "Failed to logout";
		}
		return "Logout successful";
	}

	/**
	 * Logout query, setting all users' IsLoggedIn field to 0 in the db. Happens on
	 * server starts.
	 */
	public static void logoutAll() {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT UserName FROM bitemedb.users");
			while (rs.next()) {
				String user = rs.getString(1);
				logout(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Getting the 6 favourite restaurants from the DB to display on the main page.
	 * The restaurants are order by their name.
	 * 
	 * @return ServerResponse
	 */
	public static ServerResponse favRestaurants() {
		ServerResponse serverResponse = new ServerResponse("FavRestaurants");
		Statement stmt;
		HashMap<String, File> favRestaurants = new HashMap<>();
		try {
			String query = "SELECT * FROM bitemedb.suppliers ORDER BY RestaurantName";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				favRestaurants.put(rs.getString(1), null);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		serverResponse.setMsg("Success");
		serverResponse.setServerResponse(favRestaurants);
		return serverResponse;
	}
}