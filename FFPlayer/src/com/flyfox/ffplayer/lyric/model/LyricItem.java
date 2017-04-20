package com.flyfox.ffplayer.lyric.model;

import java.io.Serializable;

/**
 * 歌词项目信息
 * 
 * @author flyfox
 * @date 2014年11月14日
 */
public class LyricItem implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 时间戳
	 */
	private long time;

	/**
	 * 当前行歌词内容
	 */
	private String content;

	public LyricItem() {
	}

	public LyricItem(long time, String content) {
		this.time = time;
		this.content = content;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
