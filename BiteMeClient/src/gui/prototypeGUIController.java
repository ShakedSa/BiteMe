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
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class prototypeGUIController {
	Client client;
	private Stage stage;
    @FXML
    private Button show;

    @FXML
    private Button update;
    
    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    public void setClient(Client client) {
    	this.client = client;
    }
    @FXML
    void openShowStage(ActionEvent event) {
    	client.show();
    }
    
    
    @FXML
    void openUpdateStage(ActionEvent event) {
    	AnchorPane updateContainer;
    	prototypeUpdateController controller;
    	try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(getClass().getResource("prototypeUpdateGUI.fxml"));
    		updateContainer = loader.load();
    		controller = loader.getController();
    		controller.setStage(stage);
    		controller.setClient(client);
    		ObservableList<?> options = 
    		        FXCollections.observableArrayList(
    		            TypeOfOrder.basicDelivery,
    		            TypeOfOrder.takeaway,
    		            TypeOfOrder.preorderDelivery,
    		            TypeOfOrder.robotDelivery,
    		            TypeOfOrder.sharedDelivery
    		        );
    		ComboBox<TypeOfOrder> typeOfOrder = new ComboBox<>();
    		typeOfOrder.getItems().addAll(TypeOfOrder.values());
       		controller.setCombo(typeOfOrder);
    		Scene updateScene = new Scene(updateContainer);
    		stage.setScene(updateScene);
    		stage.show();
    	}catch(IOException e) {
    		e.printStackTrace();
    		return;
    	}
    }

}
