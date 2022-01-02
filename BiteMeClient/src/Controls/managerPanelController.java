package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Entities.ServerResponse;
import Entities.User;
import Enums.UserType;
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

	public final UserType type= UserType.BranchManager;
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
	private Rectangle leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Text profileBtn;

	@FXML
	private Label viewMonthlyReports;

    @FXML
    private Label UploadRevenueQuarterlyReport;
    
	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}


	@FXML
	void returnToHomePage(MouseEvent event) {
		router.changeSceneToHomePage();
	}

	/**
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		router.setAvatar(avatar);
	}

	@FXML
	void AddNewSupplierClicked(MouseEvent event) {
		if (router.getAddNewSupplierTableController() == null) {
			AnchorPane mainContainer;
			addNewSupplierTableController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/bitemeAddNewSupplierTablePage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.resetInfo();
				controller.initTable();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Add New Supplier");
				stage.setScene(mainScene);
				stage.show();
				
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			router.getAddNewSupplierTableController().resetInfo();
			router.getAddNewSupplierTableController().initTable();
			stage.setTitle("BiteMe - Add New Supplier");
			stage.setScene(router.getAddNewSupplierTableController().getScene());
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
				loader.setLocation(getClass().getResource("/gui/bitemeAuthorizedEmployerApprovalPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.initTable();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Authorized Employer Approval");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {

			router.getAuthorizedEmployerApprovalController().initTable();
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
				loader.setLocation(getClass().getResource("/gui/bitemeOpenNewAccountPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.initTable();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Open New Account");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {

			router.getOpenNewAccountController().initTable();
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
				loader.setLocation(getClass().getResource("/gui/bitemeUpdateUserInformationPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.resetScreen();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Update User Information");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			router.getUpdateUserInformationController().resetScreen();
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
				loader.setLocation(getClass().getResource("/gui/bitemeUploadQuarterlyReportPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
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
    void UploadRevenueQuarterlyReportClicked(MouseEvent event) {
		if (router.getCreateRevenueQuarterlyReportController() == null) {
			AnchorPane mainContainer;
			createRevenueQuarterlyReportController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/bitemeCreateRevenueQuarterlyReportPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Create Revenue Quarterly Report");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - Create Revenue Quarterly Report");
			stage.setScene(router.getCreateRevenueQuarterlyReportController().getScene());
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
				loader.setLocation(getClass().getResource("/gui/bitemeViewMonthlyReportsPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
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
