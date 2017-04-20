package com.flyfox.ffplayer.util;

import javafx.scene.Node;
import javafx.scene.effect.Effect;

public class EventUtils {

	public static void mouseOn(Node node, Effect effect) {
		mouseOn(node, effect, null);
	}

	public static void mouseOn(Node node, Effect effectOn, Effect effectOff) {
		node.setOnMouseMoved(event -> {
			node.setEffect(effectOn);
		});
		node.setOnMouseExited(event -> {
			node.setEffect(effectOff);
		});
	}
}
