package ClientServerComm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import client.ChatClient;

public class Client implements ChatIF {

	public static final int DEFAULT_PORT = 5555;
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

	public void accept() {
		try {
			BufferedReader fromConsole = new BufferedReader(new InputStreamReader(System.in));
			while (true) {
				String message = fromConsole.readLine();
				this.client.handleMessageFromClientUI(message);
			}
		} catch (Exception ex) {
			System.out.println("Unexpected error while reading from console!!");
		}
	}

	public void display(String message) {
	}

	public void getResultFromServer(String message) {
		this.res = message;
	}

	public static void main(String[] args) {
		String host = "";
		int port = DEFAULT_PORT;
		try {
			host = args[0];
		} catch (ArrayIndexOutOfBoundsException e) {
			host = "localhost";
		}
		Client chat = new Client(host, port);
		chat.accept();
	}

}
