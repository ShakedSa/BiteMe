package Controls;

import java.io.IOException;

import Entities.User;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
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

public class restaurantSelectionController {

	private Stage stage;
	
	@FXML
	private Rectangle avatar;

	@FXML
	private Text homePageBtn;

	@FXML
	private ImageView leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Text profileBtn;

	@FXML
	private Text restaurantsBtn;

	@FXML
	private ImageView rightArrowBtn;

	@FXML
	private TextField searchRestaurantFieldTxt;

	@FXML
	void logoutClicked(MouseEvent event) {
		User user = ClientGUI.client.getUser();
		if (user != null) {
			ClientGUI.client.logout(user.getUserName());
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
			ClientGUI.client.setUser(null);
		}
		changeSceneToHomePage(false);
	}

	@FXML
	void moveLeftClicked(MouseEvent event) {

	}

	@FXML
	void moveRightClicked(MouseEvent event) {

	}

	@FXML
	void openProfile(MouseEvent event) {

	}

	/**
	 * Changing the current scene to homepage.
	 */
	@FXML
	void returnToHomePage(MouseEvent event) {
		changeSceneToHomePage(true);
	}
	
	void changeSceneToHomePage(boolean val) {
		AnchorPane mainContainer;
		homePageController controller;
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("../gui/bitemeHomePage.fxml"));
			mainContainer = loader.load();
			controller = loader.getController();
			controller.setStage(stage);
			controller.setAvatar();
			controller.setProfile(val);
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
	 * Setting the stage instance.
	 * 
	 * @param Stage stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	/**
	 * Setting user avatar.
	 * */
	public void setAvatar() {
		try {
			avatar.setArcWidth(65);
			avatar.setArcHeight(65);
			ImagePattern pattern = new ImagePattern(
					new Image(getClass().getResource("../images/guest-avatar.png").toString()));
			avatar.setFill(pattern);
			avatar.setEffect(new DropShadow(3, Color.BLACK));
			avatar.setStyle("-fx-border-width: 0");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
	}
	
	/**
	 * Getting restaurants from the db.
	 * */
	public void setRestaurants() {
		ClientGUI.client.getRestaurants();
	}

}
