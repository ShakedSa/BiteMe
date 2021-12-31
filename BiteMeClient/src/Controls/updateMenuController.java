package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import Entities.Component;
import Entities.Product;
import Entities.ServerResponse;
import Entities.User;
import Enums.TypeOfProduct;
import Enums.UserType;
import client.ClientGUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Natali 
 * This class display a table - list of all the products of a
 * specific restaurant/supplier It includes 3 buttons - add item, edit
 * item and delete item from menu
 */
public class updateMenuController implements Initializable {

	public final UserType type = UserType.Supplier;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private TextArea deleteExplanation;

	@FXML
	private TextArea editExplanation;

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
	private Rectangle leftArrowBtn;

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

	private User user = (User) ClientGUI.getClient().getUser().getServerResponse();
	private String restaurant = user.getOrganization();
	private Product product;

	/**
	 * This method get from DB list of products and create a table with them
	 */
	public void setMenu() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				ClientGUI.getClient().getRestaurantMenu(restaurant);
				synchronized (ClientGUI.getMonitor()) {
					try {
						ClientGUI.getMonitor().wait();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
				ServerResponse sr = ClientGUI.getClient().getLastResponse();
				@SuppressWarnings("unchecked")
				// get the server response- list of product (menu)
				ArrayList<Product> response = (ArrayList<Product>) sr.getServerResponse();
				setTable(response);
				return;
			}
		});
		t.start();
	}

	/**
	 * This method gets list of products and set a table
	 * @param products
	 */
	public void setMenu(ArrayList<Product> products) {
		setTable(products);
	}

	/**
	 * initialize the next controller - addNewItemController
	 */
	@FXML
	void addNewItemClicked(MouseEvent event) {
		clearPage();
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

	/**
	 * initialize the next controller - editMenuItemController
	 */
	@FXML
	void editItemClicked(MouseEvent event) {
		clearPage();
		if (product == null) {
			errorMsg.setText("Please select a row to edit");
			return;
		}
		if (router.getEditMenuItemController() == null) {
			AnchorPane mainContainer;
			editMenuItemController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeEditMenuItemPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.setProduct(product);
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
			router.getEditMenuItemController().setProduct(product);
			stage.setTitle("BiteMe - Edit Menu Item");
			stage.setScene(router.getEditMenuItemController().getScene());
			stage.show();
		}
	}

	/**
	 * This method deletes an item from menu and display a feedback to screen
	 */
	@FXML
	void deleteItemTxtClicked(MouseEvent event) {
		if (product == null) {
			errorMsg.setText("Please select a row to delete");
			return;
		}
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				ClientGUI.getClient().deleteItemFromMenu(restaurant, product.getDishName());
				synchronized (ClientGUI.getMonitor()) {
					try {
						ClientGUI.getMonitor().wait();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
				if (!checkServerResponse()) {
					return;
				}

				// get the server response- update list of product (menu)
				ServerResponse sr = ClientGUI.getClient().getLastResponse();
				@SuppressWarnings("unchecked")
				// update the table with the new menu, after delete item
				ArrayList<Product> response = (ArrayList<Product>) sr.getServerResponse();
				Platform.runLater(() -> {
					setMenu(response);
					// display that the delete was successes
					VImage.setVisible(true);
					menuUpdatedSuccessfullyTxt.setText("The item was deleted successfully");
				});
			}
		});
		t.start();
	}

	/**
	 * present an explanation how supplier suppose to edit an item
	 */
	@FXML
	void explainHowEdit(MouseEvent event) {
		editExplanation.setVisible(true);
	}

	/**
	 * close the explanation of how supplier suppose to edit an item
	 */
	@FXML
	void closeExplainEdit(MouseEvent event) {
		editExplanation.setVisible(false);
	}

	/**
	 * present an explanation how supplier suppose to delete an item
	 */
	@FXML
	void explainHowDelete(MouseEvent event) {
		deleteExplanation.setVisible(true);
	}

	/**
	 * close the explanation of how supplier suppose to delete an item
	 */
	@FXML
	void closeExplainDelete(MouseEvent event) {
		deleteExplanation.setVisible(false);
	}

	/**
	 * Checks server response and display relevant information.
	 * return true if the deleting was completed successfully and false else
	 */
	private boolean checkServerResponse() {
		if (ClientGUI.getClient().getLastResponse() == null) {
			errorMsg.setText("Deleting an item from menu was failed");
			return false;
		}
		switch (ClientGUI.getClient().getLastResponse().getMsg().toLowerCase()) {
		case "":
			errorMsg.setText("Deleting an item from menu was failed");
			return false;
		case "success":
			return true;
		default:
			return false;
		}
	}

	/**
	 * Logout and change scene to home page
	 */
	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	/**
	 * Changes scene to profile
	 */
	@FXML
	void profileBtnClicked(MouseEvent event) {
		clearPage();
		router.showProfile();
	}

	/**
	 * Changes scene to home page
	 */
	@FXML
	void returnToHomePage(MouseEvent event) {
		clearPage();
		router.changeSceneToHomePage();
	}

	/**
	 * Changes scene to supplier panel
	 */
	@FXML
	void returnToSupplierPanel(MouseEvent event) {
		clearPage();
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
		router.setUpdateMenuController(this);
		setStage(router.getStage());
		router.setArrow(leftArrowBtn, -90);
		initTable();
		clearPage();
	}

	/**
	 * This method set visibility of buttons and text
	 */
	private void clearPage() {
		VImage.setVisible(false);
		menuUpdatedSuccessfullyTxt.setText("");
		deleteExplanation.setVisible(false);
		editExplanation.setVisible(false);
		errorMsg.setText("");
	}


	/**
	 * This method initialize the titles of the table
	 */
	private void initTable() {
		table_Type.setCellValueFactory(new PropertyValueFactory<>("type"));
		table_DishName.setCellValueFactory(new PropertyValueFactory<>("dishName"));
		table_Components.setCellValueFactory(new PropertyValueFactory<>("components"));
		table_Price.setCellValueFactory(new PropertyValueFactory<>("price"));
		table_Description.setCellValueFactory(new PropertyValueFactory<>("description"));
	}

	/**
	 * This method set table columns and values
	 * @param menu
	 */
	private void setTable(ArrayList<Product> menu) {
		menuTable.setItems(getProduct(menu));
	}

	/**
	 * This method change arrayList to ObservableList
	 * @param list
	 * @return ObservableList of products - menu
	 */
	private ObservableList<Product> getProduct(ArrayList<Product> list) {
		ObservableList<Product> menu = FXCollections.observableArrayList();
		list.forEach(p -> {
			if (p.getDescription() == null || p.getDescription().equals("")) {
				p.setDescription("");
			}
			if (p.getComponents() == null || p.getComponents().size() == 0) {
				p.setComponents(new ArrayList<>(Arrays.asList(new Component(""))));
			}
		});
		menu.addAll(list);
		return menu;
	}

	/**
	 * This method get the data from the selected row
	 * @param event
	 */
	@FXML
	void getRowData(MouseEvent event) {
		product = menuTable.getSelectionModel().getSelectedItem();
		if (product == null) {
			return;
		}
	}

	/**
	 * This method set product
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		if (product != null) {
			this.product = product;
		}
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
