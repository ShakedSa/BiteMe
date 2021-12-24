
package client;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import ClientServerComm.Client;
import Entities.Component;
import Entities.MyFile;
import Entities.NewSupplier;
import Entities.NewUser;
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
	ServerResponse user, lastResponse;
	/** Storing response from the server. */
	Object res;

	/**
	 * Constructor. Creating a new client, on creation failed close the application
	 * and notify the user.
	 * 
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
	 * 
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
	 * 
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
	 * the db.
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
	 * Getting 6 favorite restaurants in the db to display on home page.
	 * 
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
	 * Sending a query request from the server. update a user information
	 * 
	 * @param restaurantName
	 */
	public void updateUserInfo(String userName, String userType, String status) {
		try {
			ArrayList<String> arr = new ArrayList<>();
//			arr.add("updateUser");
			arr.add(userName);
			arr.add(userType);
			arr.add(status);
			ServerResponse serverResponse = new ServerResponse("updateUser");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Sending a query request from the server. add new supplier to the db
	 * 
	 * @param restaurantName
	 */
	public void addNewSupplier(NewUser supplier) {
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
	 * 
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
	 * Sending a query request from the server. check for employers approvals
	 * restaurant.
	 * 
	 * @param restaurantName
	 */
	public void checkForApprovals() {
		try {
//			ArrayList<String> arr = new ArrayList<>();
//			arr.add("employersApproval");
			ServerResponse serverResponse = new ServerResponse("employersApproval");
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Sending a query request from the server. approve employer in the db
	 * restaurant.
	 * 
	 * @param restaurantName
	 */
	public void employerApproval(String employerCode) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.add("employerApproval");
			arr.add(employerCode);
			client.handleMessageFromClientUI(arr);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Sending a query request from the server, Getting the optional components for
	 * a certain product.
	 * 
	 * @param restaurantName
	 * @param productName
	 */
	public void componentsInProduct(String restaurantName, String productName) {
		try {
			ArrayList<String> arr = new ArrayList<>();
//			arr.add("componentsInProduct");
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
	 * Sending the server a search order request.
	 * 
	 * @param orderNumber
	 * 
	 */
	public void searchOrder(String orderNumber) {
		try {
			ArrayList<String> arr = new ArrayList<>();
//			arr.addAll(Arrays.asList("searchOrder", orderNumber));
			arr.add(orderNumber);
			ServerResponse serverResponse = new ServerResponse("searchOrder");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Sending the server a creatNewBusinessCustomer request.
	 *
	 * @param orderNumber
	 * 
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
	 * Sending the server a request to select all CustomerAndbudget for HR approval.
	 *
	 * @param hrUserName,employerCompanyName
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
	 *
	 * @param hrUserName,employerCompanyName
	 * 
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
	 * 
	 * @param orderNumber
	 * 
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
	 * supplier update order status
	 * 
	 * @param receivedOrReady
	 * @param orderNumber
	 * @param time
	 * @param status
	 */
	public void UpdateOrderStatus(String receivedOrReady, String orderNumber, String time, String status) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList(receivedOrReady, orderNumber, time, status));
			ServerResponse serverResponse = new ServerResponse("updateOrderStatus");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * get order details
	 * 
	 * @param orderNumber
	 */
	public void getOrderInfo(String orderNumber) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList(orderNumber));
			ServerResponse serverResponse = new ServerResponse("getOrderInfo");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

	}

	/**
	 * get customer details
	 * 
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
	 * supplier add new item to menu
	 * 
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
	 * supplier update item in menu
	 * 
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

//	/**
//	 * @return the searchOrder
//	 */
//	public ServerResponse getSearchOrder() {
//		return SearchOrder;
//	}
//
//	/**
//	 * @param searchOrder the searchOrder to set
//	 */
//	public void setSearchOrder(ServerResponse searchOrder) {
//		SearchOrder = searchOrder;
//	}

	/**
	 * A must implemented method from ChatIF interface.
	 * 
	 * @param message
	 */
	public void display(String message) {
	}

	/**
	 * Setting the message from the server to res.
	 * 
	 * @param message
	 */
	public void getResultFromServer(Object message) {
		this.res = message;
	}

	/**
	 * Setting the user associated with this client.
	 * 
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

	public void sendReport(File pdfToUpload, String Month, String Year, String ReportType) {
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
			client.handleMessageFromClientUI(msg);
			fis.close();
			bis.close();
		} catch (Exception e) {
			System.out.println("Error sending (Files msg) to Server");
		}

	}

	public void viewORcheckQuarterReport(String quarter, String Year, String branch, String func) {
		ArrayList<String> arr = new ArrayList<String>();
		try {
			arr.addAll(Arrays.asList(quarter, Year, branch,func));
			ServerResponse serverResponse = new ServerResponse("viewQuarterReport");
			serverResponse.setServerResponse(arr);
			client.handleMessageFromClientUI(serverResponse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//function to CheckQuarterReport before enabling user to viewQuarterReport
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

	public void checkUserName(String userName) {
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

	@Override
	public void setLastResponse(ServerResponse serverResponse) {
		lastResponse = serverResponse;

	}

	public ServerResponse getLastResponse() {
		return lastResponse;
	}

}