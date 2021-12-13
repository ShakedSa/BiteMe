package Controls;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.Delivery;
import Entities.Order;
import Entities.OrderDeliveryMethod;
import Entities.Product;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
	void SubmitOrder(MouseEvent event) {

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
			Label priceLabel = new Label(p.getPrice() + "¤");
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
		if (delivery != null) {
			deliveryInformation = new Label(delivery.toString());
		}
		deliveryInformation.setFont(new Font("Berlin Sans FB", 13));
		deliveryInformation.setLayoutX(100);
		deliveryInformation.setLayoutY(380);
		totalPrice = new Label("Total: " + fullOrder.getFinalPrice() + "¤");
		totalPrice.setFont(new Font("Berlin Sans FB", 22));
		totalPrice.setStyle("-fx-text-fill: #0a62a1;");
		totalPrice.setLayoutX(600);
		totalPrice.setLayoutY(400);
		if (root != null) {
			root.getChildren().addAll(orderDisplay, itemsTitle, deliveryTitle, deliveryInformation, totalPrice);
		}
		System.out.println(fullOrder);
	}
}
