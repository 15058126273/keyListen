package com.play;

import com.play.base.Base;

import java.io.IOException;

/**
 * 主程序启动类
 * Created by yjy on 2017/8/18.
 */
public class Main extends Base {

    private static MyFrame frame;
    private static KeyHook keyHook;

    /**
     * 主程序入口
     * @param args ..
     */
    public static void main(String[] args) throws IOException {

        // 初始化log4j
        ServerInit.getInstance().initLog4j();
        System.out.println("initLog4j success....");

        // 开启程序窗口
        frame = new MyFrame();
        System.out.println("start frame success....");

        // 启动更新数据线程
        new DataManager();
        System.out.println("start dataManager success....");

        // 启动监听键盘线程
        keyHook = new KeyHook();

    }


    static MyFrame getFrame() {
        return frame;
    }

    static KeyHook getKeyHook() {
        return keyHook;
    }


}
