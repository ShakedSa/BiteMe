package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.BusinessCustomer;
import Entities.ServerResponse;
import Enums.UserType;
import Util.InputValidation;
import client.ClientGUI;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class authorizedEmployerApprovalController implements Initializable{

	public final UserType type= UserType.BranchManager;
	private Router router;
	private Stage stage;
	private Scene scene;
	
	 @FXML
	 private Label approvalBtn;

	 @FXML
	 private ImageView leftArrowBtn;

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
	    private Text employerCode;

	    @FXML
	    private TextField employerCodeTxtField;

	    @FXML
	    private Text noApprovals;

	    @FXML
	    private Text employerCompanyName;

	    @FXML
	    private TextField employerCompanyNameTxtField;

	    @FXML
	    private TableView<BusinessCustomer> approvalTable;
	    
	    @FXML
		private TableColumn<BusinessCustomer, String> EmployerCode;

		@FXML
		private TableColumn<BusinessCustomer,String> EmployeCompanyName;

		@FXML
		private TableColumn<BusinessCustomer, Integer> IsApproved;

		@FXML
		private TableColumn<BusinessCustomer, String> HRname;
		
		@FXML
		private ImageView approvalSuccsess;
		
		@FXML
		private Text approvalSuccsess1;
		
		private ArrayList<BusinessCustomer> needApproval;

		
	@FXML
    void approvalClicked(MouseEvent event) {
		boolean ableToAprrove = false;
		//check user input
		if(!checkFields())
			return;
		for(BusinessCustomer bc: needApproval) {
			if(bc.getEmployeCompanyName().equals(employerCompanyNameTxtField.getText()) &&
					bc.getEmployerCode().equals(employerCodeTxtField.getText()))
				ableToAprrove = true;
		}
		if(!ableToAprrove) {
			noApprovals.setVisible(true);
			noApprovals.setText("Unable to improve this Employer");
			return;
		}
		//user input does requiers an approval, make the approval
		ClientGUI.client.employerApproval(employerCodeTxtField.getText());
		approvalSuccsess.setVisible(true);
		approvalSuccsess1.setVisible(true);
		noApprovals.setVisible(false);
	}
	
	private boolean checkFields() {
		if( InputValidation.checkSpecialCharacters(employerCodeTxtField.getText())) {
    		noApprovals.setVisible(true);
    		noApprovals.setText("Employer code can't contain special characters!");
    		return false;
    	}
    	if(employerCodeTxtField.getText().length() == 0) {
    		noApprovals.setVisible(true);
    		noApprovals.setText("Please fill an employer code!");
    		return false;
    	}
    	if( InputValidation.checkSpecialCharacters(employerCompanyNameTxtField.getText())) {
    		noApprovals.setVisible(true);
    		noApprovals.setText("Employer code can't contain special characters!");
    		return false;
    	}
    	if(employerCompanyNameTxtField.getText().length() == 0) {
    		noApprovals.setVisible(true);
    		noApprovals.setText("Please fill a company name!");
    		return false;
    	}
    	return true;	
	}
	
	
	//show table with employers that are waiting for approval
	public void initTable(){
		ClientGUI.client.checkForApprovals();
		//wait for response
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
		//handle server response
		ServerResponse sr = ClientGUI.client.getLastResponse();
		@SuppressWarnings("unchecked")
		//get the server response- Business employers that needs approval
		ArrayList<BusinessCustomer> response = (ArrayList<BusinessCustomer>) sr.getServerResponse();
		//check if business customers are waiting for approval
		if(response.size() == 0)
		{
			noApprovals.setVisible(true);
			return;
		}
		needApproval = response;
		setTable( response);
		ObservableList<BusinessCustomer> list = approvalTable.getItems();
		if(list.isEmpty())
			noApprovals.setVisible(true);
		else {
			noApprovals.setText("Some employers are waiting for your approval");
			noApprovals.setVisible(true);
		}
	}
	
	//set table columns and values
	private void setTable(ArrayList<BusinessCustomer> list) {
		EmployerCode.setCellValueFactory(new PropertyValueFactory<>("EmployerCode"));
		EmployeCompanyName.setCellValueFactory(new PropertyValueFactory<>("EmployeCompanyName"));
		IsApproved.setCellValueFactory(new PropertyValueFactory<>("IsApproved"));
		HRname.setCellValueFactory(new PropertyValueFactory<>("HRname"));
		approvalTable.setItems(getCustomer(list));
		approvalTable.setEditable(true);
	}
	
	//change arrayList to ObservableList
	private ObservableList<BusinessCustomer> getCustomer(ArrayList<BusinessCustomer> list) {
		ObservableList<BusinessCustomer> customers = FXCollections.observableArrayList();
		for (BusinessCustomer customer : list) {
			BusinessCustomer customerPlusBudget = new BusinessCustomer(customer.getEmployerCode(),
					customer.getEmployeCompanyName(), customer.getIsApproved(), customer.getHRname());
			customers.add(customerPlusBudget);
		}
		return customers;
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
	}

    
	public void setScene(Scene scene) {
		this.scene = scene;
	}

	public Scene getScene() {
		return scene;
	}

	public void setStage(Stage stage) {
		this.stage=stage;
	}

}

