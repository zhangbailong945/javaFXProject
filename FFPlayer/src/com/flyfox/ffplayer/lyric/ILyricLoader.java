package com.flyfox.ffplayer.lyric;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flyfox.ffplayer.lyric.model.Lyric;
import com.flyfox.ffplayer.lyric.model.LyricItem;
import com.flyfox.ffplayer.model.FFPlayer;
import com.flyfox.ffplayer.util.PathUtils;
import com.flyfox.util.FileUtils;
import com.flyfox.util.NumberUtils;
import com.flyfox.util.StrUtils;

/**
 * 歌词接口
 * 
 * @author flyfox
 * @date 2014年11月14日
 */
public interface ILyricLoader {

	/**
	 * 加载歌词
	 * 
	 * @param ffPlayer
	 * @return
	 */
	default public Lyric load(FFPlayer ffPlayer) {
		if (!containLocolLyric(ffPlayer)) {
			// 不存在就下载
			int ret = downLyric(ffPlayer);
			if (ret < 0) { // 失败，返回错误码
				Lyric lyric = new Lyric();
				lyric.setInfo(ret);
				return lyric;
			}
		}

		// 存在或者下载完成，那么就加载歌词
		return loadLyric(ffPlayer);
	}

	/**
	 * 通过音频文件信息，下载歌词
	 * 
	 * @param ffPlayer
	 * @return 返回错误码,错误码小于0失败
	 */
	public int downLyric(FFPlayer ffPlayer);

	/**
	 * 通过音频文件信息，获取歌词
	 * 
	 * @param ffPlayer
	 * @return
	 */
	default Lyric loadLyric(FFPlayer ffPlayer) {
		if (!containLocolLyric(ffPlayer)) {
			return null;
		}

		Lyric lyric = new Lyric();
		String lyricPath = getLyricPath(ffPlayer);
		try {
			byte[] data = FileUtils.read(lyricPath);
			String str = new String(data, "UTF-8");
			String[] lyricArray = str.split("\n");
			int lines = 0;

			List<LyricItem> listItem = new ArrayList<LyricItem>();
			for (int i = 0; i < lyricArray.length; i++) {
				String lyricStr = lyricArray[i].replace("\r", "");

				if (lyricStr.startsWith("[ti")) { // 歌曲
					lyricStr = lyricStr.replace("[ti:", "");
					lyricStr = lyricStr.replace("]", "");
					lyric.setName(lyricStr);
				} else if (lyricStr.startsWith("[ar")) { // 歌手
					lyricStr = lyricStr.replace("[ar:", "");
					lyricStr = lyricStr.replace("]", "");
					lyric.setSinger(lyricStr);
				} else if (lyricStr.startsWith("[al")) { // 所属专辑名
				} else if (lyricStr.startsWith("[by")) { // lrc歌词制作者
				} else if (lyricStr.startsWith("[offset")) { // 补偿时间
				} else {
					LyricItem item = new LyricItem();
					lyricStr = lyricStr.replace("[", "");
					String[] infoArray = lyricStr.split("]");
					// 不符合要求，不记录
					if (infoArray.length < 2) {
						continue;
					} else {
						// 处理多时间戳情况，正常infoArray.length=2 ，只循环一次
						for (int j = 0; j < infoArray.length - 1; j++) {
							// 时间处理
							String[] timeStr = infoArray[j].split(":");
							// 时间错误，丢弃
							if (timeStr.length < 2) {
								continue;
							}

							long time = (long) ((NumberUtils.parseDbl(timeStr[0]) * 60 + NumberUtils
									.parseDbl(timeStr[1])) * 1000);
							String content = infoArray[infoArray.length - 1];
							item.setTime(time);
							item.setContent(content);
							listItem.add(item);
							lines++;
						}
					}

				}
			}

			// 进行排序
			Collections.sort(listItem, (o1, o2) -> {
				if (o1.getTime() > o2.getTime()) {
					return 1;
				}
				if (o1.getTime() < o2.getTime()) {
					return -1;
				}
				return 0;
			});

			// 歌词行数
			lyric.setLyricItems(listItem);
			lyric.setLines(lines);
			// 成功
			lyric.setInfo(0);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return lyric;
	}

	/**
	 * 本地是否存在歌词
	 * 
	 * @param ffPlayer
	 * @return
	 */
	default boolean containLocolLyric(FFPlayer ffPlayer) {
		if (StrUtils.isEmpty(ffPlayer.getArtist()) //
				|| StrUtils.isEmpty(ffPlayer.getTitle())) {
			return false;
		}
		String lyricPath = getLyricPath(ffPlayer);
		return new File(lyricPath).exists();
	}

	/**
	 * 获取歌词路径
	 * 
	 * @param ffPlayer
	 * @return
	 */
	default String getLyricPath(FFPlayer ffPlayer) {
		String lyricPath = PathUtils.LYRIC_PATH + File.separator //
				+ ffPlayer.getTitle() + " - " + ffPlayer.getArtist() + ".lrc";
		return lyricPath;
	}
}
