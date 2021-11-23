package gui;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Config.TypeOfOrder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Update GUI controller.
 * 
 * @author Aviel Malayev
 * @author Natali Krief
 * @author Michael Ben Israel
 * @author Eden Ben Abu
 * @author Shaked Sabag
 * @version November 2021 (1.0)
 */
public class prototypeUpdateController {

	private Stage stage;

	@FXML
	private TextField orderAddressTxt;

	@FXML
	private TextField orderNumTxt;

	@FXML
	private TextField orderTimeTxt;

	@FXML
	private TextField phoneNumberTxt;

	@FXML
	private TextField restaurantTxt;

	@FXML
	private ImageView back;

	@FXML
	private ComboBox<TypeOfOrder> typeOfOrder;

	@FXML
	private Label errorMsg;

	@FXML
	private Rectangle updateBG;

	@FXML
	private Text updateBtn;

	/**
	 * Getting the stage of the application.
	 * 
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Creating combobox for types of order delivery methods.
	 */
	public void setCombo() {
		typeOfOrder.getItems().addAll(TypeOfOrder.values());
	}

	/**
	 * Setting default values of the order.
	 * 
	 * @param result
	 */
	public void setFields(String result) {
		String[] arr = result.split("_");
		orderNumTxt.setText(arr[0]);
		restaurantTxt.setText(arr[1]);
		orderTimeTxt.setText(arr[2]);
		phoneNumberTxt.setText(arr[3]);
		orderAddressTxt.setText(arr[5]);
		for (TypeOfOrder t : TypeOfOrder.values()) {
			if (arr[4].equals(t.toString())) {
				typeOfOrder.getSelectionModel().select(t);
				break;
			}
		}
	}

	/**
	 * On click event handler. Sending a server request to update the order address
	 * and the type of order.
	 * 
	 * @param event
	 */
	@FXML
	void updateDB(MouseEvent event) {
		String address = orderAddressTxt.getText();
		String phoneNumber = phoneNumberTxt.getText();
		TypeOfOrder type = typeOfOrder.getValue();
		if (!checkInput(address)) {
			errorMsg.setTextFill(Color.web("red"));
			errorMsg.setText("Must fill Order Address field");
			return;
		}
		if (checkSpecialCharacters(address)) {
			errorMsg.setTextFill(Color.web("red"));
			errorMsg.setText("Address can't contains special characters");
			return;
		}
		if (!checkInput(phoneNumber)) {
			errorMsg.setTextFill(Color.web("red"));
			errorMsg.setText("Must fill Phone Number field");
			return;
		}
		if (checkSpecialCharacters(phoneNumber)) {
			errorMsg.setTextFill(Color.web("red"));
			errorMsg.setText("Phone number can't contains special characters");
			return;
		}
		if (!checkInput(type)) {
			errorMsg.setTextFill(Color.web("red"));
			errorMsg.setText("Must choose a delivery method");
			return;
		}
		String validType = type.toString();
		ClientGUI.client.update(orderNumTxt.getText(), address, validType, phoneNumber);
		errorMsg.setTextFill(Color.web("green"));
		errorMsg.setText("Updated!");
	}

	/**
	 * Checking if a single string is valid by standard definition. Not empty string
	 * or a null.
	 * 
	 * @param input
	 */
	public boolean checkInput(Object input) {
		if (input == null || input.equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * Checking for special characters in a string.
	 * 
	 * @param input
	 */
	public boolean checkSpecialCharacters(String input) {
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(input);
		return m.find();
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
		prototypeSelectOrderController controller;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("prototypeSelectOrderGUI.fxml"));
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

	/**
	 * Creating hover effect on all the buttons.
	 * 
	 * @param event
	 */
	@FXML
	void mouseEnter(MouseEvent event) {
		updateBG.setStyle("-fx-fill:#2197ff;");
	}

	/**
	 * Creating hover effect on all the buttons.
	 * 
	 * @param event
	 */
	@FXML
	void mouseExit(MouseEvent event) {
		updateBG.setStyle("-fx-fill:#ededed;");
	}
}