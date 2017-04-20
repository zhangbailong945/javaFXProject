package com.flyfox.ffplayer.control;

import java.util.List;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import com.flyfox.ffplayer.lyric.LyricManager;
import com.flyfox.ffplayer.lyric.model.Lyric;
import com.flyfox.ffplayer.lyric.model.LyricItem;
import com.flyfox.ffplayer.model.FFPlayer;
import com.flyfox.util.NumberUtils;

/**
 * 歌词展示
 * 
 * @author flyfox
 * @date 2014年11月7日
 */
public class LyricInfo {

	BorderPane pane;
	VBox lyricPane;
	Text lyric;

	public LyricInfo() {
		init();
	}

	private void init() {
		pane = new BorderPane();
		pane.getStyleClass().add("ffplayer-lyric");

		lyricPane = new VBox();
		lyricPane.setAlignment(Pos.CENTER);
		pane.setCenter(lyricPane);
	}

	public void loadLyric(FFPlayer ffplayer, double time) {
		lyricPane.getChildren().clear();

		long nowTime = (long) (time * 1000);
		Lyric lyric = LyricManager.load(ffplayer);
		int index = 0;

		if (lyric == null) {
			return;
		}

		if (NumberUtils.parseInt(lyric.getInfo()) < 0) {
			System.err.println("歌词加载失败：" + lyric.getInfo());
			return;
		}

		List<LyricItem> list = lyric.getLyricItems();
		int size = list.size();

		// 获取当前位置
		for (int i = size - 1; i >= 0; i--) {
			LyricItem item = lyric.getLyricItems().get(i);
			long cha = nowTime - item.getTime();
			if (100 < cha) {
				index = i;
				break;
			}
		}

		// 展示歌词
		for (int i = 0; i < 13; i++) {
			// 太靠前
			if (index - 6 + i < 0) {
				continue;
			}
			// 超过最后的了
			if (index - 6 + i >= size) {
				break;
			}

			LyricItem item = list.get(index - 6 + i);

			if (i < 6) { // 前几句
				getOtherText(item, i);
			} else if (i == 6) { // 当前歌词
				getCurrentText(item);
			} else { // 后几句
				getOtherText(item, 12 - i);
			}

		}

	}

	/**
	 * 当前歌词
	 * 
	 * @param item
	 */
	private void getCurrentText(LyricItem item) {
		HBox box = new HBox();
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(8, 0, 8, 0));

		Text text = new Text(item.getContent());
		text.setFont(Font.font("Arial", 24));
		text.setFill(Color.ORANGE);
		text.setStrokeWidth(0.2);
		text.setStroke(Color.WHITE);
		box.getChildren().add(text);
		lyricPane.getChildren().add(box);
	}

	/**
	 * 其他歌词
	 * 
	 * @param item
	 * @param i
	 */
	private void getOtherText(LyricItem item, int i) {
		HBox box = new HBox();
		box.setAlignment(Pos.CENTER);
		box.setPadding(new Insets(i * 0.8, 0, i * 0.8, 0));

		Text text = new Text(item.getContent());
		text.setFont(Font.font("Arial", 15 + i));
		text.setOpacity(0.25 * (i + 1));
		text.setFill(Color.WHITE);
		text.setStrokeWidth(0.2);
		text.setStroke(Color.WHITE);
		box.getChildren().add(text);
		lyricPane.getChildren().add(box);
	}

	public Pane getNode() {
		return pane;
	}
}
