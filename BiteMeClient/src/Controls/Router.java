package Controls;

public class Router {
	private static Router router=null;
	private loginController logincontroller;
	private EnterGUIController Enterguicontroller;
	private restaurantSelectionController restaurantselectionController;
	private homePageController HomePageController;

	public static Router getInstance() {
		if(router==null)
			router=new Router();
		return router;
		
	}
	
	/**
	 * @param logincontroller the logincontroller to set
	 */
	public void setLogincontroller(loginController logincontroller) {
		this.logincontroller = logincontroller;
		System.out.println("test");
	}
	/**
	 * @param enterguicontroller the enterguicontroller to set
	 */
	public void setEnterguicontroller(EnterGUIController enterguicontroller) {
		Enterguicontroller = enterguicontroller;
	}
	/**
	 * @param restaurantselectionController the restaurantselectionController to set
	 */
	public void setRestaurantselectionController(restaurantSelectionController restaurantselectionController) {
		this.restaurantselectionController = restaurantselectionController;
	}
	/**
	 * @param homePageController the homePageController to set
	 */
	public void setHomePageController(homePageController homePageController) {
		HomePageController = homePageController;
	}
	
/*	public static void show(Object c) { // router.show(loginController.getClass());
		switch(c)
		case loginController:  open logincontroller;
		case EnterGUIController: open Enterguicontroller
		
	}*/
	
}
