package Controls;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.Order;
import Entities.ServerResponse;
import Entities.User;
import Enums.UserType;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Natali 
 * This class is creating a simulation of message that send to customer
 */
public class sendMsgToCustomerController implements Initializable {

	public final UserType type = UserType.Supplier;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private Rectangle avatar;

	@FXML
	private Label closeMsgBtn;

	@FXML
	private Text customerNameTxt;

	@FXML
	private Text homePageBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Text phoneNumberTxt;

	@FXML
	private Text plannedTimeTxt;

	@FXML
	private Text profileBtn;

	@FXML
	private Text statusTxt;

	@FXML
	private Text supplierPanelBtn;

	private User user = (User) ClientGUI.client.getUser().getServerResponse();
	private String restaurantName = user.getOrganization();
	private Integer deliveryNumber;
	private Order order;

	/**
	 * This method is setting order info that received from
	 * "supplierUpdateOrderController"
	 * 
	 * @param order
	 * @param deliveryNumber
	 */
	public void setOrderInfo(Order order, int deliveryNumber) {
		this.order = order;
		this.deliveryNumber = deliveryNumber;
		displayOrderInfo();
		getCustomerInfo();
	}

	/**
	 * This method approached to the server, get customer's name and phone number
	 * And then set them into the appropriate variables
	 */
	private void displayOrderInfo() {
		statusTxt.setText("Your order status from " + restaurantName + ": " + order.getStatus());
		if (order.getStatus().equals("Received")) {
			plannedTimeTxt.setText("Order received time: " + order.getOrderRecieved());
		} else // "Ready"
			plannedTimeTxt.setText("Order planned time: " + order.getPlannedTime());
	}

	private ArrayList<String> getCustomerInfo() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				ClientGUI.client.getCustomerInfo(deliveryNumber.toString());
				synchronized (ClientGUI.monitor) {
					try {
						ClientGUI.monitor.wait();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
			}
		});
		t.start();
		try {
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// handle server response
		ServerResponse sr = ClientGUI.client.getLastResponse();
		if (sr == null) {
			return null;
		}
		@SuppressWarnings("unchecked")
		ArrayList<String> response = (ArrayList<String>) sr.getServerResponse();
		if (response == null) {
			return null;
		}

		// set customer's name and phone number
		customerNameTxt.setText("Dear " + response.get(1) + ",");
		phoneNumberTxt.setText("Phone number: " + response.get(0));

		return response;
	}

	/**
	 * Change scene to the previous page
	 */
	@FXML
	void closeMsgBtnClicked(MouseEvent event) {
		router.getUpdateOrderTableController().updateOrderClicked(event);
	}

	/**
	 * Logout and change scene to home page
	 */
	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	/**
	 * Changes scene to profile
	 */
	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}

	/**
	 * Changes scene to home page
	 */
	@FXML
	void returnToHomePage(MouseEvent event) {
		router.changeSceneToHomePage();
	}

	/**
	 * Changes scene to supplier panel
	 */
	@FXML
	void returnToSupplierPanel(MouseEvent event) {
		router.returnToSupplierPanel(event);
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
		router.setSendMsgToCustomerController(this);
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
}
