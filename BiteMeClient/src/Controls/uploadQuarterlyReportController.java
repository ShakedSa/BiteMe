package Controls;

import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;

import Enums.UserType;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class uploadQuarterlyReportController implements Initializable{
	

	public final UserType type= UserType.BranchManager;
	private Router router;
	private Scene scene;
	
    @FXML
    private Text UploadMsgTxt;

    @FXML
    private ImageView UploadMsgImg;
    
    @FXML
    private ImageView ImportImage;

    @FXML
    private Rectangle avatar;

    @FXML
    private Text homePageBtn;

    @FXML
    private Rectangle leftArrowBtn;

    @FXML
    private Text logoutBtn;

    @FXML
    private Text managerPanelBtn;

    @FXML
    private ComboBox<String> monthBox;

    @FXML
    private Text profileBtn;

    @FXML
    private Label uploadReportBtn;

    @FXML
    private ComboBox<String> yearBox;
    
    @FXML
    private Text InvalidMsg;
    
    private File pdfToUpload;

    @FXML
    void ImportImageClicked(MouseEvent event) {
    	UploadMsgImg.setVisible(false);
    	UploadMsgTxt.setVisible(false); // set upload success msg after importing new file.
    	FileChooser fc = new FileChooser();
    	fc.setTitle("Open Folder");
    	pdfToUpload = fc.showOpenDialog(router.getStage());
    	
    	if(pdfToUpload == null || !pdfToUpload.toString().contains("pdf"))
    		InvalidMsg.setVisible(true);
    	else
    		InvalidMsg.setVisible(false);
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

    @FXML
    void uploadReportClicked(MouseEvent event) {
    	if(monthBox.getValue() == null || yearBox.getValue() == null )
    	{
    		InvalidMsg.setVisible(true);
    		return;
    	}
		InvalidMsg.setVisible(false);
    	ClientGUI.client.sendReport(pdfToUpload,monthBox.getValue(), yearBox.getValue(), "Quarterly Report");
    	UploadMsgImg.setVisible(true);
    	UploadMsgTxt.setVisible(true);
    }
    
	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
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
		router.setUploadQuarterlyReportController(this);
		router.setArrow(leftArrowBtn, -90);
		String[] tempQuarter= {"1", "2", "3", "4"};
		yearBox.getItems().addAll(generateYears());
		monthBox.getItems().addAll(tempQuarter);
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

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Scene getScene() {
		return scene;
	}
	
}

