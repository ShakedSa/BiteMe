package Controls;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.BusinessCustomer;
import Entities.ServerResponse;
import Enums.UserType;
import client.ClientGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class authorizedEmployerApprovalController implements Initializable {

	public final UserType type = UserType.BranchManager;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private Label approvalBtn;

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
	private Text noApprovals;

	@FXML
	private TableView<BusinessCustomer> approvalTable;

	@FXML
	private TableColumn<BusinessCustomer, String> EmployerCode;

	@FXML
	private TableColumn<BusinessCustomer, String> EmployeCompanyName;

	@FXML
	private TableColumn<BusinessCustomer, Integer> IsApproved;

	@FXML
	private TableColumn<BusinessCustomer, String> HRname;

	@FXML
	private ImageView approvalSuccsess;

	@FXML
	private Text approvalSuccsess1;

	@FXML 
	private Text instructions;

	private String employerCode = "";

	
	
	/**init the table with data on the employers that are waiting for approval
	 * modifies: approvalTable
	 */
	public void initTable() {
		ClientGUI.client.checkForApprovals();
		// wait for response
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
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
		// handle server response
		ServerResponse sr = ClientGUI.client.getLastResponse();
		@SuppressWarnings("unchecked")
		// get the server response- Business employers that needs approval
		ArrayList<BusinessCustomer> response = (ArrayList<BusinessCustomer>) sr.getServerResponse();
		// check if no one is waiting for approval
		if (response.size() == 0) {
			noApprovals.setText("No employers waiting for approvallll");
			noApprovals.setVisible(true);
			approvalBtn.setVisible(false);
			instructions.setVisible(false);
			noApprovals.setVisible(true);
			approvalSuccsess.setVisible(false);
			approvalSuccsess1.setVisible(false);
			return;
		}
		setTable(response);
		noApprovals.setText("Some employers are waiting for your approval");
		noApprovals.setVisible(true);
		approvalSuccsess.setVisible(false);
		approvalSuccsess1.setVisible(false);
		approvalBtn.setVisible(true);
		instructions.setVisible(true);

	}

	
	
	/**prepare the table culomns
	 * modifies:approvalTable
	 * @param list
	 */
	private void setTable(ArrayList<BusinessCustomer> list) {
		EmployerCode.setCellValueFactory(new PropertyValueFactory<>("EmployerCode"));
		EmployeCompanyName.setCellValueFactory(new PropertyValueFactory<>("EmployeCompanyName"));
		IsApproved.setCellValueFactory(new PropertyValueFactory<>("IsApproved"));
		HRname.setCellValueFactory(new PropertyValueFactory<>("HRname"));
		approvalTable.setItems(getCustomer(list));
		approvalTable.setEditable(true);
	}

	
	
	/**convert  arrayList to an ObservableList
	 * @param list = employerCode, employeCompanyName, isApproved, hRname
	 * @return ObservableList of type 'BusinessCustomer'
	 */
	private ObservableList<BusinessCustomer> getCustomer(ArrayList<BusinessCustomer> list) {
		ObservableList<BusinessCustomer> customers = FXCollections.observableArrayList();
		for (BusinessCustomer customer : list) {
			BusinessCustomer customerPlusBudget = new BusinessCustomer(customer.getEmployerCode(),
					customer.getEmployeCompanyName(), customer.getIsApproved(), customer.getHRname());
			customers.add(customerPlusBudget);
		}
		return customers;
	}

	/**after a table's row is double clicked, save the row values
	 * @param event
	 * modifies: employerCode
	 */
	@FXML
	void copyTableData(MouseEvent event) {
		if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
			employerCode = (approvalTable.getSelectionModel().getSelectedItem().getEmployerCode());
			approvalSuccsess.setVisible(false);
			approvalSuccsess1.setVisible(false);
		}
	}

	
	/**send a request to Client UI inorder to approve the selected supplier
	 * @param event
	 */
	@FXML
	void approvalClicked(MouseEvent event) {
		//check that a row has been selected from the table
		if (employerCode.equals("")) {
			noApprovals.setVisible(true);
			noApprovals.setText("Please double click on a user!");
			approvalSuccsess.setVisible(false);
			approvalSuccsess1.setVisible(false);
			return;
		}
		//send request to Client UI
		ClientGUI.client.employerApproval(employerCode);
		BusinessCustomer selectedItem = approvalTable.getSelectionModel().getSelectedItem();
		approvalTable.getItems().remove(selectedItem);
		approvalSuccsess.setVisible(true);
		approvalSuccsess1.setVisible(true);
		noApprovals.setVisible(false);
		employerCode = "";
	}
	
	
	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
	}

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		router.changeSceneToHomePage();
	}

	@FXML
	void returnToManagerPanel(MouseEvent event) {
		router.returnToManagerPanel(event);
	}
	
	/**change the scene into the app homepage 
	 * @param val
	 */
	void changeSceneToHomePage(boolean val) {
		stage.setTitle("BiteMe - HomePage");
		stage.setScene(router.getHomePageController().getScene());
		stage.show();
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
		router.setAuthorizedEmployerApprovalController(this);
		setStage(router.getStage());
		router.setArrow(leftArrowBtn, -90);
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
