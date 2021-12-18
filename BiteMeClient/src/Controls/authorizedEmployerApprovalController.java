package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.BusinessCustomer;
import Entities.ImportedUser;
import Entities.ServerResponse;
import Entities.User;
import Enums.UserType;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
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

public class authorizedEmployerApprovalController implements Initializable{

	public final UserType type= UserType.BranchManager;
	private Router router;
	private Stage stage;
	private Scene scene;
	
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
	    private Text employerCode;

	    @FXML
	    private TextField employerCodeTxtField;

	    @FXML
	    private Text noApprovals;

	    @FXML
	    private Text employerCompanyName;

	    @FXML
	    private TextField employerCompanyNameTxtField;

	    @FXML
	    private TableView<?> approvalTable;

	    @FXML
	    private Button checkForApprovals;

	@FXML
    void approvalClicked(MouseEvent event) {

	}

	@FXML
	void checkForApprovalsClicked(MouseEvent event) {
		ClientGUI.client.checkForApprovals();
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
		//get the server response- Business employers that needs approval 
		ArrayList<BusinessCustomer> response = (ArrayList<BusinessCustomer>) sr.getServerResponse();

		//check if ID is valid
		if(response.get(0) == null)
		{
			noApprovals.setVisible(true);
			enableEdit(false);
			return;
		}
		enableEdit(true);
		
	}
	
	private void enableEdit(boolean val) {
		employerCode.setVisible(val);
		employerCodeTxtField.setVisible(val);
		employerCompanyName.setVisible(val);
		employerCompanyNameTxtField.setVisible(val);
		approvalTable.setVisible(val);
	}
    
	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
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
    
    void changeSceneToHomePage(boolean val) {
		stage.setTitle("BiteMe - HomePage");
		stage.setScene(router.getHomePageController().getScene());
		stage.show();
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
		router.setAuthorizedEmployerApprovalController(this);
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

