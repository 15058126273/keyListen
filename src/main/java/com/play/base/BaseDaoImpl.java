package com.play.base;

import com.play.util.SQLiteUtil;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Dao层默认实现
 * Created by yjy on 2017/8/22.
 */
public class BaseDaoImpl<T> implements BaseDao<T> {

    private static final Logger log = Logger.getLogger(BaseDaoImpl.class);

    public boolean save(T t) {
        return false;
    }

    public boolean delete(int id) {
        return false;
    }

    public boolean update(T t) {
        return false;
    }

    public T findById(int id) {
        return null;
    }

    public T findByT(T t) {
        return null;
    }

    public List<T> findListByT(T t) {
        return null;
    }

    public List<T> findAll() {
        return null;
    }

    public List<T> findPage(int pageNo, int pageSize) {
        return null;
    }

    /**
     * 执行增删改
     * @param sql sql
     * @param parameters 参数
     * @return 影响条数
     */
    protected static int execute(String sql, Object[] parameters){
        Connection con = SQLiteUtil.openConnection();
        int count = 0;
        if (con != null) {
            try {
                PreparedStatement ps = con.prepareStatement(sql);
                for (int i = 1; i <= parameters.length; i++) {
                    ps.setObject(i, parameters[i - 1]);
                }
                count = ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                SQLiteUtil.closeAll(con, null, null);
            }
        }
        return count;
    }

    /**
     * 执行查询，并将值反射到bean
     * @param sql sql
     * @param parameters 参数
     * @param clazz 反射类
     * @return list
     */
    protected static <T> List<T> select(String sql, Object[] parameters, Class<T> clazz) {
        List<T> list = new ArrayList<T>();
        Connection conn = SQLiteUtil.openConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = conn.prepareStatement(sql);
            if(parameters != null){
                for (int i = 1; i <= parameters.length; i++) {
                    ps.setObject(i, parameters[i - 1]);
                }
            }
            // 执行查询方法
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            List<String> columnList = new ArrayList<String>();
            for (int i = 0; i < rsmd.getColumnCount(); i++) {
                columnList.add(rsmd.getColumnName(i + 1));
            }
            // 循环遍历记录
            while (rs.next()) {
                // 创建封装记录的对象
                T obj = clazz.newInstance();
                // 遍历一个记录中的所有列
                for (int i = 0; i < columnList.size(); i++) {
                    // 获取列名
                    String column = columnList.get(i);
                    // 根据列名创建set方法(将数据库字段中的'_',替换掉)
                    String setMethod = "set" + column.replace("_", "");
                    // 获取clazz中所有方法对应的Method对象
                    Method[] ms = clazz.getMethods();
                    // 循环遍历ms
                    for (int j = 0; j < ms.length; j++) {
                        // 获取每一个method对象
                        Method m = ms[j];
                        // 判断m中对应的方法名和数据库中列名创建的set方法名是否形同
                        if (m.getName().equalsIgnoreCase(setMethod)) {
                            // 反调set方法封装数据
                            Object o = rs.getObject(column);
                            try {
                                m.invoke(obj, o);// 获取rs中对应的值，封装到obj中
                                log.debug("map column success > column : " + column);
                                break;
                            } catch (Exception e){
                                log.debug("setMethod arguments is illegal, illegal column : " + column + ", type : " + o.getClass().getName());
                            }
                        }
                    }
                }
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SQLiteUtil.closeAll(conn, ps, rs);
        }
        return list;
    }
}
