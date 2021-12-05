package gui;

import java.io.IOException;

import ClientServerComm.Client;
import Config.ReadPropertyFile;
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
	/** Default port reading from default values file. */
	public static final int DEFAULT_PORT = Integer.parseInt(ReadPropertyFile.getInstance().getProp("DefaultPort"));
	/** Default ip reading from default values file. */
	public static final String DEFAULT_IP = ReadPropertyFile.getInstance().getProp("ClientDefaultIP");
	/** Client logic of server-client communication. */
	public static Client client;

	/**
	 * Overriding the default method in Application to run our GUI.
	 * 
	 * @param primaryStage
	 */
	@Override
	public void start(Stage primaryStage) {
		AnchorPane mainContainer;
		prototypeEnterGUIController controller;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("prototypeEnterGUI.fxml"));
			mainContainer = loader.load();
			controller = loader.getController();
			controller.setStage(primaryStage);
			Scene mainScene = new Scene(mainContainer);
			primaryStage.setScene(mainScene);
			primaryStage.setOnCloseRequest((EventHandler<WindowEvent>) new EventHandler<WindowEvent>() {
				/** Setting the 'X' button to close the application. */
				@Override
				public void handle(WindowEvent t) {
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
}
