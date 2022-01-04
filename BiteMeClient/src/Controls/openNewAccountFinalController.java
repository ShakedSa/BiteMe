package Controls;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.ServerResponse;
import Enums.UserType;
import Util.InputValidation;
import client.ClientGUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Eden
 * This class is in charge of the logics and scene setup for opening new account page.
 */
public class openNewAccountFinalController implements Initializable{
	public final UserType type= UserType.BranchManager;
	private Router router;
	private Stage stage;
	private Scene scene,prevScene;
	private String userType;
    @FXML
    private Text EmpName;

    @FXML
    private Text MonthlyBud;

    @FXML
    private TextField employersNameTxtField;

    @FXML
    private Label infoMsg;
    
    @FXML
    private ImageView approvalBtn1;

    @FXML
    private Label approvalBtn;

    @FXML
	private Rectangle leftArrowBtn;

    @FXML
    private Rectangle avatar;

    @FXML
    private Text managerPanelBtn;

    @FXML
    private Text homePageBtn;

    @FXML
    private Text logoutBtn;

    @FXML
    private Text profileBtn;

    @FXML
    private Text wouldLikeToOpen;

    @FXML
    private Text cardNumber;

    @FXML
    private TextField creditCardNumberTxtField;

    @FXML
    private Text m6;

    @FXML
    private Text m7;

    @FXML
    private Text m8;

    @FXML
    private ComboBox<String> accountCombo;

    @FXML
    private TextField monthlyBudTxtField;

    @FXML
    private Text dailyBud;

    @FXML
    private TextField dailyBudTxtField;

    @FXML
    private Text Error;

    @FXML
    private Text shekel;

    @FXML
    private Text shekel1;
    
    @FXML
    private Text srvErrorTxt;

    @FXML
    private Text updateSucess;

    @FXML
    private Text inputErrorTxt;

    @FXML
    private ImageView updateSucess1;
    
	private String fname;
	private String lname;
	private String username;


    @FXML
    void logoutClicked(MouseEvent event) {
    	router.logOut();
    }

    /**
     * this method is activated once the user chose an item from comboBox.
     * it will set up the visibility of the relevant objects on the scene.
     * @param event
     */
    @FXML 
    void openTheFields(ActionEvent event) {
    	if(accountCombo.getValue()==null) return;
    	inputErrorTxt.setVisible(false);
		srvErrorTxt.setVisible(false);
    	switch(accountCombo.getValue()) {
    	case "Private Account":
    		setEmployerDataVisibility(false);
    		setPrivateDataVisibility(true);
    		break;
    	case "Business Account":
    		setEmployerDataVisibility(true);
    		setPrivateDataVisibility(false);
    		break;
    	case "Business & Private Account" : 
    		setEmployerDataVisibility(true);
			setPrivateDataVisibility(true);
			break;
		default:
			break;
    	}
    }
    
    /**
     * changes scene to profile.
     */
    @FXML
    void profileBtnClicked(MouseEvent event) {
    	router.showProfile();
    }

    /**
     * changes scene to home page.
     */
    @FXML
    void returnToHomePage(MouseEvent event) {
    	router.changeSceneToHomePage();
    }
    
    /**
     * changes scene to manager panel.
     */
    @FXML
    void returnToManagerPanel(MouseEvent event) {
    	router.returnToManagerPanel(event);
    }

    /**
     * changes scene to previous page.
     */
    @FXML
    void returnToPreviousScene(MouseEvent event) {
    	// if edit completed go back to manager menu
    	approvalBtn.setDisable(false);
    	router.setOpenNewAccountFinalController(null);
    	router.getOpenNewAccountController().reset();
		router.getOpenNewAccountController().initTable();
    	if(updateSucess.isVisible()) {
    		router.returnToManagerPanel(event);
    	}
    	// if regret go previous screen.
    	else {

    		stage.setTitle("BiteMe - Open New Account");
			stage.setScene(prevScene);
			stage.show();
    	}
    }

    /**
     * this methods sends information to the server in order to update the new customer's info.
     * @param event
     */
    @FXML
    void approvalClicked(MouseEvent event) {
		srvErrorTxt.setVisible(false);
    	ArrayList<String> values = new ArrayList<String>();
    	// if input data is illegal show error message
		if(illegalInput())
		{
			inputErrorTxt.setVisible(true);
			return;
		}
		else
			inputErrorTxt.setVisible(false);
		//insert relevant info for each type to the array list
    	switch(accountCombo.getValue()) {
    	//values = userType,username,monthly bud,daily budget,credit card number,employer's name.
    	case "Private Account":
    		values.add("Private");
    		values.add(username);
    		values.add("");
    		values.add("");
    		values.add(creditCardNumberTxtField.getText());
    		values.add("");
    		break;
    	case "Business Account":
    		values.add("Business");
    		values.add(username);
    		values.add(monthlyBudTxtField.getText());
    		values.add(dailyBudTxtField.getText());
    		values.add("");
    		values.add(employersNameTxtField.getText());
    		break;

    	case "Business & Private Account" : 
    		values.add("Both");
    		values.add(username);
    		values.add(monthlyBudTxtField.getText());
    		values.add(dailyBudTxtField.getText());
    		values.add(creditCardNumberTxtField.getText());
    		values.add(employersNameTxtField.getText());
    		break;
		default:
			return;
    	}
		//values = userType,username,monthly bud,daily budget,credit card number,employer's name.
    	//modify user status, add customer table, add w4c:
    	ClientGUI.getClient().openNewAccount(values);
    	//wait for response:
  		Thread t = new Thread(new Runnable() {
  			@Override
  			public void run() {
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
  		//handle server response
  		ServerResponse sr = ClientGUI.getClient().getLastResponse();
  		if(sr==null) //server error:
  		{
  			updateSucess.setVisible(false);
  			updateSucess1.setVisible(false);
  			srvErrorTxt.setVisible(true);
  			return;
  		}
  		String ans = sr.getDataType();
  		if (ans.equals("Success")) //succes
  		{
  			updateSucess.setVisible(true);
  			updateSucess1.setVisible(true);
  			srvErrorTxt.setVisible(false);
  			approvalBtn.setDisable(true);
  			return; // all good
  		}
  		if(ans.equals("unApprovedEmployer")) {
  			srvErrorTxt.setText("Employer name is not found or lacking permissions.");
  		}
  		 // error happend on server side.
  			updateSucess.setVisible(false);
  			updateSucess1.setVisible(false);
  			srvErrorTxt.setVisible(true);
  		
  		
    }
    
    
    /**
     * @return true if data is illegal in text boxes.
     */
    private boolean illegalInput() {
    	if(accountCombo.getValue()==null)
    	{
    		inputErrorTxt.setText("Please choose account permissions");
			return true;
    	}

    	if(accountCombo.getValue().equals("Private Account") && creditCardNumberTxtField.getText().equals("")) {
			inputErrorTxt.setText("Please enter creditcard number");
			return true;
    	}
    		
    	if(accountCombo.getValue().equals("Business Account") && monthlyBudTxtField.getText().equals("")) {
    		inputErrorTxt.setText("Please enter monthly budget");
    		return true;
    	}
    		
    	if(accountCombo.getValue().equals("Business & Private Account") && (creditCardNumberTxtField.getText().equals("") ||monthlyBudTxtField.getText().equals("") ))
    	{
    		inputErrorTxt.setText("Creditcard and Monthly budget must be filled !");
    		return true;
    	}
    	
    	if(employersNameTxtField.getText().equals("") && accountCombo.getValue().contains("Business")) {
			inputErrorTxt.setText("Please enter employer's name");
			return true;
    	}
    	
    	if(InputValidation.CheckIntegerInput(monthlyBudTxtField.getText()))
    	{
    		inputErrorTxt.setText("Monthly budget must containt numbers only !");
    		return true;
    	}
    	if(InputValidation.CheckIntegerInput(creditCardNumberTxtField.getText()))
    	{
    		inputErrorTxt.setText("Creditcard number must containt numbers only !");
    		return true;
    	}
    	if(InputValidation.CheckIntegerInput(dailyBudTxtField.getText()))
    	{
    		inputErrorTxt.setText("Daily budget must containt numbers only !");
    		return true;
    	}
		return false;
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
		router.setOpenNewAccountFinalController(this);
		router.setArrow(leftArrowBtn, -90);
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
	
	/**
	 * setup the scene values on entry.
	 * @param id
	 * @param username
	 * @param fname
	 * @param lname
	 */
	public void initScene(String id, String username , String fname, String lname ) {
		this.fname=fname;
		this.lname=lname;
		this.username=username;
		approvalBtn.setDisable(false);
		inputErrorTxt.setVisible(false);
		updateSucess.setVisible(false);
		updateSucess1.setVisible(false);
		srvErrorTxt.setVisible(false);
		creditCardNumberTxtField.clear();
		dailyBudTxtField.clear();
		employersNameTxtField.clear();
		monthlyBudTxtField.clear();
		// hide all fields:
		setEmployerDataVisibility(false);
		setPrivateDataVisibility(false);
		//get user type p.customer/b.customer/user with query and set visibility accordingly

		ClientGUI.getClient().checkCustomerStatus(username);
  		Thread t = new Thread(new Runnable() {
  			@Override
  			public void run() {
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
  		//handle server response
  		ServerResponse sr = ClientGUI.getClient().getLastResponse();
  		userType = sr.getDataType();
		//set relevant comboBox values:
		setAccountTypeComboBox(userType);
		infoMsg.setText("You are creating account for " + fname + " " + lname + " - ID: " +
				id + ", Username: " + username);
	}

	public void setPrevScene(Scene prevScene) {
		
		this.prevScene=prevScene;
	}


	/**
	 * sets the visibility of business customer data 
	 * @param val
	 */
	public void setEmployerDataVisibility(boolean val) {
		dailyBudTxtField.setVisible(val);
		employersNameTxtField.setVisible(val);
		monthlyBudTxtField.setVisible(val);
		EmpName.setVisible(val);
		MonthlyBud.setVisible(val);
		dailyBud.setVisible(val);
		shekel.setVisible(val);
		shekel1.setVisible(val);
		m7.setVisible(val);
		m8.setVisible(val);
	}

	/**
	 * sets the visibility of private customer data 
	 * @param val
	 */
	public void setPrivateDataVisibility(boolean val) {
		creditCardNumberTxtField.setVisible(val);
		cardNumber.setVisible(val);
		m6.setVisible(val);
	}
	

 	/**set the values of the account type combo box
 	 * @param accountType
 	 */
 	private void setAccountTypeComboBox(String accountType) {
 		String[] all = {"Private Account", "Business Account", "Business & Private Account"};
 		ArrayList<String> type = new ArrayList<String>();
 		switch(accountType) {
 		case "isPrivate":
 			type.add("Business Account");
 			break;
 		case "isBusiness":
			type.add("Private Account");
			break;
 		case "isUser":
		type.add("Private Account");
		type.add("Business Account");
		type.add("Business & Private Account");
		break;
 		}

 		String[] stringArray = type.toArray(new String[0]);
 		accountCombo.getItems().removeAll(all);
 		accountCombo.getItems().addAll(stringArray);
 	}
	
}
