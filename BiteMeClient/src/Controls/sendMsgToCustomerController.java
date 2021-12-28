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
	
	public void setOrderInfo(Order order, int deliveryNumber) {
		System.out.println("Order: " + order);
		System.out.println("delivery num: " + deliveryNumber);
		this.order = order;
		this.deliveryNumber = deliveryNumber;
		displayOrderInfo();
		getCustomerInfo();
	}

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
		ArrayList<String> response2 = (ArrayList<String>) sr.getServerResponse();
		if (response2 == null) {
			return null;
		}
		customerNameTxt.setText("Dear " + response2.get(1) + ",");
		phoneNumberTxt.setText("Phone number: " + response2.get(0));
		
		return response2;
	}

	@FXML
	void closeMsgBtnClicked(MouseEvent event) {
		router.getUpdateOrderTableController().updateOrderClicked(event);
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
		//displayMsg(res1, res2);
		
		
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
	
	
	
//	Thread t = new Thread(new Runnable() {
//		@Override
//		public void run() {
//			//ClientGUI.client.getOrderInfo(orderNumber);
//			synchronized (ClientGUI.monitor) {
//				try {
//					ClientGUI.monitor.wait();
//				} catch (Exception e) {
//					e.printStackTrace();
//					return;
//				}
//			}
//		}
//	});
//	t.start();
//	ClientGUI.client.getOrderInfo(restaurantName);
//	try {
//		t.join();
//	} catch (Exception e) {
//		e.printStackTrace();
//		return null;
//	}
//	// handle server response
//	ServerResponse sr = ClientGUI.client.getLastResponse();
//	if (sr == null) {
//		return null;
//	}
//	@SuppressWarnings("unchecked")
//	ArrayList<String> response1 = (ArrayList<String>) sr.getServerResponse();
//	if (response1 == null) {
//		return null;
//	}

}
