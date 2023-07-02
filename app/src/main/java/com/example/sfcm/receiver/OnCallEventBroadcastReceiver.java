package com.example.sfcm.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.sfcm.src.ContactFilter;
import com.example.sfcm.src.calllog.CallLogElem;
import com.example.sfcm.src.calllog.CallLogReader;
import com.example.sfcm.src.db.ContactPreference;
import com.example.sfcm.src.db.ContactPreferenceDB;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class OnCallEventBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = OnCallEventBroadcastReceiver.class.getSimpleName();

    //TODO Hyeon : Make DATABASE Cache and check Database updatetime.

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {

        } else {

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);

            final int currentCallState = tm.getCallState();

            // Hyeon : to ignore the intent from READ_CALL_LOG permission

            if (intent.hasExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)) {

                String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);

                //final String phone_number = incomingNumber;
                final String phone_number = PhoneNumberUtils.formatNumber(incomingNumber, Locale.getDefault().getCountry());
                ContactPreferenceDB database = ContactPreferenceDB.getInstance(context);

                //TODO Hyeon : Make DATABASE Cache and check Database updatetime.

                ContactFilter cf = new ContactFilter(database);
                List<CallLogElem> log = CallLogReader.fetch(context);

                if (cf.contains(phone_number)) {
                    Log.d(TAG, "db contains phone number !! : " + phone_number);
                    //CallLogReader.fetch(context);
                    switch (currentCallState) {
                        case TelephonyManager.CALL_STATE_RINGING:
                            Log.d(TAG, "Phone num : " + phone_number);
                            long ut = Calendar.getInstance().getTimeInMillis() / 1000;

                            //TODO : Apply prefs
                            long count = log.stream().filter(v -> (Long.parseLong(v.date) > (ut - 10) && (v.number.equals(phone_number)))).count();

                            Log.d(TAG, "Count : " + count);


                            break;
                        case TelephonyManager.CALL_STATE_OFFHOOK:
                            Log.d(TAG, "OnCallOffhook");

                            break;
                        case TelephonyManager.CALL_STATE_IDLE:
                            Log.d(TAG, "OnCallIdle");

                            break;
                    }

                }

            }

//            final String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);


//              Check if phone number filtered
//            new Handler().postDelayed(() -> {
//                List<CallLogElem> logs = CallLogReader.fetch(context, 10);
//                //Runproc
//            }, 200);

        }
    }
}
