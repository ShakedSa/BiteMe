package client;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import ClientServerComm.Client;
import Entities.ServerResponse;

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
	ServerResponse user, ResRestaurants, ResFavRestaurants, ResRestaurantMenu, ResComponentsInProducts;
	HashMap<String, File> restaurants, favRestaurants;
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
	 */
	public void logout(String userName) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.addAll(Arrays.asList("logout", userName));
			client.handleMessageFromClientUI(arr);
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
			ArrayList<String> arr = new ArrayList<>();
			arr.add("getRestaurants");
			client.handleMessageFromClientUI(arr);
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
			ArrayList<String> arr = new ArrayList<>();
			arr.add("favRestaurants");
			client.handleMessageFromClientUI(arr);
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
			arr.add("menu");
			arr.add(restaurantName);
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
			arr.add("componentsInProduct");
			arr.add(restaurantName);
			arr.add(productName);
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
	public void setUser(ServerResponse user) {
		this.user = user;
	}

	public Object getResult() {
		return res;
	}

	public ServerResponse getUser() {
		return user;
	}

	public void setRestaurants(ServerResponse restaurants) {
		this.ResRestaurants = restaurants;
	}

	public ServerResponse getRestaurants() {
		return ResRestaurants;
	}

	public void setFavRestaurants(ServerResponse favRestaurants) {
		this.ResFavRestaurants = favRestaurants;

	}

	public ServerResponse getFavRestaurants() {
		return ResFavRestaurants;
	}

	public void setMenu(ServerResponse menu) {
		ResRestaurantMenu = menu;
	}

	public ServerResponse getMenu() {
		return ResRestaurantMenu;
	}

	public void setOptionalComponentsInProduct(ServerResponse optionalComponents) {
		ResComponentsInProducts = optionalComponents;
	}
	
	public ServerResponse getOptionalComponentsInProduct() {
		return ResComponentsInProducts;
	}
}
