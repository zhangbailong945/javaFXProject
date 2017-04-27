/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxmlsample;

import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import util.DatabaseManager;

/**
 *
 * @author loach
 */
public class FXMLSample extends Application {
    
     private static FXMLSample instance;
     static DatabaseManager dbManager;
     private  double xOffset=0;
     private  double yOffset =0;   
     static AnchorPane indexPage,loginPage;
     private Stage stage;
     
     public static FXMLSample FXMLSample()
     {
         return instance;
     }
     
    @Override
    public void start(Stage stage) throws Exception {
        //stage.setTitle("FXML Example");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"),ResourceBundle.getBundle("fxmlsample.zh_cn"));
        
        Scene scene = new Scene(root,400,300);
        Button closeButton=(Button)root.lookup("#closeButton");
        Button submitButton=(Button)root.lookup("#submitButton");
        
        
        
        //关闭按钮，单击事件，退出舞台
        closeButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                stage.close();
            }
        
        });
        
        //用于拖动整个组件,当鼠标对于登录窗体按住不放进行拖动
        root.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                xOffset=event.getSceneX();
                yOffset=event.getSceneY();
            }
        
        });
        //当鼠标释放，组件的位置就是当前的位置
        root.setOnMouseDragged(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                stage.setX(event.getScreenX()-xOffset);
                if(event.getScreenY()-xOffset<0)
                {
                   stage.setY(0);
                }
                else
                {
                   stage.setY(event.getScreenY()-yOffset);
                }
            }
        
        });
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
