package com.play.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.play.Constants;

import java.sql.*;


/**
 * SQLite工具类 使用c3p0连接池
 * Created by yjy on 2017/8/22.
 */
public class SQLiteUtil {

    private static ComboPooledDataSource ds = new ComboPooledDataSource();

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            ds.setJdbcUrl("jdbc:sqlite:" + Constants.DATA_BASE_NAME + ".db");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 打开数据库连接
     */
    public static Connection openConnection() {
        Connection conn = null;
        try {
            conn = ds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭数据库
     */
    public static void closeAll(Connection con, Statement stmt, ResultSet rs) {
        try {
            if (con != null) {
                con.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
