package Controls;

public class Router {
	private static Router router = null;
	private loginController Logincontroller;
	private enterGUIController Enterguicontroller;
	private restaurantSelectionController RestaurantselectionController;
	private homePageController HomePageController;
	private managerPanelController ManagerPanelController;
	private supplierPanelController SupplierPanelController;

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

	public loginController getLogincontroller() {
		return Logincontroller;
	}

	public enterGUIController getEnterguicontroller() {
		return Enterguicontroller;
	}

	public restaurantSelectionController getRestaurantselectionController() {
		return RestaurantselectionController;
	}

	public homePageController getHomePageController() {
		return HomePageController;
	}

	public managerPanelController getManagerPanelController() {
		return ManagerPanelController;
	}
	
	public supplierPanelController getSupplierPanelController() {
		return SupplierPanelController;
	}

	/*
	 * public static void show(Object c) { //
	 * router.show(loginController.getClass()); switch(c) case loginController: open
	 * logincontroller; case EnterGUIController: open Enterguicontroller
	 * 
	 * }
	 */

}
