package Controls;

import java.net.URL;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import Entities.User;
import Enums.UserType;
import client.ClientGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Natali
 * This class allowed supplier view his income receipts
 */
public class viewIncomeReceiptController implements Initializable {
	
	public final UserType type = UserType.Supplier;
	private Router router;
	private Stage stage;
	private Scene scene;

    @FXML
    private Rectangle avatar;

    @FXML
    private Text commissionPercentTxt;

    @FXML
    private Text dearRestaurantTxt;
    
    @FXML
    private Text errorMsg;

    @FXML
    private Text finalAmountNum;

    @FXML
    private Text finalAmountTxt;

    @FXML
    private Text homePageBtn;

    @FXML
    private Rectangle leftArrowBtn;

    @FXML
    private Text logoutBtn;
    
    @FXML
    private Text nis1;

    @FXML
    private Text nis2;

    @FXML
    private Text nis3;
    
    @FXML
    private Line minusLine;

    @FXML
    private ComboBox<Month> monthBox;

    @FXML
    private Text profileBtn;
    
    @FXML
    private Rectangle rect;
    
    @FXML
    private Line sumLine;

    @FXML
    private Label showReceiptBtn;

    @FXML
    private Text supplierPanelBtn;

    @FXML
    private Text totalCommissionNum;

    @FXML
    private Text totalCommissionTxt;

    @FXML
    private Text totalOrderNum;

    @FXML
    private Text totalOrderTxt;

    @FXML
    private ComboBox<String> yearBox;
    
    private ObservableList<String> list;
    private ObservableList<Month> monthList;
	private User user = (User) ClientGUI.getClient().getUser().getServerResponse();
	private String restaurant = user.getOrganization();
	private float commissionPercent, totalOrder, totalCommission, finalAmount;
	
	/**
	 * set values of years inside the comboBox
	 */
	private void setYearComboBox() {
		ArrayList<String> type = new ArrayList<String>();
		yearBox.getItems().addAll(generateYears());
	}
	
	/**
	 * set values of months inside the comboBox
	 */
	private void setMonthComboBox() {
		ArrayList<Month> type = new ArrayList<Month>();
		type.add(Month.JANUARY);
		type.add(Month.FEBRUARY);
		type.add(Month.MARCH);
		type.add(Month.APRIL);
		type.add(Month.MAY);
		type.add(Month.JUNE);
		type.add(Month.JULY);
		type.add(Month.AUGUST);
		type.add(Month.SEPTEMBER);
		type.add(Month.OCTOBER);
		type.add(Month.NOVEMBER);
		type.add(Month.DECEMBER);

		monthList = FXCollections.observableArrayList(type);
		monthBox.setItems(monthList);
	}
    
    @FXML
    void showReceiptClicked(MouseEvent event) {
    	String year = yearBox.getValue().toString();
    	String month = Integer.toString(monthBox.getValue().getValue());
    	ArrayList<String> date = new ArrayList<>();
    	date.add(0, restaurant);
    	date.add(1, month);
    	date.add(2, year);
    	Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				ClientGUI.getClient().getSupplierReceipt(date);
				synchronized (ClientGUI.getMonitor()) {
					try {
						ClientGUI.getMonitor().wait();
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
			return;
		}
		if (!checkServerResponse()) {
			errorMsg.setText("There is no receipt for this date");
			return;
		}
		//arrayList inside the server response with: [total income, commission %]
		@SuppressWarnings("unchecked")
		ArrayList<String> response =  (ArrayList<String>)ClientGUI.getClient().getLastResponse().getServerResponse();
		totalOrder = Float.parseFloat(response.get(0));
		commissionPercent = Float.parseFloat(response.get(1));
		totalCommission = totalOrder*(commissionPercent/100);
		finalAmount = totalOrder - totalCommission;
		displayReceipt();
    }
    
    private void displayReceipt() {
		dearRestaurantTxt.setVisible(true);
		dearRestaurantTxt.setText("Dear " + restaurant + ",");
		commissionPercentTxt.setVisible(true);
		commissionPercentTxt.setText("Your commission percentage is: " + commissionPercent + "%"); 
		//total order amount:
		totalOrderTxt.setVisible(true);
		totalOrderNum.setVisible(true);
		totalOrderNum.setText(totalOrder + ""); 
		nis1.setVisible(true);
		minusLine.setVisible(true);
		//total commission amount:
		totalCommissionTxt.setVisible(true);
		totalCommissionNum.setVisible(true);
		totalCommissionNum.setText(totalCommission + "");
		nis2.setVisible(true);
		sumLine.setVisible(true);
		//final receipt amount:
		finalAmountTxt.setVisible(true);
		finalAmountNum.setVisible(true);
		finalAmountNum.setText(finalAmount + "");
		nis3.setVisible(true);
		rect.setVisible(true);
    }
    
	/**
	 * Checks server response and display relevant information.
	 * return true if the income receipt is exist and false else
	 */
	private boolean checkServerResponse() {
		if (ClientGUI.getClient().getLastResponse() == null) {
			errorMsg.setText("There is no receipt for this date");
			return false;
		}
		switch (ClientGUI.getClient().getLastResponse().getMsg().toLowerCase()) {
		case "success":
			return true;
		default:
			errorMsg.setText("There is no receipt for this date");
			return false;
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
	 * Changes scene to profile
	 */
	@FXML
	void profileBtnClicked(MouseEvent event) {
		clearPage();
		router.showProfile();
	}

	/**
	 * Changes scene to home page
	 */
	@FXML
	void returnToHomePage(MouseEvent event) {
		clearPage();
		router.changeSceneToHomePage();
	}

	/**
	 * Changes scene to supplier panel
	 */
	@FXML
	void returnToSupplierPanel(MouseEvent event) {
		clearPage();
		router.returnToSupplierPanel(event);
	}

	/**
	 * This method clear page, set visibility of buttons and text
	 */
	private void clearPage() {
		errorMsg.setText("");
		dearRestaurantTxt.setVisible(false);
		commissionPercentTxt.setVisible(false);
		//total order amount:
		totalOrderTxt.setVisible(false);
		totalOrderNum.setVisible(false);
		nis1.setVisible(false);
		minusLine.setVisible(false);
		//total commission amount:
		totalCommissionTxt.setVisible(false);
		totalCommissionNum.setVisible(false);
		nis2.setVisible(false);
		sumLine.setVisible(false);
		//final receipt amount:
		finalAmountTxt.setVisible(false);
		finalAmountNum.setVisible(false);
		nis3.setVisible(false);
		rect.setVisible(false);
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
		router.setViewIncomeReceiptController(this);
		setStage(router.getStage());
		router.setArrow(leftArrowBtn, -90);
		setYearComboBox();
		setMonthComboBox();
		clearPage();
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
	 * @return generated String[5] contains 5 last years from today
	 */
	private String[] generateYears() {
		int currentYear=Calendar.getInstance().get(Calendar.YEAR);
		String years_tmp[]= new String[5];
		for(int i=0;i<5;i++)
		{
			years_tmp[i]=Integer.toString(currentYear-4+i);
		}
		return years_tmp;
	}

}
