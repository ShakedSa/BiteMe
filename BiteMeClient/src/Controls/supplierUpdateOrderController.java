package Controls;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import Entities.ServerResponse;
import Enums.UserType;
import Util.InputValidation;
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
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class supplierUpdateOrderController implements Initializable {

	public final UserType type = UserType.Supplier;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private TextField OrderNumberTxtField;
	
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
    private Text orderStatusTxt;

	@FXML
	private RadioButton includeDeliveryBtn;

	@FXML
	private Text includeDeliveryTxt;

	@FXML
	private ImageView leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private ComboBox<String> minutesBox;

	@FXML
	private Text minutesTxt;

	@FXML
	private RadioButton notIncludeDeliveryBtn;

	@FXML
	private Text profileBtn;

	@FXML
	private Label searchBtn;

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
	
    @FXML
    private Text noUpdate;

	ObservableList<String> list;
	String orderNumber;
	String receivedOrReady;
	Integer deliveryNumber = 0;
	String status;

	/**
	 * This method initialized this screen
	 */
	private void setStartPage() {
		updateOrderBtn.setDisable(true);
		updateDataTxt.setVisible(false);
		updateDataComboBox.setVisible(false);
		updateDataComboBox.getSelectionModel().clearSelection();
		includeDeliveryTxt.setVisible(false);
		includeDeliveryBtn.setSelected(false);
		includeDeliveryBtn.setVisible(false);
		notIncludeDeliveryBtn.setVisible(false);
		notIncludeDeliveryBtn.setSelected(false);
		enterPlannedTineTxt.setVisible(false);
		hourBox.setVisible(false);
		hourTxt.setVisible(false);
		minutesBox.setVisible(false);
		minutesTxt.setVisible(false);
		VImage.setVisible(false);
		successMsg.setVisible(false);
		newMsgImage.setVisible(false);
		errorMsg.setText("");
		deliveryNumber = null;
		noUpdate.setText("");
		//hide status line:
		orderStatusTxt.setVisible(false);
		PendingTxt.setVisible(false);
		ReceivedTxt.setVisible(false);
		ReadyTxt.setVisible(false);
	}

	/**
	 * This method updates order status
	 * 
	 * @param event
	 */
	@FXML
	void UpdateOrderClicked(MouseEvent event) {
		if (includeDeliveryBtn.isSelected() && !checkTime() && !CheckUserInput(orderNumber)) {
			return;
		}
		if (updateDataComboBox.getValue() == null) {
			errorMsg.setText("Please select the status of the order");
			return;
		}
		errorMsg.setText("");
		receivedOrReady = updateDataComboBox.getValue();
		orderNumber = OrderNumberTxtField.getText();
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
				synchronized (ClientGUI.monitor) {
					try {
						ClientGUI.monitor.wait();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
			}
		});
		t.start();
		
		if(!checkServerResponse()) {
			return;
		}

		if (receivedOrReady.equals("Order Received")) {
			ClientGUI.client.UpdateOrderStatus(receivedOrReady, orderNumber, nowTime, statusReceived);
		} else { // Order Is Ready
			ClientGUI.client.UpdateOrderStatus(receivedOrReady, orderNumber, plannedTime, statusReady);
		}
		try {
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		deliveryNumber = (int) ClientGUI.client.getLastResponse().getServerResponse();
		System.out.println(deliveryNumber);

		VImage.setVisible(true);
		successMsg.setVisible(true);
		newMsgImage.setVisible(true); // new message send to customer
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
	 * This function check the order number that entered. if it correct, display the
	 * comboBox to choose what need to update: "Order Received" or "Order Is Ready"
	 * 
	 * @param event
	 */
	@FXML
	void searchClicked(MouseEvent event) {
		orderNumber = OrderNumberTxtField.getText();
		if (!CheckUserInput(orderNumber)) {
			return;
		}
		ClientGUI.client.searchOrder(orderNumber);
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (ClientGUI.monitor) {
					try {
						ClientGUI.monitor.wait();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
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
		if (!checkServerResponse()) { //order number doesn't exist
			return;
		}
		setStartPage();
		//OrderNumberTxtField.setDisable(true);
		getStatusOrder(); //get currently order status
		//set comboBox
		   if(status.equals("Pending")) {
			   ArrayList<String> type = new ArrayList<String>();
				type.add("Order Received");
				list = FXCollections.observableArrayList(type);
				updateDataComboBox.setItems(list);
		   }
		   else if(status.equals("Received")) {
			   ArrayList<String> type = new ArrayList<String>();
				type.add("Order Is Ready"); 
				list = FXCollections.observableArrayList(type);
				updateDataComboBox.setItems(list);
		   }
		   else { //status.equals("Ready")) 
			   noUpdate.setText("There is no available update for this order.");
			   updateOrderBtn.setDisable(true);
			   return;
		   }
		updateDataTxt.setVisible(true);
		updateDataComboBox.setVisible(true);
		updateDataComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == "Order Is Ready")
				orderIsReady();
			else {
				includeDeliveryTxt.setVisible(false);
				includeDeliveryBtn.setVisible(false);
				notIncludeDeliveryBtn.setVisible(false);
				notIncludeDelivery();
			}
		});
		
	}

	/**
	 * get the currently order status and display it on screen
	 */
	private void getStatusOrder() {
		orderNumber = OrderNumberTxtField.getText();
		if (!CheckUserInput(orderNumber)) {
			return;
		}
		ClientGUI.client.getOrderInfo(orderNumber);
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (ClientGUI.monitor) {
					try {
						ClientGUI.monitor.wait();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
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
		
		// handle server response
		ServerResponse sr = ClientGUI.client.getLastResponse();
		if (sr == null) {
			return;
		}
		@SuppressWarnings("unchecked")
		ArrayList<String> response = (ArrayList<String>) sr.getServerResponse();
		if (response == null) {
			System.out.println("test");
			return;
		}
		orderStatusTxt.setVisible(true);
		status = response.get(3);
	   if(status.equals("Pending")) {
		   PendingTxt.setVisible(true);
		   ReceivedTxt.setVisible(false);
		   ReadyTxt.setVisible(false);
		   updateOrderBtn.setDisable(false);
	   }
	   else if(status.equals("Received")) {
		   PendingTxt.setVisible(false);
		   ReceivedTxt.setVisible(true);
		   ReadyTxt.setVisible(false);
		   updateOrderBtn.setDisable(false);
	   }
	   else { //status.equals("Ready")) 
		   PendingTxt.setVisible(false);
		   ReceivedTxt.setVisible(false);
		   ReadyTxt.setVisible(true);
	   }
	}
	
	/**
	 * checks the order information received from Server. display relevant
	 * information.
	 */
	private boolean checkServerResponse() {
		if (ClientGUI.client.getLastResponse() == null) {
			return false;
		}

		switch (ClientGUI.client.getLastResponse().getMsg().toLowerCase()) {
		case "order number doesn't exist":
			errorMsg.setText("This order doesn't exist");
			//hide status line:
			orderStatusTxt.setVisible(false);
			PendingTxt.setVisible(false);
			ReceivedTxt.setVisible(false);
			ReadyTxt.setVisible(false);
			//hide combo
			updateDataTxt.setVisible(false);
			updateDataComboBox.setVisible(false);
			//hide success feedback
			successMsg.setVisible(false);
			VImage.setVisible(false);
			//hide the buttons of ready status
			includeDeliveryTxt.setVisible(false);
			includeDeliveryBtn.setVisible(false);
			notIncludeDeliveryBtn.setVisible(false);
			notIncludeDelivery();
			updateOrderBtn.setDisable(true);
			return false;
		case "success":
			updateOrderBtn.setDisable(false);
			return true;
		default:
			return false;
		}
	}

	private boolean CheckUserInput(String orderNumber) {
		if (!InputValidation.checkValidText(orderNumber)) {
			errorMsg.setText("Must fill order number");
			return false;
		}
		if (InputValidation.checkSpecialCharacters(orderNumber)) {
			errorMsg.setText("Special characters aren't allowed in order number");
			return false;
		}
		if (InputValidation.checkContainCharacters(orderNumber)) {
			errorMsg.setText("Characters aren't allowed in order number");
			return false;
		}
		errorMsg.setText("");
		return true;
	}

	@FXML
	void includeDeliveryBtnClicked(MouseEvent event) {
		if (notIncludeDeliveryBtn.isSelected()) {
			notIncludeDeliveryBtn.setSelected(false);
		}
		includeDeliveryBtn.setSelected(true);
		includeDelivery();
	}

	@FXML
	void notIncludeDeliveryBtnClicked(MouseEvent event) {
		if (includeDeliveryBtn.isSelected()) {
			includeDeliveryBtn.setSelected(false);
		}
		notIncludeDeliveryBtn.setSelected(true);
		notIncludeDelivery();
	}

	private void orderIsReady() {
		includeDeliveryTxt.setVisible(true);
		includeDeliveryBtn.setVisible(true);
		notIncludeDeliveryBtn.setVisible(true);
	}

	private void includeDelivery() {
		enterPlannedTineTxt.setVisible(true);
		hourBox.setVisible(true);
		hourTxt.setVisible(true);
		minutesBox.setVisible(true);
		minutesTxt.setVisible(true);
	}

	private void notIncludeDelivery() {
		enterPlannedTineTxt.setVisible(false);
		hourBox.setVisible(false);
		hourTxt.setVisible(false);
		minutesBox.setVisible(false);
		minutesTxt.setVisible(false);
	}

	@FXML
	void newMsgClicked(MouseEvent event) {
		if (router.getSendMsgToCustomerController() == null) {
			AnchorPane mainContainer;
			sendMsgToCustomerController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeSendMsgToCustomerPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.setOrderInfo(getOrderNumber(), getStatus(), getDeliveryNumber());
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Message Simulation");
				stage.setScene(mainScene);
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - Message Simulation");
			router.getSendMsgToCustomerController().setOrderInfo(getOrderNumber(), getStatus(), getDeliveryNumber());
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
	 * 
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
	
	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
		setStartPage();
		OrderNumberTxtField.clear();
	}

	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
		deliveryNumber = null;
		setStartPage();
		OrderNumberTxtField.clear();
	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		router.changeSceneToHomePage();
		setStartPage();
		OrderNumberTxtField.clear();
	}

	@FXML
	void returnToSupplierPanel(MouseEvent event) {
		router.returnToSupplierPanel(event);
		setStartPage();
		OrderNumberTxtField.clear();
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
		setStartPage();
		OrderNumberTxtField.clear();
		//setUpdateComboBox();
		hourBox.getSelectionModel().select(String.format("%02d", LocalTime.now().getHour()));
		minutesBox.getSelectionModel().select(String.format("%02d", LocalTime.now().getMinute()));

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

	public String getOrderNumber() {
		orderNumber = OrderNumberTxtField.getText();
		return orderNumber;
	}

	public String getStatus() {
		receivedOrReady = updateDataComboBox.getValue();
		return receivedOrReady;
	}

	public int getDeliveryNumber() {
		if (deliveryNumber == null) {
			deliveryNumber = (int) ClientGUI.client.getLastResponse().getServerResponse();
		}
		return deliveryNumber;
	}

}
