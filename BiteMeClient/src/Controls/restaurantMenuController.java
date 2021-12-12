package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import Entities.Component;
import Entities.Order;
import Entities.Product;
import Entities.ServerResponse;
import Enums.UserType;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class restaurantMenuController implements Initializable {

	public final UserType type = UserType.Customer;
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
		if(itemsCounter.getText().equals("0")) {
			return;
		}
		router.getOrder().setProducts(productsInOrder);
		router = Router.getInstance();
		if (router.getPickDateAndTimeController() == null) {
			AnchorPane mainContainer;
			pickDateAndTimeController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemePickDateAndTimePage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.setRestaurant(restaurantName);
				controller.setItemsCounter();
				controller.createCombos();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Pick Date And Time");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException ex) {
				ex.printStackTrace();
				return;
			}
		} else {
			router.getPickDateAndTimeController().setRestaurant(restaurantName);
			router.getPickDateAndTimeController().setAvatar();
			router.getPickDateAndTimeController().setItemsCounter();
			router.getPickDateAndTimeController().createCombos();
			stage.setTitle("BiteMe - Pick Date And Time");
			stage.setScene(router.getPickDateAndTimeController().getScene());
			stage.show();
		}
	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		root.getChildren().remove(tabPane);
		router.changeSceneToHomePage();
	}

	@FXML
	void returnToIdentify(MouseEvent event) {
		root.getChildren().remove(tabPane);
		router.getIdentifyController().setItemsCounter();
		stage.setTitle("BiteMe - Identification Page");
		stage.setScene(router.getIdentifyController().getScene());
		stage.show();
	}

	@FXML
	void returnToRestaurants(MouseEvent event) {
		root.getChildren().remove(tabPane);
		router.getRestaurantselectionController().setItemsCounter();
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

	/**
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		router.setAvatar(avatar);
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
		int i = 0; // Index for the items layout on the scrollpane.
		/** Creating scrollpane for the tab. */
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
				nextBtn.setDisable(true);
				/** Pane for the overlay screen. */
				Pane overlayPane = new Pane();
				root.getChildren().add(overlayPane);
				overlayPane.setPrefHeight(375);
				overlayPane.setPrefWidth(689);
				overlayPane.setStyle(
						"-fx-background-color:  #006875; -fx-effect:  dropshadow(three-pass-box, rgba(0,0,0,0.5),10,0,0,0)");
				overlayPane.setLayoutX(71);
				overlayPane.setLayoutY(105);
				/** Close button, closing the overlay screen. */
				Label closeBtn = new Label("X");
				closeBtn.setStyle("-fx-text-fill: red; -fx-cursor: hand;");
				closeBtn.setFont(new Font("Berlin Sans FB", 22));
				/** Setting a close button for the "choose components" screen. */
				closeBtn.setOnMouseClicked(clickedEvent -> {
					nextBtn.setDisable(false);
					root.getChildren().remove(overlayPane);
					ClientGUI.client.setOptionalComponentsInProduct(null);
				});
				closeBtn.setLayoutX(650);
				closeBtn.setLayoutY(15);
				/** Title for the overlay screen, showing the product name. */
				Label title = new Label(nameLabel.getText());
				title.setFont(new Font("Berlin Sans FB", 30));
				title.setStyle("-fx-text-fill: white;");
				title.setLayoutX(41);
				title.setLayoutY(15);
				/** Under line for the overlay screen's title. */
				Line line = new Line();
				/** Setting the line to cross from 1 end of the screen to another(roughly). */
				line.setStartX(-100);
				line.setEndX(530);
				line.setStartY(0);
				line.setEndY(0);
				line.setScaleX(1);
				line.setScaleY(1);
				line.setScaleY(1);
				line.setLayoutX(134);
				line.setLayoutY(51);
				line.setStroke(Color.WHITE);
				/** Product's description. */
				Label description = new Label(p.getDescription());
				description.setFont(new Font("Berlin Sans FB", 14));
				description.setLayoutX(41);
				description.setLayoutY(61);
				description.setTextFill(Color.WHITE);
				/** Getting the optional component from the server for this specific product. */
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
				/** Checking the server's response. */
				ServerResponse serverResponse = ClientGUI.client.getOptionalComponentsInProduct();
				ArrayList<Component> componentInDish; // Components received from query.
				ArrayList<Component> componentInProduct = new ArrayList<>(); // Components in actual dish for the order.
				if (serverResponse != null) {
					componentInDish = (ArrayList<Component>) serverResponse.getServerResponse();
					if (componentInDish == null || componentInDish.size() == 0) {
						/** If this dish doens't have any optional components */
						Label noComp = new Label(
								"Product " + nameLabel.getText() + " doesn't have optional components");
						noComp.setFont(new Font("Berlin Sans FB", 16));
						noComp.setTextFill(Color.WHITE);
						noComp.setLayoutX(30);
						noComp.setLayoutY(215);
						overlayPane.getChildren().add(noComp);
					} else {
						/** Creating scroll pane for all the optional components. */
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
							/**
							 * Setting onclick event for each component, if the user wants this components
							 * and clicks it --> add to components in product array. in the end add the
							 * desired component to the prodcut in the products array.
							 */
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
					/** Counter to show how many product of this product are in the order. */
					Label counter = new Label("1");
					/** Setting position for counter. */
					counter.setLayoutX(346);
					counter.setLayoutY(338);
					counter.setTextFill(Color.WHITE);
					counter.setFont(new Font("Berlin Sans FB", 22));

					/** Button to add more of this product to the order, infinite cap. */
					Label plus = new Label("+");
					/** Setting position for plus button. */
					plus.setId("plusMinus");
					plus.setLayoutX(298);
					plus.setLayoutY(348);
					plus.setFont(new Font("Berlin Sans FB", 24));
					plus.setOnMouseClicked(mEvent -> {
						counter.setText(Integer.parseInt(counter.getText()) + 1 + "");
					});

					/** Button to remove 1 or more of this product from the order, finite at 1. */
					Label minus = new Label("-");
					/** Setting position for minus button. */
					minus.setId("plusMinus");
					minus.setLayoutX(372);
					minus.setLayoutY(348);
					minus.setFont(new Font("Berlin Sans FB", 24));
					minus.setOnMouseClicked(mEvent -> {
						int count = Integer.parseInt(counter.getText());
						if (count == 1) {
							return;
						}
						counter.setText(count - 1 + "");
					});

					/** Free text for the user to specify any notes for the order. */
					TextArea restrictions = new TextArea();
					restrictions.setPromptText("Anything we need to know?");
					restrictions.setId("txtarea");
					restrictions.setLayoutX(384);
					restrictions.setLayoutY(108);
					/** Title for the free text. */
					Label textAreaTitle = new Label("Anything we need to know?");
					textAreaTitle.setFont(new Font("Berlin Sans FB", 16));
					textAreaTitle.setLayoutX(384);
					textAreaTitle.setLayoutY(85);
					textAreaTitle.setTextFill(Color.WHITE);
					/**
					 * Add item button, adding the selected item with all the selected components
					 * and the free text from the user. Adding the item <counter> times to the
					 * order.
					 */
					Label addItem = new Label("Add Item");
					addItem.setId("addItem");
					addItem.setLayoutX(545);
					addItem.setLayoutY(330);
					/** Adding the desired product to the products array of the order */
					addItem.setOnMouseClicked(mEvent -> {
						String notes = restrictions.getText();
						/**
						 * If the restrictions free text is not null or empty, add new component to the
						 * order.
						 */
						if (notes != null && !notes.equals("")) {
							Component c = new Component(null, null, restrictions.getText());
							componentInProduct.add(c);
						}
						/** Adding the components list to this product. */
						p.setComponents(componentInProduct);
						/**
						 * Increasing the counter of the bag items (the one near the profile picture).
						 */
						int count = Integer.parseInt(counter.getText());
						/**
						 * Adding the product to products array count times(amount specify by the user).
						 */
						for (int k = 0; k < count; k++) {
							productsInOrder.add(p);
						}
						/**
						 * Setting global state for the router, adding <productsInOrder> to router
						 * singleton.
						 */
						router.setBagItems(productsInOrder);
						setItemsCounter();
						root.getChildren().remove(overlayPane); // after <addItem> clicked, remove the overlay.
						nextBtn.setDisable(false);
					});
					/**
					 * Add all the labels to the overlay.
					 */
					overlayPane.getChildren().addAll(title, closeBtn, line, description, plus, minus, counter, addItem,
							restrictions, textAreaTitle);
				}
			});
			i++;
		}
		/** Adding the anchorpane to the scrollpane. */
		tabContent.setContent(tabLabels);
		/** Adding the scrollpane to the tab. */
		tab.setContent(tabContent);
	}

	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}
	
	public void setItemsCounter() {
		itemsCounter.setText(router.getBagItems().size() + "");
	}
}
