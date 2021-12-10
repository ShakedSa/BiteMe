package Controls;

import java.net.URL;
import java.util.ResourceBundle;
import Entities.ServerResponse;
import Entities.User;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class profileController implements Initializable{
	
	private Router router;
	private Stage stage;
	private Scene scene;

    @FXML
    private Text CEOPanelBtn;

    @FXML
    private Rectangle avatar;

    @FXML
    private Text emailTxt;

    @FXML
    private Text firstNameTxt;

    @FXML
    private Text homePageBtn;

    @FXML
    private Text idTxt;

    @FXML
    private Text lastNameTxt;

    @FXML
    private ImageView leftArrowBtn;

    @FXML
    private Text logoutBtn;

    @FXML
    private Text mainBranchTxt;

    @FXML
    private Text phoneNumberTxt;

    @FXML
    private Text profileBtn;

    @FXML
    private Text statusTxt;
    
    public void initProfile() {
    	User user = (User) ClientGUI.client.getUser().getServerResponse();
    	firstNameTxt.setText(user.getFirstName());
    	lastNameTxt.setText(user.getLastName());
    	idTxt.setText(user.getId());
    	phoneNumberTxt.setText(user.getPhoneNumber());
    	emailTxt.setText(user.getEmail());
    	mainBranchTxt.setText(user.getMainBranch().toString());
    	statusTxt.setText(user.getStatus().toString());
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
    void returnToHomePage(MouseEvent event) {
    	changeSceneToHomePage(true);
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
					new Image(getClass().getResource("../images/CEO-avatar.png").toString()));
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
		router.setProfileController(this);
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


