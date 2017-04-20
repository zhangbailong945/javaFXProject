package com.flyfox.ffplayer.lyric;

import com.flyfox.ffplayer.lyric.baidu.BaiduLyricLoader;
import com.flyfox.ffplayer.lyric.model.Lyric;
import com.flyfox.ffplayer.lyric.qq.QQLyricLoader;
import com.flyfox.ffplayer.model.FFPlayer;
import com.flyfox.util.NumberUtils;
import com.flyfox.util.StrUtils;

public class LyricManager {

	private static final BaiduLyricLoader baidu = new BaiduLyricLoader();
	private static final QQLyricLoader qq = new QQLyricLoader();

	/**
	 * 先通过baidu查询，再通过qq查询
	 * 
	 * @param ffPlayer
	 * @return
	 */
	public static Lyric load(FFPlayer ffPlayer) {
		if (StrUtils.isEmpty(StrUtils.trim(ffPlayer.getTitle())) //
				|| StrUtils.isEmpty(StrUtils.trim(ffPlayer.getArtist()))) {
			return null;
		}

		Lyric lyric = baidu.load(ffPlayer);

		if (NumberUtils.parseInt(lyric.getInfo()) >= 0 // 百度查询成功,直接返回
				&& lyric.getLyricItems().size() > 0) {
			return lyric;
		}

		lyric = qq.load(ffPlayer);

		if (NumberUtils.parseInt(lyric.getInfo()) >= 0 // qq查询成功,直接返回
				|| lyric.getLyricItems().size() > 0) {
			return lyric;
		}

		return null;
	}

}
