package com.play.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 按键数记录
 * Created by yjy on 2017/8/17.
 */
public class KeyRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    private long id; // id
    private String user; // 用户
    private Date time; // 时间
    private int beatNum; // 敲击次数

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getBeatNum() {
        return beatNum;
    }

    public void setBeatNum(int beatNum) {
        this.beatNum = beatNum;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
