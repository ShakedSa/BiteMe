package Controls;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

import Enums.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class registerEmployerAsLegacyController implements Initializable{
	
	public final UserType type= UserType.EmployerHR;
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
    private ImageView leftArrowBtn;

    @FXML
    private Text logoutBtn;

    @FXML
    private Text profileBtn;

    @FXML
    private Label registerBtn;

    @FXML
    private TextField employerNameTxtField;
    
    @FXML
    private ImageView VImage;
    
    @FXML
    private Text errorMsg;
    
    @FXML
    private Text successMsg;

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
    	//need to check server response
    	// if false - display: errorMsg.setText("This employer company name doesn't exist");
    	if(checkInput()) {
    		VImage.setVisible(true);
    		successMsg.setVisible(true);
    	}
    	
    }
    
    private boolean checkInput() {
		String employerName = employerNameTxtField.getText();
		if (employerName.trim().isEmpty()) {
			errorMsg.setText("Please enter employer company name");
			return false;
		}
		errorMsg.setText("");
		return true;
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
		router.setRegisterEmployerAsLegacyController(this);
		setStage(router.getStage());
		VImage.setVisible(false);
    	successMsg.setVisible(false);
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

