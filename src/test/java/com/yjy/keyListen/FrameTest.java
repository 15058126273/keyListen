package com.yjy.keyListen;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.sun.deploy.uitoolkit.ToolkitStore.dispose;

/**
 * Created by Administrator on 2017/8/17.
 */
public class FrameTest {

    public static void main(String[] args) {
        createFrame();
    }

    private static void createFrame() {
        Frame frame = new Frame("记键器");
        frame.setSize(600,300);
        frame.setLocation(500, 500);
        frame.setLayout(new GridLayout(5,5));
        //lable组件
        Label label = new Label("记键器运行中...");
        label.setBackground(new Color(100,100,100));
        label.setForeground(new Color(255, 255, 255));
        frame.add(label);

        TextField textField = new TextField("今日总键数:", 10);
        textField.setText("0");
        frame.add(textField);
        frame.pack();//调整窗口以容纳所有的组件
        frame.setVisible(true);//显示窗口
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }
}
