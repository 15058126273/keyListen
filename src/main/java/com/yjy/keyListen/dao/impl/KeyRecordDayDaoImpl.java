package com.yjy.keyListen.dao.impl;

import com.yjy.keyListen.Constants;
import com.yjy.keyListen.base.BaseDaoImpl;
import com.yjy.keyListen.dao.KeyRecordDayDao;
import com.yjy.keyListen.entity.KeyRecordDay;

import java.util.ArrayList;
import java.util.List;

/**
 * keyRecordDayDao 记录每天的按键数
 * Created by yjy on 2017/8/22.
 */
public class KeyRecordDayDaoImpl extends BaseDaoImpl<KeyRecordDay> implements KeyRecordDayDao {

    private static final String TABLE = Constants.DATA_BASE + "key_record_day";
    public static KeyRecordDayDaoImpl keyRecordDayDao = new KeyRecordDayDaoImpl();

    public boolean save(KeyRecordDay t) {
        String sql = "insert into " + TABLE + " (id, user, date, beat_num) values (?,?,?,?)";
        return execute(sql, new Object[]{t.getId(), t.getUser(), t.getDate(), t.getBeatNum()}) == 1;
    }

    public boolean delete(int id) {
        String sql = "delete from " + TABLE + " where id = ?";
        return execute(sql, new Object[]{id}) >= 1;
    }

    public boolean update(KeyRecordDay t) {
        String sql = "update " + TABLE + " set user = ?, date = ?, beat_num = ? where id = ?";
        return execute(sql, new Object[]{t.getUser(), t.getDate(), t.getBeatNum(), t.getId()}) == 1;
    }

    public KeyRecordDay findById(int id) {
        String sql = "select * from " + TABLE + " where id = ?";
        List<KeyRecordDay> list = select(sql, new Object[]{id}, KeyRecordDay.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public KeyRecordDay findByT(KeyRecordDay t) {
        String sql = "select * from " + TABLE + " where 1 = 1 ";
        List<Object> paramList = new ArrayList<Object>();
        if (t.getId() != null) {
            sql += " and id = ? ";
            paramList.add(t.getId());
        }
        if (t.getUser() != null) {
            sql += " and user = ?";
            paramList.add(t.getUser());
        }
        if (t.getDate() != null) {
            sql += " and date = ?";
            paramList.add(t.getDate());
        }
        if (t.getBeatNum() != null) {
            sql += " and beat_num = ?";
            paramList.add(t.getBeatNum());
        }
        List<KeyRecordDay> list = select(sql, paramList.toArray(), KeyRecordDay.class);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    public List<KeyRecordDay> findPage(int pageNo, int pageSize) {
        int start = (pageNo - 1) * pageSize, limit = pageSize;
        String sql = "select * from " + TABLE + " order by id desc limit " + start + ", " + limit;
        return select(sql, new Object[]{}, KeyRecordDay.class);
    }

}
