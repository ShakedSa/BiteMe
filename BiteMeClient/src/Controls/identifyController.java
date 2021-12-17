package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Entities.Customer;
import Entities.Order;
import Enums.UserType;
import Util.InputValidation;
import Util.QRReader;
import client.ClientGUI;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.util.Duration;

/**
 * @author Eden Controller for customer identification by QR code/W4C id
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
	private ImageView leftArrowBtn;

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
	void QRClicked(MouseEvent event) {
		Customer user = (Customer) ClientGUI.client.getUser().getServerResponse();
		String qrCode = QRReader.ReadQRCode(user); // Reading qr simulation.

		/**
		 * Simulating the qrcode reading.
		 */
		final IntegerProperty i = new SimpleIntegerProperty(0);
		Timeline timeline = new Timeline();
		KeyFrame keyFrame = new KeyFrame(Duration.millis(150), e -> {
			if (i.get() > qrCode.length()) {
				/** when the keyframe reach the end of the string stop and identify. */
				timeline.stop();
				identifyClicked(null);
			} else {
				w4cCodeFieldTxt.setText(qrCode.substring(0, i.get()));
				i.set(i.get() + 1);
			}
		});
		timeline.getKeyFrames().add(keyFrame);
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	/**
	 * Onclick event handler for w4c code identification.
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
		Customer user = (Customer) ClientGUI.client.getUser().getServerResponse();
		if (user.getW4c().getW4CID() != Integer.parseInt(w4cCode) && !user.getW4c().getQRCode().equals(w4cCode)) {
			errorMsg.setText("W4C code is incorrect.\nPlease try again or use the QR identification option.");
			return;
		}
		errorMsg.setText("");
		changeToRestaurantMenuPage();
	}

	private void changeToRestaurantMenuPage() {
		w4cCodeFieldTxt.clear();
		router = Router.getInstance();
		Order newOrder = new Order();
		newOrder.setRestaurantName(restaurantsName);
		router.setOrder(newOrder);
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
			stage.show();
		}
	}

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	/**
	 * Onclick event listener to change scene back to home page.
	 */
	@FXML
	void returnToHomePage(MouseEvent event) {
		w4cCodeFieldTxt.clear();
		router.changeSceneToHomePage();
	}

	/**
	 * Onclick event listener to change scene back to restaurants selection page.
	 */
	@FXML
	void returnToRestaurants(MouseEvent event) {
		w4cCodeFieldTxt.clear();
		router.getRestaurantselectionController().setItemsCounter();
		stage.setTitle("BiteMe - Restaurants");
		stage.setScene(router.getRestaurantselectionController().getScene());
		stage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setIdentifyController(this);
		setStage(router.getStage());
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

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

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}

	public void setItemsCounter() {
		itemsCounter.setText(router.getBagItems().size() + "");
	}
}
