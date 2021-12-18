package Controls;

import java.net.URL;
import java.util.ResourceBundle;

import Entities.User;
import Enums.UserType;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class sendMsgToCustomerController implements Initializable {
	
	public final UserType type = UserType.Supplier;
	private Router router;
	private Stage stage;
	private Scene scene;

    @FXML
    private Rectangle avatar;

    @FXML
    private Label closeMsgBtn;

    @FXML
    private Text customerNameTxt;

    @FXML
    private Text priceTxt;

    @FXML
    private Text errorMsg;

    @FXML
    private Text homePageBtn;

    @FXML
    private Text logoutBtn;

    @FXML
    private Text phoneNumberTxt;

    @FXML
    private Text plannedTimeTxt;

    @FXML
    private Text profileBtn;

    @FXML
    private Text statusTxt;

    @FXML
    private Text supplierPanelBtn;
    
    public void displayMsg() {
		User user = (User) ClientGUI.client.getUser().getServerResponse();
		customerNameTxt.setText("Dear " );
		phoneNumberTxt.setText("Phone Number: " );
		priceTxt.setText("Your Order From " + user.getOrganization() + "Cost: ");
		statusTxt.setText("Order Status: ");
		//if the status is ready with delivery:
		plannedTimeTxt.setText("Planned Time: ");
		//need to display order status and planned time
	}

    @FXML
    void closeMsgBtnClicked(MouseEvent event) {
    	router.getSupplierPanelController().updateOrederClicked(event);
    }

    @FXML
    void logoutClicked(MouseEvent event) {
    	router.logOut();
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
    void returnToSupplierPanel(MouseEvent event) {
    	router.returnToSupplierPanel(event);
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
		router.setSendMsgToCustomerController(this);
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

