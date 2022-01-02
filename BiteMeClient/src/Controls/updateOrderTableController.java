package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.Order;
import Entities.ServerResponse;
import Entities.User;
import Enums.UserType;
import client.ClientGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Natali 
 * This class display a table - all the orders of a specific
 * restaurant with orders that in status: "Pending" OR "Received"
 */
public class updateOrderTableController implements Initializable {

	public final UserType type = UserType.Supplier;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private Text errorMsg;

	@FXML
	private Rectangle avatar;

	@FXML
	private Text homePageBtn;

	@FXML
	private Rectangle leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private TableView<Order> orderTable;

	@FXML
	private Text profileBtn;

	@FXML
	private Text supplierPanelBtn;

	@FXML
	private TableColumn<Order, Integer> table_OrderNumber;

	@FXML
	private TableColumn<Order, String> table_OrderStatus;

	@FXML
	private TableColumn<Order, String> table_OrderTime;

	@FXML
	private TableColumn<Order, String> table_ReceivedTime;

	@FXML
	private TextArea updateExplanation;

	@FXML
	private Text updateOrderBtn;

	@FXML
	private ImageView updateOrderImage;

	private User user = (User) ClientGUI.getClient().getUser().getServerResponse();
	private String restaurant = user.getOrganization();
	private Order order;

	/**
	 * This method get from DB list of order with status: "Pending" OR "Received"
	 * and create a table with them
	 */
	public void setOrder() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				ClientGUI.getClient().getOrderInfo(restaurant);
				synchronized (ClientGUI.getMonitor()) {
					try {
						ClientGUI.getMonitor().wait();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
				ServerResponse sr = ClientGUI.getClient().getLastResponse();
				@SuppressWarnings("unchecked")
				// get the server response- list of orders
				ArrayList<Order> response = (ArrayList<Order>) sr.getServerResponse();
				setOrder(response);
				return;
			}
		});
		t.start();
	}

	/**
	 * This method gets list of orders and set a table
	 * @param orders
	 */
	public void setOrder(ArrayList<Order> orders) {
		setTable(orders);
	}

	/**
	 * initialize the next controller - supplierUpdateOrderController
	 */
	@FXML
	void updateOrderClicked(MouseEvent event) {
		if (order == null) {
			errorMsg.setText("Please select a row to update");
			return;
		}
		if (router.getSupplierUpdateOrderController() == null) {
			AnchorPane mainContainer;
			supplierUpdateOrderController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/bitemeSupplierUpdateOrderPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setOrder(order);
				controller.setAvatar();
				controller.createCombos();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Update Order");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			router.getSupplierUpdateOrderController().setOrder(order);
			router.getSupplierUpdateOrderController().createCombos();
			stage.setTitle("BiteMe - Update Order");
			stage.setScene(router.getSupplierUpdateOrderController().getScene());
			stage.show();
		}
	}

	/**
	 * close the explanation of how supplier suppose to update an order
	 */
	@FXML
	void closeExplainUpdate(MouseEvent event) {
		updateExplanation.setVisible(false);
	}

	/**
	 * present an explanation how supplier suppose to update an order
	 */
	@FXML
	void explainHowUpdate(MouseEvent event) {
		updateExplanation.setVisible(true);
	}

	/**
	 * This method get the data from the selected row
	 */
	@FXML
	void getRowData(MouseEvent event) {
		order = orderTable.getSelectionModel().getSelectedItem();
		if (order == null) {
			return;
		}
	}

	/**
	 * Logout and change scene to home page
	 */
	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	/**
	 * Changes scene to profile
	 */
	@FXML
	void profileBtnClicked(MouseEvent event) {
		errorMsg.setText("");
		router.showProfile();
	}

	/**
	 * Changes scene to home page
	 */
	@FXML
	void returnToHomePage(MouseEvent event) {
		errorMsg.setText("");
		router.changeSceneToHomePage();
	}

	/**
	 * Changes scene to supplier panel
	 */
	@FXML
	void returnToSupplierPanel(MouseEvent event) {
		errorMsg.setText("");
		router.returnToSupplierPanel(event);
	}

	/**
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		router.setAvatar(avatar);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setUpdateOrderTableController(this);
		setStage(router.getStage());
		router.setArrow(leftArrowBtn, -90);
		initTable();
		errorMsg.setText("");
		updateExplanation.setVisible(false);
	}

	/**
	 * This method initialize the titles of the table
	 */
	private void initTable() {
		table_OrderNumber.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
		table_OrderTime.setCellValueFactory(new PropertyValueFactory<>("OrderTime"));
		table_ReceivedTime.setCellValueFactory(new PropertyValueFactory<>("orderRecieved"));
		table_OrderStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
	}

	/**
	 * This method set table columns and values
	 * @param orders
	 */
	private void setTable(ArrayList<Order> orders) {
		orderTable.setItems(getOrder(orders));
	}

	/**
	 * This method change arrayList to ObservableList
	 * @param list
	 * @return ObservableList of orders
	 */
	private ObservableList<Order> getOrder(ArrayList<Order> list) {
		ObservableList<Order> orders = FXCollections.observableArrayList();
		list.forEach(p -> {
			
			if (p.getOrderTime() == null || p.getOrderTime().equals("")) {
				p.setOrderTime("");
			}
			if (p.getOrderRecieved() == null || p.getOrderRecieved().equals("")) {
				p.setOrderRecieved("");
			}
			if (p.getPlannedTime() == null || p.getPlannedTime().equals("")) {
				p.setPlannedTime("");
			}
		});
		orders.addAll(list);
		return orders;
	}

	/**
	 * This method set order
	 * @param order the order to set
	 */
	public void setOrder(Order order) {
		if (order != null) {
			this.order = order;
		}
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Scene getScene() {
		return scene;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
