package JDBC;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
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
import Entities.Component;
import Entities.Customer;
import Entities.Delivery;
import Entities.EmployerHR;
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
					String restaurantAddress = "";
					int monthlyComission = 12;
					ArrayList<Product> menu = null;
					if (rs.next()) {
						restaurantName = rs.getString(1);
						restaurantAddress = rs.getString(6); // added RestaurantAddress to supplier in DB - aviel
						monthlyComission = rs.getInt(3);
						menu = getMenu(restaurantName);
					}
					user = new Supplier(userName, password, firstName, lastName, id, email, phoneNumber, userType,
							organization, branch, role, status, avatar, restaurantName, menu, monthlyComission,
							restaurantAddress); // change last input from branch to restaurantAddress - aviel
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
				return new W4CCard(w4cID, employerCode, qrCode, creditCardNumber, monthlyBudget, balance, dailyBudget);
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
				TypeOfProduct type = TypeOfProduct.getEnum(rs.getString(3));
				menu.add(new Product(restaurantName, type, rs.getString(2), null, rs.getFloat(4), rs.getString(5)));
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
				switch (rs.getString(1)) {
				case "Doneness":
					components.add(new Component(Doneness.medium));
					break;
				case "Size":
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
		ServerResponse serverResponse = new ServerResponse("OrderNumber");
		try {
			String query = "SELECT * FROM bitemedb.orders WHERE OrderNumber = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setInt(1, Integer.parseInt(orderNumber));
			ResultSet rs = stmt.executeQuery(query);
			if (!rs.next()) {// if (rs.getRow() == 0) {
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
		serverResponse.setServerResponse(orderNumber);
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
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, 1);
			statement.setString(2, filename);
			statement.setDate(3, new Date(2011, 11, 11));
			statement.setBlob(4, is);
			statement.setString(5, BranchName.North.toString());
			statement.setString(6, filename);
			statement.setString(7, "Burgerim");
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
	public static void insertOrderDelivery(OrderDeliveryMethod orderToInsert) {
		int orderNewKey = 0, deliveryNewKey = 0;
		PreparedStatement stmt;
		try {
			orderNewKey = insertOrder(orderToInsert.getOrder());
			if (orderNewKey == -1) {
				return;
			}
			System.out.println("After order insert:) & ordernewkey not -1\nBefore delivery insert");
			deliveryNewKey = insertDelivery(orderToInsert.getDelivery(), orderToInsert.getTypeOfOrder());
			if (deliveryNewKey == -1) {
				return;
			}
			String query = "INSERT INTO bitemedb.ordereddelivery (DeliveryNumber, OrderNumber, UserName, FinalPrice) VALUES(?,?,?,?)";
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, deliveryNewKey);
			stmt.setInt(2, orderNewKey);
			stmt.setString(3, orderToInsert.getCustomerInfo().getUserName());
			stmt.setFloat(4, orderToInsert.getFinalPrice());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
			System.out.println("Creating the query string");
			String query = "INSERT INTO bitemedb.orders (RestaurantName, OrderTime, OrderPrice, PaymentMethod) VALUES(?, ?, ?, ?)";
			String[] keys = { "OrderNumber" };
			stmt = conn.prepareStatement(query, keys);
			stmt.setString(1, order.getRestaurantName());
			stmt.setString(2, order.getDateTime());
			stmt.setFloat(3, order.getOrderPrice());
			stmt.setString(4, PaymentMethod.getEnum(order.getPaymentMethod()));
			stmt.executeUpdate();
			System.out.println("Executed order insert");
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
				String query = "INSERT INTO bitemedb.productinorder (OrderNumber, RestaurantName, DishName, Component) VALUES(?,?,?,?)";
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
				stmt.setString(6, delivery.getFirstName() + delivery.getLastName());
			case sharedDelivery:
				SharedDelivery shared = (SharedDelivery) delivery;
				query = "INSERT INTO bitemedb.deliveries (OrderAddress, DeliveryType, Discount, AmountOfPeople, DeliveryPhoneNumber, DeliveryReceiver) VALUES (?,?,?,?,?,?)";
				stmt = conn.prepareStatement(query, keys);
				stmt.setString(1, delivery.getOrderAddress());
				stmt.setString(2, typeOfOrder.toString());
				stmt.setFloat(3, delivery.getDiscount());
				stmt.setInt(4, shared.getAmountOfPeople());
				stmt.setString(5, delivery.getPhoneNumber());
				stmt.setString(6, delivery.getFirstName() + delivery.getLastName());
			default:
				query = "INSERT INTO bitemedb.deliveries (OrderAddress, DeliveryType, Discount, DeliveryPhoneNumber, DeliveryReceiver) VALUES (?,?,?,?,?)";
				stmt = conn.prepareStatement(query, keys);
				stmt.setString(1, delivery.getOrderAddress());
				stmt.setString(2, typeOfOrder.toString());
				stmt.setFloat(3, delivery.getDiscount());
				stmt.setString(4, delivery.getPhoneNumber());
				stmt.setString(5, delivery.getFirstName() + delivery.getLastName());
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
}