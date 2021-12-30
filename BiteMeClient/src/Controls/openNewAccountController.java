package Controls;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Entities.NewAccountUser;
import Entities.NewAccountUser;
import Entities.ServerResponse;
import Entities.User;
import Enums.BranchName;
import Enums.Status;
import Enums.UserType;
import Util.InputValidation;
import client.ClientGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class openNewAccountController implements Initializable{

	public final UserType type= UserType.BranchManager;
	private Router router;
	private Stage stage;
	private Scene scene;

   

	  @FXML
	    private ImageView approvalBtn1;

	    @FXML
	    private Label next;

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

    private ObservableList<String> list;
    
    private ObservableList<String> list1;
    
    
    
    
  //show table with employers that are waiting for approval
  	public void initTable(){
  		id="";
  		//wait for response
	  		ClientGUI.client.searchForNewUsers();
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
  		ArrayList<NewAccountUser> response = (ArrayList<NewAccountUser>) sr.getServerResponse();
  		//check if business customers are waiting for approval
  		if(response.size() == 0)
  		{
  			msg.setText("Currently no accounts are needed");
  			msg.setVisible(true);
  			next.setVisible(false);
  			next.setVisible(false);
  			return;
  		}
  		setTable( response);
  			msg.setText("Some employers are waiting for your approval");
  			msg.setVisible(true);
  			instructions.setVisible(true);		
  	}
  	
  	
  	//set table columns and values
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
  	
  	
	//change arrayList to ObservableList
	private ObservableList<NewAccountUser> getCustomer(ArrayList<NewAccountUser> list2) {
		ObservableList<NewAccountUser> users = FXCollections.observableArrayList();
		for (NewAccountUser customer : list2) {
			NewAccountUser customerPlusBudget = new NewAccountUser(customer.getUserName(),
					customer.getFirstName(), customer.getLastName(), customer.getId(),
					customer.getEmail(), customer.getPhone());
			users.add(customerPlusBudget);
		}
		return users;
	}
  	
  	
      @FXML
      void copyTableData(MouseEvent event) {
      	if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
              id = (approvalTable.getSelectionModel().getSelectedItem().getId());
              username = (approvalTable.getSelectionModel().getSelectedItem().getUserName());
              fName = (approvalTable.getSelectionModel().getSelectedItem().getFirstName());
              lName = (approvalTable.getSelectionModel().getSelectedItem().getLastName());
      	}
      }
      
      @FXML
      void approvalClicked(MouseEvent event) {
    	  if(id.equals("")) {
  			msg.setVisible(true);
  			msg.setText("Please double click on a user!");
  			return;
  			}
    	  if (router.getOpenNewAccountFinalController() == null) {
  			AnchorPane mainContainer;
  			openNewAccountFinalController controller;
  			try {
  				FXMLLoader loader = new FXMLLoader();
  				loader.setLocation(getClass().getResource("../gui/bitemeOpenNewAccountFinalPage.fxml"));
  				mainContainer = loader.load();
  				controller = loader.getController();
  				controller.setAvatar();
  				controller.initScene(id,username,fName,lName);
  				controller.setPrevScene(scene);
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
  			router.getOpenNewAccountFinalController().initScene(id,username,fName,lName);
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
	//	router.setArrow(leftArrowBtn, -90);
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

