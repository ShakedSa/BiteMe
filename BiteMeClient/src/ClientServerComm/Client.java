package ClientServerComm;

import java.io.IOException;

import Entities.ServerResponse;
import client.ClientGUI;
import client.ClientIF;
import ocsf.client.AbstractClient;

/**
 * Client logic of server-client communication.
 * 
 * @author Aviel Malayev
 * @author Natali Krief
 * @author Michael Ben Israel
 * @author Eden Ben Abu
 * @author Shaked Sabag
 * @version November 2021 (1.0)
 */
public class Client extends AbstractClient {

	/** Client gui logic */
	ClientIF clientUI;

	/**
	 * Constructor Creating new server-client communication logic.
	 * 
	 * @param host
	 * @param port
	 * @param clientUI
	 * 
	 * @throws IOException
	 */
	public Client(String host, int port, ClientIF clientUI) throws IOException {
		super(host, port);
		this.clientUI = clientUI;
		openConnection();
	}

	/**
	 * Overridden method from AbstractClient. Getting response from server and
	 * setting it in the client ui logic.
	 * 
	 * @param msg
	 */
	@SuppressWarnings("unchecked")
	public void handleMessageFromServer(Object msg) {
		if (msg instanceof String) {
			synchronized (ClientGUI.monitor) {
				ClientGUI.monitor.notifyAll();
			}
			return;
		}
		ServerResponse serverResponse = (ServerResponse) msg;
		switch (serverResponse.getDataType()) {
		case "User":
			clientUI.setUser(serverResponse);
			break;
		case "Restaurants":
			clientUI.setRestaurants(serverResponse);
			break;
		case "FavRestaurants":
			clientUI.setFavRestaurants(serverResponse); 
			break;
		case "menu":
			clientUI.setMenu(serverResponse);
			break;
		default:
			System.out.println("Null");
		}
		synchronized (ClientGUI.monitor) {
			ClientGUI.monitor.notifyAll();
		} 
	}

	/**
	 * Getting a message from the client ui and sending a request to the server.
	 * 
	 * @param message
	 */
	public void handleMessageFromClientUI(Object message) {
		try {
			sendToServer(message);
		} catch (IOException e) {
			this.clientUI.display("Could not send message to server.  Terminating client.");
			quit();
		}
	}

	/**
	 * Killing this client connection
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException localIOException) {
		}
		System.out.println("BYE");
		System.exit(0);
	}
}
