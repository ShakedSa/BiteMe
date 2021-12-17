package Controls;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Entities.Customer;
import Entities.Delivery;
import Entities.OrderDeliveryMethod;
import Entities.PreorderDelivery;
import Entities.SharedDelivery;
import Entities.W4CCard;
import Enums.TypeOfOrder;
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

	/*
	 * All variables below are hidden by default. showing base on the the selected
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
		if (!InputValidation.checkValidText(address)) {
			errorMsg.setText("Please fill address for the delivery.");
			return false;
		}
		if (InputValidation.checkSpecialCharacters(address)) {
			errorMsg.setText("Address can't contain special characters.");
			return false;
		}
		if (!InputValidation.checkValidText(firstName)) {
			errorMsg.setText("Please fill first name.");
			return false;
		}
		if (InputValidation.checkSpecialCharacters(firstName)) {
			errorMsg.setText("First name can't contain special characters.");
			return false;
		}
		if (!InputValidation.checkValidText(lastName)) {
			errorMsg.setText("Please fill last name.");
			return false;
		}
		if (InputValidation.checkSpecialCharacters(lastName)) {
			errorMsg.setText("Last name can't contain special characters.");
			return false;
		}
		if (!InputValidation.checkValidText(phoneNumberPrefix) || !InputValidation.checkValidText(phoneNumber)) {
			errorMsg.setText("Please fill phone number.");
			return false;
		}
		String fullPhoneNumber = phoneNumberPrefix + phoneNumber;
		if (!InputValidation.checkPhoneNumber(fullPhoneNumber)) {
			errorMsg.setText("Invalid phone number.");
			return false;
		}
		return true;
	}

	/**
	 * Checking if the shared's delivery fields are all field and are legal values.
	 * 
	 * @param typeOfOrder
	 * 
	 * @return boolean
	 */
	public boolean checkShared(TypeOfOrder typeOfOrder) {
		if (!checkBasic()) {
			return false;
		}
		String amount = amountTextField.getText();
		String businessCode = businessCodeTextField.getText();
		if (!InputValidation.checkValidText(amount)) {
			errorMsg.setText("Please fill amount of people in the order.");
			return false;
		}
		if (InputValidation.checkSpecialCharacters(amount)) {
			errorMsg.setText("Amount of people can't contain special characters.");
			return false;
		}
		if (!InputValidation.checkValidText(businessCode)) {
			errorMsg.setText("Please fill business code.");
			return false;
		}
		if (InputValidation.checkSpecialCharacters(businessCode)) {
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
		if (!InputValidation.checkValidText(hours) || !InputValidation.checkValidText(minutes)) {
			errorMsg.setText("Please fill time of the delivery");
			return false;
		}
		if (InputValidation.checkSpecialCharacters(hours) || InputValidation.checkSpecialCharacters(minutes)) {
			errorMsg.setText("Delivery time can't contain special characters");
			return false;
		}
		int hour = Integer.parseInt(hours);
		int minute = Integer.parseInt(minutes);
		LocalTime now = LocalTime.now();
		LocalTime orderTime = LocalTime.of(hour, minute);
		/** If time selection is before now */
		if (now.compareTo(orderTime) == 1) {
			errorMsg.setText("Time of order must be greater or equals to now: " + now.toString());
			return false;
		}
		/** Preorder is up to 2 hours from the order time. */
		if (hour - now.getHour() > 2) {
			errorMsg.setText("Preorder deliverys are up to 2 hours");
			return false;
		}
		if (hour - now.getHour() == 2) {
			if (minute > now.getMinute()) {
				errorMsg.setText("Preorder deliverys are up to 2 hours");
				return false;
			}
		}
		return true;
	}

	/**
	 * On click event listener. <br>
	 * Checking all the inputs of the user, if all the inputs are valid calculate
	 * the final price of the order, else display an appropriate message to the user
	 * that the information is not sufficient.
	 * 
	 * @param event
	 */
	@FXML
	void nextOrderStep(MouseEvent event) {
		/** Validating method selection. */
		String selectedMethod = deliveryMethodBox.getSelectionModel().getSelectedItem();
		if (selectedMethod == null) {
			errorMsg.setText("Please select delivery method.");
			return;
		}
		TypeOfOrder typeOfOrder = TypeOfOrder.getEnum(selectedMethod);
		Delivery newDelivery;
		String address = addressTxtField.getText();
		String firstName = firstNameTxtField.getText();
		String lastName = lastNameTxtField.getText();
		String phoneNumberPrefix = prefixPhoneNumberBox.getSelectionModel().getSelectedItem();
		String phoneNumber = phoneNumberPrefix + phoneNumberTxtField.getText();
		switch (typeOfOrder) {
		case BasicDelivery:
			/** Validating the delivery input. */
			if (!checkBasic()) {
				return;
			}
			newDelivery = new Delivery(address, firstName, lastName, phoneNumber, 25, 0);
			break;
		case preorderDelivery:
			/** Validating the delivery input. */
			if (!checkPre()) {
				return;
			}
			String hours = hourBox.getSelectionModel().getSelectedItem();
			String minutes = minutesBox.getSelectionModel().getSelectedItem();
			String time = hours + ":" + minutes;
			newDelivery = new PreorderDelivery(address, firstName, lastName, phoneNumber, 25, 0, time);
			break;
		case takeaway:
			/** Take away doesn't have delivery --> delivery method should be null */
			newDelivery = new Delivery(null, firstName, lastName, phoneNumber, 0, 0);
			break;
		case sharedDelivery:
			/** Validating the delivery input. */
			if (!checkShared(typeOfOrder)) {
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
		/**
		 * Setting the delivery in the delivery state of the application.
		 */
		router.setDelivery(newDelivery);
		Customer user = (Customer) ClientGUI.client.getUser().getServerResponse();
		/**
		 * Setting the connector class of delivery order and user.
		 */
		OrderDeliveryMethod orderDeliveryMethod = new OrderDeliveryMethod(router.getOrder(), newDelivery, typeOfOrder,
				user);
		router.setOrderDeliveryMethod(orderDeliveryMethod);
		errorMsg.setText("");
		/**
		 * Calculating the final price of the order.
		 */
		router.getOrderDeliveryMethod().calculateFinalPrice();
		changeToPaymentMethod();
	}

	/**
	 * Method to switch scene to the payment method selection page.
	 */
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

	/**
	 * Switch to profile scene.
	 * 
	 * @param event
	 */
	@FXML
	void openProfile(MouseEvent event) {
		router.showProfile();
	}

	/**
	 * Switch to home page scene.
	 * 
	 * @param event
	 */
	@FXML
	void returnToHomePage(MouseEvent event) {
		router.changeSceneToHomePage();
	}

	/**
	 * Returning to the previous step in the order process.
	 * 
	 * @param event
	 */
	@FXML
	void returnToPickDateAndTime(MouseEvent event) {
		router.getPickDateAndTimeController().setItemsCounter();
		router.getPickDateAndTimeController().setAvatar();
		stage.setTitle("BiteMe - Pick Date And Time");
		stage.setScene(router.getPickDateAndTimeController().getScene());
		stage.show();
	}

	/**
	 * Switch to restaurants page.
	 * 
	 * @param event
	 */
	@FXML
	void returnToRestaurants(MouseEvent event) {
		router.getRestaurantselectionController().setItemsCounter();
		router.getRestaurantMenuController().setAvatar();
		stage.setTitle("BiteMe - Restaurants");
		stage.setScene(router.getRestaurantselectionController().getScene());
		stage.show();
	}

	/**
	 * Initializing the router.
	 * 
	 * @param location
	 * @param resources
	 */
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

	/**
	 * Setting the item counter of the order.
	 */
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
		Customer customer = (Customer) ClientGUI.client.getUser().getServerResponse();
		W4CCard w4cCard = customer.getW4c();
		ObservableList<String> typeOfOrders;
		if (w4cCard.getEmployerID() == null || w4cCard.getEmployerID().equals("")) {
			typeOfOrders = FXCollections.observableArrayList(
					Arrays.asList(TypeOfOrder.BasicDelivery.toString(), TypeOfOrder.preorderDelivery.toString(),
							TypeOfOrder.takeaway.toString(), TypeOfOrder.RobotDelivery.toString()));
		} else {
			typeOfOrders = FXCollections.observableArrayList(Arrays.asList(TypeOfOrder.BasicDelivery.toString(),
					TypeOfOrder.preorderDelivery.toString(), TypeOfOrder.sharedDelivery.toString(),
					TypeOfOrder.takeaway.toString(), TypeOfOrder.RobotDelivery.toString()));
		}
		deliveryMethodBox.getItems().addAll(typeOfOrders);
		/**
		 * Setting on change event listener. display the appropriate fields base on the
		 * choosen delivery method.
		 */
		deliveryMethodBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			TypeOfOrder typeOfOrder = TypeOfOrder.getEnum(newVal);
			nextOrderStep.setDisable(false);
			errorMsg.setText("");
			hideBaseOnSelection(typeOfOrder);
		});
		/** Creating the hours and minutes combo boxes for the preorder delivery */
		ObservableList<String> hourOptions = FXCollections.observableArrayList(Arrays.asList(router.generator(24)));
		hourBox.getItems().addAll(hourOptions);
		hourBox.getSelectionModel().select(0);
		ObservableList<String> minuteOptions = FXCollections.observableArrayList(Arrays.asList(router.generator(60)));
		minutesBox.getItems().addAll(minuteOptions);
		minutesBox.getSelectionModel().select(0);
		/** Creating the phone's prefix combo box */
		ObservableList<String> phonePrefix = FXCollections
				.observableArrayList(Arrays.asList("050", "052", "053", "054", "055", "057", "058"));
		prefixPhoneNumberBox.getItems().addAll(phonePrefix);
		prefixPhoneNumberBox.getSelectionModel().select("050");
	}

	/**
	 * Displaying the fields of the delivery informatin based on TypeOfOrder
	 * 
	 * @param typeOfOrder
	 */
	private void hideBaseOnSelection(TypeOfOrder typeOfOrder) {
		switch (typeOfOrder) {
		case BasicDelivery:
			displayBasic(true);
			displayShared(false);
			displayPre(false);
			break;
		case sharedDelivery:
			displayBasic(true);
			displayShared(true);
			displayPre(false);
			break;
		case preorderDelivery:
			displayBasic(true);
			displayShared(false);
			displayPre(true);
			break;
		case takeaway:
			displayBasic(true);
			displayShared(false);
			displayPre(false);
			break;
		case RobotDelivery:
			errorMsg.setText("Robot delivery is not available yet.\nPlease select a different delivery method.");
			nextOrderStep.setDisable(true);
		default:
			displayBasic(false);
			displayShared(false);
			displayPre(false);
			break;
		}
	}

	/**
	 * Displaying the basic delivery fields that are required to fill by the user.
	 * 
	 * @param val
	 */
	private void displayBasic(boolean val) {
		addressText.setVisible(val);
		addressTxtField.setVisible(val);
		addStar.setVisible(val);
		firstNameText.setVisible(val);
		firstNameTxtField.setVisible(val);
		fnameStar.setVisible(val);
		lastNameText.setVisible(val);
		lastNameTxtField.setVisible(val);
		lnameStar.setVisible(val);
		phoneNumberText.setVisible(val);
		phoneNumberTxtField.setVisible(val);
		prefixPhoneNumberBox.setVisible(val);
		pnumberStar.setVisible(val);
		includeFee.setVisible(val);
		requires.setVisible(val);
	}

	/**
	 * Displaying the shared delivery fields that are required to fill by the user.
	 * 
	 * @param val
	 */
	private void displayShared(boolean val) {
		amountTextField.setVisible(val);
		amntStar.setVisible(val);
		amoutOfPeopleText.setVisible(val);
		businessCodeText.setVisible(val);
		businessCodeTextField.setVisible(val);
		businessStar.setVisible(val);
	}

	/**
	 * Displaying the preorder delivery fields that are required to fill by the
	 * user.
	 * 
	 * @param val
	 */
	private void displayPre(boolean val) {
		pickStar.setVisible(val);
		pickTimeTxt.setVisible(val);
		hourBox.setVisible(val);
		minutesBox.setVisible(val);
		hoursText.setVisible(val);
		minutesText.setVisible(val);
	}

}
