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
import org.tjumyk.CourseManager.CourseRecordSet;
import org.tjumyk.StudentManager.StudentRecordSet;
import org.tjumyk.TakeCourseManager.TakeCourseRecordSet;

/**
 * FXML Controller class
 *
 * @author E40-G8C
 */
public class StudentPanelController implements Initializable {

    private static StudentPanelController instance;

    public static StudentPanelController getInstance() {
        return instance;
    }
    @FXML
    TextField lookup, jump_Page, new_id, new_name, new_start_year, new_start_age;
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
    ChoiceBox new_gender;
    @FXML
    ComboBox new_class;
    @FXML
    HBox buttonPane;
    @FXML
    AnchorPane tableContainer, calculatePanel;
    @FXML
    BarChart chart;
    @FXML
    Label cal_max_score, cal_min_score, cal_fail_count, cal_fail_rate, cal_ave_score, cal_power_ave_score;
    @FXML
    Button cal_return;
     XYChart.Series<String, Number> score_series = new XYChart.Series<String, Number>();
     
    ArrayList<Integer> classIDList = new ArrayList<Integer>();
   

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
            String className, id, name, gender;
            int sy, sa;

            @Override
            public void background() throws Exception {
                int idNum = 0;
                boolean isID = true;
                try {
                    idNum = Integer.parseInt(input);
                } catch (Exception e1) {
                    isID = false;
                }
                StudentRecordSet rs;
                if (isID) {
                    rs = StudentManager.searchStudentsByID(idNum);
                } else {
                    rs = StudentManager.searchStudentsByName(input);
                }
                while (rs.next()) {
                    total++;
                    int classID = rs.getClassID();
                    ClassRecordSet rs2 = ClassManager.getClassByID(classID);
                    if (!rs2.next()) {
                        rs2.close();
                        throw new Exception("Class Not Found: ID=" + classID);
                    }
                    className = rs2.getName();
                    id = rs.getID() + "";
                    name = rs.getName();
                    gender = rs.getGender() ? "女" : "男";
                    sy = rs.getStartYear();
                    sa = rs.getStartAge();
                    runForeground();
                }
                rs.close();
            }

            @Override
            public void foreground() {
                table.getItems().add(new StudentTableRecord(id, name, gender, sy, sa, className));
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
        loadAllClasses();
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
        loadAllClasses();
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
        StudentTableRecord record = (StudentTableRecord) table.getSelectionModel().getSelectedItem();
        if (record == null) {
            MainPageController.getInstance().showErrorMessage("请先选择需要进行统计的目标！");
            return;
        }
        cal_ave_score.setText("NaN");
        cal_fail_count.setText("NaN");
        cal_fail_rate.setText("NaN");
        cal_max_score.setText("NaN");
        cal_min_score.setText("NaN");
        cal_power_ave_score.setText("NaN");
        score_series.getData().clear();
        showCalculatePanel(true);
        MainPageController.getInstance().showMessage("正在进行统计...");
        final long sid = Long.parseLong(record.getId());
        new BackgroundRunner() {
            int max = -1, min = TakeCourseManager.MAX_SCORE + 1, fail_count = 0;
            double fail_rate, ave, power_ave;
            String current_cname;
            int current_score;
            int total_point = 0, total_count = 0;

            @Override
            public void background() throws Exception {
                TakeCourseRecordSet rs = TakeCourseManager.getTakeCoursesBySID(sid);
                while (rs.next()) {
                    int score = rs.getScore();
                    int cid = rs.getCID();
                    CourseRecordSet rs2 = CourseManager.getCourseByID(cid);
                    if (!rs2.next()) {
                        rs2.close();
                        throw new Exception("Cannot find Course： ID=" + cid);
                    }
                    int point = rs2.getPoint();
                    current_cname = rs2.getName();
                    rs2.close();

                    total_count++;
                    total_point += point;
                    current_score = score;
                    if (max < score) {
                        max = score;
                    }
                    if (min > score) {
                        min = score;
                    }
                    if (score < TakeCourseManager.MAX_SCORE * 0.6) {
                        fail_count++;
                    }
                    ave += TakeCourseManager.calculateScorePoint(score) * point;
                    power_ave += score * point;
                    runForeground();
                }
                rs.close();
            }

            @Override
            public void foreground() throws Exception {
                score_series.getData().add(new XYChart.Data<String, Number>(current_cname, current_score));
            }

            @Override
            public void handleException(Exception e) throws Exception {
                MainPageController.getInstance().showErrorMessage("加载数据出错：" + e.getLocalizedMessage());
                e.printStackTrace();
            }

            @Override
            public void finish() throws Exception {
                if (total_count <= 0) {
                    MainPageController.getInstance().showErrorMessage("该生没有选课记录！");
                    return;
                }
                fail_rate = fail_count * 1.0 / total_count;
                ave /= total_point;
                power_ave /= total_point;

                cal_ave_score.setText(((int) (ave * 100)) / 100.0 + "");
                cal_fail_count.setText(fail_count + "");
                cal_fail_rate.setText((int) (fail_rate * 100) + "%");
                cal_max_score.setText(max + "");
                cal_min_score.setText(min + "");
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
            if (idText.length() != 10) {
                MainPageController.getInstance().showErrorMessage("学号长度必须是10位！");
                return;
            }
            long id = Long.parseLong(idText);
            String name = new_name.getText();
            if (name.length() == 0) {
                MainPageController.getInstance().showErrorMessage("请输入姓名！");
                return;
            }
            int gender = new_gender.getSelectionModel().getSelectedIndex();//控件保证了要么是男，要么是女
            if (gender != 0 && gender != 1) {
                MainPageController.getInstance().showErrorMessage("请选择性别！");
                return;
            }
            String genderText = (String) new_gender.getSelectionModel().getSelectedItem();
            String sy = new_start_year.getText();
            String sa = new_start_age.getText();
            if (sy.length() == 0) {
                MainPageController.getInstance().showErrorMessage("请输入入学年份！");
                return;
            }
            if (sa.length() == 0) {
                MainPageController.getInstance().showErrorMessage("请输入入学年龄！");
                return;
            }
            int startYear = Integer.parseInt(sy);
            int startAge = Integer.parseInt(sa);
            if (startAge < 10 || startAge > 50) {
                MainPageController.getInstance().showErrorMessage("入学年龄超出范围[10,50]！");
                return;
            }
            String className = (String) new_class.getSelectionModel().getSelectedItem();
            if (className == null) {
                MainPageController.getInstance().showErrorMessage("请选择班级！");
                return;
            }
            int classID = classIDList.get(new_class.getSelectionModel().getSelectedIndex());
            state = 1;
            StudentManager.addStudent(id, name, gender == 1, startYear, startAge, classID);
            state = 2;
            table.getItems().add(new StudentTableRecord("" + id, name, genderText, startYear, startAge, className));
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
            StudentTableRecord record = (StudentTableRecord) table.getSelectionModel().getSelectedItem();

            String idText = new_id.getText();
            if (idText.length() != 10) {
                MainPageController.getInstance().showErrorMessage("学号长度必须是10位！");
                return;
            }
            long id = Long.parseLong(idText);
            String name = new_name.getText();
            if (name.length() == 0) {
                MainPageController.getInstance().showErrorMessage("请输入姓名！");
                return;
            }
            int gender = new_gender.getSelectionModel().getSelectedIndex();//控件保证了要么是男，要么是女
            if (gender != 0 && gender != 1) {
                MainPageController.getInstance().showErrorMessage("请选择性别！");
                return;
            }
            String genderText = (String) new_gender.getSelectionModel().getSelectedItem();
            String sy = new_start_year.getText();
            String sa = new_start_age.getText();
            if (sy.length() == 0) {
                MainPageController.getInstance().showErrorMessage("请输入入学年份！");
                return;
            }
            if (sa.length() == 0) {
                MainPageController.getInstance().showErrorMessage("请输入入学年龄！");
                return;
            }
            int startYear = Integer.parseInt(sy);
            int startAge = Integer.parseInt(sa);
            if (startAge < 10 || startAge > 50) {
                MainPageController.getInstance().showErrorMessage("入学年龄超出范围[10,50]！");
                return;
            }
            String className = (String) new_class.getSelectionModel().getSelectedItem();
            if (className == null) {
                MainPageController.getInstance().showErrorMessage("请选择班级！");
                return;
            }
            int classID = classIDList.get(new_class.getSelectionModel().getSelectedIndex());
            state = 1;
            StudentManager.updateStudent(id, name, gender == 1, startYear, startAge, classID);
            state = 2;
            record.setId(id + "");
            record.setName(name);
            record.setGender(genderText);
            record.setStartYear(startYear);
            record.setStartAge(startAge);
            record.setClassName(className);

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
            StudentTableRecord record = (StudentTableRecord) table.getSelectionModel().getSelectedItem();
            String id = record.getId();

            state = 1;
            StudentManager.deleteStudent(Long.parseLong(id));

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
            String id, name, gender, className;
            int sy, sa;

            @Override
            public void background() throws Exception {
                StudentRecordSet rs = StudentManager.getAllStudents();
                while (rs.next()) {
                    total++;
                    int classID = rs.getClassID();
                    ClassRecordSet rs2 = ClassManager.getClassByID(classID);
                    if (!rs2.next()) {
                        rs2.close();
                        throw new Exception("Class Not Found: ID=" + classID);
                    }
                    className = rs2.getName();
                    rs2.close();
                    id = rs.getID() + "";
                    name = rs.getName();
                    gender = rs.getGender() ? "女" : "男";
                    sy = rs.getStartYear();
                    sa = rs.getStartAge();
                    runForeground();
                }
                rs.close();
            }

            @Override
            public void foreground() {
                table.getItems().add(new StudentTableRecord(id, name, gender, sy, sa, className));
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

    private void loadAllClasses() {
        new_class.getItems().clear();
        classIDList.clear();
        new_class.setDisable(true);
        new BackgroundRunner() {
            String name;
            int id;
            int total;

            @Override
            public void background() throws Exception {
                ClassRecordSet rs = ClassManager.getAllClasses();
                total = 0;
                while (rs.next()) {
                    total++;
                    name = rs.getName();
                    id = rs.getID();
                    runForeground();
                }
                rs.close();
            }

            @Override
            public void foreground() throws Exception {
                new_class.getItems().add(name);
                classIDList.add(id);
            }

            @Override
            public void handleException(Exception e) throws Exception {
                MainPageController.getInstance().showErrorMessage("加载班级出错：" + e.getLocalizedMessage());
                e.printStackTrace();
            }

            @Override
            public void finish() throws Exception {
                new_class.setDisable(false);
                MainPageController.getInstance().showMessage("已加载" + total + "个班级");
                StudentTableRecord rec = (StudentTableRecord) table.getSelectionModel().getSelectedItem();
                if (rec != null) {
                    String selected = rec.getClassName();
                    if (new_class.getItems().contains(selected)) {
                        new_class.getSelectionModel().select(selected);
                    }
                }
            }
        }.run();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        instance = this;
        new_gender.getItems().clear();
        new_gender.getItems().addAll("男", "女");
        new_class.getItems().clear();
        table.getColumns().addAll(
                TableColumnBuilder.create().text("ID").cellValueFactory(new PropertyValueFactory("id")).prefWidth(110).build(),
                TableColumnBuilder.create().text("姓名").cellValueFactory(new PropertyValueFactory("name")).prefWidth(90).build(),
                TableColumnBuilder.create().text("性别").cellValueFactory(new PropertyValueFactory("gender")).prefWidth(40).build(),
                TableColumnBuilder.create().text("入学年份").cellValueFactory(new PropertyValueFactory("startYear")).prefWidth(90).build(),
                TableColumnBuilder.create().text("入学年龄").cellValueFactory(new PropertyValueFactory("startAge")).prefWidth(90).build(),
                TableColumnBuilder.create().text("班级").cellValueFactory(new PropertyValueFactory("className")).prefWidth(160).build());
        chart.getData().clear();
        score_series.setName("课程成绩");
        chart.getData().addAll(score_series);
        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                StudentTableRecord record = (StudentTableRecord) table.getSelectionModel().getSelectedItem();
                if (record == null) {
                    return;
                }
                showBottomPanel(false);
                score_series.getData().clear();
                new_id.setText(record.getId());
                new_name.setText(record.getName());
                new_gender.getSelectionModel().select(record.getGender().equals("男") ? 0 : 1);
                new_start_year.setText(record.getStartYear() + "");
                new_start_age.setText(record.getStartAge() + "");
                new_class.getSelectionModel().select(record.getClassName());
            }
        });
    }
}
