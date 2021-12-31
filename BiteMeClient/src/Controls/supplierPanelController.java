package Controls;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import Entities.MyFile;
import Entities.ServerResponse;
import Entities.User;
import Enums.UserType;
import client.ClientGUI;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Natali 
 * This class describes the supplier panel
 */
public class supplierPanelController implements Initializable {

	public final UserType type = UserType.Supplier;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private ImageView supplierImage;

	@FXML
	private Rectangle avatar;

	@FXML
	private Label createMenuBtn;

	@FXML
	private Text homePageBtn;

	@FXML
	private Rectangle leftArrowBtn;

	@FXML
	private Text loadingTxt;

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
	private Text errorMsg;

	private User user = (User) ClientGUI.getClient().getUser().getServerResponse();
	private String restaurant = user.getOrganization();

	/**
	 * This method approached to the DB and display the LOGO of the supplier -
	 * restaurant in his panel
	 */
	public void setImage() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				ClientGUI.getClient().getSupplierImage(restaurant);
				synchronized (ClientGUI.getMonitor()) {
					try {
						ClientGUI.getMonitor().wait();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
				ServerResponse sr = ClientGUI.getClient().getLastResponse();
				MyFile myFile = (MyFile) sr.getServerResponse();
				byte[] imageArr = myFile.getMybytearray();
				BufferedImage img;
				try {
					img = ImageIO.read(new ByteArrayInputStream(imageArr));
					Image image = SwingFXUtils.toFXImage(img, null);
					loadingTxt.setVisible(false); // hide loading text
					supplierImage.setImage(image);
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			}
		});
		t.start();
	}

	/**
	 * This method initialize the next controller - updateMenuController
	 */
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
				controller.setMenu(); // set the last version of menu according DB
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
			router.getUpdateMenuController().setMenu();
			stage.setTitle("BiteMe - Update Menu");
			stage.setScene(router.getUpdateMenuController().getScene());
			stage.show();
		}
	}

	/**
	 * This method initialize the next controller - updateOrderTableController
	 */
	@FXML
	void updateOrederClicked(MouseEvent event) {
		if (router.getUpdateOrderTableController() == null) {
			AnchorPane mainContainer;
			updateOrderTableController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeUpdateOrderTablePage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setOrder(); // set update table with list of relevant orders
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
			router.getUpdateOrderTableController().setOrder();
			stage.setTitle("BiteMe - Update Order");
			stage.setScene(router.getUpdateOrderTableController().getScene());
			stage.show();
		}
	}
	
	/**
	 * This method initialize the next controller - viewIncomeReceiptController
	 */
    @FXML
    void viewIncomeReceiptClicked(MouseEvent event) {
		if (router.getViewIncomeReceiptController() == null) {
			AnchorPane mainContainer;
			viewIncomeReceiptController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeViewIncomeReceiptPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - View Income Receipt");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - View Income Receipt");
			stage.setScene(router.getViewIncomeReceiptController().getScene());
			stage.show();
		}
    }

	/**
	 * Logout and change scene to home page
	 */
	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	/**
	 * Changes scene to home page
	 */
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
		router.setArrow(leftArrowBtn, -90);
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

	/**
	 * Changes scene to profile
	 */
	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}
}
