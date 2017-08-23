package com.play;

import com.play.base.Base;
import com.play.dao.impl.KeyRecordDaoImpl;
import com.play.dao.impl.KeyRecordDayDaoImpl;
import com.play.entity.KeyRecord;
import com.play.entity.KeyRecordDay;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 程序数据处理
 * Created by yjy on 2017/8/18.
 */
class DataManager extends Base {

    private static final Logger log = Logger.getLogger(DataManager.class);
    private static String currentTime = "";
    private static int currentCount = 0;
    static int count = 0;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    DataManager() {
        new Thread(() -> {
            for (;;) {
                try {
                    String now = simpleDateFormat.format(new Date());
                    if (!currentTime.equals(now)) {
                        // 更新当日数据
                        int today = addBeat(count - currentCount);
                        if (today != count) {
                            count = today;
                        }
                        currentCount = count;
                        currentTime = now;
                    }
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    log.error("记录敲键数出错了",e);
                }
            }
        }).start();
    }

    /**
     * 更新键数
     * @param addNum 新增键数
     */
    private static int addBeat(int addNum) {
        KeyRecord keyRecord = new KeyRecord();
        keyRecord.setBeatNum(addNum);
        keyRecord.setTime(new Date());
        keyRecord.setUser(HOST_ADDRESS);
        KeyRecordDaoImpl.keyRecordDao.add(keyRecord);
        // 更新日记录
        return addToDayRecord(addNum);
    }

    /**
     * 更新日记录
     * @param addNum 新增键数
     * @return 今日总键数
     */
    private static int addToDayRecord(int addNum) {
        KeyRecordDayDaoImpl keyRecordDayDao = KeyRecordDayDaoImpl.keyRecordDayDao;
        KeyRecordDay keyRecordDay = new KeyRecordDay(HOST_ADDRESS);
        keyRecordDay.setDate(new Date());
        keyRecordDay = keyRecordDayDao.getObject(keyRecordDay);
        if (keyRecordDay == null) {
            keyRecordDay = new KeyRecordDay(HOST_ADDRESS);
            keyRecordDay.setDate(new Date());
            keyRecordDay.setBeatNum(addNum);
            keyRecordDayDao.add(keyRecordDay);
        } else {
            keyRecordDay.setBeatNum(keyRecordDay.getBeatNum() + addNum);
            keyRecordDayDao.update(keyRecordDay);
        }
        return keyRecordDay.getBeatNum();
    }
}
