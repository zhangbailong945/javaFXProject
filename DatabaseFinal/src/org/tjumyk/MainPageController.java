/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tjumyk;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author E40-G8C
 */
public class MainPageController implements Initializable {

    private static MainPageController instance;

    public static MainPageController getInstance() {
        return instance;
    }
    
    
    @FXML
    AnchorPane titlepane, menupane, central_panel;
    @FXML
    Label message;

    public void showMessage(String msg) {
        System.out.println("[Message]: " + msg);
        message.setTextFill(Color.WHITE);
        message.setText(msg);
        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(0.3), new KeyValue(message.opacityProperty(), 1), new KeyValue(message.translateZProperty(), 10)),
                new KeyFrame(Duration.seconds(3.0), new KeyValue(message.opacityProperty(), 0), new KeyValue(message.translateZProperty(), 0)));
        tl.play();
    }

    public void showErrorMessage(String msg) {
        System.err.println("[Error]: " + msg);
        message.setTextFill(Color.YELLOW);
        message.setText(msg);
        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(0.3), new KeyValue(message.opacityProperty(), 1), new KeyValue(message.translateZProperty(), 10)),
                new KeyFrame(Duration.seconds(3.0), new KeyValue(message.opacityProperty(), 0), new KeyValue(message.translateZProperty(), 0)));
        tl.play();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
    }
    private Button defaultBtn = null;
    @FXML
    private void studentBtnFired(ActionEvent e) {
        setPanel(Main.studentPanel, "打开学生管理面板");
        Button btn = (Button)e.getSource();
        if(defaultBtn != null)
            defaultBtn.setDefaultButton(false);
        btn.setDefaultButton(true);
        defaultBtn = btn;
        StudentPanelController.getInstance().loadTable();
    }

    @FXML
    private void classBtnFired(ActionEvent e) {
        setPanel(Main.classPanel, "打开班级管理面板");
        Button btn = (Button)e.getSource();
        if(defaultBtn != null)
            defaultBtn.setDefaultButton(false);
        btn.setDefaultButton(true);
        defaultBtn = btn;
        ClassPanelController.getInstance().loadTable();
    }

    @FXML
    private void courseBtnFired(ActionEvent e) {
        setPanel(Main.coursePanel, "打开课程管理面板");
        Button btn = (Button)e.getSource();
        if(defaultBtn != null)
            defaultBtn.setDefaultButton(false);
        btn.setDefaultButton(true);
        defaultBtn = btn;
        CoursePanelController.getInstance().loadTable();
    }

    @FXML
    private void teacherBtnFired(ActionEvent e) {
        setPanel(Main.teacherPanel, "打开教师管理面板");
        Button btn = (Button)e.getSource();
        if(defaultBtn != null)
            defaultBtn.setDefaultButton(false);
        btn.setDefaultButton(true);
        defaultBtn = btn;
        TeacherPanelController.getInstance().loadTable();
    }

    @FXML
    private void takeCourseBtnFired(ActionEvent e) {
        setPanel(Main.takeCoursePanel, "打开选课管理面板");
        Button btn = (Button)e.getSource();
        if(defaultBtn != null)
            defaultBtn.setDefaultButton(false);
        btn.setDefaultButton(true);
        defaultBtn = btn;
        TakeCoursePanelController.getInstance().loadTable();
    }

    public void showAnimation() {
        new Timeline(new KeyFrame(Duration.ZERO, new KeyValue(menupane.translateXProperty(), -200), new KeyValue(titlepane.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(0.3), new KeyValue(menupane.translateXProperty(), -200), new KeyValue(titlepane.opacityProperty(), 1)),
                new KeyFrame(Duration.seconds(0.6), new KeyValue(menupane.translateXProperty(), 0), new KeyValue(titlepane.opacityProperty(), 1))).play();
    }
    

    public void setPanel(final AnchorPane panel, String name) {
        if (panel == null) {
            return;
        }
        if (central_panel.getChildren().size() > 0 && central_panel.getChildren().get(0) == panel) {
            return;
        }
        showMessage("正在" + name + "...");
        EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    central_panel.getChildren().clear();
                    central_panel.getChildren().add(panel);
                    menupane.toFront();
                    titlepane.toFront();
                } catch (Exception e) {
                    showErrorMessage(e.getLocalizedMessage());
                }
            }
        };
//        EventHandler<ActionEvent> eh2 = new EventHandler<ActionEvent>() {
//            @Override
//            public void handle(ActionEvent event) {
//                try{
//                    showMessage("加载完成！");
//                }catch(Exception e){
//                    showErrorMessage(e.getLocalizedMessage());
//                }
//            }
//        };
//        StudentPanelController.getInstance().loadTable();
        if (central_panel.getChildren().size() > 0) {
            if (central_panel.getChildren().get(0) == panel) {
                return;
            }
            new Timeline(
                    new KeyFrame(Duration.seconds(0.15), new KeyValue(central_panel.translateXProperty(), -600), new KeyValue(central_panel.opacityProperty(), 0)),
                    new KeyFrame(Duration.seconds(0.16), eh),
                    new KeyFrame(Duration.seconds(0.3), new KeyValue(central_panel.translateXProperty(), 0), new KeyValue(central_panel.opacityProperty(), 1)) //,new KeyFrame(Duration.seconds(0.31), eh2)
                    ).play();
        } else {
            new Timeline(
                    new KeyFrame(Duration.seconds(0.45), new KeyValue(central_panel.opacityProperty(), 0)),
                    new KeyFrame(Duration.seconds(0.46), eh),
                    new KeyFrame(Duration.seconds(0.6), new KeyValue(central_panel.opacityProperty(), 1)) //,new KeyFrame(Duration.seconds(0.31), eh2)
                    ).play();
        }
    }

    public void initPanel() {
        final AnchorPane panel = Main.studentPanel;
        showMessage("正在初始化面板...");
        EventHandler<ActionEvent> eh = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    central_panel.getChildren().clear();
                    central_panel.getChildren().add(panel);
                    menupane.toFront();
                    titlepane.toFront();
                } catch (Exception e) {
                    showErrorMessage(e.getLocalizedMessage());
                }
            }
        };
        EventHandler<ActionEvent> eh2 = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    StudentPanelController.getInstance().loadTable();
                } catch (Exception e) {
                    showErrorMessage(e.getLocalizedMessage());
                }
            }
        };

        final double delay = 0.60;
        new Timeline(
                new KeyFrame(Duration.seconds(delay), new KeyValue(central_panel.translateXProperty(), -600), new KeyValue(central_panel.opacityProperty(), 0)),
                new KeyFrame(Duration.seconds(delay+0.01), eh),
                new KeyFrame(Duration.seconds(delay+0.15), new KeyValue(central_panel.translateXProperty(), 0), new KeyValue(central_panel.opacityProperty(), 1)),
                new KeyFrame(Duration.seconds(delay+0.16), eh2)).play();
    }
}
