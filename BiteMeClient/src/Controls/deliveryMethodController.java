package Controls;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import Enums.TypeOfOrder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class deliveryMethodController implements Initializable {

	private Router router;

	private Stage stage;

	private Scene scene;

	private String restaurantName;

	@FXML
	private Rectangle avatar;

	@FXML
	private ComboBox<String> deliveryMethodBox;

	@FXML
	private Label errorMsg;

	@FXML
	private Text homePageBtn;

	@FXML
	private Text itemsCounter;

	@FXML
	private ImageView leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Label nextOrderStep;

	@FXML
	private Text profileBtn;

	@FXML
	private Text restaurantsBtn;

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	@FXML
	void nextOrderStep(MouseEvent event) {
		String selectedMethod = deliveryMethodBox.getSelectionModel().getSelectedItem();
		if (selectedMethod == null) {
			errorMsg.setText("Please select delivery method.");
			return;
		}
		if (selectedMethod.equals("Robot Delivery")) {
			errorMsg.setText("Robot delivery is not available yet.\nPlease select a different delivery method.");
			return;
		}
		TypeOfOrder typeOfOrder = TypeOfOrder.getEnum(selectedMethod);
		errorMsg.setText("");
		/**
		 * Move to the next step in the order. Will wait for order - delivery conflict ><
		 */
	}

	@FXML
	void openProfile(MouseEvent event) {
		router.showProfile();
	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		router.getHomePageController().setItemsCounter();
		router.getHomePageController().setAvatar();
		stage.setTitle("BiteMe - HomePage");
		stage.setScene(router.getHomePageController().getScene());
		stage.show();
	}

	@FXML
	void returnToPickDateAndTime(MouseEvent event) {
		router.getPickDateAndTimeController().setItemsCounter();
		router.getPickDateAndTimeController().setAvatar();
		stage.setTitle("BiteMe - Pick Date And Time");
		stage.setScene(router.getPickDateAndTimeController().getScene());
		stage.show();
	}

	@FXML
	void returnToRestaurants(MouseEvent event) {
		router.getRestaurantselectionController().setItemsCounter();
		router.getRestaurantMenuController().setAvatar();
		stage.setTitle("BiteMe - Restaurants");
		stage.setScene(router.getRestaurantselectionController().getScene());
		stage.show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setDeliveryMethodController(this);
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

	public void setRestaurant(String restaurantName) {
		this.restaurantName = restaurantName;
	}

	public void setItemsCounter() {
		itemsCounter.setText(router.getBagItems().size() + "");
	}

	public void setAvatar() {
		router.setAvatar(avatar);
	}

	public void createCombo() {
		ObservableList<String> typeOfOrders = FXCollections
				.observableArrayList(Arrays.asList(TypeOfOrder.BasicDelivery.toString(),
						TypeOfOrder.preorderDelivery.toString(), TypeOfOrder.sharedDelivery.toString(),
						TypeOfOrder.takeaway.toString(), TypeOfOrder.RobotDelivery.toString()));
		deliveryMethodBox.getItems().addAll(typeOfOrders);
	}

}
