package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Enums.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class employerHRPanelController implements Initializable {

	public final UserType type = UserType.EmployerHR;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private Text employerHRPanelBtn;

	@FXML
	private Rectangle avatar;

	@FXML
	private Text homePageBtn;

	@FXML
	private Rectangle leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Text profileBtn;

	@FXML
	private Label viewPDFReportBtn;

	@FXML
	private Label viewRevenueReportBtn;

	@FXML
	private Label registerEmployerBtn;
	
    @FXML
    private Text loadingTxt;
    
    @FXML
    private ImageView hrImage;
    
	/**
	 * setting confirmBusinessAccount page
	 * @param event
	 */
	@FXML
	void confirmBusinessAccountBtnClicked(MouseEvent event) {
		if (router.getConfirmBusinessAccountController() == null) {
			
			AnchorPane mainContainer;
			confirmBusinessAccountController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/bitemeConfirmBusinessAccountPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.setTable();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Confirm Business Account");
				stage.setScene(mainScene);
				
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - Confirm Business Account");
			router.getConfirmBusinessAccountController().setTable();
			stage.setScene(router.getConfirmBusinessAccountController().getScene());
		}
		
		stage.show();
	}
	
	/**
	 * setting registerEmployer page
	 * @param event
	 */
	@FXML
	void registerEmployerBtnClicked(MouseEvent event) {
		if (router.getRegisterEmployerAsLegacyController() == null) {
			AnchorPane mainContainer;
			registerEmployerAsLegacyController controller;

			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/bitemeRegisterEmployerAsLegacyPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Register Employer As Legacy");
				stage.setScene(mainScene);

			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - Register Employer As Legacy");
			stage.setScene(router.getRegisterEmployerAsLegacyController().getScene());

		}
		stage.show();
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

	/**
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		router.setAvatar(avatar);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setEmployerHRPanelController(this);
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

	public Label getRegisterEmployerBtn() {
		return registerEmployerBtn;
	}

}
