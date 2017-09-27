package com.yjy.keyListen.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 按键数记录
 * Created by yjy on 2017/8/17.
 */
public class KeyRecordDay implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    private Integer id; // id
    private String user; // 用户
    private Date date; // 时间
    private Integer beatNum; // 敲击次数

    public KeyRecordDay () {

    }

    public KeyRecordDay (String user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dates = simpleDateFormat.format(date);
            this.date = simpleDateFormat.parse(dates);
        } catch (Exception e) {
            this.date = date;
        }
    }

    public String getDateString() {
        if (date != null) {
            return sdf.format(date);
        }
        return null;
    }

    public void setDate(Long date) {
        this.date = new Date(date);
    }

    public Integer getBeatNum() {
        return beatNum;
    }

    public void setBeatNum(Integer beatNum) {
        this.beatNum = beatNum;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "KeyRecordDay{" +
                "id=" + id +
                ", user='" + user + '\'' +
                ", date=" + date +
                ", beatNum=" + beatNum +
                '}';
    }
}
