package Util;

import javafx.animation.RotateTransition;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class LoadingAnimation {

	private static RotateTransition rotateTransition;

	public static void LoadStart(Circle c) {
		rotateTransition = new RotateTransition(Duration.seconds(10), c);
		rotateTransition.setByAngle(360);
		rotateTransition.setDelay(Duration.seconds(0));
		rotateTransition.setRate(10);
		rotateTransition.setCycleCount(18);
		rotateTransition.play();
	}
}
