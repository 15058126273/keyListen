package com.play.base;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 项目内基类
 * Created by yjy on 2017/8/18.
 */
public class Base {

    protected static final String PROJECT_NAME = "keyRecord";

    protected static final String CONFIG_ROOT = "config";

    protected static String HOST_ADDRESS = "未知";

    static {
        try {
            // 获取本机host
            HOST_ADDRESS = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


}
