package com.play;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * .
 * Created by yjy on 2017/8/17.
 */
class MyFrame extends JFrame {

    private TrayIcon trayIcon;//托盘图标
    private boolean closeFirst = true;
    private boolean hideFirst = true;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    MyFrame() {

        super("记键器");

        // 获得系统托盘的实例
        SystemTray systemTray = SystemTray.getSystemTray();
        setSize(600,300);
        setLayout(new GridLayout(4, 20));
        setLocationRelativeTo(null); // Frame在窗体居中
        //lable组件
        java.awt.Label label1 = new Label("记键器运行中...");
        label1.setFont(new Font(null, 0, 14));
        add(label1);

        java.awt.Label label2 = new Label("当前时间: 0000-00-00 00:00:00");
        label2.setFont(new Font(null, 0, 14));
        add(label2);


        java.awt.Label label3 = new Label("今日总键数: 刷新中...");
        label3.setFont(new Font(null, 0, 14));
        label3.setForeground(new Color(255, 0, 0));
        add(label3);

        new Thread(() -> {
            for (;;) {
                try {
                    label2.setText("当前时间: " + sdf.format(new Date()));
                    label3.setText("今日总键数: " + DataManager.count);
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "系统错误,请尝试重新启动", "提示",JOptionPane.WARNING_MESSAGE);
                    System.exit(0);
                }
            }
        }).start();

        pack();//调整窗口以容纳所有的组件
        setVisible(true);//显示窗口
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        try {
            trayIcon = new TrayIcon(ImageIO.read(new File("resource/timg.png")));
            PopupMenu popupMenu = new PopupMenu(); // 创建弹出菜单
            MenuItem exit = new MenuItem("退出"); // 创建菜单项
            //响应方法
            exit.addActionListener(e -> System.exit(0));

            popupMenu.add(exit); // 为弹出菜单添加菜单项
            trayIcon.setPopupMenu(popupMenu); // 为托盘图标加弹出菜弹
            try {
                systemTray.add(trayIcon); // 为系统托盘加托盘图标
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (IOException e1) {e1.printStackTrace();}

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
