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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author loach
 */
public class FXMLSample extends Application {
    
     private  double xOffset=0;
     private  double yOffset =0;   
     
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("FXML Example");
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"),ResourceBundle.getBundle("fxmlsample.FXMLSample"));        
        Scene scene = new Scene(root,400,300);
        Button closeButton=(Button)root.lookup("#closeButton");
        closeButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                stage.close();
            }
        
        });
        root.setOnMousePressed(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                xOffset=event.getSceneX();
                yOffset=event.getSceneY();
            }
        
        });
        
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
