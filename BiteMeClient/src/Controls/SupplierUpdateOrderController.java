package Controls;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import Entities.Order;
import Entities.User;
import Enums.TypeOfOrder;
import Enums.UserType;
import client.ClientGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Natali This class updates an order by supplier
 */
public class SupplierUpdateOrderController implements Initializable {

	public final UserType type = UserType.Supplier;
	private TypeOfOrder deliveryType;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private Text PendingTxt;

	@FXML
	private Text ReadyTxt;

	@FXML
	private Text ReceivedTxt;

	@FXML
	private ImageView newMsgImage;

	@FXML
	private ImageView VImage;

	@FXML
	private Rectangle avatar;

	@FXML
	private Text enterPlannedTineTxt;

	@FXML
	private Text errorMsg;

	@FXML
	private Text homePageBtn;

	@FXML
	private ComboBox<String> hourBox;

	@FXML
	private Text hourTxt;

	@FXML
	private Text orderNumberTxt;

	@FXML
	private Text orderStatusTxt;

	@FXML
	private RadioButton includeDeliveryBtn;

	@FXML
	private Text includeDeliveryTxt;

	@FXML
	private Rectangle leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private ComboBox<String> minutesBox;

	@FXML
	private Text minutesTxt;

	@FXML
	private RadioButton notIncludeDeliveryBtn;

	@FXML
	private Text noUpdateTxt;

	@FXML
	private Text profileBtn;

	@FXML
	private Text successMsg;

	@FXML
	private Text supplierPanelBtn;

	@FXML
	private ComboBox<String> updateDataComboBox;

	@FXML
	private Text updateDataTxt;

	@FXML
	private Label updateOrderBtn;

	private ObservableList<String> list;
	private User user = (User) ClientGUI.getClient().getUser().getServerResponse();
	private String restaurantName = user.getOrganization();
	private String orderNumber, status;
	private String receivedOrReady;
	private int orderNumberInt;
	private Integer deliveryNumber = 0;
	private Order order;

	/**
	 * This method updates order status and save the changes in DB
	 */
	@FXML
	void UpdateOrderClicked(MouseEvent event) {
		if (updateDataComboBox.getValue() == null) {
			errorMsg.setText("Please select the status of the order");
			return;
		}
		if (order.getStatus().equals("Received") && deliveryType != TypeOfOrder.takeaway) {
			if (!checkTime()) {
				return;
			}
		}
		errorMsg.setText("");
		receivedOrReady = updateDataComboBox.getValue();
		LocalDate now = LocalDate.now();
		// Order received
		String statusReceived = "Received";
		LocalTime timeNow = LocalTime.now();
		String nowTime = now.toString() + " " + timeNow.getHour() + ":" + timeNow.getMinute();
		// Order is ready
		String statusReady = "Ready";
		String hourPlannedTime = hourBox.getSelectionModel().getSelectedItem();
		String minutePlannedTime = minutesBox.getSelectionModel().getSelectedItem();
		String plannedTime = now.toString() + " " + hourPlannedTime + ":" + minutePlannedTime;

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				if (receivedOrReady.equals("Order Received")) {
					order.setStatus("Received");
					order.setOrderRecieved(nowTime);
					ClientGUI.getClient().UpdateOrderStatus(restaurantName, receivedOrReady, orderNumber, nowTime,
							statusReceived);
				} else { // Order Is Ready
					order.setStatus("Ready");
					order.setPlannedTime(plannedTime);
					ClientGUI.getClient().UpdateOrderStatus(restaurantName, receivedOrReady, orderNumber, plannedTime,
							statusReady);
				}
				synchronized (ClientGUI.getMonitor()) {
					try {
						ClientGUI.getMonitor().wait();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
			}
		});
		t.start();
		if (!checkServerResponse()) {
			return;
		}

		try {
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// save the return value from query - delivery number
		deliveryNumber = (int) ClientGUI.getClient().getLastResponse().getServerResponse();
		// order update successfully
		VImage.setVisible(true);
		successMsg.setVisible(true);
		// new message send to customer
		newMsgImage.setVisible(true);
		// can't update more than once
		updateOrderBtn.setDisable(true);
	}

	/**
	 * Private method to check if the input time is valid time.
	 */
	private boolean checkTime() {
		String hourToOrder = hourBox.getSelectionModel().getSelectedItem();
		String minuteToOrder = minutesBox.getSelectionModel().getSelectedItem();
		/** If no time selection was made */
		if (hourToOrder == null && minuteToOrder == null) {
			errorMsg.setText("Please pick time for the order");
			return false;
		}
		if (minuteToOrder == null) {
			errorMsg.setText("Please pick minutes for the order");
			return false;
		}
		if (hourToOrder == null) {
			errorMsg.setText("Please pick hours for the order");
			return false;
		}
		errorMsg.setText("");
		LocalTime now = LocalTime.now();
		LocalTime orderTime = LocalTime.of(Integer.parseInt(hourToOrder), Integer.parseInt(minuteToOrder));
		/** If time selection is before now */
		if (now.compareTo(orderTime) == 1) {
			errorMsg.setText("Time of order must be greater or equals to now: " + now.toString());
			return false;
		}
		errorMsg.setText("");
		return true;
	}

	/**
	 * Get the currently order status and display it on screen
	 */
	private void checkStatusOrder() {
		orderStatusTxt.setVisible(true);
		if (status.equals("Pending")) {
			// set comboBox
			ArrayList<String> type = new ArrayList<String>();
			type.add("Order Received");
			list = FXCollections.observableArrayList(type);
			updateDataComboBox.setItems(list);
			// display correct status
			PendingTxt.setVisible(true);
			ReceivedTxt.setVisible(false);
			ReadyTxt.setVisible(false);
			noUpdateTxt.setVisible(false);
			updateOrderBtn.setDisable(false);
		} else if (status.equals("Received")) {
			// set comboBox
			ArrayList<String> type = new ArrayList<String>();
			type.add("Order Is Ready");
			list = FXCollections.observableArrayList(type);
			updateDataComboBox.setItems(list);
			// display correct status
			PendingTxt.setVisible(false);
			ReceivedTxt.setVisible(true);
			ReadyTxt.setVisible(false);
			noUpdateTxt.setVisible(false);
			updateOrderBtn.setDisable(false);
		} else { // order status is ready
			ReadyTxt.setVisible(true);
			noUpdateTxt.setVisible(true);
			updateOrderBtn.setDisable(true);
			if (deliveryType != TypeOfOrder.takeaway && !order.getStatus().equals("Ready")) {
				updateDataComboBox.setVisible(true);
				updateDataTxt.setVisible(true);
			} else {
				updateDataComboBox.setVisible(false);
				updateDataTxt.setVisible(false);
			}
		}
		updateDataComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == "Order Is Ready")
				includeDelivery();
		});
	}

	/**
	 * Checks server response and display relevant information. return true if the
	 * update was completed successfully and false else
	 */
	private boolean checkServerResponse() {
		if (ClientGUI.getClient().getLastResponse() == null) {
			errorMsg.setText("update order was failed");
			return false;
		}
		switch (ClientGUI.getClient().getLastResponse().getMsg().toLowerCase()) {
		case "update order was failed":
			return false;
		case "success":
			updateOrderBtn.setDisable(false);
			return true;
		default:
			return false;
		}
	}

	/**
	 * When order status is ready and supplier selects YES - the order includes
	 * delivery, and he needs to enter planned time
	 */
	@FXML
	void includeDeliveryBtnClicked(MouseEvent event) {
		if (notIncludeDeliveryBtn.isSelected()) {
			notIncludeDeliveryBtn.setSelected(false);
		}
		includeDeliveryBtn.setSelected(true);
		includeDelivery();
	}

	/**
	 * When order status is ready and supplier selects NO - the order doesn't
	 * includes delivery
	 */
	@FXML
	void notIncludeDeliveryBtnClicked(MouseEvent event) {
		if (includeDeliveryBtn.isSelected()) {
			includeDeliveryBtn.setSelected(false);
		}
		notIncludeDeliveryBtn.setSelected(true);
		notIncludeDelivery();
	}

	/**
	 * set visibility of order that doesn't include delivery
	 */
	private void includeDelivery() {
		enterPlannedTineTxt.setVisible(true);
		hourBox.setVisible(true);
		hourTxt.setVisible(true);
		minutesBox.setVisible(true);
		minutesTxt.setVisible(true);
	}

	/**
	 * set visibility of order that doesn't include delivery
	 */
	private void notIncludeDelivery() {
		enterPlannedTineTxt.setVisible(false);
		hourBox.setVisible(false);
		hourTxt.setVisible(false);
		minutesBox.setVisible(false);
		minutesTxt.setVisible(false);
	}

	/**
	 * This method initialize the next controller - sendMsgToCustomerController
	 */
	@FXML
	void newMsgClicked(MouseEvent event) {
		if (router.getSendMsgToCustomerController() == null) {
			AnchorPane mainContainer;
			SendMsgToCustomerController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/bitemeSendMsgToCustomerPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.setOrderInfo(getOrder(), getDeliveryNumber());
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Message Simulation");
				stage.setScene(mainScene);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - Message Simulation");
			router.getSendMsgToCustomerController().setOrderInfo(getOrder(), getDeliveryNumber());
			stage.setScene(router.getSendMsgToCustomerController().getScene());
		}
		stage.show();
	}

	/**
	 * Setting values for the combo boxes. hourBox values from 0-23, minutesBox
	 * values from 0-59.
	 */
	public void createCombos() {
		ObservableList<String> hourOptions = FXCollections.observableArrayList(Arrays.asList(generator(24)));
		hourBox.getItems().addAll(hourOptions);
		ObservableList<String> minuteOptions = FXCollections.observableArrayList(Arrays.asList(generator(60)));
		minutesBox.getItems().addAll(minuteOptions);
	}

	/**
	 * Private method generating <size> strings to add to the combo box.
	 * 
	 * @param size
	 * @return String[]
	 */
	private String[] generator(int size) {
		String[] res = new String[size];
		for (int i = 0; i < res.length; i++) {
			if (i < 10) {
				res[i] = "0" + i;
			} else {
				res[i] = i + "";
			}
		}
		return res;
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
		router.showProfile();
		deliveryNumber = null;
	}

	/**
	 * Changes scene to home page
	 */
	@FXML
	void returnToHomePage(MouseEvent event) {
		router.changeSceneToHomePage();
	}

	/**
	 * Changes scene to supplier panel
	 */
	@FXML
	void returnToSupplierPanel(MouseEvent event) {
		router.returnToSupplierPanel(event);
	}

	/**
	 * Change scene to the previous page - table of orders
	 */
	@FXML
	void returnToUpdateOrderTable(MouseEvent event) {
		router.getSupplierPanelController().updateOrederClicked(event);
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
		router.setSupplierUpdateOrderController(this);
		setStage(router.getStage());
		router.setArrow(leftArrowBtn, -90);
		// set time check box
		hourBox.getSelectionModel().select(String.format("%02d", LocalTime.now().getHour()));
		minutesBox.getSelectionModel().select(String.format("%02d", LocalTime.now().getMinute()));
	}

	/**
	 * @return the selected order
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * @return deliveryNumber
	 */
	public int getDeliveryNumber() {
		if (deliveryNumber == null) {
			deliveryNumber = (int) ClientGUI.getClient().getLastResponse().getServerResponse();
		}
		return deliveryNumber;
	}

	/**
	 * This method set order and call to "setStartPage" that set visibility
	 */
	public void setOrder(Order order) {
		if (order != null) {
			this.order = order;
			orderNumberInt = order.getOrderNumber();
			orderNumber = orderNumberInt + "";
			orderNumberTxt.setText("Order Number: " + orderNumber);
			status = order.getStatus();
			setStartPage();
			// get currently order status
			checkStatusOrder();
		}
	}

	/**
	 * This method initialized this screen and set visibility of buttons and text
	 */
	private void setStartPage() {
		Thread t = new Thread(() -> {
			synchronized (ClientGUI.getMonitor()) {
				ClientGUI.getClient().getDeliveryInfo(order.getOrderNumber());
				try {
					ClientGUI.getMonitor().wait();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				deliveryType = TypeOfOrder
						.valueOf((String) ClientGUI.getClient().getLastResponse().getServerResponse());
			}
		});
		errorMsg.setText("");
		deliveryNumber = null;
		updateOrderBtn.setDisable(true);
		updateDataComboBox.getSelectionModel().clearSelection();
//		includeDeliveryTxt.setVisible(false);
		// not include delivery
//		notIncludeDeliveryBtn.setVisible(false);
//		notIncludeDeliveryBtn.setSelected(false);
		// include delivery
//		includeDeliveryBtn.setSelected(false);
//		includeDeliveryBtn.setVisible(false);
		enterPlannedTineTxt.setVisible(false);
		// time check box
		hourBox.setVisible(false);
		hourTxt.setVisible(false);
		minutesBox.setVisible(false);
		minutesTxt.setVisible(false);
		// successfully update
		VImage.setVisible(false);
		successMsg.setVisible(false);
		newMsgImage.setVisible(false);
		// hide status:
		PendingTxt.setVisible(false);
		ReceivedTxt.setVisible(false);
		ReadyTxt.setVisible(false);
		noUpdateTxt.setVisible(false);
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
