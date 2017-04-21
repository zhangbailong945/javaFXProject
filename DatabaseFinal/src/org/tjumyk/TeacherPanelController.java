/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tjumyk;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumnBuilder;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.tjumyk.TeacherManager.TeacherRecordSet;

/**
 * FXML Controller class
 *
 * @author E40-G8C
 */
public class TeacherPanelController implements Initializable {

    private static TeacherPanelController instance;
	public static TeacherPanelController getInstance() {
        return instance;
    }
	@FXML
    TextField lookup, jump_Page, new_id, new_name;
    @FXML
    Label current_page, total_page;
    @FXML
    HBox new_panel;
    @FXML
    VBox bottomPanel;
    @FXML
    TableView table;
    @FXML
    Button sureAddBtn, sureUpdateBtn, sureDeleteBtn;
    @FXML
    Label deletePrompt;
    @FXML
    HBox buttonPane;
    @FXML
    AnchorPane tableContainer;

    @FXML
    private void LookUpFired(ActionEvent e) {
        if (lookup.getText().length() <= 0) {
            loadTable();
            return;
        }
        table.getItems().clear();
        final String input = lookup.getText();

        MainPageController.getInstance().showMessage("正在查询中...");
        new BackgroundRunner() {
            int total = 0;
            String  id, name;

            @Override
            public void background() throws Exception {
                int idNum = 0;
                boolean isID = true;
                try {
                    idNum = Integer.parseInt(input);
                } catch (Exception e1) {
                    isID = false;
                }
                TeacherRecordSet rs;
                if (isID) {
                    rs = TeacherManager.searchTeachersByID(idNum);
                } else {
                    rs = TeacherManager.searchTeachersByName(input);
                }
                while (rs.next()) {
                    total++;
                    id = rs.getID() + "";
                    name = rs.getName();
                    runForeground();
                }
                rs.close();
            }

            @Override
            public void foreground() {
                table.getItems().add(new TeacherTableRecord(id, name));
            }

            @Override
            public void handleException(Exception e) {
                MainPageController.getInstance().showErrorMessage("查询数据出错：" + e.getLocalizedMessage());
                e.printStackTrace();
            }

            @Override
            public void finish() {
                MainPageController.getInstance().showMessage("已找到" + total + "个匹配的记录");
            }
        }.run();
    }

    @FXML
    private void JumpPageFired(ActionEvent e) {
        MainPageController.getInstance().showErrorMessage("本功能正在建设中...");
    }

    private void showBottomPanel(boolean show) {
        if (show) {
            bottomPanel.toFront();
            buttonPane.toBack();
            tableContainer.toBack();
            new Timeline(new KeyFrame(Duration.seconds(0.3), new KeyValue(bottomPanel.opacityProperty(), 1),
                    new KeyValue(buttonPane.opacityProperty(), 0))).play();
        } else {
            tableContainer.toFront();
            buttonPane.toFront();
            bottomPanel.toBack();
            new Timeline(new KeyFrame(Duration.seconds(0.3), new KeyValue(bottomPanel.opacityProperty(), 0),
                    new KeyValue(buttonPane.opacityProperty(), 1))).play();
        }
    }

    @FXML
    private void AddFired(ActionEvent e) {
        //MainPageController.getInstance().showMessage("正在准备数据...");
        sureAddBtn.setVisible(true);
        sureAddBtn.toFront();
        sureUpdateBtn.setVisible(false);
        sureDeleteBtn.setVisible(false);
        deletePrompt.setVisible(false);
        new_panel.setVisible(true);
        showBottomPanel(true);
        //MainPageController.getInstance().showMessage("准备就绪！");
    }

    @FXML
    private void UpdateFired(ActionEvent e) {
        if (table.getSelectionModel().getSelectedItem() == null) {
            MainPageController.getInstance().showErrorMessage("请先选择需要修改的记录！");
            return;
        }
        //MainPageController.getInstance().showMessage("正在准备数据...");
        sureAddBtn.setVisible(false);
        sureUpdateBtn.setVisible(true);
        sureUpdateBtn.toFront();
        sureDeleteBtn.setVisible(false);
        deletePrompt.setVisible(false);
        new_panel.setVisible(true);
        showBottomPanel(true);
        //MainPageController.getInstance().showMessage("准备就绪！");
    }

    @FXML
    private void DeleteFired(ActionEvent e) {
        if (table.getSelectionModel().getSelectedItem() == null) {
            MainPageController.getInstance().showErrorMessage("请先选择需要删除的记录！");
            return;
        }
        sureAddBtn.setVisible(false);
        sureUpdateBtn.setVisible(false);
        sureDeleteBtn.setVisible(true);
        sureDeleteBtn.toFront();
        deletePrompt.setVisible(true);
        new_panel.setVisible(false);
        showBottomPanel(true);
    }

    @FXML
    private void CalculateFired(ActionEvent e) {
    }

    @FXML
    private void SureAddFired(ActionEvent e) {
        int state = 0;//0-prepare;1-execSQL;2-addToTable
        try {
            String idText = new_id.getText();
            if (idText.length() == 0) {
                MainPageController.getInstance().showErrorMessage("请输入ID！");
                return;
            }
			int id = Integer.parseInt(idText);
            String name = new_name.getText();
            if (name.length() == 0) {
                MainPageController.getInstance().showErrorMessage("请输入名称！");
                return;
            }
            state = 1;
            TeacherManager.addTeacher(id, name);
            state = 2;
            table.getItems().add(new TeacherTableRecord("" + id, name));
            showBottomPanel(false);
            MainPageController.getInstance().showMessage("记录已成功添加！");
        } catch (Exception e1) {
            if (state == 0) {
                MainPageController.getInstance().showErrorMessage("数据格式不正确！" + e1.getLocalizedMessage());
            } else if (state == 1) {
                MainPageController.getInstance().showErrorMessage("数据库错误：" + e1.getLocalizedMessage());
            } else {
                MainPageController.getInstance().showErrorMessage("表格控件错误：" + e1.getLocalizedMessage());
            }
            e1.printStackTrace();
        }
    }

    @FXML
    private void SureUpdateFired(ActionEvent e) {
        int state = 0;//0-prepare;1-execSQL;2-addToTable
        try {
            TeacherTableRecord record = (TeacherTableRecord) table.getSelectionModel().getSelectedItem();

            String idText = new_id.getText();
            if (idText.length() == 0) {
                MainPageController.getInstance().showErrorMessage("请输入ID！");
                return;
            }
			int id = Integer.parseInt(idText);
            String name = new_name.getText();
            if (name.length() == 0) {
                MainPageController.getInstance().showErrorMessage("请输入名称！");
                return;
            }
            state = 1;
            TeacherManager.updateTeacher(id, name);
            state = 2;
            record.setId(id + "");
            record.setName(name);

            showBottomPanel(false);
            MainPageController.getInstance().showMessage("记录已成功更新！");
        } catch (Exception e1) {
            if (state == 0) {
                MainPageController.getInstance().showErrorMessage("数据格式不正确！" + e1.getLocalizedMessage());
            } else if (state == 1) {
                MainPageController.getInstance().showErrorMessage("数据库错误：" + e1.getLocalizedMessage());
            } else {
                MainPageController.getInstance().showErrorMessage("表格控件错误：" + e1.getLocalizedMessage());
            }
            e1.printStackTrace();
        }
    }

    @FXML
    private void SureDeleteFired(ActionEvent e) {
        int state = 0;//0:read;1-execSQL;2-deleteInTable
        try {
            TeacherTableRecord record = (TeacherTableRecord) table.getSelectionModel().getSelectedItem();
            String id = record.getId();

            state = 1;
            TeacherManager.deleteTeacher(Integer.parseInt(id));

            state = 2;
            table.getItems().remove(record);
            showBottomPanel(false);
            MainPageController.getInstance().showMessage("记录已成功删除！");
        } catch (Exception e1) {
            if (state == 0) {
                MainPageController.getInstance().showErrorMessage("记录读取出错！" + e1.getLocalizedMessage());
            } else if (state == 1) {
                MainPageController.getInstance().showErrorMessage("数据库错误：" + e1.getLocalizedMessage());
            } else {
                MainPageController.getInstance().showErrorMessage("表格控件错误：" + e1.getLocalizedMessage());
            }
            e1.printStackTrace();
        }
    }

    @FXML
    private void CancelFired(ActionEvent e) {
        showBottomPanel(false);
    }

    @FXML
    private void PrevPageFired(ActionEvent e) {
        MainPageController.getInstance().showErrorMessage("本功能正在建设中...");
    }

    @FXML
    private void NextPageFired(ActionEvent e) {
        MainPageController.getInstance().showErrorMessage("本功能正在建设中...");
    }

    public void loadTable() {
        lookup.setText("");
        showBottomPanel(false);
        table.getItems().clear();
        new BackgroundRunner() {
            int total = 0;
            String id, name;

            @Override
            public void background() throws Exception {
                TeacherRecordSet rs = TeacherManager.getAllTeachers();
                while (rs.next()) {
                    total++;
                    id = rs.getID() + "";
                    name = rs.getName();
                    runForeground();
                }
                rs.close();
            }

            @Override
            public void foreground() {
                table.getItems().add(new TeacherTableRecord(id, name));
            }

            @Override
            public void handleException(Exception e) {
                MainPageController.getInstance().showErrorMessage("加载数据出错：" + e.getLocalizedMessage());
                e.printStackTrace();
            }

            @Override
            public void finish() {
                MainPageController.getInstance().showMessage("已加载" + total + "条记录");
            }
        }.run();

    }

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        table.getColumns().addAll(
                TableColumnBuilder.create().text("ID").cellValueFactory(new PropertyValueFactory("id")).prefWidth(110).build(),
                TableColumnBuilder.create().text("班级名称").cellValueFactory(new PropertyValueFactory("name")).prefWidth(180).build());

        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TeacherTableRecord record = (TeacherTableRecord) table.getSelectionModel().getSelectedItem();
                if (record == null) {
                    return;
                }
                showBottomPanel(false);
                new_id.setText(record.getId());
                new_name.setText(record.getName());
            }
        });
    }
}   

