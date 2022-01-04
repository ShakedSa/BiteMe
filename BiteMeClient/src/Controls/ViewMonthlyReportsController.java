package Controls;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import Entities.MyFile;
import Entities.ServerResponse;
import Entities.User;
import Enums.UserType;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
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
import javafx.stage.Stage;

public class ViewMonthlyReportsController implements Initializable {

	public final UserType type = UserType.BranchManager;
	private Router router;
	private Stage stage;
	private Scene scene;

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
	private ComboBox<String> reportTypeBox;

	@FXML
	private Label showReportBtn;

	@FXML
	private ComboBox<String> yearBox;

	@FXML
	private Text reportErrorMsg;

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	/**
     * 
     * @param event
     */
    @FXML
    void showReportClicked(MouseEvent event) {
    	User user = (User) ClientGUI.getClient().getUser().getServerResponse();
    	ArrayList<String> arr = new ArrayList<>();
    	arr.add(reportTypeBox.getValue());
    	arr.add(monthBox.getValue());
    	arr.add(yearBox.getValue());
    	arr.add(user.getMainBranch().toString());
    	// fetch report from server's sql (if valid)
    	//wait for response:
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
		    	ClientGUI.getClient().getReport(arr);
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
		//wait for thread to finish (response receiver)
		try {
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		//check if report exists:
		ServerResponse response = ClientGUI.getClient().getLastResponse();
		if(response==null) {
			reportErrorMsg.setVisible(true);
		}
		else {
			reportErrorMsg.setVisible(false);
			MyFile myFile = (MyFile) response.getServerResponse();
			byte[] pdfArray = myFile.getMybytearray();

			try (FileOutputStream stream = new FileOutputStream("Report.pdf")) {
						stream.write(pdfArray);
				} catch (IOException e) {
					e.printStackTrace();
				}
			//open the pdf:
			if (Desktop.isDesktopSupported()) {
			    try {
			        File f = new File("Report.pdf");
			        Desktop.getDesktop().open(f);
			    } catch (IOException ex) {
			        // no application registered for PDFs
			    }
			}
			
			
			}
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
		router.setViewMonthlyReportsController(this);
		setStage(router.getStage());
		router.setArrow(leftArrowBtn, -90);
		String months_tmp[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
		String reports_tmp[] = { "Revenue", "Orders", "Performance" };
		yearBox.getItems().addAll(generateYears());
		monthBox.getItems().addAll(months_tmp);
		reportTypeBox.getItems().addAll(reports_tmp);// tbd
		yearBox.setValue("2021");
		monthBox.setValue("1");
		reportTypeBox.setValue("Revenue");
	}

	/**
	 * @return generated String[5] contains 5 last years from today
	 */
	private String[] generateYears() {
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		String years_tmp[] = new String[5];
		for (int i = 0; i < 5; i++) {
			years_tmp[i] = Integer.toString(currentYear - 4 + i);
		}
		return years_tmp;
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
