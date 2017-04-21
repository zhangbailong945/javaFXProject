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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author E40-G8C
 */
public class IndexPageController implements Initializable {

    @FXML
    TextField server;
    @FXML
    TextField user;
    @FXML
    PasswordField password;
    @FXML
    Label message;

    @FXML
    private void loginFired(ActionEvent event) {
        final String s = server.getText();
        final String u = user.getText();
        final String p = password.getText();
        showMessage("正在登陆中...");

        new BackgroundRunner() {
            String prompt;
            boolean isError;
            boolean ok = false;

            @Override
            public void background() throws Exception {
                try {
                    Main.dbManager.setDriver(DatabaseManager.DEFAULT_DRIVER);
                    Main.dbManager.connect(s, u, p);
                    ok = true;
                } catch (Exception e) {
                    showError("连接选课数据库失败！正在诊断问题...");
                    e.printStackTrace();
                    if (Main.dbManager.tryConnect(s, u, p)) {//能连接数据库但没有选课数据库
                        showPrompt("正在安装选课数据库...");
                        try {
                            Main.dbManager.install(s, u, p);
                            showPrompt("正在重新连接选课数据库...");
                            try {
                                Main.dbManager.connect(s, u, p);
                                ok = true;
                            } catch (Exception e2) {
                                showError("安装数据库后仍然无法连接选课数据库！");
                                e2.printStackTrace();
                            }
                        } catch (Exception e1) {
                            showError("尝试安装数据库失败！");
                            e1.printStackTrace();
                        }
                    } else {
                        showError("连接数据库失败！请检查参数是否正确！");
                    }
                }
            }

            public void showPrompt(String prompt) {
                this.prompt = prompt;
                this.isError = false;
                runForeground();
            }

            public void showError(String prompt) {
                this.prompt = prompt;
                this.isError = true;
                runForeground();
            }

            @Override
            public void foreground() throws Exception {
                if (isError) {
                    showErrorMessage(prompt);
                } else {
                    showMessage(prompt);
                }
            }

            @Override
            public void handleException(Exception e) throws Exception {
                showErrorMessage(e.getLocalizedMessage());
                e.printStackTrace();
            }

            @Override
            public void finish() throws Exception {
                if (ok) {
                    showMessage("登陆成功！");
                    Main.getInstance().enterMainScene();
                }
            }
        }.run();
    }

    private void showMessage(String msg) {
        System.out.println("[Message]: " + msg);
        message.setTextFill(Color.BLACK);
        message.setText(msg);
        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(0.3), new KeyValue(message.opacityProperty(), 1), new KeyValue(message.translateZProperty(), 10)),
                new KeyFrame(Duration.seconds(3.0), new KeyValue(message.opacityProperty(), 0), new KeyValue(message.translateZProperty(), 0)));
        tl.play();
    }

    private void showErrorMessage(String msg) {
        System.err.println("[Error]: " + msg);
        message.setTextFill(Color.RED);
        message.setText(msg);
        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(0.3), new KeyValue(message.opacityProperty(), 1), new KeyValue(message.translateZProperty(), 10)),
                new KeyFrame(Duration.seconds(3.0), new KeyValue(message.opacityProperty(), 0), new KeyValue(message.translateZProperty(), 0)));
        tl.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }
}
