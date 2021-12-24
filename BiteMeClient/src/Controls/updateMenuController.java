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
	private Product product;

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
		setTable(response);
		return;
	}

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

	@FXML
	void editItemClicked(MouseEvent event) {
		clearPage();
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

	@FXML
    void deleteItemTxtClicked(MouseEvent event) {
		if(product == null) {
			return;
		}
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
		ClientGUI.client.deleteItemFromMenu(restaurant, product.getDishName());
		try {
			t.join();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		if (!checkServerResponse()) {
			return;
		}

		ClientGUI.client.getLastResponse().getServerResponse();
		//update the new menu after delete item
		Menu();
		//display that the delete was successes
		VImage.setVisible(true);
		menuUpdatedSuccessfullyTxt.setText("The item was deleted successfully");
    }
	
    @FXML
    void explainHowEdit(MouseEvent event) {
    	editExplanation.setVisible(true);
    }
    
    @FXML
    void closeExplainEdit(MouseEvent event) {
    	editExplanation.setVisible(false);
    }
	
    @FXML
    void explainHowDelete(MouseEvent event) {
    	deleteExplanation.setVisible(true);
    }
	
    @FXML
    void closeExplainDelete(MouseEvent event) {
    	deleteExplanation.setVisible(false);
    }
	
	/**
	 * checks the user information received from Server. display relevant
	 * information.
	 */
	private boolean checkServerResponse() {
		if (ClientGUI.client.getLastResponse() == null) {
			return false;
		}

		switch (ClientGUI.client.getLastResponse().getMsg().toLowerCase()) {
		case "":
			errorMsg.setText("Adding an item to menu was failed");
			return false;
		case "success":
			return true;
		default:
			return false;
		}
	}

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	@FXML
	void profileBtnClicked(MouseEvent event) {
		router.showProfile();
		clearPage();
	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		router.changeSceneToHomePage();
		clearPage();
	}

	@FXML
	void returnToSupplierPanel(MouseEvent event) {
		router.returnToSupplierPanel(event);
		clearPage();
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
		clearPage();
	}
	
	private void clearPage() {
		VImage.setVisible(false);
		menuUpdatedSuccessfullyTxt.setText("");
		deleteExplanation.setVisible(false);
		editExplanation.setVisible(false);
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
				p.setComponents(new ArrayList<>(Arrays.asList(new Component(""))));
			}
		});
		menu.addAll(list);
		return menu;
	}

	/**
	 * get the data of specific item that selected
	 * 
	 * @param event
	 */
	@FXML
	void getRowData(MouseEvent event) {
		if (menuTable.getSelectionModel() == null) {
			return;
		}
		product = menuTable.getSelectionModel().getSelectedItem();
		if (product == null) {
			return;
		}
	}
	
	/**
	 * @param product the product to set
	 */
	public void setProduct(Product product) {
		if(product!=null) {
			this.product = product;
		}
	}

}
