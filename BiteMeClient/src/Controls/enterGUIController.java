package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import client.ClientGUI;
import client.ClientUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Controller for the landing page of the application. user needs to provide the
 * server IP.
 * 
 * @author Shaked
 * @author Natali
 * 
 * @version December 05 2021, v1.0
 *
 */
public class enterGUIController implements Initializable {

	private Router router;

	private Stage stage;

	@FXML
	private Label enterBtn;

	@FXML
	private Label errorMsg;

	@FXML
	private Label exitBtn;

	@FXML
	private TextField ipTxt;

	/**
	 * Setting the stage instance.
	 * 
	 * @param Stage stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
		router.setStage(stage);
	}

	/**
	 * OnClick event handler, closing the application.
	 * 
	 * @param MouseEvent event
	 */
	@FXML
	void closeApplication(MouseEvent event) {
		System.exit(0);
	}

	/**
	 * OnClick event handler, checking if the IP is a valid string and switch scenes
	 * to the homePage scene.
	 * 
	 * @param MouseEvent event.
	 */
	@FXML
	void enterApplication(MouseEvent event) {
		String ip = ipTxt.getText();
		if (ip == null || ip.equals("")) {
			errorMsg.setTextFill(Color.web("red"));
			errorMsg.setText("Must fill ip");
			return;
		}
		ClientGUI.setClient(new ClientUI(ip, 5555));
		AnchorPane mainContainer;
		if (router.getHomePageController() == null) {
			homePageController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeHomePage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.setProfile(false);
				controller.setFavRestaurants();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - HomePage");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - HomePage");
			stage.setScene(router.getHomePageController().getScene());
			stage.show();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setEnterguicontroller(this);
		setStage(router.getStage());
	}

}
