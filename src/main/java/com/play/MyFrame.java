package com.play;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Vector;

/**
 * .
 * Created by yjy on 2017/8/17.
 */
class MyFrame extends JFrame {

    private TrayIcon trayIcon;//托盘图标
    private boolean closeFirst = true;
    private boolean hideFirst = true;
    static JTable table;

    MyFrame() {

        super("记键器");
        this.setVisible(true);//显示窗口
        this.setBounds(100,100,200,100);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setLocationRelativeTo(null); // Frame在窗体居中

        Vector columns = new Vector();
        columns.add("日期");
        columns.add("键数");
        table = new JTable(null, columns);
        JScrollPane scrollPane = new JScrollPane(table);
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        this.pack();//调整窗口以容纳所有的组件

        // 延迟载入数据
        for (;"".equals(DataManager.getCurrentDate());) {}
        Vector<Vector> data = DataManager.findByDate();
        if (data != null && data.size() > 0) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            data.stream().forEachOrdered(model::addRow);
        }

        new Thread(() -> {
            for (;;) {
                try {
                    Thread.sleep(1000L);
                    table.setValueAt(DataManager.count, 0, 1);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "系统错误,请尝试重新启动", "提示",JOptionPane.WARNING_MESSAGE);
                    System.exit(0);
                }
            }
        }).start();

        // 处理注册窗口事件
        handleSystemTray();
    }

    private void handleSystemTray() {
        // 获得系统托盘的实例
        SystemTray systemTray = SystemTray.getSystemTray();
        try {
            trayIcon = new TrayIcon(ImageIO.read(new File("resource/timg.png")));
            PopupMenu popupMenu = new PopupMenu(); // 创建弹出菜单
            MenuItem exit = new MenuItem("退出"); // 创建菜单项
            //响应方法
            exit.addActionListener(e -> System.exit(0));
            popupMenu.add(exit); // 为弹出菜单添加菜单项
            trayIcon.setPopupMenu(popupMenu); // 为托盘图标加弹出菜弹
            systemTray.add(trayIcon); // 为系统托盘加托盘图标
        }
        catch (Exception e1) {e1.printStackTrace();}

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (closeFirst) {
                    closeFirst = false;
                    trayIcon.displayMessage("通知：", "程序最小化到系统托盘", TrayIcon.MessageType.INFO);
                }
            }
        });

        addWindowListener(
                new WindowAdapter(){

                    public void windowIconified(WindowEvent e){
                        dispose();//窗口最小化时dispose该窗口
                        if (hideFirst) {
                            hideFirst = false;
                            trayIcon.displayMessage("通知：", "程序最小化到系统托盘", TrayIcon.MessageType.INFO);
                        }
                    }
                });

        trayIcon.addMouseListener(
                new MouseAdapter(){
                    public void mouseClicked(MouseEvent e){
                        if(e.getClickCount() == 2)//双击托盘窗口再现
                            setExtendedState(Frame.NORMAL);
                        setVisible(true);
                    }
                });

    }

}
