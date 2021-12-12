package Controls;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

import Entities.ServerResponse;
import Enums.UserType;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class restaurantSelectionController implements Initializable {

	public final UserType type = UserType.Customer;
	private Router router;

	private Stage stage;

	private Scene scene;

	private List<ImageView> resImages;

	private List<Text> resNameTexts;

	private List<Label> resOrders;

	private List<Rectangle> borders;

	private Set<String> restaurantsNames;

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
	private Rectangle border1;

	@FXML
	private Rectangle border2;

	@FXML
	private Rectangle border3;

	@FXML
	private Rectangle border4;

	@FXML
	private Rectangle border5;

	@FXML
	private Rectangle border6;

	@FXML
	private Text restaurantsBtn;

	@FXML
	private ImageView rightArrowBtn;

	@FXML
	private TextField searchRestaurantFieldTxt;

	@FXML
	private Text itemsCounter;

	/**
	 * Search functionality, filtering the restaurants based on value in search
	 * field.
	 * 
	 * @param event
	 */
	@FXML
	void filterRestaurants(KeyEvent event) {
		String text = searchRestaurantFieldTxt.getText();
		List<String> searchResults = restaurantsNames.stream().filter(r -> r.toLowerCase().contains(text.toLowerCase()))
				.collect(Collectors.toList());
		HashMap<String, File> newRestaurants = new HashMap<>();
		for (String name : searchResults) {
			newRestaurants.put(name, null);
		}
		createRestaurants(newRestaurants);
	}

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

	/**
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		router.setAvatar(avatar);
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
		restaurantsNames = ((HashMap<String, File>) restaurants.getServerResponse()).keySet();
		createRestaurants((HashMap<String, File>) restaurants.getServerResponse());
	}

	private void hideRestaurants(int amountToHide) {
		for (int i = 0; i < amountToHide; i++) {
			resImages.get(resImages.size() - 1 - i).setVisible(false);
			resNameTexts.get(resNameTexts.size() - 1 - i).setVisible(false);
			resOrders.get(resOrders.size() - 1 - i).setVisible(false);
			if (borders.get(borders.size() - 1 - i) != null) {
				borders.get(borders.size() - 1 - i).setVisible(false);
			}
		}
	}

	/**
	 * Creating restaurants options selection. Setting onclick event on every Order
	 * Now label affiliate with the restaurant.
	 * 
	 * @param restaurants
	 */
	private void createRestaurants(HashMap<String, File> restaurants) {
		if (resImages == null) {
			resImages = Arrays.asList(resImage1, resImage2, resImage3, resImage4, resImage5, resImage6);
		}
		if (resNameTexts == null) {
			resNameTexts = Arrays.asList(resText1, resText2, resText3, resText4, resText5, resText6);
		}
		if (resOrders == null) {
			resOrders = Arrays.asList(resOrder1, resOrder2, resOrder3, resOrder4, resOrder5, resOrder6);
		}
		if (borders == null) {
			borders = Arrays.asList(border1, border2, border3, border4, border5, border6);
		}
		for (int i = 0; i < 6; i++) {
			resImages.get(i).setVisible(true);
			resNameTexts.get(i).setVisible(true);
			resOrders.get(i).setVisible(true);
			if (borders.get(i) != null) {
				borders.get(i).setVisible(true);
			}
		}
		List<String> resNames = new ArrayList<>();
		resNames.addAll(restaurants.keySet());
		int amount = resNames.size();
		switch (amount) {
		case 0:
			hideRestaurants(6);
			break;
		case 1:
			hideRestaurants(5);
			break;
		case 2:
			hideRestaurants(4);
			break;
		case 3:
			hideRestaurants(3);
			break;
		case 4:
			hideRestaurants(2);
			break;
		case 5:
			hideRestaurants(1);
			break;
		}
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

	public void setItemsCounter() {
		itemsCounter.setText(router.getBagItems().size() + "");
	}

}
