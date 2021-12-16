package Util;

import Controls.Router;
import Entities.Customer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;

public class QRReader {
	private static Object readerMonitor = new Object();

	/**
	 * Module simulating QR Code reader.
	 * 
	 * @param user
	 * 
	 * @return String
	 */
	public static boolean ReadQRCode(Customer customer) {
		String qrCode = customer.getW4c().getQRCode();
		if (qrCode == null || qrCode.equals("")) {
			return false;
		}
		final IntegerProperty i = new SimpleIntegerProperty(0);
		Timeline timeline = new Timeline();
		KeyFrame keyFrame = new KeyFrame(Duration.millis(100), e -> {
			if (i.get() < qrCode.length()) {
				Platform.runLater(() -> {
					Router.getInstance().getIdentifyController().updateTextField(qrCode.substring(0, i.get()));
				});
				i.set(i.get() + 1);
			}
		});
		timeline.getKeyFrames().add(keyFrame);
		timeline.setCycleCount(qrCode.length());
		timeline.play();
		timeline.setOnFinished(e -> Router.getInstance().getIdentifyController().changeToRestaurantMenuPage());
		return true;
	}
}
