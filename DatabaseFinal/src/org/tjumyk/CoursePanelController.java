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
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
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
import org.tjumyk.CourseManager.CourseRecordSet;
import org.tjumyk.TakeCourseManager.TakeCourseRecordSet;
import org.tjumyk.TeacherManager.TeacherRecordSet;

/**
 * FXML Controller class
 *
 * @author E40-G8C
 */
public class CoursePanelController implements Initializable {

    private static CoursePanelController instance;

    public static CoursePanelController getInstance() {
        return instance;
    }
    @FXML
    TextField lookup, jump_Page, new_id, new_name, new_point, new_min_grade, new_cancel_year;
    @FXML
    Label current_page, total_page;
    @FXML
    HBox new_panel;
    @FXML
    ComboBox new_tid_tname;
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
    Label cal_max, cal_min, cal_total, cal_fail_count, cal_fail_rate, cal_ave;
    @FXML
    Button cal_return;
    XYChart.Series<String, Number> score_series = new XYChart.Series<String, Number>();
    XYChart.Data<String, Number> score_zone_fail = new XYChart.Data<String, Number>("不及格", 0);
    XYChart.Data<String, Number> score_zone_60_69 = new XYChart.Data<String, Number>("60-69", 0);
    XYChart.Data<String, Number> score_zone_70_79 = new XYChart.Data<String, Number>("70-79", 0);
    XYChart.Data<String, Number> score_zone_80_89 = new XYChart.Data<String, Number>("80-89", 0);
    XYChart.Data<String, Number> score_zone_90_99 = new XYChart.Data<String, Number>("90-99", 0);
    XYChart.Data<String, Number> score_zone_full = new XYChart.Data<String, Number>("满分", 0);
    ArrayList<Integer> tidList = new ArrayList<Integer>();
    ArrayList<String> tnameList = new ArrayList<String>();

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
            int tid;
            int point;
            String tname;
            int min_grade, cancel_year;

            @Override
            public void background() throws Exception {
                int idNum = 0;
                boolean isID = true;
                try {
                    idNum = Integer.parseInt(input);
                } catch (Exception e1) {
                    isID = false;
                }
                CourseRecordSet rs;
                if (isID) {
                    rs = CourseManager.searchCoursesByID(idNum);
                } else {
                    rs = CourseManager.searchCoursesByName(input);
                }
                while (rs.next()) {
                    total++;
                    id = rs.getID() + "";
                    name = rs.getName();
                    tid = rs.getTeacherID();
                    point = rs.getPoint();
                    TeacherRecordSet rs2 = TeacherManager.getTeacherByID(tid);
                    if (!rs2.next()) {
                        rs2.close();
                        throw new Exception("Cannot find Teacher: id=" + tid);
                    }
                    tname = rs2.getName();
                    rs2.close();
                    min_grade = rs.getMinGrade();
                    cancel_year = rs.getCancelYear();
                    runForeground();
                }
                rs.close();
            }

            @Override
            public void foreground() {
                table.getItems().add(new CourseTableRecord(id, name, tid, tname, point, min_grade, cancel_year));
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
        MainPageController.getInstance().showMessage("正在准备数据...");
        sureAddBtn.setVisible(true);
        sureAddBtn.toFront();
        sureUpdateBtn.setVisible(false);
        sureDeleteBtn.setVisible(false);
        deletePrompt.setVisible(false);
        new_panel.setVisible(true);
        showBottomPanel(true);
        loadAllTeachers();
        //MainPageController.getInstance().showMessage("准备就绪！");
    }

    @FXML
    private void UpdateFired(ActionEvent e) {
        if (table.getSelectionModel().getSelectedItem() == null) {
            MainPageController.getInstance().showErrorMessage("请先选择需要修改的记录！");
            return;
        }
        MainPageController.getInstance().showMessage("正在准备数据...");
        sureAddBtn.setVisible(false);
        sureUpdateBtn.setVisible(true);
        sureUpdateBtn.toFront();
        sureDeleteBtn.setVisible(false);
        deletePrompt.setVisible(false);
        new_panel.setVisible(true);
        showBottomPanel(true);
        loadAllTeachers();
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
        resetChart();
    }

    @FXML
    private void CalculateFired(ActionEvent e) {
        CourseTableRecord record = (CourseTableRecord) table.getSelectionModel().getSelectedItem();
        if (record == null) {
            MainPageController.getInstance().showErrorMessage("请先选择需要进行统计的目标！");
            return;
        }
        cal_ave.setText("NaN");
        cal_fail_count.setText("NaN");
        cal_fail_rate.setText("NaN");
        cal_total.setText("NaN");
        cal_max.setText("NaN");
        cal_min.setText("NaN");
        resetChart();
        showCalculatePanel(true);
        MainPageController.getInstance().showMessage("正在进行统计...");
        final int cid = Integer.parseInt(record.getId());
        new BackgroundRunner() {
            int max = -1, min = TakeCourseManager.MAX_SCORE + 1;
            int fail_count = 0;
            double fail_rate, ave;
            int total_score = 0, total_num = 0;
            int current_score;

            @Override
            public void background() throws Exception {
                TakeCourseRecordSet rs = TakeCourseManager.getTakeCoursesByCID(cid);
                while (rs.next()) {
                    total_num++;
                    int score = rs.getScore();
                    current_score = score;
                    if(max < score)
                        max = score;
                    if(min > score)
                        min = score;
                    if(score < TakeCourseManager.MAX_SCORE * 0.6)
                        fail_count++;
                    total_score+= score;
                    runForeground();
                }
                rs.close();
            }

            @Override
            public void foreground() throws Exception {
                if(current_score < 60){
                    ObjectProperty<Number> p = score_zone_fail.YValueProperty();
                    p.setValue(p.getValue().intValue()+1);
                }else if(current_score < 70){
                    ObjectProperty<Number> p = score_zone_60_69.YValueProperty();
                    p.setValue(p.getValue().intValue()+1);
                }else if(current_score < 80){
                    ObjectProperty<Number> p = score_zone_70_79.YValueProperty();
                    p.setValue(p.getValue().intValue()+1);
                }else if(current_score < 90){
                    ObjectProperty<Number> p = score_zone_80_89.YValueProperty();
                    p.setValue(p.getValue().intValue()+1);
                }else if(current_score < 100){
                    ObjectProperty<Number> p = score_zone_90_99.YValueProperty();
                    p.setValue(p.getValue().intValue()+1);
                }else{
                    ObjectProperty<Number> p = score_zone_full.YValueProperty();
                    p.setValue(p.getValue().intValue()+1);
                }
            }

            @Override
            public void handleException(Exception e) throws Exception {
                MainPageController.getInstance().showErrorMessage("加载数据出错：" + e.getLocalizedMessage());
                e.printStackTrace();
            }

            @Override
            public void finish() throws Exception {
                if (total_num <= 0 ) {
                    MainPageController.getInstance().showErrorMessage("该课程没有选课记录！");
                    return;
                }
                ave = total_score*1.0 / total_num;
                fail_rate = fail_count * 1.0 / total_num;
                
                cal_total.setText(total_num + "");
                cal_max.setText(max+"");
                cal_min.setText(min+"");
                cal_fail_count.setText(fail_count + "");
                cal_fail_rate.setText((int) (fail_rate * 100) + "%");
                cal_ave.setText(((int) (ave * 100)) / 100.0 + "");
                MainPageController.getInstance().showMessage("统计完成！");
            }
        }.run();
    }

    @FXML
    private void SureAddFired(ActionEvent e) {
        int state = 0;//0-prepare;1-execSQL;2-addToTable
        try {
            String idText = new_id.getText();
            if (idText.length() != 7) {
                MainPageController.getInstance().showErrorMessage("课程ID必须是7位！");
                return;
            }
            int id = Integer.parseInt(idText);
            String name = new_name.getText();
            if (name.length() == 0) {
                MainPageController.getInstance().showErrorMessage("请输入名称！");
                return;
            }
            int index = new_tid_tname.getSelectionModel().getSelectedIndex();
            if (index < 0) {
                MainPageController.getInstance().showErrorMessage("请选择教师！");
                return;
            }
            int tid = tidList.get(index);
            String tname = tnameList.get(index);
            String pointStr = new_point.getText();
            if (pointStr.length() <= 0) {
                MainPageController.getInstance().showErrorMessage("请输入学分！");
                return;
            }
            int point = Integer.parseInt(pointStr);
            if (point <= 0) {
                MainPageController.getInstance().showErrorMessage("学分必须大于0！");
                return;
            }
            String min_grade_str = new_min_grade.getText();
            if (min_grade_str.length() <= 0) {
                MainPageController.getInstance().showErrorMessage("请输入最低接受年级！");
                return;
            }
            int min_grade = Integer.parseInt(min_grade_str);
            String cancel_year_str = new_cancel_year.getText();
            int cancel_year = 0;
            if (cancel_year_str.length() > 0) {
                cancel_year = Integer.parseInt(cancel_year_str);
            }

            state = 1;
            CourseManager.addCourse(id, name, tid, point, min_grade, cancel_year);
            state = 2;
            table.getItems().add(new CourseTableRecord("" + id, name, tid, tname, point, min_grade, cancel_year));
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
            CourseTableRecord record = (CourseTableRecord) table.getSelectionModel().getSelectedItem();

            String idText = new_id.getText();
            if (idText.length() != 7) {
                MainPageController.getInstance().showErrorMessage("课程ID必须是7位！");
                return;
            }
            int id = Integer.parseInt(idText);
            String name = new_name.getText();
            if (name.length() == 0) {
                MainPageController.getInstance().showErrorMessage("请输入名称！");
                return;
            }
            int index = new_tid_tname.getSelectionModel().getSelectedIndex();
            if (index < 0) {
                MainPageController.getInstance().showErrorMessage("请选择教师！");
                return;
            }
            int tid = tidList.get(index);
            String tname = tnameList.get(index);
            String pointStr = new_point.getText();
            if (pointStr.length() <= 0) {
                MainPageController.getInstance().showErrorMessage("请输入学分！");
                return;
            }
            int point = Integer.parseInt(pointStr);
            if (point <= 0) {
                MainPageController.getInstance().showErrorMessage("学分必须大于0！");
                return;
            }
            String min_grade_str = new_min_grade.getText();
            if (min_grade_str.length() <= 0) {
                MainPageController.getInstance().showErrorMessage("请输入最低接受年级！");
                return;
            }
            int min_grade = Integer.parseInt(min_grade_str);
            String cancel_year_str = new_cancel_year.getText();
            int cancel_year = 0;
            if (cancel_year_str.length() > 0) {
                cancel_year = Integer.parseInt(cancel_year_str);
            }

            state = 1;
            CourseManager.updateCourse(id, name, point, point, min_grade, cancel_year);
            state = 2;
            record.setId(id + "");
            record.setName(name);
            record.setTid(tid);
            record.setTname(tname);
            record.setPoint(point);
            record.setMin_grade(min_grade);
            record.setCancel_year(cancel_year);
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
            CourseTableRecord record = (CourseTableRecord) table.getSelectionModel().getSelectedItem();
            String id = record.getId();

            state = 1;
            CourseManager.deleteCourse(Integer.parseInt(id));

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

    public void loadAllTeachers() {
        new_tid_tname.setDisable(true);
        new_tid_tname.getItems().clear();
        tidList.clear();
        tnameList.clear();
        new BackgroundRunner() {
            int total = 0;
            int id;
            String name;

            @Override
            public void background() throws Exception {
                TeacherRecordSet rs = TeacherManager.getAllTeachers();
                while (rs.next()) {
                    total++;
                    id = rs.getID();
                    name = rs.getName();
                    runForeground();
                }
                rs.close();
            }

            @Override
            public void foreground() {
                tidList.add(id);
                tnameList.add(name);
                new_tid_tname.getItems().add(name + " (" + id + ")");
            }

            @Override
            public void handleException(Exception e) {
                MainPageController.getInstance().showErrorMessage("加载数据出错：" + e.getLocalizedMessage());
                e.printStackTrace();
            }

            @Override
            public void finish() {
                new_tid_tname.setDisable(false);
                MainPageController.getInstance().showMessage("已加载" + total + "位教师");
                CourseTableRecord rec = (CourseTableRecord) table.getSelectionModel().getSelectedItem();
                if (rec != null) {
                    String label = rec.getName() + " (" + rec.getId() + ")";
                    if (new_tid_tname.getItems().contains(label)) {
                        new_tid_tname.getSelectionModel().select(label);
                    }
                }
            }
        }.run();

    }

    public void loadTable() {
        lookup.setText("");
        showBottomPanel(false);
        table.getItems().clear();
        new BackgroundRunner() {
            int total = 0;
            String id, name;
            String tname;
            int tid, min_grade, cancel_year, point;

            @Override
            public void background() throws Exception {
                CourseRecordSet rs = CourseManager.getAllCourses();
                while (rs.next()) {
                    total++;
                    id = rs.getID() + "";
                    name = rs.getName();
                    tid = rs.getTeacherID();
                    point = rs.getPoint();
                    TeacherRecordSet rs2 = TeacherManager.getTeacherByID(tid);
                    if (!rs2.next()) {
                        rs2.close();
                        throw new Exception("Cannot find Teacher: id=" + tid);
                    }
                    tname = rs2.getName();
                    rs2.close();
                    min_grade = rs.getMinGrade();
                    cancel_year = rs.getCancelYear();
                    runForeground();
                }
                rs.close();
            }

            @Override
            public void foreground() {
                table.getItems().add(new CourseTableRecord(id, name, tid, tname, point, min_grade, cancel_year));
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
                TableColumnBuilder.create().text("课程ID").cellValueFactory(new PropertyValueFactory("id")).prefWidth(120).build(),
                TableColumnBuilder.create().text("课程名称").cellValueFactory(new PropertyValueFactory("name")).prefWidth(200).build(),
                TableColumnBuilder.create().text("教师ID").cellValueFactory(new PropertyValueFactory("tid")).prefWidth(90).build(),
                TableColumnBuilder.create().text("教师姓名").cellValueFactory(new PropertyValueFactory("tname")).prefWidth(100).build(),
                TableColumnBuilder.create().text("学分").cellValueFactory(new PropertyValueFactory("point")).prefWidth(90).build(),
                TableColumnBuilder.create().text("最低接受年级").cellValueFactory(new PropertyValueFactory("min_grade")).prefWidth(120).build(),
                TableColumnBuilder.create().text("取消年份").cellValueFactory(new PropertyValueFactory("cancel_year")).prefWidth(90).build());
        chart.getData().clear();
        score_series.setName("成绩分段人数");
        score_series.getData().addAll(score_zone_fail, score_zone_60_69, score_zone_70_79, score_zone_80_89, score_zone_90_99, score_zone_full);
        chart.getData().addAll(score_series);
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                CourseTableRecord record = (CourseTableRecord) table.getSelectionModel().getSelectedItem();
                if (record == null) {
                    return;
                }
                showBottomPanel(false);
                resetChart();
                new_id.setText(record.getId());
                new_name.setText(record.getName());
                new_min_grade.setText(record.getMin_grade() + "");
                new_point.setText(record.getPoint() + "");
                int cancel = record.getCancel_year();
                if (cancel != 0) {
                    new_cancel_year.setText(cancel + "");
                } else {
                    new_cancel_year.setText("");
                }
                new_tid_tname.getSelectionModel().select(record.getTname() + " (" + record.getTid() + ")");//name+" ("+id+")"
            }
        });
    }
    
    private void resetChart(){
        ObservableList<Data<String, Number>>  data = score_series.getData();
        for(int i = 0 ; i < data.size(); i++)
            data.get(i).YValueProperty().setValue(0);
    }
}
