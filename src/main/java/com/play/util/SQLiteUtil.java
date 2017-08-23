package com.play.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import com.play.base.BaseUtil;
import org.apache.log4j.Logger;
import javax.sql.DataSource;
import java.sql.*;
import java.util.Properties;


/**
 * SQLite工具类 使用c3p0连接池
 * Created by yjy on 2017/8/22.
 */
public class SQLiteUtil extends BaseUtil {

    private static final Logger log = Logger.getLogger(SQLiteUtil.class);
    private static DataSource ds = new ComboPooledDataSource();

    static {
        try {
            Properties c3p0Properties = PropertyUtil.getInstance().getProperties(CONFIG_ROOT + "/c3p0.properties");
            Class.forName(c3p0Properties.getProperty("driverClass"));

            log.debug("c3p0Properties : " + c3p0Properties);
            //常规数据库连接属性
            Properties jdbcProperties = new Properties();
            //连接池配置属性
            Properties c3p0PooledProp = new Properties();
            for (Object key : c3p0Properties.keySet()) {
                String skey = (String) key;
                if (skey.startsWith("c3p0")) {
                    c3p0PooledProp.put(skey,c3p0Properties.getProperty(skey));
                } else {
                    jdbcProperties.put(skey,c3p0Properties.getProperty(skey));
                }
            }
            //建立连接池
            DataSource unPooled = DataSources.unpooledDataSource(c3p0Properties.getProperty("jdbcUrl"),jdbcProperties);
            ds = DataSources.pooledDataSource(unPooled,c3p0PooledProp);
        } catch (Exception e) {
            log.error("init c3p0 throw an error", e);
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
            log.error("openConnection throw an error", e);
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
