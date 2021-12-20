			  
package ClientServerCommunication;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import Entities.MyFile;
import Entities.NewSupplier;
import Entities.NewUser;
import Entities.OrderDeliveryMethod;
import Entities.Product;
import JDBC.mysqlConnection;
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
	 * 
	 * @param msg
	 * @param client
	 */
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if(msg instanceof MyFile) // handle upload pdf file to sql
		{
			  MyFile message = ((MyFile) msg);
			  controller.setMessage("File message received: PDF Report " + message.getFileName() + " from " + client);
			try {
				InputStream is = new ByteArrayInputStream(((MyFile)msg).getMybytearray());
				mysqlConnection.updateFile(is,message.getFileName(),message.getDescription());
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("Error while handling files in Server");
			}
			return;
		}
		
		if(msg instanceof OrderDeliveryMethod) {
			try {
			mysqlConnection.insertOrderDelivery((OrderDeliveryMethod)msg);
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("Error while handling message in server");
			}
			return;
		}
		
		if(msg instanceof NewUser) {
			try {
			NewSupplier supplier = ((NewUser)msg).getSupplier();
			//add supplier to users table
			mysqlConnection.addNewUser((NewUser)msg);
			//add supplier to suppliers table
			mysqlConnection.addNewSupplier(supplier);
			}catch(Exception e) {
				e.printStackTrace();
				System.out.println("Error while handling message in server");
			}
			return;
		}
		if(msg instanceof Product) {
			try {
				this.sendToClient(mysqlConnection.addItemToMenu((Product)msg), client);
				//this.sendToClient(mysqlConnection.editItemInMenu((Product)msg), client);
			}catch(Exception e) {
				e.printStackTrace();
			}
			return;
		}

		
		controller.setMessage("Msg recieved:" + msg);
		@SuppressWarnings("unchecked")
		ArrayList<String> m = (ArrayList<String>) msg;
						
		switch (m.get(0)) {
		case "login":
			this.sendToClient(mysqlConnection.login(m.get(1), m.get(2)), client);
			break;
		case "logout":
			this.sendToClient(mysqlConnection.logout(m.get(1)), client);
			break;
		case "getRestaurants":
			this.sendToClient(mysqlConnection.getRestaurants(), client);
			break;
		case "favRestaurants":
			this.sendToClient(mysqlConnection.favRestaurants(), client);
			break;
		case "menu":
			this.sendToClient(mysqlConnection.getMenuToOrder(m.get(1)), client);
			break;
		case "componentsInProduct":
			this.sendToClient(mysqlConnection.getComponentsInProduct(m.get(1), m.get(2)), client);
			break;
		case "searchOrder":
			this.sendToClient(mysqlConnection.searchOrder(m.get(1)), client);
			break;
		case "checkUser":
			this.sendToClient(mysqlConnection.checkUsername(m.get(1)), client);
			break;
		case "checkID":
			this.sendToClient(mysqlConnection.checkID(m.get(1)), client);
			break;
		case "updateUser":
			mysqlConnection.updateUserInformation(m.get(1), m.get(2), m.get(3));
			break;
		case "employersApproval":
			mysqlConnection.getEmployersForApproval();
			break;
		case "createNewBusinessCustomer":
			this.sendToClient(mysqlConnection.createNewBusinessCustomer(m.get(1),m.get(2),m.get(3)),client);
			break;
		case "selectCustomerAndbudget":
			this.sendToClient(mysqlConnection.selectCustomerAndbudget(m.get(1)),client);
			break;
		case "approveCustomerAsBusiness":
			this.sendToClient(mysqlConnection.approveCustomerAsBusiness(m.get(1),m.get(2)), client);
			break;
		case "updateOrderStatus":
			this.sendToClient(mysqlConnection.updateOrderStatus(m.get(1), m.get(2), m.get(3), m.get(4)), client);
			break;
		case "getOrderInfo":
			this.sendToClient(mysqlConnection.getOrderInfo(m.get(1)), client);
			break;
		case "getCustomerInfo":
			this.sendToClient(mysqlConnection.getCustomerInfo(m.get(1)), client);
			break;
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