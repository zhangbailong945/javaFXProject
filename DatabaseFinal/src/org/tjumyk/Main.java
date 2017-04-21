/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tjumyk;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author E40-G8C
 */
public class Main extends Application {
    private static Main instance;
    static DatabaseManager dbManager;
    static AnchorPane indexPage,mainPage;
    static AnchorPane studentPanel,classPanel,coursePanel,teacherPanel,takeCoursePanel;
    
    private Stage stage;
    
    public static Main getInstance(){
        return instance;
    }
    
    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        instance = this;
        this.stage = stage;
        dbManager = new DatabaseManager();
        indexPage = FXMLLoader.load(getClass().getResource("IndexPage.fxml"));
        mainPage = FXMLLoader.load(getClass().getResource("MainPage.fxml"));
        teacherPanel = FXMLLoader.load(getClass().getResource("TeacherPanel.fxml"));
        studentPanel = FXMLLoader.load(getClass().getResource("StudentPanel.fxml"));
        classPanel = FXMLLoader.load(getClass().getResource("ClassPanel.fxml"));
        coursePanel = FXMLLoader.load(getClass().getResource("CoursePanel.fxml"));
        takeCoursePanel = FXMLLoader.load(getClass().getResource("TakeCoursePanel.fxml"));
        
        stage.setTitle("选课管理系统 V1.0 | Copyright by tjumyk 2012");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        stage.setMinHeight(520);
        stage.setMinWidth(650);
        stage.setScene(new Scene(indexPage));
        stage.show();
    }
    
    public void enterMainScene(){
        this.stage.setScene(new Scene(mainPage));
        MainPageController.getInstance().showAnimation();
        MainPageController.getInstance().initPanel();
    }
    
    @Override
    public void stop(){
        if(dbManager != null)
            dbManager.disconnect();
    }
}
