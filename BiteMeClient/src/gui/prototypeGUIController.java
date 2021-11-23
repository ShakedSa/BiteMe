package gui;

import java.io.IOException;

import ClientServerComm.Client;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * BiteMe prototype GUI controller.
 * 
 * @author Aviel Malayev
 * @author Natali Krief
 * @author Michael Ben Israel
 * @author Eden Ben Abu
 * @author Shaked Sabag
 * @version November 2021 (1.0)
 */
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

	/**
	 * Getting the stage of the application.
	 * 
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Exit button of the application. On click event handler closing the
	 * application.
	 * 
	 * @param event
	 */
	@FXML
	void closeApplication(MouseEvent event) {
		System.exit(0);
	}

	/**
	 * Creating hover effect on all the buttons.
	 * 
	 * @param event
	 */
	@FXML
	void mouseEnter(MouseEvent event) {
		Text txt;
		Rectangle bg;
		if (event.getSource() instanceof Text) {
			txt = (Text) event.getSource();
			switch (txt.getText()) {
			case "Update Order":
				updateBG.setStyle("-fx-fill:#2197ff;");
				break;
			case "Display Orders":
				showBG.setStyle("-fx-fill:#2197ff;");
				break;
			case "Exit":
				exitBG.setStyle("-fx-fill:#2197ff;");
			}
		} else {
			bg = (Rectangle) event.getSource();
			switch (bg.getId()) {
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

	/**
	 * Creating hover effect on all the buttons.
	 * 
	 * @param event
	 */
	@FXML
	void mouseExit(MouseEvent event) {
		if (event.getSource() instanceof Text) {
			return;
		}
		Rectangle bg;
		if (event.getSource() instanceof Rectangle) {
			bg = (Rectangle) event.getSource();
			switch (bg.getId()) {
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

	/**
	 * On click event handler. Swapping the current scene to display the 'show'
	 * scene.
	 * 
	 * @param event
	 */
	@FXML
	void openShowStage(MouseEvent event) {
		AnchorPane showContainer;
		prototypeShowController controller;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("prototypeShowGUI.fxml"));
			showContainer = loader.load();
			controller = loader.getController();
			controller.setTable();
			controller.setStage(stage);
			Scene showScene = new Scene(showContainer);
			stage.setScene(showScene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * On click event handler. Swapping the current scene to display the 'update'
	 * scene.
	 * 
	 * @param event
	 */
	@FXML
	void openUpdateStage(MouseEvent event) {
		AnchorPane updateContainer;
		prototypeSelectOrderController controller;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("prototypeSelectOrderGUI.fxml"));
			updateContainer = loader.load();
			controller = loader.getController();
			controller.setStage(stage);
//			controller.setCombo();
			Scene updateScene = new Scene(updateContainer);
			stage.setScene(updateScene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

}