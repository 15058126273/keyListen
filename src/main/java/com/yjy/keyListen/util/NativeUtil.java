package com.yjy.keyListen.util;

import com.yjy.keyListen.base.BaseUtil;

/**
 * 本地工具方法
 * Created by yjy on 2017/8/18.
 */
public class NativeUtil extends BaseUtil {

    /************单例************************************/
    private NativeUtil() {}

    private static NativeUtil instance;

    public static NativeUtil getInstance() {
        return instance == null ? (instance = new NativeUtil()) : instance;
    }


}
