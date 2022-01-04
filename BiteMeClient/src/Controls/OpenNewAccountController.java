package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.NewAccountUser;
import Entities.ServerResponse;
import Enums.UserType;
import client.ClientGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class OpenNewAccountController implements Initializable { 

	public final UserType type = UserType.BranchManager;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private ImageView approvalBtn1;

	@FXML
	private Label next;

	@FXML
	private Rectangle leftArrowBtn;

	@FXML
	private Rectangle avatar;

	@FXML
	private Text managerPanelBtn;

	@FXML
	private Text homePageBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Text profileBtn;

	@FXML
	private Text msg;

	@FXML
	private Text instructions;

	@FXML
	private TableView<NewAccountUser> approvalTable;

	@FXML
	private TableColumn<NewAccountUser, String> UserName;

	@FXML
	private TableColumn<NewAccountUser, String> FirstName;

	@FXML
	private TableColumn<NewAccountUser, String> LastName;

	@FXML
	private TableColumn<NewAccountUser, String> ID;

	@FXML
	private TableColumn<NewAccountUser, String> Email;

	@FXML
	private TableColumn<NewAccountUser, String> Phone;

	private String id = "";
	
	private String username = "";
	
	private String fName = "";
	
	private String lName = "";
	
	private ObservableList<NewAccountUser> users = FXCollections.observableArrayList();

	/**
	 * init the table with information of users thats needs a private/business account 
	 * modifies: approvalTable
	 */
	public void initTable() {
		id = "";
		//send a request to clientUI - get users from the db 
		ClientGUI.getClient().findUsersInNeedOfAccount();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (ClientGUI.getMonitor()) {
					try {
						ClientGUI.getMonitor().wait();
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
		// handle server response
		ServerResponse sr = ClientGUI.getClient().getLastResponse();
		@SuppressWarnings("unchecked")
		// get the server response- users that are in need of an account
		ArrayList<NewAccountUser> response = (ArrayList<NewAccountUser>) sr.getServerResponse();
		// check if there are any such users
		if (response.size() == 0) {
			msg.setText("Currently no accounts are needed");
			msg.setVisible(true);
			next.setVisible(false);
			next.setVisible(false);
			return;
		}
		msg.setVisible(false);
		next.setVisible(true);
		setTable(response);
		instructions.setVisible(true);
	}

	
	/**
	 * set the table rows and columns
	 * @param list = userName, firstName, lastName, id, email, phone
	 */
	private void setTable(ArrayList<NewAccountUser> list) {
		UserName.setCellValueFactory(new PropertyValueFactory<>("userName"));
		FirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		LastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		ID.setCellValueFactory(new PropertyValueFactory<>("id"));
		Email.setCellValueFactory(new PropertyValueFactory<>("email"));
		Phone.setCellValueFactory(new PropertyValueFactory<>("phone"));
		approvalTable.setItems(getCustomer(list));
		approvalTable.setEditable(true);
	}

	// change arrayList to ObservableList
	/**
	 * converts an ArrayList to an ObservableList
	 * @param list2
	 * @return ObservableList of the Class: NewAccountUser
	 */
	private ObservableList<NewAccountUser> getCustomer(ArrayList<NewAccountUser> list2) {
		users = FXCollections.observableArrayList();
		for (NewAccountUser customer : list2) {
			NewAccountUser customerPlusBudget = new NewAccountUser(customer.getUserName(), customer.getFirstName(),
					customer.getLastName(), customer.getId(), customer.getEmail(), customer.getPhone());
			users.add(customerPlusBudget);
		}
		return users;
	}
	
	
	/**
	 * resets certain components in the screen
	 */
	public void reset() {
		approvalTable.refresh();
    	users.removeAll(users);
    	msg.setVisible(false);
	}

	
	/**
	 * when a table row is double clicked- save the row data
	 * modifies: id, username, fName, lName
	 * @param event
	 */
	@FXML
	void copyTableData(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
			id = (approvalTable.getSelectionModel().getSelectedItem().getId());
			username = (approvalTable.getSelectionModel().getSelectedItem().getUserName());
			fName = (approvalTable.getSelectionModel().getSelectedItem().getFirstName());
			lName = (approvalTable.getSelectionModel().getSelectedItem().getLastName());
		}
	}

	
	/**
	 * after a user was double clicked, changes to page into the next step
	 * @param event
	 */
	@FXML
	void approvalClicked(MouseEvent event) {
		if (id.equals("")) {
			msg.setVisible(true);
			msg.setText("Please double click on a user!");
			return;
		}
		if (router.getOpenNewAccountFinalController() == null) {
			AnchorPane mainContainer;
			OpenNewAccountFinalController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/bitemeOpenNewAccountFinalPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.initScene(id, username, fName, lName);
				controller.setPrevScene(scene);
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Open New Account");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			router.getOpenNewAccountFinalController().initScene(id, username, fName, lName);
			stage.setTitle("BiteMe - Open New Account");
			stage.setScene(router.getOpenNewAccountFinalController().getScene());
			stage.show();
		}

	}


	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		router.changeSceneToHomePage();
	}

	@FXML
	void returnToManagerPanel(MouseEvent event) {
		router.returnToManagerPanel(event);
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
		router.setOpenNewAccountController(this);
		router.setArrow(leftArrowBtn, -90);
		setStage(router.getStage());
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

}
