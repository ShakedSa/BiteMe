package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import Entities.Customer;
import Entities.W4CCard;
import Enums.PaymentMethod;
import client.ClientGUI;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Controller for the view of the payment method selection page.
 * 
 * @author Shaked
 */
public class PaymentController implements Initializable {

	private Router router;

	private Stage stage;

	private Scene scene;

	Customer user;
	
	private float refundForBreakDown = 0;

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
	private TextArea refundAmount;

	/**
	 * Method to log out the user.
	 * 
	 * @param event
	 */
	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	/**
	 * Method to change scene to My Cart.
	 * 
	 * @param event
	 */
	@FXML
	public void changeToCart(MouseEvent event) {
		router.changeToMyCart("Payment");
	}

	/**
	 * Method to switch scene to the next step in the order process.
	 * 
	 * @param event
	 */
	@FXML
	void nextOrderStep(MouseEvent event) {
		errorMsg.setText("");
		W4CCard w4c = user.getW4c();
		if (businessRadio.isSelected() || bothRadio.isSelected()) {
			if (w4c.getBalance() == 0 && w4c.getDailyBalance() == 0) {
				if (user.isPrivate()) {
					errorMsg.setText("Employer's balance is 0.\nPlease select a different type of payment method.");
					return;
				} else {
					errorMsg.setText(
							"Employer's balance is 0 and your account is not accepted as private.\nPlease reach to the branch manager for resolving this issue before ordering.");
					nextOrderStep.setDisable(true);
				}
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
		router.getOrderDeliveryMethod().setFinalPrice(checkRefundSelected());
		router.getOrderDeliveryMethod().setRefundForBreakDown(refundForBreakDown);
		router.getOrderDeliveryMethod().setRefundSelected(refundCheck.isSelected());
		if (router.getReviewOrderController() == null) {
			AnchorPane mainContainer;
			ReviewOrderController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/bitemeReviewOrderPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setItemsCounter();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
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
			router.getOrderDeliveryMethod().setRefundForBreakDown(refundForBreakDown);
			router.getOrderDeliveryMethod().setRefundSelected(refundCheck.isSelected());
			stage.show();
			router.getReviewOrderController().displayOrder();
		}
	}

	/**
	 * Method to display the refund information(amount).
	 * 
	 * @param event
	 */
	@FXML
	void displayRefundAmount(MouseEvent event) {
		refundAmount.setVisible(true);
	}

	/**
	 * Method to hide the refund information.
	 * 
	 * @param event
	 */
	@FXML
	void hideRefundAmount(MouseEvent event) {
		refundAmount.setVisible(false);
	}

	/**
	 * On click event handler for the refund check box.<br>
	 * Calculating the price of the order depends on the refund selection and
	 * changing the view state based on the payment method selected by the user.
	 * 
	 * @param event
	 */
	@FXML
	void checkBalanceAfterRefund(ActionEvent event) {
		if (refundCheck.isSelected()) {
			final FloatProperty finalPrice = new SimpleFloatProperty(checkRefundSelected());
			if (bothRadio.isSelected()) {
				W4CCard w4c = user.getW4c();
				if (w4c.getDailyBalance() == 0) {
					checkW4CBudgetAndBalance(w4c, finalPrice.get(), true);
				} else {
					checkW4CBudgetAndBalance(w4c, finalPrice.get(), false);
				}
			}
		} else {
			if (privateRadio.isSelected()) {
				selectPrivate(null);
			} else {
				selectBusiness(null);
			}
		}
	}

	/**
	 * Switch to profile scene.
	 * 
	 * @param event
	 */
	@FXML
	void openProfile(MouseEvent event) {
		router.showProfile();
	}

	/**
	 * Switch to home page scene.
	 * 
	 * @param event
	 */
	@FXML
	void returnToHomePage(MouseEvent event) {
		router.changeSceneToHomePage();
	}

	/**
	 * Switch to the previous step in the order process.
	 * 
	 * @param event
	 */
	@FXML
	void returnToDeliveryMethod(MouseEvent event) {
		router.getDeliveryMethodController().setItemsCounter();
		router.getDeliveryMethodController().setAvatar();
		stage.setTitle("BiteMe - Select Delivery Method");
		stage.setScene(router.getDeliveryMethodController().getScene());
		stage.show();
	}

	/**
	 * Switch to restaurants page.
	 * 
	 * @param event
	 */
	@FXML
	void returnToRestaurants(MouseEvent event) {
		router.getRestaurantselectionController().setItemsCounter();
		router.getRestaurantMenuController().setAvatar();
		stage.setTitle("BiteMe - Restaurants");
		stage.setScene(router.getRestaurantselectionController().getScene());
		stage.show();
	}

	/**
	 * Checks if the user selected to use his refund.<br>
	 * And calculate the final price based on the user's refund amount..
	 */
	private float checkRefundSelected() {
		final FloatProperty finalPrice = new SimpleFloatProperty(router.getOrderDeliveryMethod().getFinalPrice());
		if (refundCheck.isSelected()) {
			final FloatProperty refundAmount = new SimpleFloatProperty(
					user.getRefunds().get(router.getOrder().getRestaurantName()));
			if (refundAmount.get() >= finalPrice.get()) {
				refundForBreakDown = finalPrice.get();
				finalPrice.set(0);
			} else {
				refundForBreakDown = finalPrice.get() - refundAmount.get();
				finalPrice.set(finalPrice.get() - refundAmount.get());
			}
		}
		return finalPrice.get();
	}

	/**
	 * FXML method for selecting the business radio button.
	 */
	@FXML
	void selectBusiness(MouseEvent event) {
		final FloatProperty finalPrice = new SimpleFloatProperty(checkRefundSelected());
		if (privateRadio.isSelected()) {
			privateRadio.setSelected(false);
		}
		businessRadio.requestFocus();
		businessRadio.setFocusTraversable(true);
		businessRadio.setSelected(true);
		showTextField(true);
		if (finalPrice.get() == 0) {
			return;
		}
		/** Check w4c card */
		W4CCard w4cCard = user.getW4c();
		if (w4cCard.getDailyBudget() == 0) {
			checkW4CBudgetAndBalance(w4cCard, finalPrice.get(), true);
		} else {
			checkW4CBudgetAndBalance(w4cCard, finalPrice.get(), false);
		}
	}

	/**
	 * Method to check if the user's balance is good for the order's price. The
	 * method checks for daily balance and monthly balance, depend on the account's
	 * preferences(set in advanced).
	 * 
	 * @param w4c
	 * @param finalPrice
	 * @param val
	 */
	private void checkW4CBudgetAndBalance(W4CCard w4c, float finalPrice, boolean val) {
		float balance = val ? w4c.getBalance() : w4c.getDailyBalance();
		if (balance < finalPrice) {
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
						"W4C card budget is lower than order price.\nYour account is not connected with private account.\nPlease ask the branch manager to accept your account as private before ordering.");
				nextOrderStep.setDisable(true);
			}
		} else {
			bothRadio.setVisible(false);
			businessRadio.setSelected(true);
			businessRadio.requestFocus();
			errorMsg.setText("");
			showTextField(true);
		}
	}

	/**
	 * FXML method for selecting the private radio button.
	 */
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
		refundForBreakDown = checkRefundSelected();
		showTextField(false);
	}

	/**
	 * Display or hide the employer's fields(Controlling the state of the user's
	 * view).
	 * 
	 * @param val
	 */
	private void showTextField(boolean val) {
		employerCodeTextField.setVisible(val);
		employerCodeTxt.setVisible(val);
	}

	/**
	 * FXML method for selecting the business radio button.
	 */
	@FXML
	void bothSelected(MouseEvent event) {
		bothRadio.setSelected(true);
	}

	/**
	 * Initialize method. A required method from the Initializable interface.
	 * 
	 * @param location
	 * @param resources
	 */
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
		user = (Customer) ClientGUI.getClient().getUser().getServerResponse();
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

	/**
	 * Checking if the user got available refunds for this restaurant. <br>
	 * Changing the view of the page accordingly.
	 */
	@SuppressWarnings("unchecked")
	public void checkRefunds() {
		Customer user = (Customer) ClientGUI.getClient().getUser().getServerResponse();
		Thread t = new Thread(() -> {
			synchronized (ClientGUI.getMonitor()) {
				ClientGUI.getClient().getRefunds(user);
				try {
					ClientGUI.getMonitor().wait();
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
		user.setRefunds((HashMap<String, Float>) ClientGUI.getClient().getLastResponse().getServerResponse());
		if (user.getRefunds().containsKey(router.getOrder().getRestaurantName())) {
			refundCheck.setVisible(true);
			refundAmount.setText("You got " + user.getRefunds().get(router.getOrder().getRestaurantName())
					+ "\u20AA refund for this restaurant.");
		}
	}

	/**
	 * Passing scene's reference.
	 * 
	 * @param scene
	 */
	public void setScene(Scene scene) {
		this.scene = scene;
	}

	/**
	 * Getting this controller's scene.
	 * 
	 * @return Scene
	 */
	public Scene getScene() {
		return scene;
	}

	/**
	 * Setting the stage instance.
	 * 
	 * @param Stage stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Setting the item counter of the order.
	 */
	public void setItemsCounter() {
		itemsCounter.setText(router.getBagItems().size() + "");
	}

	/**
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		router.setAvatar(avatar);
	}

}
