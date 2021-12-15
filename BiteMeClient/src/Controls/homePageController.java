package Controls;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.Set;

import Entities.ServerResponse;
import Entities.User;
import Enums.UserType;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
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
	private Deque<String> favListDisplayed = new LinkedList<>(); // Displayed 3 fav restaurants
	private Deque<String> favListHidden = new LinkedList<>(); // 3 hidden fav restaurants


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
	private Text restaurantBtn;

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
	private Text itemsCounter;

	/**
	 * Moving the carousel 1 step backward.
	 * 
	 * @param MouseEvent event
	 */
	@FXML
	void caruasalLeftClicked(MouseEvent event) {
		String resName = favListHidden.pollLast().toLowerCase();
		favListHidden.addFirst(res1.getId());
		favListDisplayed.addLast(resName);
		res1.setImage(res2.getImage());
		res1.setId(res2.getId());
		res2.setImage(res3.getImage());
		res2.setId(res3.getId());
		res3.setImage(new Image(getClass().getResource("../images/" + resName + "-logo.jpg").toString()));
		res3.setId(resName);
	}

	/**
	 * Moving the carousel 1 step forward
	 * 
	 * @param MouseEvent event
	 */
	@FXML
	void caruasalRight(MouseEvent event) {
		String resName = favListHidden.pollFirst().toLowerCase();
		favListHidden.addLast(res3.getId());
		favListDisplayed.addFirst(resName);
		res3.setImage(res2.getImage());
		res3.setId(res2.getId());
		res2.setImage(res1.getImage());
		res2.setId(res1.getId());
		res1.setImage(new Image(getClass().getResource("../images/" + resName + "-logo.jpg").toString()));
		res1.setId(resName);
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
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}

	/**
	 * OnClicked event handler, switching scenes to restaurant selection scene.
	 * 
	 * @param MouseEvent event
	 */
	@FXML
	void restaurantBtnClicked(MouseEvent event) {
		router.returnToCustomerPanel(event);
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
		ServerResponse resUser = ClientGUI.client.getUser();
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
		} else {
			switch (user.getUserType()) {
			case Customer:
				restaurantBtn.setVisible(val);
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
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Scene getScene() {
		return scene;
	}

	/**
	 * Getting the favourite restaurants in the DB.
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void setFavRestaurants() {
		ServerResponse ResFavRestaurants = ClientGUI.client.getFavRestaurants();
		HashMap<String, File> favRestaurants;
		if (ResFavRestaurants == null) {
			ClientGUI.client.favRestaurantsRequest();
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					while (ClientGUI.client.getFavRestaurants() == null) {
						synchronized (ClientGUI.monitor) {
							try {
								ClientGUI.monitor.wait();
							} catch (Exception e) {
								e.printStackTrace();
								return;
							}
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
		}
		favRestaurants = (HashMap<String, File>) ClientGUI.client.getFavRestaurants().getServerResponse();
		Set<String> resSet = favRestaurants.keySet();
		String[] res = new String[resSet.size()];
		int j = 0;
		for (String s : resSet) {
			res[j] = s;
			j++;
		}
		setFavDisplayed(res);
	}

	/**
	 * Setting the queue's for the carousel.
	 * 
	 * @param String[] res
	 */
	private void setFavDisplayed(String[] res) {
		for (int i = 0; i < 3; i++) {
			favListDisplayed.addLast(res[i]);
			favListHidden.addLast(res[i + 3]);
		}
		String resName = favListDisplayed.pollFirst().toLowerCase();
		res1.setImage(new Image(getClass().getResource("../images/" + resName + "-logo.jpg").toString()));
		res1.setId(resName);
		favListDisplayed.addLast(resName);
		resName = favListDisplayed.pollFirst().toLowerCase();
		res2.setImage(new Image(getClass().getResource("../images/" + resName + "-logo.jpg").toString()));
		res2.setId(resName);
		favListDisplayed.addLast(resName);
		resName = favListDisplayed.pollFirst().toLowerCase();
		res3.setImage(new Image(getClass().getResource("../images/" + resName + "-logo.jpg").toString()));
		res3.setId(resName);
		favListDisplayed.addLast(resName);
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
