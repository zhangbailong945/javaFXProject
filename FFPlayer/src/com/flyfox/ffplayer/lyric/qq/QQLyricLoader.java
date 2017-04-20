package com.flyfox.ffplayer.lyric.qq;

import java.io.IOException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.flyfox.ffplayer.lyric.ILyricLoader;
import com.flyfox.ffplayer.model.FFPlayer;
import com.flyfox.ffplayer.util.HttpUtils;
import com.flyfox.util.FileUtils;
import com.flyfox.util.NumberUtils;
import com.flyfox.util.StrUtils;

/**
 * 歌词QQ加载
 * 
 * @author flyfox
 * @date 2014年11月14日
 */
public class QQLyricLoader implements ILyricLoader {

	/**
	 * 歌曲信息请求地址
	 */
	protected static final String SONGINFO_BASE_URL = "http://qqmusic.qq.com/fcgi-bin/qm_getLyricId.fcg";

	/**
	 * 歌词文件请求地址
	 */
	protected static final String LYRIC_BASE_URL = "http://music.qq.com/miniportal/static/lyric";

	/**
	 * @ -1 SongInfo地址解析失败 -2 解析id失败 -3 LYRIC地址解析失败 @ -4 网络异常 -90 MP3自身信息无法获取
	 * -99 异常
	 */
	@Override
	public int downLyric(FFPlayer ffPlayer) {
		Map<String, String> map = new HashMap<String, String>();

		if (StrUtils.isEmpty(StrUtils.trim(ffPlayer.getTitle())) //
				|| StrUtils.isEmpty(StrUtils.trim(ffPlayer.getArtist()))) {
			return -90;
		}

		try {
			String title = URLEncoder.encode(ffPlayer.getTitle(), "GBK");
			String artist = URLEncoder.encode(ffPlayer.getArtist(), "GBK");
			// String title = ffPlayer.getTitle();
			// String artist = ffPlayer.getArtist();
			map.put("name", title);
			map.put("singer", artist);
			map.put("from", "qqplayer");

			String songInfo = HttpUtils.get(SONGINFO_BASE_URL, map);

			if (songInfo == null) {
				return -1;
			}

			// 通过songInfo 获取ID
			int id = parseInfo(songInfo);

			// 解析失败
			if (id == 0) {
				return -2;
			}

			int postfix = id % 100;
			StringBuffer sb = new StringBuffer();
			sb.append(LYRIC_BASE_URL);
			sb.append("/");
			sb.append(postfix);
			sb.append("/");
			sb.append(id);
			sb.append(".xml");

			String lyric = HttpUtils.get(sb.toString(), null, "GBK");
			if (lyric == null) {
				return -3;
			}

			// 解析歌词
			lyric = parseLyric(lyric);

			String lyricPath = getLyricPath(ffPlayer);
			// 写入文件
			FileUtils.write(lyricPath, lyric.getBytes("UTF-8"));

		} catch (UnknownHostException e) {
			System.err.println("网络错误，请查看：" + e.getMessage());
			return -4;
		} catch (IOException e) {
			e.printStackTrace();
			return -99;
		}

		return 0;
	}

	/**
	 * 解析歌词
	 * 
	 * @param lyric
	 * @return
	 */
	private String parseLyric(String lyric) {
		lyric = lyric.replace("<?xml version=\"1.0\" encoding=\"GB2312\" ?><lyric><![CDATA[", "");
		lyric = lyric.replace("]]></lyric>", "");
		return lyric;
	}

	/**
	 * 解析信息
	 * 
	 * @param songInfo
	 * @return
	 */
	private int parseInfo(String songInfo) {
		String regEx = "<songinfo id=\"([0-9]*)\"  scroll=";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(songInfo);
		boolean rs = mat.find();

		// 读取信息，取第一组
		if (rs && mat.groupCount() >= 1) {
			return NumberUtils.parseInt(mat.group(1));
		}

		return 0;
	}

}
