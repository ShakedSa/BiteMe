package Controls;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Enums.TypeOfProduct;
import Enums.UserType;
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

public class viewPDFQuarterlyReportController implements Initializable {

	public final UserType type = UserType.CEO;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private Text CEOPanelBtn;

	@FXML
	private Text errorMsg;

	@FXML
	private Rectangle avatar;

	@FXML
	private Text homePageBtn;

	@FXML
	private ImageView leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private ComboBox<String> monthBox;

	@FXML
	private Text profileBtn;

	@FXML
	private ComboBox<String> BranchBox;

	@FXML
	private Label viewPDFReportBtn;

	@FXML
	private ComboBox<String> yearBox;

	ObservableList<String> branch, month, year;

	// creating list of branches
	private void setBranchComboBox() {
		ArrayList<String> type = new ArrayList<String>();
		type.add("North");
		type.add("Central");
		type.add("South");

		branch = FXCollections.observableArrayList(type);
		BranchBox.setItems(branch);
	}

	// creating list of months
	private void setMonthComboBox() {
		ArrayList<String> type = new ArrayList<String>();
		type.add("January");
		type.add("February");
		type.add("March");
		type.add("April");
		type.add("May");
		type.add("June");
		type.add("July");
		type.add("August");
		type.add("September");
		type.add("October");
		type.add("November");
		type.add("December");

		month = FXCollections.observableArrayList(type);
		monthBox.setItems(month);
	}

	// creating list of years
	private void setYearComboBox() {
		ArrayList<String> type = new ArrayList<String>();
		type.add("2016");
		type.add("2017");
		type.add("2018");
		type.add("2019");
		type.add("2020");
		type.add("2021");

		year = FXCollections.observableArrayList(type);
		yearBox.setItems(year);
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
	void returnToCEOPanel(MouseEvent event) {
		router.returnToCEOPanel(event);
	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		router.changeSceneToHomePage();
	}

	@FXML
	void viewPDFReportClicked(MouseEvent event) {
		checkInputs();
	}

	private boolean checkInputs() {
		String branch = BranchBox.getValue();
		String month = monthBox.getValue();
		String year = yearBox.getValue();

		if (branch == null) {
			errorMsg.setText("Please select Branch");
			return false;
		}
		if (year == null) {
			errorMsg.setText("Please select an year");
			return false;
		}
		if (month == null) {
			errorMsg.setText("Please select specific month");
			return false;
		}
		errorMsg.setText("");
		return true;
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
		router.setViewPDFQuarterlyReportController(this);
		setStage(router.getStage());
		setBranchComboBox();
		setMonthComboBox();
		setYearComboBox();

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
