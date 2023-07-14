package com.example.sfcm.src.calllog;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;

import java.net.URI;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

public class CallLogReader {
    public static final String TAG = CallLogReader.class.getSimpleName();
    public static final int CALLLOG_READ_LIMIT = 10;

    private static final String[] projection = new String[]{
            android.provider.CallLog.Calls.NUMBER,
            android.provider.CallLog.Calls.TYPE,
            android.provider.CallLog.Calls.DATE
    };

    CallLogReader() {
    }

    public static List<CallLogElem> fetch(Context _context) {
        ContentResolver resolver = _context.getContentResolver();

        Cursor cursor = getCursor(resolver, CALLLOG_READ_LIMIT);

        final int count = cursor.getCount();
        Log.d(TAG, "count : " + count);

        cursor.moveToFirst();

        int idxNumber = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int idxDate = cursor.getColumnIndex(CallLog.Calls.DATE);
        int idxType = cursor.getColumnIndex(CallLog.Calls.TYPE);

        List<CallLogElem> container = new ArrayList<>();

        while (!cursor.isAfterLast()) {

            String number = cursor.getString(idxNumber);
            String date = cursor.getString(idxDate);
            String type = cursor.getString(idxType);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");


            Log.d(TAG, "item number : " + number + "date :" + sdf.format(Long.parseLong(date)));

            container.add(new CallLogElem(number, date, type));

            cursor.moveToNext();
        }


        cursor.close();

        return container;
    }


    private static Cursor getCursor(ContentResolver resolver, int limit) {

//        Bundle queryArgs = new Bundle();
//        queryArgs.putInt(CallLog.Calls.LIMIT_PARAM_KEY, limit);

        Uri queryUri = CallLog.Calls.CONTENT_URI
                .buildUpon()
                .appendQueryParameter(CallLog.Calls.LIMIT_PARAM_KEY, Integer.toString(limit))
                .build();

        return resolver.query(
                queryUri,
                projection,
                null,
                null
        );
    }
}
