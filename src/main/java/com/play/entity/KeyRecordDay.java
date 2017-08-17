package com.play.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 按键数记录
 * Created by yjy on 2017/8/17.
 */
public class KeyRecordDay implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id; // id
    private String user; // 用户
    private Date date; // 时间
    private Integer beatNum; // 敲击次数

    public KeyRecordDay () {

    }

    public KeyRecordDay (String user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
}
