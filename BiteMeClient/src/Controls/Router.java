package Controls;

import java.io.IOException;
import java.util.ArrayList;

import Entities.Delivery;
import Entities.Order;
import Entities.OrderDeliveryMethod;
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
	private LoginController Logincontroller;
	private EnterGUIController Enterguicontroller;
	private MyCartController MyCartController;
	private MyOrdersController MyOrdersController;
	private HomePageController HomePageController;
	private ManagerPanelController ManagerPanelController;
	private SupplierPanelController SupplierPanelController;
	private CeoPanelController CEOPanelController;
	private ProfileController ProfileController;
	// Order Process pages:
	private RestaurantSelectionController RestaurantselectionController;
	private IdentifyController IdentifyController;
	private RestaurantMenuController RestaurantMenuController;
	private PickDateAndTimeController PickDateAndTimeController;
	private DeliveryMethodController DeliveryMethodController;
	private ReviewOrderController ReviewOrderController;
	private PaymentController PaymentController;
	private OrderReceivedController OrderReceivedController;
	// Manager Panel pages:
	private AddNewSupplierController AddNewSupplierController;
	private AddNewSupplierTableController AddNewSupplierTableController;
	private ViewMonthlyReportsController ViewMonthlyReportsController;
	private UploadQuarterlyReportController UploadQuarterlyReportController;
	private UpdateUserInformationController UpdateUserInformationController;
	private AuthorizedEmployerApprovalController AuthorizedEmployerApprovalController;
	private OpenNewAccountController OpenNewAccountController;
	private OpenNewAccountFinalController OpenNewAccountFinalController;
	private CreateRevenueQuarterlyReportController CreateRevenueQuarterlyReportController;
	// Supplier Panel pages:
	private AddNewItemController AddNewItemController;
	private UpdateMenuController UpdateMenuController;
	private EditMenuItemController EditMenuItemController;
	private SupplierUpdateOrderController SupplierUpdateOrderController;
	private SendMsgToCustomerController SendMsgToCustomerController;
	private UpdateOrderTableController UpdateOrderTableController;
	private ViewIncomeReceiptController ViewIncomeReceiptController;
	// Employer HR Panel pages:
	private EmployerHRPanelController EmployerHRPanelController;
	private RegisterEmployerAsLegacyController RegisterEmployerAsLegacyController;
	private ConfirmBusinessAccountController ConfirmBusinessAccountController;
	// CEO Panel pages:
	private ViewPDFQuarterlyReportController ViewPDFQuarterlyReportController;
	private ViewRevenueQuarterlyReportController ViewRevenueQuarterlyReportController;

	/** State of the order application: */
	/***************************************/
	private Order order = new Order();
	private Delivery delivery;
	private OrderDeliveryMethod orderDeliveryMethod;

	/***************************************/
	public static Router getInstance() {
		if (router == null)
			router = new Router();
		return router;
	}
	
	
	/**
	 * function that shows the current user profile 
	 */
	public void showProfile() {
		AnchorPane mainContainer;
		if (ProfileController == null) // first time clicking profile
		{
			try {
				//change the page scene into the profile page
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/bitemeProfilePage.fxml"));
				mainContainer = loader.load();
				ProfileController = loader.getController();
				ProfileController.setAvatar();
				ProfileController.initProfile();
				ProfileController.setItemsCounter();
				ProfileController.setLastScene(stage.getScene(), stage.getTitle());
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
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
			ProfileController.setLastScene(stage.getScene(), stage.getTitle());
			stage.setTitle("BiteMe - Profile");
			stage.setScene(ProfileController.getScene());
			stage.show();
		}
	}
	
	
	/**
	 * logs out the user from the app
	 */
	public void logOut() {
		ServerResponse resUser = ClientGUI.getClient().getUser();
		if (resUser != null) {
			User user = (User) resUser.getServerResponse();
			if (user != null) { //reset all the app controllers
				ClientGUI.getClient().logout(user.getUserName());
				EmployerHRPanelController = null;
				RegisterEmployerAsLegacyController = null;
				ConfirmBusinessAccountController = null;
				AddNewItemController = null;
				AddNewSupplierController = null;
				AuthorizedEmployerApprovalController = null;
				EditMenuItemController = null;
				OpenNewAccountController = null;
				RegisterEmployerAsLegacyController = null;
				SendMsgToCustomerController = null;
				SupplierUpdateOrderController = null;
				UpdateMenuController = null;
				UpdateUserInformationController = null;
				UploadQuarterlyReportController = null;
				ViewMonthlyReportsController = null;
				ViewPDFQuarterlyReportController = null;
				RestaurantMenuController = null;
				RestaurantselectionController = null;
				IdentifyController = null;
				MyCartController = null;
				PaymentController = null;
				PickDateAndTimeController = null;
				DeliveryMethodController = null;
				ReviewOrderController = null;
				OrderReceivedController = null;
				SupplierPanelController = null;
				CreateRevenueQuarterlyReportController=null;
				order = new Order();
				delivery = null;
				orderDeliveryMethod = null;
				ClientGUI.getClient().setUser(null);
			}
		}
		router.getHomePageController().setProfile(false);
		setBagItems(new ArrayList<>());
		changeSceneToHomePage();
	}
	
	/**
	 * Method that switch scene to Home page.
	 * */
	public void changeSceneToHomePage() {
		getHomePageController().setItemsCounter();
		stage.setTitle("BiteMe - HomePage");
		stage.setScene(router.getHomePageController().getScene());
		stage.show();
	}
	
	/**
	 * sets the current user avatar depanding on his permissions
	 * @param avatar
	 * @return a Rectangle with the users avatar
	 */
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
	
	
	/**
	 * checks the current logged in user and 
	 * @return an avatar according to his permmisions
	 */
	ImagePattern getAvatarImage() {
		ServerResponse userResponse = ClientGUI.getClient().getUser();
		if (userResponse == null) {
			return new ImagePattern(new Image(getClass().getResource("/images/guest-avatar.png").toString()));
		}
		User user = (User) userResponse.getServerResponse();
		if (user == null) {
			return new ImagePattern(new Image(getClass().getResource("/images/guest-avatar.png").toString()));
		}
		switch (user.getUserType()) {
		case Supplier:
			return new ImagePattern(new Image(getClass().getResource("/images/supplier-avatar.png").toString()));
		case BranchManager:
			return new ImagePattern(new Image(getClass().getResource("/images/manager-avatar.png").toString()));
		case CEO:
			return new ImagePattern(new Image(getClass().getResource("/images/CEO-avatar.png").toString()));
		case Customer:
			return new ImagePattern(new Image(getClass().getResource("/images/random-user.gif").toString()));
		case EmployerHR:
			return new ImagePattern(new Image(getClass().getResource("/images/HR-avatar.png").toString()));
		default:
			return new ImagePattern(new Image(getClass().getResource("/images/guest-avatar.png").toString()));
		}
	}
	
	
	/**
	 * changes the current page scene into the app homepage
	 * @param event
	 */
	void returnToSupplierPanel(MouseEvent event) {
		if (router.getSupplierPanelController() == null) {
			AnchorPane mainContainer;
			SupplierPanelController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/bitemeSupplierPanelPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.setImage();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
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
	
	
	/**
	 * sets the 'go back to the previous page' arrow inside an rectangle
	 * @param arrow
	 * @param rotationDegree
	 */
	public void setArrow(Rectangle arrow, int rotationDegree) {
		ImagePattern pattern = new ImagePattern(new Image(getClass().getResource("/images/arrow.gif").toString()));
		arrow.setFill(pattern);
		arrow.setStyle("-fx-stroke: null;-fx-cursor: hand");
		arrow.setRotate(rotationDegree);
	}

	
	/**
	 * changes the current page scene into the employersHR panel
	 * @param event
	 */
	void returnToEmployerHRPanel(MouseEvent event) {
		if (router.getEmployerHRPanelController() == null) {
			AnchorPane mainContainer;
			EmployerHRPanelController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/bitemeEmployerHRPanelPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
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

	
	/**
	 * changes the current page scene into the manager panel
	 * @param event
	 */
	void returnToManagerPanel(MouseEvent event) {
		if (router.getManagerPanelController() == null) {
			AnchorPane mainContainer;
			ManagerPanelController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/bitemeManagerPanelPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
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

	
	/**
	 * changes the current page scene into the CEO panel
	 * @param event
	 */
	void returnToCEOPanel(MouseEvent event) {
		if (router.getCEOPanelController() == null) {
			AnchorPane mainContainer;
			CeoPanelController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/bitemeCEOPanelPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
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

	
	/**
	 * changes the current page scene into the customer panel
	 * @param event
	 */
	void returnToCustomerPanel(MouseEvent event) {
		if (router.getRestaurantselectionController() == null) {
			AnchorPane mainContainer;
			RestaurantSelectionController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/bitemeRestaurantsPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.setRestaurants();
				controller.setItemsCounter();
				controller.setButtons();
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - Restaurants");
				stage.setScene(mainScene);
				stage.show();
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			router.getRestaurantselectionController().setItemsCounter();
			router.getRestaurantselectionController().setButtons();
			stage.setTitle("BiteMe - Restaurants");
			stage.setScene(router.getRestaurantselectionController().getScene());
			stage.show();
		}
	}

	
	/**
	 * changes the current page scene into the 'My cart' page
	 * @param lastPage
	 */
	public void changeToMyCart(String lastPage) {
		if (router.getMyCartController() == null) {
			AnchorPane mainContainer;
			MyCartController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("/gui/bitemeMyCartPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.displayOrder();
				controller.setLastPage(lastPage);
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("/gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - My Cart");
				stage.setScene(mainScene);
				
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		} else {
			router.getMyCartController().setAvatar();
			router.getMyCartController().displayOrder();
			router.getMyCartController().setLastPage(lastPage);
			stage.setTitle("BiteMe - My Cart");
			stage.setScene(router.getMyCartController().getScene());
		}
		stage.show();
	}

	
	/**
	 * Setting the order items
	 * @param order
	 */
	public void setBagItems(ArrayList<Product> products) {
		if(order == null) {
			order = new Order();
			order.setProducts(products);
			return;
		}
		order.setProducts(products);
	}

	/**
	 * Gets all of the order items
	 * @return order = list of all the products in the order
	 */
	public ArrayList<Product> getBagItems() {
		if (order == null || order.getProducts() == null) {
			return new ArrayList<>();
		}
		return order.getProducts();
	}

	/**
	 * Global method generating array of strings. Usage : combo box in
	 * deliveryMethodController & pickDateAndTimeController
	 * @param size
	 * @return String[]
	 */
	public String[] generator(int size) {
		String[] res = new String[size];
		for (int i = 0; i < res.length; i++) {
			if (i < 10) {
				res[i] = "0" + i;
			} else {
				res[i] = i + "";
			}
		}
		return res;
	}
	
	/**
	 * @return the current stage
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
	
	
	/**
	 * sets the order
	 * @param order
	 */
	public void setOrder(Order order) {
		this.order = order;
	}

	
	/**
	 * @return the order
	 */
	public Order getOrder() {
		return order;
	}

	/**
	 * @return the delivery
	 */
	public Delivery getDelivery() {
		return delivery;
	}
	
	/**
	 * @param logincontroller the loginController to set
	 */
	public void setLogincontroller(LoginController logincontroller) {
		Logincontroller = logincontroller;
	}

	/**
	 * @param enterguicontroller the enterguiController to set
	 */
	public void setEnterguicontroller(EnterGUIController enterGUIController) {
		Enterguicontroller = enterGUIController;
	}

	/**
	 * @param restaurantselectionController the restaurantselectionController to set
	 */
	public void setRestaurantselectionController(RestaurantSelectionController restaurantSelectionController) {
		RestaurantselectionController = restaurantSelectionController;
	}

	/**
	 * @param homePageController the homePageController to set
	 */
	public void setHomePageController(HomePageController homePageController) {
		HomePageController = homePageController;
	}
	/**
	 * @param managerPanelController the managerPanelController to set
	 */
	
	public void setManagerPanelController(ManagerPanelController managerPanelController) {
		ManagerPanelController = managerPanelController;
	}

	/**
	 * @param supplierPanelController the supplierPanelController to set
	 */
	public void setSupplierPanelController(SupplierPanelController supplierPanelController) {
		SupplierPanelController = supplierPanelController;
	}

	/**
	 * @param ceoPanelController the ceoPanelController to set
	 */
	public void setCEOPanelController(CeoPanelController ceoPanelController) {
		CEOPanelController = ceoPanelController;
	}

	/**
	 * @param restaurantMenuController the restaurantMenuController to set
	 */
	public void setRestaurantMenuController(RestaurantMenuController restaurantMenuController) {
		RestaurantMenuController = restaurantMenuController;
	}

	/**
	 * @param employerHRPanelController the employerHRPanelController to set
	 */
	public void setEmployerHRPanelController(EmployerHRPanelController employerHRPanelController) {
		EmployerHRPanelController = employerHRPanelController;
	}

	/**
	 * @param identifyController the identifyController to set
	 */
	public void setIdentifyController(IdentifyController identifyController) {
		IdentifyController = identifyController;
	}
	/**
	 * @return the uploadRevenueQuarterlyReportController
	 */
	public CreateRevenueQuarterlyReportController getCreateRevenueQuarterlyReportController() {
		return CreateRevenueQuarterlyReportController;
	}

	/**
	 * @param uploadRevenueQuarterlyReportController the uploadRevenueQuarterlyReportController to set
	 */
	public void setCreateRevenueQuarterlyReportController(
			CreateRevenueQuarterlyReportController createRevenueQuarterlyReportController) {
		CreateRevenueQuarterlyReportController = createRevenueQuarterlyReportController;
	}

	/**
	 * @param profileController the profileController to set
	 */
	public void setProfileController(ProfileController profileController) {
		ProfileController = profileController;
	}

	/**
	 * @param addNewSupplierController the addNewSupplierController to set
	 */
	public void setAddNewSupplierController(AddNewSupplierController addNewSupplierController) {
		AddNewSupplierController = addNewSupplierController;
	}

	/**
	 * @param viewMonthlyReportsController the viewMonthlyReportsController to set
	 */
	public void setViewMonthlyReportsController(ViewMonthlyReportsController viewMonthlyReportsController) {
		ViewMonthlyReportsController = viewMonthlyReportsController;
	}

	/**
	 * @param uploadQuarterlyReportController the uploadQuarterlyReportController to
	 *                                        set
	 */
	public void setUploadQuarterlyReportController(UploadQuarterlyReportController uploadQuarterlyReportController) {
		UploadQuarterlyReportController = uploadQuarterlyReportController;
	}

	/**
	 * @param updateUserInformationController the updateUserInformationController to
	 *                                        set
	 */
	public void setUpdateUserInformationController(UpdateUserInformationController updateUserInformationController) {
		UpdateUserInformationController = updateUserInformationController;
	}

	/**
	 * @param authorizedEmployerApprovalController the
	 *                                             authorizedEmployerApprovalController
	 *                                             to set
	 */
	public void setAuthorizedEmployerApprovalController(
			AuthorizedEmployerApprovalController authorizedEmployerApprovalController) {
		AuthorizedEmployerApprovalController = authorizedEmployerApprovalController;
	}

	/**
	 * @param openNewAccountController the openNewAccountController to set
	 */
	public void setOpenNewAccountController(OpenNewAccountController openNewAccountController) {
		OpenNewAccountController = openNewAccountController;
	}

	/**
	 * @param addNewItemController the addNewItemController to set
	 */
	public void setAddNewItemController(AddNewItemController addNewItemController) {
		AddNewItemController = addNewItemController;
	}

	/**
	 * @param updateMenuController the updateMenuController to set
	 */
	public void setUpdateMenuController(UpdateMenuController updateMenuController) {
		UpdateMenuController = updateMenuController;
	}

	/**
	 * @param editMenuItemController the editMenuItemController to set
	 */
	public void setEditMenuItemController(EditMenuItemController editMenuItemController) {
		EditMenuItemController = editMenuItemController;
	}

	/**
	 * @param supplierUpdateOrderController the supplierUpdateOrderController to set
	 */
	public void setSupplierUpdateOrderController(SupplierUpdateOrderController supplierUpdateOrderController) {
		SupplierUpdateOrderController = supplierUpdateOrderController;
	}

	/**
	 * @param registerEmployerAsLegacyController the
	 *                                           registerEmployerAsLegacyController
	 *                                           to set
	 */
	public void setRegisterEmployerAsLegacyController(
			RegisterEmployerAsLegacyController registerEmployerAsLegacyController) {
		RegisterEmployerAsLegacyController = registerEmployerAsLegacyController;
	}

	/**
	 * setter for confirmBusinessAccountController
	 * @param the confirmBusinessAccountController to set
	 */
	public void setConfirmBusinessAccountController(ConfirmBusinessAccountController confirmBusinessAccountController) {
		ConfirmBusinessAccountController = confirmBusinessAccountController;
	}

	
	/**
	 * @param pickDateAndTimeController the pickDateAndTimeController to set
	 */
	public void setPickDateAndTimeController(PickDateAndTimeController pickDateAndTimeController) {
		PickDateAndTimeController = pickDateAndTimeController;
	}

	/**
	 * @param sendMsgToCustomerController the sendMsgToCustomerController to set
	 */
	public void setSendMsgToCustomerController(SendMsgToCustomerController sendMsgToCustomerController) {
		SendMsgToCustomerController = sendMsgToCustomerController;
	}

	/**
	 * @param viewPDFQuarterlyReportController the viewPDFQuarterlyReportController
	 *                                         to set
	 */
	public void setViewPDFQuarterlyReportController(ViewPDFQuarterlyReportController viewPDFQuarterlyReportController) {
		ViewPDFQuarterlyReportController = viewPDFQuarterlyReportController;
	}
	
	/**

	 * @param viewRevenueQuarterlyReportController the viewRevenueQuarterlyReportController to set
	 */
	public void setViewRevenueQuarterlyReportController(
			ViewRevenueQuarterlyReportController viewRevenueQuarterlyReportController) {
		ViewRevenueQuarterlyReportController = viewRevenueQuarterlyReportController;
	}
	
	/**
	 * @return the viewRevenueQuarterlyReportController
	 */
	public ViewRevenueQuarterlyReportController getViewRevenueQuarterlyReportController() {
		return ViewRevenueQuarterlyReportController;
	}
	/**
	 * @param updateOrderTableController the updateOrderTableController to set
	 */
	public void setUpdateOrderTableController(UpdateOrderTableController updateOrderTableController) {
		UpdateOrderTableController = updateOrderTableController;
	}


	/**
	 * @return the Logincontroller
	 */
	public LoginController getLogincontroller() {
		return Logincontroller;
	}

	/**
	 * @return the Enterguicontroller
	 */
	public EnterGUIController getEnterguicontroller() {
		return Enterguicontroller;
	}

	/**
	 * @return the RestaurantselectionController
	 */
	public RestaurantSelectionController getRestaurantselectionController() {
		return RestaurantselectionController;
	}
	
	/**
	 * @return the HomePageController
	 */
	public HomePageController getHomePageController() {
		return HomePageController;
	}

	/**
	 * @return the ManagerPanelController
	 */
	public ManagerPanelController getManagerPanelController() {
		return ManagerPanelController;
	}

	/**
	 * @return the SupplierPanelController
	 */
	public SupplierPanelController getSupplierPanelController() {
		return SupplierPanelController;
	}

	/**
	 * @return the identifyController
	 */
	public IdentifyController getIdentifyController() {
		return IdentifyController;
	}

	/**
	 * @return the restaurantMenuController
	 */
	public RestaurantMenuController getRestaurantMenuController() {
		return RestaurantMenuController;
	}

	/**
	 * @return the restaurantMenuController
	 */
	public CeoPanelController getCEOPanelController() {
		return CEOPanelController;
	}

	/**
	 * @return the employerHRPanelController
	 */
	public EmployerHRPanelController getEmployerHRPanelController() {
		return EmployerHRPanelController;
	}

	/**
	 * @return the employerHRPanelController
	 */
	public ProfileController getProfileController() {
		return ProfileController;
	}

	/**
	 * @return the addNewSupplierController
	 */
	public AddNewSupplierController getAddNewSupplierController() {
		return AddNewSupplierController;
	}

	/**
	 * @return the viewMonthlyReportController
	 */
	public ViewMonthlyReportsController getViewMonthlyReportsController() {
		return ViewMonthlyReportsController;
	}

	/**
	 * @return the uploadQuarterlyReportController
	 */
	public UploadQuarterlyReportController getUploadQuarterlyReportController() {
		return UploadQuarterlyReportController;
	}

	/**
	 * @return the updateUserInformationController
	 */
	public UpdateUserInformationController getUpdateUserInformationController() {
		return UpdateUserInformationController;
	}

	/**
	 * @return the authorizedEmployerApprovalController
	 */
	public AuthorizedEmployerApprovalController getAuthorizedEmployerApprovalController() {
		return AuthorizedEmployerApprovalController;
	}

	/**
	 * @return the openNewAccountController
	 */
	public OpenNewAccountController getOpenNewAccountController() {
		return OpenNewAccountController;
	}

	/**
	 * @return the addNewItemController
	 */
	public AddNewItemController getAddNewItemController() {
		return AddNewItemController;
	}

	/**
	 * @return the updateMenuController
	 */
	public UpdateMenuController getUpdateMenuController() {
		return UpdateMenuController;
	}

	/**
	 * @return the editMenuItemController
	 */
	public EditMenuItemController getEditMenuItemController() {
		return EditMenuItemController;
	}

	/**
	 * @return the supplierUpdateOrderController
	 */
	public SupplierUpdateOrderController getSupplierUpdateOrderController() {
		return SupplierUpdateOrderController;
	}

	/**
	 * @return the registerEmployerAsLegacyController
	 */
	public RegisterEmployerAsLegacyController getRegisterEmployerAsLegacyController() {
		return RegisterEmployerAsLegacyController;
	}

	/**
	 * @return the confirmBusinessAccountController
	 */
	public ConfirmBusinessAccountController getConfirmBusinessAccountController() {
		return ConfirmBusinessAccountController;
	}
	/**
	 * @return the pickDateAndTimeController
	 */
	public PickDateAndTimeController getPickDateAndTimeController() {
		return PickDateAndTimeController;
	}

	/**
	 * @return the deliveryMethodController
	 */
	public DeliveryMethodController getDeliveryMethodController() {
		return DeliveryMethodController;
	}

	/**
	 * @param deliveryMethodController the deliveryMethodController to set
	 */
	public void setDeliveryMethodController(DeliveryMethodController deliveryMethodController) {
		DeliveryMethodController = deliveryMethodController;
	}

	/**
	 * @return the paymentController
	 */
	public PaymentController getPaymentController() {
		return PaymentController;
	}

	/**
	 * @param paymentController the paymentController to set
	 */
	public void setPaymentController(PaymentController paymentController) {
		PaymentController = paymentController;
	}

	/**
	 * @return the reviewOrderController
	 */
	public ReviewOrderController getReviewOrderController() {
		return ReviewOrderController;
	}

	/**
	 * @return the sendMsgToCustomerController
	 */
	public SendMsgToCustomerController getSendMsgToCustomerController() {
		return SendMsgToCustomerController;
	}

	/**
	 * @return the viewPDFQuarterlyReportController
	 */
	public ViewPDFQuarterlyReportController getViewPDFQuarterlyReportController() {
		return ViewPDFQuarterlyReportController;
	}
	
	/**
	 * @return the viewIncomeReceiptController
	 */
	public ViewIncomeReceiptController getViewIncomeReceiptController() {
		return ViewIncomeReceiptController;
	}

	/**
	 * @param reviewOrderController the reviewOrderController to set
	 */
	public void setReviewOrderController(ReviewOrderController reviewOrderController) {
		ReviewOrderController = reviewOrderController;
	}

	/**
	 * @return the myCartController
	 */
	public MyCartController getMyCartController() {
		return MyCartController;
	}

	/**
	 * @param myCartController the myCartController to set
	 */
	public void setMyCartController(MyCartController myCartController) {
		MyCartController = myCartController;
	}

	/**
	 * @return the orderReceivedController
	 */
	public OrderReceivedController getOrderReceivedController() {
		return OrderReceivedController;
	}

	/**
	 * @param orderReceivedController the orderReceivedController to set
	 */
	public void setOrderReceivedController(OrderReceivedController orderReceivedController) {
		OrderReceivedController = orderReceivedController;
	}

	/**
	 * @return the myOrdersController
	 */
	public MyOrdersController getMyOrdersController() {
		return MyOrdersController;
	}

	/**
	 * @param myOrdersController the myOrdersController to set
	 */
	public void setMyOrdersController(MyOrdersController myOrdersController) {
		MyOrdersController = myOrdersController;
	}
	
	/**
	 * @return the updateOrderTableController
	 */
	public UpdateOrderTableController getUpdateOrderTableController() {
		return UpdateOrderTableController;
	}

	/**
	 * @param delivery the delivery to set
	 */
	public void setDelivery(Delivery delivery) {
		this.delivery = delivery;
	}

	/**
	 * @return the orderDeliveryMethod
	 */
	public OrderDeliveryMethod getOrderDeliveryMethod() {
		return orderDeliveryMethod;
	}

	/**
	 * @param orderDeliveryMethod the orderDeliveryMethod to set
	 */
	public void setOrderDeliveryMethod(OrderDeliveryMethod orderDeliveryMethod) {
		this.orderDeliveryMethod = orderDeliveryMethod;
	}
	

	/**
	 * @return the openNewAccountFinalController
	 */
	public OpenNewAccountFinalController getOpenNewAccountFinalController() {
		return OpenNewAccountFinalController;
	}

	/**
	 * @param openNewAccountFinalController the openNewAccountFinalController to set
	 */
	public void setOpenNewAccountFinalController(OpenNewAccountFinalController openNewAccountFinalController) {
		OpenNewAccountFinalController = openNewAccountFinalController;
	}
	
	/**
	 * @return the addNewSupplierTableController
	 */
	public AddNewSupplierTableController getAddNewSupplierTableController() {
		return AddNewSupplierTableController;
	}

	/**
	 * @param addNewSupplierTableController the addNewSupplierTableController to set
	 */
	public void setAddNewSupplierTableController(AddNewSupplierTableController addNewSupplierTableController) {
		AddNewSupplierTableController = addNewSupplierTableController;
	}
	
	/**
	 * @param viewIncomeReceiptController the viewIncomeReceiptController to set
	 */
	public void setViewIncomeReceiptController(ViewIncomeReceiptController viewIncomeReceiptController) {
		ViewIncomeReceiptController = viewIncomeReceiptController;
	}

}
