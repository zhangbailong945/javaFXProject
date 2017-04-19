/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author loach
 */
public class TestApplication extends Application {
    
    @Override
    public void start(Stage stage)
    {
       Button btn=new Button();
       btn.setText("Say Hello World!");
       btn.setOnAction(new EventHandler<ActionEvent>(){
           @Override
           public void handle(ActionEvent Event)
           {
              System.out.println("Hello World!");
           }
       });
       
       StackPane root=new StackPane();
       root.getChildren().add(btn);
       Scene scene=new Scene(root,100,100);
       stage.setTitle("第一个JAVAFX程序");
       stage.setScene(scene);
       stage.show();
    }
    
    public static void main(String[] args)
    {
      launch(args);
    }
}
