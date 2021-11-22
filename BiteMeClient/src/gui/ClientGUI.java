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

public class ClientGUI extends Application {
	public static final int DEFAULT_PORT = Integer.parseInt(ReadPropertyFile.getInstance().getProp("DefaultPort"));
	public static final String DEFAULT_IP = ReadPropertyFile.getInstance().getProp("ClientDefaultIP");
	public static Client client = new Client(DEFAULT_IP, DEFAULT_PORT);
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
			primaryStage.setScene(mainScene);
			primaryStage.setOnCloseRequest((EventHandler<WindowEvent>) new EventHandler<WindowEvent>() {
			    @Override
			    public void handle(WindowEvent t) {
			        Platform.exit();
			        System.exit(0);
			    }
			});
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
