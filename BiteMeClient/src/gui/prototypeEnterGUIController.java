package gui;

import java.io.IOException;

import ClientServerComm.Client;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class prototypeEnterGUIController {

	private Stage stage;
    @FXML
    private Rectangle enterBG;

    @FXML
    private Text enterBtn;

    @FXML
    private Rectangle exitBG;

    @FXML
    private Text exitTxt;

    @FXML
    private TextField ipTxt;
    
    @FXML
    private Label errorMsg;

    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    @FXML
    void closeApplication(MouseEvent event) {
    	System.exit(0);
    }

    @FXML
    void enterApplication(MouseEvent event) {
    	String ip = ipTxt.getText();
    	if(ip == null || ip.equals("")) {
    		errorMsg.setTextFill(Color.web("red"));
			errorMsg.setText("Must fill ip");
			return;
    	}
    	ClientGUI.client = new Client(ip, 5555);
    	AnchorPane mainContainer;
		prototypeGUIController controller;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("prototypeGUI.fxml"));
			mainContainer = loader.load();
			controller = loader.getController();
			controller.setStage(stage);
			Scene mainScene = new Scene(mainContainer);
			stage.setScene(mainScene);
			stage.setOnCloseRequest((EventHandler<WindowEvent>) new EventHandler<WindowEvent>() {
				/** Setting the 'X' button to close the application. */
				@Override
				public void handle(WindowEvent t) {
					Platform.exit();
					System.exit(0);
				}
			});
			stage.setResizable(false);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
    }

    /**
	 * Creating hover effect on all the buttons.
	 * 
	 * @param event
	 */
	@FXML
	void mouseEnter(MouseEvent event) {
		Text txt;
		Rectangle bg;
		if (event.getSource() instanceof Text) {
			txt = (Text) event.getSource();
			switch (txt.getText()) {
			case "Enter":
				enterBG.setStyle("-fx-fill:#2197ff;");
				break;
			case "Exit":
				exitBG.setStyle("-fx-fill:#2197ff;");
			}
		} else {
			bg = (Rectangle) event.getSource();
			switch (bg.getId()) {
			case "enterBG":
				enterBG.setStyle("-fx-fill:#2197ff;");
				break;
			case "exitBG":
				exitBG.setStyle("-fx-fill:#2197ff;");
			}
		}
	}

	/**
	 * Creating hover effect on all the buttons.
	 * 
	 * @param event
	 */
	@FXML
	void mouseExit(MouseEvent event) {
		if (event.getSource() instanceof Text) {
			return;
		}
		Rectangle bg;
		if (event.getSource() instanceof Rectangle) {
			bg = (Rectangle) event.getSource();
			switch (bg.getId()) {
			case "enterBG":
				enterBG.setStyle("-fx-fill:#2197ff00;");
				break;
			case "exitBG":
				exitBG.setStyle("-fx-fill:#2197ff00;");
			}
		}
	}

}
