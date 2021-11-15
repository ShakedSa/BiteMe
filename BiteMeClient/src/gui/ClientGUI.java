package gui;

import java.io.IOException;

import ClientServerComm.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ClientGUI extends Application {
	public static final int DEFAULT_PORT = 5555;// Integer.parseInt(ReadPropertyFile.getInstance().getProp("DefaultPort"));
//	public static final String DEFAULT_IP = ReadPropertyFile.getInstance().getProp("ClientDefaultIP");
	public static final String DEFAULT_IP = "192.168.1.128";
	
	@Override
	public void start(Stage primaryStage) {
		AnchorPane mainContainer;
		prototypeGUIController controller;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("prototypeGUI.fxml"));
			mainContainer = loader.load();
			controller = loader.getController();
			Scene mainScene = new Scene(mainContainer);
			controller.setStage(primaryStage);
			controller.setClient(new Client(DEFAULT_IP, DEFAULT_PORT));
			primaryStage.setScene(mainScene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
