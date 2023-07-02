package com.example.sfcm.src.calllog;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class CallLogReader {
    public static final String TAG = CallLogReader.class.getSimpleName();
    public static final int CALLLOG_READ_LIMIT = 10;

    CallLogReader() {
    }

    public static List<CallLogElem> fetch(Context _context) {
        ContentResolver resolver = _context.getContentResolver();

        Cursor cursor = getCursor(resolver, CALLLOG_READ_LIMIT);

        cursor.moveToFirst();

        int idxNumber = cursor.getColumnIndex(CallLog.Calls.NUMBER);
        int idxDate = cursor.getColumnIndex(CallLog.Calls.DATE);
        int idxType = cursor.getColumnIndex(CallLog.Calls.TYPE);

        List<CallLogElem> container = new ArrayList<>();

        while (!cursor.isAfterLast()) {

            String number = cursor.getString(idxNumber);
            String date = cursor.getString(idxDate);
            String type = cursor.getString(idxType);

            container.add(new CallLogElem(number, date, type));

            cursor.moveToNext();
        }

        cursor.close();

        return container;
    }


    private static Cursor getCursor(ContentResolver resolver, int limit) {

        Bundle queryArgs = new Bundle();
        queryArgs.putInt(ContentResolver.QUERY_ARG_OFFSET, 0);
        queryArgs.putInt(ContentResolver.QUERY_ARG_LIMIT, limit);

        return resolver.query(
                android.provider.CallLog.Calls.CONTENT_URI,
                new String[]{android.provider.CallLog.Calls.NUMBER,
                        android.provider.CallLog.Calls.TYPE,
                        android.provider.CallLog.Calls.DATE},
                queryArgs,
                null
        );
    }
}
