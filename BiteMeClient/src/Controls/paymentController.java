package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import Entities.Customer;
import Entities.W4CCard;
import Enums.PaymentMethod;
import client.ClientGUI;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
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

	Customer user;

	@FXML
	private Rectangle avatar;

	@FXML
	private RadioButton businessRadio;

	@FXML
	private Text homePageBtn;

	@FXML
	private Text itemsCounter;

	@FXML
	private Rectangle leftArrowBtn;

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
	private RadioButton bothRadio;

	@FXML
	private TextField employerCodeTextField;

	@FXML
	private Text employerCodeTxt;

	@FXML
	private CheckBox refundCheck;

	@FXML
	private Label refundText;

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	@FXML
	public void changeToCart(MouseEvent event) {
		router.changeToMyCart("Payment");
	}

	@FXML
	void nextOrderStep(MouseEvent event) {
		errorMsg.setText("");
		Customer user = (Customer) ClientGUI.client.getUser().getServerResponse();
		W4CCard w4c = user.getW4c();
		/** Checking if the user got refund and selected to use it. */
		if (refundCheck.isSelected()) {
			float refund = user.getRefunds().get(router.getOrder().getRestaurantName());
			/** Checking whether the refund is larger than the order final price. */
			if (refund > router.getOrderDeliveryMethod().getFinalPrice()) {
				user.getRefunds().put(router.getOrder().getRestaurantName(),
						refund - router.getOrderDeliveryMethod().getFinalPrice());
				router.getOrderDeliveryMethod().setFinalPrice(0);
			} else {
				router.getOrderDeliveryMethod().setFinalPrice(router.getOrderDeliveryMethod().getFinalPrice() - refund);
				user.getRefunds().remove(router.getOrder().getRestaurantName());
			}
		}
		if (businessRadio.isSelected() || bothRadio.isSelected()) {
			if (w4c.getBalance() == 0 || w4c.getDailyBalance() == 0) {
				errorMsg.setText("Employer's balance is 0.\nPlease select different type of payment method.");
				return;
			}
			if (!employerCodeTextField.getText().equals(w4c.getEmployerID())) {
				errorMsg.setText("Incorrect employer's code.");
				return;
			}
			if (bothRadio.isSelected()) {
				router.getOrder().setPaymentMethod(PaymentMethod.Both);
			} else {
				router.getOrder().setPaymentMethod(PaymentMethod.BusinessCode);
			}
		} else {
			router.getOrder().setPaymentMethod(PaymentMethod.CreditCard);
		}

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
		businessRadio.requestFocus();
		businessRadio.setFocusTraversable(true);
		businessRadio.setSelected(true);
		/** Check w4c card */
		W4CCard w4cCard = user.getW4c();
		if (w4cCard.getDailyBudget() == 0) {
			if (w4cCard.getBalance() < router.getOrderDeliveryMethod().getFinalPrice()) {
				if (user.isPrivate()) {
					bothRadio.setVisible(true);
					bothRadio.setSelected(true);
					bothRadio.requestFocus();
					businessRadio.setSelected(false);
					errorMsg.setText(
							"W4C Card budget is lower than order price.\nWould you like to pay with the business card & private credit card?\nFor convenient 'both' option is automatically selected.");
					showTextField(true);
				} else {
					businessRadio.setSelected(false);
					businessRadio.setDisable(true);
					errorMsg.setText(
							"W4C card budget is lower than order price.\nYour account is not connected with private account, please ask the branch manager to accept your account as private before ordering.");
					nextOrderStep.setDisable(true);
				}
			}
		}else {
			if(w4cCard.getDailyBalance() < router.getOrderDeliveryMethod().getFinalPrice()) {
				if (user.isPrivate()) {
					bothRadio.setVisible(true);
					bothRadio.setSelected(true);
					bothRadio.requestFocus();
					businessRadio.setSelected(false);
					showTextField(true);
					errorMsg.setText(
							"W4C Card budget is lower than order price.\nWould you like to pay with the business card & private credit card?\nFor convenient 'both' option is automatically selected.");
				} else {
					businessRadio.setSelected(false);
					businessRadio.setDisable(true);
					nextOrderStep.setDisable(true);
					errorMsg.setText(
							"W4C card budget is lower than order price.\nYour account is not connected with private account, please ask the branch manager to accept your account as private.");
				}
			}
		}
	}
	
	@FXML
	void selectPrivate(MouseEvent event) {
		if (businessRadio.isSelected() || bothRadio.isSelected()) {
			businessRadio.setSelected(false);
			bothRadio.setSelected(false);
			bothRadio.setVisible(false);
			errorMsg.setText("");
		}
		privateRadio.setSelected(true);
		privateRadio.requestFocus();
		showTextField(false);
	}

	private void showTextField(boolean val) {
		employerCodeTextField.setVisible(val);
		employerCodeTxt.setVisible(val);
	}

	@FXML
	void bothSelected(MouseEvent event) {
		bothRadio.setSelected(true);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setPaymentController(this);
		router.setArrow(leftArrowBtn, -90);
		setStage(router.getStage());
		/**
		 * On load disable for the user the option to select 'business account' if the
		 * user is not an approved business account
		 */
		user = (Customer) ClientGUI.client.getUser().getServerResponse();
		if ((user.isBusiness() && !user.isApproved()) || !user.isBusiness()) {
			selectPrivate(null);
			businessRadio.setDisable(true);
			privateRadio.setFocusTraversable(true);
		} else {
			if (!user.isPrivate()) {
				selectBusiness(null);
				privateRadio.setDisable(true);
				businessRadio.setFocusTraversable(true);
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void checkRefunds() {
		Customer user = (Customer) ClientGUI.client.getUser().getServerResponse();
		Thread t = new Thread(() -> {
			synchronized (ClientGUI.monitor) {
				ClientGUI.client.getRefunds(user);
				try {
					ClientGUI.monitor.wait();
				} catch (Exception ex) {
					ex.printStackTrace();
					return;
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
		user.setRefunds((HashMap<String, Float>) ClientGUI.client.getLastResponse().getServerResponse());
		if (user.getRefunds().containsKey(router.getOrder().getRestaurantName())) {
			refundCheck.setVisible(true);
			refundText.setText("You got " + user.getRefunds().get(router.getOrder().getRestaurantName())
					+ "\u20AA refund for this restaurant.\nCheck the check box if you would like to use it.");
		}
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
