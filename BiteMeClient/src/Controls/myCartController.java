package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.Customer;
import Entities.Order;
import Entities.Product;
import client.ClientGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class myCartController implements Initializable {

	private Router router;

	private Stage stage;

	private Scene scene;
	@FXML
	private Rectangle leftArrowBtn;

	ScrollPane orderDisplay;
	Text cartTitle = new Text("My Cart");
	Label itemsTitle;
	Label deliveryTitle;
	Label deliveryInformation;
	Label totalPrice;
	Label removeItem;
	Label tableTitle;
	TableView<Order> orderTable;
	private String lastPage;
	Label errorMsg;

	@FXML
	private Rectangle avatar;

	@FXML
	private Text homePageBtn;

	@FXML
	private Text itemsCounter;

	@FXML
	private Text logoutBtn;

	@FXML
	private Text profileBtn;

	@FXML
	private Text restaurantsBtn;

	private AnchorPane root;

	@FXML
	private ScrollPane scrollRoot;

	/** When ever the user switch scenes remove the order from the screen. */
	private void clearScreen() {
		root.getChildren().clear();
		scrollRoot.setContent(null);
	}

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	@FXML
	void openProfile(MouseEvent event) {
		router.showProfile();
	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		router.changeSceneToHomePage();
	}

	@FXML
	void returnToMyOrders(MouseEvent event) {
		if (router.getMyOrdersController() == null) {
			AnchorPane mainContainer;
			myOrdersController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeMyOrders.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.displayOpenOrders();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - My Orders");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			router.getMyOrdersController().setItemsCounter();
			router.getMyOrdersController().displayOpenOrders();
			stage.setTitle("BiteMe - My Orders");
			stage.setScene(router.getMyOrdersController().getScene());
			stage.show();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setMyCartController(this);
		setStage(router.getStage());
		router.setArrow(leftArrowBtn, -90);
	}

	@FXML
	void returnToLastPage(MouseEvent event) {
		switch (lastPage) {
		case "Menu":
			if (router.getOrder().getRestaurantName() != null) {
				stage.setTitle("BiteMe - " + router.getOrder().getRestaurantName() + " Menu");
				stage.setScene(router.getRestaurantMenuController().getScene());
				router.getRestaurantMenuController().setRestaurantName(router.getOrder().getRestaurantName());
				router.getRestaurantMenuController().setMenu();
				router.getRestaurantMenuController().setItemsCounter();
				stage.show();
			} else {
				router.returnToCustomerPanel(event);
			}
			break;
		case "Restaurants":
			router.returnToCustomerPanel(event);
			break;
		case "Identify":
			router.returnToCustomerPanel(event);
			break;
		case "Review":
			router.getReviewOrderController().setItemsCounter();
			stage.setTitle("BiteMe - Review Order");
			stage.setScene(router.getReviewOrderController().getScene());
			stage.show();
			router.getReviewOrderController().displayOrder();
			break;
		case "Payment":
			router.getOrderDeliveryMethod().calculateFinalPrice();
			router.getPaymentController().setAvatar();
			router.getPaymentController().setItemsCounter();
			router.getPaymentController().checkRefunds();
			stage.setTitle("BiteMe - Select Payment Method");
			stage.setScene(router.getPaymentController().getScene());
			stage.show();
			break;
		case "Delivery":
			router.getDeliveryMethodController().setAvatar();
			router.getDeliveryMethodController().setItemsCounter();
			router.getDeliveryMethodController().createCombo();
			stage.setTitle("BiteMe - Select Delivery Method");
			stage.setScene(router.getDeliveryMethodController().getScene());
			stage.show();
			break;
		case "HomePage":
			returnToHomePage(null);
			break;
		case "MyCart":
			router.changeToMyCart("MyCart");
			break;
		case "Profile":
			openProfile(null);
			break;
		case "DateTime":
			router.getPickDateAndTimeController().setRestaurant(router.getOrder().getRestaurantName());
			router.getPickDateAndTimeController().setAvatar();
			router.getPickDateAndTimeController().setItemsCounter();
			router.getPickDateAndTimeController().createCombos();
			stage.setTitle("BiteMe - Pick Date And Time");
			stage.setScene(router.getPickDateAndTimeController().getScene());
			stage.show();
			break;
		case "Received":
			router.getOrderReceivedController().setAvatar();
			router.getOrderReceivedController().setItemsCounter();
			router.getOrderReceivedController().setRates((int) ClientGUI.getClient().getLastResponse().getServerResponse());
			stage.setTitle("BiteMe - BiteMe - Rate Us");
			stage.setScene(router.getOrderReceivedController().getScene());
			stage.show();
			break;
		case "MyOrders":
			router.getMyOrdersController().setItemsCounter();
			router.getMyOrdersController().displayOpenOrders();
			stage.setTitle("BiteMe - My Orders");
			stage.setScene(router.getMyOrdersController().getScene());
			stage.show();
			break;
		default:
			returnToHomePage(null);
			break;
		}
	}

	/**
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		router.setAvatar(avatar);
	}

	/**
	 * Setting the stage instance.
	 * 
	 * @param Stage stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Scene getScene() {
		return scene;
	}

	public void setItemsCounter() {
		itemsCounter.setText(router.getBagItems().size() + "");
	}

	@FXML
	public void changeToCart(MouseEvent event) {
		clearScreen();
		router.changeToMyCart("MyCart");
	}

	public void setLastPage(String lastPage) {
		this.lastPage = lastPage;
	}

	/**
	 * Displaying customer's order.<br>
	 * If the customer doesn't have an order yet, notify accordingly.
	 */
	public void displayOrder() {
		root = new AnchorPane();
		clearScreen();
		setItemsCounter();
		leftArrowBtn.setLayoutX(30);
		leftArrowBtn.setLayoutY(405);
		leftArrowBtn.setOnMouseClicked(e -> returnToLastPage(e));
		leftArrowBtn.setCursor(Cursor.HAND);
		root.getChildren().add(leftArrowBtn);
		cartTitle.setFont(new Font("Berlin Sans FB", 30));
		cartTitle.setLayoutX(51);
		cartTitle.setLayoutY(41);
		Order order = router.getOrder();
		ArrayList<Product> products = order.getProducts();
		itemsTitle = new Label("Products:");
		itemsTitle.setFont(new Font("Berlin Sans FB", 16));
		itemsTitle.setLayoutX(140);
		itemsTitle.setLayoutY(65);
		if (products == null || products.size() == 0) {
			itemsTitle.setText("Cart is empty");
			if (root != null) {
				root.getChildren().addAll(cartTitle, itemsTitle);
			}
		} else {
			orderDisplay = new ScrollPane();
			Pane orderDisplayContent = new Pane();
			int i = 0;
			/** Creating the pane layout of the screen. */
			for (Product p : products) {
				Pane pane = new Pane();
				Label nameLabel = new Label(p.getDishName());
				Label priceLabel = new Label(p.getPrice() + "\u20AA");
				nameLabel.setStyle("-fx-padding: 10 0");
				priceLabel.setStyle("-fx-padding: 10 0");
				nameLabel.setLayoutX(15);
				priceLabel.setLayoutX(260);
				if (i % 2 != 0) {
					pane.setLayoutX(305);
					pane.setLayoutY((i - 1) * 25 + 15);
				} else {
					pane.setLayoutY(i * 25 + 15);
					pane.setLayoutX(5);
				}
				pane.setId("menuBtn");
				pane.getStyleClass().add(p.getDishName());
				/** On click event to select items to remove. */
				pane.setOnMouseClicked(e -> {
					if (pane.getStyleClass().contains("toRemove")) {
						pane.getStyleClass().remove("toRemove");
					} else {
						pane.getStyleClass().add("toRemove");
					}
				});
				pane.getChildren().addAll(nameLabel, priceLabel);
				orderDisplayContent.getChildren().add(pane);
				i++;
			}
			orderDisplay.setContent(orderDisplayContent);
			orderDisplay.setPrefWidth(630);
			orderDisplay.setMaxHeight(130);
			orderDisplay.setLayoutX(120);
			orderDisplay.setLayoutY(100);
			orderDisplay.setId("scrollPane");
			totalPrice = new Label("Total: " + order.getOrderPrice() + "\u20AA");
			totalPrice.setFont(new Font("Berlin Sans FB", 22));
			totalPrice.setStyle("-fx-text-fill: #0a62a1;");
			totalPrice.setLayoutX(620);
			totalPrice.setLayoutY(250);
			removeItem = new Label("Remove Selected");
			removeItem.getStyleClass().add("removeBtn");
			removeItem.setFont(new Font("Berlin Sans FB", 16));
			removeItem.setLayoutX(120);
			removeItem.setLayoutY(250);
			/** On click event to remove all selected products. */
			removeItem.setOnMouseClicked(e -> {
				ArrayList<Product> newProducts = new ArrayList<>();
				orderDisplayContent.getChildren().removeIf(pane -> pane.getStyleClass().contains("toRemove"));
				products.forEach(p -> {
					/** Creating new products list */
					orderDisplayContent.getChildren().forEach(pane -> {
						if (pane.getStyleClass().contains(p.getDishName())) {
							newProducts.add(p);
						}
					});
				});
				router.setBagItems(newProducts);
				clearScreen();
				displayOrder();
			});
			root.getChildren().addAll(cartTitle, orderDisplay, itemsTitle, totalPrice, removeItem);
		}
		scrollRoot.setContent(root);
	}
}