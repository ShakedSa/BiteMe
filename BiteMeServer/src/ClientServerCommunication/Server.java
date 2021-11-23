package ClientServerCommunication;

import java.util.ArrayList;

import Config.ReadPropertyFile;
import JDBC.mysqlConnection;
import gui.ServerGUIController;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
public class Server extends AbstractServer {
	private ServerGUIController controller;
	public static final int DEFAULT_PORT = 5555;
	public Server(int port) {
		super(port);
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		controller.setMessage("Msg recieved:" + msg);
		@SuppressWarnings("unchecked")
		ArrayList<String> m = (ArrayList<String>) msg;
		String text;
		switch(m.get(0)){
		case "show":
			sendToAllClients(mysqlConnection.printOrders());
			break;
		case "update":
			 text = mysqlConnection.updateOrderInfo(m.get(1), m.get(2), m.get(3), m.get(4));
			controller.setMessage(text);
			sendToAllClients(text);
			break;
		case "getOrder":
			text = mysqlConnection.getOrderInfo(m.get(1));
			controller.setMessage(text);
			sendToAllClients(text);
			break;
		default:
			sendToAllClients("default");
			break;
		}
		
	}

	protected void serverStarted() {
		controller.setMessage("Server listening for connections on port " + getPort());
	}

	protected void serverStopped() {
		controller.setMessage("Server has stopped listening for connections.");
	}

	public String showConnectionInfo() {
		if(this.getClientConnections().length>0)
		{
			String str;
			String[] connectionInfo= this.getClientConnections()[0].toString().split(" ");
			str= "IP: "+ connectionInfo[0] + "\n\tHost: "+  connectionInfo[1] + "\n\tStatus: Connected.";
			return str;
		}
			//return this.getClientConnections()[0].toString()+ "Status: "+this.getClientConnections()[0].isAlive();
		else return "No Connections !";
	}
	
	public void setController(ServerGUIController controller) {
		this.controller = controller;
	}
	
	
}
