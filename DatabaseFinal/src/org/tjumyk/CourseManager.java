/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.tjumyk;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author E40-G8C
 */
public class CourseManager {

    public static final String TABLE_NAME = "Course";

    public static void addCourse(int id, String name, int tid, int point, int min_grade, int cancel_year) throws SQLException {
        Main.dbManager.execSQL("INSERT INTO " + TABLE_NAME + " (cid,cname,tid,point,min_grade,cancel_year) VALUES ("
                + id + ",\'" + name + "\'," + tid + "," + point + "," + min_grade + "," + cancel_year + ")");
    }

    static void updateCourse(int id, String name, int tid, int point, int min_grade, int cancel_year) throws SQLException {
        Main.dbManager.execSQL("UPDATE " + TABLE_NAME + " SET "
                + "cid=" + id
                + ",cname=\'" + name
                + "\',tid=" + tid
                + ",point=" + point
                + ",min_grade=" + min_grade
                + ",cancel_year=" + cancel_year
                + " WHERE cid=" + id);
    }

    static void deleteCourse(long id) throws SQLException {
        Main.dbManager.execSQL("DELETE  FROM " + TABLE_NAME + " WHERE cid=" + id);
    }

    public static CourseRecordSet getAllCourses() throws SQLException {
        return new CourseRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME));
    }

    public static CourseRecordSet getCourseByID(int cid) throws SQLException {
        return new CourseRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME + " row WHERE row.cid=" + cid));
    }

    public static CourseRecordSet searchCoursesByID(long id) throws SQLException {
        return new CourseRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME + " row WHERE row.cid LIKE \'%" + id + "%\'"));
    }

    public static CourseRecordSet searchCoursesByName(String name) throws SQLException {
        return new CourseRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME + " row WHERE row.cname LIKE \'%" + name + "%\'"));
    }

    public static class CourseRecordSet {

        private ResultSet rs;

        public CourseRecordSet(ResultSet rs) {
            this.rs = rs;
        }

        public void close() throws SQLException {
            rs.getStatement().close();
            rs.close();
        }

        public boolean next() throws SQLException {
            return rs.next();
        }

        public int getID() throws SQLException {
            return rs.getInt("cid");
        }

        public String getName() throws SQLException {
            return rs.getString("cname");
        }

        public int getTeacherID() throws SQLException {
            return rs.getInt("tid");
        }

        public int getPoint() throws SQLException {
            return rs.getInt("point");
        }

        public int getMinGrade() throws SQLException {
            return rs.getInt("min_grade");
        }

        public int getCancelYear() throws SQLException {
            return rs.getInt("cancel_year");
        }
    }
}
