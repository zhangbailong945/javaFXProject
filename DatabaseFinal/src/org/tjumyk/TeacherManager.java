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
public class TeacherManager {
     public static final String TABLE_NAME = "Teacher";
    
    public static void addTeacher(int id,String name) throws SQLException{
        Main.dbManager.execSQL("INSERT INTO "+TABLE_NAME+" (tid,tname) VALUES ("
                +id+",\'"+name+"\')");
    }
	
	static void updateTeacher(long id, String name) throws SQLException{
        Main.dbManager.execSQL("UPDATE "+TABLE_NAME+" SET "
                +"tid="+id
                +",tname=\'"+name
                +"\' WHERE tid="+id);
    }
    
    static void deleteTeacher(long id) throws SQLException {
        Main.dbManager.execSQL("DELETE  FROM "+ TABLE_NAME+ " WHERE tid="+id);
    }
    
    public static TeacherRecordSet getAllTeachers() throws SQLException{
        return new TeacherRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM "+ TABLE_NAME));
    }
    
    public static TeacherRecordSet getTeacherByID(int tID) throws SQLException{
        return new TeacherRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM "+ TABLE_NAME +" row WHERE row.tid="+tID));
    }
	
	public static TeacherRecordSet searchTeachersByID(long id) throws SQLException{
        return new TeacherRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM "+ TABLE_NAME + " row WHERE row.tid LIKE \'%"+id+"%\'"));
    }

    public static TeacherRecordSet searchTeachersByName(String name) throws SQLException{
        return new TeacherRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM "+ TABLE_NAME + " row WHERE row.tname LIKE \'%"+name+"%\'"));
    }
    
    public static class TeacherRecordSet{
        private ResultSet rs;
        public TeacherRecordSet(ResultSet rs){
            this.rs = rs;
        }
        public void close() throws SQLException{
            rs.getStatement().close();
            rs.close();
        }
        public boolean next() throws SQLException{
            return rs.next();
        }
        
        public int getID() throws SQLException{
            return rs.getInt("tid");
        }
        
        public String getName() throws SQLException{
            return rs.getString("tname");
        }
    }
}
