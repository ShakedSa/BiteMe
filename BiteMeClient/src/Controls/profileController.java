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
    private Text restaurantBtn;
    
    @FXML
    private Text managerBtn;

    @FXML
    private Text supplierBtn;

    @FXML
    private Text ceoBtn;

    @FXML
    private Text employerHRBtn;
    
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
    	setAvatar();
    	setProfile();
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
			ImagePattern pattern = getAvatarImage();
			avatar.setFill(pattern);
			avatar.setEffect(new DropShadow(3, Color.BLACK));
			avatar.setStyle("-fx-border-width: 0");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	/**
	 * 
	 * @return ImagePattern relevant to user permissions
	 */
	private ImagePattern getAvatarImage() {
		ServerResponse userResponse = ClientGUI.client.getUser();
		if (userResponse == null) {
			return new ImagePattern(new Image(getClass().getResource("../images/guest-avatar.png").toString()));
		}
		User user = (User) userResponse.getServerResponse();
		if (user == null) {
			return new ImagePattern(new Image(getClass().getResource("../images/guest-avatar.png").toString()));
		}
		switch (user.getUserType()) {
		case Supplier:
			return new ImagePattern(new Image(getClass().getResource("../images/supplier-avatar.png").toString()));
		case BranchManager:
			return new ImagePattern(new Image(getClass().getResource("../images/manager-avatar.png").toString()));
		case CEO:
			return new ImagePattern(new Image(getClass().getResource("../images/CEO-avatar.png").toString()));
		case Customer:
			return new ImagePattern(new Image(getClass().getResource("../images/random-user.gif").toString()));
		case EmployerHR:
			return new ImagePattern(new Image(getClass().getResource("../images/HR-avatar.png").toString()));
		default:
			return new ImagePattern(new Image(getClass().getResource("../images/guest-avatar.png").toString()));
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
	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}
	
	/**
	 * Setting Profile page buttons to match user's permissions.
	 */
	public void setProfile() {
		boolean val=true;
		ServerResponse resUser = ClientGUI.client.getUser();
		User user = (User) resUser.getServerResponse();
		switch (user.getUserType()) {
		case Customer:
			restaurantBtn.setVisible(val);
			break;
		case BranchManager:
			managerBtn.setVisible(val);
			break;
		case Supplier:
			supplierBtn.setVisible(val);
			break;
		case CEO:
			ceoBtn.setVisible(val);
			break;
		case EmployerHR:
			employerHRBtn.setVisible(val);
			break;
		default:
			break;
		}
			logoutBtn.setStyle("-fx-cursor: hand;");
			profileBtn.setStyle("-fx-cursor: hand;");
			homePageBtn.setStyle("-fx-cursor: hand;");
	}
	
	//////////////
	

    @FXML
    void ceoBtnClicked(MouseEvent event) {
    	router.getHomePageController().ceoBtnClicked(event);
    }

    @FXML
    void employerHRBtnClicked(MouseEvent event) {

    }



    @FXML
    void managerBtnClicked(MouseEvent event) {

    }



    @FXML
    void restaurantBtnClicked(MouseEvent event) {

    }


    @FXML
    void supplierBtnClicked(MouseEvent event) {

    }
	///

}

