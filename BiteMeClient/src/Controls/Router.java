package Controls;

import java.io.IOException;
import java.util.ArrayList;

import Entities.Product;
import Entities.ServerResponse;
import Entities.User;
import client.ClientGUI;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Router {
	private static Router router = null;
	private Stage stage;
	private loginController Logincontroller;
	private enterGUIController Enterguicontroller;
	private restaurantSelectionController RestaurantselectionController;
	private homePageController HomePageController;
	private managerPanelController ManagerPanelController;
	private supplierPanelController SupplierPanelController;
	private identifyController IdentifyController;
	private restaurantMenuController RestaurantMenuController;
	private pickDateAndTimeController PickDateAndTimeController;
	private ceoPanelController CEOPanelController;
	/**
	 * @return the pickDateAndTimeController
	 */
	public pickDateAndTimeController getPickDateAndTimeController() {
		return PickDateAndTimeController;
	}

	/**
	 * @param pickDateAndTimeController the pickDateAndTimeController to set
	 */
	public void setPickDateAndTimeController(pickDateAndTimeController pickDateAndTimeController) {
		PickDateAndTimeController = pickDateAndTimeController;
	}

	private employerHRPanelController EmployerHRPanelController;
	private profileController ProfileController;
	// Manager Panel pages:
	private addNewSupplierController AddNewSupplierController;
	private viewMonthlyReportsController ViewMonthlyReportsController;
	private uploadQuarterlyReportController UploadQuarterlyReportController;
	private updateUserInformationController UpdateUserInformationController;
	private authorizedEmployerApprovalController AuthorizedEmployerApprovalController;
	private openNewAccountController OpenNewAccountController;
	// Supplier Panel pages:
	private createMenuController CreateMenuController;
	private addNewItemController AddNewItemController;
	private updateMenuController UpdateMenuController;
	private editMenuItemController EditMenuItemController;
	// Employer HR Panel pages:
	private registerEmployerAsLegacyController registerEmployerAsLegacyController;

	private supplierUpdateOrderController SupplierUpdateOrderController;

	/** Items in order, should be available across all application. */
	private ArrayList<Product> order = new ArrayList<>();

	public static Router getInstance() {
		if (router == null)
			router = new Router();
		return router;
	}

	/**
	 * @param logincontroller the loginController to set
	 */
	public void setLogincontroller(loginController logincontroller) {
		Logincontroller = logincontroller;
	}

	/**
	 * @param enterguicontroller the enterguiController to set
	 */
	public void setEnterguicontroller(enterGUIController enterGUIController) {
		Enterguicontroller = enterGUIController;
	}

	/**
	 * @param restaurantselectionController the restaurantselectionController to set
	 */
	public void setRestaurantselectionController(restaurantSelectionController restaurantSelectionController) {
		RestaurantselectionController = restaurantSelectionController;
	}

	/**
	 * @param homePageController the homePageController to set
	 */
	public void setHomePageController(homePageController homePageController) {
		HomePageController = homePageController;
	}

	/**
	 * @param managerPanelController the managerPanelController to set
	 */
	public void setManagerPanelController(managerPanelController managerPanelController) {
		ManagerPanelController = managerPanelController;
	}

	/**
	 * @param supplierPanelController the supplierPanelController to set
	 */
	public void setSupplierPanelController(supplierPanelController supplierPanelController) {
		SupplierPanelController = supplierPanelController;
	}

	/**
	 * @param ceoPanelController the ceoPanelController to set
	 */
	public void setCEOPanelController(ceoPanelController ceoPanelController) {
		CEOPanelController = ceoPanelController;
	}

	/**
	 * @param restaurantMenuController the restaurantMenuController to set
	 */
	public void setRestaurantMenuController(restaurantMenuController restaurantMenuController) {
		RestaurantMenuController = restaurantMenuController;
	}

	/**
	 * @param employerHRPanelController the employerHRPanelController to set
	 */
	public void setEmployerHRPanelController(employerHRPanelController employerHRPanelController) {
		EmployerHRPanelController = employerHRPanelController;
	}

	/**
	 * @param identifyController the identifyController to set
	 */
	public void setIdentifyController(identifyController identifyController) {
		IdentifyController = identifyController;
	}

	/**
	 * @param profileController the profileController to set
	 */
	public void setProfileController(profileController profileController) {
		ProfileController = profileController;
	}

	/**
	 * @param addNewSupplierController the addNewSupplierController to set
	 */
	public void setAddNewSupplierController(addNewSupplierController addNewSupplierController) {
		AddNewSupplierController = addNewSupplierController;
	}

	/**
	 * @param viewMonthlyReportsController the viewMonthlyReportsController to set
	 */
	public void setViewMonthlyReportsController(viewMonthlyReportsController viewMonthlyReportsController) {
		ViewMonthlyReportsController = viewMonthlyReportsController;
	}

	/**
	 * @param uploadQuarterlyReportController the uploadQuarterlyReportController to
	 *                                        set
	 */
	public void setUploadQuarterlyReportController(uploadQuarterlyReportController uploadQuarterlyReportController) {
		UploadQuarterlyReportController = uploadQuarterlyReportController;
	}

	/**
	 * @param updateUserInformationController the updateUserInformationController to
	 *                                        set
	 */
	public void setUpdateUserInformationController(updateUserInformationController updateUserInformationController) {
		UpdateUserInformationController = updateUserInformationController;
	}

	/**
	 * @param authorizedEmployerApprovalController the
	 *                                             authorizedEmployerApprovalController
	 *                                             to set
	 */
	public void setAuthorizedEmployerApprovalController(
			authorizedEmployerApprovalController authorizedEmployerApprovalController) {
		AuthorizedEmployerApprovalController = authorizedEmployerApprovalController;
	}

	/**
	 * @param openNewAccountController the openNewAccountController to set
	 */
	public void setOpenNewAccountController(openNewAccountController openNewAccountController) {
		OpenNewAccountController = openNewAccountController;
	}

	/**
	 * @param createMenuController the createMenuController to set
	 */
	public void setCreateMenuController(createMenuController createMenuController) {
		CreateMenuController = createMenuController;
	}

	/**
	 * @param addNewItemController the addNewItemController to set
	 */
	public void setAddNewItemController(addNewItemController addNewItemController) {
		AddNewItemController = addNewItemController;
	}

	/**
	 * @param updateMenuController the updateMenuController to set
	 */
	public void setUpdateMenuController(updateMenuController updateMenuController) {
		UpdateMenuController = updateMenuController;
	}

	/**
	 * @param editMenuItemController the editMenuItemController to set
	 */
	public void setEditMenuItemController(editMenuItemController editMenuItemController) {
		EditMenuItemController = editMenuItemController;
	}

	/**
	 * @param supplierUpdateOrderController the supplierUpdateOrderController to set
	 */
	public void setSupplierUpdateOrderController(supplierUpdateOrderController supplierUpdateOrderController) {
		SupplierUpdateOrderController = supplierUpdateOrderController;
	}

	/**
	 * @param registerEmployerAsLegacyController the
	 *                                           registerEmployerAsLegacyController
	 *                                           to set
	 */
	public void setRegisterEmployerAsLegacyController(
			registerEmployerAsLegacyController registerEmployerAsLegacyController) {
		this.registerEmployerAsLegacyController = registerEmployerAsLegacyController;
	}

	/**
	 * @return the Logincontroller
	 */
	public loginController getLogincontroller() {
		return Logincontroller;
	}

	/**
	 * @return the Enterguicontroller
	 */
	public enterGUIController getEnterguicontroller() {
		return Enterguicontroller;
	}

	/**
	 * @return the RestaurantselectionController
	 */
	public restaurantSelectionController getRestaurantselectionController() {
		return RestaurantselectionController;
	}

	/**
	 * @return the HomePageController
	 */
	public homePageController getHomePageController() {
		return HomePageController;
	}

	/**
	 * @return the ManagerPanelController
	 */
	public managerPanelController getManagerPanelController() {
		return ManagerPanelController;
	}

	/**
	 * @return the SupplierPanelController
	 */
	public supplierPanelController getSupplierPanelController() {
		return SupplierPanelController;
	}

	/**
	 * @return the identifyController
	 */
	public identifyController getIdentifyController() {
		return IdentifyController;
	}

	/**
	 * @return the restaurantMenuController
	 */
	public restaurantMenuController getRestaurantMenuController() {
		return RestaurantMenuController;
	}

	/**
	 * @return the restaurantMenuController
	 */
	public ceoPanelController getCEOPanelController() {
		return CEOPanelController;
	}

	/**
	 * @return the employerHRPanelController
	 */
	public employerHRPanelController getEmployerHRPanelController() {
		return EmployerHRPanelController;
	}

	/**
	 * @return the employerHRPanelController
	 */
	public profileController getProfileController() {
		return ProfileController;
	}

	/**
	 * @return the addNewSupplierController
	 */
	public addNewSupplierController getAddNewSupplierController() {
		return AddNewSupplierController;
	}

	/**
	 * @return the viewMonthlyReportController
	 */
	public viewMonthlyReportsController getViewMonthlyReportsController() {
		return ViewMonthlyReportsController;
	}

	/**
	 * @return the uploadQuarterlyReportController
	 */
	public uploadQuarterlyReportController getUploadQuarterlyReportController() {
		return UploadQuarterlyReportController;
	}

	/**
	 * @return the updateUserInformationController
	 */
	public updateUserInformationController getUpdateUserInformationController() {
		return UpdateUserInformationController;
	}

	/**
	 * @return the authorizedEmployerApprovalController
	 */
	public authorizedEmployerApprovalController getAuthorizedEmployerApprovalController() {
		return AuthorizedEmployerApprovalController;
	}

	/**
	 * @return the openNewAccountController
	 */
	public openNewAccountController getOpenNewAccountController() {
		return OpenNewAccountController;
	}

	/**
	 * @return the createMenuController
	 */
	public createMenuController getCreateMenuController() {
		return CreateMenuController;
	}

	/**
	 * @return the addNewItemController
	 */
	public addNewItemController getAddNewItemController() {
		return AddNewItemController;
	}

	/**
	 * @return the updateMenuController
	 */
	public updateMenuController getUpdateMenuController() {
		return UpdateMenuController;
	}

	/**
	 * @return the editMenuItemController
	 */
	public editMenuItemController getEditMenuItemController() {
		return EditMenuItemController;
	}

	/**
	 * @return the supplierUpdateOrderController
	 */
	public supplierUpdateOrderController getSupplierUpdateOrderController() {
		return SupplierUpdateOrderController;
	}

	/**
	 * @return the registerEmployerAsLegacyController
	 */
	public registerEmployerAsLegacyController getRegisterEmployerAsLegacyController() {
		return registerEmployerAsLegacyController;
	}

	/**
	 * @return the stage
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * @param stage the stage to set
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void showProfile() {
		AnchorPane mainContainer;
		if (ProfileController == null) // first time clicking profile
		{
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeProfilePage.fxml"));
				mainContainer = loader.load();
				ProfileController = loader.getController();
				ProfileController.setAvatar();
				ProfileController.initProfile();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				ProfileController.setScene(mainScene);
				stage.setTitle("BiteMe - Home Page");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else // profile already been clicked before (not necessarily same user)
		{
			ProfileController.initProfile();
			ProfileController.setItemsCounter();
			stage.setTitle("BiteMe - Profile");
			stage.setScene(ProfileController.getScene());
			stage.show();
		}
	}

	public void logOut() {
		ServerResponse resUser = ClientGUI.client.getUser();
		if (resUser != null) {
			User user = (User) resUser.getServerResponse();
			if (user != null) {
				ClientGUI.client.logout(user.getUserName());
				ClientGUI.client.setUser(null);
			}
		}
		router.getHomePageController().setProfile(false);
		setBagItems(new ArrayList<>());
		changeSceneToHomePage();
	}

	public void changeSceneToHomePage() {
		getHomePageController().setItemsCounter();
		stage.setTitle("BiteMe - HomePage");
		stage.setScene(router.getHomePageController().getScene());
		stage.show();
	}

	public Rectangle setAvatar(Rectangle avatar) {
		try {
			avatar.setArcWidth(65);
			avatar.setArcHeight(65);
			ImagePattern pattern = getAvatarImage();
			avatar.setFill(pattern);
			avatar.setEffect(new DropShadow(3, Color.BLACK));
			avatar.setStyle("-fx-border-width: 0");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return avatar;
	}

	ImagePattern getAvatarImage() {
		ServerResponse userResponse = ClientGUI.client.getUser();
		if (userResponse == null) {
			return new ImagePattern(new Image(getClass().getResource("../images/guest-avatar.png").toString()));
		}
		User user = (User) userResponse.getServerResponse();
		if (user == null) {
			return new ImagePattern(new Image(getClass().getResource("../images/guest-avatar.png").toString()));
		}
		switch (user.getUserType()) {
		case Supplier:
			return new ImagePattern(new Image(getClass().getResource("../images/supplier-avatar.png").toString()));
		case BranchManager:
			return new ImagePattern(new Image(getClass().getResource("../images/manager-avatar.png").toString()));
		case CEO:
			return new ImagePattern(new Image(getClass().getResource("../images/CEO-avatar.png").toString()));
		case Customer:
			return new ImagePattern(new Image(getClass().getResource("../images/random-user.gif").toString()));
		case EmployerHR:
			return new ImagePattern(new Image(getClass().getResource("../images/HR-avatar.png").toString()));
		default:
			return new ImagePattern(new Image(getClass().getResource("../images/guest-avatar.png").toString()));
		}
	}
	
	void returnToSupplierPanel(MouseEvent event) {
    	if (router.getSupplierPanelController() == null) {
			AnchorPane mainContainer;
			supplierPanelController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeSupplierPanelPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Supplier Panel");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return; 
			}
		} else {
			stage.setTitle("BiteMe - Supplier Panel");
			stage.setScene(router.getSupplierPanelController().getScene());
			stage.show();
		}
    }
	
	void returnToEmployerHRPanel(MouseEvent event) {
    	if (router.getEmployerHRPanelController() == null) {
			AnchorPane mainContainer;
			employerHRPanelController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeEmployerHRPanelPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Employer HR Panel");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return; 
			}
		} else {
			stage.setTitle("BiteMe - Employer HR Panel");
			stage.setScene(router.getEmployerHRPanelController().getScene());
			stage.show();
		}
    }
	
	void returnToManagerPanel(MouseEvent event) {
    	if (router.getManagerPanelController() == null) {
			AnchorPane mainContainer;
			managerPanelController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeManagerPanelPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Manager Panel");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - Manager Panel");
			stage.setScene(router.getManagerPanelController().getScene());
			stage.show();
		}
    }
	
	void returnToCEOPanel(MouseEvent event) {
		if (router.getCEOPanelController() == null) {
			AnchorPane mainContainer;
			ceoPanelController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeCEOPanelPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - CEO Home Page");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - CEO Panel");
			stage.setScene(router.getCEOPanelController().getScene());
			stage.show();
		}
	}
	
	void returnToCustomerPanel(MouseEvent event) {
		if (router.getRestaurantselectionController() == null) {
			AnchorPane mainContainer;
			restaurantSelectionController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeRestaurantsPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.setRestaurants();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Restaurants");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			stage.setTitle("BiteMe - Restaurants");
			stage.setScene(router.getRestaurantselectionController().getScene());
			stage.show();
		}
	}
	

	/**
	 * Setting the order items
	 * 
	 * @param order
	 */
	public void setBagItems(ArrayList<Product> order) {
		this.order = order;
	}

	/**
	 * Getting the order items
	 * 
	 * @return order
	 */
	public ArrayList<Product> getBagItems() {
		return order;
	}
  
	/*
	 * public static void show(Object c) { //
	 * router.show(loginController.getClass()); switch(c) case loginController: open
	 * logincontroller; case EnterGUIController: open Enterguicontroller
	 * 
	 * }
	 */

}
