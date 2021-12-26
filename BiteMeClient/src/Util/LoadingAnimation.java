package Util;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class LoadingAnimation {

	/**
	 * Processing animation.
	 * 
	 * @param Circle
	 */
	public static void LoadStart(Circle c) {
		RotateTransition rotateTransition = new RotateTransition(Duration.seconds(10), c);
		rotateTransition.setByAngle(360);
		rotateTransition.setDelay(Duration.seconds(0));
		rotateTransition.setRate(10);
		rotateTransition.setCycleCount(18);
		rotateTransition.play();
	}

	/**
	 * Overlay popup animation.
	 * 
	 * @param Pane
	 */
	public static void overlayPaneTransition(Pane overlayPane) {
		ScaleTransition st = new ScaleTransition(Duration.millis(300), overlayPane);
		st.setFromX(0.5);
		st.fromXProperty();
		st.setFromY(0.5);
		st.setToX(1);
		st.setToY(1);
		st.play();
	}
}
