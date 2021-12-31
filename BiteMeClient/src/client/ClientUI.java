
package client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import ClientServerComm.Client;
import Entities.Customer;
import Entities.MyFile;
import Entities.NewSupplier;
import Entities.Order;
import Entities.OrderDeliveryMethod;
import Entities.Product;
import Entities.ServerResponse;
import Entities.User;

/**
 * Logic of the client GUI.
 * 
 * @author Aviel Malayev
 * @author Natali Krief
 * @author Michael Ben Israel
 * @author Eden Ben Abu
 * @author Shaked Sabag
 * @version November 2021 (1.0)
 */
public class ClientUI implements ClientIF {

	/** A client logic for client-server communication */
	Client client;
	ServerResponse user=null, lastResponse=null;
	/** Storing response from the server. */
	Object res;

	/**
	 * Constructor. Creating a new client, on creation failed close the application
	 * and notify the user.
	 * @param host
	 * @param port
	 */
	public ClientUI(String host, int port) {
		try {
			this.client = new Client(host, port, this);
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection! Terminating client.");
			System.exit(1);
		}
	}

	/**
	 * Sending the server a login request.
	 * @param userName
	 * @param password
	 * 
	 */
	public void login(String userName, String password) {
		try {
			ServerResponse serverResponse = new ServerResponse("login");
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList(userName, password));
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Sending the server a logout request.
	 * @param userName
	 */
	public void logout(String userName) {
		try {
			ServerResponse serverResponse = new ServerResponse("logout");
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList(userName));
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Sending the server a restaurants request. Getting all the restaurants from
	 * the DB.
	 */
	public void restaurantsRequest() {
		try {
			ServerResponse serverResponse = new ServerResponse("Restaurants");
//			ArrayList<String> arr = new ArrayList<>();
//			arr.add("getRestaurants");
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Getting 6 favorite restaurants in the DB to display on home page.
	 */
	public void favRestaurantsRequest() {
		try {
//			ArrayList<String> arr = new ArrayList<>();
//			arr.add("favRestaurants");
			ServerResponse serverResponse = new ServerResponse("favRestaurants");
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Sending a query request from the server. adds a new supplier to the db
	 * @param restaurantName
	 */
	public void addNewSupplier(NewSupplier supplier) {
		try {
			ServerResponse serverResponse = new ServerResponse("newSupplier");
			serverResponse.setServerResponse(supplier);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Sending a query request from the server. Getting the menu of a certain
	 * restaurant.
	 * @param restaurantName
	 */
	public void getRestaurantMenu(String restaurantName) {
		try {
			ArrayList<String> arr = new ArrayList<>();
//			arr.add("menu");
			arr.add(restaurantName);
			ServerResponse serverResponse = new ServerResponse("menu");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Sending a query request from the server. get data of employers thats need an approvals.
	 * 
	 * @param restaurantName
	 */
	public void checkForApprovals() {
		try {
			ServerResponse serverResponse = new ServerResponse("employersApproval");
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Sending a query request from the server.
	 * get the data of the newly imported users

	 */
	public void searchForNewUsers() {
		try {
			ServerResponse serverResponse = new ServerResponse("getUsersCustomersInfo");
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	/**
	 * Sending a query request from the server.
	 * approve the employer with the given employer code
	 * 
	 * @param employerCode
	 */
	public void employerApproval(String employerCode) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.add(employerCode);
			ServerResponse serverResponse = new ServerResponse("employerApproval");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Sending a query request from the server, Getting the optional components for
	 * a certain product.
	 * @param restaurantName
	 * @param productName
	 */
	public void componentsInProduct(String restaurantName, String productName) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.add(restaurantName);
			arr.add(productName);
			ServerResponse serverResponse = new ServerResponse("componentsInProduct");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Sending the server a creatNewBusinessCustomer request.
	 * @param orderNumber

	 * Sending the server a search order request.
	 * 
	 * @param orderNumber
	 * 
	 */
	public void searchOrder(String orderNumber, String restaurantName) {
		try {
			ArrayList<String> arr = new ArrayList<>();
//			arr.addAll(Arrays.asList("searchOrder", orderNumber));
			arr.addAll(Arrays.asList(orderNumber, restaurantName));
			ServerResponse serverResponse = new ServerResponse("searchOrder");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**

	 * Sending the server a "creatNewBusinessCustomer" request.
	 * Updates the businessCustomers table
	 * @param orderNumber
	 */
	public void createNewBusinessCustomer(String hrUserName, String employerCode, String employerCompanyName) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList(hrUserName, employerCode, employerCompanyName));
			ServerResponse serverResponse = new ServerResponse("createNewBusinessCustomer");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Sending the server a request to select all the relevant CustomerAndbudget for HR approval.
	 *
	 * @param employerCompanyName
	 * 
	 */
	public void selectCustomerAndbudget(String employerCompanyName) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList(employerCompanyName));
			ServerResponse serverResponse = new ServerResponse("selectCustomerAndbudget");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Sending the server a request to select all CustomerAndbudget for HR approval.
	 * @param hrUserName,employerCompanyName
	 */
	public void approveCustomerAsBusiness(String employerCompanyName, String customerId) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList(employerCompanyName, customerId));
			ServerResponse serverResponse = new ServerResponse("approveCustomerAsBusiness");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void setRate(int orderNumber, int rate) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList(orderNumber + "", rate + ""));
			ServerResponse serverResponse = new ServerResponse("rate");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Sending the server a request to check if the business customer is already
	 * created.
	 * @param orderNumber
	 */
	public void checkIfBusinessCustomerExist(String hrUserName) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList(hrUserName));
			ServerResponse serverResponse = new ServerResponse("createNewBusinessCustomer");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void insertOrder(OrderDeliveryMethod orderToInsert) {
		try {
			ServerResponse serverResponse = new ServerResponse("InsertOrder");
			serverResponse.setServerResponse(orderToInsert);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * update order status and time (received & planned time)
	 * @param receivedOrReady
	 * @param orderNumber
	 * @param time
	 * @param status
	 */
	public void UpdateOrderStatus(String restaurantName, String receivedOrReady, String orderNumber, String time, String status) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList(restaurantName, receivedOrReady, orderNumber, time, status));
			ServerResponse serverResponse = new ServerResponse("updateOrderStatus");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * gets order details from orders table
	 * @param restaurantName
	 */
	public void getOrderInfo(String restaurantName) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.add(restaurantName);
			ServerResponse serverResponse = new ServerResponse("getOrderInfo");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * gets customer details - name and phone number
	 * @param deliveryNumber
	 */
	public void getCustomerInfo(String deliveryNumber) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList(deliveryNumber));
			ServerResponse serverResponse = new ServerResponse("getCustomerInfo");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * adding new item to menu
	 * @param product
	 */
	public void addItemToMenu(Product product) {
		try {
			ServerResponse serverResponse = new ServerResponse("addItem");
			serverResponse.setServerResponse(product);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * updates some details of specific item in menu
	 * @param product
	 */
	public void editItemInMenu(Product product) {
		try {
			ServerResponse serverResponse = new ServerResponse("editItemInMenu");
			serverResponse.setServerResponse(product);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	
	/**
	 * deletes an item from restaurant menu
	 * @param restaurant
	 * @param dishName
	 */
	public void deleteItemFromMenu(String restaurant, String dishName) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList(restaurant, dishName));
			ServerResponse serverResponse = new ServerResponse("deleteItemFromMenu");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * gets restaurant LOGO from DB
	 * @param restaurant
	 */
	public void getSupplierImage(String restaurant) {
		ArrayList<String> arr = new ArrayList<String>();
		try {
			arr.addAll(Arrays.asList(restaurant));
			ServerResponse serverResponse = new ServerResponse("getSupplierImage");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}																											   		

	/**
	 * A must implemented method from ChatIF interface.
	 * @param message
	 */
	public void display(String message) {
	}

	/**
	 * Setting the message from the server to res.
	 * @param message
	 */
	public void getResultFromServer(Object message) {
		this.res = message;
	}

	/**
	 * Setting the user associated with this client.
	 * @param User user
	 */
	public void setUser(ServerResponse user) {
		this.user = user;
	}

	public Object getResult() {
		return res;
	}

	public ServerResponse getUser() {
		return user;
	}

	/**
	 * @param pdfToUpload
	 * @param Month
	 * @param Year
	 * @param ReportType
	 * sends a PDF quarterly report from manager's file system to SQL
	 */ 
	public void sendReport(File pdfToUpload, String Month, String Year, String ReportType) {
		ServerResponse serverResponse = new ServerResponse("uploadQuarterlyReport");
		MyFile msg = new MyFile(Month + " " + Year);
		// extract user:
		User user = (User) this.user.getServerResponse();
		msg.getDescription().add(ReportType);
		msg.getDescription().add(Month);
		msg.getDescription().add(Year);
		msg.getDescription().add(user.getMainBranch().toString());
		try {

			byte[] mybytearray = new byte[(int) pdfToUpload.length()];
			FileInputStream fis = new FileInputStream(pdfToUpload);
			BufferedInputStream bis = new BufferedInputStream(fis);

			msg.initArray(mybytearray.length);
			msg.setSize(mybytearray.length);

			bis.read(msg.getMybytearray(), 0, mybytearray.length);
			serverResponse.setServerResponse(msg);
			client.handleMessageFromClientUI(serverResponse);
			fis.close();
			bis.close();
		} catch (Exception e) {
			System.out.println("Error sending (Files msg) to Server");
		}

	}
	
	/**
	 * sending a view request (is exists - therefore also used as a checker)
	 * to the relevant quarter pdf report 
	 * @param quarter
	 * @param Year
	 * @param branch
	 */
	public void viewORcheckQuarterReport(String quarter, String Year, String branch) {
		ArrayList<String> arr = new ArrayList<String>();
		try {
			arr.addAll(Arrays.asList(quarter, Year, branch));
			ServerResponse serverResponse = new ServerResponse("viewQuarterReport");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * sending a view request (is exists - therefore also used as a checker)
	 * to the relevant quarter revenue report 
	 * @param quarter
	 * @param Year
	 * @param branch
	 */
	public void viewORcheckRevenueQuarterReport(String quarter, String Year, String branch) {
		ArrayList<String> arr = new ArrayList<String>();
		try {
			arr.addAll(Arrays.asList(quarter, Year, branch));
			ServerResponse serverResponse = new ServerResponse("viewRevenueQuarterReport");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * function to CheckQuarterReport before enabling user to viewQuarterReport
	 * @param quarter
	 * @param Year
	 * @param branch
	 */
	public void CheckQuarterReport(String quarter, String Year, String branch) {
		ArrayList<String> arr = new ArrayList<String>();
		try {
			arr.addAll(Arrays.asList(quarter, Year, branch));
			ServerResponse serverResponse = new ServerResponse("CheckQuarterReport");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void checkUserNameAccountType(String userName) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList(userName));
			ServerResponse serverResponse = new ServerResponse("checkUserNameAccountType");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void checkUserNameForSupplier(String userName) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList(userName));
			ServerResponse serverResponse = new ServerResponse("checkuserName");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void checkUserNameIsClient(String userName) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList(userName));
			ServerResponse serverResponse = new ServerResponse("checkuserNameIsClient");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void changeClientPerrmisions(String userName, String newStatus) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList(userName, newStatus));
			ServerResponse serverResponse = new ServerResponse("changeClientPerrmisions");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return; 
		}
	}

	public void getCustomersOrder(Customer customer) {
		try {
			ServerResponse serverResponse = new ServerResponse("customersOrders");
			serverResponse.setServerResponse(customer);
			client.handleMessageFromClientUI(serverResponse);
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	public void updateOrderReceived(Order order) {
		try {
			ServerResponse serverResponse = new ServerResponse("UpdateorderReceived");
			serverResponse.setServerResponse(order);
			client.handleMessageFromClientUI(serverResponse);
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}

	@Override
	public void setLastResponse(ServerResponse serverResponse) {
		lastResponse = serverResponse;

	}

	public ServerResponse getLastResponse() {
		return lastResponse;
	}

	/**
	 * sends a report data request to server SQL
	 * @param arr order : reportType,month,year,branch
	 */
	public void getReport(ArrayList<String> arr) {
		ServerResponse serverResponse = new ServerResponse("getReport");
		serverResponse.setServerResponse(arr);
		client.handleMessageFromClientUI(serverResponse);
	}
	
	public void getRefunds(Customer cutsomer) {
		ServerResponse serverResponse = new ServerResponse("getRefunds");
		serverResponse.setServerResponse(cutsomer);
		client.handleMessageFromClientUI(serverResponse);
	}

	/**
	 * request from server to create quarterly revenue report for given:
	 * @param quarter
	 * @param year
	 * @param branch
	 */
	public void createQuarterlyRevenueReport(String quarter, String year, String branch) {
		ArrayList<String> arr = new ArrayList<>();
		arr.add(quarter);
		arr.add(year);
		arr.add(branch); // arr = quarter,year,branch
		ServerResponse serverResponse = new ServerResponse("createQuarterlyRevenueReport");
		serverResponse.setServerResponse(arr);
		client.handleMessageFromClientUI(serverResponse);
	}

	public void checkCustomerStatus(String username) {
		ArrayList<String> arr = new ArrayList<>();
		arr.add(username);
		ServerResponse serverResponse = new ServerResponse("checkCustomerStatus");
		serverResponse.setServerResponse(arr);
		client.handleMessageFromClientUI(serverResponse);
	}

	/**
	 * request from server to create new account with the following information:
	 * @param values = userType,username,monthly bud,daily budget,credit card number,employer's name.
	 */
	public void openNewAccount(ArrayList<String> values) {
		
		ServerResponse serverResponse = new ServerResponse("openNewAccount");
		serverResponse.setServerResponse(values);
		client.handleMessageFromClientUI(serverResponse);
	}

}