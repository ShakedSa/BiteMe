package Controls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	private loginController Logincontroller;
	private enterGUIController Enterguicontroller;
	private myCartController MyCartController;
	private myOrdersController MyOrdersController;
	private homePageController HomePageController;
	private managerPanelController ManagerPanelController;
	private supplierPanelController SupplierPanelController;
	private ceoPanelController CEOPanelController;
	private profileController ProfileController;
	// Order Process pages:
	private restaurantSelectionController RestaurantselectionController;
	private identifyController IdentifyController;
	private restaurantMenuController RestaurantMenuController;
	private pickDateAndTimeController PickDateAndTimeController;
	private deliveryMethodController DeliveryMethodController;
	private reviewOrderController ReviewOrderController;
	private paymentController PaymentController;
	private orderReceivedController OrderReceivedController;
	// Manager Panel pages:
	private addNewSupplierController AddNewSupplierController;
	private addNewSupplierTableController AddNewSupplierTableController;
	private viewMonthlyReportsController ViewMonthlyReportsController;
	private uploadQuarterlyReportController UploadQuarterlyReportController;
	private updateUserInformationController UpdateUserInformationController;
	private authorizedEmployerApprovalController AuthorizedEmployerApprovalController;
	private openNewAccountController OpenNewAccountController;
	private openNewAccountFinalController OpenNewAccountFinalController;
	private createRevenueQuarterlyReportController CreateRevenueQuarterlyReportController;
	// Supplier Panel pages:
	private addNewItemController AddNewItemController;
	private updateMenuController UpdateMenuController;
	private editMenuItemController EditMenuItemController;
	private supplierUpdateOrderController SupplierUpdateOrderController;
	private sendMsgToCustomerController SendMsgToCustomerController;
	private updateOrderTableController UpdateOrderTableController;
	// Employer HR Panel pages:
	private employerHRPanelController EmployerHRPanelController;
	private registerEmployerAsLegacyController RegisterEmployerAsLegacyController;
	private confirmBusinessAccountController ConfirmBusinessAccountController;
	// CEO Panel pages:
	private viewPDFQuarterlyReportController ViewPDFQuarterlyReportController;
	private viewRevenueQuarterlyReportController ViewRevenueQuarterlyReportController;


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
	 * @return the uploadRevenueQuarterlyReportController
	 */
	public createRevenueQuarterlyReportController getCreateRevenueQuarterlyReportController() {
		return CreateRevenueQuarterlyReportController;
	}

	/**
	 * @param uploadRevenueQuarterlyReportController the uploadRevenueQuarterlyReportController to set
	 */
	public void setCreateRevenueQuarterlyReportController(
			createRevenueQuarterlyReportController createRevenueQuarterlyReportController) {
		CreateRevenueQuarterlyReportController = createRevenueQuarterlyReportController;
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
		RegisterEmployerAsLegacyController = registerEmployerAsLegacyController;
	}

	/**
	 * @param confirmBusinessAccountController the confirmBusinessAccountController
	 *                                         to set
	 */
	public void setConfirmBusinessAccountController(confirmBusinessAccountController confirmBusinessAccountController) {
		ConfirmBusinessAccountController = confirmBusinessAccountController;
	}

	/**
	 * @param pickDateAndTimeController the pickDateAndTimeController to set
	 */
	public void setPickDateAndTimeController(pickDateAndTimeController pickDateAndTimeController) {
		PickDateAndTimeController = pickDateAndTimeController;
	}

	/**
	 * @param sendMsgToCustomerController the sendMsgToCustomerController to set
	 */
	public void setSendMsgToCustomerController(sendMsgToCustomerController sendMsgToCustomerController) {
		SendMsgToCustomerController = sendMsgToCustomerController;
	}

	/**
	 * @param viewPDFQuarterlyReportController the viewPDFQuarterlyReportController
	 *                                         to set
	 */
	public void setViewPDFQuarterlyReportController(viewPDFQuarterlyReportController viewPDFQuarterlyReportController) {
		ViewPDFQuarterlyReportController = viewPDFQuarterlyReportController;
	}
	
	/**

	 * @param viewRevenueQuarterlyReportController the viewRevenueQuarterlyReportController to set
	 */
	public void setViewRevenueQuarterlyReportController(
			viewRevenueQuarterlyReportController viewRevenueQuarterlyReportController) {
		ViewRevenueQuarterlyReportController = viewRevenueQuarterlyReportController;
	}
	
	/**
	 * @return the viewRevenueQuarterlyReportController
	 */
	public viewRevenueQuarterlyReportController getViewRevenueQuarterlyReportController() {
		return ViewRevenueQuarterlyReportController;
	}
	
	 * @param updateOrderTableController the updateOrderTableController to set
	 */
	public void setUpdateOrderTableController(updateOrderTableController updateOrderTableController) {
		UpdateOrderTableController = updateOrderTableController;


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
		return RegisterEmployerAsLegacyController;
	}

	/**
	 * @return the confirmBusinessAccountController
	 */
	public confirmBusinessAccountController getConfirmBusinessAccountController() {
		return ConfirmBusinessAccountController;
	}

	/**
	 * @return the pickDateAndTimeController
	 */
	public pickDateAndTimeController getPickDateAndTimeController() {
		return PickDateAndTimeController;
	}

	/**
	 * @return the deliveryMethodController
	 */
	public deliveryMethodController getDeliveryMethodController() {
		return DeliveryMethodController;
	}

	/**
	 * @param deliveryMethodController the deliveryMethodController to set
	 */
	public void setDeliveryMethodController(deliveryMethodController deliveryMethodController) {
		DeliveryMethodController = deliveryMethodController;
	}

	/**
	 * @return the paymentController
	 */
	public paymentController getPaymentController() {
		return PaymentController;
	}

	/**
	 * @param paymentController the paymentController to set
	 */
	public void setPaymentController(paymentController paymentController) {
		PaymentController = paymentController;
	}

	/**
	 * @return the reviewOrderController
	 */
	public reviewOrderController getReviewOrderController() {
		return ReviewOrderController;
	}

	/**
	 * @return the sendMsgToCustomerController
	 */
	public sendMsgToCustomerController getSendMsgToCustomerController() {
		return SendMsgToCustomerController;
	}

	/**
	 * @return the viewPDFQuarterlyReportController
	 */
	public viewPDFQuarterlyReportController getViewPDFQuarterlyReportController() {
		return ViewPDFQuarterlyReportController;
	}

	/**
	 * @param reviewOrderController the reviewOrderController to set
	 */
	public void setReviewOrderController(reviewOrderController reviewOrderController) {
		ReviewOrderController = reviewOrderController;
	}

	/**
	 * @return the myCartController
	 */
	public myCartController getMyCartController() {
		return MyCartController;
	}

	/**
	 * @param myCartController the myCartController to set
	 */
	public void setMyCartController(myCartController myCartController) {
		MyCartController = myCartController;
	}

	/**
	 * @return the orderReceivedController
	 */
	public orderReceivedController getOrderReceivedController() {
		return OrderReceivedController;
	}

	/**
	 * @param orderReceivedController the orderReceivedController to set
	 */
	public void setOrderReceivedController(orderReceivedController orderReceivedController) {
		OrderReceivedController = orderReceivedController;
	}

	/**
	 * @return the myOrdersController
	 */
	public myOrdersController getMyOrdersController() {
		return MyOrdersController;
	}

	/**
	 * @param myOrdersController the myOrdersController to set
	 */
	public void setMyOrdersController(myOrdersController myOrdersController) {
		MyOrdersController = myOrdersController;
	}
	
	/**
	 * @return the updateOrderTableController
	 */
	public updateOrderTableController getUpdateOrderTableController() {
		return UpdateOrderTableController;
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
				ProfileController.setItemsCounter();
				ProfileController.setLastScene(stage.getScene(), stage.getTitle());
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
			ProfileController.setLastScene(stage.getScene(), stage.getTitle());
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
	
	public void setArrow(Rectangle arrow, int rotationDegree) {
		ImagePattern pattern = new ImagePattern(new Image(getClass().getResource("../images/arrow.gif").toString()));
		arrow.setFill(pattern);
		arrow.setStyle("-fx-stroke: null;-fx-cursor: hand");
		arrow.setRotate(rotationDegree);
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
				controller.setImage();
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
				controller.setItemsCounter();
				controller.setButtons();
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
			router.getRestaurantselectionController().setItemsCounter();
			router.getRestaurantselectionController().setButtons();
			stage.setTitle("BiteMe - Restaurants");
			stage.setScene(router.getRestaurantselectionController().getScene());
			stage.show();
		}
	}

	public void changeToMyCart(String lastPage) {
		if (router.getMyCartController() == null) {
			AnchorPane mainContainer;
			myCartController controller;
			try {
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("../gui/bitemeMyCartPage.fxml"));
				mainContainer = loader.load();
				controller = loader.getController();
				controller.setAvatar();
				controller.displayOrder();
				controller.setLastPage(lastPage);
				Scene mainScene = new Scene(mainContainer);
				mainScene.getStylesheets().add(getClass().getResource("../gui/style.css").toExternalForm());
				controller.setScene(mainScene);
				stage.setTitle("BiteMe - My Cart");
				stage.setScene(mainScene);
				stage.show();
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
			stage.show();
		}
	}

	/**
	 * Setting the order items
	 * 
	 * @param order
	 */
	public void setBagItems(ArrayList<Product> products) {
		if(order == null) {
			return;
		}
		if (order.getProducts() == null || products == null) {
			order.setProducts(products);
			return;
		}
		List<Product> newProducts = order.getProducts().stream().filter(p -> !products.contains(p))
				.collect(Collectors.toList());
		newProducts.addAll(products);
		order.setProducts((ArrayList<Product>) newProducts);
	}

	/**
	 * Getting the order items
	 * 
	 * @return order
	 */
	public ArrayList<Product> getBagItems() {
		if (order == null || order.getProducts() == null) {
			return new ArrayList<>();
		}
		return order.getProducts();
	}

	public void setOrder(Order order) {
		this.order = order;
	}

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
	public openNewAccountFinalController getOpenNewAccountFinalController() {
		return OpenNewAccountFinalController;
	}

	/**
	 * @param openNewAccountFinalController the openNewAccountFinalController to set
	 */
	public void setOpenNewAccountFinalController(openNewAccountFinalController openNewAccountFinalController) {
		OpenNewAccountFinalController = openNewAccountFinalController;
	}
	
	

	/**
	 * @return the addNewSupplierTableController
	 */
	public addNewSupplierTableController getAddNewSupplierTableController() {
		return AddNewSupplierTableController;
	}

	/**
	 * @param addNewSupplierTableController the addNewSupplierTableController to set
	 */
	public void setAddNewSupplierTableController(addNewSupplierTableController addNewSupplierTableController) {
		AddNewSupplierTableController = addNewSupplierTableController;
	}

	/**
	 * Global method generating array of strings. Usage : combo box in
	 * deliveryMethodController & pickDateAndTimeController
	 * 
	 * @param size
	 * 
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

}
