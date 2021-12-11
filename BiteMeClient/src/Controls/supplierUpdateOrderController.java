package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
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

public class supplierUpdateOrderController implements Initializable{
	
	private Router router;
	private Stage stage;
	private Scene scene;

    @FXML
    private TextField OrderNumberTxtField;

    @FXML
    private Rectangle avatar;

    @FXML
    private Text homePageBtn;

    @FXML
    private ComboBox<?> hourBox;

    @FXML
    private RadioButton includeDeliveryBtn;

    @FXML
    private ImageView leftArrowBtn;

    @FXML
    private Text logoutBtn;

    @FXML
    private ComboBox<?> minutesBox;

    @FXML
    private RadioButton notIncludeDeliveryBtn;

    @FXML
    private CheckBox orderIsReadyBox;

    @FXML
    private CheckBox orderReceivedBox;

    @FXML
    private Text profileBtn;

    @FXML
    private Label searchBtn;

    @FXML
    private Text supplierPanelBtn;

    @FXML
    private Label updateOrderBtn;

    @FXML
    void UpdateOrderClicked(MouseEvent event) {
    	
    }

    @FXML
    void chooseHour(MouseEvent event) {

    }

    @FXML
    void chooseMinutes(MouseEvent event) {

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
    	if (router.getSupplierPanelController() == null) {
			AnchorPane mainContainer;
			supplierPanelController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeSupplierPanelPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Supplier Panel");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return; 
			}
		} else {
			stage.setTitle("BiteMe - Supplier Panel");
			stage.setScene(router.getSupplierPanelController().getScene());
			stage.show();
		}
    }

    @FXML
    void searchClicked(MouseEvent event) {

    }
    
    /**
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		//router.setAvatar(avatar);
	}

    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setSupplierUpdateOrderController(this);
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

