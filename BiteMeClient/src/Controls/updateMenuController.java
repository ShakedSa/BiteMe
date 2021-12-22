package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import Controls.confirmBusinessAccountController.CustomerPlusBudget;
import Entities.BusinessCustomer;
import Entities.Component;
import Entities.Customer;
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

public class updateMenuController implements Initializable {

	public final UserType type = UserType.Supplier;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private ImageView VImage;

	@FXML
	private Text menuUpdatedSuccessfullyTxt;

	@FXML
	private Circle addNewItemPlus;
	
    @FXML
    private Circle deleteItemPlus;

    @FXML
    private Text deleteItemTxt;

	@FXML
	private Text addNewItemTxt;

	@FXML
	private Rectangle avatar;

	@FXML
	private ImageView editItemImage;

	@FXML
	private Text editItemTxt;

	@FXML
	private Text homePageBtn;

	@FXML
	private ImageView leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private Text profileBtn;

	@FXML
	private Text saveTxt;

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

		// get components for each product
//		for (int i = 0; i < response.size(); i++) {
//			ClientGUI.client.componentsInProduct(restaurant, response.get(i).getDishName());
//			Thread t1 = new Thread(new Runnable() {
//				@Override
//				public void run() {
//					synchronized (ClientGUI.monitor) {
//						try {
//							ClientGUI.monitor.wait();
//						} catch (Exception e) {
//							e.printStackTrace();
//							return;
//						}
//					}
//				}
//			});
//			t1.start();
//			try {
//				t1.join();
//			} catch (Exception e) {
//				e.printStackTrace();
//				return;
//			}
//
//			ServerResponse sr1 = ClientGUI.client.getLastResponse();
//			@SuppressWarnings("unchecked")
//			// get the server response- list of components
//			ArrayList<Component> components = (ArrayList<Component>) sr1.getServerResponse();
//			// set components in each product
//			response.get(i).setComponents(components);
//		}
		setTable(response);
		return;
	}

	@FXML
	void addNewItemClicked(MouseEvent event) {
		if (router.getAddNewItemController() == null) {
			AnchorPane mainContainer;
			addNewItemController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeAddNewItemToMenuPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
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
			stage.setTitle("BiteMe - Add New Item To Menu");
			stage.setScene(router.getAddNewItemController().getScene());
			stage.show();
		}
	}

	@FXML
	void editItemClicked(MouseEvent event) {
		if (router.getEditMenuItemController() == null) {
			AnchorPane mainContainer;
			editMenuItemController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeEditMenuItemPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Edit Menu Item");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - Edit Menu Item");
			stage.setScene(router.getEditMenuItemController().getScene());
			stage.show();
		}
	}
	
    @FXML
    void deleteItemTxt(MouseEvent event) {

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

	@FXML
	void saveClicked(MouseEvent event) {
		// need to check server response
		if (checkServerResponse()) {
			VImage.setVisible(true);
			menuUpdatedSuccessfullyTxt.setVisible(true);
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

	/**
	 * Setting the avatar image of the user.
	 */
	public void setAvatar() {
		router.setAvatar(avatar);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		router = Router.getInstance();
		router.setUpdateMenuController(this);
		setStage(router.getStage());
		VImage.setVisible(false);
		menuUpdatedSuccessfullyTxt.setVisible(false);
		// createTable();
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
//	

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
		list.forEach(p -> {
			if (p.getDescription() == null || p.getDescription().equals("")) {
				p.setDescription("");
			}
			if (p.getComponents() == null || p.getComponents().size() == 0) {
				p.setComponents(new ArrayList<>(Arrays.asList(new Component("None"))));
			}
		});
		menu.addAll(list);
		return menu;
	}
}
