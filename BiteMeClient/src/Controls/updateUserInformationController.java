package Controls;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.ServerResponse;
import Enums.UserType;
import Util.InputValidation;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class updateUserInformationController implements Initializable{

	public final UserType type= UserType.BranchManager;
	private Router router;
	private Stage stage;
	private Scene scene;


	@FXML
    private Rectangle leftArrowBtn;

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
    private ImageView updateSucess1;

    @FXML
    private Label updateUserBtn;

    @FXML
    private Text updateSucess2;

    @FXML
    private Text userName;

    @FXML
    private TextField userNameTxtField;

    @FXML
    private Button searchBtn;

    @FXML
    private Text userNameError;

    @FXML
    private Text Error;

    @FXML
    private Label informUser;

    @FXML
    private Text userStatusLabel;

    @FXML
    private Label informUser1;

    @FXML
    private RadioButton action1;

    @FXML
    private RadioButton action2;
    
    private String validUserName;
    
    private String clientStatus;
    
    private String newStatus = "";
    
	
	
	@FXML
    void searchClicked(MouseEvent event) {
		if(!checkValues()) {
    		return;
    	}

		ClientGUI.getClient().checkUserNameIsClient(userNameTxtField.getText());

		//wait for response
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				ClientGUI.getClient().checkUserNameIsClient(userNameTxtField.getText());
				synchronized (ClientGUI.getMonitor()) {
					try {
						ClientGUI.getMonitor().wait();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
			}
		});
		t.start();
		try {
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		//handle server response
		ServerResponse sr = ClientGUI.getClient().getLastResponse();
		if(sr == null) {
			return;
		}
		@SuppressWarnings("unchecked")
		ArrayList<String> response = (ArrayList<String>) sr.getServerResponse();
		//check if the username is a client
		if(sr.getMsg().equals("is not client")) {
			userNameError.setText("This username is not a client!");
			userNameError.setVisible(true);
			enableEdit(false);
			return;
		}
		//check if username is valid
		if(sr.getMsg().equals("Error"))
		{
			userNameError.setText("This username doesn't exist");
			userNameError.setVisible(true);
			enableEdit(false);
			return;
		}
		userNameError.setVisible(true);
		userNameError.setText("Name: " + response.get(0) +" " + response.get(1));
		userStatusLabel.setText(response.get(2));
		updateSucess1.setVisible(false);
    	updateSucess2.setVisible(false);
		enableEdit(true);
		setUserOptions(response.get(2));
		validUserName = userNameTxtField.getText();
		clientStatus = response.get(2);	
    }
	
	
	
	/**change the visibality of screen items
	 * @param val
	 */
	private void enableEdit(boolean val) {
		informUser.setVisible(val);
		userStatusLabel.setVisible(val);
		informUser1.setVisible(val);
		action1.setVisible(val);
		action2.setVisible(val);
		updateUserBtn.setVisible(val);
		if(val == false) {
			updateSucess1.setVisible(val);
	    	updateSucess2.setVisible(val);
		}
	}
	
	private void setUserOptions(String status) {
		switch(status){
			case "Active":
				action1.setText("Freeze client account");
				action2.setText("Permanently remove this client");
				break;
			case "Frozen":
				action1.setText("Change client status to Active");
				action2.setText("Permanently remove this client");
				break;
			case "Unverified":
				action1.setText("Freeze client account");
				action2.setText("Permanently remove this client");
				break;
		}
	}
	
	
	/**manager marks the option of freezing client account
	* @param event
	*/
	@FXML
	void freezeClientClicked(MouseEvent event) {
		if(action1.isSelected()) {
			if(action2.isSelected())
				action2.setSelected(false);
			if(clientStatus.equals("Active")) {
				newStatus = "Frozen";
				updateSucess2.setText("Client account has been Frozen");
			}
			else {
				updateSucess2.setText("Client account has been Activated");
				newStatus = "Active";
			}
		}
		else {
			newStatus = "";
		}
	    }

	 
	/**manager marks the option of removing client account
	* @param event
	*/
	@FXML
	void removeClientClicked(MouseEvent event) {
		if(action2.isSelected()) {
			if(action1.isSelected())
				action1.setSelected(false); 
			newStatus = "Deleted";
			updateSucess2.setText("Client account has been Deleted");
		}
		else newStatus = "";
	}

    
    /**checks that the needed fields are filled correctly
     * @return
     */
    private boolean checkValues() {
    	if( InputValidation.checkSpecialCharacters(userNameTxtField.getText())) {
    		userNameError.setVisible(true);
    		enableEdit(false);
    		userNameError.setText("User name can't contain special characters!");
    		return false;
    	}
    	if(userNameTxtField.getText().length() == 0) {
    		userNameError.setVisible(true);
    		enableEdit(false);
    		userNameError.setText("User name must be filled!");
    		return false;
    	}
    	return true;
    }
    
    @FXML
    void updateUserBtnClicked(MouseEvent event) {
    	if(!userNameTxtField.getText().equals(validUserName)) {
    		userNameError.setText("UserName has to be searched first");
    		enableEdit(false);
    		userNameError.setVisible(true);
    		Error.setVisible(false);
    		return;
    	}
    	if(newStatus.equals("")) {
    		Error.setVisible(true);
    		return;
    	}
    	ClientGUI.getClient().changeClientPerrmisions(userNameTxtField.getText(), newStatus);
    	Error.setVisible(false);
    	enableEdit(false);
    	updateSucess2.setVisible(true);
    	updateSucess1.setVisible(true);
    	clearChoises();
    	
    }
    
    private void clearChoises() {
    	action1.setSelected(false);
    	action1.setSelected(false);
    	newStatus = "";
    }
    public void resetScreen() {
    	enableEdit(false);
    	Error.setVisible(false);
    	userNameError.setVisible(false);
    	userNameTxtField.clear();
    	newStatus = "";
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
    	router.returnToManagerPanel(event);
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
		router.setUpdateUserInformationController(this);
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

}

