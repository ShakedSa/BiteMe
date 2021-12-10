package Controls;

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
	
	

	
	/*
	 * public static void show(Object c) { //
	 * router.show(loginController.getClass()); switch(c) case loginController: open
	 * logincontroller; case EnterGUIController: open Enterguicontroller
	 * 
	 * }
	 */

}
