package com.play.util;

import com.play.base.BaseUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;

import java.io.FileInputStream;

/**
 * Mybatis工具类
 */
public class MyBatisUtil extends BaseUtil {

    /************单例************************************/
    private MyBatisUtil() {}

    private static MyBatisUtil instance;

    public static MyBatisUtil getInstance() {
        return instance == null ? (instance = new MyBatisUtil()) : instance;
    }

    private static final Logger log = Logger.getLogger(MyBatisUtil.class);

    private static SqlSessionFactory sqlSessionFactory;

    // 创建sqlSessionFactory
    static{
        try {
            SqlSessionFactoryBuilder FactoryBuilder = new SqlSessionFactoryBuilder();
            FileInputStream fi = PropertyUtil.getInstance().getFileInputStream(CONFIG_ROOT + "/mybatisConf.xml");
            sqlSessionFactory = FactoryBuilder.build(fi);
        } catch (Exception e) {
            log.error("加载配置文件出错", e);
        }
    }

    /**
     * 获取session会话
     * @param autoCommit 是否自动提交
     * @return session
     */
    public SqlSession getSession(boolean autoCommit) {
          return sqlSessionFactory.openSession(autoCommit);
    }



}
