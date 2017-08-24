package com.play.dao.impl;

import com.play.Constants;
import com.play.base.BaseDaoImpl;
import com.play.dao.KeyRecordDao;
import com.play.entity.KeyRecord;

import java.util.List;

/**
 * keyRecordDao 记录每一分钟的按键数
 * Created by yjy on 2017/8/22.
 */
public class KeyRecordDaoImpl extends BaseDaoImpl<KeyRecord> implements KeyRecordDao {

    private static final String TABLE = Constants.DATA_BASE + "key_record";
    public static final KeyRecordDaoImpl keyRecordDao = new KeyRecordDaoImpl();

    public boolean save(KeyRecord t) {
        String sql = "insert into " + TABLE + " (id, user, time, beat_num) values (?,?,?,?)";
        return execute(sql, new Object[]{t.getId(), t.getUser(), t.getTime(), t.getBeatNum()}) == 1;
    }

    public boolean delete(int id) {
        String sql = "delete from " + TABLE + " where id = ?";
        return execute(sql, new Object[]{id}) >= 1;
    }

    public boolean update(KeyRecord t) {
        String sql = "update " + TABLE + " set user = ?, time = ?, beat_num = ? where id = ?";
        return execute(sql, new Object[]{t.getUser(), t.getTime(), t.getBeatNum(), t.getId()}) == 1;
    }

    public KeyRecord findById(int id) {
        String sql = "select * from " + TABLE + " where id = ?";
        List<KeyRecord> list = select(sql, new Object[]{id}, KeyRecord.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

}
