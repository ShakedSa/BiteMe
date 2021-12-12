package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.ServerResponse;
import Entities.User;
import Enums.UserType;
import client.ClientGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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

public class addNewSupplierController implements Initializable {

	public final UserType type = UserType.BranchManager;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private Label addSupplierBtn;

	@FXML
	private Rectangle avatar;

	@FXML
	private Text homePageBtn;

	@FXML
	private Text uploadImageTxt;

	@FXML
	private ImageView leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Text managerPanelBtn;

	@FXML
	private ComboBox<String> monthlyCommissionBox;

	@FXML
	private Text profileBtn;

	@FXML
	private TextField restaurantAddressTxtField;

	@FXML
	private TextField restaurantManagerNameTxtField;

	@FXML
	private TextField restaurantNameTxtField;

	@FXML
	private TextField restaurantTypeTxtField;

	@FXML
	private ImageView uploadImage;
	
	ObservableList<String> list;

	// creating list of Commission
	private void setMonthlyCommissionComboBox() {
		ArrayList<String> type = new ArrayList<String>();
		type.add("7%");
		type.add("8%");
		type.add("9%");
		type.add("10%");
		type.add("11%");
		type.add("12%");

		list = FXCollections.observableArrayList(type);
		monthlyCommissionBox.setItems(list);
	}

	@FXML
	void addSupplierClicked(MouseEvent event) {

	}

	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		router.changeSceneToHomePage();
	}

	@FXML
	void returnToManagerPanel(MouseEvent event) {
		router.returnToManagerPanel(event);
	}

	@FXML
	void uploadImageClicked(MouseEvent event) {

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
		router.setAddNewSupplierController(this);
		setStage(router.getStage());
		setMonthlyCommissionComboBox();
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

}
