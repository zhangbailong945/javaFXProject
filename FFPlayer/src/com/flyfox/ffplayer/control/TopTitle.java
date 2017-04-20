package com.flyfox.ffplayer.control;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import com.flyfox.ffplayer.util.ImageUtils;

public class TopTitle {

	BorderPane pane;
	Label logo; // logo
	Label minimize; // 最小化
	Label close; // 下一首

	public TopTitle() {
		init();
	}

	public void init() {
		pane = new BorderPane();
		pane.getStyleClass().add("ffplayer-topTitle");
		logo = new Label("ffplayer");
		logo.getStyleClass().add("ffplayer-logo");

		ImageView minimizeIamgeView = ImageUtils.getImageView("minimize.png", 18, 18);
		minimize = new Label("", minimizeIamgeView);
		minimize.getStyleClass().addAll("ffplayer-top-min", "ffplayer-button");

		ImageView closeIamgeView = ImageUtils.getImageView("close.png", 18, 18);
		close = new Label("", closeIamgeView);
		close.getStyleClass().addAll("ffplayer-top-close", "ffplayer-button");

		HBox hBox = new HBox();
		hBox.getChildren().addAll(minimize, close);
		pane.setLeft(logo);
		pane.setRight(hBox);
	}

	public Label getMinimize() {
		return minimize;
	}

	public Label getClose() {
		return close;
	}

	public Pane getNode() {
		return pane;
	}
}
