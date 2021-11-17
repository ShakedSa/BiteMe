package ClientServerComm;

import java.io.IOException;
import java.util.ArrayList;

import Config.ReadPropertyFile;
import client.ChatClient;

public class Client implements ChatIF {
	public static final int DEFAULT_PORT = Integer.parseInt(ReadPropertyFile.getInstance().getProp("DefaultPort"));
	public static final String DEFAULT_IP = ReadPropertyFile.getInstance().getProp("ClientDefaultIP");
	ChatClient client;
	public String res;

	public Client(String host, int port) {
		try {
			this.client = new ChatClient(host, port, this);
		} catch (IOException exception) {
			System.out.println("Error: Can't setup connection! Terminating client.");

			System.exit(1);
		}
	}

	public void update(String orderAddress, String typeOfOrder) {
		try {
			ArrayList<String> arr = new ArrayList<>();
			arr.add("update");
			arr.add(orderAddress);
			arr.add(typeOfOrder);
		this.client.handleMessageFromClientUI(arr);
		} catch (Exception ex) {
			System.out.println("Unexpected error while sending update command!");
			ex.printStackTrace();
		}
	}

	public void show() {
		try {
		ArrayList<String> arr = new ArrayList<>();
		arr.add("show");
		this.client.handleMessageFromClientUI(arr);
		}catch(Exception ex) {
			System.out.println("Unexpected error while sending show command!");
		}
	}

	public void display(String message) {
	}

	public void getResultFromServer(String message) {
		this.res = message;
	}

}
