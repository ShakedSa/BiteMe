package Controls;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.Customer;
import Entities.Order;
import client.ClientGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MyOrdersController implements Initializable {

	private Router router;

	private Stage stage;

	private Scene scene;
	
	TableView<Order> orderTable;
	Label tableTitle;
	Label errorMsg;
	Button updateOrder;

	@FXML
	private Rectangle avatar;

	@FXML
	private ImageView bagImg;

	@FXML
	private Text homePageBtn;

	@FXML
	private Text homePageBtn1;

	@FXML
	private Text itemsCounter;

	@FXML
	private Circle itemsCounterCircle;

	@FXML
	private Text logOutBtn;

	@FXML
	private Text profileBtn;

	@FXML
	private AnchorPane root;

	
	private void clearScreen() {
		root.getChildren().removeAll(orderTable,tableTitle,errorMsg,updateOrder);
	}
	@FXML
	void changeToCart(MouseEvent event) {
		router.changeToMyCart("MyOrders");
	}

	@FXML
	void logOutBtnClicked(MouseEvent event) {
		router.logOut();
	}

	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}
	
	@FXML
    void returnToHomePage(MouseEvent event) {
		router.changeSceneToHomePage();
    }
	
	/**
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		router.setAvatar(avatar);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		router = Router.getInstance();
		router.setMyOrdersController(this);
		setStage(router.getStage());
	}
	
	public void setScene(Scene scene) {
		this.scene = scene;
	}
	
	public Scene getScene() {
		return scene;
	}
	
	/**
	 * Setting the stage instance.
	 * 
	 * @param Stage stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void setItemsCounter() {
		itemsCounter.setText(router.getBagItems().size() + "");
	}
	
	/**
	 * private method to create table
	 */
	private void createTable() {
		orderTable = new TableView<>();
		TableColumn<Order, String> orderNumCol = new TableColumn<Order, String>("Order Number");
		orderNumCol.setCellValueFactory(new PropertyValueFactory<Order, String>("orderNumber"));
		orderTable.getColumns().add(orderNumCol);
		TableColumn<Order, String> restaurantCol = new TableColumn<Order, String>("Restaurant");
		restaurantCol.setCellValueFactory(new PropertyValueFactory<Order, String>("restaurantName"));
		orderTable.getColumns().add(restaurantCol);
		TableColumn<Order, String> orderTimeCol = new TableColumn<Order, String>("Order Time");
		orderTimeCol.setCellValueFactory(new PropertyValueFactory<Order, String>("dateTime"));
		orderTable.getColumns().add(orderTimeCol);
		TableColumn<Order, String> typeOfOrderCol = new TableColumn<Order, String>("Order Price");
		typeOfOrderCol.setCellValueFactory(new PropertyValueFactory<Order, String>("orderPrice"));
		orderTable.getColumns().add(typeOfOrderCol);
		TableColumn<Order, String> orderAddressCol = new TableColumn<Order, String>("Status");
		orderAddressCol.setCellValueFactory(new PropertyValueFactory<Order, String>("status"));
		orderTable.getColumns().add(orderAddressCol);
		orderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}
	
	/**
	 * Private method to build the view table with all the customer's open orders.
	 */
	@SuppressWarnings("unchecked")
	public void displayOpenOrders() {
		clearScreen();
		createTable();
		Thread t = new Thread(() -> {
			synchronized (ClientGUI.getMonitor()) {
				ClientGUI.getClient().getCustomersOrder((Customer) (ClientGUI.getClient().getUser()).getServerResponse());
				try {
					ClientGUI.getMonitor().wait();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
			}
		});
		t.start();
		try {
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		ArrayList<Order> orders = (ArrayList<Order>) (ClientGUI.getClient().getLastResponse().getServerResponse());
		tableTitle = new Label("Orders:");
		tableTitle.setFont(new Font("Berlin Sans FB", 16));
		tableTitle.setLayoutX(120);
		tableTitle.setLayoutY(193);
		if (orders.size() == 0) {
			tableTitle.setText("Your account doesn't have any open orders.");
			root.getChildren().add(tableTitle);
			return;
		} else {
			ObservableList<Order> ordersObservable = FXCollections.observableArrayList(orders);
			orderTable.setItems(ordersObservable);
			orderTable.setLayoutX(100);
			orderTable.setLayoutY(213);
			orderTable.setMaxHeight(250);
			orderTable.setPrefWidth(630);
			updateOrder = new Button("Update Received Order");
			errorMsg = new Label();
			errorMsg.setLayoutX(120);
			errorMsg.setLayoutY(470);
			updateOrder.setOnAction(e -> {
				Order order = orderTable.getSelectionModel().getSelectedItem();
				errorMsg.setTextFill(Color.RED);
				if (order == null) {
					errorMsg.setText("Please select order to update.");
					return;
				}
				if (order.getStatus() == null || !order.getStatus().equals("Ready")) {
					errorMsg.setText("Selected order was not deliveried yet.");
					return;
				}
				Thread th = new Thread(() -> {
					synchronized (ClientGUI.getMonitor()) {
						ClientGUI.getClient().updateOrderReceived(order);
						try {
							ClientGUI.getMonitor().wait();
						} catch (Exception ex) {
							ex.printStackTrace();
							return;
						}
					}
				});
				th.start();
				try {
					th.join();
				} catch (Exception ex) {
					ex.printStackTrace();
					return;
				}
				if (ClientGUI.getClient().getLastResponse().getMsg().equals("Success")) {
					errorMsg.setText("Table Updated");
					errorMsg.setTextFill(Color.GREEN);
					ordersObservable.remove(orderTable.getSelectionModel().getSelectedItem());
					orderTable.refresh();
				}
			});
			updateOrder.setLayoutX(120);
			updateOrder.setLayoutY(500);
			root.getChildren().addAll(orderTable, tableTitle, errorMsg, updateOrder);
		}
	}
}
