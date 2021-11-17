package gui;

import java.io.IOException;

import ClientServerComm.Client;
import Config.TypeOfOrder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class prototypeUpdateController {
	Client client;
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

	public void setClient(Client client) {
		this.client = client;
	}

	public void setCombo() {
		typeOfOrder.getItems().addAll(TypeOfOrder.values());
	}

	@FXML
	void updateDB(MouseEvent event) {
		if (orderAddress.getText().equals("")) {
			errorMsg.setText("Must fill Order Address field");
			return;
		}
		if (typeOfOrder.getValue() == null) {
			errorMsg.setText("Must choose a delivery method");
			return;
		}
		String address = orderAddress.getText();
		String type = typeOfOrder.getSelectionModel().getSelectedItem().toString();
		client.update(address, type);
//		Alert displayMessage = new Alert(AlertType.INFORMATION);
		errorMsg.setText(address + " " + type);
		errorMsg.setStyle("-fx-color:green;");
		orderAddress.clear();
		typeOfOrder.getSelectionModel().clearSelection();
//		errorMsg.setText("");
//		displayMessage.show();
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
