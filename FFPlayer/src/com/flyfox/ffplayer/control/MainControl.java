package com.flyfox.ffplayer.control;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.effect.Light.Point;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import com.flyfox.ffplayer.FFPlayerManager;
import com.flyfox.ffplayer.model.FFPlayer;
import com.flyfox.ffplayer.util.FFPlayerUtils;
import com.flyfox.ffplayer.util.ImageUtils;

public class MainControl {

	private static final double HEIGHT = 600;
	private static final double WIGHT = 1000;
	private static Point point = new Point();
	FFPlayerManager ffPlayer;
	Stage stage;
	BorderPane root;
	private ParallelTransition transition = null;
	Pane bottom;
	TableView<FFPlayer> musicList;
	Pane info;
	Pane topTitle;

	public MainControl(Stage stage) {
		this.stage = stage;
	}

	public void init() {
		root = new BorderPane();
		Scene scene = new Scene(root, WIGHT, HEIGHT);
		scene.getStylesheets().add("resources/ffplayer.css");
		// 初始化
		ffPlayer = new FFPlayerManager(stage);
		bottom = ffPlayer.getFuncBottom().getNode();
		musicList = ffPlayer.getMusicList().getNode();
		info = ffPlayer.getLyricInfo().getNode();
		topTitle = ffPlayer.getTopTitle().getNode();

		initLoyout();
		initEvent();

		stage.setFullScreen(false);
		stage.setResizable(false);
		stage.setTitle("FFPlayer");
		stage.setScene(scene);
		stage.initStyle(StageStyle.TRANSPARENT);
		// stage.show();

	}

	private void initLoyout() {
		final HBox vbox = new HBox();
		vbox.setSpacing(5);

		vbox.getChildren().addAll(musicList, info);
		root.setTop(topTitle);
		root.setCenter(vbox);
		root.setBottom(bottom);

		// 加入背景
		BackgroundImage bgImage = new BackgroundImage(ImageUtils.getImage("bg.jpg"), BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
		Background bg = new Background(bgImage);
		root.setBackground(bg);

	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		// 底部功能区展示
		FadeTransition bottomShow = fade(bottom, 1000, 1);
		FadeTransition bottomHide = fade(bottom, 1000, 0);

		// 播放列表展示
		FadeTransition musicListShow = fade(musicList, 1000, 0.8);
		FadeTransition musicListHide = fade(musicList, 1000, 0);

		// 拖拽实现
		root.setOnMousePressed(event -> {
			point.setX(stage.getX() - event.getScreenX());
			point.setY(stage.getY() - event.getScreenY());
		});

		root.setOnMouseReleased(event -> {
			if (stage.getY() < 0) {
				stage.setY(0);
			}
		});

		root.setOnMouseDragged(event -> {
			if (stage.isFullScreen()) {
				return;
			}

			double x = (event.getScreenX() + point.getX());
			double y = (event.getScreenY() + point.getY());

			Platform.runLater(() -> {
				stage.setX(x);
				stage.setY(y);
			});

		});

		// 双击全屏
		root.setOnMouseClicked(event -> {
			if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2) {
				stage.setFullScreen(!stage.isFullScreen());

				if (transition != null)
					transition.stop();
				if (stage.isFullScreen()) {
					transition = new ParallelTransition(bottomHide, musicListHide);
				} else {
					transition = new ParallelTransition(bottomShow, musicListShow);
				}
				transition.play();
			}
		});

		// 去除全屏提示
		stage.setFullScreenExitHint("");

		// 最小化
		ffPlayer.getTopTitle().getMinimize().setOnMouseClicked(event -> {
			stage.setIconified(true);
		});

		// 关闭按钮
		ffPlayer.getTopTitle().getClose().setOnMouseClicked(event -> {
			Event.fireEvent(stage, new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
		});

		// 关闭
		stage.setOnCloseRequest(event -> {
			exit();
		});

		// 划入显示菜单
		root.setOnMouseEntered(event -> {
			if (transition != null)
				transition.stop();

			transition = new ParallelTransition(bottomShow, musicListShow);
			transition.play();
		});

		// 划出不显示菜单
		root.setOnMouseExited(event -> {
			// 解决音乐列表右键，隐藏问题
			if (0 < event.getX() && event.getX() < stage.getWidth() //
					&& 0 < event.getY() && event.getY() < stage.getHeight()) {
				return;
			}

			if (transition != null)
				transition.stop();

			transition = new ParallelTransition(bottomHide, musicListHide);
			transition.play();
		});

		// 拖拽需要显示出播放列表
		root.setOnDragOver(event -> {
			if (transition != null)
				transition.stop();

			transition = new ParallelTransition(musicListShow);
			transition.play();
		});

		// 定时任务
		int duration = 5;
		Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);

		KeyFrame keyFrame = new KeyFrame(Duration.millis(duration), event -> {
			if (stage.isFullScreen()) {
				transition = new ParallelTransition(bottomHide, musicListHide);
				transition.play();
			}
			bottom.setDisable(stage.isFullScreen());
			musicList.setDisable(stage.isFullScreen());
		});
		timeline.getKeyFrames().add(keyFrame);
		timeline.play();
	}

	public static void exit() {
		while (!FFPlayerUtils.CLOSE.get()) {
			try {
				Thread.sleep(100L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		Platform.exit();
		System.exit(0);
	}

	/**
	 * fade 特效
	 * 
	 * @param node
	 * @param time
	 * @param value
	 * @return
	 */
	private FadeTransition fade(Node node, double time, double value) {
		FadeTransition musicListShow = new FadeTransition();
		musicListShow.setNode(node);
		musicListShow.setDuration(Duration.millis(500));
		musicListShow.setToValue(value);
		musicListShow.setInterpolator(Interpolator.EASE_OUT);
		return musicListShow;
	}
}
