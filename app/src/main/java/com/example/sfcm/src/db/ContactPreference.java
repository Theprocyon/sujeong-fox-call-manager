package com.example.sfcm.src.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ContactPreference {

    //TODO thep : get from preference

//    public static final int MIN_COUNT = 0;
//    public static final int MAX_COUNT = 10;
//    public static final int MIN_TIME = 120;
//    public static final int MAX_TIME = 600;


    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "phone_num")
    public String phone;

    @ColumnInfo(name = "call_count")
    public int missingCallCount;

    @ColumnInfo(name = "interval_time")
    public int intervalTime;

    @ColumnInfo(name = "enabled")
    public boolean enabled;

//    public String getName() {
//        return this.getName();
//    }
//
//    public int getID() {
//        return this.id;
//    }
//
//    public String getPhoneNumber() {
//        return this.phone;
//    }
//
//    public int getMissingCallCount() {
//        return missingCallCount;
//    }
//
//    public int getIntervalTime() {
//        return intervalTime;
//    }
//
//    public void setName(String _name){
//        this.name = _name;
//    }
//
//    public void setId(int _id) {
//        this.id = _id;
//    }
//
//    public void setPhoneNumber(String _phoneNum) {
//        this.phone = _phoneNum;
//    }
//
//    public void setMissingCallCount(int _count) {
//        missingCallCount = (_count < MIN_COUNT) ? MIN_COUNT : (_count > MAX_COUNT) ? MAX_COUNT : _count;
//    }
//
//    public void setIntervalTime(int _time) {
//        intervalTime = (_time < MIN_TIME) ? MIN_TIME : (_time > MAX_TIME) ? MAX_TIME : _time;
//    }
}