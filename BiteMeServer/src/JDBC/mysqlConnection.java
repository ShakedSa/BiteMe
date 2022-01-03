package JDBC;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import Config.ReadPropertyFile;
import Entities.BranchManager;
import Entities.BusinessCustomer;
import Entities.CEO;
import Entities.Component;
import Entities.Customer;
import Entities.Delivery;
import Entities.EmployerHR;
import Entities.ImportedUser;
import Entities.NewAccountUser;
import Entities.MyFile;
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
import Enums.RestaurantType;
import Enums.Size;
import Enums.Status;
import Enums.TypeOfOrder;
import Enums.TypeOfProduct;
import Enums.UserType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * MySQL Connection class. Using a single connector to the DB.
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
	 * Default arguments for connection received from configuration file.
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
	@SuppressWarnings("resource")
	public static ServerResponse login(String userName, String password) {
		ServerResponse serverResponse = new ServerResponse("User");
		PreparedStatement stmt;
		User user = null;
		try {
			String query = "SELECT * FROM bitemedb.users WHERE UserName = ? AND Password = ? And NOT Status = 'Deleted'";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, userName);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				if (rs.getInt(12) == 1) {
					serverResponse.setMsg("Already Logged In");
					serverResponse.setServerResponse(null);
					return serverResponse;
				}
				if (rs.getString(13).equals("Frozen")) {
					serverResponse.setMsg("Frozen");
					serverResponse.setServerResponse(null);
					return serverResponse;
				}
				if (rs.getString(8).equals("User") || rs.getString(13).equals("Unverified")) {
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
					boolean isPrivate = false, isBusiness = false, isApproved = false;
					if (rs.next()) {
						cusID = rs.getInt(1);
						isPrivate = rs.getInt(5) == 0 ? false : true;
						isBusiness = rs.getInt(3) == 0 ? false : true;
						isApproved = rs.getInt(4) == 0 ? false : true;
					}
					W4CCard w4cCard = getW4CCard(cusID);
					HashMap<String, Float> refunds = getRefund(cusID);
					user = new Customer(userName, password, firstName, lastName, id, email, phoneNumber, userType,
							organization, branch, role, status, avatar, w4cCard, refunds, cusID, isPrivate, isBusiness,
							isApproved);
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
					MyFile restaurantLogo = null;
					if (rs.next()) {
						restaurantName = rs.getString(1);
						restaurantAddress = rs.getString(2); // added RestaurantAddress to supplier in DB - aviel
						monthlyComission = rs.getFloat(4);
						menu = getMenu(restaurantName);
						restaurantLogo = new MyFile(restaurantName + " logo");
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
	 * Query to get restaurants based on the user's branch.
	 * 
	 * @param user
	 * 
	 * @return ServerResponse serverResponse
	 */
	public static ServerResponse getRestaurants(User user) {
		ServerResponse serverResponse = new ServerResponse("Restaurants");
		PreparedStatement stmt;
		ArrayList<Supplier> restaurants = new ArrayList<>();
		try {
			String query = "SELECT RestaurantName, RestaurantType, Image FROM bitemedb.suppliers WHERE branch = ? ORDER BY RestaurantName";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, user.getMainBranch().toString());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Blob image = rs.getBlob(3);
				MyFile file = new MyFile(rs.getString(1) + " logo");
				byte[] array = image.getBytes(1, (int) image.length());
				file.initArray(array.length);
				file.setMybytearray(array);
				restaurants.add(new Supplier(rs.getString(1), RestaurantType.valueOf(rs.getString(2)), file));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		serverResponse.setMsg("Success");
		serverResponse.setServerResponse(restaurants);
		return serverResponse;
	}

	/**
	 * Query to update user's main branch.
	 * 
	 * @param userName
	 * @param newBranch
	 * 
	 * @return ServerResponse
	 */
	public static ServerResponse changeBranch(String userName, String newBranch) {
		ServerResponse serverResponse = new ServerResponse("ChangeBranch");
		PreparedStatement stmt;
		try {
			String query = "UPDATE bitemedb.users SET MainBranch = ? WHERE UserName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, newBranch);
			stmt.setString(2, userName);
			stmt.executeUpdate();
			serverResponse.setMsg("Success");
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg("Failed");
		}
		return serverResponse;
	}

	/**
	 * Query to update a client information in the db.
	 * 
	 * @param userName
	 * @param status
	 * 
	 * @return ServerResponse serverResponse
	 */
	public static void changeClientPerrmisions(String userName, String status) {
//		ServerResponse serverResponse = new ServerResponse("updateUser");
		PreparedStatement stmt;
		String query;
		try {
			query = "UPDATE bitemedb.users SET Status = ? WHERE UserName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, status);
			stmt.setString(2, userName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
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

	/**
	 * A Private method for receiving refunds of Customer.
	 * 
	 * @param CustomerID
	 * 
	 * @return HashMap<String, Float>
	 */
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
	 * Query to get all of the customer's available refunds.
	 * 
	 * @param customer
	 * 
	 * @return ServerResponse
	 */
	public static ServerResponse getRefund(Customer customer) {
		ServerResponse serverResponse = new ServerResponse("Refunds");
		HashMap<String, Float> refunds = getRefund(customer.getCustomerID());
		serverResponse.setServerResponse(refunds);
		return serverResponse;
	}

	/**
	 * execute a Query to add a new supplier to the db.
	 * 
	 * @return ServerResponse serverResponse
	 */
	public static ServerResponse addNewSupplier(NewSupplier supplier) {
		ServerResponse serverResponse = new ServerResponse("newSupplier");
		PreparedStatement stmt = null;
		int monthlyCommision = Character.getNumericValue(supplier.getMonthlyCommision().charAt(0));
		try {
			// check that the resturant name and address are'nt already exist
			String query = "SELECT RestaurantName, RestaurantAddress FROM bitemedb.suppliers WHERE RestaurantName = ? AND RestaurantAddress = ? ";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, supplier.getResturantName());
			stmt.setString(2, supplier.getResturantAddress());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				serverResponse.setMsg("already exist");
				return serverResponse;
			}
			// give suppliers permissions to the chosen user
			query = "UPDATE bitemedb.users SET UserType = ?  WHERE UserName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, "Supplier");
			stmt.setString(2, supplier.getUserName());
			stmt.executeUpdate();

			// add a new supplier to the suppliers table
			query = "INSERT INTO bitemedb.suppliers" + "(RestaurantName, RestaurantAddress, UserName, MonthlyComission,"
					+ " Image, RestaurantType, branch)" + "VALUES(?, ?, ?, ?, ?, ?, ?)";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, supplier.getResturantName());
			stmt.setString(2, supplier.getResturantAddress());
			stmt.setString(3, supplier.getUserName());
			stmt.setInt(4, monthlyCommision);
			stmt.setBlob(5, new ByteArrayInputStream(supplier.getImagUpload().mybytearray));
			stmt.setString(6, supplier.getResturantType());
			stmt.setString(7, supplier.getBranchName().name());
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("query failed");
		}
		serverResponse.setMsg("success");
		return serverResponse;
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
				@SuppressWarnings("unchecked")
				ArrayList<Component> components = (ArrayList<Component>) (getComponentsInProduct(restaurantName,
						rs.getString(2))).getServerResponse();
				menu.add(new Product(restaurantName, type, rs.getString(2), components, rs.getFloat(4),
						rs.getString(5)));
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
	 * @return ServerResponse
	 */
	public static ServerResponse logout(String userName) {
		if (!updateIsLoggedIn(userName, 0)) {
			return new ServerResponse("Logout Failed.");
		}
		return new ServerResponse("Logout Successful.");
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
	 * Getting the 6 favorite restaurants from the DB to display on the main page.
	 * The restaurants are order by their name.
	 * 
	 * @return ServerResponse
	 */
	public static ServerResponse favRestaurants() {
		ServerResponse serverResponse = new ServerResponse("FavRestaurants");
		Statement stmt;
		ArrayList<Supplier> favRestaurants = new ArrayList<>();
		try {
			String query = "SELECT S.RestaurantName, S.RestaurantType, S.Image, SUM(R.Rating) as rates"
					+ " FROM bitemedb.orders O inner join bitemedb.ratings R inner join bitemedb.suppliers S WHERE O.RestaurantName = S.RestaurantName AND O.OrderNumber = R.OrderNumber\r\n"
					+ " GROUP BY S.RestaurantName" + " ORDER BY rates DESC" + " LIMIT 6;";
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Blob image = rs.getBlob(3);
				MyFile file = new MyFile(rs.getString(1));
				byte[] array = image.getBytes(1, (int) image.length());
				file.initArray(array.length);
				file.setMybytearray(array);
				favRestaurants.add(new Supplier(rs.getString(1), RestaurantType.valueOf(rs.getString(2)), file));
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
	 * Checks if a user name number is exist in the DB or not
	 * 
	 * @param hrUserName
	 * @param employerCompanyName
	 * @return true if exist. false if doesn't
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
	 * exe a query to create a new business customer
	 * 
	 * @param hrUserName,employerCode,employerCompanyName
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
	 * updating customer to approved in DB and returning updated list of not
	 * approved customers that related to this employerComapnyName
	 * 
	 * @param employerCompanyName
	 * @param hrUserName
	 * @return ArrayList-ServerResponse
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
	 * @param employerCompanyName
	 * @return ArrayList-ServerResponse
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

	/**
	 * Query to get user's type and status.
	 * 
	 * @param username
	 * 
	 * @return ServerResponse
	 */
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
	 * a query thats checks what accounts the received user has
	 * 
	 * @param orderNumber
	 * @return ServerResponse
	 */
	public static ServerResponse checkUserNameAccountType(String username) {
		ServerResponse serverResponse = new ServerResponse("ArrayList");
		ArrayList<String> response = new ArrayList<>();
		try {
			PreparedStatement stmt;
			String query = "SELECT * FROM bitemedb.users WHERE UserName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) { // check if the username exist
				response.add(rs.getString(3));
				response.add(rs.getString(4));
				if (rs.getString(8).equals("User")) { // check what is his current permissions
					serverResponse.setMsg("both"); // we can open for him private and business account
					return serverResponse;
				} else if (rs.getString(8).equals("Customer") && !rs.getString(13).equals("Deleted")) {
					query = "SELECT * FROM bitemedb.customers WHERE UserName = ?";
					stmt = conn.prepareStatement(query);
					stmt.setString(1, username);
					ResultSet rs1 = stmt.executeQuery();
					if (rs1.next()) {
						// if the user has private and business accounts
						if (rs1.getInt(3) == 1 && rs1.getInt(4) == 1) {
							serverResponse.setMsg("has both");
							serverResponse.setServerResponse(response);
							return serverResponse;
						} else if (rs1.getInt(3) == 1) {
							serverResponse.setServerResponse(response);
							serverResponse.setMsg("is private");
							return serverResponse;
						} else if (rs1.getInt(4) == 1) {
							serverResponse.setServerResponse(response);
							serverResponse.setMsg("is businss");
							return serverResponse;
						}
					}
				} else {
					serverResponse.setMsg("not client");
					serverResponse.setServerResponse(response);
					return serverResponse;
				}
			} else {
				serverResponse.setMsg("Error");
				return serverResponse;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		serverResponse.setMsg("doesnt have");
		serverResponse.setServerResponse(response);
		return serverResponse;
	}

	/**
	 * Checks if a user name number is exist and has client permmissions
	 * 
	 * @param orderNumber
	 * @return ServerResponse
	 */
	public static ServerResponse checkUserNameIsClient(String username) {
		ServerResponse serverResponse = new ServerResponse("ArrayList");
		ArrayList<String> response = new ArrayList<>();
		try {
			PreparedStatement stmt;
			String query = "SELECT * FROM bitemedb.users WHERE UserName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, username);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				if (rs.getString(8).equals("Customer") && !rs.getString(13).equals("Deleted")) {
					response.add(rs.getString(3));
					response.add(rs.getString(4));
					response.add(rs.getString(13));
				} else {
					serverResponse.setMsg("is not client");
					return serverResponse;
				}
			} else {
				serverResponse.setMsg("Error");
				return serverResponse;
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
	 * @param is   File inputstream to upload as a blob
	 * @param date - report date
	 * @param desc - contains info about the report as string arrayList
	 *             reportType,Month,Year,branch
	 */
	public static void updateFile(InputStream is, ArrayList<String> desc) {
		if (is == null)
			return;
		String filename;
		int month = Integer.parseInt(desc.get(1).toString());
		if (desc.get(0).equals("Quarterly Report"))
			filename = "Report" + desc.get(2) + "-Quarter" + month + ".pdf";
		else if (desc.get(0).equals("QuarterlyRevenueReport"))
			filename = desc.get(3) + "RevenueReport" + desc.get(2) + "-Quarter" + month + ".pdf";
		else// format: <branch>-<reportType>Report<Year>-<Month>.pdf
			filename = desc.get(3) + "-" + desc.get(0) + desc.get(2) + "-" + desc.get(1) + ".pdf";
		String sql = "INSERT INTO reports (Title,Date,content,BranchName,ReportType) values( ?, ?, ?, ?, ?)";
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, filename);
			statement.setDate(2, Date.valueOf("" + desc.get(2) + "-" + desc.get(1) + "-02"));
			statement.setBlob(3, is);
			statement.setString(4, desc.get(3));
			statement.setString(5, desc.get(0));
			statement.executeUpdate();
		} catch (SQLException e) {
			if (e instanceof SQLIntegrityConstraintViolationException)
				return;
			e.printStackTrace();
		}
	}

	/**
	 * Inserting a new order to the DB, should alert the supplier(restaurant) that a
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
	 */
	private static void updateRefund(Customer customer, String RestaurantName) {
		PreparedStatement stmt;
		try {
			int customerID = getCustomer(customer.getUserName());
			if (customer.getRefunds().get(RestaurantName) == null || customer.getRefunds().get(RestaurantName) == 0) {
				deleteRefund(customerID, RestaurantName);
				return;
			}
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
	 * Query to delete row from refunds table.
	 * 
	 * @param customerID
	 * @param RestaurantName
	 */
	private static void deleteRefund(int customerID, String RestaurantName) {
		PreparedStatement stmt;
		try {
			String query = "DELETE FROM bitemedb.refunds WHERE CustomerID = ? AND RestaurantName =?";
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, customerID);
			stmt.setString(2, RestaurantName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Query to get Quarterly Report OR to check if exists.
	 * 
	 * @param quarter
	 * @param year
	 * @param branch
	 * @return MyFile-serverResponse
	 */
	public static ServerResponse viewORcheckQuarterReport(String quarter, String year, String branch) {
		ServerResponse serverResponse = new ServerResponse("MyFile");
		try {
			String query = "SELECT distinct Content FROM bitemedb.reports where date like ? and Title like ? and ReportType = 'Quarterly Report' and BranchName = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, year + "%");
			stmt.setString(2, "%Quarter" + quarter + "%");
			stmt.setString(3, branch);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				serverResponse.setMsg("NotExists");
				serverResponse.setServerResponse(null);
				return serverResponse;
			}
			Blob blob = rs.getBlob(1);
			MyFile file = new MyFile("Blob");
			byte[] array = blob.getBytes(1, (int) blob.length());
			file.initArray(array.length);
			file.setMybytearray(array);
			serverResponse.setServerResponse(file);
			serverResponse.setMsg("Success");
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg("Failed");
			serverResponse.setServerResponse(null);
		}
		return serverResponse;
	}

	/**
	 * Query to get Revenue Quarterly Report OR to check if exists.
	 * 
	 * @param quarter
	 * @param year
	 * @param branch
	 * @return MyFile-serverResponse
	 */
	public static ServerResponse viewORcheckRevenueQuarterReport(String quarter, String year, String branch) {

		ServerResponse serverResponse = new ServerResponse("MyFile");
		try {

			String query = "SELECT distinct Content FROM bitemedb.reports where date like ? and Title like ? and ReportType = 'QuarterlyRevenueReport' and BranchName = ?";

			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, year + "%");
			stmt.setString(2, "%Quarter" + quarter + "%");
			stmt.setString(3, branch);
			ResultSet rs = stmt.executeQuery();

			if (!rs.next()) {
				serverResponse.setMsg("NotExists");
				serverResponse.setServerResponse(null);
				return serverResponse;
			}

			Blob blob = rs.getBlob(1);
			MyFile file = new MyFile("Blob");
			byte[] array = blob.getBytes(1, (int) blob.length());
			file.initArray(array.length);
			file.setMybytearray(array);
			serverResponse.setServerResponse(file);
			serverResponse.setMsg("Success");

		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg("Failed");
			serverResponse.setServerResponse(null);
		}
		return serverResponse;
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
	 * @return int - customer's id
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
				String query = "INSERT INTO bitemedb.productinorder (OrderNumber, RestaurantName, DishName, Components, Amount) VALUES(?,?,?,?,?)";
				stmt = conn.prepareStatement(query);
				stmt.setInt(1, orderNumber);
				stmt.setString(2, p.getRestaurantName());
				stmt.setString(3, p.getDishName());
				stmt.setString(4, p.getComponents().toString());
				stmt.setInt(5, p.getAmount());
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
				break;
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
				break;
			case takeaway:
				query = "INSERT INTO bitemedb.deliveries (DeliveryType, Discount, DeliveryPhoneNumber, DeliveryReceiver) VALUES (?,?,?,?)";
				stmt = conn.prepareStatement(query, keys);
				stmt.setString(1, typeOfOrder.toString());
				stmt.setFloat(2, delivery.getDiscount());
				stmt.setString(3, delivery.getPhoneNumber());
				stmt.setString(4, name);
				break;
			default:
				query = "INSERT INTO bitemedb.deliveries (OrderAddress, DeliveryType, Discount, DeliveryPhoneNumber, DeliveryReceiver) VALUES (?,?,?,?,?)";
				stmt = conn.prepareStatement(query, keys);
				stmt.setString(1, delivery.getOrderAddress());
				stmt.setString(2, typeOfOrder.toString());
				stmt.setFloat(3, delivery.getDiscount());
				stmt.setString(4, delivery.getPhoneNumber());
				stmt.setString(5, name);
				break;
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
	 * exe query to get all the unapproved businessCustomers
	 * 
	 * @return
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

			// save in response all employers that needs approval
			while (rs.next()) {
				response.add(new BusinessCustomer(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getString(4)));
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
	 * Updates order status and planned time/received time in order table if order
	 * status = "Received" -> update received time if order status = "Ready" ->
	 * update planned time
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
	public static ServerResponse updateOrderStatus(String restaurantName, String receivedOrReady, String orderNumber,
			String time, String status) {
		ServerResponse serverResponse = new ServerResponse("Integer");
		try {
			if (receivedOrReady.equals("Order Received")) {
				String query = "UPDATE bitemedb.orders SET OrderStatus = ?, OrderReceived = ? WHERE OrderNumber = ? AND RestaurantName = ?";
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setString(1, status);
				stmt.setString(2, time);
				stmt.setString(3, orderNumber);
				stmt.setString(4, restaurantName);
				stmt.executeUpdate();
			} else { // Order Is Ready
				String query = "UPDATE bitemedb.orders SET OrderStatus = ?, PlannedTime = ? WHERE OrderNumber = ? AND RestaurantName = ?";
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setString(1, status);
				stmt.setString(2, time);
				stmt.setString(3, orderNumber);
				stmt.setString(4, restaurantName);
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

	/**
	 * Getting data about specific order of the supplier "restaurantName"
	 * 
	 * @param orderNumber
	 * @return array list that includes - order number, order time, received time,
	 *         planned time and status
	 */
	public static ServerResponse getOrderInfo(String restaurantName) {
		ServerResponse serverResponse = new ServerResponse("ArrayList");
		ArrayList<Order> response = new ArrayList<>();
		try {
			PreparedStatement stmt;
			String query = "SELECT * FROM bitemedb.orders WHERE RestaurantName = ? AND (OrderStatus = 'Pending' OR OrderStatus = 'Received')";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, restaurantName);
			ResultSet rs = stmt.executeQuery();
			// save in response the appropriate orders
			while (rs.next()) {
				response.add(
						new Order(rs.getInt(1), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(9)));
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
	 * Getting phone number and customer name for this specific deliveryNumber
	 * 
	 * @param deliveryNumber
	 * @return array list that includes - phone number and customer name
	 */
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

	/**
	 * Adding new item to menu by supplier
	 * 
	 * @param product
	 * @return arrayList of products - update menu
	 */
	public static ServerResponse addItemToMenu(Product product) {
		ServerResponse serverResponse = new ServerResponse("ArratList");
		try {
			// first check for duplicates:
			String check = "SELECT DishName FROM bitemedb.products WHERE RestaurantName= ? AND DishName= ? ";
			PreparedStatement checkStmt;
			checkStmt = conn.prepareStatement(check);
			checkStmt.setString(1, product.getRestaurantName());
			checkStmt.setString(2, product.getDishName());
			ResultSet rs = checkStmt.executeQuery();
			if (rs.next()) {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		try {
			// if not duplicates, insert:
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

		// set components
		if (product.getComponents() != null) {
			for (int i = 0; i < product.getComponents().size(); i++) {
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
		}
		serverResponse.setMsg("Success");
		return getMenuToOrder(product.getRestaurantName());
	}

	/**
	 * Edits an item in menu - by supplier
	 * 
	 * @param product
	 * @return arrayList of products - update menu
	 */
	public static ServerResponse editItemInMenu(Product product) {
		ServerResponse serverResponse = new ServerResponse("ArratList");
		int flag = 0;
		try {
			PreparedStatement stmt;
			String query = "UPDATE bitemedb.products SET Type = ?, Price = ?, ProductDescription = ? WHERE RestaurantName = ? AND DishName = ?";
			stmt = conn.prepareStatement(query);
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

		// check if there are old components to delete
		try {
			PreparedStatement stmt;
			String query = "SELECT RestaurantName, DishName FROM bitemedb.components WHERE RestaurantName = ? AND DishName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, product.getRestaurantName());
			stmt.setString(2, product.getDishName());
			stmt.executeQuery();
			flag = 1; // there is a row to delete
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}

		// delete the old components
		if (flag == 1) {
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
		}

		// set the new components if there are
		if (product.getComponents() != null) {
			for (int i = 0; i < product.getComponents().size(); i++) {
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
		}
		serverResponse.setMsg("Success");
		return getMenuToOrder(product.getRestaurantName());
	}

	/**
	 * exe a query to get the newly imported users from the DB
	 * 
	 * @return
	 */
	public static ServerResponse getImportedUsers() {
		ServerResponse serverResponse = new ServerResponse("ArrayList");
		ArrayList<NewAccountUser> response = new ArrayList<>();
		try {
			PreparedStatement stmt;
			String query = "SELECT * FROM bitemedb.users WHERE UserType = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, "User");
			ResultSet rs = stmt.executeQuery();
			// save in response all newly imported users
			while (rs.next()) {
				response.add(new NewAccountUser(rs.getString(1), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7)));
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
	 * get newly imported users from the DB
	 * 
	 * @return
	 */
	public static ServerResponse getAllUsersAndCustomers() {
		ServerResponse serverResponse = new ServerResponse("ArrayList");
		ArrayList<NewAccountUser> response = new ArrayList<>();
		try {
			PreparedStatement stmt;
			String query = "SELECT * FROM bitemedb.users WHERE UserType = ? OR UserType = ?"
					+ " and UserName not in (SELECT UserName FROM bitemedb.customers"
					+ " where (IsBusiness=1 and IsPrivate=1))";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, "User");
			stmt.setString(2, "Customer");
			ResultSet rs = stmt.executeQuery();
			// save in response all newly imported users
			while (rs.next()) {
				response.add(new NewAccountUser(rs.getString(1), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7)));
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

	/**
	 * @param Branch
	 * @return arrayList of restaurant names in the branch
	 */
	public static ArrayList<String> getRestaurantList(String Branch) {
		PreparedStatement stmt;
		String query;
		ArrayList<String> arr = new ArrayList<String>();
		try {
			query = "SELECT RestaurantName FROM bitemedb.suppliers WHERE branch=? order by RestaurantName";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, Branch);
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				arr.add(rs.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
			return arr;
		}
		return arr;
	}

	/**
	 * @param restaurantName
	 * @param month
	 * @param year
	 * @return number of orders made in restaurant on chosen month.
	 */
	public static int getNumOfOrders(String restaurantName, String month, String year) {
		PreparedStatement stmt;
		String query;
		int num = 0;
		ResultSet rs;
		try {
			query = "SELECT count(OrderNumber) FROM bitemedb.orders where MONTH(OrderReceived)=? AND YEAR(OrderReceived)=? AND RestaurantName=?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, month);
			stmt.setString(2, year);
			stmt.setString(3, restaurantName);
			rs = stmt.executeQuery();
			if (rs.next())
				num = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return num;
	}

	/**
	 * Query to get all the income of a selected restaurant.
	 * 
	 * @param restaurantName
	 * @param month
	 * @param year
	 * 
	 * @return total income in selected restaurants on the given month.
	 */
	public static int getEarnings(String restaurantName, String month, String year) {
		PreparedStatement stmt;
		String query;
		int num = 0;
		ResultSet rs;
		// SELECT OrderNumber FROM bitemedb.orders where RestaurantName=?
		try {
			query = "SELECT SUM(FinalPrice) FROM bitemedb.ordereddelivery WHERE OrderNumber IN (SELECT OrderNumber FROM bitemedb.orders where RestaurantName=? AND MONTH(OrderReceived)=? AND YEAR(OrderReceived)=?)";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, restaurantName);
			stmt.setString(2, month);
			stmt.setString(3, year);
			rs = stmt.executeQuery();
			if (rs.next())
				num = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return num;
	}

	/**
	 * Query to get all the dishes ordered in a restaurant.
	 * 
	 * @param res
	 * @param month
	 * @param year
	 * @return arrayList of dishes ordered in a restaurant in the given month.
	 */
	public static ArrayList<String> getDishesList(String restaurantName, String month, String year) {

		PreparedStatement stmt;
		String query;
		ArrayList<String> arr = new ArrayList<String>();
		try {
			query = "SELECT distinct(DishName) FROM bitemedb.productinorder "
					+ "WHERE OrderNumber IN (SELECT OrderNumber FROM bitemedb.orders "
					+ "WHERE RestaurantName=? and MONTH(OrderReceived)=? AND YEAR(OrderReceived)=?)";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, restaurantName);
			stmt.setString(2, month);
			stmt.setString(3, year);
			ResultSet rs = stmt.executeQuery();
			while (rs.next())
				arr.add(rs.getString(1));
		} catch (SQLException e) {
			e.printStackTrace();
			return arr;
		}
		return arr;
	}

	/**
	 * @param restaurantName
	 * @param month
	 * @param year
	 * @param dish
	 * @return returns the number of dishes ordered on restaurant in selected month
	 */
	public static int getNumOfOrderedDishes(String restaurantName, String month, String year, String dish) {

		PreparedStatement stmt;
		String query;
		int num = 0;
		try {
			query = "SELECT Count(DishName) as count FROM bitemedb.productinorder WHERE"
					+ " Dishname=? and OrderNumber IN (SELECT OrderNumber FROM bitemedb.orders"
					+ " WHERE RestaurantName=? and MONTH(OrderReceived)=? AND YEAR(OrderReceived)=?)";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, dish);
			stmt.setString(2, restaurantName);
			stmt.setString(3, month);
			stmt.setString(4, year);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				num = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return num;
		}
		return num;
	}

	/**
	 * Query to get all the delayed order from a restaurant.
	 * 
	 * @param restaurantName
	 * @param month
	 * @param year
	 * @return returns number of delayed orders on a restaurant in chosen month
	 */
	public static int getDelayedOrders(String restaurantName, String month, String year) {
		PreparedStatement stmt;
		String query;
		int delayedOrders = 0;
		ResultSet rs;
		// get delayed non preorders:
		try {
			query = "SELECT count(OrderNumber) as num FROM bitemedb.orders where MONTH(OrderReceived)=?"
					+ " AND YEAR(OrderReceived)=? AND RestaurantName=? AND HOUR(TIMEDIFF(CustomerReceived, OrderReceived))>1"
					+ " AND OrderNumber in ( SELECT OrderNumber FROM bitemedb.ordereddelivery where"
					+ " deliveryNumber not in (SELECT DeliveryNumber FROM bitemedb.deliveries"
					+ " WHERE deliverytype=?))";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, month);
			stmt.setString(2, year);
			stmt.setString(3, restaurantName);
			stmt.setString(4, "Preorder Delivery");
			rs = stmt.executeQuery();
			if (rs.next())
				delayedOrders += rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		// get delayed preorders
		try {
			query = "SELECT count(OrderNumber) as num FROM bitemedb.orders where MONTH(OrderReceived)=?"
					+ " AND YEAR(OrderReceived)=? AND RestaurantName=? AND MINUTE(TIMEDIFF(CustomerReceived, OrderReceived))>=20"
					+ " AND OrderNumber in ( SELECT OrderNumber FROM bitemedb.ordereddelivery where"
					+ " deliveryNumber in (SELECT DeliveryNumber FROM bitemedb.deliveries" + " WHERE deliverytype=?))";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, month);
			stmt.setString(2, year);
			stmt.setString(3, restaurantName);
			stmt.setString(4, "Preorder Delivery");
			rs = stmt.executeQuery();
			if (rs.next())
				delayedOrders += rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return delayedOrders;
	}

	/*
	 * Query to received all the open orders that the customer have.
	 * 
	 * @param customer
	 * 
	 * @return
	 */
	public static ServerResponse customersOrder(Customer customer) {
		ServerResponse serverResponse = new ServerResponse("CustomersOrders");
		PreparedStatement stmt;
		ArrayList<Order> orders = new ArrayList<>();
		try {
			String query = "SELECT OrderNumber, FinalPrice FROM bitemedb.ordereddelivery WHERE UserName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, customer.getUserName());
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				Order order = getOrderInfo(rs.getInt(1), rs.getFloat(2));
				if (order != null) {
					orders.add(order);
				}
			}
			serverResponse.setMsg("Success");
			serverResponse.setServerResponse(orders);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return serverResponse;
	}

	/**
	 * Private query to received orders by their order number.
	 * 
	 * @param orderNumber
	 * @param finalPrice
	 * @return
	 */
	private static Order getOrderInfo(int orderNumber, float finalPrice) {
		Order order = new Order();
		PreparedStatement stmt;
		try {
			String query = "SELECT RestaurantName, PlannedTime, OrderStatus FROM bitemedb.orders WHERE OrderNumber = ? AND (CustomerReceived IS NULL)";
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, orderNumber);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				order.setOrderNumber(orderNumber);
				order.setRestaurantName(rs.getString(1));
				order.setDateTime(rs.getString(2));
				order.setOrderPrice(finalPrice);
				order.setStatus(rs.getString(3));
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return order;
	}

	/**
	 * Query to update that the customer received the order.
	 * 
	 * @param order
	 * @return
	 */
	public static ServerResponse updateOrderReceived(Order order) {
		ServerResponse serverResponse = new ServerResponse("Update Order");
		PreparedStatement stmt;
		try {
			String query = "UPDATE bitemedb.orders SET CustomerReceived = ? WHERE OrderNumber = ?";
			stmt = conn.prepareStatement(query);
			LocalDateTime dateTime = LocalDateTime.now();
			stmt.setString(1, dateTime.toString());
			stmt.setInt(2, order.getOrderNumber());
			stmt.executeUpdate();
			checkRefund(order, dateTime);
			serverResponse.setMsg("Success");
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return serverResponse;
	}

	/**
	 * Query to check if a refund is require for an order.
	 * 
	 * @param order
	 * @param dateTime
	 * 
	 */
	@SuppressWarnings("resource")
	private static void checkRefund(Order order, LocalDateTime dateTime) {
		PreparedStatement stmt;
		TypeOfOrder typeOfOrder = null;
		String preorderTime = "";
		String userName = "";
		BooleanProperty flag = new SimpleBooleanProperty(false);
		try {
			String query = "SELECT DeliveryType, PreOrderTime, UserName FROM bitemedb.deliveries D INNER JOIN bitemedb.ordereddelivery O WHERE"
					+ " D.DeliveryNumber = O.DeliveryNumber AND OrderNumber = ?";
			stmt = conn.prepareStatement(query);
			stmt.setInt(1, order.getOrderNumber());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				typeOfOrder = TypeOfOrder.getEnum(rs.getString(1));
				preorderTime = rs.getString(2);
				userName = rs.getString(3);
			}
			int arriveTime = dateTime.getHour() * 60 + dateTime.getMinute();
			switch (typeOfOrder) {
			case BasicDelivery:
			case sharedDelivery:
				int plannedTimeHour = Integer.parseInt((order.getDateTime().split(" "))[1].split(":")[0]);
				int plannedTimeMinute = Integer.parseInt((order.getDateTime().split(" ")[1].split(":")[1]));
				int plannedArrivedTime = plannedTimeHour * 60 + plannedTimeMinute;
				if (arriveTime - plannedArrivedTime > 60) {
//					Needs refund
					flag.set(true);
				}
				break;
			case preorderDelivery:
				int plannedPreOrderHour = Integer.parseInt((preorderTime.split(" ")[1]).split(":")[0]);
				int plannedPreOrderMinute = Integer.parseInt((preorderTime.split(" ")[1]).split(":")[1]);
				int plannedPreOrderTime = plannedPreOrderHour * 60 + plannedPreOrderMinute;
				if (arriveTime - plannedPreOrderTime > 20) {
//					Needs refund.
					flag.set(true);
				}
				break;
			default:
				break;
			}
			if (flag.get()) {
				query = "UPDATE bitemedb.orders SET RefundAmount = ? WHERE OrderNumber = ?";
				stmt = conn.prepareStatement(query);
				stmt.setFloat(1, order.getOrderPrice() * (float) 0.5);
				stmt.setInt(2, order.getOrderNumber());
				stmt.executeUpdate();
				int customerId = getCustomer(userName);
				float refund;
				if ((refund = checkIfRefundExsits(order.getRestaurantName(), customerId)) == 0) {
					query = "INSERT INTO bitemedb.refunds (CustomerID, RestaurantName, Refund) VALUES(?,?,?)";
					stmt = conn.prepareStatement(query);
					stmt.setInt(1, customerId);
					stmt.setString(2, order.getRestaurantName());
					stmt.setFloat(3, order.getOrderPrice() * (float) 0.5);
					stmt.executeUpdate();
				} else {
					query = "UPDATE bitemedb.refunds SET Refund = ? WHERE CustomerID = ? AND RestaurantName = ?";
					stmt = conn.prepareStatement(query);
					stmt.setFloat(1, refund + order.getOrderPrice() * (float) 0.5);
					stmt.setInt(2, customerId);
					stmt.setString(3, order.getRestaurantName());
					stmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Private method to check if a refund exist for a certain user in a certain
	 * restaurant.
	 * 
	 * @param restaurantName
	 * @param customerID
	 * 
	 * @return float
	 */
	private static float checkIfRefundExsits(String restaurantName, int customerID) {
		try {
			PreparedStatement stmt = conn.prepareStatement(
					"SELECT Refund FROM bitemedb.refunds WHERE CustomerID = ? AND RestaurantName = ?");
			stmt.setInt(1, customerID);
			stmt.setString(2, restaurantName);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getFloat(1);
			}
		} catch (SQLException e) {
			return 0;
		}
		return 0;
	}

	/**
	 * deletes an item from menu - by supplier
	 * 
	 * @param restaurantName
	 * @param dishName
	 * @return arrayList of products - update menu
	 */
	public static ServerResponse deleteItemFromMenu(String restaurantName, String dishName) {
		ServerResponse serverResponse = new ServerResponse("ArratList");
		int flag1 = 0, flag2 = 0;
		// delete from products table
		try {
			PreparedStatement stmt;
			String query = "SET foreign_key_checks=0";
			stmt = conn.prepareStatement(query);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}

		try {
			PreparedStatement stmt;
			String query = "DELETE FROM bitemedb.products WHERE RestaurantName = ? AND DishName = ?;";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, restaurantName);
			stmt.setString(2, dishName);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}
		try {

			PreparedStatement stmt;
			String query = "SET foreign_key_checks=1";
			stmt = conn.prepareStatement(query);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}

		// check if need delete from productInOrder table too
		try {
			PreparedStatement stmt;
			String query = "SELECT RestaurantName,DishName FROM bitemedb.productinorder WHERE RestaurantName = ? AND DishName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, restaurantName);
			stmt.setString(2, dishName);
			stmt.executeQuery();
			flag1 = 1; // there is a row to delete
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}

		// delete from productInOrder table
		if (flag1 == 1) {
			try {
				PreparedStatement stmt;
				String query = "DELETE FROM bitemedb.productinorder WHERE RestaurantName = ? AND DishName = ?";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, restaurantName);
				stmt.setString(2, dishName);
				stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				serverResponse.setMsg(e.getMessage());
				serverResponse.setServerResponse(null);
				return serverResponse;
			}
		}

		// check if there are components to delete
		try {
			PreparedStatement stmt;
			String query = "SELECT RestaurantName, DishName FROM bitemedb.components WHERE RestaurantName = ? AND DishName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, restaurantName);
			stmt.setString(2, dishName);
			stmt.executeQuery();
			flag2 = 1; // there is a row to delete
		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg(e.getMessage());
			serverResponse.setServerResponse(null);
			return serverResponse;
		}

		// delete item components
		if (flag2 == 1) {
			try {
				PreparedStatement stmt;
				String query = "DELETE FROM bitemedb.components WHERE RestaurantName = ? AND DishName = ?";
				stmt = conn.prepareStatement(query);
				stmt.setString(1, restaurantName);
				stmt.setString(2, dishName);
				stmt.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				serverResponse.setMsg(e.getMessage());
				serverResponse.setServerResponse(null);
				return serverResponse;
			}
		}
		serverResponse.setMsg("Success");
		return getMenuToOrder(restaurantName);
	}

	// SELECT Date FROM bitemedb.reports order by Date desc;
	/**
	 * @return creation date of latest report
	 */
	public static Date checkLastReportDate() {
		Statement stmt;
		ResultSet rs;
		Date date = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(
					"SELECT Date FROM bitemedb.reports WHERE ReportType like '%Monthly%' order by Date desc");
			if (rs.next())
				date = rs.getDate(1);
		} catch (SQLException e) {
			// TODO: handle exception
		}
		return date;
	}

	/**
	 * @param m order: reportType,month,year,branch
	 * @return "fail" if report doesn't exists, report file otherwise.
	 */
	public static ServerResponse getMonthlyReport(ArrayList<String> m) {
		PreparedStatement stmt;
		Blob content;
		ServerResponse serverResponse = new ServerResponse("ReportContent");
		try {
			String query = "SELECT Content FROM bitemedb.reports WHERE MONTH(date)=? AND YEAR(DATE)=? and BranchName=? and ReportType like ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, m.get(1)); // month
			stmt.setString(2, m.get(2)); // year
			stmt.setString(3, m.get(3)); // branch
			stmt.setString(4, "%" + m.get(0) + "%"); // report type
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				content = rs.getBlob(1);
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		byte[] array;
		MyFile file = new MyFile("Blob");
		try {

			
			array = content.getBytes(1, (int) content.length());
			file.initArray(array.length);
			file.setMybytearray(array);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		serverResponse.setServerResponse(file);
		return serverResponse;
	}

	/**
	 * gets the LOGO of specific restaurant from DB
	 * 
	 * @param restaurant
	 * @return MyFile (Blob) - supplier LOGO
	 */
	public static ServerResponse getSupplierImage(String restaurant) {
		ServerResponse serverResponse = new ServerResponse("MyFile");
		try {
			String query = "SELECT Image FROM bitemedb.suppliers WHERE RestaurantName = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, restaurant);
			ResultSet rs = stmt.executeQuery();
			if (!rs.next()) {
				serverResponse.setMsg("NotExists");
				serverResponse.setServerResponse(null);
				return serverResponse;
			}
			Blob blob = rs.getBlob(1);
			MyFile file = new MyFile("Blob");
			byte[] array = blob.getBytes(1, (int) blob.length());
			file.initArray(array.length);
			file.setMybytearray(array);
			serverResponse.setServerResponse(file);
			serverResponse.setMsg("Success");

		} catch (SQLException e) {
			e.printStackTrace();
			serverResponse.setMsg("Failed");
			serverResponse.setServerResponse(null);
		}
		return serverResponse;
	}

	// SELECT AVG(Rating) FROM bitemedb.ratings where orderNumber IN (SELECT
	// OrderNumber FROM bitemedb.orders Where RestaurantName="Japanika" and
	// month(OrderTime) = 12);

	/**
	 * @param res
	 * @param month
	 * @param year
	 * @return average rating
	 */
	public static int getAvgRating(String restaurant, String month, String year) {
		try {
			String query = "SELECT AVG(Rating) FROM bitemedb.ratings where orderNumber IN (SELECT OrderNumber FROM bitemedb.orders Where RestaurantName=? and month(OrderTime) = ? and year(OrderTime) = ?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, restaurant);
			stmt.setString(2, month);
			stmt.setString(3, year);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				return rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * Query to reset the daily balance of each business customer.
	 */
	public static void resetDailyBalance() {
		String query = "UPDATE bitemedb.w4ccards SET DailyBalance = DailyBudget;";
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Query to reset the monthly balance of each business customer.
	 */
	public static void resetMonthlyBalance() {
		String query = "UPDATE bitemedb.w4ccards SET balance = MonthlyBudget;";
		Statement stmt;
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static boolean checkReportExists(String branch, String quarter, String year) {
		try {
			String query = "SELECT * FROM bitemedb.reports where ReportType like '%QuarterlyRevenue%'"
					+ " and BranchName=? and YEAR(Date) = ? and MONTH(Date) = ?";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, branch);
			stmt.setString(2, year);
			stmt.setInt(3, Integer.parseInt(quarter) * 3);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				return true;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * exe a query to get the customers status by his username
	 * 
	 * @param username
	 * @return
	 */
	public static ServerResponse checkCustomerStatus(String username) {
		PreparedStatement stmt;
		ServerResponse response = new ServerResponse();
		try {
			String query = "SELECT * FROM bitemedb.customers where username = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, username); // username
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int isBusiness = rs.getInt(3);
				int isPrivate = rs.getInt(5);
				if (isPrivate == 1 && isBusiness == 1) {
					response.setDataType("isBoth");
					return response;
				}
				// return new ServerResponse("isBoth");
				if (isPrivate == 1) {
					response.setDataType("isPrivate");
					return response;
				}
				// return new ServerResponse("isPrivate");
				if (isBusiness == 1) {
					response.setDataType("isBusiness");
					return response;
				}
				// return new ServerResponse("isBusiness");
			} else { // if not in customer table, it is user
				response.setDataType("isUser");
				return response;
				// return new ServerResponse("User");
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		response.setDataType("bug");
		return response;
	}

	/**
	 * creates a new customer for existing account in database, modifies: user
	 * status, adds him to customer table, and adds w4c values
	 * 
	 * @param values = userType, username, monthly bud, daily budget,credit card
	 *               number,employer's name.
	 * @return true if success, false if not
	 */
	@SuppressWarnings("resource")
	public static ServerResponse openNewAccount(ArrayList<String> values) {
		// userType: Private/Business/Both.
		String[] keys = { "CustomerID" };
		int customerId = 0;
		PreparedStatement stmt;
		String query;
		// avielCode:
		String customerType = values.get(0);

		// first, get customer's code for query 3 (check first to avoid adding invalid
		// customer):
		String empCode = "";
		if (customerType.equals("Business") || customerType.equals("Both")) {
			try {
				query = "SELECT EmployerCode FROM bitemedb.businesscustomer where EmployeCompanyName = ? AND IsApproved='1'";
				stmt = conn.prepareStatement(query);
				System.out.println(values.get(5));
				stmt.setString(1, values.get(5)); // customer's name
				ResultSet rs = stmt.executeQuery();
				if (rs.next())
					empCode = rs.getString(1);
				else {
					return new ServerResponse("unApprovedEmployer");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
			
		}
		// query 1: set new userType value on users table:
		try {
			query = "UPDATE bitemedb.users SET UserType = 'Customer' WHERE UserName = ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, values.get(1));
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} // end of first query

		// query 2 : update/insert info in Customers table:
		// first, check if need to update or to insert:
		String temp = null;
		int needUpdate = 0;
		try {
			query = "SELECT UserName FROM bitemedb.customers where UserName= ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, values.get(1)); // user name
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				temp = rs.getString(1);
			if (temp != null)
				needUpdate = 1;
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		if (needUpdate == 1) {
			try {
				switch (values.get(0)) {
				case "Private":
					query = "UPDATE bitemedb.customers SET isPrivate = ? WHERE UserName = ?";
					stmt = conn.prepareStatement(query, keys);
					stmt.setString(2, values.get(1)); // user name
					stmt.setInt(1, 1);
					break;
				case "Business":
					query = "UPDATE bitemedb.customers SET IsBusiness= ? WHERE UserName = ?";
					stmt = conn.prepareStatement(query, keys);
					stmt.setString(2, values.get(1)); // user name
					stmt.setInt(1, 1);
					break;
				case "Both":
					query = "UPDATE bitemedb.customers SET IsBusiness= ?, isPrivate = ? WHERE UserName = ?";
					stmt = conn.prepareStatement(query, keys);
					stmt.setInt(1, 1);
					stmt.setInt(2, 1);
					stmt.setString(3, values.get(1)); // user name
					break;
				default:
					break;
				}
				stmt.execute();
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					customerId = rs.getInt(1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			}
		}
		//
		else { // if need to insert
			try {
				query = "INSERT INTO bitemedb.customers (UserName, IsBusiness, isPrivate) values (?, ?, ?)";
				stmt = conn.prepareStatement(query, keys);
				stmt.setString(1, values.get(1));
				switch (values.get(0)) {
				case "Private":
					stmt.setInt(2, 0);
					stmt.setInt(3, 1);
					break;
				case "Business":
					stmt.setInt(2, 1);
					stmt.setInt(3, 0);
					break;
				case "Both":
					stmt.setInt(2, 1);
					stmt.setInt(3, 1);
					break;
				default:
					stmt.setInt(2, 0);
					stmt.setInt(3, 0);
					break;
				}
				stmt.executeUpdate();
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					customerId = rs.getInt(1);
					System.out.println(customerId);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return null;
			} // end of 2nd query
		}

		// generate random qrcode:
		Random rand = new Random();
		String qrCode = Integer.toString(rand.nextInt(1999999999));
		
		
		//get customer ID
		try {
			query = "SELECT CustomerID FROM bitemedb.customers where UserName= ?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, values.get(1)); // user name
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				customerId = rs.getInt(1);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		
		
		// query 3 : insert info in w4c: TBD

		try {
			query = "INSERT INTO bitemedb.w4ccards (CustomerID,EmployerCode, QRCode ,"
					+ " CreditCardNumber, MonthlyBudget, DailyBudget, Balance, DailyBalance) values (?, ?, ?, ?, ?, ?, ?, ?)";
			stmt = conn.prepareStatement(query);
			// Values = userType,username,monthly bud,daily budget,credit card number.
			stmt.setInt(1, customerId);
			stmt.setString(3, qrCode);
			stmt.setString(4, values.get(4));// credit card number
			stmt.setString(5, values.get(2)); // monthly budget
			String dailyBudget = values.get(3);
//			if(dailyBudget.equals("")) {
//				dailyBudget="0";
//				stmt.setString(8, values.get(2)); // daily balance = monthly budget
//			}
//			else {
//				stmt.setString(8, values.get(3)); // daily balance=budget				
//			}
			if(dailyBudget.equals("")) {
				dailyBudget = "0";
			}
			stmt.setString(8, dailyBudget);
			stmt.setString(6, dailyBudget); // daily budget
			stmt.setString(7, values.get(2)); // monthly balance = budget
			switch (customerType) {
			case "Private":
				stmt.setInt(6, 0); // daily budget
				stmt.setInt(8, 0); // daily balance
				stmt.setInt(7, 0); // monthly balance
				stmt.setInt(5, 0); // monthly budget
				stmt.setString(2, null);// no employer code
				break;
			case "Business":
				stmt.setString(2, empCode); // employers code
				break;
			case "Both": // has employer code:
				stmt.setString(2, empCode); // employers code
				break;
			default:
				stmt.setString(2, null);// no employer code
				break;
			}
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return new ServerResponse("Success");
	}

	/**
	 * @param restaurant
	 * @param month
	 * @param year
	 * @return avg preparation time of orders for the restaurant in that month.
	 */
	public static int getAvgPrepTime(String restaurant, String month, String year) {
		try {
			String query = "SELECT AVG(60*Hour(timediff(CustomerReceived, OrderReceived))+"
					+ "MINUTE(timediff(CustomerReceived, OrderReceived))) FROM bitemedb.orders WHERE OrderNumber "
					+ "IN (SELECT OrderNumber FROM bitemedb.orders Where RestaurantName=?"
					+ "and month(OrderTime) = ? and year(OrderTime) = ?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			stmt.setString(1, restaurant);
			stmt.setString(2, month);
			stmt.setString(3, year);
			ResultSet rs = stmt.executeQuery();
			if (rs.next())
				return rs.getInt(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * @param restaurantName
	 * @param month
	 * @param year
	 * @return total refunds for the restaurant on the given time.
	 */
	public static int getTotalRefunds(String restaurantName, String month, String year) {
		PreparedStatement stmt;
		String query;
		int num = 0;
		ResultSet rs;
		// SELECT OrderNumber FROM bitemedb.orders where RestaurantName=?
		try {
			query = "SELECT SUM(RefundAmount) FROM bitemedb.orders WHERE OrderNumber IN (SELECT OrderNumber FROM bitemedb.orders where RestaurantName=? AND MONTH(OrderReceived)=? AND YEAR(OrderReceived)=?)";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, restaurantName);
			stmt.setString(2, month);
			stmt.setString(3, year);
			rs = stmt.executeQuery();
			if (rs.next())
				num = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return num;
	}

	/**
	 * @param month
	 * @param year
	 */
	public static void createMonthlySuppliersReceipt(int month, int year) {
		ArrayList<String> resNames = new ArrayList<String>();
		int totalEarned = 0;
		float comission = 0;
		for (BranchName branch : BranchName.values())
			resNames.addAll(mysqlConnection.getRestaurantList(branch.toString()));
		// resNames stores all restaurant names now.
		// query 1: get comission and income for each restaurant, and store it in the
		// table:
		for (String res : resNames) {
			totalEarned = mysqlConnection.getEarnings(res, Integer.toString(month), Integer.toString(year));
			comission = mysqlConnection.getComission(res);
			// insert to table query:
			try {
				String query = "INSERT INTO bitemedb.supplierpayments (`SupplierName`, `Comission`, `TotalIncome`, `PaymentDate`) VALUES (?, ?, ?, ?)";

				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setString(1, res);
				stmt.setFloat(2, comission);
				stmt.setInt(3, totalEarned);
				stmt.setDate(4, Date.valueOf("" + year + "-" + month + "-02"));
				stmt.executeUpdate();
			} catch (SQLException e) {
				if (e instanceof SQLIntegrityConstraintViolationException)
					return; // already exists, all good !
				e.printStackTrace();
			}
			// end query
		}

	}

	/**
	 * Private method to get the comission of a certain restaurant.
	 * 
	 * @param restaurantName
	 * 
	 * @return float
	 */
	private static float getComission(String restaurantName) {
		PreparedStatement stmt;
		String query;
		int num = 0;
		ResultSet rs;
		try {
			query = "SELECT MonthlyComission FROM bitemedb.suppliers where RestaurantName=?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, restaurantName);
			rs = stmt.executeQuery();
			if (rs.next())
				num = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}
		return num;
	}

	/**
	 * returns receipt information for supplier in the chosen time.
	 * 
	 * @param values = restaurant name, month, year.
	 * @return arraylist inside the server response with: [total income, comission
	 *         %]
	 */
	public static ServerResponse getSupplierReceipt(ArrayList<String> values) {
		PreparedStatement stmt;
		String query;
		String restaurantName = values.get(0), month = values.get(1), year = values.get(2);
		ArrayList<String> output = new ArrayList<String>();
		int num = 0;
		ResultSet rs;
		try {
			query = "SELECT * FROM bitemedb.supplierpayments where SupplierName=? and month(PaymentDate) = ? and year(PaymentDate)=?";
			stmt = conn.prepareStatement(query);
			stmt.setString(1, restaurantName);
			stmt.setString(2, month);
			stmt.setString(3, year);
			rs = stmt.executeQuery();
			if (rs.next()) {
				output.add(rs.getString(3));
				output.add(Float.toString(rs.getFloat(2)));
			} else
				return null;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		ServerResponse response = new ServerResponse("ArrayList");
		response.setServerResponse(output);
		response.setMsg("success");
		return response;
	}

}
