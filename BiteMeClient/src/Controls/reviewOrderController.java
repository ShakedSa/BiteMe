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

public class reviewOrderController implements Initializable {

	private Router router;

	private Stage stage;

	private Scene scene;

	ScrollPane orderDisplay;
	Label itemsTitle;
	Label deliveryTitle;
	Label deliveryInformation;
	Label totalPrice;

	@FXML
	private Rectangle avatar;

	@FXML
	private Text itemsCounter;

	@FXML
	private Text returnToHome;

	@FXML
	private ImageView returnToPaymentMethod;

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
				root.getChildren().removeAll(orderDisplay, itemsTitle, deliveryTitle, deliveryInformation, totalPrice);
			});
			circle.setVisible(true);
			LoadingAnimation.LoadStart(circle);
		});
		animation.start();
		Order order = router.getOrder();
		switch (order.getPaymentMethod()) {
		case BusinessCode:
		case Both:
			Customer customer = (Customer) ClientGUI.client.getUser().getServerResponse();
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
		// Before:
//		Thread t = new Thread(() -> {
//			synchronized (ClientGUI.monitor) {
//				ClientGUI.client.insertOrder(router.getOrderDeliveryMethod());
//				try {
//					ClientGUI.monitor.wait();
//				} catch (Exception e) {
//					e.printStackTrace();
//					return;
//				}
//			}
//		});
		// After
		Thread t = new Thread(() -> {
			synchronized (ClientGUI.monitor) {
				ClientGUI.client.insertOrder(router.getOrderDeliveryMethod());
				try {
					ClientGUI.monitor.wait();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				/** After server finished handling the request continue executing. */
				if (ClientGUI.client.getLastResponse() != null
						&& ClientGUI.client.getLastResponse().getServerResponse() instanceof Integer) {
					router.setBagItems(null);
					router.setOrder(new Order());
					router.setDelivery(null);
					router.setOrderDeliveryMethod(null);
					/**
					 * Platform.runLater allows to change the view in a not fx application thread.
					 */
					Platform.runLater(() -> changeToRateUs());
				} else {
					System.out.println("Failed to insert order");
				}
			}
		});
		t.start();
//		try {
//			t.join();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return;
//		}
	}

	private void changeToRateUs() {
		if (router.getOrderReceivedController() == null) {
			AnchorPane mainContainer;
			orderReceivedController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeOrderReceivedPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.setItemsCounter();
				controller.setRates((int) ClientGUI.client.getLastResponse().getServerResponse());
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
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
			router.getOrderReceivedController().setRates((int) ClientGUI.client.getLastResponse().getServerResponse());
			stage.setTitle("BiteMe - BiteMe - Rate Us");
			stage.setScene(router.getOrderReceivedController().getScene());
			stage.show();
		}
	}

	@FXML
	public void changeToCart(MouseEvent event) {
		root.getChildren().removeAll(orderDisplay, itemsTitle, deliveryTitle, deliveryInformation, totalPrice);
		router.changeToMyCart("Review");
	}

	@FXML
	void returnToHome(MouseEvent event) {
		root.getChildren().removeAll(orderDisplay, itemsTitle, deliveryTitle, deliveryInformation, totalPrice);
		router.changeSceneToHomePage();
	}

	@FXML
	void logOut(MouseEvent event) {
		router.logOut();
	}

	@FXML
	void showProfile(MouseEvent event) {
		root.getChildren().removeAll(orderDisplay, itemsTitle, deliveryTitle, deliveryInformation, totalPrice);
		router.showProfile();
	}

	@FXML
	void returnToPaymentMethod(MouseEvent event) {
		root.getChildren().removeAll(orderDisplay, itemsTitle, deliveryTitle, deliveryInformation, totalPrice);
		router.getPaymentController().setAvatar();
		router.getPaymentController().setItemsCounter();
		stage.setTitle("BiteMe - Select Payment Method");
		stage.setScene(router.getPaymentController().getScene());
		stage.show();
	}

	@FXML
	void returnToRestaurants(MouseEvent event) {
		root.getChildren().removeAll(orderDisplay, itemsTitle, deliveryTitle, deliveryInformation, totalPrice);
		router.getRestaurantselectionController().setItemsCounter();
		stage.setTitle("BiteMe - Restaurants");
		stage.setScene(router.getRestaurantselectionController().getScene());
		stage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setReviewOrderController(this);
		setStage(router.getStage());
		router.setAvatar(avatar);

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

	public void setItemsCounter() {
		itemsCounter.setText(router.getBagItems().size() + "");
	}

	public void displayOrder() {
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
			priceLabel.setLayoutX(260);
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
		itemsTitle.setFont(new Font("Berlin Sans FB", 14));
		itemsTitle.setLayoutX(120);
		itemsTitle.setLayoutY(193);
		orderDisplay.setContent(orderDisplayContent);
		orderDisplay.setPrefWidth(676);
		orderDisplay.setPrefHeight(130);
		orderDisplay.setLayoutX(100);
		orderDisplay.setLayoutY(213);
		orderDisplay.setId("scrollPane");
		deliveryTitle = new Label("Delivery information: ");
		deliveryTitle.setFont(new Font("Berlin Sans FB", 14));
		deliveryTitle.setLayoutX(120);
		deliveryTitle.setLayoutY(360);
		deliveryInformation = new Label(delivery.toString());
		deliveryInformation.setFont(new Font("Berlin Sans FB", 13));
		deliveryInformation.setLayoutX(100);
		deliveryInformation.setLayoutY(380);
		totalPrice = new Label("Total: " + fullOrder.getFinalPrice() + "\u20AA");
		totalPrice.setFont(new Font("Berlin Sans FB", 22));
		totalPrice.setStyle("-fx-text-fill: #0a62a1;");
		totalPrice.setLayoutX(600);
		totalPrice.setLayoutY(400);
		if (root != null) {
			root.getChildren().addAll(orderDisplay, itemsTitle, deliveryTitle, deliveryInformation, totalPrice);
			circle.toFront();
		}
	}
}
