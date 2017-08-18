package com.play;

import com.play.base.Base;
import com.play.util.PropertyUtil;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.Properties;

/**
 * 服务启动时的初始化内容
 * Created by yjy on 2017/8/18.
 */
public class ServerInit extends Base {

    private ServerInit() {
//        throw new UnsupportedOperationException("There has no instance for you!");
    }

    private static ServerInit instance;

    public static ServerInit getInstance() {
        return instance == null ? (instance = new ServerInit()) : instance;
    }

    /**
     * 加载log4j配置
     * @throws IOException ioException
     */
    void initLog4j() throws IOException{
        Properties properties = PropertyUtil.getInstance().getProperties(CONFIG_ROOT + "/log4j.properties");
        PropertyConfigurator.configure(properties);
    }


}
