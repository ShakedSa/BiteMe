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

public class ceoPanelController implements Initializable{
	

	public final UserType type= UserType.CEO;
	private Router router;
	private Stage stage;
	private Scene scene;

    @FXML
    private Text CEOPanelBtn;

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
    private Label viewAllReportBtn;

    @FXML
    private Label viewPDFReportBtn;

    @FXML
    private Label viewRevenueReportBtn;
    
    @FXML
    void viewAllReportBtnClicked(MouseEvent event) {

    }

    @FXML
    void viewPDFReportBtnClicked(MouseEvent event) {
    	if (router.getViewPDFQuarterlyReportController() == null) {
			AnchorPane mainContainer;
			viewPDFQuarterlyReportController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeViewPDFQuarterlyReportPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - View PDF Quarterly Report");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - View PDF Quarterly Report");
			stage.setScene(router.getViewPDFQuarterlyReportController().getScene());
			stage.show();
		}
    }

    @FXML
    void viewRevenueReportBtnClicked(MouseEvent event) {

    }

   
    @FXML
    void returnToHomePage(MouseEvent event) {
    	router.changeSceneToHomePage();
    }
    
    
    @FXML
    void logoutClicked(MouseEvent event) {
    	router.logOut();
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
		router.setCEOPanelController(this);
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
		this.stage=stage;
	}
	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}

}
