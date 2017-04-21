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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
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
import org.tjumyk.ClassManager.ClassRecordSet;
import org.tjumyk.StudentManager.StudentRecordSet;
import org.tjumyk.TakeCourseManager.TakeCourseRecordSet;

/**
 * FXML Controller class
 *
 * @author E40-G8C
 */
public class ClassPanelController implements Initializable {

    private static ClassPanelController instance;

    public static ClassPanelController getInstance() {
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
    AnchorPane tableContainer, calculatePanel;
    @FXML
    BarChart chart;
    @FXML
    Label cal_max_power_ave, cal_total_num, cal_fail_count, cal_fail_rate, cal_ave_score, cal_power_ave_score;
    @FXML
    Button cal_return;
    XYChart.Series<String, Number> score_series = new XYChart.Series<String, Number>();

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
            String id, name;

            @Override
            public void background() throws Exception {
                int idNum = 0;
                boolean isID = true;
                try {
                    idNum = Integer.parseInt(input);
                } catch (Exception e1) {
                    isID = false;
                }
                ClassRecordSet rs;
                if (isID) {
                    rs = ClassManager.searchClassesByID(idNum);
                } else {
                    rs = ClassManager.searchClassesByName(input);
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
                table.getItems().add(new ClassTableRecord(id, name));
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
            calculatePanel.toBack();
            new Timeline(new KeyFrame(Duration.seconds(0.3), new KeyValue(bottomPanel.opacityProperty(), 1),
                    new KeyValue(buttonPane.opacityProperty(), 0), new KeyValue(calculatePanel.opacityProperty(), 0))).play();
        } else {
            tableContainer.toFront();
            buttonPane.toFront();
            bottomPanel.toBack();
            calculatePanel.toBack();
            new Timeline(new KeyFrame(Duration.seconds(0.3), new KeyValue(bottomPanel.opacityProperty(), 0),
                    new KeyValue(buttonPane.opacityProperty(), 1), new KeyValue(calculatePanel.opacityProperty(), 0))).play();
        }
    }

    private void showCalculatePanel(boolean show) {
        if (show) {
            calculatePanel.toFront();
            bottomPanel.toBack();
            buttonPane.toBack();
            tableContainer.toBack();
            new Timeline(new KeyFrame(Duration.seconds(0.3), new KeyValue(calculatePanel.opacityProperty(), 1),
                    new KeyValue(bottomPanel.opacityProperty(), 0),
                    new KeyValue(buttonPane.opacityProperty(), 0))).play();
        } else {
            tableContainer.toFront();
            buttonPane.toFront();
            bottomPanel.toBack();
            calculatePanel.toBack();
            new Timeline(new KeyFrame(Duration.seconds(0.3), new KeyValue(calculatePanel.opacityProperty(), 0),
                    new KeyValue(bottomPanel.opacityProperty(), 0),
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
    private void CalculateReturnFired(ActionEvent e) {
        showCalculatePanel(false);
        score_series.getData().clear();
    }

    @FXML
    private void CalculateFired(ActionEvent e) {
        ClassTableRecord record = (ClassTableRecord) table.getSelectionModel().getSelectedItem();
        if (record == null) {
            MainPageController.getInstance().showErrorMessage("请先选择需要进行统计的目标！");
            return;
        }
        cal_ave_score.setText("NaN");
        cal_fail_count.setText("NaN");
        cal_fail_rate.setText("NaN");
        cal_total_num.setText("NaN");
        cal_max_power_ave.setText("NaN");
        cal_power_ave_score.setText("NaN");
        score_series.getData().clear();
        showCalculatePanel(true);
        MainPageController.getInstance().showMessage("正在进行统计...");
        final int classid = Integer.parseInt(record.getId());
        new BackgroundRunner() {
            int  fail_course_count = 0;
            double max_power_ave = 0;
            double fail_rate, ave, power_ave;
            String current_sname, max_power_ave_sname;
            int current_ave;
            int current_power_ave;
            int current_total_point;
            int total_course_count = 0;
            int total_point = 0, total_num = 0;

            @Override
            public void background() throws Exception {
                StudentRecordSet rs = StudentManager.getStudentsByClassID(classid);
                while (rs.next()) {
                    total_num++;
                    long sid = rs.getID();
                    current_sname = rs.getName();
                    current_ave = 0;
                    current_power_ave = 0;
                    current_total_point = 0;

                    TakeCourseRecordSet rs1 = TakeCourseManager.getTakeCoursesBySID(sid);
                    while (rs1.next()) {
                        int score = rs1.getScore();
                        int cid = rs1.getCID();
                        CourseManager.CourseRecordSet rs2 = CourseManager.getCourseByID(cid);
                        if (!rs2.next()) {
                            rs2.close();
                            throw new Exception("Cannot find Course： ID=" + cid);
                        }
                        int point = rs2.getPoint();
                        rs2.close();

                        current_total_point += point;
                        total_course_count++;
                        total_point += point;

                        if (score < TakeCourseManager.MAX_SCORE * 0.6) {
                            fail_course_count++;
                        }
                        double temp = TakeCourseManager.calculateScorePoint(score) * point;
                        current_ave += temp;
                        ave += temp;
                        temp = score * point;
                        current_power_ave += temp;
                        power_ave += temp;
                    }
                    if (current_total_point > 0) {
                        current_ave /= current_total_point;
                        current_power_ave /= current_total_point;
                        if (current_power_ave > max_power_ave) {
                            max_power_ave = current_power_ave;
                            max_power_ave_sname = current_sname;
                        }
                    }
                    runForeground();
                    rs1.close();
                }
                rs.close();
            }

            @Override
            public void foreground() throws Exception {
                score_series.getData().add(new XYChart.Data<String, Number>(current_sname, current_power_ave));
            }

            @Override
            public void handleException(Exception e) throws Exception {
                MainPageController.getInstance().showErrorMessage("加载数据出错：" + e.getLocalizedMessage());
                e.printStackTrace();
            }

            @Override
            public void finish() throws Exception {
                if (total_num <= 0 || total_course_count <= 0) {
                    MainPageController.getInstance().showErrorMessage("该班没有选课记录！");
                    return;
                }
                ave /= total_point;
                power_ave /= total_point;
                fail_rate = fail_course_count * 1.0 / total_course_count;

                cal_total_num.setText(total_num+"");
                cal_max_power_ave.setText(max_power_ave_sname);
                cal_fail_count.setText(fail_course_count + "");
                cal_fail_rate.setText((int) (fail_rate * 100) + "%");
                cal_ave_score.setText(((int) (ave * 100)) / 100.0 + "");
                cal_power_ave_score.setText(((int) (power_ave * 10)) / 10.0 + "");
                MainPageController.getInstance().showMessage("统计完成！");
            }
        }.run();
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
            ClassManager.addClass(id, name);
            state = 2;
            table.getItems().add(new ClassTableRecord("" + id, name));
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
            ClassTableRecord record = (ClassTableRecord) table.getSelectionModel().getSelectedItem();

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
            ClassManager.updateClass(id, name);
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
            ClassTableRecord record = (ClassTableRecord) table.getSelectionModel().getSelectedItem();
            String id = record.getId();

            state = 1;
            ClassManager.deleteClass(Integer.parseInt(id));

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
                ClassRecordSet rs = ClassManager.getAllClasses();
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
                table.getItems().add(new ClassTableRecord(id, name));
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
        chart.getData().clear();
        score_series.setName("加权平均成绩");
        chart.getData().addAll(score_series);
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                ClassTableRecord record = (ClassTableRecord) table.getSelectionModel().getSelectedItem();
                if (record == null) {
                    return;
                }
                showBottomPanel(false);
                score_series.getData().clear();
                new_id.setText(record.getId());
                new_name.setText(record.getName());
            }
        });
    }
}
