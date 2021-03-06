package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.Customer;
import Entities.Delivery;
import Entities.Order;
import Entities.OrderDeliveryMethod;
import Entities.Product;
import Entities.W4CCard;
import Util.LoadingAnimation;
import client.ClientGUI;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Controller for the view of the order review page.
 * 
 * @author Shaked
 */
public class ReviewOrderController implements Initializable {

	private Router router;

	private Stage stage;

	private Scene scene;

	ScrollPane orderDisplay;
	Label itemsTitle;
	Label deliveryTitle;
	Label deliveryInformation;
	Label totalPrice;
	Label priceBreakDown;
	
	@FXML
	private Rectangle avatar;

	@FXML
	private Text itemsCounter;

	@FXML
	private Text returnToHome;

	@FXML
	private Rectangle returnToPaymentMethod;

	@FXML
	private Text returnToRestaurants;

	@FXML
	private Label submitOrder;

	@FXML
	private AnchorPane root;

	@FXML
	private Text logOut;

	@FXML
	private Text showProfile;

	@FXML
	private Circle circle;

	/**
	 * Save order in the db, switch to rate us scene.
	 */
	@FXML
	void SubmitOrder(MouseEvent event) {
		/** Creating loading animation to display while server processing the data. */
		Thread animation = new Thread(() -> {
			Platform.runLater(() -> {
				root.getChildren().removeAll(orderDisplay, itemsTitle, deliveryTitle, deliveryInformation,
						priceBreakDown, totalPrice);
				root.setDisable(true);
			});
			circle.setVisible(true);
			LoadingAnimation.LoadStart(circle);
		});
		animation.start();
		Order order = router.getOrder();
		switch (order.getPaymentMethod()) {
		case BusinessCode:
		case Both:
			Customer customer = (Customer) ClientGUI.getClient().getUser().getServerResponse();
			W4CCard w4cCard = customer.getW4c();
			if (router.getOrderDeliveryMethod().getFinalPrice() > w4cCard.getDailyBudget()) {
				w4cCard.setBalance(w4cCard.getBalance() - w4cCard.getDailyBudget());
				w4cCard.setDailyBalance(0);
			} else {
				w4cCard.setBalance(w4cCard.getBalance() - router.getOrderDeliveryMethod().getFinalPrice());
				w4cCard.setDailyBalance(w4cCard.getDailyBalance() - router.getOrderDeliveryMethod().getFinalPrice());
			}
			break;
		default:
			break;
		}
		Thread t = new Thread(() -> {
			synchronized (ClientGUI.getMonitor()) {
				ClientGUI.getClient().insertOrder(router.getOrderDeliveryMethod());
				try {
					ClientGUI.getMonitor().wait();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				/** After server finished handling the request continue executing. */
				if (ClientGUI.getClient().getLastResponse() != null
						&& ClientGUI.getClient().getLastResponse().getServerResponse() instanceof Integer) {
					/**
					 * Platform.runLater allows to change the view in a not fx application thread.
					 */
					Platform.runLater(() -> {
						root.setDisable(true);
						router.setBagItems(null);
						router.setOrder(new Order());
						router.setDelivery(null);
						router.setOrderDeliveryMethod(null);
						clearOrderProcess();
						root.setDisable(false);
						circle.setVisible(false);
						changeToRateUs();
					});
				} else {
					System.out.println("Failed to insert order");
				}
			}
		});
		t.start();
	}

	/**
	 * Private method to reset the order process after the user finished with the
	 * current one.
	 */
	private void clearOrderProcess() {
		router.setPaymentController(null);
		router.setIdentifyController(null);
		router.setPickDateAndTimeController(null);
		router.setRestaurantMenuController(null);
		router.setDeliveryMethodController(null);
	}

	/**
	 * Method to switch scene to Rate Us page.
	 */
	private void changeToRateUs() {
		if (router.getOrderReceivedController() == null) {
			AnchorPane mainContainer;
			OrderReceivedController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/bitemeOrderReceivedPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.setItemsCounter();
				controller.setRates((int) ClientGUI.getClient().getLastResponse().getServerResponse());
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Rate Us");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException ex) {
				ex.printStackTrace();
				return;
			}
		} else {
			router.getOrderReceivedController().setAvatar();
			router.getOrderReceivedController().setItemsCounter();
			router.getOrderReceivedController()
					.setRates((int) ClientGUI.getClient().getLastResponse().getServerResponse());
			stage.setTitle("BiteMe - BiteMe - Rate Us");
			stage.setScene(router.getOrderReceivedController().getScene());
			stage.show();
		}
	}

	/**
	 * Method to change scene to My Cart.
	 * 
	 * @param event
	 */
	@FXML
	public void changeToCart(MouseEvent event) {
		router.changeToMyCart("Review");
	}

	/**
	 * Switch to home page scene.
	 * 
	 * @param event
	 */
	@FXML
	void returnToHome(MouseEvent event) {
		router.changeSceneToHomePage();
	}

	/**
	 * Method to log out the user.
	 * 
	 * @param event
	 */
	@FXML
	void logOut(MouseEvent event) {
		router.logOut();
	}

	/**
	 * Switch to profile scene.
	 * 
	 * @param event
	 */
	@FXML
	void showProfile(MouseEvent event) {
		router.showProfile();
	}

	/**
	 * Method that switch to the previous step in the order process.
	 * 
	 * @param event
	 */
	@FXML
	void returnToPaymentMethod(MouseEvent event) {
		router.getOrderDeliveryMethod().calculateFinalPrice();
		router.getPaymentController().setAvatar();
		router.getPaymentController().setItemsCounter();
		router.getPaymentController().checkRefunds();
		stage.setTitle("BiteMe - Select Payment Method");
		stage.setScene(router.getPaymentController().getScene());
		stage.show();
	}

	/**
	 * Switch to restaurants page.
	 * 
	 * @param event
	 */
	@FXML
	void returnToRestaurants(MouseEvent event) {
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
		router.setReviewOrderController(this);
		setStage(router.getStage());
		router.setAvatar(avatar);
		router.setArrow(returnToPaymentMethod, -90);
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
	 * Setting the item counter of the order.
	 */
	public void setItemsCounter() {
		itemsCounter.setText(router.getBagItems().size() + "");
	}

	/**
	 * Method that create the view of the review order page.<br>
	 * Displaying the items in the order, delivery information and the final price
	 * of the order.
	 * 
	 */
	public void displayOrder() {
		root.getChildren().removeAll(orderDisplay, itemsTitle, deliveryTitle, deliveryInformation, priceBreakDown,
				totalPrice);
		OrderDeliveryMethod fullOrder = router.getOrderDeliveryMethod();
		Order order = fullOrder.getOrder();
		ArrayList<Product> products = order.getProducts();
		Delivery delivery = fullOrder.getDelivery();
		orderDisplay = new ScrollPane();
		Pane orderDisplayContent = new Pane();
		int i = 0;
		for (Product p : products) {
			Pane pane = new Pane();
			Label nameLabel = new Label(p.getDishName());
			Label priceLabel = new Label(p.getPrice() + "\u20AA");
			nameLabel.setStyle("-fx-padding: 10 0");
			priceLabel.setStyle("-fx-padding: 10 0");
			nameLabel.setLayoutX(15);
			priceLabel.setLayoutX(250);
			if (i % 2 != 0) {
				pane.setLayoutX(305);
				pane.setLayoutY((i - 1) * 25 + 15);
			} else {
				pane.setLayoutY(i * 25 + 15);
				pane.setLayoutX(5);
			}
			pane.setId("menuBtn");
			pane.getChildren().addAll(nameLabel, priceLabel);
			orderDisplayContent.getChildren().add(pane);
			i++;
		}
		itemsTitle = new Label("Products:");
		itemsTitle.getStyleClass().addAll("subtitle", "itemsTitle");
		orderDisplay.setContent(orderDisplayContent);
		orderDisplay.getStyleClass().add("orderDisplayScroll");
		orderDisplay.setId("scrollPane");
		deliveryTitle = new Label("Delivery information: ");
		deliveryTitle.getStyleClass().addAll("subtitle", "deliveryTitle");
		deliveryInformation = new Label(delivery.toString());
		deliveryInformation.getStyleClass().addAll("fields", "deliveryInfo");
		/* Creating the break down of the price. */
		priceBreakDown = new Label("Price Break Down:\n");
		priceBreakDown.getStyleClass().add("breakDown");
		totalPrice = new Label(String.format("Order Price: %.2f\u20AA", order.getOrderPrice()));
		totalPrice.setText(String.format("%s\nDelivery Price: %.2f\u20AA", totalPrice.getText(),
				delivery.getDelievryPrice() + delivery.getDiscount()));
		if(fullOrder.isRefundSelected()) {
			totalPrice.setText(String.format("%s\nRefund: %.2f\u20AA", totalPrice.getText(), fullOrder.getRefundForBreakDown()));
		}
		switch (fullOrder.getTypeOfOrder()) {
		case preorderDelivery:
			totalPrice.setText(
					String.format("%s\nOrder Discount: %.2f\u20AA", totalPrice.getText(), delivery.getDiscount()));
			break;
		case sharedDelivery:
			totalPrice.setText(
					String.format("%s\nDelivery Discount: %.2f\u20AA", totalPrice.getText(), delivery.getDiscount()));		
			break;
		default:
			break;
		}
		priceBreakDown.setText(String.format("%s\n\n\n\nFinal Price: %.2f\u20AA", priceBreakDown.getText(),
				fullOrder.getFinalPrice()));
		totalPrice.getStyleClass().add("totalPrice");
		if (root != null) {
			root.getChildren().addAll(orderDisplay, itemsTitle, deliveryTitle, deliveryInformation, totalPrice,
					priceBreakDown);
			circle.toFront();
		}
	}
}
