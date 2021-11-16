package gui;

import java.io.IOException;

import ClientServerComm.Client;
import Config.TypeOfOrder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class prototypeUpdateController {
	Client client;
	private Stage stage;
	
    @FXML
    private TextField orderAddress;

    @FXML
    private Button updateBtn;

    @FXML
    private ImageView back;

    @FXML
    private ComboBox<TypeOfOrder> typeOfOrder;
    
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
    void updateDB(ActionEvent event) {
    	String address = orderAddress.getText();
    	String type = typeOfOrder.getSelectionModel().getSelectedItem().toString();
    	client.update(address, type);
    	Alert displayMessage = new Alert(AlertType.INFORMATION);
    	displayMessage.setHeaderText(address+ " "+type);
    	orderAddress.clear();
    	typeOfOrder.getSelectionModel().clearSelection();
    	displayMessage.show();
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
    	}catch(IOException e) {
    		e.printStackTrace();
    		return;
    	}
    }

}
