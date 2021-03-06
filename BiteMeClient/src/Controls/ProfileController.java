package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import Entities.ServerResponse;
import Entities.User;
import Enums.BranchName;
import Enums.UserType;
import client.ClientGUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ProfileController implements Initializable {
	
	private Router router;
	private Stage stage;
	private Scene scene;
	private Scene lastScene;

	@FXML
	private ImageView bagImg;

	@FXML
	private Circle itemsCounterCircle;

	@FXML
	private Text restaurantBtn;

	@FXML
	private Text managerBtn;

	@FXML
	private Text supplierBtn;

	@FXML
	private Text ceoBtn;

	@FXML
	private Text employerHRBtn;

	@FXML
	private Text CEOPanelBtn;

	@FXML
	private Rectangle avatar;

	@FXML
	private Text emailTxt;

	@FXML
	private Text firstNameTxt;

	@FXML
	private Text homePageBtn;

	@FXML
	private Text idTxt;

	@FXML
	private Text lastNameTxt;

	@FXML
	private Rectangle leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private ComboBox<BranchName> mainBranchCombo;

	@FXML
	private Text phoneNumberTxt;

	@FXML
	private Text profileBtn;

	@FXML
	private Text statusTxt;

	@FXML
	private Text itemsCounter;
	private String lastSceneTitle;

	/**
	 * this method will set up profile information with relevant data
	 */
	public void initProfile() {
		setAvatar();
		setProfile();
		User user = (User) ClientGUI.getClient().getUser().getServerResponse();
		setBagVisibility(user.getUserType() == UserType.Customer); // hide bag on profile for non customers
		firstNameTxt.setText(user.getFirstName());
		lastNameTxt.setText(user.getLastName());
		idTxt.setText(user.getId());
		phoneNumberTxt.setText(user.getPhoneNumber());
		emailTxt.setText(user.getEmail());
		ObservableList<BranchName> branches = FXCollections.observableArrayList(Arrays.asList(BranchName.values()));
		mainBranchCombo.setItems(branches);
		mainBranchCombo.getSelectionModel().select(user.getMainBranch());
		statusTxt.setText(user.getStatus().toString());
	}

	/**
	 * set up bag visibility accorting to val
	 * @param val
	 */
	private void setBagVisibility(boolean val) {
		bagImg.setVisible(val);
		itemsCounter.setVisible(val);
		itemsCounterCircle.setVisible(val);
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
	void returnToLastPage(MouseEvent event) {
		stage.setScene(lastScene);
		stage.setTitle(lastSceneTitle);
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
		router.setProfileController(this);
		setStage(router.getStage());
		router.setArrow(leftArrowBtn, -90);
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

	@FXML
	void profileBtnClicked(MouseEvent event) {
		leftArrowBtn.setVisible(true);
		router.showProfile();
	}

	/**
	 * Setting Profile page buttons to match user's permissions.
	 */
	public void setProfile() {
		ServerResponse resUser = ClientGUI.getClient().getUser();
		hideAllBtns();
		User user = (User) resUser.getServerResponse();
		switch (user.getUserType()) {
		case Customer:
			restaurantBtn.setVisible(true);
			break;
		case BranchManager:
			managerBtn.setVisible(true);
			break;
		case Supplier:
			supplierBtn.setVisible(true);
			break;
		case CEO:
			ceoBtn.setVisible(true);
			break;
		case EmployerHR:
			employerHRBtn.setVisible(true);
			break;
		default:
			break;
		}
		logoutBtn.setStyle("-fx-cursor: hand;");
		profileBtn.setStyle("-fx-cursor: hand;");
		homePageBtn.setStyle("-fx-cursor: hand;");
		leftArrowBtn.setVisible(true);
	}

	/**
	 * sets disable to all buttons
	 */
	private void hideAllBtns() {
		restaurantBtn.setVisible(false);
		managerBtn.setVisible(false);
		supplierBtn.setVisible(false);
		ceoBtn.setVisible(false);
		employerHRBtn.setVisible(false);

	}

	@FXML
	void ceoBtnClicked(MouseEvent event) {
		router.returnToCEOPanel(event);
	}

	@FXML
	void employerHRBtnClicked(MouseEvent event) {
		router.returnToEmployerHRPanel(event);
	}

	@FXML
	void managerBtnClicked(MouseEvent event) {
		router.returnToManagerPanel(event);
	}

	@FXML
	void restaurantBtnClicked(MouseEvent event) {
		if (router.getMyOrdersController() == null) {
			AnchorPane mainContainer;
			MyOrdersController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/bitemeMyOrders.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.displayOpenOrders();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
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

	@FXML
	void supplierBtnClicked(MouseEvent event) {
		router.returnToSupplierPanel(event);
	}

	@FXML
	void changeToCart(MouseEvent event) {
		router.changeToMyCart("Profile");
	}

	/**
	 * sets item counter to relevant value.
	 */
	public void setItemsCounter() {
		itemsCounter.setText(router.getBagItems().size() + "");
	}

	/**
	 * @return the lastScene
	 */
	public Scene getLastScene() {
		return lastScene;
	}

	/**
	 * @param lastScene the lastScene to set
	 * @param Title
	 */
	public void setLastScene(Scene lastScene, String title) {
		this.lastScene = lastScene;
		this.lastSceneTitle = title;
	}

	/**
	 * this method will updated user's main branch according to his chosen value.
	 * @param event
	 */
	@FXML
	void updateProfile(ActionEvent event) {
		Thread t = new Thread(() -> {
			synchronized (ClientGUI.getMonitor()) {
				BranchName newVal = mainBranchCombo.getSelectionModel().getSelectedItem();
				ClientGUI.getClient().changeBranch(newVal);
				try {
					ClientGUI.getMonitor().wait();
				} catch (Exception e) {
					e.printStackTrace();
					return;
				}
				if (ClientGUI.getClient().getLastResponse().getMsg() == "Failed") {
					Platform.runLater(() -> {
						mainBranchCombo.getSelectionModel()
								.select(((User) ClientGUI.getClient().getUser().getServerResponse()).getMainBranch());
					});
				} else {
					Platform.runLater(() -> {
						mainBranchCombo.getSelectionModel().select(newVal);
						((User) ClientGUI.getClient().getUser().getServerResponse()).setMainBranch(newVal);
					});
				}
			}
		});
		t.start();
		leftArrowBtn.setVisible(false);
	}
	
	
}
