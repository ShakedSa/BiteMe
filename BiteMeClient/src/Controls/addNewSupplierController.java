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
import Entities.User;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This controller is incharge of adding new suppliers logic
 * @author Michael
 *
 */
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
	private ImageView infoIcon;

	@FXML
	private TextArea infoTxtArea;
	
	@FXML
    private Text InvalidMsg;
	
	@FXML
	private Text restaurantName;
	
	@FXML
    private Text userInfo;

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
	private Rectangle leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Text managerPanelBtn;

	@FXML
	private Text updateSucess;
	
	@FXML
	private Text required;

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
	
	private File imgToUpload;
	
	private ObservableList<String> list;
	
	private String userName;
	

	
	/**set possible commissions inside the combo box
	 * modifies: monthlyCommissionBox
	 */
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
	
	
		/**set possible restuarant types inside the combo box
		 * modifies: restaurantTypeComboBox
		 */
		private void setRestaurantTypeComboBox() {
			ArrayList<String> type = new ArrayList<String>();
			type.add("Italian");
			type.add("Fastfood");
			type.add("Asian");
			type.add("Other");
			list = FXCollections.observableArrayList(type);
			restaurantTypeCombo.setItems(list);
		}

		
	/**hides or shows certain componets in the page
	 * @param val = True / false
	 */
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
	
	
	/**gets user information from the prev page and shows it in a text field
	 * @param fName = user first name
	 * @param lName = user last name
	 * @param userName
	 */
	public void setUserInfo(String fName, String lName, String userName) {
    	userInfo.setText(userName + " (" + fName + " " + lName+ ")");
    	this.userName = userName;
    }

	
	
	/**check user input in all possible fields
	 * @return true if the fields are filled correctly
	 */
	private boolean checkInfoFields() {
		//checks for special characters
    	if( InputValidation.checkSpecialCharacters(restaurantNameTxtField.getText())) {
    		Error.setVisible(true);
    		Error.setText("Name can't contain special characters!");
    		return false;
    	}
    	//checks if the field is not empty
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
    	//check that the address is in valid format
    	if(!restaurantAddressTxtField.getText().matches("[a-zA-Z]+ \\d,( [a-zA-Z]+)+")) {
    		Error.setVisible(true);
    		Error.setText("Address is not in the format");
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
    	//checks that an image has been uploaded
    	if(imgToUpload == null) {
    		Error.setVisible(true);
    		Error.setText("Please upload an image");
    		return false;
    	}
    	if(checkImageFormat()) {
    		Error.setVisible(true);
    		Error.setText("File foramt must be: jpj, gif or png");
    		UploadMsgTxt.setVisible(false);
    		return false;
    	}
    	return true;
    }
	
	
	/**shows instructions label for correct address format 
	 * @param event
	 */
	@FXML
	void infoIconEnter(MouseEvent event) {
		infoTxtArea.setVisible(true);
	}
	
	
	/**hides instructions label for correct address format
	 * @param event
	 */
	@FXML
	void infoIconExit(MouseEvent event) {
		infoTxtArea.setVisible(false);
	}

	
	/**send request to client UI in order to add a new supplier
	 * @param event
	 */
	@FXML
	void addSupplierClicked(MouseEvent event) {
		if(!checkInfoFields()) { //check that all the fields are filled correctly
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
			User user = (User) ClientGUI.client.getUser().getServerResponse();
			NewSupplier newSupplier = new NewSupplier(userName,
					restaurantTypeCombo.getValue(),restaurantNameTxtField.getText(),
					restaurantAddressTxtField.getText(),image,
					monthlyCommissionBox.getValue(), user.getMainBranch());
			//send to clientUI
			ClientGUI.client.addNewSupplier(newSupplier);
			fis.close();
			bis.close();
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
	
	
	/**let's the user browse a file from his computer
	 * @param event
	 * modifies: imgToUpload (File)
	 */
	@FXML
    void uploadImageClicked(MouseEvent event) {
    	UploadMsgTxt.setVisible(false);
    	FileChooser fc = new FileChooser();
    	fc.setTitle("Open Folder");                               
    	imgToUpload = fc.showOpenDialog(router.getStage());
    	//check that the file is an image
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
	
	
	/**checks that the file format is suitable for an image
	 * @return true if it is
	 */
	private boolean checkImageFormat() {
		return imgToUpload == null || 
				!( (imgToUpload.toString().toLowerCase().contains("jpg") ) ||
    			(imgToUpload.toString().toLowerCase().contains("png")) ||
    			(imgToUpload.toString().toLowerCase().contains("gif")) );
	}
	
	
	/**delete user previous actions when entering and exiting the page
	 */
	public void reSetTheScreen() {
		restaurantTypeCombo.valueProperty().set(null);
		monthlyCommissionBox.valueProperty().set(null);
		restaurantAddressTxtField.clear();
		restaurantNameTxtField.clear();
		UploadMsgTxt.setVisible(false);
		updateSucess1.setVisible(false);
		updateSucess.setVisible(false);
		imgToUpload = null;
		infoTxtArea.setVisible(false);
		Error.setVisible(false);
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
	
	
	/**
	 *init controller information upon creation
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setAddNewSupplierController(this);
		setStage(router.getStage());
		setMonthlyCommissionComboBox();
		setRestaurantTypeComboBox();
		router.setArrow(leftArrowBtn, -90);
		
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
