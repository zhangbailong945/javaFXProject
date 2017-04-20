/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxmlsample;
import java.net.URI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
/**
 *
 * @author loach
 */
public class LoginContorller implements Initializable{
    @FXML
    private Label buttonStatusText;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    @FXML
    public void handlePasswordFieldAction(ActionEvent event)
    {
       buttonStatusText.setText("输入密码！");
    }
    
    public void handleSubmitButtonAction(ActionEvent event)
    {
       buttonStatusText.setText("已提交！");
    }
}
