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

public class supplierPanelController implements Initializable {

	public final UserType type= UserType.Supplier;
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
    
	private User user = (User) ClientGUI.client.getUser().getServerResponse();
	private String restaurant = user.getOrganization();
    
    public void setImage() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				ClientGUI.client.getSupplierImage(restaurant);
				synchronized (ClientGUI.monitor) {
					try {
						ClientGUI.monitor.wait();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
				ServerResponse sr = ClientGUI.client.getLastResponse();
				MyFile myFile = (MyFile) sr.getServerResponse();
				byte[] imageArr = myFile.getMybytearray(); 
				BufferedImage img;
				try {
					img = ImageIO.read(new ByteArrayInputStream(imageArr));
					Image image = SwingFXUtils.toFXImage(img, null);
					loadingTxt.setVisible(false);// hide loading text
					supplierImage.setImage(image);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return;
				
			}
		});
		t.start();

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
				controller.setMenu();
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
				controller.createCombos();
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
			router.getSupplierUpdateOrderController().createCombos();
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
	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}

}

