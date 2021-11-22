package gui;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ClientServerComm.Client;
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

public class prototypeUpdateController {
	
	private Stage stage;
	@FXML
	private TextField orderAddress;

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

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setCombo() {
		typeOfOrder.getItems().addAll(TypeOfOrder.values());
	}

	@FXML
	void updateDB(MouseEvent event) {
		if (orderAddress.getText().equals("")) {
			errorMsg.setTextFill(Color.web("red"));
			errorMsg.setText("Must fill Order Address field");
			return;
		}
		//Check if order address contains special characters
		String address = orderAddress.getText();
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(address);
		boolean b = m.find();
		if(b) {
			errorMsg.setTextFill(Color.web("red"));
			errorMsg.setText("Address can't contains special characters");
			return;
		}
		if (typeOfOrder.getValue() == null) {
			errorMsg.setTextFill(Color.web("red"));
			errorMsg.setText("Must choose a delivery method");
			return;
		}
		String type = typeOfOrder.getSelectionModel().getSelectedItem().toString();
		ClientGUI.client.update(address, type);
		errorMsg.setText(address + " " + type);
		errorMsg.setTextFill(Color.web("green"));
		orderAddress.clear();
		typeOfOrder.getSelectionModel().clearSelection();
	}

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

	@FXML
	void mouseEnter(MouseEvent event) {
		updateBG.setStyle("-fx-fill:#2197ff;");
	}

	@FXML
	void mouseExit(MouseEvent event) {
		updateBG.setStyle("-fx-fill:#ededed;");
	}
}