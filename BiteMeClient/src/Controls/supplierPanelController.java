package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Entities.ServerResponse;
import Entities.User;
import Enums.UserType;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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

public class supplierPanelController implements Initializable {

	public final UserType type= UserType.Supplier;
	private Router router;
	private Stage stage;
	private Scene scene;
	
    @FXML
    private Rectangle avatar;

    @FXML
    private Label createMenuBtn;

    @FXML
    private Text homePageBtn;

    @FXML
    private ImageView leftArrowBtn;

    @FXML
    private Text logoutBtn;

    @FXML
    private Text profileBtn;

    @FXML
    private Text supplierPanelBtn;

    @FXML
    private Label updateMenuBtn;

    @FXML
    private Label updateOrderBtn;

    @FXML
    void createMenuClicked(MouseEvent event) {
    	if (router.getCreateMenuController() == null) {
			AnchorPane mainContainer;
			createMenuController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeCreateMenuPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Create Menu");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return; 
			}
		} else {
			stage.setTitle("BiteMe - Create Menu");
			stage.setScene(router.getCreateMenuController().getScene());
			stage.show();
		}
    }
    
    @FXML
    void updateMenuClicked(MouseEvent event) {
    	if (router.getUpdateMenuController() == null) {
			AnchorPane mainContainer;
			updateMenuController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeUpdateMenuPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Update Menu");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return; 
			}
		} else {
			stage.setTitle("BiteMe - Update Menu");
			stage.setScene(router.getUpdateMenuController().getScene());
			stage.show();
		}
    }

    @FXML
    void updateOrederClicked(MouseEvent event) {
    	if (router.getSupplierUpdateOrderController() == null) {
			AnchorPane mainContainer;
			supplierUpdateOrderController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeSupplierUpdateOrderPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Update Order");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - Update Order");
			stage.setScene(router.getSupplierUpdateOrderController().getScene());
			stage.show();
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
    

    /**
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		router.setAvatar(avatar);
	}
    
    @Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setSupplierPanelController(this);
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

}

