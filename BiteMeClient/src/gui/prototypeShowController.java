package gui;

import java.io.IOException;

import Config.Order;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Show GUI controller.
 * 
 * @author Aviel Malayev
 * @author Natali Krief
 * @author Michael Ben Israel
 * @author Eden Ben Abu
 * @author Shaked Sabag
 * @version November 2021 (1.0)
 */
public class prototypeShowController {

	private Stage stage;

	@FXML
	private ImageView back;

	@FXML
	private TableView<Order> tableView;

	/**
	 * Getting the stage of the application.
	 * 
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Creating the table of this scene. Content of the table is all the orders in
	 * the db.
	 */
	public void setTable() {
		ClientGUI.client.show();
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		TableColumn<Order, String> orderNumCol = new TableColumn<Order, String>("Order Number");
		orderNumCol.setCellValueFactory(new PropertyValueFactory<Order, String>("orderNumber"));
		tableView.getColumns().add(orderNumCol);
		TableColumn<Order, String> restaurantCol = new TableColumn<Order, String>("Restaurant");
		restaurantCol.setCellValueFactory(new PropertyValueFactory<Order, String>("restaurant"));
		tableView.getColumns().add(restaurantCol);
		TableColumn<Order, String> orderTimeCol = new TableColumn<Order, String>("Order Time");
		orderTimeCol.setCellValueFactory(new PropertyValueFactory<Order, String>("orderTime"));
		tableView.getColumns().add(orderTimeCol);
		TableColumn<Order, String> phoneNumberCol = new TableColumn<Order, String>("Phone Number");
		phoneNumberCol.setCellValueFactory(new PropertyValueFactory<Order, String>("phoneNumber"));
		tableView.getColumns().add(phoneNumberCol);
		TableColumn<Order, String> typeOfOrderCol = new TableColumn<Order, String>("Type Of Order");
		typeOfOrderCol.setCellValueFactory(new PropertyValueFactory<Order, String>("typeOfOrder"));
		tableView.getColumns().add(typeOfOrderCol);
		TableColumn<Order, String> orderAddressCol = new TableColumn<Order, String>("Order Address");
		orderAddressCol.setCellValueFactory(new PropertyValueFactory<Order, String>("orderAddress"));
		tableView.getColumns().add(orderAddressCol);
		tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		addValues();
	}

	/**
	 * Adding values received from the server to the table.
	 */
	public void addValues() {
		String[] arr = ClientGUI.client.res.split("_");
		for (int i = 0; i < arr.length - 5; i += 6) {
			tableView.getItems().add(new Order(arr[i], arr[i + 1], arr[i + 2], arr[i + 3], arr[i + 4], arr[i + 5]));
		}
		tableView.refresh();
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