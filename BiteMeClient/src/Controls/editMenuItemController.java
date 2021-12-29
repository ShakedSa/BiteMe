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
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class editMenuItemController implements Initializable {

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

	ObservableList<TypeOfProduct> list;
	private User user = (User) ClientGUI.client.getUser().getServerResponse();
	private String restaurant = user.getOrganization();
	private ArrayList<Component> optionalComponents = new ArrayList<>();
	private Product product;

	// creating list of Types
	private void setTypeComboBox() {
		ArrayList<TypeOfProduct> type = new ArrayList<TypeOfProduct>();
		type.add(TypeOfProduct.mainDish);
		type.add(TypeOfProduct.entry);
		type.add(TypeOfProduct.dessert);
		type.add(TypeOfProduct.drink);

		list = FXCollections.observableArrayList(type);
		selectTypeBox.setItems(list);
	}

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
		if (sizeCheckBox.isSelected()) { // checkSelectionSize(sizeCheckBox)
			String size = "Size";
			if (temp.equals("") == false) { // there are some free text components
				temp = temp + "," + size;
			} else
				temp = temp + size;
		}
		if (donenessCheckBox.isSelected()) { // checkSelectionSize(donenessCheckBox)
			String doneness = "Doneness";
			if (temp.equals("") == false) { // there are some free text components or size
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
				ClientGUI.client.editItemInMenu(product);
				synchronized (ClientGUI.monitor) {
					try {
						ClientGUI.monitor.wait();
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
					VImage.setVisible(true);
					successMsg.setVisible(true);
				});
			}
		});
		t.start();
	}

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
	 * checks the user information received from Server. display relevant
	 * information.
	 */
	private boolean checkServerResponse() {
		if (ClientGUI.client.getLastResponse() == null) {
			return false;
		}

		switch (ClientGUI.client.getLastResponse().getMsg().toLowerCase()) {
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

	@FXML
	void infoIconEnter(MouseEvent event) {
		infoTxtArea.setVisible(true);
	}

	@FXML
	void infoIconExit(MouseEvent event) {
		infoTxtArea.setVisible(false);
	}

	@FXML
	void logoutClicked(MouseEvent event) {
		router.logOut();
	}

	@FXML
	void profileBtnClicked(MouseEvent event) {
		clearPage();
		router.showProfile();
	}

	@FXML
	void returnToHomePage(MouseEvent event) {
		clearPage();
		router.changeSceneToHomePage();
	}

	@FXML
	void returnToSupplierPanel(MouseEvent event) {
		clearPage();
		router.returnToSupplierPanel(event);
	}

	@FXML
	void returnToUpdateMenu(MouseEvent event) {
		clearPage();
		router.getSupplierPanelController().updateMenuClicked(event);
	}

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
