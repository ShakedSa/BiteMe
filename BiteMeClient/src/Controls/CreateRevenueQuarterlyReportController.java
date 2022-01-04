package Controls;

import java.net.URL;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

import Entities.ServerResponse;
import Entities.User;
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

/**
 * @author Eden
 * This controller is in charge of the logics and scene setup for the report creation page as branch manager.
 */
public class CreateRevenueQuarterlyReportController implements Initializable{
	

	public final UserType type= UserType.BranchManager;
	private Router router;
	private Scene scene;
	
    @FXML
    private Text UploadMsgTxt;

    @FXML
    private Rectangle avatar;

    @FXML
    private Text homePageBtn;

    @FXML
    private Rectangle leftArrowBtn;

    @FXML
    private Text logoutBtn;
    
    @FXML
    private ImageView VImage;

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
 
    @FXML
    void logoutClicked(MouseEvent event) {
    	
    	router.logOut();
    }

    
    @FXML
    void returnToHomePage(MouseEvent event) {
    	clearMessages();
    	router.changeSceneToHomePage();
    }

    @FXML
    void returnToManagerPanel(MouseEvent event) {
    	clearMessages();
    	router.returnToManagerPanel(event);
    }

    void clearMessages() {
    	UploadMsgTxt.setVisible(false);
		VImage.setVisible(false);
		InvalidMsg.setVisible(false);
    }
    /**
     * this method sends request to the client in order to upload the report into the server's database.
     * @param event
     */
    @FXML
    public void uploadReportClicked(MouseEvent event) {
    	if(checkLegality()) // check data legality, print error if illegal.
    	{
    		UploadMsgTxt.setVisible(false);
    		VImage.setVisible(false);
    		InvalidMsg.setText("Invalid year or quarter");
    		InvalidMsg.setVisible(true);
    		return;
    	}
		InvalidMsg.setVisible(false);
		User user = (User) ClientGUI.getClient().getUser().getServerResponse();
    	//request server to create a report with the relevant info:
		Thread t = new Thread(() -> {
			synchronized (ClientGUI.getMonitor()) {
		    	ClientGUI.getClient().createQuarterlyRevenueReport(monthBox.getValue(), yearBox.getValue(), user.getMainBranch().toString());
				try {
					ClientGUI.getMonitor().wait();
				} catch (Exception e) {
					e.printStackTrace();
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
    	//server response: "exists" if order exists, "created" if created succesfully.
		ServerResponse response = ClientGUI.getClient().getLastResponse();
    	if(response!=null && response.getDataType().equals("exists")) {
    		UploadMsgTxt.setVisible(false);
    		VImage.setVisible(false);
    		InvalidMsg.setText("Report for the selected date is already on the system");
    		InvalidMsg.setVisible(true);
    	}
    	else {
    		UploadMsgTxt.setVisible(true);
    		VImage.setVisible(true);
    		InvalidMsg.setVisible(false);
    	}
    }
    
	/**
	 * @return true if month and year values are illegal, false if they are fine.
	 */
	private boolean checkLegality() { // if month/year ==null or quarter isn't ended yet, return true
		return monthBox.getValue() == null ||
				yearBox.getValue() == null || 
				(yearBox.getValue().equals(Integer.toString(LocalDate.now().getYear())) && (Integer.parseInt(monthBox.getValue())*3) >= LocalDate.now().getMonthValue());
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
		router.setCreateRevenueQuarterlyReportController(this);
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


	/**
	 * @return the monthBox
	 */
	public ComboBox<String> getMonthBox() {
		return monthBox;
	}


	/**
	 * @return the yearBox
	 */
	public ComboBox<String> getYearBox() {
		return yearBox;
	}


	/**
	 * @return the invalidMsg
	 */
	public Text getInvalidMsg() {
		return InvalidMsg;
	}


	/**
	 * @return the uploadMsgTxt
	 */
	public Text getUploadMsgTxt() {
		return UploadMsgTxt;
	}

	
	
}

