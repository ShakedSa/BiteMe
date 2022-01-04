
package ClientServerCommunication;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.ArrayList;

import Entities.Customer;
import Entities.MyFile;
import Entities.NewSupplier;
import Entities.Order;
import Entities.OrderDeliveryMethod;
import Entities.Product;
import Entities.ServerResponse;
import Entities.User;
import Enums.BranchName;
import JDBC.mysqlConnection;
import ServerUtils.DailyThread;
import ServerUtils.reportsHandler;
import gui.ServerGUIController;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * Server Logic
 * 
 * @author Aviel Malayev
 * @author Natali Krief
 * @author Michael Ben Israel
 * @author Eden Ben Abu
 * @author Shaked Sabag
 * @version November 2021 (1.0)
 */

public class Server extends AbstractServer {

	/** Server gui controller for message handling between gui and logic */
	private ServerGUIController controller;
	private DailyThread dailyThread;
	public static final int DEFAULT_PORT = 5555;

	/**
	 * Constructor. Building new server logic object.
	 * 
	 * @param port
	 */
	public Server(int port) {
		super(port);
	}

	/**
	 * Overridden method from AbstractServer. Handling message received from a
	 * client. At this moment can handle message from only 1 single client.
	 * @param msg
	 * @param client
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		controller.setMessage("Msg recieved:" + msg);
		ServerResponse serverResponse = (ServerResponse) msg;
		ArrayList<String> m;
		switch (serverResponse.getDataType()) {
		case "login":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.login(m.get(0), m.get(1)), client);
			break;
		case "logout":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.logout(m.get(0)), client);
			break;
		case "Restaurants":
			this.sendToClient(mysqlConnection.getRestaurants((User)serverResponse.getServerResponse()), client);
			break;
		case "favRestaurants":
			this.sendToClient(mysqlConnection.favRestaurants(), client);
			break;
		case "menu":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.getMenuToOrder(m.get(0)), client);
			break;
		case "componentsInProduct":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.getComponentsInProduct(m.get(0), m.get(1)), client);
			break;
		case "checkUser":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.checkUsername(m.get(0)), client);
			break;
		case "checkUserNameAccountType":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.checkUserNameAccountType(m.get(0)), client);
			break;
		case "checkuserNameIsClient":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.checkUserNameIsClient(m.get(0)), client);
			break;
		case "changeClientPerrmisions":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			mysqlConnection.changeClientPerrmisions(m.get(0), m.get(1));
			break;
		case "employersApproval":
			this.sendToClient(mysqlConnection.getEmployersForApproval(), client);
			break;
		case "createNewBusinessCustomer":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.createNewBusinessCustomer(m.get(0), m.get(1), m.get(2)), client);
			break;
		case "selectCustomerAndbudget":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.selectCustomerAndbudget(m.get(0)), client);
			break;
		case "approveCustomerAsBusiness":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.approveCustomerAsBusiness(m.get(0), m.get(1)), client);
			break;
		case "updateOrderStatus":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.updateOrderStatus(m.get(0), m.get(1), m.get(2), m.get(3), m.get(4)), client);
			break;
		case "getOrderInfo":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.getOrderInfo(m.get(0)), client);
			break;
		case "getCustomerInfo":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.getCustomerInfo(m.get(0)), client);
			break;
		case "employerApproval":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			mysqlConnection.approveEmployer(m.get(0));
			break;
		case "rate":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.setRate(m.get(0), m.get(1)), client);
			break;
		case "newSupplier":
			this.sendToClient(mysqlConnection.addNewSupplier((NewSupplier) serverResponse.getServerResponse()), client);
			break;
		case "InsertOrder":
			this.sendToClient(
					mysqlConnection.insertOrderDelivery((OrderDeliveryMethod) serverResponse.getServerResponse()),
					client);
			break;
		case "addItem":
			this.sendToClient(mysqlConnection.addItemToMenu((Product) serverResponse.getServerResponse()), client);
			break;
		case "editItemInMenu":
			this.sendToClient(mysqlConnection.editItemInMenu((Product) serverResponse.getServerResponse()), client);
			break;
		case "customersOrders":
			this.sendToClient(mysqlConnection.customersOrder((Customer)serverResponse.getServerResponse()), client);
			break;
		case "UpdateorderReceived":
			this.sendToClient(mysqlConnection.updateOrderReceived((Order)serverResponse.getServerResponse()), client);
			break;
		case "deleteItemFromMenu":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.deleteItemFromMenu(m.get(0), m.get(1)), client);
			break;
		case "viewQuarterReport":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.viewORcheckQuarterReport(m.get(0), m.get(1), m.get(2)), client);
			break;
		case "viewRevenueQuarterReport":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.viewORcheckRevenueQuarterReport(m.get(0), m.get(1), m.get(2)), client);
			break;
		case "getReport":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.getMonthlyReport(m),client);
			break;
		case "getSupplierImage":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.getSupplierImage(m.get(0)), client);
			break;
		case "uploadQuarterlyReport":
			MyFile message = (MyFile) serverResponse.getServerResponse();
			controller.setMessage("File message received: PDF Report " + message.getFileName() + " from " + client);
			try {
			InputStream is = new ByteArrayInputStream(((MyFile) message).getMybytearray());
			mysqlConnection.updateFile(is, message.getDescription());
			break;
		} catch (Exception e) {
			e.printStackTrace();
			controller.setMessage("Error while handling files in Server");
		}
			break;
		case "getRefunds":
			this.sendToClient(mysqlConnection.getRefund((Customer)serverResponse.getServerResponse()), client);
			break;
		case "createQuarterlyRevenueReport":// arr= quarter,year,branch
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(reportsHandler.quarterlyRevenueReportPdf(m.get(2), m.get(0), m.get(1)),client);
			break;
		case "importedUsers":
			this.sendToClient(mysqlConnection.getImportedUsers((BranchName)serverResponse.getServerResponse()), client);
			break;
		case "getUsersCustomersInfo":
			this.sendToClient(mysqlConnection.getAllUsersAndCustomers((BranchName)serverResponse.getServerResponse()), client);
			break;
		case "checkCustomerStatus":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.checkCustomerStatus(m.get(0)), client);
			break;
		case "openNewAccount":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.openNewAccount(m), client);
			break;
		case "getSupplierReceipt":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection. getSupplierReceipt(m), client);
			break;
		case "ChangeBranch":
			m = (ArrayList<String>) serverResponse.getServerResponse();
			this.sendToClient(mysqlConnection.changeBranch(m.get(0), m.get(1)), client);
			break;
		case "GetDeliveryInfo":
			this.sendToClient(mysqlConnection.getDeliveryInfo((int)serverResponse.getServerResponse()), client);
		default:
			sendToClient("default", client);
			break;
		}

	}

	/**
	 * sendToClient method will send the received msg to a specific client
	 * 
	 * @param msg    - object to send
	 * @param client - receiving client
	 */
	public void sendToClient(Object msg, ConnectionToClient client) {
		try {
			client.sendToClient(msg);
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println(" Error sending msg to client !");
		}
	}

	/**
	 * Overridden method from AbstractServer. Gets invoke when the server starts and
	 * sending a message to the gui.
	 */
	protected void serverStarted() {
		mysqlConnection.logoutAll();
		controller.setMessage("Server listening for connections on port " + getPort());
		dailyThread = new DailyThread();
		Thread t = new Thread(dailyThread);
		t.start();
		int date[]=reportsHandler.getLastReportDate();
		int year=date[0],month=date[1];
		if(year!=LocalDate.now().getYear() || month!=(LocalDate.now().getMonthValue()))
		{//report updates needed for last report month+1:
			//check if new year:
			if(LocalDate.now().getMonthValue()==1 && month==12)
				return;
		//(no need next ones since server was off and no other data was collected)
			reportsHandler.createAllReports(month+1, year);
			controller.setMessage("Monthly reports were created.");
		}
	}

	/**
	 * Overridden method from AbstractServer. Gets invoke when the server stopped
	 * and sending a message to the gui.
	 */
	protected void serverStopped() {
		controller.setMessage("Server has stopped listening for connections.");
	}

	/**
	 * Method to get the connected client from the server.
	 * 
	 * @return str
	 */
	public String showConnectionInfo() {
		if (this.getClientConnections().length > 0) {
			String str;
			String[] connectionInfo = this.getClientConnections()[0].toString().split(" ");
			str = "IP: " + connectionInfo[0] + "\n\tHost: " + connectionInfo[1] + "\n\tStatus: Connected.";
			return str;
		} else
			return "No Connections !";
	}

	/**
	 * Setting a ServerGUIController.
	 * 
	 * @param controller
	 */
	public void setController(ServerGUIController controller) {
		this.controller = controller;
	}

}