package com.play.util;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.FileInputStream;

/**
 * MyBatis工具类
 *
 * @author lee
 *         2015-2-6
 */
public class MyBatisUtil {

    private static SqlSessionFactory sqlSessionFactory;

    /**
     * 数据库会话
     */
    private SqlSession sqlSession;

    private static MyBatisUtil instance = new MyBatisUtil();

    private MyBatisUtil() {}

    public static MyBatisUtil getInstance() {
        return instance;
    }

    /**
     * 加载 配置文件
     */
    static{
        try {
            SqlSessionFactoryBuilder FactoryBuilder = new SqlSessionFactoryBuilder();

            FileInputStream fi = getMyBatisPro();

            sqlSessionFactory = FactoryBuilder.build(fi);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 取得会话
     *
     * @param autoCommit
     * @return
     */
    public SqlSession getSession(boolean autoCommit) {
          return sqlSessionFactory.openSession(autoCommit);
    }

    /**
     * 获取MyBatis配置
     *
     * @return
     * @throws Exception
     */
    public static FileInputStream getMyBatisPro() throws Exception {
        FileInputStream fi = new FileInputStream("config/mybatisConf.xml");
        return fi ;
    }

}
