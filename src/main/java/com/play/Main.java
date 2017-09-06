package com.play;

import com.play.base.Base;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Timer;

/**
 * 主程序启动类
 * Created by yjy on 2017/8/18.
 */
public class Main extends Base {

    private static final Logger log = Logger.getLogger(Main.class);
    private static MyFrame frame;
    private static KeyHook keyHook;

    /**
     * 主程序入口
     * @param args ..
     */
    public static void main(String[] args) throws IOException {

        // 初始化服务
        ServerInit.getInstance().initAll();
        log.info("init success....");

        // 启动更新数据线程
        new DataManager();
        log.info("start dataManager success....");

        // 开启程序窗口
        frame = new MyFrame();
        log.info("start frame success....");

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
