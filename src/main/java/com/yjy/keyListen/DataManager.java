package com.yjy.keyListen;

import com.yjy.keyListen.base.Base;
import com.yjy.keyListen.dao.impl.KeyRecordDaoImpl;
import com.yjy.keyListen.dao.impl.KeyRecordDayDaoImpl;
import com.yjy.keyListen.entity.KeyRecord;
import com.yjy.keyListen.entity.KeyRecordDay;
import com.yjy.keyListen.util.PropertyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 程序数据处理
 * Created by yjy on 2017/8/18.
 */
class DataManager extends Base {

    private static final Logger log = LoggerFactory.getLogger(DataManager.class);

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

    // 当前时间 (分)
    private transient static String currentTime;
    // 当前日期 (日)
    private transient static String currentDate;
    // 当日总键数
    transient static int count = 0;

    DataManager() {
        new Thread(() -> {
            Calendar calendar = new GregorianCalendar();
            try {
                // 首次启动
                if (currentTime == null) {
                    Date date = new Date();
                    count = addBeat(0, date);
                    currentDate = simpleDateFormat1.format(date);
                    calendar.setTime(new Date());
                    calendar.add(Calendar.MINUTE, -1);
                    date = calendar.getTime();
                    currentTime = simpleDateFormat.format(date);
                }
                log.debug("首次启动成功...count: " + count + ", currentTime : " + currentTime + ", currentDate : " + currentDate);
                // 通知Frame初始化数据
                Main.getFrame().dataWorker(findByDate());
                log.debug("通知Frame初始化数据成功...count: " + count + ", currentTime : " + currentTime + ", currentDate : " + currentDate);
            } catch (Exception e) {
                log.error("init DataManager throw an error", e);
                throw new RuntimeException(e);
            }

            for (;;) {
                try {
                    calendar.setTime(new Date());
                    calendar.add(Calendar.MINUTE, -1);
                    Date date = calendar.getTime();
                    String time = simpleDateFormat.format(date);
                    if (!time.equals(currentTime)) {
                        currentTime = time;
                        String thisDay = simpleDateFormat1.format(date);
                        if (currentDate.equals(thisDay)) {
                            log.debug("更新数据前...count: " + count + ", currentTime : " + currentTime + ", currentDate : " + currentDate);
                            count = addBeat(count, date);
                            log.debug("更新数据成功...count: " + count + ", currentTime : " + currentTime + ", currentDate : " + currentDate);
                        } else {
                            log.debug("日期不同步, 更新取消...currentDate : " + currentDate + ", thisDay : " + thisDay + ", count : " + count);
                        }

                        String nowDay = simpleDateFormat1.format(new Date());
                        if (!currentDate.equals(nowDay)) {
                            log.debug("更新日期..currentDate : " + currentDate + ", nowDay : " + nowDay);
                            count = 0;
                            currentDate = nowDay;
                            addRowForNewDay(nowDay, count);
                        }
                        log.debug("刷新成功...count: " + count + ", currentTime : " + currentTime + ", currentDate : " + currentDate);
                    }
                    Thread.sleep(1000L);
                } catch (Exception e) {
                    log.error("记录敲键数出错了",e);
                }
            }
        }).start();
    }

    /**
     * 表格中插入一行数据
     * @param day 日期
     */
    private void addRowForNewDay(String day, int count) {
        Vector vector = new Vector();
        vector.add(day);
        vector.add(count);
        DefaultTableModel defaultTableModel = MyFrame.model;
        defaultTableModel.insertRow(0, vector);
    }

    /**
     * 更新键数
     * @param count 当前总键数
     * @param now 时间
     * @return 当期总键数
     */
    private static int addBeat(int count, Date now) {
        int[] counts = addToDayRecord(count, now);
        log.debug("当日总键数更新成功...addCount : " + counts[1] + ", count : " + counts[0]);
        KeyRecord keyRecord = new KeyRecord();
        keyRecord.setBeatNum(counts[1]);
        keyRecord.setTime(now);
        keyRecord.setUser(HOST_ADDRESS);
        KeyRecordDaoImpl.keyRecordDao.save(keyRecord);
        // 更新日记录
        return counts[0];
    }

    /**
     * 更新日记录
     * @param count 总键数
     * @param now 当前时间
     * @return [当日总键数, 当前新增键数]
     */
    private static int[] addToDayRecord(int count, Date now) {
        int originCount = 0;
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
            originCount = keyRecordDay.getBeatNum();
            if (count != 0) {
                keyRecordDay.setBeatNum(count);
                keyRecordDayDao.update(keyRecordDay);
            }
        }
        int nowCount = keyRecordDay.getBeatNum();
        int addCount = nowCount - originCount;
        addCount = addCount < 0 ? 0 : addCount;
        return new int[]{nowCount, addCount};
    }

    /**
     * 获取前10天的数据
     * @return 每天的总键数
     */
    private static Vector<Vector> findByDate() throws Exception {
        Properties properties = PropertyUtil.getInstance().getProperties(CONFIG_ROOT + "conf.properties");
        Integer showRows = Integer.parseInt(properties.getProperty("showRows"));
        Vector<Vector> data = new Vector<>();
        KeyRecordDayDaoImpl keyRecordDayDao = KeyRecordDayDaoImpl.keyRecordDayDao;
        List<KeyRecordDay> list = keyRecordDayDao.findPage(1,showRows);
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

}
