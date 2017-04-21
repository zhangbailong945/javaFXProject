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
public class StudentManager {

    public static final String TABLE_NAME = "Student";

    public static void addStudent(long id, String name, boolean gender, int start_year, int start_age, int classID) throws SQLException {
        Main.dbManager.execSQL("INSERT INTO " + TABLE_NAME + " (sid,sname,gender,start_age,start_year,classid) VALUES ("
                + id + ",\'" + name + "\'," + (gender ? 1 : 0) + "," + start_age + "," + start_year + "," + classID + ")");
    }

    static void updateStudent(long id, String name, boolean gender, int start_year, int start_age, int classID) throws SQLException {
        Main.dbManager.execSQL("UPDATE " + TABLE_NAME + " SET "
                + "sid=" + id
                + ",sname=\'" + name
                + "\',gender=" + (gender ? 1 : 0)
                + ",start_age=" + start_age
                + ",start_year=" + start_year
                + ",classid=" + classID + " WHERE sid=" + id);
    }

    static void deleteStudent(long id) throws SQLException {
        Main.dbManager.execSQL("DELETE  FROM " + TABLE_NAME + " WHERE sid=" + id);
    }

    public static StudentRecordSet getAllStudents() throws SQLException {
        return new StudentRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME));
    }

    public static StudentRecordSet getStudentByID(long sid) throws SQLException {
        return new StudentRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME + " row WHERE row.sid=" + sid));
    }

    public static StudentRecordSet getStudentsByClassID(int classID) throws SQLException {
        return new StudentRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME + " row WHERE row.classid=" + classID));
    }

    public static StudentRecordSet searchStudentsByID(long id) throws SQLException {
        return new StudentRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME + " row WHERE row.sid LIKE \'%" + id + "%\'"));
    }

    public static StudentRecordSet searchStudentsByName(String name) throws SQLException {
        return new StudentRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME + " row WHERE row.sname LIKE \'%" + name + "%\'"));
    }

    public static class StudentRecordSet {

        private ResultSet rs;

        public StudentRecordSet(ResultSet rs) {
            this.rs = rs;
        }

        public void close() throws SQLException {
            rs.getStatement().close();
            rs.close();
        }

        public boolean next() throws SQLException {
            return rs.next();
        }

        public long getID() throws SQLException {
            return rs.getBigDecimal("sid").longValue();
        }

        public String getName() throws SQLException {
            return rs.getString("sname");
        }

        public boolean getGender() throws SQLException {
            return rs.getInt("gender") == 1;
        }

        public int getStartYear() throws SQLException {
            return rs.getInt("start_year");
        }

        public int getStartAge() throws SQLException {
            return rs.getInt("start_age");
        }

        public int getClassID() throws SQLException {
            return rs.getInt("classid");
        }
    }
}
