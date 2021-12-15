package Controls;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Entities.ServerResponse;
import Entities.User;
import Enums.BranchName;
import Enums.Status;
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
    private Text userName;
    
    @FXML
    private TextField userNameTxtField;
    
    @FXML
    private TextField PhoneNumberTxtField;

    @FXML
    private Label approvalBtn;

    @FXML
    private Rectangle avatar;
    
    @FXML
	private Label errorMsg;

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
    private Text EmpDetails;
    
    @FXML
    private Text EmpName;

    @FXML
    private Text EmpCode;
    
    @FXML
    private Text MonthlyDeb;
    
    @FXML
    private Text star1;
    
    @FXML
    private Text star2;
    
    @FXML
    private Text star3;
    
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
    	if(!CheckUserInput())
    		return;
    	
    	/*User newUser = new User(userNameTxtField.getText(), passwordTxtField.getText(),
    			firstNameTxtField.getText(), lastNameTxtField.getText(), idTxtField.getText(),
    			emailTxtField.getText(),PhoneNumberTxtField.getText(), userType,
    			employersNameTxtField.getText(), mainBranch, roll, status,avatar);*/
    	
    	
    }

    @FXML
    void businessAccountClicked(MouseEvent event) {
    	if(privateAccountBtn.isSelected())
    		privateAccountBtn.setSelected(false);
    	businessAccountFieldsVisabilaty(true);
    	
    }
    
    private void businessAccountFieldsVisabilaty(boolean val) {
    	EmpDetails.setVisible(val);
    	EmpCode.setVisible(val);
    	EmpName.setVisible(val);
    	MonthlyDeb.setVisible(val);
    	employersNameTxtField.setVisible(val);
    	employersCodeTxtField.setVisible(val);
    	monthlyDebitSpinner.setVisible(val);
    	star1.setVisible(val);
    	star2.setVisible(val);
    	star3.setVisible(val);
    }

    @FXML
    void logoutClicked(MouseEvent event) {
    	router.logOut();
    }
    
    private boolean CheckUserInput() {
		if (!router.checkValidText(userNameTxtField.getText())) {
			errorMsg.setText("Must fill username");
			return false;
		}
		if (router.checkSpecialCharacters(userNameTxtField.getText())) {
			errorMsg.setText("Special characters aren't allowed in username");
			return false;
		}
		if (!router.checkValidText(firstNameTxtField.getText())) {
			errorMsg.setText("Must fill first name");
			return false;
		}
		if (router.checkSpecialCharacters(firstNameTxtField.getText())) {
			errorMsg.setText("Special characters aren't allowed in name");
			return false;
		}
		if (!router.checkValidText(lastNameTxtField.getText())) {
			errorMsg.setText("Must fill in last name");
			return false;
		}
		if (router.checkSpecialCharacters(lastNameTxtField.getText())) {
			errorMsg.setText("Special characters aren't allowed in last name");
			return false;
		}
		if (!router.checkValidText(idTxtField.getText())) {
			errorMsg.setText("Must fill an id");
			return false;
		}
		if (router.checkSpecialCharacters(idTxtField.getText())) {
			errorMsg.setText("Special characters aren't allowed in id");
			return false;
		}
		if (!router.checkValidText(PhoneNumberTxtField.getText())) {
			errorMsg.setText("Must fill a phone number");
			return false;
		}
		if (router.checkSpecialCharacters(PhoneNumberTxtField.getText())) {
			errorMsg.setText("Special characters aren't allowed in phone number");
			return false;
		}
		if (!router.checkValidText(emailTxtField.getText())) {
			errorMsg.setText("Must fill in an email");
			return false;
		}
		if (!checkEmail(emailTxtField.getText())) {
			errorMsg.setText("Email must contain '@'");
			return false;
		}
		if (!router.checkValidText(creditCardNumberTxtField.getText())) {
			errorMsg.setText("Must fill credit card number");
			return false;
		}
		if (router.checkSpecialCharacters(creditCardNumberTxtField.getText())) {
			errorMsg.setText("Special characters aren't allowed in credit card number");
			return false;
		}
		
		
		
		errorMsg.setText("");
		return true;
	}
    
    boolean checkEmail(String email) {
    	return email.contains("@");
    }
    

    @FXML
    void privateAccountClicked(MouseEvent event) {
    	if(businessAccountBtn.isSelected())
    		businessAccountBtn.setSelected(false);
    	businessAccountFieldsVisabilaty(false);
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

