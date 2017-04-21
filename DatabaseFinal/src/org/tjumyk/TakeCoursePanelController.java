/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tjumyk;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.tjumyk.CourseManager.CourseRecordSet;
import org.tjumyk.StudentManager.StudentRecordSet;
import org.tjumyk.TakeCourseManager.TakeCourseRecordSet;

/**
 * FXML Controller class
 *
 * @author E40-G8C
 */
public class TakeCoursePanelController implements Initializable {

    private static TakeCoursePanelController instance;

    public static TakeCoursePanelController getInstance() {
        return instance;
    }
    @FXML
    TextField lookupStudent, lookupCourse, jump_Page;
    @FXML
    TextField new_sid, new_year, new_score;
    @FXML
    ComboBox new_cname_cid;
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
    
    ArrayList<Integer> cidList = new ArrayList<Integer>();
    ArrayList<String> cnameList = new ArrayList<String>();

    @FXML
    private void LookUpFired(ActionEvent e) {
        final boolean lookups = lookupStudent.getText().length() > 0;
        final boolean lookupc = lookupCourse.getText().length() > 0;
        if ((!lookups) && (!lookupc)) {
            loadTable();
            return;
        }
        table.getItems().clear();
        final String inputs = lookupStudent.getText();
        final String inputc = lookupCourse.getText();

        MainPageController.getInstance().showMessage("正在查询中...");
        new BackgroundRunner() {
            int total = 0;
            long sid;
            String sname, cname;
            int cid, year, score;

            @Override
            public void background() throws Exception {

                long idNums = 0;
                int idNumc = 0;
                boolean isIDs = true, isIDc = true;
                try {
                    idNums = Long.parseLong(inputs);
                } catch (Exception e1) {
                    isIDs = false;
                }
                try {
                    idNumc = Integer.parseInt(inputc);
                } catch (Exception e1) {
                    isIDc = false;
                }
                TakeCourseRecordSet rs;
                if (!lookupc) {//仅查询Student
                    if (isIDs) {
                        rs = TakeCourseManager.searchTakeCoursesBySID(idNums);
                    } else {
                        rs = TakeCourseManager.searchTakeCoursesBySName(inputs);
                    }
                } else if (!lookups) {//仅查询Course
                    if (isIDc) {
                        rs = TakeCourseManager.searchTakeCoursesByCID(idNumc);
                    } else {
                        rs = TakeCourseManager.searchTakeCoursesByCName(inputc);
                    }
                } else {//两个查询条件
                    if (isIDs && isIDc) {
                        rs = TakeCourseManager.searchTakeCoursesBySIDAndCID(idNums, idNumc);
                    } else if (isIDs && !isIDc) {
                        rs = TakeCourseManager.searchTakeCoursesBySIDAndCName(idNums, inputc);
                    } else if (!isIDs && isIDc) {
                        rs = TakeCourseManager.searchTakeCoursesBySNameAndCID(inputs, idNumc);
                    } else {
                        rs = TakeCourseManager.searchTakeCoursesBySNameAndCName(inputs, inputc);
                    }
                }
                while (rs.next()) {
                    total++;
                    sid = rs.getSID();
                    cid = rs.getCID();
                    year = rs.getYear();
                    score = rs.getScore();
                    StudentRecordSet rs2 = StudentManager.getStudentByID(sid);
                    if (!rs2.next()) {
                        rs2.close();
                        throw new Exception("Cannot find Student: id=" + sid);
                    }
                    sname = rs2.getName();
                    rs2.close();
                    CourseRecordSet rs3 = CourseManager.getCourseByID(cid);
                    if (!rs3.next()) {
                        rs3.close();
                        throw new Exception("Cannot find Course: id=" + cid);
                    }
                    cname = rs3.getName();
                    rs3.close();

                    runForeground();
                }
                rs.close();
            }

            @Override
            public void foreground() {
                table.getItems().add(new TakeCourseTableRecord(sid + "", sname, cid, cname, year, score));
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
        MainPageController.getInstance().showMessage("正在准备数据...");
        sureAddBtn.setVisible(true);
        sureAddBtn.toFront();
        sureUpdateBtn.setVisible(false);
        sureDeleteBtn.setVisible(false);
        deletePrompt.setVisible(false);
        new_panel.setVisible(true);
        showBottomPanel(true);
        loadAllCourses();
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
        loadAllCourses();
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
        int state = 0;//0-prepare;1-checkSQL;2-execSQL;3-addToTable
        try {
            String idText = new_sid.getText();
            if (idText.length() == 0) {
                MainPageController.getInstance().showErrorMessage("请输入学号！");
                return;
            }
            long sid = Long.parseLong(idText);
//            idText = new_cid.getText();
//            if (idText.length() == 0) {
//                MainPageController.getInstance().showErrorMessage("请输入课程ID！");
//                return;
//            }
//            int cid = Integer.parseInt(idText);
            int index = new_cname_cid.getSelectionModel().getSelectedIndex();
            if (index < 0) {
                MainPageController.getInstance().showErrorMessage("请选择课程！");
                return;
            }
            int cid = cidList.get(index);
            String cname = cnameList.get(index);
            
            idText = new_year.getText();
            if (idText.length() == 0) {
                MainPageController.getInstance().showErrorMessage("请输入选课年份！");
                return;
            }
            int year = Integer.parseInt(idText);
            idText = new_score.getText();
            if (idText.length() == 0) {
                MainPageController.getInstance().showErrorMessage("请输入成绩！");
                return;
            }
            int score = Integer.parseInt(idText);

            state = 1;
            StudentRecordSet rs2 = StudentManager.getStudentByID(sid);
            if (!rs2.next()) {
                rs2.close();
                MainPageController.getInstance().showErrorMessage("未找到对应学生：ID=" + sid);
                return;
            }
            String sname = rs2.getName();
            int startYear = rs2.getStartYear();
            rs2.close();
            CourseRecordSet rs3 = CourseManager.getCourseByID(cid);
            if (!rs3.next()) {
                rs3.close();
                MainPageController.getInstance().showErrorMessage("未找到对应课程：ID=" + cid);
                return;
            }
            int cy = rs3.getCancelYear();
            if(cy > 0 && year >= cy){
                MainPageController.getInstance().showErrorMessage("该课程已经在选课时间前取消！");
                return;
            }
            if(year - startYear + 1 < rs3.getMinGrade()){
                MainPageController.getInstance().showErrorMessage("该学生选课时的年级低于所选课程的最低年级要求！");
                return;
            }
            rs3.close();
            
            state =2;
            TakeCourseManager.addTakeCourse(sid, cid, year, score);
            
            
            state = 3;
            table.getItems().add(new TakeCourseTableRecord(sid + "", sname, cid, cname, year, score));
            showBottomPanel(false);
            MainPageController.getInstance().showMessage("记录已成功添加！");
        } catch (Exception e1) {
            if (state == 0) {
                MainPageController.getInstance().showErrorMessage("数据格式不正确！" + e1.getLocalizedMessage());
            } else if (state == 1) {
                MainPageController.getInstance().showErrorMessage("校验时产生数据库错误：" + e1.getLocalizedMessage());
            }else if (state == 2) {
                MainPageController.getInstance().showErrorMessage("数据库错误：" + e1.getLocalizedMessage());
            } else {
                MainPageController.getInstance().showErrorMessage("表格控件错误：" + e1.getLocalizedMessage());
            }
            e1.printStackTrace();
        }
    }

    @FXML
    private void SureUpdateFired(ActionEvent e) {
        int state = 0;//0-prepare;1-checkSQL;2-execSQL;3-addToTable
        try {
            TakeCourseTableRecord record = (TakeCourseTableRecord) table.getSelectionModel().getSelectedItem();

            String idText = new_sid.getText();
            if (idText.length() == 0) {
                MainPageController.getInstance().showErrorMessage("请输入学号！");
                return;
            }
            long sid = Long.parseLong(idText);
//            idText = new_cid.getText();
//            if (idText.length() == 0) {
//                MainPageController.getInstance().showErrorMessage("请输入课程ID！");
//                return;
//            }
//            int cid = Integer.parseInt(idText);
            int index = new_cname_cid.getSelectionModel().getSelectedIndex();
            if (index < 0) {
                MainPageController.getInstance().showErrorMessage("请选择课程！");
                return;
            }
            int cid = cidList.get(index);
            String cname = cnameList.get(index);
            
            idText = new_year.getText();
            if (idText.length() == 0) {
                MainPageController.getInstance().showErrorMessage("请输入选课年份！");
                return;
            }
            int year = Integer.parseInt(idText);
            idText = new_score.getText();
            if (idText.length() == 0) {
                MainPageController.getInstance().showErrorMessage("请输入成绩！");
                return;
            }
            int score = Integer.parseInt(idText);

            state = 1;
            StudentRecordSet rs2 = StudentManager.getStudentByID(sid);
            if (!rs2.next()) {
                rs2.close();
                MainPageController.getInstance().showErrorMessage("未找到对应学生：ID=" + sid);
                return;
            }
            String sname = rs2.getName();
            int startYear = rs2.getStartYear();
            rs2.close();
            CourseRecordSet rs3 = CourseManager.getCourseByID(cid);
            if (!rs3.next()) {
                rs3.close();
                MainPageController.getInstance().showErrorMessage("未找到对应课程：ID=" + cid);
                return;
            }
            int cy = rs3.getCancelYear();
            if(cy > 0 && year >= cy){
                MainPageController.getInstance().showErrorMessage("该课程已经在选课时间前取消！");
                return;
            }
            if(year - startYear + 1 < rs3.getMinGrade()){
                MainPageController.getInstance().showErrorMessage("该学生选课时的年级低于所选课程的最低年级要求！");
                return;
            }
            rs3.close();
            
            state = 2;
            TakeCourseManager.updateTakeCourse(sid, cid, year, score);
            
            state = 3;
            record.setSid(sid + "");
            record.setCid(cid);
            record.setSname(sname);
            record.setCname(cname);
            record.setYear(year);
            record.setScore(score);

            showBottomPanel(false);
            MainPageController.getInstance().showMessage("记录已成功更新！");
        } catch (Exception e1) {
            if (state == 0) {
                MainPageController.getInstance().showErrorMessage("数据格式不正确！" + e1.getLocalizedMessage());
            } else if (state == 1) {
                MainPageController.getInstance().showErrorMessage("校验时产生数据库错误：" + e1.getLocalizedMessage());
            } else if (state == 2) {
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
            TakeCourseTableRecord record = (TakeCourseTableRecord) table.getSelectionModel().getSelectedItem();
            long sid = Long.parseLong(record.getSid());
            int cid = record.getCid();

            state = 1;
            TakeCourseManager.deleteTakeCourse(sid, cid);

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
    
    public void loadAllCourses(){
        new_cname_cid.setDisable(true);
        new_cname_cid.getItems().clear();
        cidList.clear();
        cnameList.clear();
        new BackgroundRunner() {
            int total = 0;
            int id;
            String name;
            
            @Override
            public void background() throws Exception {
                CourseRecordSet rs = CourseManager.getAllCourses();
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
                cidList.add(id);
                cnameList.add(name);
                new_cname_cid.getItems().add(name+" ("+id+")");
            }

            @Override
            public void handleException(Exception e) {
                MainPageController.getInstance().showErrorMessage("加载数据出错：" + e.getLocalizedMessage());
                e.printStackTrace();
            }

            @Override
            public void finish() {
                new_cname_cid.setDisable(false);
                MainPageController.getInstance().showMessage("已加载" + total + "个课程");
                TakeCourseTableRecord rec = (TakeCourseTableRecord) table.getSelectionModel().getSelectedItem();
                if (rec != null) {
                    String label = rec.getCname()+" ("+rec.getCid()+")";
                    if (new_cname_cid.getItems().contains(label)) {
                        new_cname_cid.getSelectionModel().select(label);
                    }
                }
            }
        }.run();
    }

    public void loadTable() {
        lookupCourse.setText("");
        lookupStudent.setText("");
        showBottomPanel(false);
        table.getItems().clear();
        new BackgroundRunner() {
            int total = 0;
            long sid;
            int cid, year, score;
            String sname, cname;

            @Override
            public void background() throws Exception {
                TakeCourseRecordSet rs = TakeCourseManager.getAllTakeCourses();
                while (rs.next()) {
                    total++;
                    sid = rs.getSID();
                    cid = rs.getCID();
                    year = rs.getYear();
                    score = rs.getScore();
                    StudentRecordSet rs2 = StudentManager.getStudentByID(sid);
                    if (!rs2.next()) {
                        rs2.close();
                        throw new Exception("Cannot find Student: id=" + sid);
                    }
                    sname = rs2.getName();
                    rs2.close();
                    CourseRecordSet rs3 = CourseManager.getCourseByID(cid);
                    if (!rs3.next()) {
                        rs3.close();
                        throw new Exception("Cannot find Course: id=" + cid);
                    }
                    cname = rs3.getName();
                    rs3.close();
                    runForeground();
                }
                rs.close();
            }

            @Override
            public void foreground() {
                table.getItems().add(new TakeCourseTableRecord(sid + "", sname, cid, cname, year, score));
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
                TableColumnBuilder.create().text("学号").cellValueFactory(new PropertyValueFactory("sid")).prefWidth(110).build(),
                TableColumnBuilder.create().text("姓名").cellValueFactory(new PropertyValueFactory("sname")).prefWidth(90).build(),
                TableColumnBuilder.create().text("课程ID").cellValueFactory(new PropertyValueFactory("cid")).prefWidth(100).build(),
                TableColumnBuilder.create().text("课程名称").cellValueFactory(new PropertyValueFactory("cname")).prefWidth(200).build(),
                TableColumnBuilder.create().text("选课年份").cellValueFactory(new PropertyValueFactory("year")).prefWidth(100).build(),
                TableColumnBuilder.create().text("成绩").cellValueFactory(new PropertyValueFactory("score")).prefWidth(100).build());

        table.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TakeCourseTableRecord record = (TakeCourseTableRecord) table.getSelectionModel().getSelectedItem();
                if (record == null) {
                    return;
                }
                showBottomPanel(false);
                new_sid.setText(record.getSid());
                //new_cid.setText(record.getCid() + "");
                new_year.setText(record.getYear() + "");
                new_score.setText(record.getScore() + "");
                new_cname_cid.getSelectionModel().select(record.getCname()+" ("+record.getCid()+")");
            }
        });
    }
}
