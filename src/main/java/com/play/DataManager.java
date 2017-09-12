package com.play;

import com.play.base.Base;
import com.play.dao.impl.KeyRecordDaoImpl;
import com.play.dao.impl.KeyRecordDayDaoImpl;
import com.play.entity.KeyRecord;
import com.play.entity.KeyRecordDay;
import org.apache.log4j.Logger;

import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 程序数据处理
 * Created by yjy on 2017/8/18.
 */
class DataManager extends Base {

    private static final Logger log = Logger.getLogger(DataManager.class);
    private static String currentTime = "";
    private static String currentDate = "";
    private static int currentCount = 0;
    static int count = 0;
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

    DataManager() {
        new Thread(() -> {
            for (;;) {
                try {
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(new Date());
                    calendar.add(Calendar.MINUTE, -1);
                    Date date = calendar.getTime();
                    String time = simpleDateFormat.format(date);
                    if (!currentTime.equals(time)) {
                        int addNum = count - currentCount;

                        // 更新当日数据
                        count = addBeat(count, addNum, date);
                        currentCount = count;
                        currentTime = time;

                        String nowDay = simpleDateFormat1.format(new Date());
                        if (!currentDate.equals(nowDay)) {
                            // 如果是新的一天,则插入一行数据
                            if (!"".equals(currentDate) && MyFrame.table != null) {
                                Vector vector = new Vector();
                                vector.add(nowDay);
                                vector.add(0);
                                DefaultTableModel defaultTableModel = (DefaultTableModel)MyFrame.table.getModel();
                                defaultTableModel.insertRow(0, vector);
                                count = 0;
                                currentCount = 0;
                            }
                            if ("".equals(currentDate)) Main.getFrame().dataWorker();
                            currentDate = nowDay;
                        }
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
     * @param count 当前总键数
     * @param addNum 新增键数
     * @param now 时间
     */
    private static int addBeat(int count, int addNum, Date now) {
        KeyRecord keyRecord = new KeyRecord();
        keyRecord.setBeatNum(addNum);
        keyRecord.setTime(now);
        keyRecord.setUser(HOST_ADDRESS);
        KeyRecordDaoImpl.keyRecordDao.save(keyRecord);
        // 更新日记录
        return addToDayRecord(count, now);
    }

    /**
     * 更新日记录
     * @param count 总键数
     * @param now 当前时间
     */
    private static int addToDayRecord(int count, Date now) {
        KeyRecordDayDaoImpl keyRecordDayDao = KeyRecordDayDaoImpl.keyRecordDayDao;
        KeyRecordDay keyRecordDay = new KeyRecordDay(HOST_ADDRESS);
        keyRecordDay.setDate(now);
        keyRecordDay = keyRecordDayDao.findByT(keyRecordDay);
        if (keyRecordDay == null) {
            keyRecordDay = new KeyRecordDay(HOST_ADDRESS);
            keyRecordDay.setDate(now);
            keyRecordDay.setBeatNum(count);
            keyRecordDayDao.save(keyRecordDay);
        } else {
            if (count != 0) {
                keyRecordDay.setBeatNum(count);
                keyRecordDayDao.update(keyRecordDay);
            }
        }
        return keyRecordDay.getBeatNum();
    }

    static Vector<Vector> findByDate() {
        Vector<Vector> data = new Vector<>();
        KeyRecordDayDaoImpl keyRecordDayDao = KeyRecordDayDaoImpl.keyRecordDayDao;
        List<KeyRecordDay> list = keyRecordDayDao.findPage(1,10);
        if (list != null) {
            for (KeyRecordDay keyRecordDay : list) {
                Vector<Object> vector = new Vector<>();
                vector.add(keyRecordDay.getDateString());
                vector.add(keyRecordDay.getBeatNum());
                data.add(vector);
            }
        }
        return data;
    }

    static String getCurrentDate() {
        return currentDate;
    }
}
