package com.play;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2017/8/17.
 */
public class FrameTest2 extends JFrame {

    private TrayIcon trayIcon;//托盘图标
    private SystemTray systemTray;//系统托盘

    public FrameTest2()
    {
        super("系统托盘图标");
        systemTray = SystemTray.getSystemTray();//获得系统托盘的实例
        setSize(150,150);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        try {
            trayIcon = new TrayIcon(ImageIO.read(new File("D:\\桌面-历史项目\\美图一堆堆\\1366137.gif")));
            PopupMenu popupMenu = new PopupMenu(); // 创建弹出菜单
            MenuItem exit = new MenuItem("退出"); // 创建菜单项
            //响应方法
            exit.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }

            });

            popupMenu.add(exit); // 为弹出菜单添加菜单项
            trayIcon.setPopupMenu(popupMenu); // 为托盘图标加弹出菜弹
            SystemTray systemTray = SystemTray.getSystemTray(); // 获得系统托盘对象
            try {
                systemTray.add(trayIcon); // 为系统托盘加托盘图标
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (IOException e1) {e1.printStackTrace();}

        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.addWindowListener(
                new WindowAdapter(){
                    public void windowIconified(WindowEvent e){
                        dispose();//窗口最小化时dispose该窗口
                        trayIcon.displayMessage("通知：", "程序最小化到系统托盘", TrayIcon.MessageType.INFO);
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

    public static void main(String args[])
    {
        new FrameTest2();
    }
}
