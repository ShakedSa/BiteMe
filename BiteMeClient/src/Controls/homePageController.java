package Controls;

import java.io.IOException;

import Entities.User;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Homepage form controller.
 * 
 * @author Shaked
 * @author Natali
 * 
 * @version December 05 2021, v1.0
 */
public class homePageController {

	private Stage stage;

	@FXML
	private ImageView caruasalLeft;

	@FXML
	private ImageView caruasalRight;

	@FXML
	private Text homePageBtn;

	@FXML
	private Text logOutBtn;

	@FXML
	private Text loginBtn;

	@FXML
	private Text profileBtn;

	@FXML
	private Text restaurantBtn;

	@FXML
	private Text userFirstName;

	@FXML
	private ImageView avatar;

	@FXML
	void caruasalLeftClicked(MouseEvent event) {

	}

	@FXML
	void caruasalRight(MouseEvent event) {

	}

	/**
	 * Switching scenes to login page.
	 * 
	 * @param MouseEvent event
	 */
	@FXML
	void displayLoginScreen(MouseEvent event) {
		AnchorPane mainContainer;
		loginController controller;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("../gui/bitemeLoginPage.fxml"));
			mainContainer = loader.load();
			controller = loader.getController();
			controller.setStage(stage);
			Scene mainScene = new Scene(mainContainer);
			mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
			stage.setTitle("BiteMe - Login");
			stage.setScene(mainScene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Setting homepage to match user's permissions.
	 */
	public void setProfile() {
		User user = ClientGUI.client.getUser();
		userFirstName.setText(user.getFirstName());
		setDefaults(true);
		logOutBtn.setStyle("-fx-cursor: hand;");
		profileBtn.setStyle("-fx-cursor: hand;");
		restaurantBtn.setStyle("-fx-cursor: hand;");
		homePageBtn.setStyle("-fx-cursor: hand;");
	}

	/**
	 * Onclicked event handler, logout.
	 * 
	 * @param MouseEvent event
	 */
	@FXML
	void logOutBtnClicked(MouseEvent event) {
		ClientGUI.client.logout(ClientGUI.client.getUser().getUserName());
		ClientGUI.client.setUser(null);
		userFirstName.setText("Guest");
		setDefaults(false);
	}

	/**
	 * Creating the illusion of logging out.
	 * 
	 * @param boolean val
	 */
	private void setDefaults(boolean val) {
		logOutBtn.setVisible(val);
		profileBtn.setVisible(val);
		restaurantBtn.setVisible(val);
		homePageBtn.setVisible(val);
		loginBtn.setVisible(!val);
	}

	@FXML
	void profileBtnClicked(MouseEvent event) {

	}

	@FXML
	void restaurantBtnClicked(MouseEvent event) {

	}

}
