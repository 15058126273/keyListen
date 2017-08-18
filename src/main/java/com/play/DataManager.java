package com.play;

import com.play.base.Base;
import com.play.entity.KeyRecord;
import com.play.entity.KeyRecordDay;
import com.play.mapper.KeyRecordDayMapper;
import com.play.mapper.KeyRecordMapper;
import com.play.util.MyBatisUtil;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        SqlSession session = MyBatisUtil.getInstance().getSession(false);
        KeyRecordMapper keyRecordMapper = session.getMapper(KeyRecordMapper.class);
        KeyRecord keyRecord = new KeyRecord();
        keyRecord.setBeatNum(addNum);
        keyRecord.setTime(new Date());
        keyRecord.setUser(HOST_ADDRESS);
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
        KeyRecordDay keyRecordDay = new KeyRecordDay(HOST_ADDRESS);
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
