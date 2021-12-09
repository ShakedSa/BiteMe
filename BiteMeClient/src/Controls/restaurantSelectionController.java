package Controls;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class restaurantSelectionController implements Initializable{

	private Router router;
	
	private Stage stage;
	
	private Scene scene;

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
	private Text restaurantsBtn;

	@FXML
	private ImageView rightArrowBtn;

	@FXML
	private TextField searchRestaurantFieldTxt;

	@FXML
	private AnchorPane root;

	@FXML
	void logoutClicked(MouseEvent event) {
		ServerResponse resUser = ClientGUI.client.getUser();
		User user = (User)resUser.getServerResponse();
		if (user != null) {
			ClientGUI.client.logout(user.getUserName());
			ClientGUI.client.getUser().setServerResponse(null);
		}
		router.getHomePageController().setProfile(false);
		changeSceneToHomePage(false);
	}

	@FXML
	void moveLeftClicked(MouseEvent event) {

	}

	@FXML
	void moveRightClicked(MouseEvent event) {

	}

	@FXML
	void openProfile(MouseEvent event) {

	}

	/**
	 * Changing the current scene to homepage.
	 */
	@FXML
	void returnToHomePage(MouseEvent event) {
		changeSceneToHomePage(true);
	}

	void changeSceneToHomePage(boolean val) {
		stage.setTitle("BiteMe - HomePage");
		stage.setScene(router.getHomePageController().getScene());
		stage.show();
//		AnchorPane mainContainer;
//		homePageController controller;
//		try {
//			FXMLLoader loader = new FXMLLoader();
//			loader.setLocation(getClass().getResource("../gui/bitemeHomePage.fxml"));
//			mainContainer = loader.load();
//			controller = loader.getController();
//			controller.setStage(stage);
//			controller.setAvatar();
//			controller.setProfile(val);
//			controller.setFavRestaurants();
//			Scene mainScene = new Scene(mainContainer);
//			mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
//			stage.setTitle("BiteMe - HomePage");
//			stage.setScene(mainScene);
//			stage.show();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return;
//		}
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
	 * Setting user avatar.
	 */
	public void setAvatar() {
		try {
			avatar.setArcWidth(65);
			avatar.setArcHeight(65);
			ImagePattern pattern = new ImagePattern(
					new Image(getClass().getResource("../images/guest-avatar.png").toString()));
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
		System.out.println(restaurants);
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
		createRestaurants((HashMap<String, File>)restaurants.getServerResponse());
	}

	/**
	 * Creating the card for each restaurant and add it to the mainScene. Displaying
	 * at a given moment up to 6 restaurants!
	 * 
	 * @param HashMap<String, File> restaurants
	 */
	private void createRestaurants(HashMap<String, File> restaurants) {
		Set<String> resSet = restaurants.keySet();
		int i = 0;
		for (String resName : resSet) {
			Rectangle base = new Rectangle();
			base.setHeight(188);
			base.setWidth(136);
			ImagePattern pattern = new ImagePattern(
					new Image(getClass().getResource("../images/" + resName.toLowerCase() + "-logo.jpg").toString()));
			base.setFill(pattern);
			base.setEffect(new DropShadow(1, Color.BLACK));
			base.setArcHeight(15);
			base.setArcWidth(15);
			Label orderNow = new Label("Order Now");
			orderNow.setId("order");
			orderNow.setFont(new Font("Berlin Sans FB", 14));
			orderNow.setOnMouseClicked(e -> {
				/**
				 * Send request to get restaurant's info from the server. also switch scenes to
				 * the restaurant's page.
				 */
			});
			root.getChildren().addAll(base, orderNow);
			switch (i) {
			case 0:
				base.setLayoutX(177);
				base.setLayoutY(157);
				orderNow.setLayoutX(177);
				orderNow.setLayoutY(317);
				break;
			case 1:
				base.setLayoutX(177 * 2 - 5);
				base.setLayoutY(157);
				orderNow.setLayoutX(199 * 2 - 49);
				orderNow.setLayoutY(317);
				break;
			case 2:
				base.setLayoutX(177 * 3 - 11);
				base.setLayoutY(157);
				orderNow.setLayoutX(199 * 3 - 77);
				orderNow.setLayoutY(317);
				break;
			case 3:
				base.setLayoutX(177);
				base.setLayoutY(366);
				orderNow.setLayoutX(177);
				orderNow.setLayoutY(526);
				break;
			case 4:
				base.setLayoutX(177 * 2 - 5);
				base.setLayoutY(366);
				orderNow.setLayoutX(199 * 2 - 49);
				orderNow.setLayoutY(526);
				break;
			case 5:
				base.setLayoutX(177 * 3 - 11);
				base.setLayoutY(366);
				orderNow.setLayoutX(199 * 3 - 77);
				orderNow.setLayoutY(526);
				break;
			}
			i++;
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router=Router.getInstance();
		router.setRestaurantselectionController(this);
		
	}
	
	public void setScene(Scene scene) {
		this.scene=scene;
	}

	public Scene getScene() {
		return scene;
	}

}
