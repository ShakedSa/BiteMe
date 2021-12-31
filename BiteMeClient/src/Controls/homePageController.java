package Controls;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import Entities.MyFile;
import Entities.ServerResponse;
import Entities.Supplier;
import Entities.User;
import Enums.UserType;
import client.ClientGUI;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Homepage form controller.
 * 
 * @author Shaked
 * @author Natali
 * 
 * @version December 05 2021, v1.0
 */
/**
 * @author Eden
 *
 */
public class homePageController implements Initializable {

	private Router router;

	private Stage stage;

	private Scene scene = null;

	private AnchorPane mainContainer;
	private Deque<Supplier> favListDisplayed = new LinkedList<>(); // Displayed 3 fav restaurants
	private Deque<Supplier> favListHidden = new LinkedList<>(); // 3 hidden fav restaurants

	@FXML
	private ImageView bagImg;

	@FXML
	private Circle itemsCounterCircle;

	@FXML
	private ImageView caruasalLeft;

	@FXML
	private ImageView caruasalRight;

	@FXML
	private Text homePageBtn;

	@FXML
	private Text logOutBtn;

	@FXML
	private Text loginBtn;

	@FXML
	private Text profileBtn;

	@FXML
	private Button restaurantBtn;

	@FXML
	private Text userFirstName;

	@FXML
	private Rectangle avatar;

	@FXML
	private Text ceoBtn;

	@FXML
	private Text managerBtn;

	@FXML
	private Text supplierBtn;

	@FXML
	private Text employerHRBtn;

	@FXML
	private ImageView res1;

	@FXML
	private ImageView res2;

	@FXML
	private ImageView res3;
	
	@FXML
    private Text loadingTxt1;

    @FXML
    private Text loadingTxt2;

    @FXML
    private Text loadingTxt3;

	@FXML
	private Text itemsCounter;

	@FXML
	private AnchorPane root;

	@FXML
    private Rectangle arrowLeft;

    @FXML
    private Rectangle arrowRight;
	
	@FXML 
	private Text myOrdersBtn;

	/**
	 * Moving the carousel 1 step backward.
	 * 
	 * @param MouseEvent event
	 */
	@FXML
	void caruasalLeftClicked(MouseEvent event) {
		loadingTxt3.setVisible(true);
		loadingTxt3.toFront();
		Supplier supplier = favListHidden.pollLast();
		Supplier toHide = favListDisplayed.poll();
		favListHidden.addFirst(toHide);
		favListDisplayed.addLast(supplier);
		res1.setImage(res2.getImage());
		res2.setImage(res3.getImage());
		MyFile file = supplier.getRestaurantLogo();
		byte[] imageArr = file.getMybytearray();
		BufferedImage img;
		try {
			img = ImageIO.read(new ByteArrayInputStream(imageArr));
			Image image = SwingFXUtils.toFXImage(img, null);
			loadingTxt3.setVisible(false);
			res3.setImage(image);
		}catch(IOException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Moving the carousel 1 step forward
	 * 
	 * @param MouseEvent event
	 */
	@FXML
	void caruasalRight(MouseEvent event) {
		loadingTxt1.setVisible(true);
		loadingTxt1.toFront();
		Supplier supplier = favListHidden.pollFirst();
		Supplier toHide = favListDisplayed.pollLast();
		favListHidden.addLast(toHide);
		favListDisplayed.addFirst(supplier);
		res3.setImage(res2.getImage());
		res2.setImage(res1.getImage());
		MyFile file = supplier.getRestaurantLogo();
		byte[] imageArr = file.getMybytearray();
		BufferedImage img;
		try {
			img = ImageIO.read(new ByteArrayInputStream(imageArr));
			Image image = SwingFXUtils.toFXImage(img, null);
			loadingTxt1.setVisible(false);
			res1.setImage(image);
		}catch(IOException e) {
			e.printStackTrace();
			return;
		}
	}

	@FXML
	void ceoBtnClicked(MouseEvent event) {
		router.returnToCEOPanel(event);
	}

	@FXML
	void managerBtnClicked(MouseEvent event) {
		router.returnToManagerPanel(event);
	}

	@FXML
	void supplierBtnClicked(MouseEvent event) {
		router.returnToSupplierPanel(event);
	}

	@FXML
	void employerHRBtnClicked(MouseEvent event) {
		router.returnToEmployerHRPanel(event);
	}
	
	@FXML
    void myOrdersClicked(MouseEvent event) {
		if (router.getMyOrdersController() == null) {
			AnchorPane mainContainer;
			myOrdersController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeMyOrders.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.displayOpenOrders();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - My Orders");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			router.getMyOrdersController().setItemsCounter();
			router.getMyOrdersController().displayOpenOrders();
			stage.setTitle("BiteMe - My Orders");
			stage.setScene(router.getMyOrdersController().getScene());
			stage.show();
		}
    }

	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}

	/**
	 * OnClicked event handler, switching scenes to restaurant selection scene.
	 * 
	 * @param MouseEvent event
	 */
	@FXML
	void restaurantBtnClicked(ActionEvent event) {
		router.returnToCustomerPanel(null);
		if (router.getRestaurantselectionController() == null) {
			AnchorPane mainContainer;
			restaurantSelectionController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeRestaurantsPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.setRestaurants();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Restaurants");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			router.getRestaurantselectionController().setItemsCounter();
			stage.setTitle("BiteMe - Restaurants");
			stage.setScene(router.getRestaurantselectionController().getScene());
			stage.show();
		}
	}

	/**
	 * Switching scenes to login page.
	 * 
	 * @param MouseEvent event
	 */
	@FXML
	void displayLoginScreen(MouseEvent event) {
		if (router.getLogincontroller() == null) {
			AnchorPane mainContainer;
			loginController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeLoginPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Restaurants");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - Login");
			stage.setScene(router.getLogincontroller().getScene());
			stage.show();
		}
	}

	public AnchorPane getMainContainer() {
		return mainContainer;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Setting homePage buttons to match user's permissions.
	 */
	public void setProfile(boolean val) {
		ServerResponse resUser = ClientGUI.getClient().getUser();
		if (resUser != null) {
			User user = (User) resUser.getServerResponse();
			if (user != null) {
				setBagVisibility(user.getUserType() == UserType.Customer);
				userFirstName.setText(user.getFirstName());
				setDefaults(user, val);
			} else {
				setBagVisibility(false);
				userFirstName.setText("Guest");
				setDefaults(null, false);
			}
			logOutBtn.setStyle("-fx-cursor: hand;");
			profileBtn.setStyle("-fx-cursor: hand;");
			restaurantBtn.setStyle("-fx-cursor: hand;");
			homePageBtn.setStyle("-fx-cursor: hand;");

		} else {
			setBagVisibility(false);
			userFirstName.setText("Guest");
			setDefaults(null, false);
		}
	}

	/**
	 * OnClicked event handler, logout.
	 * 
	 * @param MouseEvent event
	 */
	@FXML
	void logOutBtnClicked(MouseEvent event) {
		router.logOut();
	}

	/**
	 * Changing the homePage interface depending on the user permissions.
	 * 
	 * @param boolean val
	 */
	private void setDefaults(boolean val) {
		setAvatar();
		logOutBtn.setVisible(val);
		profileBtn.setVisible(val);
		homePageBtn.setVisible(val);
		loginBtn.setVisible(!val);

	}

	/**
	 * Changing the homePage interface depending on the user permissions.
	 */
	private void setDefaults(User user, boolean val) { // after logout: null,false, after login: manager,true
		setDefaults(val);
		if (user == null) {
			managerBtn.setVisible(val);
			restaurantBtn.setVisible(val);
			supplierBtn.setVisible(val);
			employerHRBtn.setVisible(val);
			ceoBtn.setVisible(val);
			myOrdersBtn.setVisible(val);
		} else {
			switch (user.getUserType()) {
			case Customer:
				restaurantBtn.setVisible(val);
				myOrdersBtn.setVisible(val);
				break;
			case BranchManager:
				managerBtn.setVisible(val);
				break;
			case Supplier:
				supplierBtn.setVisible(val);
				break;
			case CEO:
				ceoBtn.setVisible(val);
				break;
			case EmployerHR:
				employerHRBtn.setVisible(val);
				break;
			default:
				break;
			}
		}

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
		router.setHomePageController(this);
		setStage(router.getStage());
		router.setArrow(arrowLeft, -90);
		router.setArrow(arrowRight, 90);
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Scene getScene() {
		return scene;
	}

	@FXML
	public void changeToCart(MouseEvent event) {
		router.changeToMyCart("HomePage");
	}

	/**
	 * Getting the favourite restaurants in the DB.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void setFavRestaurants() {
		Thread t = new Thread(new Runnable() {
			ArrayList<Supplier> favRestaurants;
			@Override
			public void run() {
				ClientGUI.getClient().favRestaurantsRequest();
				synchronized (ClientGUI.getMonitor()) {
					try {
						ClientGUI.getMonitor().wait();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
					favRestaurants = (ArrayList<Supplier>) ClientGUI.getClient().getLastResponse().getServerResponse();
					Platform.runLater(() -> setFavDisplayed(favRestaurants));
				}
			}
		});
		t.start();
	}

	/**
	 * Setting the queue's for the carousel.
	 * 
	 * @param String[] res
	 */
	private void setFavDisplayed(ArrayList<Supplier> favRestaurants) {
		for(int i =0 ; i< 3; i++) {
			Supplier supplier = favRestaurants.get(i);
			favListDisplayed.addLast(supplier);
			favListHidden.addLast(favRestaurants.get(i+3));
			MyFile file = supplier.getRestaurantLogo();
			byte[] imageArr = file.getMybytearray();
			BufferedImage img;
			try {
				img = ImageIO.read(new ByteArrayInputStream(imageArr));
				Image image = SwingFXUtils.toFXImage(img, null);
				switch(i) {
				case 0:
					loadingTxt1.setVisible(false);
					res1.setImage(image);
					break;
				case 1:
					loadingTxt2.setVisible(false);
					res2.setImage(image);
					break;
				case 2:
					loadingTxt3.setVisible(false);
					res3.setImage(image);
					break;
				}
			}catch(IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	
	public void setContainer(AnchorPane mainContainer) {
		this.mainContainer = mainContainer;
	}

	public void setItemsCounter() {
		itemsCounter.setText(router.getBagItems().size() + "");
	}

	private void setBagVisibility(boolean val) {
		bagImg.setVisible(val);
		itemsCounter.setVisible(val);
		itemsCounterCircle.setVisible(val);

	}
}
