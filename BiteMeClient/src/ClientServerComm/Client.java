package ClientServerComm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import Config.ReadPropertyFile;
import client.ChatClient;

public class Client implements ChatIF {
	public static final int DEFAULT_PORT = 5555;// Integer.parseInt(ReadPropertyFile.getInstance().getProp("DefaultPort"));
	public static final String DEFAULT_IP = "10.0.0.16";//ReadPropertyFile.getInstance().getProp("ClientDefaultIP");
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

//	public void accept(String orderAddress, String typeOfOrder) {
//		List<String> arr = new ArrayList<>();
//		try {
////			BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
//			while (true) {
////				String message = fromConsole.readLine();
////				arr.addAll(Arrays.asList(message.split(" ")));
//				if(typeOfOrder != null) {
//					arr.add("update");
//					arr.add(orderAddress);
//					arr.add(typeOfOrder);
//				}else {
//					arr.add(orderAddress);
//				}
//				this.client.handleMessageFromClientUI(arr);
//				arr.clear();
//			}
//
//		} catch (Exception ex) {
//			System.out.println("Unexpected error while reading from console!");
//		}
//	}

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

//	public static void main(String[] args) {
//		String host = DEFAULT_IP;
//		int port = DEFAULT_PORT;
//		try {
//			host = args[0];
//		} catch (ArrayIndexOutOfBoundsException e) {
//			host = DEFAULT_IP;
//		}
//		Client chat = new Client(host, port);
//		chat.accept();
//	}

}
