package com.yjy.keyListen.util;

import com.yjy.keyListen.base.BaseUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 配置文件工具类
 * Created by yjy on 2017/8/18.
 */
public class PropertyUtil extends BaseUtil {

    /************单例************************************/
    private PropertyUtil() {}

    private static PropertyUtil instance;

    public static PropertyUtil getInstance() {
        return instance == null ? (instance = new PropertyUtil()) : instance;
    }

    /**
     * 获取配置map
     * @param configFilePath 配置文件地址
     * @return map
     * @throws IOException e
     */
    public Map<String, String> getConfigMap(String configFilePath) throws IOException{
        Map<String, String> map = new HashMap<String, String>();
        Properties properties = getProperties(configFilePath);
        for (Object key : properties.keySet()) {
            map.put(key.toString(), properties.getProperty(key.toString()));
        }
        return map;
    }

    /**
     * 获取配置信息
     * @param configFilePath 配置文件地址
     * @return properties
     * @throws IOException e
     */
    public Properties getProperties(String configFilePath) throws IOException {
        FileInputStream fileInputStream = getFileInputStream(configFilePath);
        Properties properties = new Properties();
        properties.load(fileInputStream);
        return properties;
    }

    /**
     * 获取配置文件流
     * @param configFilePath 配置文件地址
     * @return fi
     * @throws FileNotFoundException e
     */
    public FileInputStream getFileInputStream(String configFilePath) throws FileNotFoundException {
        return new FileInputStream(configFilePath);
    }
}
