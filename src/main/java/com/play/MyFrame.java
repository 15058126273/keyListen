package com.play;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * .
 * Created by yjy on 2017/8/17.
 */
public class MyFrame extends JFrame {

    private TrayIcon trayIcon;//托盘图标
    private SystemTray systemTray;//系统托盘
    private Label label; //
    private boolean closeFirst = true;
    private boolean hideFirst = true;

    public MyFrame()
    {
        super("记键器");
        systemTray = SystemTray.getSystemTray();//获得系统托盘的实例
        setSize(600,300);
        setLayout(new GridLayout(4, 20));
        setLocationRelativeTo(null); // Frame在窗体居中
        //lable组件
        java.awt.Label label1 = new Label("记键器运行中...每分钟更新键数...");
        label1.setFont(new Font(null, 0, 14));
        add(label1);

        java.awt.Label label = new Label("今日总键数: 0");
        label.setFont(new Font(null, 0, 14));
        label.setForeground(new Color(255, 0, 0));
        this.label = label;
        add(label);
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

        this.addWindowListener(
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



    public java.awt.Label getLabel() {
        return label;
    }

    public void setLabel(java.awt.Label label) {
        label = label;
    }
}
