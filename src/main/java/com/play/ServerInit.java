package com.play;

import com.play.base.Base;
import com.play.util.PropertyUtil;
import com.play.util.SQLiteUtil;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * 服务启动时的初始化内容
 * Created by yjy on 2017/8/18.
 */
class ServerInit extends Base {

    private static final Logger log = Logger.getLogger(ServerInit.class);

    private ServerInit() {}

    private static ServerInit instance;

    static ServerInit getInstance() {
        return instance == null ? (instance = new ServerInit()) : instance;
    }

    /**
     * 初始化信息
     * @throws IOException e
     */
    void initAll() throws IOException {
        initLog4j();
        initSqlite();
    }

    /**
     * 加载log4j配置
     * @throws IOException ioException
     */
    private void initLog4j() throws IOException{
        Properties properties = PropertyUtil.getInstance().getProperties(CONFIG_ROOT + "/log4j.properties");
        PropertyConfigurator.configure(properties);
    }

    /**
     * 初始化数据库连接信息
     */
    private void initSqlite() {
        Connection connection = null;
        Statement stat = null;
        ResultSet rs = null;
        try {
            connection = SQLiteUtil.openConnection();
            stat = connection.createStatement();
            // 检测表是否存在,如果不存在,则创建表
            String checkTableSql = "select * from sqlite_master where type = 'table' and name = 'TABLE_NAME'";
            String table = "key_record";
            rs = stat.executeQuery(checkTableSql.replace("TABLE_NAME", table));
            if (rs == null || !rs.next()) {
                System.out.println(table + " is not exist");
                String sql = "create table " + Constants.DATA_BASE + table + " (id integer primary key, user varchar(20), time datetime, beat_num integer);";
                stat.executeUpdate(sql);
            }
            table = "key_record_day";
            rs = stat.executeQuery(checkTableSql.replace("TABLE_NAME", table));
            if (rs == null || !rs.next()) {
                System.out.println(table + " is not exist");
                String sql = "create table " + Constants.DATA_BASE + table + " (id integer primary key, user varchar(20), date date, beat_num integer);";
                stat.executeUpdate(sql);
            }
        } catch (Exception e) {
            log.error("数据库初始化失败", e);
        } finally {
            SQLiteUtil.closeAll(connection, stat, rs);
        }
    }


}
