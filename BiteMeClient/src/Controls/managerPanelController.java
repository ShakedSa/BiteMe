package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Entities.ServerResponse;
import Entities.User;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

public class managerPanelController implements Initializable {

	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private Label AddNewSupplier;

	@FXML
	private Label AuthorizedEmployerApproval;

	@FXML
	private Label OpenBusinessAccount;

	@FXML
	private Label OpenCustomerAccount;

	@FXML
	private Label UpdateUserInformation;

	@FXML
	private Label UploadQuarterlyReport;

	@FXML
	private Rectangle avatar;

	@FXML
	private Text homePageBtn;

	@FXML
	private Text managerPanelBtn;

	@FXML
	private ImageView leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Text profileBtn;

	@FXML
	private Label viewMonthlyReports;

	@FXML
	void logoutClicked(MouseEvent event) {
		ServerResponse resUser = ClientGUI.client.getUser();
		if (resUser != null) {
			User user = (User) resUser.getServerResponse();
			if (user != null) {
				ClientGUI.client.logout(user.getUserName());
				ClientGUI.client.setUser(null);
			}
		}
		router.getHomePageController().setProfile(false);
		changeSceneToHomePage(false);
	}

	@FXML
	void openProfile(MouseEvent event) {

	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		changeSceneToHomePage(true);
	}

	/**
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		Object userResponse = ClientGUI.client.getUser();
		Image avatarPicture = new Image(getClass().getResource("../images/guest-avatar.png").toString());
		if (userResponse != null) {
			User user = (User) ClientGUI.client.getUser().getServerResponse();
			if (user != null) {
				switch (user.getUserType()) {
				case Supplier:
					avatarPicture = new Image(getClass().getResource("../images/supplier-avatar.png").toString());
					break;
				case BranchManager:
				case CEO:
					avatarPicture = new Image(getClass().getResource("../images/manager-avatar.png").toString());
					break;
				case Customer:
				case BusinessCustomer:
					avatarPicture = new Image(getClass().getResource("../images/random-user.gif").toString());
				default:
					break;
				}
			}
		}
		try {
			avatar.setArcWidth(65);
			avatar.setArcHeight(65);
			ImagePattern pattern = new ImagePattern(avatarPicture);
			avatar.setFill(pattern);
			avatar.setEffect(new DropShadow(3, Color.BLACK));
			avatar.setStyle("-fx-border-width: 0");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

	void changeSceneToHomePage(boolean val) {
		stage.setTitle("BiteMe - HomePage");
		stage.setScene(router.getHomePageController().getScene());
		stage.show();
	}

	@FXML
	void AddNewSupplierClicked(MouseEvent event) {
		stage.setTitle("BiteMe - Add New Supplier");

		// need to add new controller

		// stage.setScene(router.getHomePageController().getScene());
		// stage.show();
	}

	@FXML
	void AuthorizedEmployerApprovalClicked(MouseEvent event) {
		stage.setTitle("BiteMe - Authorize Employer Approval");

		// need to add new controller

		// stage.setScene(router.getHomePageController().getScene());
		// stage.show();
	}

	@FXML
	void OpenBusinessAccountClicked(MouseEvent event) {
		stage.setTitle("BiteMe - Open Business Account");

		// need to add new controller

		// stage.setScene(router.getHomePageController().getScene());
		// stage.show();
	}

	@FXML
	void OpenCustomerAccountClicked(MouseEvent event) {
		stage.setTitle("BiteMe - Open Customer Account");

		// need to add new controller

		// stage.setScene(router.getHomePageController().getScene());
		// stage.show();
	}

	@FXML
	void UpdateUserInformationClicked(MouseEvent event) {
		stage.setTitle("BiteMe - Update User Information");

		// need to add new controller

		// stage.setScene(router.getHomePageController().getScene());
		// stage.show();
	}

	@FXML
	void UploadQuarterlyReportClicked(MouseEvent event) {
		stage.setTitle("BiteMe - Upload Quarterly Report");

		// need to add new controller

		// stage.setScene(router.getHomePageController().getScene());
		// stage.show();
	}

	@FXML
	void viewMonthlyReportsClicked(MouseEvent event) {

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setManagerPanelController(this);
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

}
