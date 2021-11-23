package ClientServerComm;

import java.io.IOException;
import java.util.ArrayList;

import Config.ReadPropertyFile;
import client.ChatClient;

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
public class Client implements ChatIF {

	/** Default port reading from default values file. */
	public static final int DEFAULT_PORT = Integer.parseInt(ReadPropertyFile.getInstance().getProp("DefaultPort"));
	/** Default ip reading from default values file. */
	public static final String DEFAULT_IP = ReadPropertyFile.getInstance().getProp("ClientDefaultIP");

	/** A client logic for client-server communication */
	ChatClient client;

	/** Storing response from the server. */
	public String res;

	/**
	 * Constructor. Creating a new client, on creation failed close the application
	 * and notify the user.
	 * 
	 * @param host
	 * @param port
	 */
	public Client(String host, int port) {
		try {
			this.client = new ChatClient(host, port, this);
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection! Terminating client.");
			System.exit(1);
		}
	}

	/**
	 * Sending a single order request to the server.
	 * 
	 * @param orderNumber
	 */
	public void getOrder(String orderNumber) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.add("getOrder");
			arr.add(orderNumber);
			this.client.handleMessageFromClientUI(arr);
		} catch (Exception ex) {
			System.out.println("Unexpected error while sending update command!");
			ex.printStackTrace();
		}
	}

	/**
	 * Updating the db using the server-client communication logic.
	 * 
	 * @param orderAddress
	 * @param typeOfOrder
	 */
	public void update(String orderNumber, String orderAddress, String typeOfOrder, String phoneNumber) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.add("update");
			arr.add(orderNumber);
			arr.add(orderAddress);
			arr.add(typeOfOrder);
			arr.add(phoneNumber);
			this.client.handleMessageFromClientUI(arr);
		} catch (Exception ex) {
			System.out.println("Unexpected error while sending update command!");
			ex.printStackTrace();
		}
	}

	/**
	 * Send a server request to get all the order table from db.
	 */
	public void show() {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.add("show");
			this.client.handleMessageFromClientUI(arr);
		} catch (Exception ex) {
			System.out.println("Unexpected error while sending show command!");
		}
	}

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
	public void getResultFromServer(String message) {
		this.res = message;
	}

}
