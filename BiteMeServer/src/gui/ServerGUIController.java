package gui;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import JDBC.mysqlConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Server GUI Controller
 * 
 * 
 * @author Aviel Malayev
 * @author Natali Krief
 * @author Michael Ben Israel
 * @author Eden Ben Abu
 * @author Shaked Sabag
 * @version November 2021 (1.0)
 */
public class ServerGUIController {
	
    @FXML
    private Button importUsersBtn;

	@FXML
	private TextField txtPort;

	@FXML
	private TextField txtDbPath;

	@FXML
	private TextField txtDBuser;

	@FXML
	private PasswordField txtDbPass;

	@FXML
	private Button btnConnectDB;

	@FXML
	private Button btnDiconnectDB;

	@FXML
	private Button btnClientInfo;

	@FXML
	private TextArea txtFieldClientInfo;

	@FXML
	private Button btnClose;

	/**
	 * Starting method for the server, setting the scene.
	 * 
	 * @param primaryStage 
	 * 
	 * @throws Exception
	 */
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerGui.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setTitle("Server Configuration");
		primaryStage.setScene(scene); 
		primaryStage.setOnCloseRequest((EventHandler<WindowEvent>) new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});
		primaryStage.show();
	}

	/**
	 * On click event handler. Connecting to the server.
	 * 
	 * @param event
	 */
	@FXML
	void ConnectDB(ActionEvent event) {
		/**
		 * Getting data from the GUI
		 */
		String p;
		String[] arr = { txtDbPath.getText(), txtDBuser.getText(), txtDbPass.getText() };

		p = txtPort.getText();
		if (!checkInput(arr[0]) || !checkInput(arr[1]) || !checkInput(arr[2]) || !checkInput(p)) {
			setMessage("Must fill all fields!");
			return;
		}
		if (p.trim().isEmpty()) {
			/** Checks if the inserted port is integer */
			setMessage("Must enter a port number");
			return;
		}
		if (Integer.parseInt(p) < 0 || Integer.parseInt(p) > 65535) {
			/** Checks if the inserted port is in the right range */
			setMessage("Port needs to be in range of 0 to 65535");
			return;
		}
		/** Connecting to the DB. */
		mysqlConnection.setConnection(arr);
		ServerUI.runServer(p);
		btnDiconnectDB.setDisable(false);
		btnConnectDB.setDisable(true);

		/** Setting the server UI controller to the server. */
		if (ServerUI.sv != null) {
			ServerUI.sv.setController(this);
		}
	}

	/**
	 * Checking if a single string is valid by standard definition. Not empty string
	 * or a null.
	 * 
	 * @param input
	 */
	public boolean checkInput(Object input) {
		if (input == null || input.equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * Closing the server.
	 * 
	 * @param event
	 */
	@FXML
	void closeServer(ActionEvent event) {
		// get a handle to the stage
		// Stage stage = (Stage) btnClose.getScene().getWindow();
		// do what you have to do
		// stage.close();
		System.exit(0);
	}
	
	
    /**
     * importing users from importUserSimulation table in sql to users table.
     * 
     * @param event
     */
    @FXML
    void importClicked(MouseEvent event) {
    	mysqlConnection.importUsers();
    }
    
    
	/**
	 * Disconnecting the server connection.
	 * 
	 * @param event
	 */
	@FXML
	void disconnectDB(ActionEvent event) {

		try {
			ServerUI.sv.close();
			btnDiconnectDB.setDisable(true);
			btnConnectDB.setDisable(false);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Getting the connected clients information.
	 * 
	 * @param event
	 */
	@FXML
	void getClientInfo(ActionEvent event) {
		if (ServerUI.sv != null)
			if (ServerUI.sv.isListening())
				setMessage(ServerUI.sv.showConnectionInfo());
			else
				setMessage("Server is offline, cannot check connections !");
	}

	/**
	 * Setting a new message in the textArea with a time stamp.
	 * 
	 * @param msg
	 */
	public void setMessage(String msg) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String txt = txtFieldClientInfo.getText();
		if(txt == null || txt.equals("")) {
			txtFieldClientInfo.setText(dtf.format(now) + " " + msg);
		}else {
			txtFieldClientInfo.setText(dtf.format(now) + " " + msg + "\n" + txt);
		}
	}
}
