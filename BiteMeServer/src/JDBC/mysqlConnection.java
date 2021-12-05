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
				System.out.println("DId the first query :)");
				String firstName = rs.getString(3);
				String lastName = rs.getString(4);
				String id = rs.getString(5);
				String email = rs.getString(6);
				String phoneNumber = rs.getString(7);
				UserType userType = UserType.valueOf(rs.getString(8));
				String role = rs.getString(9);
				String organization = rs.getString(10);
				Branch branch = Branch.valueOf(rs.getString(11));
				File avatar=null;
				Status status = Status.valueOf(rs.getString(13));				
				int cusID = 0, w4cID=0;
				float refBalance=0, monthlyBudget=0, balance=0, dailyBudget=0;
				String employerID="", qrCode="", creditCardNumber="";
				query = "UPDATE bitemedb.users SET IsLoggedIn = 1 WHERE UserName = ?";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, userName);
				stmt.executeUpdate();
				System.out.println("Updated user logged in");
				if (userType == UserType.Customer || userType == UserType.BusinessCustomer) {
					System.out.println("user is of type customer or business customer");
					query = "SELECT * FROM bitemedb.customers WHERE UserName = ?";
					stmt = conn.prepareStatement(query);
					stmt.setString(1, userName);
					rs = stmt.executeQuery();
					if (rs.next()) {
						cusID = rs.getInt(1);
						refBalance = rs.getFloat(3);
					}
					System.out.println("got customer's info");
					query = "SELECT * FROM bitemedb.w4ccards WHERE CustomerID = ?";
					stmt = conn.prepareStatement(query);
					stmt.setInt(1, cusID);
					rs = stmt.executeQuery();
					if (rs.next()) {
						w4cID = rs.getInt(1);
						employerID = rs.getString(3);
						qrCode = rs.getString(4);
						creditCardNumber = rs.getString(5);
						monthlyBudget = rs.getFloat(6);
						dailyBudget = rs.getFloat(7);
						balance = rs.getFloat(8);
					}
					user = new Customer(userName, password, firstName, lastName, id, email, phoneNumber, userType,
							organization, branch, role, status, avatar, new W4CCard(w4cID, employerID, qrCode,
									creditCardNumber, monthlyBudget, balance, dailyBudget),
							refBalance);
					System.out.println(user);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return user;
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
		PreparedStatement stmt;
		try {
			String query = "UPDATE bitemedb.users SET IsLoggedIn = 0 WHERE UserName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, userName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
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