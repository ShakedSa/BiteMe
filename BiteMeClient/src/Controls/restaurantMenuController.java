package Controls;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import Entities.Component;
import Entities.Product;
import Entities.ServerResponse;
import Entities.User;
import Enums.UserType;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class restaurantMenuController implements Initializable {

	public final UserType type= UserType.Customer;
	private Router router;

	private Stage stage;

	private Scene scene;

	private String restaurantName;

	TabPane tabPane;

	private ArrayList<Product> productsInOrder = new ArrayList<>();

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
	private Text itemsCounter;

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	@FXML
	void nextOrderStep(MouseEvent event) {

	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		root.getChildren().remove(tabPane);
		router.changeSceneToHomePage();
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
		if (ClientGUI.client.getMenu() != null) {
			ArrayList<Product> menu = (ArrayList<Product>) ClientGUI.client.getMenu().getServerResponse();
			if (menu == null) {
				System.out.println("Menu is not set yet for " + restaurantName);
			} else {
				createMenu(menu);
			}
		} else {
			System.out.println("Menu is not set yet for " + restaurantName);
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
		setTabContent(entrees, entriesMenu);
		setTabContent(mainDishes, mainsMenu);
		setTabContent(desserts, dessertsMenu);
		setTabContent(drinks, drinksMenu);
	}

	/**
	 * Creating tab content for each category: entrees, main dishes, desserts,
	 * drinks.
	 * 
	 * @param tab
	 * @param productsToAdd
	 */
	@SuppressWarnings("unchecked")
	private void setTabContent(Tab tab, List<Product> productsToAdd) {
		ScrollPane tabContent = new ScrollPane();
		AnchorPane tabLabels = new AnchorPane();
		int i = 0;
		/** Creating scrollpane for the tabpane. */
		for (Product p : productsToAdd) {
			Pane pane = new Pane();
			Label nameLabel = new Label(p.getDishName());
			Label priceLabel = new Label(p.getPrice() + "¤");
			nameLabel.setStyle("-fx-padding: 10 0");
			priceLabel.setStyle("-fx-padding: 10 0");
			nameLabel.setLayoutX(15);
			priceLabel.setLayoutX(260);
			pane.setId("menuBtn");
			pane.getChildren().add(nameLabel);
			pane.getChildren().add(priceLabel);
			tabLabels.getChildren().add(pane);
			if (i % 2 != 0) {
				pane.setLayoutX(375);
				pane.setLayoutY((i - 1) * 25 + 15);
			} else {
				pane.setLayoutY(i * 25 + 15);
				pane.setLayoutX(65);
			}
			/**
			 * Setting onclicked event, when user clicks on a certain product, it will
			 * display a "choose components" screen for the user to specifiy his requests.
			 * Also the user will be able to decide how many items of the same product
			 * should be in the order.
			 */
			pane.setOnMouseClicked(e -> {
				Pane overlayPane = new Pane();
				root.getChildren().add(overlayPane);
				overlayPane.setPrefHeight(375);
				overlayPane.setPrefWidth(689);
				overlayPane.setStyle(
						"-fx-background-color:  #006875; -fx-effect:  dropshadow(three-pass-box, rgba(0,0,0,0.5),10,0,0,0)");
				overlayPane.setLayoutX(71);
				overlayPane.setLayoutY(105);
				Label closeBtn = new Label("X");
				closeBtn.setStyle("-fx-text-fill: red; -fx-cursor: hand;");
				closeBtn.setFont(new Font("Berlin Sans FB", 22));
				/** Setting a close button for the "choose components" screen. */
				closeBtn.setOnMouseClicked(clickedEvent -> {
					root.getChildren().remove(overlayPane);
					ClientGUI.client.setOptionalComponentsInProduct(null);
				});
				closeBtn.setLayoutX(650);
				closeBtn.setLayoutY(15);
				overlayPane.getChildren().add(closeBtn);
				Label title = new Label(nameLabel.getText());
				title.setFont(new Font("Berlin Sans FB", 30));
				title.setStyle("-fx-text-fill: white;");
				title.setLayoutX(41);
				title.setLayoutY(15);
				overlayPane.getChildren().add(title);
				Line l = new Line();
				l.setStartX(-100);
				l.setEndX(530);
				l.setStartY(0);
				l.setEndY(0);
				l.setScaleX(1);
				l.setScaleY(1);
				l.setScaleY(1);
				l.setLayoutX(134);
				l.setLayoutY(51);
				l.setStroke(Color.WHITE);
				overlayPane.getChildren().add(l);
				Thread t = new Thread(() -> {
					ClientGUI.client.componentsInProduct(restaurantName, p.getDishName());
					synchronized (ClientGUI.monitor) {
						try {
							ClientGUI.monitor.wait();
						} catch (Exception ex) {
							ex.printStackTrace();
							return;
						}
					}
				});
				t.start();
				try {
					t.join();
				} catch (Exception ex) {
					ex.printStackTrace();
					return;
				}
				ServerResponse serverResponse = ClientGUI.client.getOptionalComponentsInProduct();
				ArrayList<Component> componentInDish; // Components received from query.
				ArrayList<Component> componentInProduct = new ArrayList<>(); // Components in actual dish for the order.
				if (serverResponse != null) {
					componentInDish = (ArrayList<Component>) serverResponse.getServerResponse();
					if (componentInDish == null) {
						root.getChildren().remove(overlayPane);
						System.out.println("Product " + nameLabel.getText() + " doesn't have optional components");
					} else {
						ScrollPane componentContent = new ScrollPane();
						AnchorPane componentLabels = new AnchorPane();
						int j = 0;
						for (Component c : componentInDish) {
							Label com = new Label(c.toString());
							com.setLayoutX(10);
							com.setLayoutY(j * 50 + 10);
							com.setFont(new Font("Berlin Sans FB", 14));
							componentLabels.getChildren().add(com);
							com.setId("optionalBtn");
							com.setOnMouseClicked(evnt -> {
								if (com.getId().equals("clickedBG")) {
									com.setId("optionalBtn");
									componentInProduct.remove(c);
								} else {
									com.setId("clickedBG");
									componentInProduct.add(c);
								}
							});
							j++;
						}
						componentContent.setId("background");
						componentLabels.setId("background");
						componentContent.setLayoutX(30);
						componentContent.setLayoutY(100);
						componentContent.setContent(componentLabels);
						overlayPane.getChildren().add(componentContent);
					}

					Label plus = new Label("+");
					plus.setId("plusMinus");
					plus.setLayoutX(298);
					plus.setLayoutY(348);
					Label minus = new Label("-");
					minus.setId("plusMinus");
					minus.setLayoutX(372);
					minus.setLayoutY(348);
					Label counter = new Label("1");
					counter.setLayoutX(345);
					counter.setLayoutY(338);
					counter.setTextFill(Color.WHITE);
					plus.setFont(new Font("Berlin Sans FB", 24));
					minus.setFont(new Font("Berlin Sans FB", 24));
					counter.setFont(new Font("Berlin Sans FB", 22));
					plus.setOnMouseClicked(mEvent -> {
						counter.setText(Integer.parseInt(counter.getText()) + 1 + "");
					});
					minus.setOnMouseClicked(mEvent -> {
						int count = Integer.parseInt(counter.getText());
						if (count == 1) {
							return;
						}
						counter.setText(count - 1 + "");
					});

					Label addItem = new Label("Add Item");
					addItem.setId("addItem");
					addItem.setLayoutX(545);
					addItem.setLayoutY(330);
					/** Adding the desired product to the products array of the order */
					addItem.setOnMouseClicked(mEvent -> {
						p.setComponents(componentInProduct);
						int count = Integer.parseInt(counter.getText());
						itemsCounter.setText(Integer.parseInt(itemsCounter.getText()) + count + "");
						for (int k = 0; k < count; k++) {
							productsInOrder.add(p);
						}
						root.getChildren().remove(overlayPane);
					});
					overlayPane.getChildren().addAll(plus, minus, counter, addItem);
				}
			});
			i++;
		}
		tabContent.setContent(tabLabels);
		tab.setContent(tabContent);
	}

	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}
}
