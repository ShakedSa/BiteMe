package Controls;

import java.net.URL;
import java.util.ResourceBundle;

import Enums.UserType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class confirmBusinessAccountController implements Initializable{
	
	public final UserType type= UserType.EmployerHR;
	private Router router;
	private Stage stage;
	private Scene scene;

    @FXML
    private Rectangle avatar;

    @FXML
    private TextField customerIDTxtField;

    @FXML
    private TextField employerCodeTxtField;

    @FXML
    private Text employerHRPanelBtn;

    @FXML
    private Text homePageBtn;

    @FXML
    private ImageView leftArrowBtn;

    @FXML
    private Text logoutBtn;

    @FXML
    private Text profileBtn;

    @FXML
    private Label registerBtn;

    @FXML
    void logoutClicked(MouseEvent event) {
    	router.logOut();
    }

    @FXML
    void profileBtnClicked(MouseEvent event) {
    	router.showProfile();
    }

    @FXML
    void registerBtnClicked(MouseEvent event) {

    }

    @FXML
    void returnToEmployerHRPanel(MouseEvent event) {
    	router.returnToEmployerHRPanel(event);
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
		router.setConfirmBusinessAccountController(this);
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
