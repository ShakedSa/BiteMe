package Controls;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class pickDateAndTimeController implements Initializable {

	private Router router;

	private Stage stage;

	private Scene scene;

	private String restaurantName;

	@FXML
	private Rectangle avatar;

	@FXML
	private DatePicker datePicker;

	@FXML
	private Text homePageBtn;

	@FXML
	private ComboBox<String> hourBox;

	@FXML
	private Text itemsCounter;

	@FXML
	private ImageView leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private ComboBox<String> minutesBox;

	@FXML
	private Text profileBtn;

	@FXML
	private Text restaurantsBtn;

	@FXML
	private Label nextOrderStep;

	@FXML
	void nextOrderStep(MouseEvent event) {
		/** Needs to check combo boxes and datepicker values! */
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
	void returnToMenuOrderPage(MouseEvent event) {
		router.getRestaurantMenuController().setItemsCounter();
		router.getRestaurantMenuController().setAvatar();
		router.getRestaurantMenuController().setRestaurantName(restaurantName);
		stage.setTitle("BiteMe - " + restaurantName + " Menu");
		stage.setScene(router.getRestaurantMenuController().getScene());
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

	/**
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		router.setAvatar(avatar);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setPickDateAndTimeController(this);
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

	/**
	 * Setting values for the combo boxes. hourBox values from 0-23, minutesBox
	 * values from 0-59.
	 */
	public void createCombos() {
		ObservableList<String> hourOptions = FXCollections.observableArrayList(Arrays.asList(generator(24)));
		hourBox.getItems().addAll(hourOptions);
		ObservableList<String> minuteOptions = FXCollections.observableArrayList(Arrays.asList(generator(60)));
		minutesBox.getItems().addAll(minuteOptions);
	}

	/**
	 * Private method generating <size> strings to add to the combo box.
	 * 
	 * @param size
	 * 
	 * @return String[]
	 */
	private String[] generator(int size) {
		String[] res = new String[size];
		for (int i = 0; i < res.length; i++) {
			if (i < 10) {
				res[i] = "0" + i;
			} else {
				res[i] = i + "";
			}
		}
		return res;
	}
}
