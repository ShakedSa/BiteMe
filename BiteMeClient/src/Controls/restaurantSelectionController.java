package Controls;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import Entities.ServerResponse;
import Entities.User;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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

public class restaurantSelectionController implements Initializable {

	private Router router;

	private Stage stage;

	private Scene scene;

	private List<ImageView> resImages;

	private List<Text> resNameTexts;

	private List<Label> resOrders;

	@FXML
	private Rectangle avatar;

	@FXML
	private Text homePageBtn;

	@FXML
	private ImageView leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Text profileBtn;

	@FXML
	private ImageView resImage1;

	@FXML
	private ImageView resImage2;

	@FXML
	private ImageView resImage3;

	@FXML
	private ImageView resImage4;

	@FXML
	private ImageView resImage5;

	@FXML
	private ImageView resImage6;

	@FXML
	private Text resText1;

	@FXML
	private Text resText2;

	@FXML
	private Text resText3;

	@FXML
	private Text resText4;

	@FXML
	private Text resText5;

	@FXML
	private Text resText6;

	@FXML
	private Label resOrder1;

	@FXML
	private Label resOrder2;

	@FXML
	private Label resOrder3;

	@FXML
	private Label resOrder4;

	@FXML
	private Label resOrder5;

	@FXML
	private Label resOrder6;

	@FXML
	private Text restaurantsBtn;

	@FXML
	private ImageView rightArrowBtn;

	@FXML
	private TextField searchRestaurantFieldTxt;

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	@FXML
	void moveLeftClicked(MouseEvent event) {

	}

	@FXML
	void moveRightClicked(MouseEvent event) {

	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		router.changeSceneToHomePage();
	}

	/**
	 * Setting the stage instance.
	 * 
	 * @param Stage stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
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
			return new ImagePattern(new Image(getClass().getResource("../images/manager-avatar.png").toString()));
		case CEO:
			return new ImagePattern(new Image(getClass().getResource("../images/CEO-avatar.png").toString()));
		case EmployerHR:
			return new ImagePattern(new Image(getClass().getResource("../images/HR-avatar.png").toString()));
		case Customer:
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

	/**
	 * Getting restaurants from the db.
	 */
	@SuppressWarnings("unchecked")
	public void setRestaurants() {
		ServerResponse restaurants = ClientGUI.client.getRestaurants();
		if (restaurants == null) {
			ClientGUI.client.restaurantsRequest();
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					while (ClientGUI.client.getRestaurants() == null) {
						synchronized (ClientGUI.monitor) {
							try {
								ClientGUI.monitor.wait();
							} catch (Exception e) {
								e.printStackTrace();
								return;
							}
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
			restaurants = ClientGUI.client.getRestaurants();
		}
		createRestaurants((HashMap<String, File>) restaurants.getServerResponse());
	}

	/**
	 * Creating restaurants options selection. Setting onclick event on every Order
	 * Now label affiliate with the restaurant.
	 * 
	 * @param restaurants
	 */
	private void createRestaurants(HashMap<String, File> restaurants) {
		resImages = Arrays.asList(resImage1, resImage2, resImage3, resImage4, resImage5, resImage6);
		resNameTexts = Arrays.asList(resText1, resText2, resText3, resText4, resText5, resText6);
		resOrders = Arrays.asList(resOrder1, resOrder2, resOrder3, resOrder4, resOrder5, resOrder6);
		List<String> resNames = new ArrayList<>();
		resNames.addAll(restaurants.keySet());
		for (int i = 0; i < resNames.size(); i++) {
			String resName = resNames.get(i);
			resImages.get(i).setImage(
					new Image(getClass().getResource("../images/" + resName.toLowerCase() + "-logo.jpg").toString()));
			resNameTexts.get(i).setText(resName);
			Label resOrder = resOrders.get(i);
			/**
			 * Setting event listener on every order now button affiliate with each
			 * restaurant.
			 */
			resOrder.setOnMouseClicked((MouseEvent e) -> {
				router = Router.getInstance();
				if (router.getIdentifyController() == null) {
					AnchorPane mainContainer;
					identifyController controller;
					try {
						FXMLLoader loader = new FXMLLoader();
						loader.setLocation(getClass().getResource("../gui/bitemeIdentifyBeforeOrderPage.fxml"));
						mainContainer = loader.load();
						controller = loader.getController();
						controller.setAvatar();
						controller.setRestaurantToOrder(resName);
						Scene mainScene = new Scene(mainContainer);
						mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
						controller.setScene(mainScene);
						stage.setTitle("BiteMe - Identification Page");
						stage.setScene(mainScene);
						stage.show();
					} catch (IOException ex) {
						ex.printStackTrace();
						return;
					}
				} else {
					router.getIdentifyController().setRestaurantToOrder(resName);
					stage.setTitle("BiteMe - Identification page");
					stage.setScene(router.getIdentifyController().getScene());
					stage.show();
				}
			});
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setRestaurantselectionController(this);
		setStage(router.getStage());
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Scene getScene() {
		return scene;
	}
	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}

}
