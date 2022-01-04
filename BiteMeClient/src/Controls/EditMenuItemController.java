package Controls;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Entities.Component;
import Entities.Product;
import Entities.User;
import Enums.Doneness;
import Enums.Size;
import Enums.TypeOfProduct;
import Enums.UserType;
import Util.InputValidation;
import client.ClientGUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Natali
 *	This class is edit an item in menu by supplier
 */
public class EditMenuItemController implements Initializable {

	public final UserType type = UserType.Supplier;
	private Router router;
	private Stage stage;
	private Scene scene;

	@FXML
	private CheckBox donenessCheckBox;

	@FXML
	private CheckBox sizeCheckBox;

	@FXML
	private ImageView VImage;

	@FXML
	private Text successMsg;

	@FXML
	private ImageView infoIcon;

	@FXML
	private TextArea infoTxtArea;

	@FXML
	private Label updateItemBtn;

	@FXML
	private Text errorMsg;

	@FXML
	private Rectangle avatar;

	@FXML
	private TextArea descriptionTxtArea;

	@FXML
	private Text homePageBtn;

	@FXML
	private TextField itemsNameTxtField;

	@FXML
	private Rectangle leftArrowBtn;

	@FXML
	private Text logoutBtn;

	@FXML
	private TextField optionalComponentsTxtField;

	@FXML
	private TextField priceTxtField;

	@FXML
	private Text profileBtn;

	@FXML
	private ComboBox<TypeOfProduct> selectTypeBox;

	@FXML
	private Text supplierPanelBtn;

	private ObservableList<TypeOfProduct> list;
	private User user = (User) ClientGUI.getClient().getUser().getServerResponse();
	private String restaurant = user.getOrganization();
	private ArrayList<Component> optionalComponents = new ArrayList<>();
	private Product product;

	/**
	 * set values of possible dish types inside the comboBox
	 */
	private void setTypeComboBox() {
		ArrayList<TypeOfProduct> type = new ArrayList<TypeOfProduct>();
		type.add(TypeOfProduct.mainDish);
		type.add(TypeOfProduct.entry);
		type.add(TypeOfProduct.dessert);
		type.add(TypeOfProduct.drink);

		list = FXCollections.observableArrayList(type);
		selectTypeBox.setItems(list);
	}

	/**
	 * This method gets the new item's details from supplier and update it in DB 
	 */
	@FXML
	void updateItemBtnClicked(MouseEvent event) {
		if (!checkInputs()) {
			return;
		}

		TypeOfProduct typeOfProduct = selectTypeBox.getValue();
		String itemName = itemsNameTxtField.getText();
		String temp = optionalComponentsTxtField.getText();
		String regex = "^[u0400-u04FFa-zA-Z ]+(,[u0400-u04FFa-zA-Z ]+)*$";
		if(!temp.equals("") && !temp.matches(regex)) {
			errorMsg.setText("Please enter optional components according the explanation");
			return;
		}
		// update temp according the check boxes
		if (sizeCheckBox.isSelected()) { 
			String size = "Size";
			// there are some free text components
			if (temp.equals("") == false) {
				temp = temp + "," + size;
			} else
				temp = temp + size;
		}
		if (donenessCheckBox.isSelected()) { 
			String doneness = "Doneness";
			// there are some free text components or size
			if (temp.equals("") == false) { 
				temp = temp + "," + doneness;
			} else
				temp = temp + doneness;
		}
		String description = descriptionTxtArea.getText();
		float price = Float.parseFloat(priceTxtField.getText());
		if (temp.equals("") == false) { // there are components
			String[] components = temp.split(",");
			optionalComponents = new ArrayList<>();
			for (int i = 0; i < components.length; i++) {
				optionalComponents.add(new Component(components[i]));
			}
			product = new Product(restaurant, typeOfProduct, itemName, optionalComponents, price, description);
		} else // there aren't components
			product = new Product(restaurant, typeOfProduct, itemName, null, price, description);

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				ClientGUI.getClient().editItemInMenu(product);
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
				Platform.runLater(() -> {
					clearPage();
					// item was updated successfully
					VImage.setVisible(true);
					successMsg.setVisible(true);
				});
			}
		});
		t.start();
	}

	/**
	 * This method checking if supplier input is valid
	 * return false if there is an error
	 * return true if the input is valid
	 */
	private boolean checkInputs() {
		String itemName = itemsNameTxtField.getText();
		String price = priceTxtField.getText();
		VImage.setVisible(false);
		successMsg.setVisible(false);

		if (selectTypeBox.getSelectionModel().getSelectedItem() == null) {
			errorMsg.setText("Please select type of the item");
			return false;
		}
		if (!InputValidation.checkValidText(itemName)) {
			errorMsg.setText("Please enter the name of the item");
			return false;
		}
		if (InputValidation.checkSpecialCharacters(itemName)) {
			errorMsg.setText("Special characters aren't allowed in item name");
			return false;
		}
		if (!InputValidation.checkValidText(price)) {
			errorMsg.setText("Please enter the price of the item");
			return false;
		}
		if (InputValidation.checkContainCharacters(price)) {
			errorMsg.setText("Characters aren't allowed in price");
			return false;
		}
		if (InputValidation.checkSpecialCharacters(price) && !price.contains(".")) {
			errorMsg.setText("Special characters aren't allowed in price,\n Only one decimal point");
			return false;
		}
		errorMsg.setText("");
		return true;
	}

	/**
	 * Checks server response and display relevant information.
	 * return true if the update was completed successfully and false else
	 */
	private boolean checkServerResponse() {
		if (ClientGUI.getClient().getLastResponse() == null) {
			return false;
		}

		switch (ClientGUI.getClient().getLastResponse().getMsg().toLowerCase()) {
		case "":
			errorMsg.setText("Edit an item in menu was failed");
			return false;
		case "success":
			return true;
		default:
			errorMsg.setText("Edit an item in menu was failed");
			return false;
		}
	}

	/**
	 * display the explanation of how supplier suppose to write a component
	 */
	@FXML
	void infoIconEnter(MouseEvent event) {
		infoTxtArea.setVisible(true);
	}

	/**
	 * close the explanation of how supplier suppose to write a component
	 */
	@FXML
	void infoIconExit(MouseEvent event) {
		infoTxtArea.setVisible(false);
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
	 * Change scene to the previous page
	 */
	@FXML
	void returnToUpdateMenu(MouseEvent event) {
		clearPage();
		router.getSupplierPanelController().updateMenuClicked(event);
	}

	/**
	 * This method clear page, set visibility of buttons and text
	 */
	private void clearPage() {
		errorMsg.setText("");
		selectTypeBox.getSelectionModel().clearSelection();
		itemsNameTxtField.clear();
		optionalComponentsTxtField.clear();
		donenessCheckBox.setSelected(false);
		sizeCheckBox.setSelected(false);
		priceTxtField.clear();
		descriptionTxtArea.clear();
		infoTxtArea.setVisible(false);
		VImage.setVisible(false);
		successMsg.setVisible(false);
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
		router.setEditMenuItemController(this);
		setStage(router.getStage());
		setTypeComboBox();
		clearPage();
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

	/**
	 * This method set product according the selected row from the table 
	 * @param product
	 */
	public void setProduct(Product product) {
		if (product != null) {
			selectTypeBox.setValue(product.getType());
			itemsNameTxtField.setText(product.getDishName());
			priceTxtField.setText(((Float) product.getPrice()).toString());
			descriptionTxtArea.setText(product.getDescription());
			String components = "";
			optionalComponents = product.getComponents();
			for (int i = 0; i < optionalComponents.size(); i++) {
				if (optionalComponents.get(i).equals(new Component(Size.Medium))) {
					sizeCheckBox.setSelected(true);
				} else if (optionalComponents.get(i).equals(new Component(Doneness.medium))) {
					donenessCheckBox.setSelected(true);
				} else // restrictions
					components = optionalComponents.get(i).toString() + "," + components;
			}
			if (!components.equals("")) {
				optionalComponentsTxtField.setText(components.substring(0, components.length() - 1));
			}

		}
	}

}
