package com.flyfox.ffplayer.control;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import com.flyfox.ffplayer.model.PlayMode;
import com.flyfox.ffplayer.util.ImageUtils;

/**
 * 底部，功能按钮
 * 
 * @author flyfox
 * @date 2014年11月6日
 */
public class FuncBottom {

	HBox bottom;

	Label play; // 播放，暂停
	Label pre; // 上一首
	Label next; // 下一首

	Label title; // 名称展示
	final Slider timeSlider = new Slider(); // 时间滑块
	Label timeLabel; // 时间显示

	ChoiceBox<String> playModelChoiceBox;

	Label volumeLabel; // 音量
	final Slider volumeSlider = new Slider(); // 音量滑块

	public FuncBottom() {
		init();
	}

	public void init() {
		initPlayButton();
		initTime();
		initPlayMode();
		initVolume();

		initLayout();
	}

	/**
	 * 初始化布局
	 */
	private void initLayout() {
		// 布局
		bottom = new HBox();
		bottom.getStyleClass().add("ffplayer-bottom");

		bottom.getChildren().addAll(pre, play, next);

		HBox infoBox = new HBox();
		HBox timeBox = new HBox();
		VBox centerBox = new VBox();

		infoBox.getChildren().add(title);
		timeBox.getChildren().addAll(timeSlider, timeLabel);
		centerBox.getChildren().addAll(infoBox, timeBox);
		bottom.getChildren().add(centerBox);

		VBox playModelBox = new VBox();
		playModelBox.getStyleClass().add("ffplayer-playModel-vbox");
		playModelBox.getChildren().add(playModelChoiceBox);
		bottom.getChildren().addAll(playModelBox, volumeLabel, volumeSlider);
	}

	public HBox getNode() {
		return bottom;
	}

	/**
	 * 加入按钮，播放，上一首，下一首
	 * 
	 * @param bottomPane
	 */
	private void initPlayButton() {
		// 播放 暂停
		Tooltip pauseTooltip = new Tooltip("暂停");
		Tooltip preTooltip = new Tooltip("上一首");
		Tooltip nextTooltip = new Tooltip("下一首");

		ImageView playIamgeView = ImageUtils.getImageView("pause.png", 48, 48);
		play = new Label("", playIamgeView);
		play.getStyleClass().addAll("ffplayer-play", "ffplayer-button");
		play.setTooltip(pauseTooltip);

		// 上一首
		ImageView preIamgeView = ImageUtils.getImageView("pre.png", 40, 40);
		pre = new Label("", preIamgeView);
		pre.getStyleClass().addAll("ffplayer-pre", "ffplayer-button");
		pre.setTooltip(preTooltip);

		// 下一首
		ImageView nextIamgeView = ImageUtils.getImageView("next.png", 40, 40);
		next = new Label("", nextIamgeView);
		next.getStyleClass().addAll("ffplayer-next", "ffplayer-button");
		next.setTooltip(nextTooltip);

	}

	/**
	 * 时间
	 */
	private void initTime() {
		title = new Label();
		title.getStyleClass().add("ffplayer-title-label");

		timeSlider.getStyleClass().add("ffplayer-time-slider");
		timeSlider.setMin(0.0);
		timeSlider.setMax(1.0);
		timeSlider.setValue(0.0);

		timeLabel = new Label("00:00/00:00");
		timeLabel.getStyleClass().add("ffplayer-time-label");

	}

	/**
	 * 播放模式
	 */
	private void initPlayMode() {
		ObservableList<String> list = FXCollections.observableArrayList(PlayMode.CYCLING.getName(),
				PlayMode.RANDOM.getName(), PlayMode.ORDER.getName(), PlayMode.ONE_CYCLING.getName());
		playModelChoiceBox = new ChoiceBox<String>();
		playModelChoiceBox.setItems(list);
		// 默认单曲循环
		playModelChoiceBox.setValue(PlayMode.ONE_CYCLING.getName());
		playModelChoiceBox.getStyleClass().add("ffplayer-playModel-choiceBox");
	}

	/**
	 * 声音
	 */
	private void initVolume() {
		volumeLabel = new Label("音量");
		volumeLabel.getStyleClass().add("ffplayer-volume-label");

		volumeSlider.setMin(0.0);
		volumeSlider.setMax(1.0);
		volumeSlider.setValue(1.0);
		volumeSlider.getStyleClass().add("ffplayer-volume-slider");

	}

	public Label getPlay() {
		return play;
	}

	public Label getPre() {
		return pre;
	}

	public Label getNext() {
		return next;
	}

	public Label getName() {
		return title;
	}

	public Slider getTimeSlider() {
		return timeSlider;
	}

	public Label getTimeLabel() {
		return timeLabel;
	}

	public Label getVolumeLabel() {
		return volumeLabel;
	}

	public Slider getVolumeSlider() {
		return volumeSlider;
	}

	public ChoiceBox<String> getPlayModelChoiceBox() {
		return playModelChoiceBox;
	}

}
