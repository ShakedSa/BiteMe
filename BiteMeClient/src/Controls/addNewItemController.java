package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Enums.UserType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class addNewItemController implements Initializable{
	
	public final UserType type= UserType.Supplier;
	private Router router;
	private Stage stage;
	private Scene scene;

    @FXML
    private Label addItemToMenuBtn;

    @FXML
    private Rectangle avatar;

    @FXML
    private TextArea descriptionTxtArea;

    @FXML
    private Text homePageBtn;

    @FXML
    private TextField itemsNameTxtField;

    @FXML
    private ImageView leftArrowBtn;

    @FXML
    private Text logoutBtn;
    
    @FXML
    private Text uploadImageTxt;

    @FXML
    private TextField optionalComponentsTxtField;

    @FXML
    private Spinner<?> priceSpinner;

    @FXML
    private Text profileBtn;

    @FXML
    private ComboBox<String> selectTypeBox;

    @FXML
    private Text supplierPanelBtn;

    @FXML
    private Label uploadImageBtn;
    
    ObservableList<String> list;
    
 // creating list of Types
  	private void setTypeComboBox() {
  		ArrayList<String> type = new ArrayList<String>();
  		type.add("main dish");
  		type.add("entry");
  		type.add("dessert");
  		type.add("drink");

  		list = FXCollections.observableArrayList(type);
  		selectTypeBox.setItems(list);
  	}

    @FXML
    void addItemToMenuClicked(MouseEvent event) {

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

    @FXML
    void uploadImageClicked(MouseEvent event) {

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
		router.setAddNewItemController(this);
		setStage(router.getStage());
		setTypeComboBox();
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

