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

	private User user = (User) ClientGUI.client.getUser().getServerResponse();
	private String restaurant = user.getOrganization();
	private Order order;

	public void setOrder() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				ClientGUI.client.getOrderInfo(restaurant);
				synchronized (ClientGUI.monitor) {
					try {
						ClientGUI.monitor.wait();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
				ServerResponse sr = ClientGUI.client.getLastResponse();
				@SuppressWarnings("unchecked")
				// get the server response- list of orders
				ArrayList<Order> response = (ArrayList<Order>) sr.getServerResponse();
				setTable(response);
				return;
			}
		});
		t.start();
	}

	public void setOrder(ArrayList<Order> orders) {
		setTable(orders);
	}

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
				loader.setLocation(getClass().getResource("../gui/bitemeSupplierUpdateOrderPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setOrder(order);
				controller.setAvatar();
				controller.createCombos();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
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

	@FXML
	void closeExplainUpdate(MouseEvent event) {
		updateExplanation.setVisible(false);
	}

	@FXML
	void explainHowUpdate(MouseEvent event) {
		updateExplanation.setVisible(true);
	}

	@FXML
	void getRowData(MouseEvent event) {
		order = orderTable.getSelectionModel().getSelectedItem();
		if (order == null) {
			return;
		}
	}

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	@FXML
	void profileBtnClicked(MouseEvent event) {
		errorMsg.setText("");
		router.showProfile();
	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		errorMsg.setText("");
		router.changeSceneToHomePage();
	}

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

	// initialize the titles of the table
	private void initTable() {
		table_OrderNumber.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
		table_OrderTime.setCellValueFactory(new PropertyValueFactory<>("OrdeTime"));
		table_ReceivedTime.setCellValueFactory(new PropertyValueFactory<>("orderRecieved"));
		table_OrderStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
		// menuTable.setEditable(true);
	}

	// set table columns and values
	private void setTable(ArrayList<Order> orders) {
		orderTable.setItems(getOrder(orders));
	}

	// change arrayList to ObservableList
	private ObservableList<Order> getOrder(ArrayList<Order> list) {
		ObservableList<Order> orders = FXCollections.observableArrayList();
		list.forEach(p -> {
			if (p.getOrdeTime() == null || p.getOrdeTime().equals("")) {
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
	 * @param order the order to set
	 */
	public void setOrder(Order order) {
		if (order != null) {
			this.order = order;
		}
	}

}
