package Controls;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Entities.User;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * Login form controller.
 * 
 * @author Shaked
 * @author Natali
 * 
 * @version December 05 2021, v1.0
 */
public class loginController {

	private Stage stage;

	@FXML
	private ImageView back;

	@FXML
	private Label errorMsg;

	@FXML
	private Label loginBtn;

	@FXML
	private PasswordField passwordTxt;

	@FXML
	private TextField usernameTxt;

	@FXML
	private Rectangle avatar;

	/**
	 * Onclick event handler, returning to homepage.
	 * 
	 * @param MouseEvent event
	 */
	@FXML
	void backToHomePage(MouseEvent event) {
		changeScenes();
	}

	/**
	 * Onclick event handler, checking for valid username and password inputs and
	 * requesting a login function from the server. On success login switch scene to
	 * homepage. On failed login display a message.
	 * 
	 * @param MouseEvent event.
	 */
	@FXML
	void loginClicked(MouseEvent event) {
		String userName = usernameTxt.getText();
		String password = passwordTxt.getText();
		if (!checkValidText(userName)) {
			errorMsg.setText("Must fill username");
			return;
		}
		if (checkSpecialCharacters(userName)) {
			errorMsg.setText("Special characters aren't allowed in username");
			return;
		}
		if (!checkValidText(password)) {
			errorMsg.setText("Must fill password");
			return;
		}
		if (checkSpecialCharacters(password)) {
			errorMsg.setText("Special characters aren't allowed in password");
			return;
		}
		errorMsg.setText("");
		ClientGUI.client.login(userName, password);
		try {
			Thread.sleep(500);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		if (ClientGUI.client.getUser() == null) {
			errorMsg.setText("Wrong username or password");
			return;
		}
		changeScenes();
	}

	/**
	 * Changing the current scene to homepage.
	 */
	private void changeScenes() {
		AnchorPane mainContainer;
		homePageController controller;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("../gui/bitemeHomePage.fxml"));
			mainContainer = loader.load();
			controller = loader.getController();
			controller.setStage(stage);
			controller.setAvatar();
			if (ClientGUI.client.getUser() != null) {
				controller.setProfile(true);
			}
			Scene mainScene = new Scene(mainContainer);
			mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
			stage.setTitle("BiteMe - HomePage");
			stage.setScene(mainScene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	/**
	 * Checks if the string is not empty.
	 * 
	 * @param String text
	 * @return boolean
	 */
	private boolean checkValidText(String input) {
		if (input == null || input.equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if the string contains special characters.
	 * 
	 * @param String input
	 * @return boolean
	 */
	public boolean checkSpecialCharacters(String input) {
		Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(input);
		return m.find();
	}

	/**
	 * Setting the stage instance.
	 * 
	 * @param Stage stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	public void setAvatar() {
		try {
		avatar.setArcWidth(65);
		avatar.setArcHeight(65);
		ImagePattern pattern = new ImagePattern(new Image(getClass().getResource("../images/guest-avatar.png").toString()));
		avatar.setFill(pattern);
		avatar.setEffect(new DropShadow(3,Color.BLACK));
		avatar.setStyle("-fx-border-width: 0");
		}catch(Exception e) {
			e.printStackTrace();
			return;
		}
	}

}
