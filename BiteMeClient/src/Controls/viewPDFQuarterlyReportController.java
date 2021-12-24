package Controls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.awt.Desktop;
import Entities.MyFile;
import Enums.UserType;
import client.ClientGUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class viewPDFQuarterlyReportController implements Initializable {

	public final UserType type = UserType.CEO;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private Text CEOPanelBtn;

	@FXML
	private Text textMsg;

	@FXML
	private Rectangle avatar;

	@FXML
	private Text homePageBtn;

	@FXML
	private ImageView leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private ComboBox<String> quarterBox;

	@FXML
	private Text profileBtn;

	@FXML
	private ComboBox<String> BranchBox;

	@FXML
	private Label viewPDFReportBtn;

	@FXML
	private ComboBox<String> yearBox;

	ObservableList<String> branch, month, year;

	// creating list of branches
	private void setBranchComboBox() {
		ArrayList<String> type = new ArrayList<String>();
		type.add("North");
		type.add("Central");
		type.add("South");

		branch = FXCollections.observableArrayList(type);
		BranchBox.setItems(branch);

	}

	// creating list of months
	private void setMonthComboBox() {
		ArrayList<String> quarter = new ArrayList<String>();

		quarter.add("1");
		quarter.add("2");
		quarter.add("3");
		quarter.add("4");
		quarterBox.setItems(FXCollections.observableArrayList(quarter));

	}

	// creating list of years
	private void setYearComboBox() {
		ArrayList<String> type = new ArrayList<String>();
		type.add("2017");
		type.add("2018");
		type.add("2019");
		type.add("2020");
		type.add("2021");

		year = FXCollections.observableArrayList(type);
		yearBox.setItems(year);
	}

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	@FXML
	void profileBtnClicked(MouseEvent event) {
		clearSelections();
		clearMsg();
		router.showProfile();
	}

	@FXML
	void returnToCEOPanel(MouseEvent event) {
		clearSelections();
		clearMsg();
		router.returnToCEOPanel(event);
	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		clearSelections();
		clearMsg();
		router.changeSceneToHomePage();
	}
	
	@FXML
	void searchOndb(ActionEvent event) {
		if(!checkInputs())return ;
		String year = yearBox.getValue();
		String quarter = quarterBox.getValue();
		String branch = BranchBox.getValue();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				ClientGUI.client.viewORcheckQuarterReport(quarter, year, branch , "check");
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
					if(ClientGUI.client.getLastResponse().getMsg().equals("NotExists")) {
						msg ="Quarter report does not exists";
					}
					else if(ClientGUI.client.getLastResponse().getMsg().equals("Exists")) {
						viewPDFReportBtn.setDisable(false);
						return;
					}
					else if(ClientGUI.client.getLastResponse().getMsg().equals("Failed")) {
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

			}
		});
		t.start();
	}

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
		if(pdfToDownload==null) {
			clearMsg();
			return;
		}
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {

				ClientGUI.client.viewORcheckQuarterReport(quarter, year, branch , "view");
				synchronized (ClientGUI.monitor) {
					try {
						ClientGUI.monitor.wait();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}


				if (ClientGUI.client.getLastResponse().getMsg().equals("Success")) {
					
					MyFile pdfFile = (MyFile) ClientGUI.client.getLastResponse().getServerResponse();
					try {
						
						
						FileOutputStream out = new FileOutputStream(pdfToDownload);
						byte[] buff = pdfFile.getMybytearray();

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
						quarterBox.setOnAction(event -> searchOndb(event) );
						BranchBox.setOnAction(event -> searchOndb(event) );
						yearBox.setOnAction(event -> searchOndb(event) );
					});
					
				}
			}
		});
		t.start();
		
	}

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
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		router.setAvatar(avatar);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setViewPDFQuarterlyReportController(this);
		setStage(router.getStage());
		setBranchComboBox();
		setMonthComboBox();
		setYearComboBox();
		viewPDFReportBtn.setDisable(true);
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
	private void clearMsg() {
		textMsg.setText("");
		
	}
	private void clearSelections() {
		quarterBox.getSelectionModel().clearSelection();
		BranchBox.getSelectionModel().clearSelection();
		yearBox.getSelectionModel().clearSelection();
		viewPDFReportBtn.setDisable(true);
	}
}
