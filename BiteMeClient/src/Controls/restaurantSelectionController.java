package Controls;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import Entities.MyFile;
import Entities.Order;
import Entities.ServerResponse;
import Entities.Supplier;
import Entities.User;
import Enums.RestaurantType;
import Enums.UserType;
import client.ClientGUI;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

/**
 * Controller for the view of the restaurant selection page.
 * 
 * @author Shaked
 */
public class restaurantSelectionController implements Initializable {

	private ServerResponse resRestaurants = null;
	public final UserType type = UserType.Customer;
	private Router router;

	private Stage stage;

	private Scene scene;

	private List<ImageView> resImages;

	private List<Text> resNameTexts;

	private List<Label> resOrders;

	private List<Text> loadingText;

	private List<Rectangle> borders;

	private ArrayList<Supplier> restaurants;
	private HashMap<String, Image> restaurantsImages = new HashMap<>();
	private IntegerProperty page = new SimpleIntegerProperty(0);

	@FXML
	private Rectangle avatar;

	@FXML
	private Text homePageBtn;

	@FXML
	private Rectangle leftArrowBtn;

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
	private Text loadingTxt1;

	@FXML
	private Text loadingTxt2;

	@FXML
	private Text loadingTxt3;

	@FXML
	private Text loadingTxt4;

	@FXML
	private Text loadingTxt5;

	@FXML
	private Text loadingTxt6;

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
	private Rectangle rightArrowBtn;

	@FXML
	private TextField searchRestaurantFieldTxt;

	@FXML
	private Text itemsCounter;

	@FXML
	private AnchorPane root;

	/**
	 * Search functionality, filtering the restaurants based on value in search
	 * field.
	 * 
	 * @param event
	 */
	@FXML
	void filterRestaurants(KeyEvent event) {
		String text = searchRestaurantFieldTxt.getText();
		List<Supplier> searchResults = restaurants.stream()
				.filter(r -> r.getRestaurantName().toLowerCase().contains(text.toLowerCase()))
				.collect(Collectors.toList());
		ArrayList<Supplier> newRestaurants = new ArrayList<>();
		for (Supplier suplier : searchResults) {
			newRestaurants.add(suplier);
		}
		createRestaurants(newRestaurants);
	}

	/**
	 * Method to change scene to My Cart.
	 * 
	 * @param event
	 */
	@FXML
	public void changeToCart(MouseEvent event) {
		router.changeToMyCart("Restaurants");
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
	 * Method to display the previous page of restaurants list.
	 * 
	 * @param event
	 */
	@FXML
	void moveLeftClicked(MouseEvent event) {
		if (restaurants.size() >= 6) {
			ArrayList<Supplier> prevPage = new ArrayList<>();
			if (page.get() == 0) {
				return;
			}
			int counter = 0;
			for (int i = (page.get() - 1) * 6; i < restaurants.size() && counter < 6; i++, counter++) {
				prevPage.add(restaurants.get(i));
			}
			page.set(page.get() - 1);
			createRestaurants(prevPage);
		}
	}

	/**
	 * Method to display the next page of restaurants list.
	 * 
	 * @param event
	 */
	@FXML
	void moveRightClicked(MouseEvent event) {
		if (restaurants.size() >= 6) {
			ArrayList<Supplier> nextPage = new ArrayList<>();
			int counter = 0;
			for (int i = (page.get() + 1) * 6; i < restaurants.size() && counter < 6; i++, counter++) {
				nextPage.add(restaurants.get(i));
			}
			page.set(page.get() + 1);
			if (nextPage.size() != 0)
				createRestaurants(nextPage);
		}
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
	 * Getting restaurants from the data base, based on the user's branch.
	 */
	@SuppressWarnings("unchecked")
	public void setRestaurants() {
		User user = (User) ClientGUI.getClient().getUser().getServerResponse();
		if (resRestaurants == null || ((ArrayList<Supplier>) resRestaurants.getServerResponse()).get(0)
				.getMainBranch() != user.getMainBranch()) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					synchronized (ClientGUI.getMonitor()) {
						ClientGUI.getClient().restaurantsRequest();
						try {
							ClientGUI.getMonitor().wait();
						} catch (Exception e) {
							e.printStackTrace();
							return;
						}
						resRestaurants = ClientGUI.getClient().getLastResponse();
						restaurants = ((ArrayList<Supplier>) resRestaurants.getServerResponse());
						if (restaurants != null) {
							Platform.runLater(() -> createRestaurants(restaurants));
						} else {
							hideRestaurants(6);
							Label errorMsg = new Label("Server Error\nCan't get restaurants from the server.");
							root.getChildren().add(errorMsg);
							errorMsg.setLayoutX(114);
							errorMsg.setLayoutY(138);
							errorMsg.getStyleClass().add("title");
							return;
						}
					}
				}
			});
			t.start();
		} else {
			createRestaurants((ArrayList<Supplier>) resRestaurants.getServerResponse());
		}
	}

	/**
	 * Hiding restaurants spots on the screen in range of 0 to 6 depend on the
	 * user's selection.
	 * 
	 * @param amountToHide
	 */
	private void hideRestaurants(int amountToHide) {
		for (int i = 0; i < amountToHide; i++) {
			resImages.get(resImages.size() - 1 - i).setVisible(false);
			resNameTexts.get(resNameTexts.size() - 1 - i).setVisible(false);
			resOrders.get(resOrders.size() - 1 - i).setVisible(false);
			loadingText.get(loadingText.size() - 1 - i).setVisible(false);
			if (borders.get(borders.size() - 1 - i) != null) {
				borders.get(borders.size() - 1 - i).setVisible(false);
			}
		}
	}

	/**
	 * Private method filtering the restaurants by the restaurant type.
	 * 
	 * @param type
	 */
	private void filterByType(RestaurantType type) {
		List<Supplier> filteredList = restaurants.stream().filter(r -> r.getRestaurantType().equals(type))
				.collect(Collectors.toList());
		createRestaurants((ArrayList<Supplier>) filteredList);
	}

	/**
	 * Setting buttons for restaurants filtering.
	 */
	public void setButtons() {
		Button showAll = new Button("Show All");
		showAll.setOnAction(e -> createRestaurants(restaurants));
		showAll.setLayoutX(44);
		showAll.setLayoutY(237);
		Button type1 = new Button(RestaurantType.Asian.toString());
		type1.setOnAction(e -> filterByType(RestaurantType.Asian));
		type1.setLayoutX(44);
		type1.setLayoutY(279);
		Button type2 = new Button(RestaurantType.Fastfood.toString());
		type2.setOnAction(e -> filterByType(RestaurantType.Fastfood));
		type2.setLayoutX(44);
		type2.setLayoutY(321);
		Button type3 = new Button(RestaurantType.Italian.toString());
		type3.setOnAction(e -> filterByType(RestaurantType.Italian));
		type3.setLayoutX(44);
		type3.setLayoutY(363);
		Button type4 = new Button(RestaurantType.Other.toString());
		type4.setOnAction(e -> filterByType(RestaurantType.Other));
		type4.setLayoutX(44);
		type4.setLayoutY(405);
		root.getChildren().addAll(showAll, type1, type2, type3, type4);
	}

	/**
	 * Creating restaurants options selection. Setting onclick event on every Order
	 * Now label affiliate with the restaurant.
	 * 
	 * @param restaurants
	 */
	private void createRestaurants(ArrayList<Supplier> restaurants) {
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
		if (loadingText == null) {
			loadingText = Arrays.asList(loadingTxt1, loadingTxt2, loadingTxt3, loadingTxt4, loadingTxt5, loadingTxt6);
		}
		for (int i = 0; i < 6; i++) {
			resImages.get(i).setVisible(true);
			resNameTexts.get(i).setVisible(true);
			resOrders.get(i).setVisible(true);
			loadingText.get(i).setVisible(true);
			loadingText.get(i).toFront();
			if (borders.get(i) != null) {
				borders.get(i).setVisible(true);
			}
		}

		int amount = restaurants.size();
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
		/** At all time display up to 6 restaurants */
		for (int i = 0; i < restaurants.size() && i < 6; i++) {
			Supplier supplier = restaurants.get(i);
			if (restaurantsImages.get(supplier.getRestaurantName()) == null) {
				MyFile file = supplier.getRestaurantLogo();
				byte[] imageArr = file.getMybytearray();
				BufferedImage img;
				try {
					img = ImageIO.read(new ByteArrayInputStream(imageArr));
					Image image = SwingFXUtils.toFXImage(img, null);
					resImages.get(i).setImage(image);
					restaurantsImages.put(supplier.getRestaurantName(), image);
				} catch (IOException e) {
					e.printStackTrace();
					return;
				}
			} else {
				resImages.get(i).setImage(restaurantsImages.get(supplier.getRestaurantName()));
			}
			loadingText.get(i).setVisible(false);
			String resName = supplier.getRestaurantName();
			resNameTexts.get(i).setText(resName);
			Label resOrder = resOrders.get(i);
			/**
			 * Setting event listener on every order now button affiliate with each
			 * restaurant.
			 */
			resOrder.setOnMouseClicked((MouseEvent e) -> {
				if (router.getOrder().getRestaurantName() != null
						&& !router.getOrder().getRestaurantName().equals(resName)) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Switch restaurants");
					alert.setHeaderText("You got an order in restaurant " + router.getOrder().getRestaurantName()
							+ "\nChoosing a different restaurant will reset your last order.");
					alert.showAndWait().filter(ButtonType.OK::equals).ifPresent(b -> {
						Order newOrder = new Order();
						System.out.println(resName);
						newOrder.setRestaurantName(resName);
						router.setOrder(new Order());
						changeToIdentify(resName);
					});
				} else {
					changeToIdentify(resName);
				}

			});
		}
	}

	/**
	 * Private method to change scenes to Identify page. the first step in the order
	 * process.
	 * 
	 * @param resName
	 */
	private void changeToIdentify(String resName) {
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
				controller.setItemsCounter();
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
			router.getIdentifyController().setItemsCounter();
			stage.setTitle("BiteMe - Identification page");
			stage.setScene(router.getIdentifyController().getScene());
			stage.show();
		}
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
		router.setRestaurantselectionController(this);
		setStage(router.getStage());
		router.setArrow(leftArrowBtn, -90);
		router.setArrow(rightArrowBtn, 90);
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
	 * @return the resRestaurants
	 */
	public ServerResponse getResRestaurants() {
		return resRestaurants;
	}

	/**
	 * @param resRestaurants the resRestaurants to set
	 */
	public void setResRestaurants(ServerResponse resRestaurants) {
		this.resRestaurants = resRestaurants;
	}

}
