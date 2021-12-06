package Controls;

import java.io.IOException;

import Entities.User;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
	private Rectangle avatar;

	@FXML
	private Text ceoBtn;

	@FXML
	private Text managerBtn;

	@FXML
	private Text supplierBtn;

	@FXML
	private Text employerHRBtn;

	@FXML
	void caruasalLeftClicked(MouseEvent event) {

	}

	@FXML
	void caruasalRight(MouseEvent event) {

	}

	@FXML
	void ceoBtnClicked(MouseEvent event) {

	}

	@FXML
	void managerBtnClicked(MouseEvent event) {

	}

	@FXML
	void supplierBtnClicked(MouseEvent event) {

	}

	@FXML
	void employerHRBtnClicked(MouseEvent event) {

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
			controller.setAvatar();
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
		setDefaults(user, true);
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
			userFirstName.setText("Guest");
			setDefaults(user, false);
		}
	}

	/**
	 * Changing the homepage interface depending on the user permissions.
	 * 
	 * @param boolean val
	 */
	private void setDefaults(boolean val) {
		logOutBtn.setVisible(val);
		profileBtn.setVisible(val);
		homePageBtn.setVisible(val);
		loginBtn.setVisible(!val);
	}

	/**
	 * Changing the homepage interface depending on the user permissions.
	 * */
	private void setDefaults(User user, boolean val) {
		setDefaults(val);
		switch (user.getUserType()) {
		case Customer:
		case BusinessCustomer:
			restaurantBtn.setVisible(val);
			break;
		case BranchManager:
			managerBtn.setVisible(val);
			break;
		case Supplier:
			supplierBtn.setVisible(val);
			break;
		case CEO:
			ceoBtn.setVisible(val);
			break;
		case EmployerHR:
			employerHRBtn.setVisible(val);
			break;
		}
	}

	/**
	 * Setting the avatar image of the user.
	 */
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

	@FXML
	void profileBtnClicked(MouseEvent event) {

	}

	@FXML
	void restaurantBtnClicked(MouseEvent event) {

	}

}
