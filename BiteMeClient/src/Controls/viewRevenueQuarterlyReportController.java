package Controls;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.MyFile;
import Enums.UserType;
import client.ClientGUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class viewRevenueQuarterlyReportController implements Initializable {
	public final UserType type = UserType.CEO;
	private Router router;
	private Stage stage;
	private Scene scene;
	private MyFile result;
	@FXML
	private ComboBox<String> BranchBox;

	@FXML
	private Text CEOPanelBtn;

	@FXML
	private Rectangle avatar;

	@FXML
	private Text homePageBtn;

	@FXML
	private Rectangle leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Text profileBtn;

	@FXML
	private ComboBox<String> quarterBox;

	@FXML
	private Text textMsg;

	@FXML
	private Label viewPDFReportBtn;

	@FXML
	private ComboBox<String> yearBox;


	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setViewRevenueQuarterlyReportController(this);
		setStage(router.getStage());
		router.setArrow(leftArrowBtn, -90);

		setQuarterBoxes();

		viewPDFReportBtn.setDisable(true);
	}

	
	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	/**
	 * clearing all the relevant selections and massages 
	 * before leaving current page
	 * @param event
	 */
	@FXML
	void profileBtnClicked(MouseEvent event) {

		clearSelections();
		clearMsg();
		router.showProfile();
	}
	
	/**
	 * clearing all the relevant selections and massages 
	 * before leaving current page
	 * @param event
	 */
	@FXML
	void returnToCEOPanel(MouseEvent event) {

		clearSelections();
		clearMsg();
		router.returnToCEOPanel(event);
	}
	
	/**
	 * clearing all the relevant selections and massages 
	 * before leaving current page
	 * @param event
	 */
	@FXML
	void returnToHomePage(MouseEvent event) {

		clearSelections();
		clearMsg();
		router.changeSceneToHomePage();
	}

	/** 
	 * creating list of branches 
	 */
	private void setBranchComboBox() {
		ArrayList<String> type = new ArrayList<String>();
		type.add("North");
		type.add("Central");
		type.add("South");
		
		BranchBox.setItems(FXCollections.observableArrayList(type));
		BranchBox.setPromptText("Branch");

	}

	/**
	 * creating list of months
	 */
	private void setQuarterComboBox() {
		ArrayList<String> quarter = new ArrayList<String>();

		quarter.add("1");
		quarter.add("2");
		quarter.add("3");
		quarter.add("4");
		quarterBox.setItems(FXCollections.observableArrayList(quarter));
		quarterBox.setPromptText("Quarter");
	}

	/**
	 *  creating list of years
	 */
	private void setYearComboBox() {
		ArrayList<String> type = new ArrayList<String>();
		type.add("2021");
		type.add("2020");
		type.add("2019");
		type.add("2018");
		type.add("2017");

		yearBox.setItems(FXCollections.observableArrayList(type));
		yearBox.setPromptText("Year");
	}
	
	/**
	 * set all the boxes of this page
	 */
	public void setQuarterBoxes() {
		setBranchComboBox();
		setQuarterComboBox();
		setYearComboBox();

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
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		router.setAvatar(avatar);
	}
	
	
	/**
	 * searching relevant report on database, if exists enabling    
	 * "viewPDFReport" button
	 * @param event
	 */
	@FXML
	private void searchOndb(ActionEvent event) {
		if (!checkInputs())
			return;
		String year = yearBox.getValue();
		String quarter = quarterBox.getValue();
		String branch = BranchBox.getValue();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				ClientGUI.client.viewORcheckRevenueQuarterReport(quarter, year, branch);
				synchronized (ClientGUI.monitor) {
					try {
						ClientGUI.monitor.wait();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}

				if (ClientGUI.client.getLastResponse().getServerResponse() == null) {
					String msg = "";
					if (ClientGUI.client.getLastResponse().getMsg().equals("NotExists")) {
						msg = "Quarter report does not exists";
					} else if (ClientGUI.client.getLastResponse().getMsg().equals("Failed")) {
						msg = "Somthing went worng";
					}
					final String m = msg;
					Platform.runLater(() -> {
						textMsg.setFill(Color.RED);
						textMsg.setText(m);
					});
					viewPDFReportBtn.setDisable(true);
					return;
				}
				if (ClientGUI.client.getLastResponse().getMsg().equals("Success")) {
					result = (MyFile) ClientGUI.client.getLastResponse().getServerResponse();
					viewPDFReportBtn.setDisable(false);
					return;
				}
			}
		});
		t.start();
	}
	
	/**
	 * downloading the report and opening quarterly revenue pdf, 
	 * clearing the page boxes after 
	 * @param event
	 */
	@FXML
	void viewPDFReportClicked(MouseEvent event) {
		textMsg.setFill(Color.BLACK);
		textMsg.setText("Downloading...");
		String year = yearBox.getValue();
		String quarter = quarterBox.getValue();
		String branch = BranchBox.getValue();
		FileChooser fc = new FileChooser();
		fc.setTitle("Save PDF File");
		String fileName = "Report_" + year + "_Quarter" + quarter + "_Branch_" + branch + ".pdf";
		fc.setInitialFileName(fileName);
		File pdfToDownload = fc.showSaveDialog(router.getStage());
		if (pdfToDownload == null) {
			clearMsg();
			return;
		}

		try {
		FileOutputStream out = new FileOutputStream(pdfToDownload);
		byte[] buff = result.getMybytearray();

			out.write(buff);
			out.close();
			
			File toOpen = new File(pdfToDownload.getPath());
			Desktop desktop = Desktop.getDesktop();
			desktop.open(toOpen);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		Platform.runLater(() -> {
			textMsg.setFill(Color.GREEN);
			textMsg.setText("Finished!");
			quarterBox.setOnAction(null);
			BranchBox.setOnAction(null);
			yearBox.setOnAction(null);
			clearSelections();
			quarterBox.setOnAction(newevent -> searchOndb(newevent) );
			BranchBox.setOnAction(newevent -> searchOndb(newevent) );
			yearBox.setOnAction(newevent -> searchOndb(newevent) );
		});
		

	}
	
	/**
	 * checking that all the boxes are selected
	 * @return
	 */
	private boolean checkInputs() {
		String branch = BranchBox.getValue();
		String month = quarterBox.getValue();
		String year = yearBox.getValue();
		textMsg.setFill(Color.RED);
		if (branch == null) {

			textMsg.setText("Please select Branch");
			return false;
		}
		if (year == null) {
			textMsg.setText("Please select an year");
			return false;
		}
		if (month == null) {
			textMsg.setText("Please select specific quarter");
			return false;
		}
		clearMsg();
		return true;
	}
	
	/**
	 * clearing massage
	 */
	private void clearMsg() {
		textMsg.setText("");

	}
	
	
	/**
	 * clearing the selections 
	 */
	private void clearSelections() {
		quarterBox.getSelectionModel().clearSelection();
		BranchBox.getSelectionModel().clearSelection();
		yearBox.getSelectionModel().clearSelection();
		setQuarterBoxes();
		viewPDFReportBtn.setDisable(true);
	}
}
