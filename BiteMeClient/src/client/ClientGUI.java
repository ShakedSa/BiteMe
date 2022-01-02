package client;

import java.io.IOException;

import Controls.Router;
import Controls.enterGUIController;
import Entities.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * BiteMe prototype GUI.
 * 
 * @author Aviel Malayev
 * @author Natali Krief
 * @author Michael Ben Israel
 * @author Eden Ben Abu
 * @author Shaked Sabag
 * @version November 2021 (1.0)
 */

public class ClientGUI extends Application {

	/** Client logic of server-client communication. */
	private static ClientUI client;
	private static Object monitor = new Object();
	private Router router;

	/**
	 * Overriding the default method in Application to run our GUI.
	 * 
	 * @param primaryStage
	 */
	@Override
	public void start(Stage primaryStage) {
		AnchorPane mainContainer;
		enterGUIController controller;
		router=Router.getInstance();
		router.setStage(primaryStage);
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/EnterGUI.fxml"));
			mainContainer = loader.load();
			controller = loader.getController();
			Scene mainScene = new Scene(mainContainer);
			mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
			primaryStage.setTitle("Connect to server");
			primaryStage.setScene(mainScene);
			primaryStage.setOnCloseRequest((EventHandler<WindowEvent>) new EventHandler<WindowEvent>() {
				/** Setting the 'X' button to close the application. */
				@Override
				public void handle(WindowEvent t) {
					/**
					 * If the user closed the application without logging out, logout the user
					 * automatically.
					 */
					if (client != null) {
						if (client.getUser() != null) {
							User user = (User) client.getUser().getServerResponse();
							if (user != null) {
								client.logout(user.getUserName());
							}
						}
					}
					Platform.exit();
					System.exit(0);
				}
			});
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Main for client application lunch.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
	/**
	 * @return the client
	 */
	public static ClientUI getClient() {
		return client;
	}
	/**
	 * @return the monitor
	 */
	public static Object getMonitor() {
		return monitor;
	}

	/**
	 * @param client the client to set
	 */
	public static void setClient(ClientUI client) {
		ClientGUI.client = client;
	}

	/**
	 * @param monitor the monitor to set
	 */
	public static void setMonitor(Object monitor) {
		ClientGUI.monitor = monitor;
	}
	
	

}
