package com.play;

import com.play.entity.KeyRecord;
import com.play.entity.KeyRecordDay;
import com.play.mapper.KeyRecordDayMapper;
import com.play.mapper.KeyRecordMapper;
import com.play.util.MyBatisUtil;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HMODULE;
import com.sun.jna.platform.win32.WinDef.LRESULT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinUser.HHOOK;
import com.sun.jna.platform.win32.WinUser.KBDLLHOOKSTRUCT;
import com.sun.jna.platform.win32.WinUser.LowLevelKeyboardProc;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/** Sample implementation of a low-level keyboard hook on W32. */
public class KeyHook {

    private static final Logger log = Logger.getLogger(KeyHook.class);
    private static String currentTime = "";
    private static int currentCount = 0;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static int count = 0;
    private static String user = "未知";
    private static HHOOK hhk;
    private static LowLevelKeyboardProc keyboardHook;

    static {
        try {
            // 初始化log4j
            Properties properties = new Properties();
            FileInputStream inputStream = new FileInputStream("config/log4j.properties");
            properties.load(inputStream);
            inputStream.close();
            PropertyConfigurator.configure(properties);

            // 获取本机host
            user = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            log.error("初始化出错了", e);
        }
    }

    /**
     * 主程序入口
     * @param args ..
     */
    public static void main(String[] args) {
        // 开启程序窗口
        MyFrame myFrame = new MyFrame();
        // 启动监听键盘线程
        final User32 lib = User32.INSTANCE;
        HMODULE hMod = Kernel32.INSTANCE.GetModuleHandle(null);
        keyboardHook = (nCode, wParam, info) -> {
            if (wParam.intValue() == 256) {
                count++;
            }
            Pointer ptr = info.getPointer();
            long peer = Pointer.nativeValue(ptr);
            return lib.CallNextHookEx(hhk, nCode, wParam, new LPARAM(peer));
        };
        hhk = lib.SetWindowsHookEx(WinUser.WH_KEYBOARD_LL, keyboardHook, hMod, 0);

        // 启动更新数据线程
        new Thread(() -> {
            for (;;) {
                try {
                    String now = simpleDateFormat.format(new Date());
                    if (!currentTime.equals(now)) {
                        int addNum = count - currentCount;
                        currentCount = count;
                        currentTime = now;
                        int today = addBeat(addNum);
                        myFrame.getLabel().setText("今日总键数: " + today);
                    }
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    log.error("记录敲键数出错了",e);
                }
            }
        }).start();

        // This bit never returns from GetMessage

        WinUser.MSG msg = new WinUser.MSG();
        lib.GetMessage(msg, null, 0, 0);
//        int result;
//        while ((result = lib.GetMessage(msg, null, 0, 0)) != 0) {
//            System.out.println("3");
//            if (result == -1) {
//                System.err.println("error in get message");
//                break;
//            }
//            else {
//                System.err.println("got message");
//                lib.TranslateMessage(msg);
//                lib.DispatchMessage(msg);
//            }
//        }
//        System.out.println("4");
//        lib.UnhookWindowsHookEx(hhk);
//        System.out.println("5");
    }

    /**
     * 更新键数
     * @param addNum 新增键数
     */
    private static int addBeat(int addNum) {
        SqlSession session = MyBatisUtil.getInstance().getSession(false);
        KeyRecordMapper keyRecordMapper = session.getMapper(KeyRecordMapper.class);
        KeyRecord keyRecord = new KeyRecord();
        keyRecord.setBeatNum(addNum);
        keyRecord.setTime(new Date());
        keyRecord.setUser(user);
        keyRecordMapper.save(keyRecord);

        // 更新日记录
        int today = addToDayRecord(session, addNum);
        // 提交事务
        session.commit();
        session.close();
        return today;
    }

    /**
     * 更新日记录
     * @param session 事务
     * @param addNum 新增键数
     * @return 今日总键数
     */
    private static int addToDayRecord(SqlSession session, int addNum) {
        KeyRecordDayMapper keyRecordDayMapper = session.getMapper(KeyRecordDayMapper.class);
        KeyRecordDay keyRecordDay = new KeyRecordDay(user);
        keyRecordDay.setDate(new Date());
        List<KeyRecordDay> list = keyRecordDayMapper.findByT(keyRecordDay);
        if (list == null || list.isEmpty()) {
            keyRecordDay.setBeatNum(addNum);
            keyRecordDayMapper.save(keyRecordDay);
        } else {
            keyRecordDay = list.get(0);
            keyRecordDay.setBeatNum(keyRecordDay.getBeatNum() + addNum);
            keyRecordDayMapper.update(keyRecordDay);
        }
        return keyRecordDay.getBeatNum();
    }
}