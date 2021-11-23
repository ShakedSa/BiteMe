package gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class prototypeSelectOrderController {

	private Stage stage;

	@FXML
	private ImageView back;

	@FXML
	private Text displayBtn;

	@FXML
	private Rectangle displayBtnBG;

	@FXML
	private Label errorMsg;

	@FXML
	private TextField orderNumTxt;

	/**
	 * Getting the stage of the application.
	 * 
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * On click event handler. sending a server request to display a certain order.
	 * scene.
	 * 
	 * @param event
	 */
	@FXML
	void displayOrder(MouseEvent event) {
		String orderNumber = orderNumTxt.getText();
		if(orderNumber == null || orderNumber.equals("")) {
			errorMsg.setTextFill(Color.web("red"));
			errorMsg.setText("Must fill order number");
			return;
		}
		ClientGUI.client.getOrder(orderNumber);
		try {
			Thread.sleep(1000);
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}
		displayOrderHandler();
	}
	
	/**
	 * Handle scene swap to display order values.
	 * */
	void displayOrderHandler() {
		String result = ClientGUI.client.res;
		if(result == null | result.equals("")) {
			errorMsg.setTextFill(Color.web("red"));
			errorMsg.setText("Order number doesn't exists");
			return;
		}
		AnchorPane updateContainer;
		prototypeUpdateController controller;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("prototypeUpdateGUI.fxml"));
			updateContainer = loader.load();
			controller = loader.getController();
			controller.setStage(stage);
			controller.setFields(result);
			controller.setCombo();
			Scene updateScene = new Scene(updateContainer);
			stage.setScene(updateScene);
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
		displayBtnBG.setStyle("-fx-fill:#2197ff;");
	}

	/**
	 * Creating hover effect on all the buttons.
	 * 
	 * @param event
	 */
	@FXML
	void mouseExit(MouseEvent event) {
		displayBtnBG.setStyle("-fx-fill:#ededed;");
	}

	/**
	 * On click event handler to switch back scenes to the main scene of BiteMe
	 * prototype.
	 * 
	 * @param event
	 */
	@FXML
	void returnToMain(MouseEvent event) {
		AnchorPane updateContainer;
		prototypeGUIController controller;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("prototypeGUI.fxml"));
			updateContainer = loader.load();
			controller = loader.getController();
			controller.setStage(stage);
			Scene updateScene = new Scene(updateContainer);
			stage.setScene(updateScene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

}
