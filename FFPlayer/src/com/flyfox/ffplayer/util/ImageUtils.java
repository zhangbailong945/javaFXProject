package com.flyfox.ffplayer.util;

import java.io.InputStream;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageUtils {

	public static String IMAGE_PATH = "resources/image";

	public static ImageView getImageView(String pic) {
		return getImageView(pic, 32, 32);
	}

	public static ImageView getImageView(String pic, double width, double height) {
		Image image = getImage(pic);
		if (image == null) {
			return null;
		}

		ImageView imageView = new ImageView(image);
		imageView.setFitHeight(width);
		imageView.setFitWidth(height);
		return imageView;
	}

	public static Image getImage(String pic) {
		InputStream is = ImageUtils.class.getResourceAsStream("/" + IMAGE_PATH + "/" + pic);
		return new Image(is);
	}
}
