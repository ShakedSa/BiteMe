package Controls;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.Component;
import Entities.Product;
import Enums.TypeOfProduct;
import Enums.UserType;
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
	void addNewItemClicked(MouseEvent event) {
		// router.getCreateMenuController().addNewItemClicked(event);
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
		createTable();
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
	public void createTable() {
		TableColumn<Product, TypeOfProduct> typeCol = new TableColumn<Product, TypeOfProduct>("Type");
		typeCol.setCellValueFactory(new PropertyValueFactory<Product, TypeOfProduct>("type"));
		menuTable.getColumns().add(typeCol);
		TableColumn<Product, String> nameCol = new TableColumn<Product, String>("Dish Name");
		nameCol.setCellValueFactory(new PropertyValueFactory<Product, String>("restaurantName"));
		menuTable.getColumns().add(nameCol);
		TableColumn<Product, ArrayList<Component>> componentCol = new TableColumn<Product, ArrayList<Component>>(
				"Components");
		componentCol.setCellValueFactory(new PropertyValueFactory<Product, ArrayList<Component>>("components"));
		menuTable.getColumns().add(componentCol);
		TableColumn<Product, Float> priceCol = new TableColumn<Product, Float>("Price");
		priceCol.setCellValueFactory(new PropertyValueFactory<Product, Float>("price"));
		menuTable.getColumns().add(priceCol);
		TableColumn<Product, String> descriptionCol = new TableColumn<Product, String>("Description");
		descriptionCol.setCellValueFactory(new PropertyValueFactory<Product, String>("description"));
		menuTable.getColumns().add(descriptionCol);
		menuTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

}
