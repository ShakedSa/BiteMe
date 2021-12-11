package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Entities.ServerResponse;
import Entities.User;
import Enums.UserType;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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

public class uploadQuarterlyReportController implements Initializable{
	

	public final UserType type= UserType.BranchManager;
	private Router router;
	private Stage stage;
	private Scene scene;

    @FXML
    private ImageView ImportImage;

    @FXML
    private Rectangle avatar;

    @FXML
    private Text homePageBtn;

    @FXML
    private ImageView leftArrowBtn;

    @FXML
    private Text logoutBtn;

    @FXML
    private Text managerPanelBtn;

    @FXML
    private ComboBox<?> monthBox;

    @FXML
    private Text profileBtn;

    @FXML
    private Label uploadReportBtn;

    @FXML
    private ComboBox<?> yearBox;

    @FXML
    void ImportImageClicked(MouseEvent event) {

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
    	if (router.getManagerPanelController() == null) {
			AnchorPane mainContainer;
			managerPanelController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeManagerPanelPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Manager Panel");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - Manager Panel");
			stage.setScene(router.getManagerPanelController().getScene());
			stage.show();
		}
    }

    @FXML
    void uploadReportClicked(MouseEvent event) {

    }
    
    
	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
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
		router.setUploadQuarterlyReportController(this);
		setStage(router.getStage());
	}

    
	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Scene getScene() {
		return scene;
	}

	public void setStage(Stage stage) {
		this.stage=stage;
	}

}

