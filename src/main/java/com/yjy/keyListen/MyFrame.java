package com.yjy.keyListen;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
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
    private static JTable table;
    static DefaultTableModel model;

    MyFrame() {

        super("记键器");
        // 设置显示Frame
        this.setVisible(true);
        // 设置Frame位置和大小
        this.setBounds(100,100,200,100);
        // 设置关闭Frame事件
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        // 设置Frame在窗体居中
        this.setLocationRelativeTo(null);

        // 新建一个表格
        table = new JTable();
        // 建立表格模型
        model = new DefaultTableModel();
        // 初始化表格数据
        Vector<String> columns = new Vector<>();
        columns.add("日期");
        columns.add("键数");
        model.setDataVector(null, columns);
        // 设置表格模型
        table.setModel(model);
        // 设置表格不可编辑
        table.setEnabled(false);
        // 设置表格内容居中
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, r);
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        this.getContentPane().add(scrollPane, BorderLayout.CENTER);
        // 调整窗口以容纳所有的组件
        this.pack();
        // 处理注册窗口事件
        handleSystemTray();
    }

    /**
     * 1.数据初始化
     * 2.启动数据更新线程
     */
    void dataWorker(Vector<Vector> data) {
        if (data != null && data.size() > 0) {
            data.stream().forEachOrdered(model::addRow);
        }
        // 开启更新数据线程
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
