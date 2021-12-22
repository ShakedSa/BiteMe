package JDBC;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Spliterator;
import java.util.concurrent.TimeUnit;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import Config.ReadPropertyFile;
import Entities.BranchManager;
import Entities.BusinessCustomer;
import Entities.CEO;
import Entities.Component;
import Entities.Customer;
import Entities.Delivery;
import Entities.EmployerHR;
import Entities.ImportedUser;
import Entities.NewSupplier;
import Entities.NewUser;
import Entities.Order;
import Entities.OrderDeliveryMethod;
import Entities.PreorderDelivery;
import Entities.Product;
import Entities.ServerResponse;
import Entities.SharedDelivery;
import Entities.Supplier;
import Entities.User;
import Entities.W4CCard;
import Enums.BranchName;
import Enums.Doneness;
import Enums.PaymentMethod;
import Enums.Size;
import Enums.Status;
import Enums.TypeOfOrder;
import Enums.TypeOfProduct;
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
				if (!rs.getString(2).equals(password)) {
					serverResponse.setMsg("Wrong Password");
					serverResponse.setServerResponse(null);
					return serverResponse;
				}
				if (rs.getString(8).equals("User")) {
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
					query = "SELECT * FROM bitemedb.customers WHERE UserName = ?";
					stmt = conn.prepareStatement(query);
					stmt.setString(1, userName);
					rs = stmt.executeQuery();
					int cusID = 0;
					if (rs.next()) {
						cusID = rs.getInt(1);
					}
					W4CCard w4cCard = getW4CCard(cusID);
					HashMap<String, Float> refunds = getRefund(cusID);
					user = new Customer(userName, password, firstName, lastName, id, email, phoneNumber, userType,
							organization, branch, role, status, avatar, w4cCard, refunds);
					stmt.close();
					break;
				case Supplier:
					/** get restaurant's info of the supplier */
					query = "SELECT * FROM bitemedb.suppliers WHERE UserName = ?";
					stmt = conn.prepareStatement(query);
					stmt.setString(1, userName);
					rs = stmt.executeQuery();
					String restaurantName = "";
					String restaurantAddress = "";
					Float monthlyComission = 12f;
					ArrayList<Product> menu = null;
					if (rs.next()) {
						restaurantName = rs.getString(1);
						restaurantAddress = rs.getString(2); // added RestaurantAddress to supplier in DB - aviel
						monthlyComission = rs.getFloat(4);
						menu = getMenu(restaurantName);
					}
					user = new Supplier(userName, password, firstName, lastName, id, email, phoneNumber, userType,
							organization, branch, role, status, avatar, restaurantName, menu, monthlyComission,
							restaurantAddress); // change last input from branch to restaurantAddress - aviel
					stmt.close();
					rs.close();
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
					query = "SELECT * FROM bitemedb.businesscustomer_hr WHERE UserName = ?";
					stmt = conn.prepareStatement(query);
					stmt.setString(1, userName);
					rs = stmt.executeQuery();
					String employerCode = "";
					if (rs.next()) {
						employerCode = rs.getString(2);
					}
					user = new EmployerHR(userName, password, firstName, lastName, id, email, phoneNumber, userType,
							organization, branch, role, status, avatar, employerCode);
					stmt.close();
					break;
				}
				/** Updating the user logged in status */
				if (!updateIsLoggedIn(userName, 1)) {
					serverResponse.setMsg("Internal Error");
					serverResponse.setServerResponse(null);
					return serverResponse;
				}
			} else {
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
	 * Query to update a user information in the db.
	 * 
	 * @return ServerResponse serverResponse
	 */
	public static void updateUserInformation(String userName, String userType, String status) {
//		ServerResponse serverResponse = new ServerResponse("updateUser");
		PreparedStatement stmt;
		try {
			String query = "UPDATE bitemedb.users SET UserType = ?, Status = ? WHERE UserName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, userType);
			stmt.setString(2, status);
			stmt.setString(3, userName);
//			ResultSet rs = stmt.executeQuery();
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("no man");
		}
		return;
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
				String employerCode = rs.getString(3);
				String qrCode = rs.getString(4);
				String creditCardNumber = rs.getString(5);
				float monthlyBudget = rs.getFloat(6);
				float dailyBudget = rs.getFloat(7);
				float balance = rs.getFloat(8);
				float dailyBalance = rs.getFloat(9);
				return new W4CCard(w4cID, employerCode, qrCode, creditCardNumber, monthlyBudget, balance, dailyBudget,
						dailyBalance);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static HashMap<String, Float> getRefund(int CustomerID) {
		PreparedStatement stmt;
		try {
			String query = "SELECT RestaurantName, Refund FROM bitemedb.refunds WHERE CustomerID = ?";
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, CustomerID);
			ResultSet rs = stmt.executeQuery();
			HashMap<String, Float> refunds = new HashMap<>();
			while (rs.next()) {
				refunds.put(rs.getString(1), rs.getFloat(2));
			}
			return refunds;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Query to add a new supplier the db.
	 * 
	 * @return ServerResponse serverResponse
	 */
	public static void addNewSupplier(NewSupplier supplier) {
		PreparedStatement stmt = null;
		int monthlyCommision = Character.getNumericValue(supplier.getMonthlyCommision().charAt(0));
		try {
			String query = "INSERT INTO bitemedb.suppliers"
					+ "(RestaurantName, RestaurantAddress, UserName, MonthlyComission,"
					+ " Image, RestaurantType, branch)" + "VALUES(?, ?, ?, ?, ?, ?, ?)";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, supplier.getResturantName());
			stmt.setString(2, supplier.getResturantAddress());
			stmt.setString(3, supplier.getUserName());
			stmt.setInt(4, monthlyCommision);
			stmt.setBlob(5, supplier.getImagUpload());
			stmt.setString(6, supplier.getResturantType());
			stmt.setString(7, supplier.getBranchName().name());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("no man");
		}
		return;
	}

	/**
	 * Query to add a new supplier the db.
	 * 
	 * @return ServerResponse serverResponse
	 */
	public static void addNewUser(NewUser user) {
		PreparedStatement stmt = null;
		try {
			String query = "INSERT INTO bitemedb.users"
					+ "(UserName, Password, FirstName, LastName, ID, Email, PhoneNumber,"
					+ " UserType, Role, Organization, MainBranch, IsLoggedIn, Status, Avatar)"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, user.getUserName());
			stmt.setString(2, user.getPassword());
			stmt.setString(3, user.getFirstName());
			stmt.setString(4, user.getLastName());
			stmt.setString(5, user.getId());
			stmt.setString(6, user.getEmail());
			stmt.setString(7, user.getPhoneNumber());
			stmt.setString(8, user.getUserType().name());
			stmt.setString(9, user.getRole());
			stmt.setString(10, user.getOrganization());
			stmt.setString(11, user.getMainBranch().name());
			stmt.setInt(12, user.getIsLoggedIn());
			stmt.setString(13, user.getStatus().name());
			stmt.setBlob(14, user.getAvatar());
			// ResultSet rs = stmt.executeQuery();
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("no man");
		}
		return;
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
				TypeOfProduct type = TypeOfProduct.getEnum(rs.getString(3));
				ArrayList<Component> components = (ArrayList<Component>)(getComponentsInProduct(restaurantName, rs.getString(2))).getServerResponse();
				menu.add(new Product(restaurantName, type, rs.getString(2), components, rs.getFloat(4), rs.getString(5)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return menu;
	}

	/**
	 * Getting menu of a specific restaurant for orders.
	 * 
	 * @param restaurantName
	 */
	public static ServerResponse getMenuToOrder(String restaurantName) {
		ServerResponse serverResponse = new ServerResponse("menu");
		ArrayList<Product> menu = getMenu(restaurantName);
		if (menu == null || menu.size() == 0) {
			serverResponse.setMsg("Failed to get menu");
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		serverResponse.setMsg("Success");
		serverResponse.setServerResponse(menu);
		return serverResponse;
	}

	/**
	 * Query to get all optional components for a certain dish.
	 * 
	 * @param restaurantName
	 * @param productName
	 */
	public static ServerResponse getComponentsInProduct(String restaurantName, String productName) {
		ServerResponse serverResponse = new ServerResponse("optionalComponents");
		ArrayList<Component> components = new ArrayList<>();
		PreparedStatement stmt;
		try {
			String query = "SELECT component FROM bitemedb.components WHERE RestaurantName = ? AND dishName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, restaurantName);
			stmt.setString(2, productName);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				switch (rs.getString(1).toLowerCase()) {
				case "doneness":
					components.add(new Component(Doneness.medium));
					break;
				case "size":
					components.add(new Component(Size.Medium));
					break;
				default:
					components.add(new Component(rs.getString(1)));
				}
			}
			serverResponse.setMsg("Success");
			serverResponse.setServerResponse(components);
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg("Failed to get components");
			serverResponse.setServerResponse(null);
		}
		return serverResponse;
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

	/**
	 * Check if the order number is exist or not
	 * 
	 * @param orderNumber
	 * @return ServerResponse
	 */
	public static ServerResponse searchOrder(String orderNumber) {
		ServerResponse serverResponse = new ServerResponse("String");
		try {
			String query1 = "SELECT * FROM bitemedb.orders WHERE OrderNumber = " + orderNumber;
			String query = "SELECT * FROM bitemedb.orders WHERE OrderNumber = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, Integer.parseInt(orderNumber));
			ResultSet rs = stmt.executeQuery(query1);

			if (!rs.next()) {
//				ResultSet rs1 = stmt.executeQuery(query);
				serverResponse.setMsg("Order number doesn't exist");
				serverResponse.setServerResponse(null);
				return serverResponse;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		serverResponse.setMsg("Success");
		return serverResponse;
	}

	/**
	 * Check if a user name number is exist in the db or not
	 * 
	 * @param orderNumber
	 * @return ServerResponse
	 */

	private static boolean checkIfBusinessCustomerExist(String hrUserName, String employerCompanyName) {

		try {
			String query = "SELECT * FROM bitemedb.businesscustomer WHERE HRname = ? AND EmployeCompanyName = ?";

			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, hrUserName);
			stmt.setString(2, employerCompanyName);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next())
				return false;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;

		}
		return true;
	}

	/**
	 * creating new business customer
	 * 
	 * @param hrUserName,employerCode,employerCompanyName
	 * 
	 * @return ServerResponse
	 */
	public static ServerResponse createNewBusinessCustomer(String hrUserName, String employerCode,
			String employerCompanyName) {
		ServerResponse serverResponse = new ServerResponse("String");
		if (checkIfBusinessCustomerExist(hrUserName, employerCompanyName)) {
			serverResponse.setMsg("Already Registered");
			return serverResponse;
		}

		try {
			String query = "INSERT INTO bitemedb.businesscustomer (EmployerCode, EmployeCompanyName, HRname) values (?, ?, ?)";

			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, employerCode);
			stmt.setString(2, employerCompanyName);
			stmt.setString(3, hrUserName);
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
		}

		serverResponse.setMsg("Success");
		return serverResponse;
	}

	/**
	 * 
	 * updating customer to approved in DB and returning updated list of not
	 * approved customers
	 * 
	 * @param hrUserName,employerCompanyName
	 * 
	 * @return ServerResponse
	 */
	public static ServerResponse approveCustomerAsBusiness(String employerCompanyName, String customerId) {
		ServerResponse serverResponse = new ServerResponse("ArrayList");

		try {
			String query = "UPDATE bitemedb.customers SET IsApprovedByHR = 1 WHERE "
					+ "customers.UserName = (select Username from bitemedb.users where users.ID = ? and users.UserType = \"Customer\")";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, customerId);
			stmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg("Update Failed");
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		return selectCustomerAndbudget(employerCompanyName);
	}

	/**
	 * selecting info of all Customers related to the HR that are not approved yet
	 * 
	 * @param hrUserName,employerCompanyName
	 * 
	 * @return ServerResponse
	 */
	public static ServerResponse selectCustomerAndbudget(String employerCompanyName) {
		ServerResponse serverResponse = new ServerResponse("ArrayList");
		ArrayList<Customer> response = new ArrayList<>();

		try {
			String query = "SELECT ID, FirstName, LastName, Role, MonthlyBudget, DailyBudget FROM bitemedb.customers, bitemedb.users, bitemedb.w4ccards"
					+ " WHERE Organization = ? AND customers.IsApprovedByHR = 0 AND users.UserName = customers.UserName and customers.CustomerID = w4ccards.CustomerID";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, employerCompanyName);
			ResultSet rs = stmt.executeQuery();
			// building list of customers that belong to the organization
			// and are not approved yet by HR
			while (rs.next()) {
				Customer customer = new Customer();
				W4CCard w4c = new W4CCard();
				customer.setId(rs.getString(1));
				customer.setFirstName(rs.getString(2));
				customer.setLastName(rs.getString(3));
				customer.setRole(rs.getString(4));
				w4c.setMonthlyBudget(rs.getFloat(5));
				w4c.setDailyBudget(rs.getFloat(6));
				customer.setW4c(w4c);
				response.add(customer);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		serverResponse.setServerResponse(response);
		serverResponse.setMsg("Success");
		return serverResponse;
	}

	public static ServerResponse checkUsername(String username) {
		ServerResponse serverResponse = new ServerResponse("ArrayList");
		ArrayList<String> response = new ArrayList<>();
		try {
			PreparedStatement stmt;
			String query = "SELECT * FROM bitemedb.users WHERE UserName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) { // 8 usertype, 13 status
				response.add(rs.getString(8));
				response.add(rs.getString(13));
			} else {
				response.add("Error");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		serverResponse.setMsg("Success");
		serverResponse.setServerResponse(response);
		return serverResponse;
	}

	/**
	 * Check if a user name number is exist and has no type
	 * 
	 * @param orderNumber
	 * @return ServerResponse
	 */

	public static ServerResponse checkUserNameWithNoType(String username) {
		ServerResponse serverResponse = new ServerResponse("ArrayList");
		ArrayList<String> response = new ArrayList<>();
		try {
			PreparedStatement stmt;
			String query = "SELECT * FROM bitemedb.users WHERE UserName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				if (rs.getString(8).equals("")) {
					response.add(rs.getString(3));
					response.add(rs.getString(4));
				} else
					response.add("already has type");
			} else {
				response.add("Error");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		serverResponse.setMsg("Success");
		serverResponse.setServerResponse(response);
		return serverResponse;
	}

	// java.sql.SQLIntegrityConstraintViolationException:
	// Cannot add or update a child row: a foreign key constraint fails
	// (`bitemedb`.`reports`, CONSTRAINT `RestaurantNameFK10` FOREIGN KEY
	// (`RestaurantName`)
	// REFERENCES `suppliers` (`RestaurantName`))

	public static void updateFile(InputStream is, String date) {
		System.out.println("test !");
		String filename = "Report " + date + ".pdf";
		String sql = "INSERT INTO reports (ReportID,Title,Date,content,BranchName,ReportType,RestaurantName) values(?, ?, ?, ?, ?, ?, ?)";
	}

	/**
	 * @param is   File inputstream to upload as a blob
	 * @param date - report date
	 * @param desc - contains info about the report as string arrayList
	 */
	public static void updateFile(InputStream is, String date, ArrayList<String> desc) {
		String filename = "Report " + date + ".pdf";
		String sql = "INSERT INTO reports (Title,Date,content,BranchName,ReportType) values( ?, ?, ?, ?, ?)";

		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, filename);
			statement.setDate(2, Date.valueOf("" + desc.get(2) + "-" + desc.get(1) + "-01"));
			statement.setBlob(3, is);
			statement.setString(4, desc.get(3));
			statement.setString(5, desc.get(0));
			statement.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Inserting a new order to the db, should alert the supplier(restaurant) that a
	 * new order was placed for his restaurant.
	 * 
	 * @param orderToInsert
	 */
	public static ServerResponse insertOrderDelivery(OrderDeliveryMethod orderToInsert) {
		ServerResponse serverResponse = new ServerResponse("OrderNumber");
		int orderNewKey = 0, deliveryNewKey = 0;
		PreparedStatement stmt;
		try {
			orderNewKey = insertOrder(orderToInsert.getOrder());
			if (orderNewKey == -1) {
				serverResponse.setMsg("Failed to insert order");
				return serverResponse;
			}
			deliveryNewKey = insertDelivery(orderToInsert.getDelivery(), orderToInsert.getTypeOfOrder());
			if (deliveryNewKey == -1) {
				serverResponse.setMsg("Failed to insert delivery");
				return serverResponse;
			}
			String query = "INSERT INTO bitemedb.ordereddelivery (DeliveryNumber, OrderNumber, UserName, FinalPrice) VALUES(?,?,?,?)";
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, deliveryNewKey);
			stmt.setInt(2, orderNewKey);
			stmt.setString(3, orderToInsert.getCustomerInfo().getUserName());
			stmt.setFloat(4, orderToInsert.getFinalPrice());
			stmt.executeUpdate();
			stmt.close();
			/**
			 * If all insertions were good update w4c card of the customer if payment method
			 * is business
			 */
			if (orderToInsert.getOrder().getPaymentMethod() == PaymentMethod.Both
					|| orderToInsert.getOrder().getPaymentMethod() == PaymentMethod.BusinessCode) {
				updateW4C(orderToInsert);
			}
			updateRefund(orderToInsert.getCustomerInfo(), orderToInsert.getOrder().getRestaurantName());
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			return serverResponse;
		}
		serverResponse.setMsg("Success");
		serverResponse.setServerResponse(orderNewKey);
		return serverResponse;
	}

	/**
	 * Query to update refund of a customer.
	 * 
	 * @param customer
	 * @param RestaurantName
	 * 
	 */
	private static void updateRefund(Customer customer, String RestaurantName) {
		PreparedStatement stmt;
		try {
			int customerID = getCustomer(customer.getUserName());
			String query = "UPDATE bitemedb.refunds SET Refund = ? WHERE CustomerID = ? && RestaurantName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setFloat(1, customer.getRefunds().get(RestaurantName));
			stmt.setInt(2, customerID);
			stmt.setString(3, RestaurantName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Query to update customer's w4c balance.
	 * 
	 * @param orderToInsert
	 */
	private static void updateW4C(OrderDeliveryMethod orderToInsert) {
		PreparedStatement stmt;
		try {
			int customerID = getCustomer(orderToInsert.getCustomerInfo().getUserName());
			String query = "UPDATE bitemedb.w4ccards SET Balance = ?, DailyBalance = ? WHERE CustomerID = ?";
			stmt = conn.prepareStatement(query);
			stmt.setFloat(1, orderToInsert.getCustomerInfo().getW4c().getBalance());
			stmt.setFloat(2, orderToInsert.getCustomerInfo().getW4c().getDailyBalance());
			stmt.setInt(3, customerID);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Query to get customer's id.
	 * 
	 * @param userName
	 * 
	 * @return int
	 */
	private static int getCustomer(String userName) {
		PreparedStatement stmt;
		int customerID = 0;
		try {
			String query = "SELECT CustomerID FROM bitemedb.customers WHERE UserName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, userName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				customerID = rs.getInt(1);
			}
			if (customerID == 0) {
				throw new SQLException("failed to get customer info");
			}
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			return 0;
		}
		return customerID;
	}

	/**
	 * Private method for order inserting query.
	 * 
	 * @param order
	 * @return integer
	 */
	private static int insertOrder(Order order) {
		int orderNewKey = 0;
		PreparedStatement stmt;
		try {
			String query = "INSERT INTO bitemedb.orders (RestaurantName, OrderTime, OrderPrice, PaymentMethod) VALUES(?, ?, ?, ?)";
			String[] keys = { "OrderNumber" };
			stmt = conn.prepareStatement(query, keys);
			stmt.setString(1, order.getRestaurantName());
			stmt.setString(2, order.getDateTime());
			stmt.setFloat(3, order.getOrderPrice());
			stmt.setString(4, PaymentMethod.getEnum(order.getPaymentMethod()));
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				orderNewKey = rs.getInt(1);
			}
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		if (orderNewKey != 0) {
			insertProducts(order.getProducts(), orderNewKey);
			return orderNewKey;
		}
		return -1;
	}

	/**
	 * Private method to insert products in order.
	 * 
	 * @param products
	 * @param orderNumber
	 */
	private static void insertProducts(ArrayList<Product> products, int orderNumber) {
		PreparedStatement stmt;
		try {
			for (Product p : products) {
				String query = "INSERT INTO bitemedb.productinorder (OrderNumber, RestaurantName, DishName, Components) VALUES(?,?,?,?)";
				stmt = conn.prepareStatement(query);
				stmt.setInt(1, orderNumber);
				stmt.setString(2, p.getRestaurantName());
				stmt.setString(3, p.getDishName());
				stmt.setString(4, p.getComponents().toString());
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Private method for delivery inserting query.
	 * 
	 * @param delivery
	 * @param typeOfOrder
	 * @return integer
	 */
	private static int insertDelivery(Delivery delivery, TypeOfOrder typeOfOrder) {
		PreparedStatement stmt;
		int deliveryNewKey = 0;
		try {
			String query;
			String[] keys = { "DeliveryNumber" };
			String name = delivery.getFirstName() + " " + delivery.getLastName();
			switch (typeOfOrder) {
			case preorderDelivery:
				PreorderDelivery preorder = (PreorderDelivery) delivery;
				query = "INSERT INTO bitemedb.deliveries (OrderAddress, DeliveryType, Discount, PreOrderTime, DeliveryPhoneNumber, DeliveryReceiver) VALUES(?,?,?,?,?,?)";
				stmt = conn.prepareStatement(query, keys);
				stmt.setString(1, delivery.getOrderAddress());
				stmt.setString(2, typeOfOrder.toString());
				stmt.setFloat(3, delivery.getDiscount());
				stmt.setString(4, preorder.getDeliveryTime());
				stmt.setString(5, delivery.getPhoneNumber());
				stmt.setString(6, name);
			case sharedDelivery:
				SharedDelivery shared = (SharedDelivery) delivery;
				query = "INSERT INTO bitemedb.deliveries (OrderAddress, DeliveryType, Discount, AmountOfPeople, DeliveryPhoneNumber, DeliveryReceiver) VALUES (?,?,?,?,?,?)";
				stmt = conn.prepareStatement(query, keys);
				stmt.setString(1, delivery.getOrderAddress());
				stmt.setString(2, typeOfOrder.toString());
				stmt.setFloat(3, delivery.getDiscount());
				stmt.setInt(4, shared.getAmountOfPeople());
				stmt.setString(5, delivery.getPhoneNumber());
				stmt.setString(6, name);
			case takeaway:
				query = "INSERT INTO bitemedb.deliveries (DeliveryType, Discount, DeliveryPhoneNumber, DeliveryReceiver) VALUES (?,?,?,?)";
				stmt = conn.prepareStatement(query, keys);
				stmt.setString(1, typeOfOrder.toString());
				stmt.setFloat(2, delivery.getDiscount());
				stmt.setString(3, delivery.getPhoneNumber());
				stmt.setString(4, name);
			default:
				query = "INSERT INTO bitemedb.deliveries (OrderAddress, DeliveryType, Discount, DeliveryPhoneNumber, DeliveryReceiver) VALUES (?,?,?,?,?)";
				stmt = conn.prepareStatement(query, keys);
				stmt.setString(1, delivery.getOrderAddress());
				stmt.setString(2, typeOfOrder.toString());
				stmt.setFloat(3, delivery.getDiscount());
				stmt.setString(4, delivery.getPhoneNumber());
				stmt.setString(5, name);
			}
			stmt.executeUpdate();
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				deliveryNewKey = rs.getInt(1);
			}
			stmt.close();
			rs.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
		return deliveryNewKey == 0 ? -1 : deliveryNewKey;
	}

	/**
	 * 
	 * /* Check if a user name number is exist in the db or not
	 * 
	 * @param orderNumber
	 * @return ServerResponse
	 */

	public static ServerResponse checkID(String id) {
		ServerResponse serverResponse = new ServerResponse("ArrayList");
		ImportedUser response;
		try {
			PreparedStatement stmt;
			String query = "SELECT * FROM bitemedb.importsimulationuser WHERE ID = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, id);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				System.out.println(rs.getString(10));
				response = new ImportedUser(id, rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9),
						BranchName.valueOf(rs.getString(10)), rs.getBlob(11));
				System.out.println(response.getEmail());
			} else {
				response = null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		serverResponse.setMsg("Success");
		serverResponse.setServerResponse(response);
		return serverResponse;
	}

	/**
	 * Check if a user name number is exist in the db or not
	 * 
	 * @param orderNumber
	 * @return ServerResponse
	 */

	public static ServerResponse getEmployersForApproval() {
		ServerResponse serverResponse = new ServerResponse("ArrayList");
		ArrayList<BusinessCustomer> response = new ArrayList<>();
		try {
			PreparedStatement stmt;
			String query = "SELECT * FROM bitemedb.businesscustomer WHERE IsApproved = ?";
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, 0);
			ResultSet rs = stmt.executeQuery();

			System.out.println("11111");
			// save in response all employers that needs approval
			while (rs.next()) {
				response.add(new BusinessCustomer(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4)));
				System.out.println(response.get(0).getEmployeCompanyName());
			}
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		serverResponse.setMsg("Success");
		serverResponse.setServerResponse(response);
		return serverResponse;
	}

	/*
	 * Update order status and planned time/received time in order table
	 * 
	 * @param receivedOrReady
	 * 
	 * @param orderNumber
	 * 
	 * @param time
	 * 
	 * @param status
	 * 
	 * @return deliveryNumber
	 */
	public static ServerResponse updateOrderStatus(String receivedOrReady, String orderNumber, String time,
			String status) {
		ServerResponse serverResponse = new ServerResponse("Integer");
		System.out.println(receivedOrReady);
		try {
			if (receivedOrReady.equals("Order Received")) {
				String query = "UPDATE bitemedb.orders SET OrderStatus = ?, OrderReceived = ? WHERE OrderNumber = ?";
				// String query = "UPDATE bitemedb.orders SET OrderStatus = ? WHERE OrderNumber
				// = ?";
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setString(1, status);
				stmt.setString(2, time);
				stmt.setString(3, orderNumber);
				stmt.executeUpdate();
			} else { // Order Is Ready
				String query = "UPDATE bitemedb.orders SET OrderStatus = ?, PlannedTime = ? WHERE OrderNumber = ?";
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setString(1, status);
				stmt.setString(2, time);
				stmt.setString(3, orderNumber);
				stmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}

		// get deliveryNumber from orderedDelivery table
		int deliveryNumber = 0;
		try {
			String query = "SELECT DeliveryNumber FROM bitemedb.ordereddelivery WHERE OrderNumber = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, orderNumber);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				deliveryNumber = rs.getInt(1);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		serverResponse.setMsg("Success");
		serverResponse.setServerResponse(deliveryNumber);
		return serverResponse;
	}

	public static ServerResponse getOrderInfo(String orderNumber) {
		ServerResponse serverResponse = new ServerResponse("ArrayList");
		ArrayList<String> response = new ArrayList<>();
		try {
			PreparedStatement stmt;
			String query = "SELECT * FROM bitemedb.orders WHERE OrderNumber = ?";
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, Integer.parseInt(orderNumber));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				response.add(rs.getString(2)); // restaurant name
				response.add(rs.getString(4)); // received time
				response.add(rs.getString(5)); // planned time
				response.add(rs.getString(8)); // status
			} else {
				response.add("Error");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		serverResponse.setMsg("Success");
		serverResponse.setServerResponse(response);
		return serverResponse;
	}

	public static ServerResponse getCustomerInfo(String deliveryNumber) {
		ServerResponse serverResponse = new ServerResponse("ArrayList");
		ArrayList<String> response = new ArrayList<>();
		try {
			PreparedStatement stmt;
			String query = "SELECT * FROM bitemedb.deliveries WHERE DeliveryNumber = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, deliveryNumber);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				response.add(rs.getString(7)); // phone number
				response.add(rs.getString(8)); // customer name
			} else {
				response.add("Error");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		serverResponse.setMsg("Success");
		serverResponse.setServerResponse(response);
		return serverResponse;
	}

	public static ServerResponse addItemToMenu(Product product) {
		ServerResponse serverResponse = new ServerResponse("String");
		try {
			PreparedStatement stmt;
			String query = "INSERT INTO bitemedb.products (RestaurantName, DishName, Type, Price, ProductDescription) VALUES(?,?,?,?,?)";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, product.getRestaurantName());
			stmt.setString(2, product.getDishName());
			stmt.setString(3, product.getType().toString());
			stmt.setFloat(4, product.getPrice());
			stmt.setString(5, product.getDescription());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		
		//set components
		for(int i=0; i<product.getComponents().size();i++) {
			try {
				PreparedStatement stmt;
				String query = "INSERT INTO bitemedb.components (RestaurantName, DishName, component) VALUES(?,?,?)";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, product.getRestaurantName());
				stmt.setString(2, product.getDishName());
				stmt.setString(3, product.getComponents().get(i).toString());
				stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				serverResponse.setMsg(e.getMessage());
				serverResponse.setServerResponse(null);
				return serverResponse;
			}
		}
		serverResponse.setMsg("Success");
		return serverResponse;
	}
	
	public static ServerResponse editItemInMenu(Product product) {
		ServerResponse serverResponse = new ServerResponse("String");
		try {
			PreparedStatement stmt;//Type, Price, ProductDescription
			String query = "UPDATE bitemedb.products SET Type = ?, Price = ?, ProductDescription = ? WHERE RestaurantName = ? AND DishName = ?";
			stmt = conn.prepareStatement(query);
			System.out.println(product);
			stmt.setString(1, product.getType().toString());
			stmt.setFloat(2, product.getPrice());
			stmt.setString(3, product.getDescription());
			stmt.setString(4, product.getRestaurantName());
			stmt.setString(5, product.getDishName());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		
		System.out.println("update");
		
		//delete the old components
			try {
				PreparedStatement stmt;
				String query = "DELETE FROM bitemedb.components WHERE RestaurantName = ? AND DishName = ?";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, product.getRestaurantName());
				stmt.setString(2, product.getDishName());
				stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				serverResponse.setMsg(e.getMessage());
				serverResponse.setServerResponse(null);
				return serverResponse;
			}		
			
			System.out.println("delete");
		
		//set the new components
		for(int i=0; i<product.getComponents().size();i++) {
			try {
				PreparedStatement stmt;
				String query = "INSERT INTO bitemedb.components (RestaurantName, DishName, component) VALUES(?,?,?)";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, product.getRestaurantName());
				stmt.setString(2, product.getDishName());
				stmt.setString(3, product.getComponents().get(i).toString());
				stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				serverResponse.setMsg(e.getMessage());
				serverResponse.setServerResponse(null);
				return serverResponse;
			}
		}
		
		System.out.println("final update");
		
//		for(int i=0; i<product.getComponents().size();i++) {
//			try {
//				PreparedStatement stmt;
//				String query = "UPDATE bitemedb.components SET component = ? WHERE RestaurantName = ? AND DishName = ?";
//				stmt = conn.prepareStatement(query);
//				stmt.setString(1, product.getComponents().get(i).toString());
//				stmt.setString(2, product.getRestaurantName());
//				stmt.setString(3, product.getDishName());
//				stmt.executeUpdate();
//			} catch (SQLException e) {
//				e.printStackTrace();
//				serverResponse.setMsg(e.getMessage());
//				serverResponse.setServerResponse(null);
//				return serverResponse;
//			}
//		}
		
		serverResponse.setMsg("Success");
		return serverResponse;
	}

	/**
	 * imports users from import simulation table to users table.
	 */
	public static void importUsers() {
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM bitemedb.importsimulationuser WHERE UserName NOT IN (SELECT UserName FROM bitemedb.users)");
			while (rs.next()) {
				// insert user to users table:
				PreparedStatement pstmt;
				String query = "INSERT INTO bitemedb.users (UserName, Password, FirstName, LastName, ID, Email, PhoneNumber, UserType, Role, Organization)"
						+ " VALUES(?,?,?,?,?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, rs.getString(2)); // username
				pstmt.setString(2, rs.getString(3)); // pw
				pstmt.setString(3, rs.getString(4)); // fname
				pstmt.setString(4, rs.getString(5)); // lname
				pstmt.setString(5, rs.getString(1)); // id
				pstmt.setString(6, rs.getString(6)); // email
				pstmt.setString(7, rs.getString(7)); // phonenumber
				pstmt.setString(8, "User"); // userType
				pstmt.setString(9, rs.getString(8)); // Role
				pstmt.setString(10, rs.getString(9)); // organization
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}

	/*
	 * Inserting new rate for order.
	 * 
	 * @param orderNumber
	 * 
	 * @param rate
	 * 
	 * @return ServerResponse
	 */
	public static ServerResponse setRate(String orderNumber, String rate) {
		ServerResponse serverResponse = new ServerResponse("rateResponse");
		try {
			PreparedStatement stmt;
			String query = "INSERT INTO bitemedb.ratings (OrderNumber, Rating) VALUES(?,?)";
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, Integer.parseInt(orderNumber));
			stmt.setString(2, rate);
			stmt.executeUpdate();
		} catch (SQLException e) {
			serverResponse.setMsg(e.getMessage());
			return serverResponse;
		}
		serverResponse.setMsg("Success");
		return serverResponse;
	}
	
	/**
	 * Query to update customer's w4c balance.
	 * 
	 * @param orderToInsert
	 */
	public static void approveEmployer(String employerCode) {
		PreparedStatement stmt;
		String query;
		try {
			query = "UPDATE bitemedb.businesscustomer SET IsApproved = ? WHERE EmployerCode = ?";
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, 1);
			stmt.setString(2, employerCode);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static void createMonthlyReportPdf() {
	Document document = new Document();
		try {
			PdfWriter.getInstance(document, new FileOutputStream("tempReport.pdf"));
			document.open();
			Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
			Chunk chunk = new Chunk("Hello World", font);
			document.add(chunk);
			document.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}