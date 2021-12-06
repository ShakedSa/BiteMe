package JDBC;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Config.ReadPropertyFile;
import Entities.Branch;
import Entities.Customer;
import Entities.Status;
import Entities.User;
import Entities.UserType;
import Entities.W4CCard;

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
	 * @return User user
	 */
	public static User login(String userName, String password) {
		PreparedStatement stmt;
		User user = null;
		try {
			String query = "SELECT * FROM bitemedb.users WHERE UserName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, userName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt(12) == 1 || !rs.getString(2).equals(password) || rs.getString(8).equals("User")) {
					return user;
				}
				String firstName = rs.getString(3);
				String lastName = rs.getString(4);
				String id = rs.getString(5);
				String email = rs.getString(6);
				String phoneNumber = rs.getString(7);
				UserType userType = UserType.valueOf(rs.getString(8));
				String role = rs.getString(9);
				String organization = rs.getString(10);
				Branch branch = Branch.valueOf(rs.getString(11));
				File avatar = null;
				Status status = Status.valueOf(rs.getString(13));
				int cusID = 0;
				float refBalance = 0;
				/** If the user is customer or business customer get his w4c card info. */
				if (userType == UserType.Customer || userType == UserType.BusinessCustomer) {
					query = "SELECT * FROM bitemedb.customers WHERE UserName = ?";
					stmt = conn.prepareStatement(query);
					stmt.setString(1, userName);
					rs = stmt.executeQuery();
					if (rs.next()) {
						cusID = rs.getInt(1);
						refBalance = rs.getFloat(3);
					}
					W4CCard w4cCard = getW4CCard(cusID);
					user = new Customer(userName, password, firstName, lastName, id, email, phoneNumber, userType,
							organization, branch, role, status, avatar, w4cCard, refBalance);
				}
				/** Updating the user logged in status */
				if (!updateIsLoggedIn(userName, 1)) {
					return user;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
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
	/*---------------------------------------------------*/
	// Prototype
//	/**
//	 * Method stores the order table information from the db and send it back to the
//	 * user
//	 * 
//	 * @return strResult
//	 */
//	public static String dispalyOrder() {
//		Statement stmt;
//		StringBuilder strResult = new StringBuilder();
//		try {
//			stmt = conn.createStatement();
//			ResultSet rs = stmt.executeQuery("SELECT * FROM bm.order;");
//			while (rs.next()) {
//				strResult.append(rs.getString(1) + "_" + rs.getString(2) + "_" + rs.getString(3) + "_" + rs.getString(4)
//						+ "_" + rs.getString(5) + "_" + rs.getString(6));
//				strResult.append("_");
//			}
//			rs.close();
//			return strResult.toString(); 
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return "Fetch failed";
//	}
//
//	/**
//	 * Updating a single order by OrderNumber.
//	 * 
//	 * @param OrderNumber
//	 * @param OrderAddress
//	 * @param TypeOfOrder
//	 * @return string
//	 */
//	public static String updateOrderInfo(String OrderNumber, String OrderAddress, String TypeOfOrder) {
//		PreparedStatement stmt;
//		try {
//			/**
//			 * update order address query:
//			 */
//			String sql = "UPDATE bm.order SET OrderAddress=?, TypeOfOrder=? WHERE OrderNumber=?";
//			stmt = conn.prepareStatement(sql);
//			stmt.setString(1, OrderAddress);
//			stmt.setString(2, TypeOfOrder);
//			stmt.setString(3, OrderNumber);
//			stmt.executeUpdate();
//			stmt.close();
//			return "Update success";
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return "Update failed";
//	}
//
//	/**
//	 * Getting information on a single order from the db and send it to the user.
//	 * 
//	 * @param orderNumber
//	 * @return strResult
//	 */
//	public static String getOrderInfo(String orderNumber) {
//		PreparedStatement stmt;
//		try {
//			String sql = "SELECT * FROM bm.order WHERE OrderNumber = ?";
//			stmt = conn.prepareStatement(sql);
//			stmt.setString(1, orderNumber);
//			ResultSet rs = stmt.executeQuery();
//			String strResult;
//			/**
//			 * if the query didn't return 1 line return an empty string.
//			 */
//			if (rs.next()) {
//				strResult = rs.getString(1) + "_" + rs.getString(2) + "_" + rs.getString(3) + "_" + rs.getString(4)
//						+ "_" + rs.getString(5) + "_" + rs.getString(6);
//			} else {
//				strResult = "";
//			}
//			return strResult;
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return "Fetch fail";
//	}
}