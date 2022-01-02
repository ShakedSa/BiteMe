package Controls;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;

import Util.InputValidation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controller for the view of the date and time selection page.
 * 
 * @author Shaked
 */
public class pickDateAndTimeController implements Initializable {

	private Router router;

	private Stage stage;

	private Scene scene;

	private String restaurantName;

	@FXML
	private Rectangle avatar;

	@FXML
	private DatePicker datePicker;

	@FXML
	private Text homePageBtn;

	@FXML
	private ComboBox<String> hourBox;

	@FXML
	private Text itemsCounter;

	@FXML
	private Rectangle leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private ComboBox<String> minutesBox;

	@FXML
	private Text profileBtn;

	@FXML
	private Text restaurantsBtn;

	@FXML
	private Label nextOrderStep;

	@FXML
	private Label errorMsg;

	/**
	 * Method that moves the user to the next step of the order process.
	 * 
	 * @param event
	 */
	@FXML
	void nextOrderStep(MouseEvent event) {
		if (!checkDate()) {
			return;
		}
		if (!checkTime()) {
			return;
		}
		errorMsg.setText("");
		/** Set date and time for order */
		LocalDate orderDate = datePicker.getValue();
		String hourToOrder = hourBox.getSelectionModel().getSelectedItem();
		String minuteToOrder = minutesBox.getSelectionModel().getSelectedItem();

		String newTime = orderDate.toString() + " " + hourToOrder + ":" + minuteToOrder;
		router.getOrder().setDateTime(newTime);
		errorMsg.setText("");
		changeToDelivery();
	}

	/**
	 * Method to change scene to My Cart.
	 * 
	 * @param event
	 */
	@FXML
	public void changeToCart(MouseEvent event) {
		router.changeToMyCart("DateTime");
	}

	/**
	 * Method that switch the scene to the next step of the order process.
	 */
	private void changeToDelivery() {
		router = Router.getInstance();
		if (router.getDeliveryMethodController() == null) {
			AnchorPane mainContainer;
			deliveryMethodController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeSelectDeliveryMethodPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.setItemsCounter();
				controller.createCombo();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Select Delivery Method");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException ex) {
				ex.printStackTrace();
				return;
			}
		} else {
			router.getDeliveryMethodController().setAvatar();
			router.getDeliveryMethodController().setItemsCounter();
			router.getDeliveryMethodController().createCombo();
			stage.setTitle("BiteMe - Select Delivery Method");
			stage.setScene(router.getDeliveryMethodController().getScene());
			stage.show();
		}
	}

	/**
	 * Private method to check if the input date is today's date.
	 */
	private boolean checkDate() {
		LocalDate orderDate = datePicker.getValue();
		if (orderDate == null) {
			errorMsg.setText("Please pick date");
			return false;
		}
		if (!InputValidation.checkDateNow(orderDate)) {
			errorMsg.setText("Date must be today :" + LocalDate.now().toString());
			return false;
		}
		return true;
	}

	/**
	 * Private method to check if the input time is valid time.
	 */
	private boolean checkTime() {
		String hourToOrder = hourBox.getSelectionModel().getSelectedItem();
		String minuteToOrder = minutesBox.getSelectionModel().getSelectedItem();
		/** If no time selection was made */
		if (!InputValidation.checkValidText(hourToOrder) || !InputValidation.checkValidText(minuteToOrder)) {
			errorMsg.setText("Please pick time for the order.");
			return false;
		}
		if (InputValidation.checkContainCharacters(hourToOrder)
				|| InputValidation.checkContainCharacters(minuteToOrder)) {
			errorMsg.setText("Time should be integers.");
			return false;
		}
		/** Checking if the inputed time is valid. */
		LocalTime selectedTime = LocalTime.of(Integer.parseInt(hourToOrder), Integer.parseInt(minuteToOrder));
		switch (LocalTime.now().compareTo(selectedTime)) {
		case 1:
			errorMsg.setText("Time of order must be greater or equals to now: " + LocalTime.now().toString());
			return false;
		}
		LocalTime now = LocalTime.now();
		int choosedTime = Integer.parseInt(hourToOrder) * 60 + Integer.parseInt(minuteToOrder);
		int nowTime = now.getHour() * 60 + now.getMinute();
		if (choosedTime - nowTime > 60) {
			errorMsg.setText("Time for order must be up to 1 hour from now.");
			return false;
		}
		/** If time selection is before now */
		return true;
	}

	/**
	 * Method to log out the user.
	 * 
	 * @param event
	 */
	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	/**
	 * Method to switch scene to the user's profile.
	 * 
	 * @param event
	 */
	@FXML
	void openProfile(MouseEvent event) {
		router.showProfile();
	}

	/**
	 * Method to return to Home Page.
	 * 
	 * @param event
	 */
	@FXML
	void returnToHomePage(MouseEvent event) {
		router.changeSceneToHomePage();
	}

	/**
	 * Method that switch to the previous step of the order process.
	 * 
	 * @param event
	 * */
	@FXML
	void returnToMenuOrderPage(MouseEvent event) {
		router.getRestaurantMenuController().setItemsCounter();
		router.getRestaurantMenuController().setAvatar();
		router.getRestaurantMenuController().setRestaurantName(restaurantName);
		stage.setTitle("BiteMe - " + restaurantName + " Menu");
		stage.setScene(router.getRestaurantMenuController().getScene());
		stage.show();
	}

	/**
	 * Method that returns the user to the restaurant selection page.
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
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		router.setAvatar(avatar);
	}

	/**
	 * Initialize method. A required method from the Initializable interface.
	 * 
	 * @param location
	 * @param resources
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setPickDateAndTimeController(this);
		setStage(router.getStage());
		router.setArrow(leftArrowBtn, -90);
		/** Setting the time and date to now. */
		datePicker.setValue(LocalDate.now());
		datePicker.setEditable(false);
		incCurrentHourByOne();
	}

	/**
	 * this method incCurrentHour By One with all the cases taking care of
	 */
	private void incCurrentHourByOne() {
		int hour = LocalTime.now().getHour();
		int minute = LocalTime.now().getMinute();

		if (minute == 59) {
			minute = 0;
			if (hour == 23) {
				hour = 0;
				Calendar date = Calendar.getInstance();
				date.add(Calendar.DATE, 1);
				datePicker.setValue(convertToLocalDateViaInstant(date.getTime())); //
			} else {
				hour++;
			}
		} else {
			minute++;
		}
		hourBox.getSelectionModel().select(String.format("%02d", hour));
		minutesBox.getSelectionModel().select(String.format("%02d", minute));

	}

	/**
	 * method to convert date type to localDate
	 * 
	 * @param dateToConvert
	 * @return
	 */
	private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
		return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	/**
	 * Passing scene's reference.
	 * 
	 * @param scene
	 */
	public void setScene(Scene scene) {
		this.scene = scene;
	}

	/**
	 * Getting this controller's scene.
	 * 
	 * @return Scene
	 */
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

	/**
	 * Setting the restaurant that was choosen by the user.
	 * 
	 * @param resName
	 * */
	public void setRestaurant(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	/**
	 * Method to display the cart's item amount in this scene.
	 */
	public void setItemsCounter() {
		itemsCounter.setText(router.getBagItems().size() + "");
	}

	/**
	 * Setting values for the combo boxes. hourBox values from 0-23, minutesBox
	 * values from 0-59.
	 */
	public void createCombos() {
		ObservableList<String> hourOptions = FXCollections.observableArrayList(Arrays.asList(router.generator(24)));
		hourBox.getItems().clear();
		hourBox.getItems().addAll(hourOptions);
		ObservableList<String> minuteOptions = FXCollections.observableArrayList(Arrays.asList(router.generator(60)));
		minutesBox.getItems().clear();
		minutesBox.getItems().addAll(minuteOptions);
	}
}
