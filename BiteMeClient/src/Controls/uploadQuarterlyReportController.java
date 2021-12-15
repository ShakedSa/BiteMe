package Controls;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.ResourceBundle;

import com.sun.javafx.tk.FileChooserType;

import Entities.MyFile;
import Entities.ServerResponse;
import Entities.User;
import Enums.UserType;
import client.ClientGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ComboBoxBase;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    private ImageView leftArrowBtn;

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
    	ClientGUI.client.sendReport(pdfToUpload, monthBox.getValue(), yearBox.getValue(), "Quarterly Report");
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
		String months_tmp[]= {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
		yearBox.getItems().addAll(generateYears());
		monthBox.getItems().addAll(months_tmp);
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

