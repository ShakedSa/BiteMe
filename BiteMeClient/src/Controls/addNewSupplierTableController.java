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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class addNewSupplierTableController implements Initializable {
	
	public final UserType type= UserType.BranchManager;
	private Router router;
	private Stage stage;
	private Scene scene;

    @FXML
    private Label next;

    @FXML
    private Rectangle avatar;

    @FXML
    private Text managerPanelBtn;
    
    @FXML
	private Rectangle leftArrowBtn;

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
    
    private String fName = "";
    
    private String lName = "";
    
    private String userName = "";
    
    
  
  	/**fill and show a table with users that could be added as suppliers
  	 * modifies: approvalTable
  	 */
  	public void initTable(){
  		ClientGUI.getClient().searchForNewUsers();
  		//wait for response
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
  		//handle server response
  		ServerResponse sr = ClientGUI.getClient().getLastResponse();
  		@SuppressWarnings("unchecked")
  		//get the server response- users without any permmisions yet
  		ArrayList<NewAccountUser> response = (ArrayList<NewAccountUser>) sr.getServerResponse();
  		//check if any suck users are exists
  		if(response.size() == 0)
  		{
  			msg.setText("Currently no accounts are needed");
  			msg.setVisible(true);
  			next.setVisible(false);
  			next.setVisible(false);
  			return;
  		}
  		setTable( response);
  			instructions.setVisible(true);		
  	}
  	
  	
  	/**set the table columns and values
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
  	
  	
	
	/**convert  arrayList to ObservableList
	 * @param list2 = = userName, firstName, lastName, id, email, phone
	 * @return ObservableList of NewAccountUser 
	 */
	private ObservableList<NewAccountUser> getCustomer(ArrayList<NewAccountUser> list2) {
		ObservableList<NewAccountUser> users = FXCollections.observableArrayList();
		//itarate through the arrayList
		for (NewAccountUser customer : list2) {
			NewAccountUser customerPlusBudget = new NewAccountUser(customer.getUserName(),
					customer.getFirstName(), customer.getLastName(), customer.getId(),
					customer.getEmail(), customer.getPhone());
			users.add(customerPlusBudget);
		}
		return users;
	}
  	
  	
      /**after a row is double clicked, save the row values
     * @param event
     * modifies: id, fName, lName, userName
     */
    @FXML
      void copyTableData(MouseEvent event) {
      	if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
              id = (approvalTable.getSelectionModel().getSelectedItem().getId());
              fName = (approvalTable.getSelectionModel().getSelectedItem().getFirstName());
              lName = (approvalTable.getSelectionModel().getSelectedItem().getLastName());
              userName = (approvalTable.getSelectionModel().getSelectedItem().getUserName());
      	}
      }
      
    
    /**delete user info fields when leaving and stepping the page
     * modifies: id, fName, lName, userName
     */
    public void resetInfo() {
    	id = "";
    	fName = "";
    	lName = "";
    }
    
    
    /**change the page into the final page of adding a new supplier
     * @param event
     */
    @FXML
    void approvalClicked(MouseEvent event) {
    	 if(id.equals("")) {
   			msg.setVisible(true);
   			msg.setText("Please double click on a user!");
   			return;
   			}
     	  if (router.getAddNewSupplierController() == null) {
   			AnchorPane mainContainer;
   			addNewSupplierController controller;
   			try {
   				FXMLLoader loader = new FXMLLoader();
   				loader.setLocation(getClass().getResource("../gui/bitemeAddNewSupplierPage.fxml"));
   				mainContainer = loader.load();
   				controller = loader.getController();
   				controller.setAvatar();
   				controller.reSetTheScreen();
   				controller.setUserInfo(fName, lName, userName);
   				Scene mainScene = new Scene(mainContainer);
   				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
   				controller.setScene(mainScene);
   				stage.setTitle("BiteMe - Open New Account");
   				stage.setScene(mainScene);
   				stage.show();
   			} catch (IOException e) {
   				e.printStackTrace();
   				return;
   			}
   		} else {
   			router.getAddNewSupplierController().reSetTheScreen();
   			router.getAddNewSupplierController().setUserInfo(fName, lName, userName);
   			stage.setTitle("BiteMe - Open New Account");
   			stage.setScene(router.getAddNewSupplierController().getScene());
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
		router.setAddNewSupplierTableController(this);
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
		this.stage=stage;
	}
}
