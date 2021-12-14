package Controls;

import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.jdi.connect.spi.Connection;

import Entities.User;
import Enums.UserType;
import client.ClientGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
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

	ObservableList<String> list;

	// creating list of update status order
	private void setUpdateComboBox() {
		ArrayList<String> type = new ArrayList<String>();
		type.add("Order Received");
		type.add("Order Is Ready");

		list = FXCollections.observableArrayList(type);
		updateDataComboBox.setItems(list);
	}

	/**
	 * This method initialized this screen
	 */
	private void setStartPage() {
		updateDataTxt.setVisible(false);
		updateDataComboBox.setVisible(false);
		includeDeliveryTxt.setVisible(false);
		includeDeliveryBtn.setVisible(false);
		notIncludeDeliveryBtn.setVisible(false);
		enterPlannedTineTxt.setVisible(false);
		hourBox.setVisible(false);
		hourTxt.setVisible(false);
		minutesBox.setVisible(false);
		minutesTxt.setVisible(false);
		VImage.setVisible(false);
		successMsg.setVisible(false);
	}

	/**
	 * This method checks if order status update successfully in DB and display a
	 * message to screen
	 * 
	 * @param event
	 */
	@FXML
	void UpdateOrderClicked(MouseEvent event) {
		// need add a check if the data was updated successfully in DB
		if (checkTime()) {
			VImage.setVisible(true);
			successMsg.setVisible(true);
		}
	}


	/**
	 * Private method to check if the input time is valid time.
	 */
	private boolean checkTime() {
		String hourToOrder = hourBox.getSelectionModel().getSelectedItem();
		String minuteToOrder = minutesBox.getSelectionModel().getSelectedItem();
		/** If no time selection was made */
		if (hourToOrder == null&&minuteToOrder == null) {
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

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
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
	 * This function check the order number that entered. if it correct, display the
	 * comboBox to choose what need to update: "Order Received" or "Order Is Ready"
	 * 
	 * @param event
	 */
	@FXML
	void searchClicked(MouseEvent event) {
		// need add a check if the order number is exist in DB
		String orderNumber = OrderNumberTxtField.getText();
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

		if(!checkServerResponse()) {
			return;
		}
		
		//ClientGUI.client.getLastResponse();
		updateDataTxt.setVisible(true);
		updateDataComboBox.setVisible(true);
		updateDataComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal == "Order Is Ready")
				orderIsReady();
			else {
				includeDeliveryTxt.setVisible(false);
				includeDeliveryBtn.setVisible(false);
				notIncludeDeliveryBtn.setVisible(false);
			}
		});
	}
	
	/**
	 * checks the user information received from Server.
	 * display relevant information.
	 */
	private boolean checkServerResponse() {
		if(ClientGUI.client.getLastResponse() == null) {
			return false;
		}
		
		switch (ClientGUI.client.getLastResponse().getMsg().toLowerCase()) {
		case "order number doesn't exist":
			errorMsg.setText("This order doesn't exist");
			return false;
		case "success":
			return true;
		default:
			return false;
		}
	}


	private boolean CheckUserInput(String orderNumber) {
		if (!router.checkValidText(orderNumber)) {
			errorMsg.setText("Must fill order number");
			return false;
		}
		if (router.checkSpecialCharacters(orderNumber)) {
			errorMsg.setText("Special characters aren't allowed in order number");
			return false;
		}
		if (router.checkContainCharacters(orderNumber)) {
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
		setUpdateComboBox();
		setStartPage();
		OrderNumberTxtField.clear();
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
