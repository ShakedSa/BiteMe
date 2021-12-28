package Controls;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Entities.NewAccountUser;
import Entities.NewAccountUser;
import Entities.ServerResponse;
import Entities.User;
import Enums.BranchName;
import Enums.Status;
import Enums.UserType;
import Util.InputValidation;
import client.ClientGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class openNewAccountController implements Initializable{

	public final UserType type= UserType.BranchManager;
	private Router router;
	private Stage stage;
	private Scene scene;

   

	  @FXML
	    private ImageView approvalBtn1;

	    @FXML
	    private Label next;

	    @FXML
	    private ImageView leftArrowBtn;

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
	    private Text msg;

	    @FXML
	    private Text instructions;

	    @FXML
	    private TableView<NewAccountUser> approvalTable;

	    @FXML
	    private TableColumn<NewAccountUser, String> UserName;

	    @FXML
	    private TableColumn<NewAccountUser, String> FirstName;

	    @FXML
	    private TableColumn<NewAccountUser, String> LastName;
	    
	    @FXML
	    private TableColumn<NewAccountUser, String> ID;

	    @FXML
	    private TableColumn<NewAccountUser, String> Email;

	    @FXML
	    private TableColumn<NewAccountUser, String> Phone;
	    
	    private String id = "";
	    
	    private String fName = "";
	    
	    private String lName = "";

    private ObservableList<String> list;
    
    private ObservableList<String> list1;
    
    
    
    
  //show table with employers that are waiting for approval
  	public void initTable(){
  		ClientGUI.client.searchForNewUsers();
  		//wait for response
  		Thread t = new Thread(new Runnable() {
  			@Override
  			public void run() {
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
  			return;
  		}
  		//handle server response
  		ServerResponse sr = ClientGUI.client.getLastResponse();
  		@SuppressWarnings("unchecked")
  		//get the server response- Business employers that needs approval
  		ArrayList<NewAccountUser> response = (ArrayList<NewAccountUser>) sr.getServerResponse();
  		//check if business customers are waiting for approval
  		if(response.size() == 0)
  		{
  			msg.setText("Currently no accounts are needed");
  			msg.setVisible(true);
  			next.setVisible(false);
  			next.setVisible(false);
  			return;
  		}
  		setTable( response);
  			msg.setText("Some employers are waiting for your approval");
  			msg.setVisible(true);
  			instructions.setVisible(true);		
  	}
  	
  	
  	//set table columns and values
  	private void setTable(ArrayList<NewAccountUser> list) {
  		UserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
  		FirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
  		LastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
  		ID.setCellValueFactory(new PropertyValueFactory<>("id"));
  		Email.setCellValueFactory(new PropertyValueFactory<>("email"));
  		Phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
  		approvalTable.setItems(getCustomer(list));
  		approvalTable.setEditable(true);
  	}
  	
  	
	//change arrayList to ObservableList
	private ObservableList<NewAccountUser> getCustomer(ArrayList<NewAccountUser> list2) {
		ObservableList<NewAccountUser> users = FXCollections.observableArrayList();
		for (NewAccountUser customer : list2) {
			NewAccountUser customerPlusBudget = new NewAccountUser(customer.getUserName(),
					customer.getFirstName(), customer.getLastName(), customer.getId(),
					customer.getEmail(), customer.getPhone());
			users.add(customerPlusBudget);
		}
		return users;
	}
  	
  	
      @FXML
      void copyTableData(MouseEvent event) {
      	if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
              id = (approvalTable.getSelectionModel().getSelectedItem().getId());
              fName = (approvalTable.getSelectionModel().getSelectedItem().getId());
              lName = (approvalTable.getSelectionModel().getSelectedItem().getId());
      	}
      }
      
      @FXML
      void approvalClicked(MouseEvent event) {
    	  if(id.equals("")) {
  			msg.setVisible(true);
  			msg.setText("Please double click on a user!");
  			return;
  			}
    	  if (router.getOpenNewAccountFinalController() == null) {
  			AnchorPane mainContainer;
  			openNewAccountFinalController controller;
  			try {
  				FXMLLoader loader = new FXMLLoader();
  				loader.setLocation(getClass().getResource("../gui/bitemeOpenNewAccountFinalPage.fxml"));
  				mainContainer = loader.load();
  				controller = loader.getController();
  				controller.setAvatar();
  				Scene mainScene = new Scene(mainContainer);
  				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
  				controller.setScene(mainScene);
  				stage.setTitle("BiteMe - Open New Account");
  				stage.setScene(mainScene);
  				stage.show();
  			} catch (IOException e) {
  				e.printStackTrace();
  				return;
  			}
  		} else {
  			stage.setTitle("BiteMe - Open New Account");
  			stage.setScene(router.getOpenNewAccountFinalController().getScene());
  			stage.show();
  		}
    	  
      }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
// 	/**set the values of the account type combo box
// 	 * @param accountType
// 	 */
// 	private void setAccountTypeComboBox(String accountType) {
// 		ArrayList<String> type = new ArrayList<String>();
// 		switch(accountType) {
// 		case "private":
// 			type.add("Business Account");
// 			break;
// 		case "business":
//			type.add("Private Account");
//			break;
// 		case "noAccount":
//		type.add("Private Account");
//		type.add("Business Account");
//		type.add("Business & Private Account");
//		break;
// 		}
// 		list = FXCollections.observableArrayList(type);
// 		accountCombo.setItems(list);
// 	}
// 	
// 	
// 	
// 		/**set the values of prefix phone numbers
// 		 * 
// 		 */
// 		private void setPhoneNumberComboBox() {
// 			ArrayList<String> type = new ArrayList<String>();
// 			type.add("050");
// 			type.add("052");
// 			type.add("053");
// 			type.add("054");
// 			type.add("057");
// 			type.add("058");
// 			list1 = FXCollections.observableArrayList(type);
// 			prefixPhoneNumberBox.setItems(list1);
// 		}
// 		
// 		//search for the given username in the database
// 		//checks that this user is'nt alreay defind
// 		@FXML
// 	    void searchClicked(MouseEvent event) {
// 			if(!checkValues()) {
// 	    		return;
// 	    	}
// 			ClientGUI.client.checkUserNameAccountType(userNameTxtField.getText());
// 			//wait for response
// 			Thread t = new Thread(new Runnable() {
// 				@Override
// 				public void run() {
// 					synchronized (ClientGUI.monitor) {
// 						try {
// 							ClientGUI.monitor.wait();
// 						} catch (Exception e) {
// 							e.printStackTrace();
// 							return;
// 						}
// 					}
// 				}
// 			});
// 			t.start();
// 			try {
// 				t.join();
// 			} catch (Exception e) {
// 				e.printStackTrace();
// 				return;
// 			}
// 			//handle server response
// 			ServerResponse sr = ClientGUI.client.getLastResponse();
// 			@SuppressWarnings("unchecked")
// 			//get the server response- Business employers that needs approval
// 			ArrayList<String> response = (ArrayList<String>) sr.getServerResponse();
// 			//check if UserName is in the database
// 			if(sr.getMsg().equals("Error"))
// 			{
// 				userNameError.setText("Unable to locate UserName");
// 				userNameError.setVisible(true);
// 				enableChooseAccount(false);
// 				return;
// 			}
// 			//check if UserName already has a private and business accounts 
// 			else if(sr.getMsg().equals("has both")){
// 				userNameError.setText("This user already has both accounts");
// 				userNameError.setVisible(true);
// 				enableChooseAccount(false);
// 				return;
// 			}
// 			//check if the User has only a private account 
// 			else if(sr.getMsg().equals("is private")){
// 				userNameError.setText("This user has only a private account");
// 				userNameError.setVisible(true);
// 				setAccountTypeComboBox("private");
// 				enableChooseAccount(true);
// 				return;
// 			}
// 			//check if the User has only a business account 
// 			else if(sr.getMsg().equals("is private")){
// 				userNameError.setText("This user has only a business account");
// 				userNameError.setVisible(true);
// 				setAccountTypeComboBox("business");
// 				enableChooseAccount(true);
// 				return;
// 			}
// 			else if(sr.getMsg().equals("not client")){
// 				userNameError.setText("This user is not a client!");
// 				userNameError.setVisible(true);
// 				enableChooseAccount(false);
// 				return;
// 			}
// 			//possible to open for user both private and business accounts
// 			enableChooseAccount(true);
// 			userNameError.setText("This user does'nt have any accounts");
// 			setAccountTypeComboBox("noAccount");
// 			userNameError.setVisible(true);
// 			validUserName = userNameTxtField.getText();
// 	    }
// 		
// 		
// 		/**check that the user name field is filled correctly
// 		 * @return
// 		 */
// 		private boolean checkValues() {
// 	    	if( InputValidation.checkSpecialCharacters(userNameTxtField.getText())) {
// 	    		userNameError.setVisible(true);
// 	    		userNameError.setText("No special characters!");
// 	    		return false;
// 	    	}
// 	    	if(userNameTxtField.getText().length() == 0) {
// 	    		userNameError.setVisible(true);
// 	    		userNameError.setText("UserName must be filled!");
// 	    		return false;
// 	    	}
// 	    	return true;
// 	    }
// 		
// 		
// 		/**hides or shows the "choose account mechanic"
// 		 * @param val
// 		 */
// 		public void enableChooseAccount(boolean val) {
// 			wouldLikeToOpen.setVisible(val);
// 			accountCombo.setVisible(val);
// 		}
// 		
// 	
// 		/**hides or shows componets for private and business account
// 		 * @param val
// 		 */
// 		public void showForBothAccounts(boolean val) {
// 			approvalBtn.setVisible(val);
// 			instructions.setVisible(val);
// 			firstName.setVisible(val);
// 			firstNameTxtField.setVisible(val);
// 			lastName.setVisible(val);
// 			lastNameTxtField.setVisible(val);
// 			id.setVisible(val);
// 			idTxtField.setVisible(val);
// 			phoneNumber.setVisible(val);
// 			prefixPhoneNumberBox.setVisible(val);
// 			PhoneNumberTxtField.setVisible(val);
// 			email.setVisible(val);
// 			emailTxtField.setVisible(val);
// 			cardNumber.setVisible(val);
// 			creditCardNumberTxtField.setVisible(val);
// 			EmpDetails.setVisible(val);
// 			EmpName.setVisible(val);
// 			employersNameTxtField.setVisible(val);
// 			employersNameTxtField.setVisible(val);
// 			MonthlyBud.setVisible(val);
// 			monthlyBudTxtField.setVisible(val);
// 			shekel.setVisible(val);
// 			shekel1.setVisible(val);
// 			dailyBud.setVisible(val);
// 			dailyBudTxtField.setVisible(val);
// 			m1.setVisible(val);m2.setVisible(val);
// 			m3.setVisible(val);m4.setVisible(val);
// 			m5.setVisible(val);m6.setVisible(val);
// 			m7.setVisible(val);m8.setVisible(val);
// 			if(val == false) {
// 				updateSucess.setVisible(val);
// 		    	updateSucess1.setVisible(val);
// 			}
// 		}
// 		
// 		
// 		/**hides or shows componets for private account
// 		 * @param val
// 		 */
// 		public void showForPrivateAccount(boolean val) {
// 			approvalBtn.setVisible(val);
// 			instructions.setVisible(val);
// 			firstName.setVisible(val);
// 			firstNameTxtField.setVisible(val);
// 			lastName.setVisible(val);
// 			lastNameTxtField.setVisible(val);
// 			id.setVisible(val);
// 			idTxtField.setVisible(val);
// 			phoneNumber.setVisible(val);
// 			prefixPhoneNumberBox.setVisible(val);
// 			PhoneNumberTxtField.setVisible(val);
// 			email.setVisible(val);
// 			emailTxtField.setVisible(val);
// 			cardNumber.setVisible(val);
// 			creditCardNumberTxtField.setVisible(val);
// 			m1.setVisible(val);m2.setVisible(val);
// 			m3.setVisible(val);m4.setVisible(val);
// 			m5.setVisible(val);m6.setVisible(val);
// 			if(val == false) {
// 				updateSucess.setVisible(val);
// 		    	updateSucess1.setVisible(val);
// 			}
// 		}
// 		
// 		
// 		/**hides or shows componets for business account
// 		 * @param val
// 		 */
// 		public void showForBusinessAccount(boolean val) {
// 			approvalBtn.setVisible(val);
// 			instructions.setVisible(val);
// 			firstName.setVisible(val);
// 			firstNameTxtField.setVisible(val);
// 			lastName.setVisible(val);
// 			lastNameTxtField.setVisible(val);
// 			id.setVisible(val);
// 			idTxtField.setVisible(val);
// 			phoneNumber.setVisible(val);
// 			prefixPhoneNumberBox.setVisible(val);
// 			PhoneNumberTxtField.setVisible(val);
// 			email.setVisible(val);
// 			emailTxtField.setVisible(val);
// 			EmpDetails.setVisible(val);
// 			EmpName.setVisible(val);
// 			employersNameTxtField.setVisible(val);
// 			employersNameTxtField.setVisible(val);
// 			MonthlyBud.setVisible(val);
// 			monthlyBudTxtField.setVisible(val);
// 			shekel.setVisible(val);
// 			shekel1.setVisible(val);
// 			dailyBud.setVisible(val);
// 			dailyBudTxtField.setVisible(val);
// 			m1.setVisible(val);m2.setVisible(val);
// 			m3.setVisible(val);m4.setVisible(val);
// 			m5.setVisible(val);m7.setVisible(val);
// 			m8.setVisible(val);
// 			if(val == false) {
// 				updateSucess.setVisible(val);
// 		    	updateSucess1.setVisible(val);
// 			}
// 		}
// 		
// 		
//    @FXML
//    void approvalClicked(MouseEvent event) {
//    	if(!checkBasicUserInput())
//    		return;
//    	else if(accountCombo.getValue().equals("Business & Private Account"))
//    		if(!checkUserInputForPrivate() || !checkUserInputForBusiness())
//    			return;
//    	else if(accountCombo.getValue().equals("Private Account"))
//    		if(!checkUserInputForPrivate())
//    			return;
//    	else if(accountCombo.getValue().equals("Business Account"))
//    		if(!checkUserInputForBusiness())
//    			return;
//    	
//    	
//    	
//    	
//    }
//
//    
//    /**shows the fileds for the user to manager to enter client info
//     * @param event
//     */
//    @FXML
//    void openTheFilelds(ActionEvent event) {
//    	
//    	if(accountCombo.getValue().equals("Business & Private Account")) {
//    		showForBusinessAccount(false);
//    		showForPrivateAccount(false);
//    		showForBothAccounts(true);
//    	}
//    	else if(accountCombo.getValue().equals("Business Account")) {
//    		showForPrivateAccount(false);
//    		showForBothAccounts(false);
//    		showForBusinessAccount(true);
//    	}
//    	else if(accountCombo.getValue().equals("Private Account")) {
//    		showForBusinessAccount(false);
//    		showForBothAccounts(false);
//    		showForPrivateAccount(true);
//    	}	
//    }
//    
//    
//    /**make sure the user input is filled correctly for mutual fields
//     * @return
//     */
//    private boolean checkBasicUserInput() {
//    	//regular experrasion for email validation
//    	String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
//    	if(firstNameTxtField.getText().length() == 0) {
//    		Error.setText("Please fill the first name");
//    		Error.setVisible(true);
//    		return false;
//    	}
//    	if(lastNameTxtField.getText().length() == 0) {
//    		Error.setText("Please fill the last name");
//    		Error.setVisible(true);
//    		return false;
//    	}
//    	if(idTxtField.getText().length() == 0) {
//    		Error.setText("Please fill the client id");
//    		Error.setVisible(true);
//    		return false;
//    	}
//    	if (! (idTxtField.getText().matches("[0-9]+") && idTxtField.getText().length() == 10) ) {
//    		Error.setText("id must contain exactly 10 digits");
//    		Error.setVisible(true);
//    		return false;
//    	}
//    	if(prefixPhoneNumberBox.getSelectionModel().isEmpty()) {
//    		Error.setText("please choose a prefix for the phone number");
//    		Error.setVisible(true);
//    		return false;
//    	}
//    	if (! (PhoneNumberTxtField.getText().matches("[0-9]+") && PhoneNumberTxtField.getText().length() == 7) ) {
//    		Error.setText("phone must conain exactly 10 digits");
//    		Error.setVisible(true);
//    		return false;
//    	}
//    	if(emailTxtField.getText().length() == 0) {
//    		Error.setText("please fill the Email field");
//    		Error.setVisible(true);
//    		return false;
//    	}
//    	if(!emailTxtField.getText().matches(regex)) {
//    		Error.setText("email format is not valid");
//    		Error.setVisible(true);
//    		return false;
//    	}
//    	Error.setText("");
//    	return true;
//	}
//    
//    
//    /**make sure the credit card is filled correctly
//     * @return
//     */
//    private boolean checkUserInputForPrivate() {
//    	if(creditCardNumberTxtField.getText().length() == 0) {
//    		Error.setText("please fill the user credit card number");
//    		Error.setVisible(true);
//    		return false;
//    	}
//    	if (! (creditCardNumberTxtField.getText().matches("[0-9]+")
//    			          && creditCardNumberTxtField.getText().length() == 16) ) {
//    		Error.setText("credit card must contain exactly 10 digits");
//    		Error.setVisible(true);
//    		return false;
//    		}
//    	Error.setText("");
//    	return true;
//    	}
//    
//    /**make sure the enployer info fields are filled correctly
//     * @return
//     */
//    private boolean checkUserInputForBusiness() {
//    	if(employersNameTxtField.getText().length() == 0) {
//    		Error.setText("please fill the employer name");
//    		Error.setVisible(true);
//    		return false;
//    	}
//    	if(monthlyBudTxtField.getText().length() == 0) {
//    		Error.setText("please fill the monthly budget");
//    		Error.setVisible(true);
//    		return false;
//    	}
//    	if (! (monthlyBudTxtField.getText().matches("[0-9]+")) ) {
//    		Error.setText("monthly budget must contain only digits");
//    		Error.setVisible(true);
//    		return false;
//    		}
//    	if(dailyBudTxtField.getText().length() == 0) {
//    		Error.setText("please fill the daily budget");
//    		Error.setVisible(true);
//    		return false;
//    	}
//    	if (! (dailyBudTxtField.getText().matches("[0-9]+")) ) {
//    		Error.setText("daily budget must contain only digits");
//    		Error.setVisible(true);
//    		return false;
//    		}
//    	Error.setText("");
//    	return true;
//    	}
    
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
    void returnToManagerPanel(MouseEvent event) {
    	router.returnToManagerPanel(event);
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
		router.setOpenNewAccountController(this);
		setStage(router.getStage());
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

}

