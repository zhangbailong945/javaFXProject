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
public class TakeCourseManager {

    public static final String TABLE_NAME = "TakeCourse";
    public static final int MAX_SCORE = 100;
    
    public static double calculateScorePoint(int score){
        if(score>=90)
            return 4.0;
        if(score >= 85)
            return 3.7;
        if(score >= 82)
            return 3.3;
        if(score >= 78)
            return 3.0;
        if(score >= 75)
            return 2.7;
        if(score >= 72)
            return 2.3;
        if(score >= 68)
            return 2.0;
        if(score >= 64)
            return 1.5;
        if(score >= 60)
            return 1.0;
        return 0;
    }

    public static void addTakeCourse(long sid, int cid, int year, int score) throws SQLException {
        Main.dbManager.execSQL("INSERT INTO " + TABLE_NAME + " (sid,cid,year,score) VALUES ("
                + sid + "," + cid + "," + year + "," + score + ")");
    }

    static void updateTakeCourse(long sid, int cid, int year, int score) throws SQLException {
        Main.dbManager.execSQL("UPDATE " + TABLE_NAME + " SET "
                + "sid=" + sid
                + ",cid=" + cid
                + ",year=" + year
                + ",score=" + score
                + " WHERE sid=" + sid + " AND cid=" + cid);
    }

    static void deleteTakeCourse(long sid, int cid) throws SQLException {
        Main.dbManager.execSQL("DELETE  FROM " + TABLE_NAME + " WHERE sid=" + sid + " AND cid=" + cid);
    }

    public static TakeCourseRecordSet getAllTakeCourses() throws SQLException {
        return new TakeCourseRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME));
    }

    public static TakeCourseRecordSet getTakeCourseBySIDAndCID(int sid, int cid) throws SQLException {
        return new TakeCourseRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME + " row WHERE row.sid=" + sid + " AND row.cid=" + cid));
    }
    
    public static TakeCourseRecordSet getTakeCoursesBySID(long sid) throws SQLException {
        return new TakeCourseRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME + " row WHERE row.sid=" + sid ));
    }

    public static TakeCourseRecordSet getTakeCoursesByCID(long cid) throws SQLException {
        return new TakeCourseRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME + " row WHERE row.cid=" + cid ));
    }

    public static TakeCourseRecordSet searchTakeCoursesBySID(long sid) throws SQLException {
        return new TakeCourseRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME + " row WHERE row.sid LIKE \'%" + sid + "%\'"));
    }

    public static TakeCourseRecordSet searchTakeCoursesByCID(long cid) throws SQLException {
        return new TakeCourseRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME + " row WHERE row.cid LIKE \'%" + cid + "%\'"));
    }

    public static TakeCourseRecordSet searchTakeCoursesBySName(String sname) throws SQLException {
        return new TakeCourseRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME + " row WHERE row.sid IN ( SELECT s.sid FROM "
                +StudentManager.TABLE_NAME+" s WHERE s.sname LIKE \'%" + sname + "%\')"));
    }

    public static TakeCourseRecordSet searchTakeCoursesByCName(String cname) throws SQLException {
        return new TakeCourseRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME + " row WHERE row.cid IN ( SELECT c.cid FROM "
                +CourseManager.TABLE_NAME+" c WHERE c.cname LIKE \'%" + cname + "%\')"));
    }

    public static TakeCourseRecordSet searchTakeCoursesBySNameAndCName(String sname, String cname) throws SQLException {
        return new TakeCourseRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME + " row WHERE row.sid IN ( SELECT r.sid FROM "
                +StudentManager.TABLE_NAME+" r WHERE r.sname LIKE \'%" + sname + "%\') AND row.cid IN ( SELECT c.cid FROM "
                +CourseManager.TABLE_NAME+" c WHERE c.cname LIKE \'%" + cname + "%\')"));
    }

    public static TakeCourseRecordSet searchTakeCoursesBySNameAndCID(String sname, int cid) throws SQLException {
        return new TakeCourseRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME + " row WHERE row.cid LIKE \'%"+cid+"%\' AND row.sid IN ( SELECT s.sid FROM "
                +StudentManager.TABLE_NAME+" s WHERE s.sname LIKE \'%" + sname + "%\')"));
    }

    public static TakeCourseRecordSet searchTakeCoursesBySIDAndCName(long sid, String cname) throws SQLException {
        return new TakeCourseRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME + " row WHERE row.sid LIKE \'%"+sid+"%\' AND row.cid IN ( SELECT c.cid FROM "
                +CourseManager.TABLE_NAME+" c WHERE c.cname LIKE \'%" + cname + "%\')"));
    }

    public static TakeCourseRecordSet searchTakeCoursesBySIDAndCID(long sid, int cid) throws SQLException {
        return new TakeCourseRecordSet(Main.dbManager.execSQLWithResult("SELECT * FROM " + TABLE_NAME + " row WHERE row.sid LIKE \'%" + sid + "%\' AND row.cid LIKE \'%" + cid + "%\'"));
    }

    public static class TakeCourseRecordSet {

        private ResultSet rs;

        public TakeCourseRecordSet(ResultSet rs) {
            this.rs = rs;
        }

        public void close() throws SQLException {
            rs.getStatement().close();
            rs.close();
        }

        public boolean next() throws SQLException {
            return rs.next();
        }

        public long getSID() throws SQLException {
            return rs.getBigDecimal("sid").longValue();
        }

        public int getCID() throws SQLException {
            return rs.getInt("cid");
        }

        public int getYear() throws SQLException {
            return rs.getInt("year");
        }

        public int getScore() throws SQLException {
            return rs.getInt("score");
        }
    }
}
