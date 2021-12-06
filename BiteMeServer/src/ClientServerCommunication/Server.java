package ClientServerCommunication;

import java.util.ArrayList;
import Entities.User;
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
		controller.setMessage("Msg recieved:" + msg);
		@SuppressWarnings("unchecked")
		ArrayList<String> m = (ArrayList<String>) msg;
		switch (m.get(0)) {
		case "login":
			User user = mysqlConnection.login(m.get(1), m.get(2));
			this.sendToClient(user, client);
			break;
		case "logout":
			this.sendToClient(mysqlConnection.logout(m.get(1)), client);
			break;
		default:
			sendToClient("default",client);
			break;
		}

	}

	  /**
	   * sendToClient method will send the received msg to a specific client
	 * @param msg - object to send
	 * @param client - receiving client
	 */
	public void sendToClient(Object msg, ConnectionToClient client) {
	      try
	      {		
	    	  client.sendToClient(msg);
	      }
	      catch (Exception ex) {System.out.println(" Error sending msg to client !");}
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
