package Controls;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.ImportedUser;
import Entities.MyFile;
import Entities.NewSupplier;
import Entities.ServerResponse;
import Enums.BranchName;
import Enums.UserType;
import Util.InputValidation;
import client.ClientGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class addNewSupplierController implements Initializable {

	public final UserType type = UserType.BranchManager;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
    private Button searchBtn;
	
	@FXML
	private Label addSupplierBtn;

	@FXML
	private Rectangle avatar;
	
	@FXML
    private Text InvalidMsg;
	
	@FXML
	private Text restaurantName;

    @FXML
	private Text restaurantAddress;

	@FXML
	private Text restaurantType;

	@FXML
	private Text monthlyCommission;

    @FXML
	private Text homePageBtn;

	@FXML
	private Text uploadImageTxt;

    @FXML
    private Text userNameError;
    
    @FXML
    private Text Error;
    
	@FXML
	private ImageView leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Text managerPanelBtn;

	@FXML
	private Text updateSucess;

	@FXML
	private ImageView updateSucess1;
	    
	@FXML
	private ComboBox<String> monthlyCommissionBox;

	@FXML
	private Text profileBtn;
	
	@FXML
	private Text UploadMsgTxt1;
	
	@FXML
	private TextField userNameTxtField;

	@FXML
	private TextField restaurantAddressTxtField;

	@FXML
	private TextField restaurantManagerNameTxtField;

	@FXML
	private TextField restaurantNameTxtField;
	
	@FXML
	private Text importImage1;

	@FXML
	private ImageView importImage;

	@FXML
	private ImageView uploadImage;
	
    @FXML
    private Text UploadMsgTxt;
    
    @FXML
    private ComboBox<String> restaurantTypeCombo;
	
	private String validUserName;
	
	private String validResttuarantName;
	
	private ImportedUser personInfo; // will hold the the person information from the db
	
	private File imgToUpload;
	
	private ArrayList<String> info;
	
	private String branchName;
	
	private ObservableList<String> list;
	

	// creating list of Commission
	private void setMonthlyCommissionComboBox() {
		ArrayList<String> type = new ArrayList<String>();
		type.add("7%");
		type.add("8%");
		type.add("9%");
		type.add("10%");
		type.add("11%");
		type.add("12%");
		list = FXCollections.observableArrayList(type);
		monthlyCommissionBox.setItems(list);
	}
	
	// creating list of Commission
		private void setRestaurantTypeComboBox() {
			ArrayList<String> type = new ArrayList<String>();
			type.add("Italian");
			type.add("Fastfood");
			type.add("Asian");
			type.add("Other");
			list = FXCollections.observableArrayList(type);
			restaurantTypeCombo.setItems(list);
		}
	
	//search for the given username in the database
	//checks that this user is'nt alreay defind
	@FXML
    void searchClicked(MouseEvent event) {
		if(!checkValues()) {
    		return;
    	}
		ClientGUI.client.checkUserName(userNameTxtField.getText());
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
		ArrayList<String> response = (ArrayList<String>) sr.getServerResponse();
		//check if UserName is in the database
		if(sr.getMsg().equals("Error"))
		{
			userNameError.setText("Unable to locate UserName");
			userNameError.setVisible(true);
			enableEdit(false);
			return;
		}
		//check if UserName already has a permission 
		else if(sr.getMsg().equals("already has type")){
			userNameError.setText("This user already has a type");
			userNameError.setVisible(true);
			enableEdit(false);
			return;
		}
		enableEdit(true);
		userNameError.setVisible(true);
		userNameError.setText("Name: " + response.get(0) +" " + response.get(1));
		validUserName = userNameTxtField.getText();
		branchName = response.get(2);
    }

	
	
	//hides or shows certain components
	public void enableEdit(boolean val) {
		restaurantName.setVisible(val);
		restaurantNameTxtField.setVisible(val);
		restaurantAddress.setVisible(val);
		restaurantAddressTxtField.setVisible(val);
		restaurantType.setVisible(val);
		restaurantTypeCombo.setVisible(val);
		monthlyCommission.setVisible(val);
		monthlyCommissionBox.setVisible(val);
		monthlyCommissionBox.setVisible(val);
		monthlyCommissionBox.setVisible(val);
		importImage.setVisible(val);
		importImage1.setVisible(val);
		addSupplierBtn.setVisible(val);
		if(val == false) {
			updateSucess.setVisible(val);
	    	updateSucess1.setVisible(val);
		}
	}
	
	private boolean checkValues() {
    	if( InputValidation.checkSpecialCharacters(userNameTxtField.getText())) {
    		userNameError.setVisible(true);
    		enableEdit(false);
    		userNameError.setText("No special characters!");
    		return false;
    	}
    	if(userNameTxtField.getText().length() == 0) {
    		userNameError.setVisible(true);
    		enableEdit(false);
    		userNameError.setText("UserName must be filled!");
    		return false;
    	}
    	return true;
    }
	
	public void removeAllMessages() {
		UploadMsgTxt.setVisible(false);
		Error.setVisible(false);
		userNameError.setVisible(false);
		userNameTxtField.clear();
	}

	
	private boolean checkInfoFields() {
    	if( InputValidation.checkSpecialCharacters(userNameTxtField.getText())) {
    		Error.setVisible(true);
    		enableEdit(false);
    		Error.setText("ID can't contain special characters!");
    		return false;
    	}
    	if(userNameTxtField.getText().length() == 0) {
    		Error.setVisible(true);
    		enableEdit(false);
    		Error.setText("ID must be filled!");
    		return false;
    	}
    	if( InputValidation.checkSpecialCharacters(restaurantNameTxtField.getText())) {
    		Error.setVisible(true);
    		Error.setText("Name can't contain special characters!");
    		return false;
    	}
    	if(restaurantNameTxtField.getText().length() == 0) {
    		Error.setVisible(true);
    		Error.setText("Name must be filled!");
    		return false;
    	}
    	if(restaurantAddressTxtField.getText().length() == 0) {
    		Error.setVisible(true);
    		Error.setText("Address must be filled!");
    		return false;
    	}
    	if(!restaurantAddressTxtField.getText().contains(",")) {
    		Error.setVisible(true);
    		Error.setText("Address is not valid");
    		return false;
    	}
    	if(restaurantTypeCombo.getSelectionModel().isEmpty()) {
    		Error.setVisible(true);
    		Error.setText("Type must be filled!");
    		return false;
    	}
    	if(monthlyCommissionBox.getSelectionModel().isEmpty()) {
    		Error.setVisible(true);
    		Error.setText("Please choose monthly commission");
    		return false;
    	}
    	if(imgToUpload == null) {
    		Error.setVisible(true);
    		Error.setText("Please upload an image");
    		return false;
    	}
    	if(checkImageFormat()) {
    		Error.setVisible(true);
    		Error.setText("File foramt must be: jpj, gif or png");
    		return false;
    	}
    	return true;
    }

	
	@FXML
	void addSupplierClicked(MouseEvent event) {
		//check that the userName text field has'nt changed since it was checked
		if(!userNameTxtField.getText().equals(validUserName) ) {
			userNameError.setVisible(true);
			userNameError.setText("Unable to locate UserName");
			Error.setVisible(false);
    		return;
    	}
		
		if(!checkInfoFields()) { //check that all the fields are filled and correct
			Error.setVisible(true);
    		return;
		}
		try {
			MyFile image = new MyFile("Supplier Report");
			
			//convert file uploaded into a blob
			byte[] mybytearray = new byte[(int) imgToUpload.length()];
			FileInputStream fis = new FileInputStream(imgToUpload);
			BufferedInputStream bis = new BufferedInputStream(fis);

			image.initArray(mybytearray.length);
			image.setSize(mybytearray.length);
			bis.read(image.getMybytearray(), 0, mybytearray.length);
			//prepare fields for table bitemedb.suppliers
			NewSupplier newSupplier = new NewSupplier(userNameTxtField.getText(),
					restaurantTypeCombo.getValue(),restaurantNameTxtField.getText(),
					restaurantAddressTxtField.getText(),image,
					monthlyCommissionBox.getValue(), BranchName.valueOf(branchName));
			
			ClientGUI.client.addNewSupplier(newSupplier);//send to clientUI
			fis.close();
			bis.close();
			userNameError.setVisible(false);
			Error.setVisible(false);
			updateSucess.setVisible(true);
			updateSucess1.setVisible(true);
			UploadMsgTxt.setVisible(false);
			addSupplierBtn.setDisable(true);
		}
		catch (Exception e) {
			System.out.println("Error sending (Files msg) to Server");
		}
	}
	
	
	@FXML
    void uploadImageClicked(MouseEvent event) {
    	UploadMsgTxt.setVisible(false);
    	FileChooser fc = new FileChooser();
    	fc.setTitle("Open Folder");                               
    	imgToUpload = fc.showOpenDialog(router.getStage());
    	if(!checkImageFormat() ) {
    		UploadMsgTxt.setStyle("-fx-text-fill: green;");
    		UploadMsgTxt.setText("The image was uploaded successfully!");
    		UploadMsgTxt.setVisible(true);
    	}
    	else {
    		UploadMsgTxt.setStyle("-fx-text-inner-color: #BA55D3;");
    		UploadMsgTxt.setText("File foramt must be: jpj, gif or png");
    		UploadMsgTxt.setVisible(true);
    	}
    }
	
	
	/**
	 * @return true if the file format is suitable for image
	 */
	private boolean checkImageFormat() {
		return imgToUpload == null || 
				!( (imgToUpload.toString().toLowerCase().contains("jpg") ) ||
    			(imgToUpload.toString().toLowerCase().contains("png")) ||
    			(imgToUpload.toString().toLowerCase().contains("gif")) );
	}
	
	public void reSetTheScreen() {
		enableEdit(false);
		userNameError.setVisible(false);
		Error.setVisible(false);
		userNameTxtField.clear();
		restaurantTypeCombo.valueProperty().set(null);
		monthlyCommissionBox.valueProperty().set(null);
		restaurantAddressTxtField.clear();
		restaurantNameTxtField.clear();
		UploadMsgTxt.setVisible(false);
		imgToUpload = null;
	}

	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
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
		router.setAddNewSupplierController(this);
		setStage(router.getStage());
		setMonthlyCommissionComboBox();
		setRestaurantTypeComboBox();
		
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
