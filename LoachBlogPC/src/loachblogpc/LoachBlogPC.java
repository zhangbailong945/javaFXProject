/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loachblogpc;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 *
 * @author loach
 */
public class LoachBlogPC extends Application {
    
     private  double xOffset=0;
     private  double yOffset =0;  
     static AnchorPane indexPage,loginPage;
     private Stage stage;
     
   public void gotoLogin(){  
      try {  
            LoginController login = (LoginController)FXMLLoader.load(getClass().getResource("Login.fxml"));  
            login.setApp(this);  
        } catch (Exception ex) {  
            System.out.println(ex.getMessage());
        }  
    } 
     
    public void gotoMain(){  
        try {  
            MainController main = (MainController)FXMLLoader.load(getClass().getResource("Main.fxml"));
            main.setApp(this);  
        } catch (Exception ex) {  
            System.out.println(ex.getMessage()); 
        }  
    } 
    
    @Override
    public void start(Stage stage) throws Exception {     
        
        Parent root =FXMLLoader.load(getClass().getResource("Login.fxml")); //登录界面
        
        Scene scene = new Scene(root);
        
        AnchorPane bcAp=(AnchorPane)root.lookup("#bcAP");//anchorPane布局
        Button exitBtn=(Button)root.lookup("#exitBtn"); //退出按钮
        Button loginBtn=(Button)root.lookup("#loginBtn"); //登录按钮
        Label msgL=(Label)root.lookup("#msgL"); //错误消息label
        
        
        //scene.setFill(Color.TRANSPARENT);//设置场景
         //关闭按钮，单击事件，退出舞台
        exitBtn.setOnMouseClicked(new EventHandler<MouseEvent>(){
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
        
        stage.setMinHeight(520);
        stage.setMinWidth(650);
        stage.initStyle(StageStyle.TRANSPARENT);
        gotoLogin();
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
