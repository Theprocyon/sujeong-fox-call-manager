package com.example.sfcm.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.telephony.TelephonyManager;
import com.example.sfcm.src.CallLogElem;
import com.example.sfcm.src.CallLogReader;
import java.util.List;

public class OnRcvCallBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = OnRcvCallBroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(final Context context, Intent intent) {

        final String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
//              String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
//              final String phone_number = PhoneNumberUtils.formatNumber(incomingNumber, Locale.getDefault().getCountry());

//              Check if phone number filtered
            new Handler().postDelayed(() -> {
                List<CallLogElem> logs = CallLogReader.fetch(context, 10);
                //Runproc
            }, 200);

        }
    }
}

