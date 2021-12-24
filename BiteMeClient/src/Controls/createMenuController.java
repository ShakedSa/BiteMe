package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.Component;
import Entities.Product;
import Entities.ServerResponse;
import Entities.User;
import Enums.TypeOfProduct;
import Enums.UserType;
import client.ClientGUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class createMenuController implements Initializable {

	public final UserType type = UserType.Supplier;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private ImageView VImage;

	@FXML
	private Text menuCreatedSuccessfullyTxt;

	@FXML
	private Circle addNewItemPlus;

	@FXML
	private Text addNewItemTxt;

	@FXML
	private Rectangle avatar;

	@FXML
	private ImageView createMenuImage;

	@FXML
	private Text createMenuTxt;

	@FXML
	private Text homePageBtn;

	@FXML
	private ImageView leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Text profileBtn;

	@FXML
	private Text supplierPanelBtn;

	@FXML
	private Text errorMsg;

	@FXML
	private TableView<Product> menuTable;
	
	@FXML
	private TableColumn<Product, ArrayList<Component>> table_Components;

	@FXML
	private TableColumn<Product, String> table_Description;

	@FXML
	private TableColumn<Product, String> table_DishName;

	@FXML
	private TableColumn<Product, Float> table_Price;

	@FXML
	private TableColumn<Product, TypeOfProduct> table_Type;
	
	private User user = (User) ClientGUI.client.getUser().getServerResponse();
	private String restaurant = user.getOrganization();

	@FXML
	void addNewItemClicked(MouseEvent event) {
		if (router.getAddNewItemToNewMenuController() == null) {
			AnchorPane mainContainer;
			addNewItemToNewMenuController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeAddNewItemToNewMenuPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				//Menu();//???????????????????????????????????????????????????????
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Add New Item To Menu");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			//router.getCreateMenuController().Menu();//??????????????????????????????????????
			stage.setTitle("BiteMe - Add New Item To Menu");
			stage.setScene(router.getAddNewItemToNewMenuController().getScene());
			stage.show();
		}
	}
	
	public void Menu() {
		ClientGUI.client.getRestaurantMenu(restaurant);
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

		ServerResponse sr = ClientGUI.client.getLastResponse();
		@SuppressWarnings("unchecked")
		// get the server response- list of product (menu)
		ArrayList<Product> response = (ArrayList<Product>) sr.getServerResponse();
		//
		if(response != null) {
			
		}
		setTable(response);
		return;
	}
	

	@FXML
	void createMenuClicked(MouseEvent event) {
		// need to check server response
		if (checkServerResponse()) {
			VImage.setVisible(true);
			menuCreatedSuccessfullyTxt.setVisible(true);
		}
	}

	private boolean checkServerResponse() {
//    	retVal = servaerResponse;
//    	if(retVal == null) {
//			errorMsg.setText("No items checked into menu");
//			return false;
//		}
//		errorMsg.setText(""); 
		return true;
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
	void returnToSupplierPanel(MouseEvent event) {
		router.returnToSupplierPanel(event);
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
		router.setCreateMenuController(this);
		setStage(router.getStage());
		VImage.setVisible(false);
		menuCreatedSuccessfullyTxt.setVisible(false);
		//createTable();
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

	/**
	 * Creating the view table columns.
	 */
//	public void createTable() {
//		TableColumn<Product, TypeOfProduct> typeCol = new TableColumn<Product, TypeOfProduct>("Type");
//		typeCol.setCellValueFactory(new PropertyValueFactory<Product, TypeOfProduct>("type"));
//		menuTable.getColumns().add(typeCol);
//		TableColumn<Product, String> nameCol = new TableColumn<Product, String>("Dish Name");
//		nameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("restaurantName"));
//		menuTable.getColumns().add(nameCol);
//		TableColumn<Product, ArrayList<Component>> componentCol = new TableColumn<Product, ArrayList<Component>>(
//				"Components");
//		componentCol.setCellValueFactory(new PropertyValueFactory<Product, ArrayList<Component>>("components"));
//		menuTable.getColumns().add(componentCol);
//		TableColumn<Product, Float> priceCol = new TableColumn<Product, Float>("Price");
//		priceCol.setCellValueFactory(new PropertyValueFactory<Product, Float>("price"));
//		menuTable.getColumns().add(priceCol);
//		TableColumn<Product, String> descriptionCol = new TableColumn<Product, String>("Description");
//		descriptionCol.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
//		menuTable.getColumns().add(descriptionCol);
//		menuTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//	}
	
	
	// set table columns and values
	private void setTable(ArrayList<Product> menu) {
		table_Type.setCellValueFactory(new PropertyValueFactory<>("type"));
		table_DishName.setCellValueFactory(new PropertyValueFactory<>("dishName"));
		table_Components.setCellValueFactory(new PropertyValueFactory<>("components"));
		table_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
		table_Description.setCellValueFactory(new PropertyValueFactory<>("description"));
		menuTable.setItems(getProduct(menu));
		menuTable.setEditable(true);
	}

	// change arrayList to ObservableList
	private ObservableList<Product> getProduct(ArrayList<Product> list) {
		ObservableList<Product> menu = FXCollections.observableArrayList();
		for (Product product : list) {
			Product p = new Product(product.getRestaurantName(), product.getType(), product.getDishName(),
					product.getComponents(), product.getPrice(), product.getDescription());
			menu.add(p);
		}
		return menu;
	}

}
