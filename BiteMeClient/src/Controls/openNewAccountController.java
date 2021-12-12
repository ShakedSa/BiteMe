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

	public final UserType type= UserType.BranchManager;
	private Router router;
	private Stage stage;
	private Scene scene;

    @FXML
    private Text managerPanelBtn;

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
    	router.logOut();
    }

    @FXML
    void privateAccountClicked(MouseEvent event) {

    }
    
	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}

    @FXML
    void returnToHomePage(MouseEvent event) {
    	router.changeSceneToHomePage();
    }
    
    @FXML
    void returnToManagerPanel(MouseEvent event) {
    	router.returnToManagerPanel(event);
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

