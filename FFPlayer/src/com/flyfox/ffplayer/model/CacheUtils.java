package com.flyfox.ffplayer.model;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.flyfox.ffplayer.util.FFPlayerUtils;
import com.flyfox.ffplayer.util.FSTSerializer;
import com.flyfox.ffplayer.util.PathUtils;
import com.flyfox.util.FileUtils;
import com.flyfox.util.serializable.Serializer;

/**
 * 数据缓存控制器
 * 
 * @author flyfox
 * @date 2014年11月13日
 */
public class CacheUtils {

	/**
	 * 用户信息对象
	 */
	private static UserData userData = new UserData();

	/**
	 * fst序列化
	 */
	private static final Serializer SERIALIZER = new FSTSerializer();
	/**
	 * 列表数据
	 */
	private static final String path = PathUtils.RESOURCES_PATH + File.separator + "music.data";

	static {
		// 初始化数据
		try {
			byte[] data = FileUtils.read(path);
			if (data != null && data.length > 0) {
				userData = SERIALIZER.deserialize(data);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将列表序列化
	 */
	public static void update() {
		FFPlayerUtils.CLOSE.set(false);
		try {
			byte[] data = SERIALIZER.serialize(userData);
			FileUtils.write(path, data);
		} catch (IOException e) {
			e.printStackTrace();
		}
		FFPlayerUtils.CLOSE.set(true);
	}

	public static List<String> getTableData() {
		return userData.getTableData();
	}

	public static int getIndex() {
		return userData.getIndex();
	}

	public static PlayMode getPlayMode() {
		return userData.getPlayMode();
	}

	public static double getVolume() {
		return userData.getVolume();
	}
	
	public static void setTableData(List<String> tableData) {
		userData.setTableData(tableData);
		CacheUtils.update();
	}
	
	public static void setIndex(int index) {
		userData.setIndex(index);
		CacheUtils.update();
	}

	public static void setVolume(double volume) {
		userData.setVolume(volume);
		CacheUtils.update();
	}
	
	public static void setPlayMode(PlayMode playMode) {
		userData.setPlayMode(playMode);
		CacheUtils.update();
	}

	public static boolean contains(String path) {
		return userData.getTableData().contains(path);
	}

	public static void add(String path) {
		userData.getTableData().add(path);
		CacheUtils.update();
	}

	public static void remove(int index) {
		userData.getTableData().remove(index);
		CacheUtils.update();
	}

	public static void remove(String path) {
		userData.getTableData().remove(path);
		CacheUtils.update();
	}

	public static void clear() {
		setIndex(0); // 设置为0
		userData.getTableData().clear();
		CacheUtils.update();
	}

}
