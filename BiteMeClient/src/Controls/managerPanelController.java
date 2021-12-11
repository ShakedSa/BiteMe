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
	private AnchorPane mainContainer;

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
					avatarPicture = new Image(getClass().getResource("../images/manager-avatar.png").toString());
				case CEO:
					avatarPicture = new Image(getClass().getResource("../images/CEO-avatar.png").toString());
					break;
				case EmployerHR:
					avatarPicture = new Image(getClass().getResource("../images/HR-avatar.png").toString());
				case Customer:
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
		if (router.getAddNewSupplierController() == null) {
			AnchorPane mainContainer;
			addNewSupplierController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeAddNewSupplierPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Add New Supplier");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - Add New Supplier");
			stage.setScene(router.getAddNewSupplierController().getScene());
			stage.show();
		}
	}

	@FXML
	void AuthorizedEmployerApprovalClicked(MouseEvent event) {
		if (router.getAuthorizedEmployerApprovalController() == null) {
			AnchorPane mainContainer;
			authorizedEmployerApprovalController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeAuthorizedEmployerApprovalPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Authorized Employer Approval");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - Authorized Employer Approval");
			stage.setScene(router.getAuthorizedEmployerApprovalController().getScene());
			stage.show();
		}
	}

	@FXML
	void OpenNewAccountClicked(MouseEvent event) {
		if (router.getOpenNewAccountController() == null) {
			AnchorPane mainContainer;
			openNewAccountController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeOpenNewAccountPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Open New Account");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - Open New Account");
			stage.setScene(router.getOpenNewAccountController().getScene());
			stage.show();
		}
	}

	@FXML
	void UpdateUserInformationClicked(MouseEvent event) {
		if (router.getUpdateUserInformationController() == null) {
			AnchorPane mainContainer;
			updateUserInformationController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeUpdateUserInformationPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Update User Information");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - Update User Information");
			stage.setScene(router.getUpdateUserInformationController().getScene());
			stage.show();
		}
	}

	@FXML
	void UploadQuarterlyReportClicked(MouseEvent event) {
		if (router.getUploadQuarterlyReportController() == null) {
			AnchorPane mainContainer;
			uploadQuarterlyReportController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeUploadQuarterlyReportPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Upload Quarterly Report");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - Manager Panel");
			stage.setScene(router.getUploadQuarterlyReportController().getScene());
			stage.show();
		}
	}

	@FXML
	void viewMonthlyReportsClicked(MouseEvent event) {
		if (router.getViewMonthlyReportsController() == null) {
			AnchorPane mainContainer;
			viewMonthlyReportsController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeViewMonthlyReportsPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - View Monthly Reports");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - View Monthly Reports");
			stage.setScene(router.getViewMonthlyReportsController().getScene());
			stage.show();
		}
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
	

	public AnchorPane getMainContainer() {
		return mainContainer;
	}
	
	public void setContainer(AnchorPane mainContainer) {
		this.mainContainer = mainContainer;
	}
	

	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}


}
