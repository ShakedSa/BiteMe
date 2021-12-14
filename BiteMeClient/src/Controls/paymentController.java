package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Entities.Customer;
import Entities.W4CCard;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class paymentController implements Initializable {

	private Router router;

	private Stage stage;

	private Scene scene;

	@FXML
	private Rectangle avatar;

	@FXML
	private RadioButton businessRadio;

	@FXML
	private Text homePageBtn;

	@FXML
	private Text itemsCounter;

	@FXML
	private ImageView leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Label nextOrderStep;

	@FXML
	private RadioButton privateRadio;

	@FXML
	private Text profileBtn;

	@FXML
	private Text restaurantsBtn;

	@FXML
	private Label errorMsg;

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	@FXML
	void nextOrderStep(MouseEvent event) {
//		errorMsg.setText("");
//		if (businessRadio.isSelected()) {
//			/** continue with business account payment. */
//		} else {
//			/** continue with private account payment. */
//		}
		if (router.getReviewOrderController() == null) {
			AnchorPane mainContainer;
			reviewOrderController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeReviewOrderPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setItemsCounter();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Review Order");
				stage.setScene(mainScene);
				stage.show();
				controller.displayOrder();
			} catch (IOException ex) {
				ex.printStackTrace();
				return;
			}
		} else {
			router.getReviewOrderController().setItemsCounter();
			stage.setTitle("BiteMe - Review Order");
			stage.setScene(router.getReviewOrderController().getScene());
			stage.show();
			router.getReviewOrderController().displayOrder();
		}
	}

	@FXML
	void openProfile(MouseEvent event) {
		router.showProfile();
	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		router.changeSceneToHomePage();
	}

	@FXML
	void returnToDeliveryMethod(MouseEvent event) {
		router.getDeliveryMethodController().setItemsCounter();
		router.getDeliveryMethodController().setAvatar();
		stage.setTitle("BiteMe - Select Delivery Method");
		stage.setScene(router.getDeliveryMethodController().getScene());
		stage.show();
	}

	@FXML
	void returnToRestaurants(MouseEvent event) {
		router.getRestaurantselectionController().setItemsCounter();
		router.getRestaurantMenuController().setAvatar();
		stage.setTitle("BiteMe - Restaurants");
		stage.setScene(router.getRestaurantselectionController().getScene());
		stage.show();
	}

	@FXML
	void selectBusiness(MouseEvent event) {
		if (privateRadio.isSelected()) {
			privateRadio.setSelected(false);
		}
		businessRadio.setSelected(true);
	}

	@FXML
	void selectPrivate(MouseEvent event) {
		if (businessRadio.isSelected()) {
			businessRadio.setSelected(false);
		}
		privateRadio.setSelected(true);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/**
		 * On load disable for the user the option to select 'business account' if the
		 * user is not an approved business account
		 */
		Customer user = (Customer) ClientGUI.client.getUser().getServerResponse();
		W4CCard w4c = user.getW4c();
		if (w4c.getEmployerID() == null || w4c.getEmployerID().equals("")) {
			businessRadio.setSelected(false);
			businessRadio.setDisable(true);
			privateRadio.setSelected(true);
		}
		router = Router.getInstance();
		router.setPaymentController(this);
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

	public void setItemsCounter() {
		itemsCounter.setText(router.getBagItems().size() + "");
	}

	public void setAvatar() {
		router.setAvatar(avatar);
	}

}
