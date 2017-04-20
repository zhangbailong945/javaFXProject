package com.flyfox.ffplayer.event;

import javafx.application.Platform;
import javafx.scene.media.MediaPlayer;

import com.flyfox.ffplayer.FFPlayerManager;
import com.flyfox.ffplayer.control.FuncBottom;
import com.flyfox.ffplayer.control.MusicList;
import com.flyfox.ffplayer.model.CacheUtils;
import com.flyfox.ffplayer.model.FFPlayer;
import com.flyfox.ffplayer.util.FFPlayerUtils;

/**
 * 歌曲事件绑定
 * 
 * @author flyfox
 * @date 2014年12月8日
 */
public class MusicEvent {

	FFPlayerManager manager;

	public MusicEvent(FFPlayerManager manager) {
		this.manager = manager;
	}

	/**
	 * mediaplayer事件绑定
	 */
	public void bind() {
		MediaPlayer mediaPlayer = manager.getMediaPlayer();
		FuncBottom funcBottom = manager.getFuncBottom();
		MusicList musicList = manager.getMusicList();
		FFPlayer model = manager.getModel();
		
		// 如果禁用，恢复
		if (funcBottom.getTimeSlider().isDisable()) {
			funcBottom.getTimeSlider().setDisable(false);
		}

		// 初始化方法
		mediaPlayer.setOnReady(() -> {
			if (isTimeUnkown()) {
				manager.getModel().setTotleTime(mediaPlayer.getTotalDuration());
			}
			if (!isTimeUnkown()) {
				funcBottom.getTimeSlider().setMax(model.getTotleTime().toSeconds());
			}
			// 播放
			// mediaPlayer.setAutoPlay(true);
			manager.getStage().show();
			});

		mediaPlayer.setOnPlaying(() -> {
			funcBottom.getName().setText(musicList.get(CacheUtils.getIndex()).getTitle());

			if (isTimeUnkown()) {
				model.setTotleTime(mediaPlayer.getTotalDuration());
			}
			if (!isTimeUnkown()) {
				funcBottom.getTimeSlider().setMax(model.getTotleTime().toSeconds());
			}

			// 播放初始化
			manager.getLyricInfo().loadLyric(model, mediaPlayer.getCurrentTime().toSeconds());
			});

		mediaPlayer.setOnEndOfMedia(() -> {
			manager.playEnd();
		});

		mediaPlayer.setOnStopped(() -> {
			// System.out.println("stop!!!!");
			});

		// 监听media播放时间，同步展示
		mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
			Platform.runLater(new Runnable() {
				public void run() {
					if (musicList.isEmpty()) {
						return;
					}

					// 展示歌词
					manager.getLyricInfo().loadLyric(model, newValue.toSeconds());

					funcBottom.getTimeLabel().setText(
							FFPlayerUtils.formatTime(newValue) + "/" + FFPlayerUtils.formatTime(model.getTotleTime()));
					if (!funcBottom.getTimeSlider().isDisabled() && !funcBottom.getTimeSlider().isValueChanging()) {
						funcBottom.getTimeSlider().setValue(newValue.toSeconds());
					}
				}

			});

		});

		// 音量滑块和media声音绑定
		if (CacheUtils.getVolume() >= 0) {
			mediaPlayer.setVolume(CacheUtils.getVolume());
		}
		funcBottom.getVolumeSlider().valueProperty().bindBidirectional(mediaPlayer.volumeProperty());
	}

	public boolean isTimeUnkown() {
		return manager.getModel().getTotleTime().isUnknown();
	}
}
