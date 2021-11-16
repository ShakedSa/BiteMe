package ClientServerCommunication;

import java.util.ArrayList;

import Config.ReadPropertyFile;
import JDBC.mysqlConnection;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;
public class Server extends AbstractServer {

	//private mysqlConnection jdbc=new mysqlConnection();
//	public static final int DEFAULT_PORT = Integer.parseInt(ReadPropertyFile.getInstance().getProp("DefaultPort"));
	public static final int DEFAULT_PORT = 5555;
	public Server(int port) {
		super(port);
	}

	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		System.out.println("Msg recieved:" + msg);
		@SuppressWarnings("unchecked")
		ArrayList<String> m = (ArrayList<String>) msg;
		switch(m.get(0)){
		case "show":
			sendToAllClients(mysqlConnection.printOrders());
			break;
		case "update":
			sendToAllClients(mysqlConnection.updateOrderInfo(m.get(1), m.get(2)));
				
			break;
		case "server":
			showConnectionInfo();
		default:
			sendToAllClients("default");
			break;
		}
	}

	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

	protected void showConnectionInfo() {
		System.out.println(this.getClientConnections()[0].toString()+ "Status: "+this.getClientConnections()[0].isAlive());
	}
	public static void main(String[] args) {
		int port = 0;
		try {
			port = Integer.parseInt(args[0]);
		} catch (Throwable t) {
			port = DEFAULT_PORT;
		}

		Server sv = new Server(port);
		try {
			sv.listen();
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!!");
		}
	}
}
