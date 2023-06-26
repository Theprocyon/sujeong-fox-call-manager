package com.example.sfcm.src.calllog;

public class CallLogElem {
    public final String number;
    public final String date;
    public final String type;

    CallLogElem(String _number, String _date, String _type) {
        number = _number;
        date = _date;
        type = _type;
    }
}