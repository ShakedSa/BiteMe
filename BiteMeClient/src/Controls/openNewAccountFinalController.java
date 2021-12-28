package Controls;

import java.net.URL;
import java.util.ResourceBundle;

import Enums.UserType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class openNewAccountFinalController implements Initializable{
	
	public final UserType type = UserType.BranchManager;
	private Router router;
	private Stage stage;
	private Scene scene;
    @FXML
    private Text EmpName;

    @FXML
    private Text MonthlyBud;

    @FXML
    private TextField employersNameTxtField;

    @FXML
    private ImageView approvalBtn1;

    @FXML
    private Label approvalBtn;

    @FXML
    private ImageView leftArrowBtn;

    @FXML
    private Rectangle avatar;

    @FXML
    private Text managerPanelBtn;

    @FXML
    private Text homePageBtn;

    @FXML
    private Text logoutBtn;

    @FXML
    private Text profileBtn;

    @FXML
    private Text wouldLikeToOpen;

    @FXML
    private Text cardNumber;

    @FXML
    private TextField creditCardNumberTxtField;

    @FXML
    private Text m6;

    @FXML
    private Text m7;

    @FXML
    private Text m8;

    @FXML
    private ComboBox<?> accountCombo;

    @FXML
    private TextField monthlyBudTxtField;

    @FXML
    private Text dailyBud;

    @FXML
    private TextField dailyBudTxtField;

    @FXML
    private Text Error;

    @FXML
    private Text shekel;

    @FXML
    private Text shekel1;

    @FXML
    private Text updateSucess;

    @FXML
    private ImageView updateSucess1;

    @FXML
    void approvalClicked(MouseEvent event) {

    }

    @FXML
    void logoutClicked(MouseEvent event) {

    }

    @FXML
    void openTheFilelds(MouseEvent event) {

    }

    @FXML
    void profileBtnClicked(MouseEvent event) {

    }

    @FXML
    void returnToHomePage(MouseEvent event) {

    }

    @FXML
    void returnToManagerPanel(MouseEvent event) {

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
		router.setOpenNewAccountFinalController(this);
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
