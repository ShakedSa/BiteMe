package Controls;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.Customer;
import Entities.User;
import Entities.W4CCard;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class confirmBusinessAccountController implements Initializable {

	public final UserType type = UserType.EmployerHR;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private TableView<CustomerPlusBudget> customerTable;

	@FXML
	private TableColumn<CustomerPlusBudget, Float> table_Dbudget;

	@FXML
	private TableColumn<CustomerPlusBudget, String> table_FirstName;

	@FXML
	private TableColumn<CustomerPlusBudget, String> table_ID;

	@FXML
	private TableColumn<CustomerPlusBudget, String> table_LastName;

	@FXML
	private TableColumn<CustomerPlusBudget, Float> table_Mbudget;

	@FXML
	private TableColumn<CustomerPlusBudget, String> table_Role;

	@FXML
	private Rectangle avatar;

	@FXML
	private Text employerHRPanelBtn;

	@FXML
	private Text homePageBtn;

	@FXML
	private Rectangle leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Text profileBtn;

	@FXML
	private Label confirmBtn;

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

	@SuppressWarnings("unchecked")
	@FXML
	void confirmBtnClicked(MouseEvent event) {
		clearPage();
		// return the ID of the selected customer on gui
		try {
			String customerID = customerTable.getSelectionModel().getSelectedItem().getId();
			String employerName = ((User) ClientGUI.client.getUser().getServerResponse()).getOrganization();
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					ClientGUI.client.approveCustomerAsBusiness(employerName, customerID);
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

			if (ClientGUI.client.getLastResponse() == null)
				errorMsg.setText("No Response");

			if (ClientGUI.client.getLastResponse().getMsg().equals("Update Failed"))
				errorMsg.setText(ClientGUI.client.getLastResponse().getMsg());

			if (ClientGUI.client.getLastResponse().getMsg().equals("Success")) {
				setTable((ArrayList<Customer>) ClientGUI.client.getLastResponse().getServerResponse());
				VImage.setVisible(true);
				successMsg.setVisible(true);
			}
		} catch (NullPointerException e) {
			errorMsg.setText("No Customer is selected");
		}
	}
	private void initTable() {
		table_ID.setCellValueFactory(new PropertyValueFactory<>("id"));
		table_FirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		table_LastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		table_Role.setCellValueFactory(new PropertyValueFactory<>("role"));
		table_Mbudget.setCellValueFactory(new PropertyValueFactory<>("monthlyBudget"));
		table_Dbudget.setCellValueFactory(new PropertyValueFactory<>("dailyBudget"));
		customerTable.setEditable(true);
	}
	public void setTable() {
		customerTable.setItems(getCustomer());
	}

	private void setTable(ArrayList<Customer> list) {	
		customerTable.setItems(getCustomer(list));	
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
		router.setConfirmBusinessAccountController(this);
		setStage(router.getStage());
		initTable();
		VImage.setVisible(false);
		successMsg.setVisible(false);
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

	private void clearPage() {
		VImage.setVisible(false);
		successMsg.setVisible(false);
		errorMsg.setText("");
	}

	@SuppressWarnings("unchecked")
	private ArrayList<Customer> CustomerAndbudget() {
		String employerName = ((User) ClientGUI.client.getUser().getServerResponse()).getOrganization();
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				ClientGUI.client.selectCustomerAndbudget(employerName);
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
			return null;
		}

		if (ClientGUI.client.getLastResponse() != null) {
			if (ClientGUI.client.getLastResponse().getMsg().equals("Success")) {
				return (ArrayList<Customer>) ClientGUI.client.getLastResponse().getServerResponse();
			}

		}
		return null;
	}

	private ObservableList<CustomerPlusBudget> getCustomer() {
		ObservableList<CustomerPlusBudget> customers = FXCollections.observableArrayList();
		ArrayList<Customer> list = CustomerAndbudget();
		for (Customer customer : list) {
			CustomerPlusBudget customerPlusBudget = new CustomerPlusBudget(customer);
			customers.add(customerPlusBudget);
		}
		return customers;
	}

	private ObservableList<CustomerPlusBudget> getCustomer(ArrayList<Customer> list) {
		ObservableList<CustomerPlusBudget> customers = FXCollections.observableArrayList();
		for (Customer customer : list) {
			CustomerPlusBudget customerPlusBudget = new CustomerPlusBudget(customer);
			customers.add(customerPlusBudget);
		}
		return customers;
	}

	protected class CustomerPlusBudget {

		private String id;
		private String firstName;
		private String lastName;
		private String role;
		private Float monthlyBudget;
		private Float dailyBudget;

		public CustomerPlusBudget(Customer customer) {
			id = customer.getId();
			firstName = customer.getFirstName();
			lastName = customer.getLastName();
			role = customer.getRole();
			monthlyBudget = customer.getW4c().getMonthlyBudget();
			dailyBudget = customer.getW4c().getDailyBudget();
		}

		public String getFirstName() {
			return firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public String getRole() {
			return role;
		}

		public Float getMonthlyBudget() {
			return monthlyBudget;
		}

		public Float getDailyBudget() {
			return dailyBudget;
		}

		public String getId() {
			return id;
		}

	}

}
