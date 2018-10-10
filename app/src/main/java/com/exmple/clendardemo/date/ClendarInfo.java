package com.exmple.clendardemo.date;

import java.io.Serializable;
import java.util.Date;

/**
 * 作者: 伍跃武
 * 时间： 2018/5/10
 * 描述：
 */

public class ClendarInfo implements Serializable {
    public Date date;
    public int numHouse;
    public boolean isOver;
    public boolean isEnd;

    public Date getDate() {
        return date;
    }

    public ClendarInfo setDate(Date date) {
        this.date = date;
        return this;
    }

    public int getNumHouse() {
        return numHouse;
    }

    public ClendarInfo setNumHouse(int numHouse) {
        this.numHouse = numHouse;
        return this;
    }
}
