/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
/**
 *
 * @author loach
 */
public class CirclesAppliction extends Application{

    @Override
    public void start(Stage primaryStage)  {
       //增加布景
       Group root=new Group();
       Scene scene=new Scene(root,400,300,Color.BLACK);
       primaryStage.setScene(scene);
       //添加图形
       Group circles=new Group();
       for(int i=0;i<30;i++)
       {
          Circle circle=new Circle(150,Color.web("white",0.05));
          circle.setStrokeType(StrokeType.OUTSIDE);
          circle.setStroke(Color.web("white",0.16));
          circle.setStrokeWidth(4);
          circles.getChildren().add(circle);
       }
       
        //创建一个矩形，并以线性渐变填充。
        Rectangle colors=new Rectangle(scene.getWidth(),scene.getHeight(),new LinearGradient(0f,1f,1f,0f,true,CycleMethod.NO_CYCLE,new Stop[]{
        new Stop(0,Color.web("#f8bd55")),
        new Stop(0.14,Color.web("#c0fe56")),
        new Stop(0.28,Color.web("#5dfbc1")),
        new Stop(0.43,Color.web("#64c2f8")),
        new Stop(0.57,Color.web("#be4af7")),
        new Stop(0.71,Color.web("#ed5fc2")),
        new Stop(0.85,Color.web("#ef504c")),
        new Stop(1,Color.web("#f2660f")),
        }));
        //root.getChildren().add(colors);
        //root.getChildren().add(circles);
        Group blendModeGroup=new Group(new Group(new Rectangle(scene.getWidth(),scene.getHeight(),Color.BLACK),circles),colors);
        colors.setBlendMode(BlendMode.OVERLAY);
         
        root.getChildren().add(blendModeGroup);
       
          
        circles.setEffect(new BoxBlur(10,10,3));
        primaryStage.show();
    }
    
    public static void main(String[] args){
       launch();
    }
}
