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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
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

public class openNewAccountController implements Initializable{
	
	private Router router;
	private Stage stage;
	private Scene scene;

    @FXML
    private Text CEOPanelBtn;

    @FXML
    private TextField PhoneNumberTxtField;

    @FXML
    private Label approvalBtn;

    @FXML
    private Rectangle avatar;

    @FXML
    private RadioButton businessAccountBtn;

    @FXML
    private TextField creditCardNumberTxtField;

    @FXML
    private TextField emailTxtField;

    @FXML
    private TextField employersCodeTxtField;

    @FXML
    private TextField employersNameTxtField;

    @FXML
    private TextField firstNameTxtField;

    @FXML
    private Text homePageBtn;

    @FXML
    private TextField idTxtField;

    @FXML
    private TextField lastNameTxtField;

    @FXML
    private ImageView leftArrowBtn;

    @FXML
    private Text logoutBtn;

    @FXML
    private Spinner<?> monthlyDebitSpinner;

    @FXML
    private ComboBox<?> prefixPhoneNumberBox;

    @FXML
    private RadioButton privateAccountBtn;

    @FXML
    private Text profileBtn;

    @FXML
    void approvalClicked(MouseEvent event) {

    }

    @FXML
    void businessAccountClicked(MouseEvent event) {

    }

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
    void privateAccountClicked(MouseEvent event) {

    }

    @FXML
    void returnToHomePage(MouseEvent event) {
    	changeSceneToHomePage(true);
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
    
    void changeSceneToHomePage(boolean val) {
		stage.setTitle("BiteMe - HomePage");
		stage.setScene(router.getHomePageController().getScene());
		stage.show();
	}
    
    /**
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		try {
			avatar.setArcWidth(65);
			avatar.setArcHeight(65);
			ImagePattern pattern = new ImagePattern(
					new Image(getClass().getResource("../images/manager-avatar.png").toString()));
			avatar.setFill(pattern);
			avatar.setEffect(new DropShadow(3, Color.BLACK));
			avatar.setStyle("-fx-border-width: 0");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}

    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setOpenNewAccountController(this);
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

