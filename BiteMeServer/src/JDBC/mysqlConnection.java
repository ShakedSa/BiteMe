package JDBC;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class mysqlConnection {

	public static String arg0= Config.ReadPropertyFile.getInstance().getProp("jdbcScheme");
	public static String arg1= Config.ReadPropertyFile.getInstance().getProp("jdbcId");
	public static String arg2= Config.ReadPropertyFile.getInstance().getProp("jdbcPass");
	
	public static void main(String[] args) 
	{
		try 
		{
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            System.out.println("Driver definition succeed");
        } catch (Exception ex) {
        	/* handle the error*/
        	 System.out.println("Driver definition failed");
        	 }
        
        try 
        {
            Connection conn = DriverManager.getConnection(arg0,arg1,arg2);
           // Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/bm?serverTimezone=IST","root","1231233");
        	System.out.println("SQL connection succeed");
            printOrders(conn);
     	} catch (SQLException ex) 
     	    {/* handle any errors*/
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            }

   	}
	
	//this method stores the information of the orders table and display it to the user.
	public static void printOrders(Connection con)
	{
		Statement stmt;
		try 
		{
           // Connection con = DriverManager.getConnection(arg0,arg1,arg2);
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM order;");
	 		while(rs.next())
	 		{
				 // Print out the values
				 System.out.println(rs.getString(1)+"  " +rs.getString(2)+"  " +rs.getString(3)+"  " +rs.getString(4)+"  " +rs.getString(5)+"  " +rs.getString(6));
			} 
			rs.close();
		} catch (SQLException e) {e.printStackTrace();}
	}
	
	
	
	
	
}