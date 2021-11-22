package gui;

import java.io.IOException;

import ClientServerComm.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class prototypeGUIController {
	
	private Stage stage;

	@FXML
    private Rectangle updateBG;

    @FXML
    private Text updateTxt;

    @FXML
    private Rectangle showBG;

    @FXML
    private Text showTxt;

    @FXML
    private Rectangle exitBG;

    @FXML
    private Text exitTxt;

    public void setStage(Stage stage) {
    	this.stage = stage;
    }
    
    @FXML
    void closeApplication(MouseEvent event) {
    	System.exit(0);
    }

    @FXML
	void mouseEnter(MouseEvent event) {
		Text txt;
		Rectangle bg;
		if(event.getSource() instanceof Text) {
			txt = (Text)event.getSource();
			switch(txt.getText()) {
			case "Update":
				updateBG.setStyle("-fx-fill:#2197ff;");
				break;
			case "Show":
				showBG.setStyle("-fx-fill:#2197ff;");
				break;
			case "Exit":
				exitBG.setStyle("-fx-fill:#2197ff;");
			}
		}else {
			bg = (Rectangle)event.getSource();
			switch(bg.getId()) {
			case "updateBG":
				updateBG.setStyle("-fx-fill:#2197ff;");
				break;
			case "showBG":
				showBG.setStyle("-fx-fill:#2197ff;");
				break;
			case "exitBG":
				exitBG.setStyle("-fx-fill:#2197ff;");
			}
		}
	}

	@FXML
	void mouseExit(MouseEvent event) {
		Rectangle bg;
		if(event.getSource() instanceof Rectangle) {
			bg = (Rectangle)event.getSource();
			switch(bg.getId()) {
			case "updateBG":
				updateBG.setStyle("-fx-fill:#2197ff00;");
				break;
			case "showBG":
				showBG.setStyle("-fx-fill:#2197ff00;");
				break;
			case "exitBG":
				exitBG.setStyle("-fx-fill:#2197ff00;");
			}
		}
	}
    
	@FXML
    void openShowStage(MouseEvent event) {
    	AnchorPane showContainer;
    	prototypeShowController controller;
    	try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(getClass().getResource("prototypeShowGUI.fxml"));
    		showContainer = loader.load();
    		controller = loader.getController();
    		controller.setStage(stage);
    		controller.setTable();
    		Scene showScene = new Scene(showContainer);
    		stage.setScene(showScene);
    		stage.show();
    	}catch(IOException e) {
    		e.printStackTrace();
    		return;
    	}
    }
    
    
    @FXML
    void openUpdateStage(MouseEvent event) {
    	AnchorPane updateContainer;
    	prototypeUpdateController controller;
    	try {
    		FXMLLoader loader = new FXMLLoader();
    		loader.setLocation(getClass().getResource("prototypeUpdateGUI.fxml"));
    		updateContainer = loader.load();
    		controller = loader.getController();
    		controller.setStage(stage);
    		controller.setCombo();
    		Scene updateScene = new Scene(updateContainer);
    		stage.setScene(updateScene);
    		stage.show();
    	}catch(IOException e) {
    		e.printStackTrace();
    		return;
    	}
    }

}