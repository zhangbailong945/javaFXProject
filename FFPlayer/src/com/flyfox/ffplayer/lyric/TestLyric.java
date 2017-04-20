package com.flyfox.ffplayer.lyric;

import com.flyfox.ffplayer.lyric.baidu.BaiduLyricLoader;
import com.flyfox.ffplayer.lyric.qq.QQLyricLoader;
import com.flyfox.ffplayer.model.FFPlayer;

public class TestLyric {

	public static void main(String[] args) {
		//baidu();
		// qq();
		//loadLyric();
		load();
	}

	public static void load() {
		FFPlayer ffPlayer = getFFPlayer();
		new QQLyricLoader().load(ffPlayer);
	}

	public static void loadLyric() {
		FFPlayer ffPlayer = getFFPlayer();
		new QQLyricLoader().loadLyric(ffPlayer);
	}

	public static void qq() {
		FFPlayer ffPlayer = getFFPlayer();
		System.out.println(new QQLyricLoader().downLyric(ffPlayer));
	}

	public static void baidu() {
		FFPlayer ffPlayer = getFFPlayer();
		System.out.println(new BaiduLyricLoader().downLyric(ffPlayer));
	}

	private static FFPlayer getFFPlayer() {
		FFPlayer ffPlayer = new FFPlayer();
		ffPlayer.setTitle("安静");
		ffPlayer.setArtist("周杰伦");
		return ffPlayer;
	}
	
}
