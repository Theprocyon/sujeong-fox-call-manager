package com.example.sfcm.receiver;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.PhoneNumberUtils;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.sfcm.src.calllog.CallLogElem;
import com.example.sfcm.src.calllog.CallLogReader;

import java.util.List;
import java.util.Locale;

public class OnCallEventBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = OnCallEventBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {

        } else {

            TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);

//            final String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                    final String phone_number = PhoneNumberUtils.formatNumber(incomingNumber, Locale.getDefault().getCountry());
                    Log.d(TAG, "IncomingCallNumberWas = " + incomingNumber + ", " + phone_number);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.d(TAG, "OnCallOffhook");
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.d(TAG, "OnCallIdle");
                    break;
            }
//              Check if phone number filtered
//            new Handler().postDelayed(() -> {
//                List<CallLogElem> logs = CallLogReader.fetch(context, 10);
//                //Runproc
//            }, 200);

        }
    }
}
