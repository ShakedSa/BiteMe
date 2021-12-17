package Controls;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.util.ResourceBundle;

import Entities.User;
import Enums.UserType;
import client.ClientGUI;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class registerEmployerAsLegacyController implements Initializable {

	public final UserType type = UserType.EmployerHR;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private TextField employerCodeTxtField;

	@FXML
	private Text employerHRPanelBtn;

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
	private Label registerBtn;

	@FXML
	private Text hrCompanyName;

	@FXML
	private ImageView VImage;

	@FXML
	private Text errorMsg;

	@FXML
	private Text successMsg;

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	@FXML
	void profileBtnClicked(MouseEvent event) {
		clearPage();
		router.showProfile();
	}

	@FXML
	void registerBtnClicked(MouseEvent event) {
		VImage.setVisible(false);
		successMsg.setVisible(false);
		// checking if input exists
		if (!checkInput()) {
			return;
		}

		// Creating request to create a New BusinessCustomer
		String hrUserName = ((User) ClientGUI.client.getUser().getServerResponse()).getUserName();
		String employerName = ((User) ClientGUI.client.getUser().getServerResponse()).getOrganization();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				ClientGUI.client.createNewBusinessCustomer(hrUserName, employerCodeTxtField.getText().trim(),
						employerName);
				synchronized (ClientGUI.monitor) {
					try {
						ClientGUI.monitor.wait();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
			}
		});
		t.start();

		try {
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		// checking server response - if there is no response:
		if (ClientGUI.client.getLastResponse() == null) {
			errorMsg.setText("Somthing went worng, Please try again");
			return;
		}
		if (ClientGUI.client.getLastResponse().getMsg().equals("Already Registered")) {
			errorMsg.setText(ClientGUI.client.getLastResponse().getMsg());
			return;
		}
		if (ClientGUI.client.getLastResponse().getMsg().equals("Success")) {
			VImage.setVisible(true);
			successMsg.setVisible(true);
		}
		// after creating businessCustomer disabling the option to create more
		// (preventing a DB span abuse by clients)

	}

	private boolean checkInput() {
		String employerCode = employerCodeTxtField.getText().trim();
		if (employerCode.isEmpty()) {
			errorMsg.setText("Please enter employer company Code");
			return false;
		}
		errorMsg.setText("");
		return true;
	}

	@FXML
	void returnToEmployerHRPanel(MouseEvent event) {
		clearPage();
		router.returnToEmployerHRPanel(event);
	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		clearPage();
		router.changeSceneToHomePage();
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
		router.setRegisterEmployerAsLegacyController(this);
		setStage(router.getStage());
		VImage.setVisible(false);
		successMsg.setVisible(false);
		hrCompanyName.setText(((User) ClientGUI.client.getUser().getServerResponse()).getOrganization());
		
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
	
	private void clearPage() {
		employerCodeTxtField.clear();
		VImage.setVisible(false);
		successMsg.setVisible(false);
		errorMsg.setText("");
	}

}
