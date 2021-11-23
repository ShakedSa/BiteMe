package client;

import java.io.IOException;
import java.util.ArrayList;

import ClientServerComm.ChatIF;
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
public class ChatClient extends AbstractClient {

	/** Client gui logic */
	ChatIF clientUI;

	/**
	 * Constructor Creating new server-client communication logic.
	 * 
	 * @param host
	 * @param port
	 * @param clientUI
	 * 
	 * @throws IOException
	 */
	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
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
	public void handleMessageFromServer(Object msg) {
		clientUI.getResultFromServer((String) msg);
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
