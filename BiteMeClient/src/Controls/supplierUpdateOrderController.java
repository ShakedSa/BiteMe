package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import Enums.UserType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class supplierUpdateOrderController implements Initializable {

	public final UserType type = UserType.Supplier;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private TextField OrderNumberTxtField;

	@FXML
	private Rectangle avatar;

	@FXML
	private Text homePageBtn;

	@FXML
	private ComboBox<String> hourBox;

	@FXML
	private RadioButton includeDeliveryBtn;

	@FXML
	private ImageView leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private ComboBox<String> minutesBox;

	@FXML
	private RadioButton notIncludeDeliveryBtn;

	@FXML
	private CheckBox orderIsReadyBox;

	@FXML
	private CheckBox orderReceivedBox;

	@FXML
	private Text profileBtn;

	@FXML
	private Label searchBtn;

	@FXML
	private Text supplierPanelBtn;

	@FXML
	private Label updateOrderBtn;

	@FXML
	void UpdateOrderClicked(MouseEvent event) {

	}

	@FXML
	void chooseHour(MouseEvent event) {

	}

	@FXML
	void chooseMinutes(MouseEvent event) {

	}

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		router.changeSceneToHomePage();
	}

	@FXML
	void returnToSupplierPanel(MouseEvent event) {
		router.returnToSupplierPanel(event);
	}

	@FXML
	void searchClicked(MouseEvent event) {

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
		router.setSupplierUpdateOrderController(this);
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
