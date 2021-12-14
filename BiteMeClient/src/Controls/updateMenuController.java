package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Enums.UserType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class updateMenuController implements Initializable {

	public final UserType type = UserType.Supplier;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private ImageView VImage;

	@FXML
	private Text menuUpdatedSuccessfullyTxt;

	@FXML
	private Circle addNewItemPlus;

	@FXML
	private Text addNewItemTxt;

	@FXML
	private Rectangle avatar;

	@FXML
	private ImageView editItemImage;

	@FXML
	private Text editItemTxt;

	@FXML
	private Text homePageBtn;

	@FXML
	private ImageView leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Text profileBtn;

	@FXML
	private Text saveTxt;

	@FXML
	private Text supplierPanelBtn;
	
	@FXML
	private Text errorMsg;

	@FXML
	void addNewItemClicked(MouseEvent event) {
		// router.getCreateMenuController().addNewItemClicked(event);
		if (router.getAddNewItemController() == null) {
			AnchorPane mainContainer;
			addNewItemController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeAddNewItemToMenuPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Add New Item To Menu");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - Add New Item To Menu");
			stage.setScene(router.getAddNewItemController().getScene());
			stage.show();
		}
	}

	@FXML
	void editItemClicked(MouseEvent event) {
		if (router.getEditMenuItemController() == null) {
			AnchorPane mainContainer;
			editMenuItemController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeEditMenuItemPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Edit Menu Item");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - Edit Menu Item");
			stage.setScene(router.getEditMenuItemController().getScene());
			stage.show();
		}
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
	void saveClicked(MouseEvent event) {
		// need to check server response
		if (checkServerResponse()) {
			VImage.setVisible(true);
			menuUpdatedSuccessfullyTxt.setVisible(true);
		}
	}

	private boolean checkServerResponse() {
//    	retVal = servaerResponse;
//    	if(retVal == null) {
//			errorMsg.setText("No items checked into menu");
//			return false;
//		}
//		errorMsg.setText(""); 
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
		router.setUpdateMenuController(this);
		setStage(router.getStage());
		VImage.setVisible(false);
		menuUpdatedSuccessfullyTxt.setVisible(false);
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
