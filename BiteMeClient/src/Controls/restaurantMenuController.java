package Controls;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import Entities.Product;
import Entities.ServerResponse;
import Entities.User;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
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

public class restaurantMenuController implements Initializable {

	private Router router;

	private Stage stage;

	private Scene scene;

	private String restaurantName;

	TabPane tabPane;

	@FXML
	private Rectangle avatar;

	@FXML
	private Tab dessertBtn;

	@FXML
	private Tab drinkBtn;

	@FXML
	private Tab entreesBtn;

	@FXML
	private Text homePageBtn;

	@FXML
	private ImageView leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Tab mainDishBtn;

	@FXML
	private Text profileBtn;

	@FXML
	private Text restaurantsBtn;

	@FXML
	private Label nextBtn;

	@FXML
	private AnchorPane root;

	@FXML
	void logoutClicked(MouseEvent event) {
		ServerResponse resUser = ClientGUI.client.getUser();
		if (resUser != null) {
			User user = (User) resUser.getServerResponse();
			if (user != null) {
				ClientGUI.client.logout(user.getUserName());
				ClientGUI.client.setUser(null);
			}
		}
		router.getHomePageController().setProfile(false);
		changeSceneToHomePage(false);
	}

	void changeSceneToHomePage(boolean val) {
		root.getChildren().remove(tabPane);
		stage.setTitle("BiteMe - HomePage");
		stage.setScene(router.getHomePageController().getScene());
		stage.show();
	}

	@FXML
	void nextOrderStep(MouseEvent event) {

	}

	@FXML
	void openProfile(MouseEvent event) {

	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		changeSceneToHomePage(true);
	}

	@FXML
	void returnToIdentify(MouseEvent event) {
		root.getChildren().remove(tabPane);
		stage.setTitle("BiteMe - Identification Page");
		stage.setScene(router.getIdentifyController().getScene());
		stage.show();
	}

	@FXML
	void returnToRestaurants(MouseEvent event) {
		root.getChildren().remove(tabPane);
		stage.setTitle("BiteMe - Restaurants");
		stage.setScene(router.getRestaurantselectionController().getScene());
		stage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setRestaurantMenuController(this);
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

	public void setRestaurantName(String resName) {
		this.restaurantName = resName;
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

	/**
	 * Setting menu for each restaurant.
	 */
	@SuppressWarnings("unchecked")
	public void setMenu() {
		ClientGUI.client.getRestaurantMenu(restaurantName);
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (ClientGUI.monitor) {
					try {
						ClientGUI.monitor.wait();
					} catch (Exception e) {
						e.printStackTrace();
						return;
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
		ArrayList<Product> menu = (ArrayList<Product>) ClientGUI.client.getMenu().getServerResponse();
		if (menu == null) {
			System.out.println("Menu is not set yet for " + restaurantName);
		} else {
			createMenu(menu);
		}
	}

	/**
	 * Creating the menu tab (currently not enabling to choose)
	 * 
	 * @param menu
	 */
	private void createMenu(ArrayList<Product> menu) {
		List<Product> entriesMenu = menu.stream().filter(p -> p.getType().equals("entry")).collect(Collectors.toList());
		List<Product> mainsMenu = menu.stream().filter(p -> p.getType().equals("main dish"))
				.collect(Collectors.toList());
		List<Product> drinksMenu = menu.stream().filter(p -> p.getType().equals("drink")).collect(Collectors.toList());
		List<Product> dessertsMenu = menu.stream().filter(p -> p.getType().equals("dessert"))
				.collect(Collectors.toList());
		Tab entrees = new Tab("Entrees");
		entrees.setClosable(false);
		Tab mainDishes = new Tab("Main Dishes");
		mainDishes.setClosable(false);
		Tab desserts = new Tab("Desserts");
		desserts.setClosable(false);
		Tab drinks = new Tab("Drinks");
		drinks.setClosable(false);
		tabPane = new TabPane();
		tabPane.getTabs().add(entrees);
		tabPane.getTabs().add(mainDishes);
		tabPane.getTabs().add(desserts);
		tabPane.getTabs().add(drinks);
		root.getChildren().add(tabPane);
		tabPane.setLayoutX(35);
		tabPane.setLayoutY(149);
		tabPane.setPrefWidth(756);
		tabPane.setPrefHeight(312);
		Label entry = new Label();
		for (Product p : entriesMenu) {
			if (entry.getText().equals("")) {
				entry.setText(p.getDishName());
			} else {
				entry.setText(p.getDishName() + "\n" + entry.getText());
			}
		}
		entrees.setContent(entry);
		Label main = new Label();
		for (Product p : mainsMenu) {
			if (main.getText().equals("")) {
				main.setText(p.getDishName());
			} else {
				main.setText(p.getDishName() + "\n" + main.getText());
			}
		}
		mainDishes.setContent(main);
		Label drink = new Label();
		for (Product p : drinksMenu) {
			if (drink.getText().equals("")) {
				drink.setText(p.getDishName());
			} else {
				drink.setText(p.getDishName() + "\n" + drink.getText());
			}
		}
		drinks.setContent(drink);
		Label dessert = new Label();
		for (Product p : dessertsMenu) {
			if (dessert.getText().equals("")) {
				dessert.setText(p.getDishName());
			} else {
				dessert.setText(p.getDishName() + "\n" + dessert.getText());
			}
		}
		desserts.setContent(dessert);

	}
	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}
}
