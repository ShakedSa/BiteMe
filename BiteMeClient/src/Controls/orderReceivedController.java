package Controls;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

import client.ClientGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controller for the view of the order received page.
 * 
 * @author Shaked
 */
public class orderReceivedController implements Initializable {

	private Router router;

	private Stage stage;

	private Scene scene;

	@FXML
	private Text itemsCounter;

	@FXML
	private Rectangle avatar;

	@FXML
	private ImageView badRate;

	@FXML
	private ImageView excellentRate;

	@FXML
	private ImageView goodRate;

	@FXML
	private ImageView okRate;

	@FXML
	private ImageView veryBadRate;

	@FXML
	private Text errorMsg;

	@FXML
	private Button rateBtn;

	@FXML
	private Text rateTxt;

	@FXML
	private Text displayedText;

	private int orderNumber;

	/**
	 * Method to change scene to My Cart.
	 * 
	 * @param event
	 */
	@FXML
	void changeToCart(MouseEvent event) {
		router.setOrderReceivedController(null);
		router.changeToMyCart("Received");
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
	 * Switch to home page scene.
	 * 
	 * @param event
	 */
	@FXML
	void returnToHome(MouseEvent event) {
		router.setOrderReceivedController(null);
		router.changeSceneToHomePage();
	}

	/**
	 * Call to action button event handler.<br>
	 * switching to home page scene.
	 * 
	 * @param event
	 */
	@FXML
	void returnToHomePage(ActionEvent event) {
		returnToHome(null);
	}

	/**
	 * Switch to restaurants page.
	 * 
	 * @param event
	 */
	@FXML
	void returnToRestaurants(MouseEvent event) {
		router.setOrderReceivedController(null);
		router.getRestaurantselectionController().setItemsCounter();
		router.getRestaurantMenuController().setAvatar();
		stage.setTitle("BiteMe - Restaurants");
		stage.setScene(router.getRestaurantselectionController().getScene());
		stage.show();
	}

	/**
	 * Switch to profile scene.
	 * 
	 * @param event
	 */
	@FXML
	void showProfile(MouseEvent event) {
		router.setOrderReceivedController(null);
		router.showProfile();
	}

	/**
	 * Submitting the user's rating.
	 * 
	 * @param event
	 */
	@FXML
	void sendRate(ActionEvent event) {
		if (choosenRate == null) {
			errorMsg.setText("Please select rate");
			return;
		}
		ClientGUI.getClient().setRate(orderNumber, rates.get(choosenRate));
		errorMsg.setFill(Color.GREEN);
		errorMsg.setText("Thank you for rating");
		Set<ImageView> images = rates.keySet();
		for (ImageView image : images) {
			image.setVisible(false);
		}
		rateBtn.setVisible(false);
		rateTxt.setVisible(false);
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
		router.setOrderReceivedController(this);
		setStage(router.getStage());
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
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		router.setAvatar(avatar);
	}

	HashMap<ImageView, Integer> rates = new HashMap<>();
	ImageView choosenRate = null;

	/**
	 * Creating the available rates for this order.
	 * 
	 * @param orderNumber
	 */
	public void setRates(int orderNumber) {
		this.orderNumber = orderNumber;
		displayedText.setText("Order #" + orderNumber + " was recieved,");
		rates.put(veryBadRate, 1);
		rates.put(badRate, 2);
		rates.put(okRate, 3);
		rates.put(goodRate, 4);
		rates.put(excellentRate, 5);
		Set<ImageView> images = rates.keySet();
		for (ImageView image : images) {
			image.setOnMouseClicked(e -> {
				checkSelected();
				image.getStyleClass().add("selected");
				choosenRate = image;
			});
		}
	}

	/**
	 * De-selecting all the rates.
	 * 
	 * @return true.
	 */
	private boolean checkSelected() {
		Set<ImageView> images = rates.keySet();
		for (ImageView image : images) {
			image.getStyleClass().remove("selected");
		}
		return true;
	}
}
