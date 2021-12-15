package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import Entities.ServerResponse;
import Entities.User;
import Enums.Status;
import Enums.TypeOfOrder;
import Enums.UserType;
import client.ClientGUI;
import client.ClientUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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

public class updateUserInformationController implements Initializable{

	public final UserType type= UserType.BranchManager;
	private Router router;
	private Stage stage;
	private Scene scene;


    @FXML
    private Button searchBtn;
    
    @FXML
    private Text managerPanelBtn;

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
    private Label saveUpdateBtn;

    @FXML
    private TextField userNameTxtField;
    
    @FXML
    private Text userNameError;

    @FXML
    private Text updateSucess;

    @FXML
    private ImageView updateSucess1;

    @FXML
    private ComboBox<String> userPermitionBox;

    @FXML
    private ComboBox<String> userStatusBox;

    @FXML
    private Text userPremTxt;

    @FXML
    private Text userStatusTxt;
    
    private String validUserName;
    
    /**
	 * Creating the combo boxes in this scene. for userPermitionBox
	 * 
	 */
	public void createUserTypeCombo() {
		ObservableList<String> typeOfUsers = FXCollections
				.observableArrayList(Arrays.asList(UserType.BranchManager.toString(),
						UserType.BusinessCustomer.toString(), UserType.Customer.toString(),
						UserType.EmployerHR.toString(), UserType.Supplier.toString()));
		userPermitionBox.getItems().addAll(typeOfUsers);
	}
	
	/**
	 * Creating the combo boxes in this scene. for deliveryMethodBox set on change
	 * event listener to change the state of the scene accordingly to the selected
	 * value.
	 */
	public void createUserStatusCombo() {
		ObservableList<String> typeOfStatus = FXCollections
				.observableArrayList(Arrays.asList(Status.Active.toString(),
						Status.Frozen.toString(), Status.Unverified.toString()));
		userStatusBox.getItems().addAll(typeOfStatus);

	}
	
	@FXML
    void searchClicked(MouseEvent event) {
		if(!checkValues()) {
    		return;
    	}
		ClientGUI.client.checkUser(userNameTxtField.getText());
		//wait for response
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (ClientGUI.monitor) {
					try {
						ClientGUI.monitor.wait();
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
		ServerResponse sr = ClientGUI.client.getLastResponse();
		@SuppressWarnings("unchecked")
		ArrayList<String> response = (ArrayList<String>) sr.getServerResponse();
		if(response== null) {
			System.out.println("test");
			return;
		}
		//check if user name is valid
		if(response.get(0).equals("Error"))
		{
			userNameError.setText("This user name doesn't exist");
			userNameError.setVisible(true);
			enableEdit(false);
			return;
		}
		enableEdit(true);
		userPermitionBox.setValue(response.get(0));
		userStatusBox.setValue(response.get(1));
		userNameError.setVisible(false);
		validUserName = userNameTxtField.getText();
    }

	private void enableEdit(boolean val) {
		userPermitionBox.setVisible(val);
		userStatusBox.setVisible(val);
		userPremTxt.setVisible(val);
		userStatusTxt.setVisible(val);
		saveUpdateBtn.setDisable(!val);
		if(val == false) {
			updateSucess.setVisible(val);
	    	updateSucess1.setVisible(val);
		}
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
	
    @FXML
    void saveUpdateClicked(MouseEvent event) {
    	if(!userNameTxtField.getText().equals(validUserName)) {
    		userNameError.setVisible(true);
    		return;
    	}
    	ClientGUI.client.updateUserInfo(userNameTxtField.getText(),userPermitionBox.getValue()
    			, userStatusBox.getValue());
    	userNameError.setVisible(false);
    	updateSucess.setVisible(true);
    	updateSucess1.setVisible(true);
    	
    	
    }
    
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
		createUserTypeCombo();
		createUserStatusCombo();
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

