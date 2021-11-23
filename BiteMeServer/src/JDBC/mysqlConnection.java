package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import types.*;

public class mysqlConnection {

	//public static String arg0 = Config.ReadPropertyFile.getInstance().getProp("jdbcScheme");
	//public static String arg1 = Config.ReadPropertyFile.getInstance().getProp("jdbcId");
	//public static String arg2 = Config.ReadPropertyFile.getInstance().getProp("jdbcPass");
	public static String arg0 = "jdbc:mysql://localhost/bm?serverTimezone=IST";
	public static String arg1 = "root";
	public static String arg2 = "1231233";

	public static void main(String[] args) {
		System.out.println("here");
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
			System.out.println("Driver definition succeed");
		} catch (Exception ex) {
			/* handle the error */
			System.out.println("Driver definition failed");
		}

		try {
			Connection conn = DriverManager.getConnection(arg0, arg1, arg2);
			// Connection conn =
			// DriverManager.getConnection("jdbc:mysql://localhost/bm?serverTimezone=IST","root","1231233");
			System.out.println("SQL connection succeed");
			printOrders();
		} catch (SQLException ex) {/* handle any errors */
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void setConnection(String[] args) {
		arg0=args[0];
		arg1=args[1];
		arg2=args[2];
	}
	
	// this method stores the information of the orders table and display it to the
	// user.
	public static String printOrders() {
		Statement stmt;
		StringBuilder str = new StringBuilder();
		try {
			Connection con = DriverManager.getConnection(arg0, arg1, arg2);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM bm.order;");
			while (rs.next()) {
				// Print out the values
				str.append(rs.getString(1) + "_" + rs.getString(2) + "_" + rs.getString(3) + "_" + rs.getString(4)
						+ "_" + rs.getString(5) + "_" + rs.getString(6));
				str.append('_');
			}
			rs.close();
			return str.toString();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// update order address+ type of order
	public static String updateOrderInfo(String OrderNumber, String OrderAddress, String TypeOfOrder) {
		PreparedStatement stmt;
		try {
			Connection con = DriverManager.getConnection(arg0, arg1, arg2);
			// update order address query:
			String sql = "UPDATE bm.order SET OrderAddress=?, TypeOfOrder=? WHERE OrderNumber=?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, OrderAddress);
			stmt.setString(2, TypeOfOrder);
			stmt.setString(3, OrderNumber);
			stmt.executeUpdate();
			stmt.close();
			System.out.println("update finished\n");
			return "Update success";
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("update failed\n");
		return "Update fail";
	}
	
	public static String getOrderInfo(String orderNumber) {
		PreparedStatement stmt;
		try {
			Connection con = DriverManager.getConnection(arg0, arg1, arg2);
			String sql = "SELECT * FROM bm.order WHERE OrderNumber = ?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, orderNumber);
			ResultSet rs = stmt.executeQuery();
			String result;
			if(rs.next()) {
				result = rs.getString(1) + "_" + rs.getString(2) + "_" + rs.getString(3) + "_" + rs.getString(4)
				+ "_" + rs.getString(5) + "_" + rs.getString(6);
			}else {
				result = "";
			}
			return result;
		}catch(SQLException e) {
			e.printStackTrace();
		}
		System.out.println("fetch failed\n");
		return "Fetch fail";
	}
}