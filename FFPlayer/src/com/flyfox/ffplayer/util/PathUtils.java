package com.flyfox.ffplayer.util;

import java.io.File;

public class PathUtils {

	// public static String ROOT_PATH =
	// PathUtils.class.getClassLoader().getResource("resources").getFile();
	public final static String USER_PATH = System.getProperty("user.dir");
	public static String RESOURCES_PATH = USER_PATH + File.separator + "resources";
	public final static String LYRIC_PATH = USER_PATH + File.separator + "resources" + File.separator + "lyric";

}
