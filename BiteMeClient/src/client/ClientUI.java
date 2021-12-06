package client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import ClientServerComm.Client;
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
	User clientUser = null;
	HashMap<String, File> restaurants;
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
	 * */
	public void login(String userName, String password) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList("login", userName, password));
			client.handleMessageFromClientUI(arr);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Sending the server a logout request.
	 * 
	 * @param userName
	 * */
	public void logout(String userName) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList("logout", userName));
			client.handleMessageFromClientUI(arr);
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Sending the server a restaurants request.
	 * Getting all the restaurants from the db.
	 * */
	public void restaurantsRequest() {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.add("getRestaurants");
			client.handleMessageFromClientUI(arr);
		}catch(Exception e) {
			e.printStackTrace();
			return;
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
	public void getResultFromServer(Object message) {
		this.res = message;
	}

	/**
	 * Setting the user associated with this client.
	 * 
	 * @param User user
	 */
	public void setUser(User user) {
		this.clientUser = user;
	}

	public Object getResult() {
		return res;
	}
	
	public User getUser() {
		return clientUser;
	}
	
	public void setRestaurants(HashMap<String, File> restaurants) {
		this.restaurants = restaurants;
	}
	
	public HashMap<String, File> getRestaurants(){
		return restaurants;
	}

	/*---------------------------------------------------*/
	// Prototype
//	/**
//	 * Sending a single order request to the server.
//	 * 
//	 * @param orderNumber
//	 */
//	public void getOrder(String orderNumber) {
//		try {
//			ArrayList<String> arr = new ArrayList<>();
//			arr.add("getOrder");
//			arr.add(orderNumber);
//			this.client.handleMessageFromClientUI(arr);
//		} catch (Exception ex) {
//			System.out.println("Unexpected error while sending update command!");
//			ex.printStackTrace();
//		}
//	}
//
//	/**
//	 * Updating the db using the server-client communication logic.
//	 * 
//	 * @param orderAddress
//	 * @param typeOfOrder
//	 */
//	public void update(String orderNumber, String orderAddress, String typeOfOrder) {
//		try {
//			ArrayList<String> arr = new ArrayList<>();
//			arr.add("update");
//			arr.add(orderNumber);
//			arr.add(orderAddress);
//			arr.add(typeOfOrder);
//			this.client.handleMessageFromClientUI(arr);
//		} catch (Exception ex) {
//			System.out.println("Unexpected error while sending update command!");
//			ex.printStackTrace();
//		}
//	}
//
//	/**
//	 * Send a server request to get all the order table from db.
//	 */
//	public void show() {
//		try {
//			ArrayList<String> arr = new ArrayList<>();
//			arr.add("show");
//			this.client.handleMessageFromClientUI(arr);
//		} catch (Exception ex) {
//			System.out.println("Unexpected error while sending show command!");
//		}
//	}
}
