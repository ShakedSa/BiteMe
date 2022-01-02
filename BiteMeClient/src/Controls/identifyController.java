package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Entities.Customer;
import Entities.Order;
import Enums.UserType;
import Util.InputValidation;
import Util.QRReader;
import client.ClientGUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controller for the view of the identification page.
 * 
 * @author Shaked
 */
public class identifyController implements Initializable {

	public final UserType type = UserType.Customer;

	private Router router;

	private Stage stage;

	private Scene scene;

	private String restaurantsName;

	@FXML
	private Button QRBtn;

	@FXML
	private Rectangle avatar;

	@FXML
	private Text homePageBtn;

	@FXML
	private Button identifyBtn;

	@FXML
	private Rectangle leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Text profileBtn;

	@FXML
	private Text restaurantsBtn;

	@FXML
	private TextField w4cCodeFieldTxt;

	@FXML
	private Label errorMsg;

	@FXML
	private Text itemsCounter;

	@FXML
	private Label readingMsg;

	/**
	 * On click event handler for QR Code.<br>
	 * Simulating reading from the user's w4c card.
	 * 
	 * @param event
	 */
	@FXML
	void QRClicked(MouseEvent event) {
		Customer user = (Customer) ClientGUI.getClient().getUser().getServerResponse();
		if (!QRReader.ReadQRCode(user)) {
			errorMsg.setText("Failed to read QR code");
			return;
		} else {
			errorMsg.setText("");
			readingMsg.setText("Reading QR...");
		}
	}

	/**
	 * Method to change scene to My Cart.
	 * 
	 * @param event
	 */
	@FXML
	public void changeToCart(MouseEvent event) {
		router.changeToMyCart("Identify");
	}

	/**
	 * On click event handler for w4c code identification.<br>
	 * Using the user's w4c personal code, not the QR code.<br>
	 * After the authentication switch the scene to the next step in the order
	 * process.
	 * 
	 * @param event
	 */
	@FXML
	void identifyClicked(MouseEvent event) {
		String w4cCode = w4cCodeFieldTxt.getText();
		if (!InputValidation.checkValidText(w4cCode)) {
			errorMsg.setText("W4C code can't be empty.\nPlease fill the W4C code or use the QR identification option.");
			return;
		}
		if (InputValidation.CheckIntegerInput(w4cCode)) {
			errorMsg.setText("W4C code must be only numbers.");
			return;
		}
		Customer user = (Customer) ClientGUI.getClient().getUser().getServerResponse();
		if (user.getW4c().getW4CID() != Integer.parseInt(w4cCode) && !user.getW4c().getQRCode().equals(w4cCode)) {
			errorMsg.setText("W4C code is incorrect.\nPlease try again or use the QR identification option.");
			return;
		}
		errorMsg.setText("");
		changeToRestaurantMenuPage();
	}

	/**
	 * Method that switch the scene to the menu page of the selected restaurant.
	 */
	public void changeToRestaurantMenuPage() {
		Platform.runLater(() -> {
			w4cCodeFieldTxt.clear();
			readingMsg.setText("");
		});
		router.getOrder().setRestaurantName(restaurantsName);
		if (router.getRestaurantMenuController() == null) {
			AnchorPane mainContainer;
			restaurantMenuController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeMenuOrderPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.setRestaurantName(restaurantsName);
				controller.setItemsCounter();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				controller.setMenu();
				stage.setTitle("BiteMe - " + restaurantsName + " Menu");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException ex) {
				ex.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - " + restaurantsName + " Menu");
			stage.setScene(router.getRestaurantMenuController().getScene());
			router.getRestaurantMenuController().setRestaurantName(restaurantsName);
			router.getRestaurantMenuController().setMenu();
			router.getRestaurantMenuController().setItemsCounter();
			stage.show();
		}
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
	 * Method to return to Home Page.
	 * 
	 * @param event
	 */
	@FXML
	void returnToHomePage(MouseEvent event) {
		w4cCodeFieldTxt.clear();
		router.changeSceneToHomePage();
	}

	/**
	 * Method that returns the user to the restaurant selection page.
	 * 
	 * @param event
	 */
	@FXML
	void returnToRestaurants(MouseEvent event) {
		w4cCodeFieldTxt.clear();
		router.getRestaurantselectionController().setItemsCounter();
		stage.setTitle("BiteMe - Restaurants");
		stage.setScene(router.getRestaurantselectionController().getScene());
		stage.show();
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
		router.setIdentifyController(this);
		setStage(router.getStage());
		router.setArrow(leftArrowBtn, -90);
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
	 * Saving selected restaurant to order from.
	 *
	 * @param restaurantName
	 */
	public void setRestaurantToOrder(String restaurantName) {
		this.restaurantsName = restaurantName;
	}

	/**
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		router.setAvatar(avatar);
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
	 * Method to switch scene to the user's profile.
	 * 
	 * @param event
	 */
	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}

	/**
	 * Method to display the cart's item amount in this scene.
	 */
	public void setItemsCounter() {
		itemsCounter.setText(router.getBagItems().size() + "");
	}

	/**
	 * Updating the w4c code text field.
	 * 
	 * @param input
	 */
	public void updateTextField(String input) {
		w4cCodeFieldTxt.setText(input);
	}
}
