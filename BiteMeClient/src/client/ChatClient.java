package client;

import java.io.IOException;

import ClientServerComm.ChatIF;
import ocsf.client.AbstractClient;

public class ChatClient extends AbstractClient{

	ChatIF clientUI;
	public String res;

	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port);
		this.clientUI = clientUI;
		openConnection();
	}

	public void handleMessageFromServer(Object msg) {
		display((String)msg);
	}

	public void handleMessageFromClientUI(Object message) {
		try {
			sendToServer(message);
		} catch (IOException e) {
			this.clientUI.display("Could not send message to server.  Terminating client.");
			quit();
		}
	}

	public void quit() {
		try {
			closeConnection();
		} catch (IOException localIOException) {
		}
		System.out.println("BYE");
		System.exit(0);
	}

	public void display(String message) {
		System.out.println("DISPLAY: " + message);
	}

	public void getResultFromServer(String message) {
	}
}
