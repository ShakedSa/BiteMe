package Controls;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Entities.Delivery;
import Entities.PreorderDelivery;
import Entities.SharedDelivery;
import Enums.TypeOfOrder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class deliveryMethodController implements Initializable {

	private Router router;

	private Stage stage;

	private Scene scene;

	@FXML
	private Rectangle avatar;

	@FXML
	private ComboBox<String> deliveryMethodBox;

	@FXML
	private Label errorMsg;

	@FXML
	private Text homePageBtn;

	@FXML
	private Text itemsCounter;

	@FXML
	private ImageView leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Label nextOrderStep;

	@FXML
	private Text profileBtn;

	@FXML
	private Text restaurantsBtn;

	/**
	 * All vars below are hidden by default. showing base on the the selected
	 * delivery method
	 */
	@FXML
	private Text addStar;

	@FXML
	private Text addressText;

	@FXML
	private TextField addressTxtField;

	@FXML
	private Text amntStar;

	@FXML
	private TextField amountTextField;

	@FXML
	private Text firstNameText;

	@FXML
	private TextField firstNameTxtField;

	@FXML
	private Text fnameStar;

	@FXML
	private ComboBox<String> hourBox;

	@FXML
	private ComboBox<String> minutesBox;

	@FXML
	private Text lastNameText;

	@FXML
	private TextField lastNameTxtField;

	@FXML
	private Text lnameStar;

	@FXML
	private Text phoneNumberText;

	@FXML
	private TextField phoneNumberTxtField;

	@FXML
	private Text pickStar;

	@FXML
	private Text pnumberStar;

	@FXML
	private ComboBox<String> prefixPhoneNumberBox;

	@FXML
	private Text includeFee;

	@FXML
	private Text requires;

	@FXML
	private Text amoutOfPeopleText;

	@FXML
	private Text pickTimeTxt;

	@FXML
	private Text hoursText;

	@FXML
	private Text minutesText;

	@FXML
	private Text businessCodeText;

	@FXML
	private TextField businessCodeTextField;

	@FXML
	private Text businessStar;

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	/**
	 * Checking if all the fields in the basic delivery are filled and valid.
	 * 
	 * @return boolean
	 */
	private boolean checkBasic() {
		String address = addressTxtField.getText();
		String firstName = firstNameTxtField.getText();
		String lastName = lastNameTxtField.getText();
		String phoneNumberPrefix = prefixPhoneNumberBox.getSelectionModel().getSelectedItem();
		String phoneNumber = phoneNumberTxtField.getText();
		if (!checkInput(address)) {
			errorMsg.setText("Please fill address for the delivery.");
			return false;
		}
		if (checkSpecialCharacters(address)) {
			errorMsg.setText("Address can't contain special characters.");
			return false;
		}
		if (!checkInput(firstName)) {
			errorMsg.setText("Please fill first name.");
			return false;
		}
		if (checkSpecialCharacters(firstName)) {
			errorMsg.setText("First name can't contain special characters.");
			return false;
		}
		if (!checkInput(lastName)) {
			errorMsg.setText("Please fill last name.");
			return false;
		}
		if (checkSpecialCharacters(lastName)) {
			errorMsg.setText("Last name can't contain special characters.");
			return false;
		}
		if (!checkInput(phoneNumberPrefix) || !checkInput(phoneNumber)) {
			errorMsg.setText("Please fill phone number.");
			return false;
		}
		if (checkSpecialCharacters(phoneNumberPrefix) || checkSpecialCharacters(phoneNumber)) {
			errorMsg.setText("Phone number can't contain special characters.");
		}
		return true;
	}

	/**
	 * Checks if the string contains special characters.
	 * 
	 * @param input
	 * 
	 * @return boolean
	 */
	public boolean checkSpecialCharacters(String input) {
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(input);
		return m.find();
	}

	/**
	 * Checking if input is null or empty string.
	 * 
	 * @param input
	 * 
	 * @return boolean
	 */
	private boolean checkInput(String input) {
		if (input == null || input.equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * Checking if the shared's delivery fields are all field and are legal values.
	 * 
	 * @return boolean
	 */
	public boolean checkShared() {
		if (!checkBasic()) {
			return false;
		}
		String amount = amountTextField.getText();
		String businessCode = businessCodeTextField.getText();
		if (!checkInput(amount)) {
			errorMsg.setText("Please fill amount of people in the order.");
			return false;
		}
		if (checkSpecialCharacters(amount)) {
			errorMsg.setText("Amount of people can't contain special characters.");
			return false;
		}
		if (!checkInput(businessCode)) {
			errorMsg.setText("Please fill business code.");
			return false;
		}
		if (checkSpecialCharacters(businessCode)) {
			errorMsg.setText("Business code can't contain special characters");
			return false;
		}
		return true;
	}

	/**
	 * Checking if the preorder's delivery fields are all field and are legal
	 * values.
	 * 
	 * @return boolean
	 */
	private boolean checkPre() {
		if (!checkBasic()) {
			return false;
		}
		String hours = hourBox.getSelectionModel().getSelectedItem();
		String minutes = minutesBox.getSelectionModel().getSelectedItem();
		if (!checkInput(hours) || !checkInput(minutes)) {
			errorMsg.setText("Please fill time of the delivery");
			return false;
		}
		if (checkSpecialCharacters(hours) || checkSpecialCharacters(minutes)) {
			errorMsg.setText("Delivery time can't contain special characters");
			return false;
		}
		LocalTime now = LocalTime.now();
		LocalTime orderTime = LocalTime.of(Integer.parseInt(hours), Integer.parseInt(minutes));
		/** If time selection is before now */
		if (now.compareTo(orderTime) == 1) {
			errorMsg.setText("Time of order must be greater or equals to now: " + now.toString());
			return false;
		}
		if (Integer.parseInt(hours) - now.getHour() > 2) {
			errorMsg.setText("Preorder deliverys are up to 2 hours");
		}
		return true;
	}

	@FXML
	void nextOrderStep(MouseEvent event) {
		String selectedMethod = deliveryMethodBox.getSelectionModel().getSelectedItem();
		if (selectedMethod == null) {
			errorMsg.setText("Please select delivery method.");
			return;
		}
		if (selectedMethod.equals("Robot Delivery")) {
			errorMsg.setText("Robot delivery is not available yet.\nPlease select a different delivery method.");
			return;
		}
		TypeOfOrder typeOfOrder = TypeOfOrder.getEnum(selectedMethod);
		Delivery newDelivery;
		String address = addressTxtField.getText();
		String firstName = firstNameTxtField.getText();
		String lastName = lastNameTxtField.getText();
		String phoneNumberPrefix = prefixPhoneNumberBox.getSelectionModel().getSelectedItem();
		String phoneNumber = phoneNumberPrefix + "-" + phoneNumberTxtField.getText();
		switch (typeOfOrder) {
		case BasicDelivery:
			if (!checkBasic()) {
				return;
			}
			newDelivery = new Delivery(address, firstName, lastName, phoneNumber, 25, 0);
			break;
		case preorderDelivery:
			if (!checkPre()) {
				return;
			}
			String hours = hourBox.getSelectionModel().getSelectedItem();
			String minutes = minutesBox.getSelectionModel().getSelectedItem();
			String time = hours + ":" + minutes;
			newDelivery = new PreorderDelivery(address, firstName, lastName, phoneNumber, 25, 0, time);
			break;
		case takeaway:
			newDelivery = null;
			break;
		case sharedDelivery:
			if (!checkShared()) {
				return;
			}
			String amount = amountTextField.getText();
			String businessCode = businessCodeTextField.getText();
			newDelivery = new SharedDelivery(address, firstName, lastName, phoneNumber, 25, 0, businessCode,
					Integer.parseInt(amount));
			break;
		default:
			return;
		}
		router.setDelivery(newDelivery);
		errorMsg.setText("");
		changeToPaymentMethod();
	}
	
	private void changeToPaymentMethod() {
		if (router.getPaymentController() == null) {
			AnchorPane mainContainer;
			paymentController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemePaymentPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.setItemsCounter();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Select Payment Method");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException ex) {
				ex.printStackTrace();
				return;
			}
		} else {
			router.getPaymentController().setAvatar();
			router.getPaymentController().setItemsCounter();
			stage.setTitle("BiteMe - Select Payment Method");
			stage.setScene(router.getPaymentController().getScene());
			stage.show();
		}
	}

	@FXML
	void openProfile(MouseEvent event) {
		router.showProfile();
	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		router.getHomePageController().setItemsCounter();
		router.getHomePageController().setAvatar();
		stage.setTitle("BiteMe - HomePage");
		stage.setScene(router.getHomePageController().getScene());
		stage.show();
	}

	@FXML
	void returnToPickDateAndTime(MouseEvent event) {
		router.getPickDateAndTimeController().setItemsCounter();
		router.getPickDateAndTimeController().setAvatar();
		stage.setTitle("BiteMe - Pick Date And Time");
		stage.setScene(router.getPickDateAndTimeController().getScene());
		stage.show();
	}

	@FXML
	void returnToRestaurants(MouseEvent event) {
		router.getRestaurantselectionController().setItemsCounter();
		router.getRestaurantMenuController().setAvatar();
		stage.setTitle("BiteMe - Restaurants");
		stage.setScene(router.getRestaurantselectionController().getScene());
		stage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setDeliveryMethodController(this);
		setStage(router.getStage());
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

	public void setItemsCounter() {
		itemsCounter.setText(router.getBagItems().size() + "");
	}

	public void setAvatar() {
		router.setAvatar(avatar);
	}

	/**
	 * Creating the combo boxes in this scene. for deliveryMethodBox set on change
	 * event listener to change the state of the scene accordingly to the selected
	 * value.
	 */
	public void createCombo() {
		ObservableList<String> typeOfOrders = FXCollections
				.observableArrayList(Arrays.asList(TypeOfOrder.BasicDelivery.toString(),
						TypeOfOrder.preorderDelivery.toString(), TypeOfOrder.sharedDelivery.toString(),
						TypeOfOrder.takeaway.toString(), TypeOfOrder.RobotDelivery.toString()));
		deliveryMethodBox.getItems().addAll(typeOfOrders);
		/**
		 * Setting on change event listener.
		 */
		deliveryMethodBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			TypeOfOrder typeOfOrder = TypeOfOrder.getEnum(newVal);
			errorMsg.setText("");
			switch (typeOfOrder) {
			case BasicDelivery:
				displayBasic();
				break;
			case sharedDelivery:
				displayShared();
				break;
			case preorderDelivery:
				displayPre();
				break;
			case takeaway:
				hideAll();
				break;
			case RobotDelivery:
				hideAll();
				errorMsg.setText("Robot delivery is not available yet.\nPlease select a different delivery method.");
			default:
				hideAll();
				return;
			}
		});
		ObservableList<String> hourOptions = FXCollections.observableArrayList(Arrays.asList(generator(24)));
		hourBox.getItems().addAll(hourOptions);
		ObservableList<String> minuteOptions = FXCollections.observableArrayList(Arrays.asList(generator(60)));
		minutesBox.getItems().addAll(minuteOptions);
		ObservableList<String> phonePrefix = FXCollections
				.observableArrayList(Arrays.asList("050", "052", "053", "054", "055", "057", "058"));
		prefixPhoneNumberBox.getItems().addAll(phonePrefix);
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
	 * Hiding by default all the texts fields for the delivery information.
	 */
	private void hideAll() {
		addressText.setVisible(false);
		addressTxtField.setVisible(false);
		addStar.setVisible(false);
		firstNameText.setVisible(false);
		firstNameTxtField.setVisible(false);
		fnameStar.setVisible(false);
		lastNameText.setVisible(false);
		lastNameTxtField.setVisible(false);
		lnameStar.setVisible(false);
		phoneNumberText.setVisible(false);
		phoneNumberTxtField.setVisible(false);
		prefixPhoneNumberBox.setVisible(false);
		pnumberStar.setVisible(false);
		includeFee.setVisible(false);
		requires.setVisible(false);

		amountTextField.setVisible(false);
		amntStar.setVisible(false);
		amoutOfPeopleText.setVisible(false);
		businessCodeText.setVisible(false);
		businessCodeTextField.setVisible(false);
		businessStar.setVisible(false);

		pickStar.setVisible(false);
		pickTimeTxt.setVisible(false);
		hourBox.setVisible(false);
		minutesBox.setVisible(false);
		hoursText.setVisible(false);
		minutesText.setVisible(false);
	}

	/**
	 * Displaying the basic delivery fields that are required to fill by the user.
	 */
	private void displayBasic() {
		addressText.setVisible(true);
		addressTxtField.setVisible(true);
		addStar.setVisible(true);
		firstNameText.setVisible(true);
		firstNameTxtField.setVisible(true);
		fnameStar.setVisible(true);
		lastNameText.setVisible(true);
		lastNameTxtField.setVisible(true);
		lnameStar.setVisible(true);
		phoneNumberText.setVisible(true);
		phoneNumberTxtField.setVisible(true);
		prefixPhoneNumberBox.setVisible(true);
		pnumberStar.setVisible(true);
		includeFee.setVisible(true);
		requires.setVisible(true);

		amountTextField.setVisible(false);
		amntStar.setVisible(false);
		amoutOfPeopleText.setVisible(false);
		businessCodeText.setVisible(false);
		businessCodeTextField.setVisible(false);
		businessStar.setVisible(false);

		pickStar.setVisible(false);
		pickTimeTxt.setVisible(false);
		hourBox.setVisible(false);
		minutesBox.setVisible(false);
		hoursText.setVisible(false);
		minutesText.setVisible(false);
	}

	/**
	 * Displaying the shared delivery fields that are required to fill by the user.
	 */
	private void displayShared() {
		displayBasic();
		amountTextField.setVisible(true);
		amntStar.setVisible(true);
		amoutOfPeopleText.setVisible(true);
		businessCodeText.setVisible(true);
		businessCodeTextField.setVisible(true);
		businessStar.setVisible(true);
	}

	/**
	 * Displaying the preorder delivery fields that are required to fill by the
	 * user.
	 */
	private void displayPre() {
		displayBasic();
		pickStar.setVisible(true);
		pickTimeTxt.setVisible(true);
		hourBox.setVisible(true);
		minutesBox.setVisible(true);
		hoursText.setVisible(true);
		minutesText.setVisible(true);
	}

}
