package form.login.obho;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class AnimatedButton extends Button {
	private static final int R = 50, G = 255, B = 100;

	private Timeline t;
	private KeyValue kv, kv0;
	private KeyFrame kf, kf0;
	private AnimationTimer at;
	private SimpleDoubleProperty alpha;

	public AnimatedButton() {
		super();

		t = new Timeline();
		at = new AnimationTimer() {
			@Override
			public void handle(long now) {
				AnimatedButton.this.setStyle("-fx-background-color: rgba(" + R + ", " + G + ", " + B + ", " + alpha.get() + ");");
			}
		};

		alpha = new SimpleDoubleProperty(0.3);
		t = new Timeline();
		kv = new KeyValue(alpha, 0.7d, Interpolator.SPLINE(1, 0, 0, 1));
		kv0 = new KeyValue(alpha, 0.3d, Interpolator.SPLINE(1, 0, 0, 1));
		kf = new KeyFrame(Duration.seconds(0.2), kv);
		kf0 = new KeyFrame(Duration.seconds(0.2), kv0);

		this.focusedProperty().addListener(e -> {
			if (this.isFocused()) focus();
			else unfocus(); 
		});

		at.start();
	}

	public AnimatedButton(String text) {
		this();
		setText(text);
	}

	private void focus() {
		Platform.runLater(() -> {
			t.stop();
			t.getKeyFrames().clear();
			t.getKeyFrames().add(kf);
			t.play();
		});
	}

	private void unfocus() {
		Platform.runLater(() -> {
			t.stop();
			t.getKeyFrames().clear();
			t.getKeyFrames().add(kf0);
			t.play();
		});
	}
}
