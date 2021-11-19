package gui;

import java.io.IOException;

import JDBC.mysqlConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerGUIController {

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

    
	public void start(Stage primaryStage) throws Exception {	
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerGui.fxml"));
				
		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("/gui/ServerPort.css").toExternalForm());
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
	
    @FXML
    void ConnectDB(ActionEvent event) {
    	//data extraction from gui:
		String p;
		String[] arr= {txtDbPath.getText(),txtDBuser.getText(),txtDbPass.getText()};
		p=txtPort.getText();
		if(p.trim().isEmpty()) {// NEED TO CHECK IF INSERTED PORT IN INTEGER !!!
			txtFieldClientInfo.setText("You must enter a port number");	
		}
		else
		{
			//setup sql+server:
			mysqlConnection.setConnection(arr);
			ServerUI.runServer(p);	
			btnDiconnectDB.setDisable(false);
		}
    }

    @FXML
    void closeServer(ActionEvent event) {
	    // get a handle to the stage
	   // Stage stage = (Stage) btnClose.getScene().getWindow();
	    // do what you have to do
	    //stage.close();
    	System.exit(0);
    }

    @FXML
    void disconnectDB(ActionEvent event) {

        	try {
        		//ServerUI.sv.stopListening();
        		ServerUI.sv.close();
    			btnDiconnectDB.setDisable(true);
    			
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

    }

    @FXML
    void getClientInfo(ActionEvent event) {
    	if(ServerUI.sv!=null)
    		if(ServerUI.sv.isListening())
    			txtFieldClientInfo.setText(ServerUI.sv.showConnectionInfo());
    	else
    		txtFieldClientInfo.setText("Server is offline,\n cannot check connections !");
    }

}
