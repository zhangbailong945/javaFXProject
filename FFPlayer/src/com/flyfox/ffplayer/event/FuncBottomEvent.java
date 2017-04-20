package com.flyfox.ffplayer.event;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import com.flyfox.ffplayer.FFPlayerManager;
import com.flyfox.ffplayer.control.FuncBottom;
import com.flyfox.ffplayer.control.MusicList;
import com.flyfox.ffplayer.model.CacheUtils;
import com.flyfox.ffplayer.model.PlayMode;
import com.flyfox.ffplayer.util.ImageUtils;


/**
 * 底部，功能按钮事件
 * 
 * @author flyfox
 * @date 2014年12月8日
 */
public class FuncBottomEvent {
	
	FFPlayerManager manager;

	public FuncBottomEvent(FFPlayerManager manager) {
		this.manager = manager;
	}
	
	/**
	 * 底部操作事件初始化
	 */
	public void bind() {
		FuncBottom funcBottom = manager.getFuncBottom();
		MusicList musicList = manager.getMusicList();
		
		Label play = funcBottom.getPlay();
		Tooltip playTooltip = new Tooltip("播放");
		Tooltip pauseTooltip = new Tooltip("暂停");
		ImageView playIamgeView = (ImageView) play.getGraphic();

		play.setOnMouseClicked(event -> {
			if (musicList.isEmpty()) {
				return;
			}

			// 新加入数据,获取当前音乐，绑定
			if (manager.getModel() == null) {
				manager.setModel(musicList.get(CacheUtils.getIndex()));
				manager.bindMusic();
			}

			if (manager.getStatus() == MediaPlayer.Status.PLAYING) {
				// 暫停播放
				playIamgeView.setImage(ImageUtils.getImage("play.png"));
				play.setTooltip(playTooltip);
				manager.pause();
			} else {
				// 播放音訊
				manager.play();
				play.setTooltip(pauseTooltip);
				playIamgeView.setImage(ImageUtils.getImage("pause.png"));
			}
		});

		// 上一首
		funcBottom.getPre().setOnMouseClicked(event -> {
			if (musicList.isEmpty()) {
				return;
			}

			manager.playPre();
		});

		// 下一首
		funcBottom.getNext().setOnMouseClicked(event -> {
			if (musicList.isEmpty()) {
				return;
			}

			manager.playNext();
		});

		Slider timeSlider = funcBottom.getTimeSlider();
		// 鼠标按下，不再更新
		timeSlider.setOnMousePressed(event -> {
			timeSlider.setValueChanging(true);
		});

		// 鼠标释放，修改时间后恢复
		timeSlider.setOnMouseReleased(event -> {
			Platform.runLater(new Runnable() {
				public void run() {
					manager.seek(Duration.seconds(timeSlider.getValue()));

					manager.getLyricInfo().loadLyric(manager.getModel(), timeSlider.getValue());

					timeSlider.setValueChanging(false);
				}

			});
		});

		// 播放模式缓存
		funcBottom.getPlayModelChoiceBox().valueProperty().addListener((observable, oldValue, newValue) -> {
			CacheUtils.setPlayMode(PlayMode.modeName(newValue));
		});

		// 音量进行缓存
		funcBottom.getVolumeSlider().valueProperty().addListener((ob, oldValue, newValue) -> {
			CacheUtils.setVolume(newValue.doubleValue());
		});
	}
}
