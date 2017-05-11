/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loachblogpc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 *
 * @author Loach
 */
public class Main extends Application{
    
    private static Main instance;
    static AnchorPane loginPage,mainPage;   
    private Stage stage;
    
    public static Main getInstance(){
        return instance;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        loginPage = FXMLLoader.load(getClass().getResource("Login.fxml"));
        mainPage = FXMLLoader.load(getClass().getResource("Main.fxml"));
        instance = this;
        stage.setScene(new Scene(loginPage));
        stage.show();
        this.stage = stage;
    }
        public void enterMainScene(){
        this.stage.setScene(new Scene(mainPage));
    }
    
}
