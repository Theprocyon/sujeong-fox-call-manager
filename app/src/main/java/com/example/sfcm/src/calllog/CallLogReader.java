package com.example.sfcm.src.calllog;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.CallLog;

import com.example.sfcm.src.calllog.CallLogElem;

import java.util.ArrayList;
import java.util.List;

public class CallLogReader {

    CallLogReader() {
    }

    public static List<CallLogElem> fetch(Context _context, int limit) {
        ContentResolver resolver = _context.getContentResolver();

        Cursor cursor = getCursor(resolver, limit);

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

        String sortOrder = android.provider.CallLog.Calls.DATE + " DESC limit " + limit;

        return resolver.query(
                android.provider.CallLog.Calls.CONTENT_URI,
                new String[]{android.provider.CallLog.Calls.NUMBER,
                        android.provider.CallLog.Calls.TYPE,
                        android.provider.CallLog.Calls.DATE},
                null,
                null,
                sortOrder
        );
    }
}
