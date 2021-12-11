package Controls;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
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
	private ceoPanelController CEOPanelController;
	private employerHRPanelController EmployerHRPanelController;
	private profileController ProfileController;
	//Manager Panel pages:
	private addNewSupplierController AddNewSupplierController;
	private viewMonthlyReportsController ViewMonthlyReportsController;
	private uploadQuarterlyReportController UploadQuarterlyReportController;
	private updateUserInformationController UpdateUserInformationController;
	private authorizedEmployerApprovalController authorizedEmployerApprovalController;
	private openNewAccountController OpenNewAccountController;

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
	public void setViewMonthlyReportController(viewMonthlyReportsController viewMonthlyReportsController) {
		ViewMonthlyReportsController = viewMonthlyReportsController;
	}
	
	/**
	 * @param uploadQuarterlyReportController the uploadQuarterlyReportController to set
	 */
	public void setUploadQuarterlyReportController(uploadQuarterlyReportController uploadQuarterlyReportController) {
		UploadQuarterlyReportController = uploadQuarterlyReportController;
	}
	
	/**
	 * @param updateUserInformationController the updateUserInformationController to set
	 */
	public void setUpdateUserInformationController(updateUserInformationController updateUserInformationController) {
		UpdateUserInformationController = updateUserInformationController;
	}
	
	/**
	 * @param authorizedEmployerApprovalController the authorizedEmployerApprovalController to set
	 */
	public void setAuthorizedEmployerApprovalController(
			authorizedEmployerApprovalController authorizedEmployerApprovalController) {
		this.authorizedEmployerApprovalController = authorizedEmployerApprovalController;
	}
	
	/**
	 * @param openNewAccountController the openNewAccountController to set
	 */
	public void setOpenNewAccountController(openNewAccountController openNewAccountController) {
		OpenNewAccountController = openNewAccountController;
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
		return authorizedEmployerApprovalController;
	}
	
	/**
	 * @return the openNewAccountController
	 */
	public openNewAccountController getOpenNewAccountController() {
		return OpenNewAccountController;
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
		System.out.println("test");
		AnchorPane mainContainer;
		if(ProfileController == null) // first time clicking profile
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
		}
		else // profile already been clicked before (not necessarily same user)
		{
			ProfileController.initProfile();
			stage.setTitle("BiteMe - Profile");
			stage.setScene(ProfileController.getScene());
			stage.show();
		}
	}


	
	/*
	 * public static void show(Object c) { //
	 * router.show(loginController.getClass()); switch(c) case loginController: open
	 * logincontroller; case EnterGUIController: open Enterguicontroller
	 * 
	 * }
	 */

}
