package com.flyfox.ffplayer;

import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import com.flyfox.ffplayer.control.MainControl;

public class MainFFPlayer extends Application {

	public static void main(String[] args) {
		launch(args);
	}

    /**
     * 初始化字体
     */
    @Override
    public void init() {
        Font.loadFont(MainFFPlayer.class.getResource("/resources/font/ClearSans-Bold.ttf").toExternalForm(), 10.0);
    }
    
	@Override
	public void start(Stage stage) throws Exception {
		MainControl main = new MainControl(stage);
		main.init();
	}

}
