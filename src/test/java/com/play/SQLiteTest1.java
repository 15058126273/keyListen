package com.play;


import java.sql.*;

/**
 * SQLite测试
 * Created by yjy on 2017/8/18.
 */
public class SQLiteTest1 {

    public static void main(String[] args)
    {
        try
        {
            // 连接SQLite的JDBC
            Class.forName("org.sqlite.JDBC");
            // 建立一个数据库名key_record.db的连接，如果不存在就在当前目录下创建之
            Connection conn = DriverManager.getConnection("jdbc:sqlite:key_record.db");
            Statement stat = conn.createStatement();
//            stat.executeUpdate("create table key_record.key_record(id INTEGER PRIMARY KEY, user varchar(20), time datetime, beat_num integer);");
            ResultSet rs = stat.executeQuery("select * from sqlite_master where type = 'table' and name ='key_record'");
            while (rs.next()) {
                System.out.println("name = " + rs.getString("name") );
            }

//            stat.executeUpdate("drop table tbl1 if exist");
//            stat.executeUpdate("create table tbl1(name varchar(20), salary int);");//创建一个表，两列
//            stat.executeUpdate("insert into tbl1 values('ZhangSan',8000);");//插入数据
//            stat.executeUpdate("insert into tbl1 values('LiSi',7800);");
//            stat.executeUpdate("insert into tbl1 values('WangWu',5800);");
//            stat.executeUpdate("insert into tbl1 values('ZhaoLiu',9100);");
//            ResultSet rs = stat.executeQuery("select * from tbl1;");//查询数据
//            while(rs.next()){//将查询到的数据打印出来
//                System.out.print("name = "+ rs.getString("name")+" ");//列属性一
//                System.out.println("salary = "+ rs.getString("salary"));//列属性二
//            }
//            rs.close();



            stat.close();
            conn.close();//结束数据库的连接
        }
        catch(Exception e )
        {
            e.printStackTrace();
        }
    }
}
