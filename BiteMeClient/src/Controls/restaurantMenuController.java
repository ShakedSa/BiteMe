package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import Entities.Component;
import Entities.Product;
import Enums.Doneness;
import Enums.Size;
import Enums.TypeOfProduct;
import Enums.UserType;
import Util.LoadingAnimation;
import client.ClientGUI;
import javafx.application.Platform;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controller for the view of the restaurant menu page.
 * 
 * @author Shaked
 */
public class restaurantMenuController implements Initializable {

	public final UserType type = UserType.Customer;
	private Router router;

	private Stage stage;

	private Scene scene;

	private String restaurantName;

	TabPane tabPane;

	private ArrayList<Product> productsInOrder;

	Label menuTitle;

	Pane overlayPane;

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
	private Rectangle leftArrowBtn;

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

	/**
	 * Method to log out the user.
	 * 
	 * @param event
	 */
	@FXML
	void logoutClicked(MouseEvent event) {
		clearScreen();
		router.logOut();
	}

	/**
	 * Method that moves the user to the next step of the order process.
	 * 
	 * @param event
	 */
	@FXML
	void nextOrderStep(MouseEvent event) {
		if (itemsCounter.getText().equals("0")) {
			return;
		}
		if (router.getOrder().getProducts() == null) {
			router.getOrder().setProducts(productsInOrder);
		} else {
			router.getOrder().getProducts().addAll(productsInOrder.stream()
					.filter(p -> !router.getOrder().getProducts().contains(p)).collect(Collectors.toList()));
		}
		router.getOrder().calculateOrderPrice();
		router = Router.getInstance();
		if (router.getPickDateAndTimeController() == null) {
			AnchorPane mainContainer;
			pickDateAndTimeController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/bitemePickDateAndTimePage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.setRestaurant(restaurantName);
				controller.setItemsCounter();
				controller.createCombos();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
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

	/**
	 * Private method to clear the scene when moving between pages in the
	 * application.
	 */
	private void clearScreen() {
		root.getChildren().removeAll(tabPane, overlayPane);
		nextBtn.setDisable(false);
		leftArrowBtn.setDisable(false);
	}

	/**
	 * Method to return to Home Page.
	 * 
	 * @param event
	 */
	@FXML
	void returnToHomePage(MouseEvent event) {
		clearScreen();
		router.changeSceneToHomePage();
	}

	/**
	 * Method that moves the user to the previous step of the order
	 * process(Identification).
	 * 
	 * @param event
	 */
	@FXML
	void returnToIdentify(MouseEvent event) {
		clearScreen();
		router.getIdentifyController().setItemsCounter();
		stage.setTitle("BiteMe - Identification Page");
		stage.setScene(router.getIdentifyController().getScene());
		stage.show();
	}

	/**
	 * Method that returns the user to the restaurant selection page.
	 * 
	 * @param event
	 */
	@FXML
	void returnToRestaurants(MouseEvent event) {
		clearScreen();
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
		router.setRestaurantMenuController(this);
		setStage(router.getStage());
		router.setArrow(leftArrowBtn, -90);
		menuTitle = new Label();
		menuTitle.getStyleClass().addAll("title", "menuTitle");
		root.getChildren().add(menuTitle);
	}

	/**
	 * Method to change scene to My Cart.
	 * 
	 * @param event
	 */
	@FXML
	public void changeToCart(MouseEvent event) {
		root.getChildren().remove(tabPane);
		router.changeToMyCart("Menu");
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
	 * Setting the stage instance.
	 * 
	 * @param Stage stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Setting the restaurant that was choosen by the user.
	 * 
	 * @param resName
	 * */
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
	 * Creating the menu for the restaurant.<br>
	 * Getting the items from the data base.
	 */
	@SuppressWarnings("unchecked")
	public void setMenu() {
		menuTitle.setText(restaurantName + " Menu");
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (ClientGUI.getMonitor()) {
					ClientGUI.getClient().getRestaurantMenu(restaurantName);
					try {
						ClientGUI.getMonitor().wait();
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
		if (ClientGUI.getClient().getLastResponse() != null) {
			ArrayList<Product> menu = (ArrayList<Product>) ClientGUI.getClient().getLastResponse().getServerResponse();
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
	 * Creating the menu
	 * 
	 * @param menu
	 */
	private void createMenu(ArrayList<Product> menu) {
		/**
		 * Filtering the menu of the restaurant by categories. Entries, Main Dishes,
		 * Drinks, Desserts.
		 */
		List<Product> entriesMenu = menu.stream().filter(p -> p.getType() == TypeOfProduct.entry)
				.collect(Collectors.toList());
		List<Product> mainsMenu = menu.stream().filter(p -> p.getType() == TypeOfProduct.mainDish)
				.collect(Collectors.toList());
		List<Product> drinksMenu = menu.stream().filter(p -> p.getType() == TypeOfProduct.drink)
				.collect(Collectors.toList());
		List<Product> dessertsMenu = menu.stream().filter(p -> p.getType() == TypeOfProduct.dessert)
				.collect(Collectors.toList());
		/** Creating new tabs for the tabpane. */
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
		tabPane.setLayoutY(179);
		tabPane.setPrefWidth(756);
		tabPane.setPrefHeight(282);
		setTabContent(entrees, entriesMenu);
		setTabContent(mainDishes, mainsMenu);
		setTabContent(desserts, dessertsMenu);
		setTabContent(drinks, drinksMenu);
	}

	/**
	 * Creating tab content for each category: entries, main dishes, desserts,
	 * drinks. Setting on click event for each dish, displaying the optional
	 * component.
	 * 
	 * @param tab
	 * @param productsToAdd
	 */
	@SuppressWarnings("unchecked")
	private void setTabContent(Tab tab, List<Product> productsToAdd) {
		ScrollPane tabContent = new ScrollPane();
		AnchorPane tabLabels = new AnchorPane();
		productsInOrder = new ArrayList<>();
		int i = 0; // Index for the items layout on the scrollpane.
		/** Creating scrollpane for the tab. */
		for (Product p : productsToAdd) {
			Pane pane = new Pane();
			Label nameLabel = new Label(p.getDishName());
			Label priceLabel = new Label(p.getPrice() + "\u20AA");
			nameLabel.getStyleClass().addAll("overlayNameLayout", "padding");
			priceLabel.getStyleClass().addAll("overylayPriceLayout", "padding");
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
			 * Setting on clicked event, when user clicks on a certain product, it will
			 * display a "choose components" screen for the user to specifiy his requests.
			 * Also the user will be able to decide how many items of the same product
			 * should be in the order.
			 */
			pane.setOnMouseClicked(e -> {
				nextBtn.setDisable(true);
				leftArrowBtn.setDisable(true);
				/** Pane for the overlay screen. */
				overlayPane = new Pane();
				root.getChildren().add(overlayPane);
				overlayPane.getStyleClass().add("overlayLayout");
				/** Close button, closing the overlay screen. */
				Label closeBtn = new Label("X");
				closeBtn.getStyleClass().add("close");
				/** Setting a close button for the "choose components" screen. */
				closeBtn.setOnMouseClicked(clickedEvent -> {
					nextBtn.setDisable(false);
					leftArrowBtn.setDisable(false);
					root.getChildren().remove(overlayPane);
					ClientGUI.getClient().setLastResponse(null);
				});
				/** Title for the overlay screen, showing the product name. */
				Label title = new Label(nameLabel.getText());
				title.getStyleClass().add("componentTitle");
				/**
				 * If the product is already in the order, won't allow duplicate items.<br>
				 * User can choose to remove item or not in the pop-up panel.
				 */
				if (router.getOrder().getProducts() != null && router.getOrder().getProducts().contains(p)) {
					title.setText("Product is already in the order.\nPlease remove it before re-selecting it.");
					title.setLayoutY(60);
					Label removeItem = new Label("Remove Item");
					removeItem.setOnMouseClicked(evnt -> {
						router.getOrder().getProducts().remove(p);
						nextBtn.setDisable(false);
						leftArrowBtn.setDisable(false);
						root.getChildren().remove(overlayPane);
						setItemsCounter();
					});
					removeItem.setId("addItem");
					overlayPane.getChildren().addAll(closeBtn, title, removeItem);
					LoadingAnimation.overlayPaneTransition(overlayPane);
				} else {
					/** Under line for the overlay screen's title. */
					Line line = new Line();
					/**
					 * Setting the line to cross from 1 end of the screen to another(roughly).<br>
					 * Creating separation between the title to the rest of the panel.
					 */
					line.getStyleClass().add("line");
					line.setStartX(-100);
					line.setEndX(530);
					line.setStartY(0);
					line.setEndY(0);
					/** Product's description. */
					Label description = new Label(p.getDescription());
					description.getStyleClass().add("componentDescription");
					/** Product's price */
					final FloatProperty initialProductPrice = new SimpleFloatProperty(p.getPrice());
					Label price = new Label(String.format("Price %.2f \u20AA", initialProductPrice.get()));
					price.getStyleClass().add("productPrice");
					/** Counter to show how many product of this product are in the order. */
					Label counter = new Label("1");
					counter.getStyleClass().add("counter");
					/** Components selected for this order */
					Thread t = new Thread(() -> {
						synchronized(ClientGUI.getMonitor()) {
							ClientGUI.getClient().componentsInProduct(restaurantName, p.getDishName());
							try {
								ClientGUI.getMonitor().wait();
							}catch(Exception ex) {
								ex.printStackTrace();
								return;
							}
						}
					});
					t.start();
					try {
						t.join();
					}catch(Exception ex) {
						ex.printStackTrace();
						return;
					}
					ArrayList<Component> components;
					if(ClientGUI.getClient().getLastResponse().getMsg().equals("Failed to get components")) {
						components = null;
					}else {
						components = (ArrayList<Component>)ClientGUI.getClient().getLastResponse().getServerResponse();
					}
					ArrayList<Component> componentInProduct = new ArrayList<>();
					if (components == null || components.size() == 0) {
						/** If this dish doens't have any optional components */
						Label noComp = new Label(
								"Product " + nameLabel.getText() + " doesn't have optional components");
						noComp.getStyleClass().add("noComponents");
						overlayPane.getChildren().add(noComp);
					} else {
						/** Creating scroll pane for all the optional components. */
						ScrollPane componentContent = new ScrollPane();
						AnchorPane componentLabels = new AnchorPane();
						int j = 0;
						for (Component c : components) {
							/**
							 * Checking what type of component c is.<br>
							 * Displaying accordingly.
							 */
							if (checkIfSize(c)) {
								Label sizeTitle = new Label("Size");
								sizeTitle.getStyleClass().add("defaultTitle");
								sizeTitle.setLayoutX(10);
								sizeTitle.setLayoutY(j * 50 + 15);
								ComboBox<Size> size = new ComboBox<>();
								size.getItems().addAll(Size.values());
								size.getSelectionModel().select(Size.Medium);
								size.setLayoutX(40);
								size.setLayoutY(j * 50 + 10);
								/**
								 * Event listener on item selection change.<br>
								 * Calculating and setting the price accordingly.
								 */
								size.getSelectionModel().selectedItemProperty().addListener((obj, oldVal, newVal) -> {
									c.setSize(newVal);
									float productPrice;
									switch (newVal) {
									case Small:
										productPrice = initialProductPrice.get() * (float) Component.smallSizePrice
												* Integer.parseInt(counter.getText());
										price.setText(String.format("Price %.2f \u20AA", productPrice));
										return;
									case Large:
										productPrice = initialProductPrice.get() * (float) Component.largeSizePrice
												* Integer.parseInt(counter.getText());
										price.setText(String.format("Price %.2f \u20AA", productPrice));
										return;
									default:
										price.setText(String.format("Price %.2f \u20AA",
												p.getPrice() * Integer.parseInt(counter.getText())));
										return;
									}
								});
								componentInProduct.add(c);
								componentLabels.getChildren().addAll(sizeTitle, size);
							} else if (checkIfDoneness(c)) {
								Label donenessTitle = new Label("Doneness");
								donenessTitle.getStyleClass().add("defaultTitle");
								donenessTitle.setLayoutX(10);
								donenessTitle.setLayoutY(j * 50 + 15);
								ComboBox<Doneness> doneness = new ComboBox<>();
								doneness.getItems().addAll(Doneness.values());
								doneness.getSelectionModel().select(Doneness.medium);
								doneness.setLayoutX(70);
								doneness.setLayoutY(j * 50 + 10);
								doneness.getSelectionModel().selectedItemProperty()
										.addListener((obj, oldVal, newVal) -> {
											c.setDoneness(newVal);
										});
								componentInProduct.add(c);
								componentLabels.getChildren().addAll(donenessTitle, doneness);
							} else {
								Label com = new Label(c.toString());
								com.setLayoutX(10);
								com.setLayoutY(j * 50 + 10);
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
								componentLabels.getChildren().add(com);
							}
							j++;
						}
						componentContent.setId("background");
						componentLabels.setId("background");
						componentContent.getStyleClass().add("componentContent");
						componentContent.setContent(componentLabels);
						overlayPane.getChildren().add(componentContent);
					}
					/** Plus button to increase amount of the same product in the order. */
					Label plus = new Label("+");
					plus.getStyleClass().addAll("plusMinus", "plus");
					/**
					 * On click event, increase the amount of this product in the order. <br>
					 * Cap at 25.
					 */
					plus.setOnMouseClicked(mEvent -> {
						int count = Integer.parseInt(counter.getText());
						if (count == 25) {
							return;
						}
						Platform.runLater(() -> calculateCurrentPrice(componentInProduct, initialProductPrice, price,
								counter, count, 1));
					});
					/** Minus button to decrease amount of the same product in the order. */
					Label minus = new Label("-");
					/** Setting position for minus button. */
					minus.getStyleClass().addAll("plusMinus", "minus");
					/**
					 * On click event, decrease the amount of this product in the order. <br>
					 * Cap at 1.
					 */
					minus.setOnMouseClicked(mEvent -> {
						int count = Integer.parseInt(counter.getText());
						if (count == 1) {
							return;
						}
						Platform.runLater(() -> calculateCurrentPrice(componentInProduct, initialProductPrice, price,
								counter, count, -1));
					});
					/** Free text for the user to specify any notes for the order. */
					TextArea restrictions = new TextArea();
					restrictions.setPromptText("Anything we need to know?");
					restrictions.setId("txtarea");
					/** Title for the free text. */
					Label textAreaTitle = new Label("Anything we need to know?");
					textAreaTitle.getStyleClass().add("textAreaTitle");
					/**
					 * Add item button, adding the selected item with all the selected components
					 * and the free text from the user. Adding the item <counter> times to the
					 * order.
					 */
					Label addItem = new Label("Add Item");
					addItem.setId("addItem");
					/** Adding the desired product to the products array of the order */
					addItem.setOnMouseClicked(mEvent -> {
						String notes = restrictions.getText();
						/**
						 * If the restrictions free text is not null or empty, add new component to the
						 * order.
						 */
						if (notes != null && !notes.equals("")) {
							Component c = new Component(restrictions.getText());
							componentInProduct.add(c);
						}
						/** Adding the components list to this product. */
						p.setComponents(componentInProduct);
						p.setPrice(Float.parseFloat(price.getText().split(" ")[1]));
						/**
						 * Increasing the counter of the bag items (the one near the profile picture).
						 */
						int count = Integer.parseInt(counter.getText());
						/**
						 * Adding the product to products array count times(amount specify by the user).
						 */
						p.setAmount(count);
						productsInOrder.add(p);
						/**
						 * Setting global state for the router, adding <productsInOrder> to router
						 * singleton.
						 */
						if (router.getOrder().getProducts() == null) {
							router.getOrder().setProducts(new ArrayList<>(Arrays.asList(p)));
						} else {
							router.getOrder().getProducts().add(p);
						}
						setItemsCounter();
						root.getChildren().remove(overlayPane); // after <addItem> clicked, remove the overlay.
						nextBtn.setDisable(false);
						leftArrowBtn.setDisable(false);
					});
					/**
					 * Add all the labels to the overlay.
					 */
					overlayPane.getChildren().addAll(title, closeBtn, line, description, plus, minus, counter, addItem,
							restrictions, textAreaTitle, price);
					/** Overlay mount animation */
					LoadingAnimation.overlayPaneTransition(overlayPane);
				}
			});
			i++;
		}
		/** Adding the anchor pane to the scroll pane. */
		tabContent.setContent(tabLabels);
		/** Adding the scroll pane to the tab. */
		tab.setContent(tabContent);
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
	 * Checking if a certain component is of type Size.
	 * 
	 * @param component
	 * @return boolean
	 */
	public boolean checkIfSize(Component component) {
		Size[] size = Size.values();
		for (int i = 0; i < size.length; i++) {
			if (size[i].toString().equals(component.toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checking if a certain component is of type Doneness.
	 * 
	 * @param component
	 * @return boolean
	 */
	public boolean checkIfDoneness(Component component) {
		Doneness[] doneness = Doneness.values();
		for (int i = 0; i < doneness.length; i++) {
			if (doneness[i].toString().equals(component.toString())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Private method to calculate the current price of a product by <br>
	 * increasing / decreasing the amount of product type in the order.
	 * 
	 * @param componentInProduct
	 * @param initialProductPrice
	 * @param price
	 * @param counter
	 */
	private void calculateCurrentPrice(ArrayList<Component> componentInProduct, FloatProperty initialProductPrice,
			Label price, Label counter, int count, int toIncrease) {
		float currentPrice;
		if (componentInProduct.contains(new Component(Size.Small))) {
			currentPrice = (count + toIncrease) * initialProductPrice.get() * Component.smallSizePrice;
		} else {
			if (componentInProduct.contains(new Component(Size.Large))) {
				currentPrice = (count + toIncrease) * initialProductPrice.get() * Component.largeSizePrice;
			} else {
				currentPrice = (count + toIncrease) * initialProductPrice.get();
			}
		}
		price.setText(String.format("Price %.2f \u20AA", currentPrice));
		counter.setText(count + toIncrease + "");
	}
}
