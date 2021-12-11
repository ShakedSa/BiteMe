package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Entities.Customer;
import Entities.ServerResponse;
import Entities.User;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class identifyController implements Initializable {

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
	void QRClicked(MouseEvent event) {
		Customer user = (Customer) ClientGUI.client.getUser().getServerResponse();
		w4cCodeFieldTxt.setText(user.getW4c().getQRCode());
		identifyClicked(null);
	}

	/**
	 * Checks if the string is not empty.
	 * 
	 * @param String text
	 * @return boolean
	 */
	private boolean checkValidText(String input) {
		if (input == null || input.equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if the string is a number. returns true if found any character besides
	 * 0-9.
	 * 
	 * @param String input
	 * @return boolean
	 */
	public boolean CheckIntegerInput(String input) {
		Pattern p = Pattern.compile("[^0-9]$");
		Matcher m = p.matcher(input);
		return m.find();
	}

	/**
	 * Onclick event handler for w4c code identification.
	 * 
	 * @param event
	 */
	@FXML
	void identifyClicked(MouseEvent event) {
		String w4cCode = w4cCodeFieldTxt.getText();
		if (!checkValidText(w4cCode)) {
			errorMsg.setText("W4C code can't be empty.\nPlease fill the W4C code or use the QR identification option.");
			return;
		}
		if (CheckIntegerInput(w4cCode)) {
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
		if(router.getRestaurantMenuController() == null) {
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

	private ImagePattern getAvatarImage() {
		ServerResponse userResponse = ClientGUI.client.getUser();
		if (userResponse == null) {
			return new ImagePattern(new Image(getClass().getResource("../images/guest-avatar.png").toString()));
		}
		User user = (User) userResponse.getServerResponse();
		if (user == null) {
			return new ImagePattern(new Image(getClass().getResource("../images/guest-avatar.png").toString()));
		}
		switch (user.getUserType()) {
		case Supplier:
			return new ImagePattern(new Image(getClass().getResource("../images/supplier-avatar.png").toString()));
		case BranchManager:
		case CEO:
			return new ImagePattern(new Image(getClass().getResource("../images/manager-avatar.png").toString()));
		case Customer:
		case BusinessCustomer:
			return new ImagePattern(new Image(getClass().getResource("../images/random-user.gif").toString()));
		default:
			return new ImagePattern(new Image(getClass().getResource("../images/guest-avatar.png").toString()));
		}
	}

	/**
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		try {
			avatar.setArcWidth(65);
			avatar.setArcHeight(65);
			ImagePattern pattern = getAvatarImage();
			avatar.setFill(pattern);
			avatar.setEffect(new DropShadow(3, Color.BLACK));
			avatar.setStyle("-fx-border-width: 0");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}

}
