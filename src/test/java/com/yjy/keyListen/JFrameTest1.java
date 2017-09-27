package com.yjy.keyListen;

import com.yjy.keyListen.util.SQLiteUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2017/8/24.
 */
public class JFrameTest1 {

    public static void ManageView() {
        JFrame jframe = new JFrame("超市购物管理");
        JMenuBar jmb = new JMenuBar();
        JMenu menu_shoping = new JMenu("查看购买商品");
        JMenu menu_user = new JMenu("会员管理");
        JMenu menu_store = new JMenu("仓库管理");
        JMenu menu_exit = new JMenu("退出");
        Object data[][] = getData();
        String columnNames[] = {"编号", "姓名", "性别"};
        JTable table = new JTable(data, columnNames);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane pane = new JScrollPane(table);
        Container contentPane = jframe.getContentPane();
        contentPane.setLayout(new FlowLayout());
//        JPanel jp=new JPanel();
//        jp.setLayout(new FlowLayout());
        JButton search = new JButton("查找");
        JButton add = new JButton("增加");
        JButton delete = new JButton("删除");
        JButton update = new JButton("修改");
        contentPane.add(search);
        contentPane.add(add);
        contentPane.add(delete);
        contentPane.add(update);

        jmb.add(menu_shoping);
        jmb.add(menu_user);
        jmb.add(menu_store);
        jmb.add(menu_exit);
        jframe.setJMenuBar(jmb);
        jframe.add(pane, BorderLayout.NORTH);
        //jframe.add(contentPane,BorderLayout.SOUTH);
        jframe.setSize(600, 500);
        jframe.setLocation(300, 200);
        jframe.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        jframe.setVisible(true);

    }

    public static void main(String[] args) {
        ManageView();
    }

    private static  Object[][] getData() {
        java.util.List<Object[]> list = new CopyOnWriteArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        try {
            connection = SQLiteUtil.openConnection();
            statement = connection.createStatement();
            rs = statement.executeQuery("select * from key_record_day");
            while (rs.next()) {
                list.add(new Object[]{rs.getInt("id"), rs.getDate("date"), rs.getInt("beat_num")});
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SQLiteUtil.closeAll(connection, statement, rs);
        }
        Object[][] data = new Object[list.size()][3];
        for (int i = 0; i < list.size(); i++) {
            data[i] = list.get(i);
        }
        return data;
    }
}
