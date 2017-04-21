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
public class ClassManager {
     public static final String TABLE_NAME = "Class";
    
    public static void addClass(int id,String name) throws SQLException{
        Main.dbManager.execSQL("INSERT INTO "+TABLE_NAME+" (classid,classname) VALUES ("
                +id+",\'"+name+"\')");
    }
	
	static void updateClass(long id, String name) throws SQLException{
        Main.dbManager.execSQL("UPDATE "+TABLE_NAME+" SET "
                +"classid="+id
                +",classname=\'"+name
                +"\' WHERE classid="+id);
    }
    
    static void deleteClass(long id) throws SQLException {
        Main.dbManager.execSQL("DELETE  FROM "+ TABLE_NAME+ " WHERE classid="+id);
    }
    
    public static ClassRecordSet getAllClasses() throws SQLException{
        return new ClassRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM "+ TABLE_NAME));
    }
    
    public static ClassRecordSet getClassByID(int classID) throws SQLException{
        return new ClassRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM "+ TABLE_NAME +" row WHERE row.classid="+classID));
    }
	
	public static ClassRecordSet searchClassesByID(long id) throws SQLException{
        return new ClassRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM "+ TABLE_NAME + " row WHERE row.classid LIKE \'%"+id+"%\'"));
    }

    public static ClassRecordSet searchClassesByName(String name) throws SQLException{
        return new ClassRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM "+ TABLE_NAME + " row WHERE row.classname LIKE \'%"+name+"%\'"));
    }
    
    public static class ClassRecordSet{
        private ResultSet rs;
        public ClassRecordSet(ResultSet rs){
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
            return rs.getInt("classid");
        }
        
        public String getName() throws SQLException{
            return rs.getString("classname");
        }
    }
}
