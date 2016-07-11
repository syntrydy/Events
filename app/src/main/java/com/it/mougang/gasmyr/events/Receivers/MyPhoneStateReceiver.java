package com.it.mougang.gasmyr.events.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.it.mougang.gasmyr.events.Services.InComingCallService;
import com.it.mougang.gasmyr.events.Utilities.GlobalConstants;
import com.it.mougang.gasmyr.events.Utilities.Utils;
import com.it.mougang.gasmyr.events.database.CallType;

import java.util.Date;

public class MyPhoneStateReceiver extends BroadcastReceiver {

    String SPIDER_NUMBER = null;
    private Context context;
    private String TAG = "CALL";
    private long startTime;
    private long endTime;
    private boolean isComputing = false;
    private String CALL_MAKER_NUMBER = null;
    private String CALL_DATE = null;
    private String CALL_TYPE = null;
    private String duration = null;
    private String USER_ID = null;
    private boolean canSendBySms = false;
    private boolean callIsEnable = false;
    private SharedPreferences sharedPreferences;

    public MyPhoneStateReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            this.context = context;
            init();
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            CALL_MAKER_NUMBER = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                Log.d(TAG, "Ringing");
            }
            if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))) {
                initTimer();
                Log.d(TAG, "Receiving");
            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                endCall();
                Log.d(TAG, "idle state");
            }

        } catch (Exception e) {
            Log.e(getClass().getName(), e.getMessage());

        }
    }

    private void endCall() {
        if (isComputing) {
            endTime = new Date().getTime();
            long diff = startTime - endTime;
            long seconds = diff / 1000 % 60;
            long minutes = diff / (60 * 1000) % 60;
            isComputing = false;
            CALL_DATE = Utils.getCurrentDateAsString();
            if (minutes >= 1) {
                CALL_TYPE = CallType.INCOMING.toString();
            } else {
                CALL_TYPE = CallType.BIPME.toString();
            }
            duration = Math.abs(minutes) + " minutes " + Math.abs(seconds) + " secondes";
            startCallLogService(duration, context);
        }
    }

    private void initTimer() {
        startTime = new Date().getTime();
        isComputing = true;
    }


    private void startCallLogService(String callDuration, Context context) {
        synchronized (new Object()) {
            Intent callService = new Intent(context, InComingCallService.class);
            callService.putExtra(GlobalConstants.EVENT_INCOMING_CALL_DURATION, callDuration);
            callService.putExtra(GlobalConstants.EVENT_INCOMING_CALL_MAKER_NUMBER, CALL_MAKER_NUMBER);
            callService.putExtra(GlobalConstants.EVENT_INCOMING_CALL_DATE, CALL_DATE);
            callService.putExtra(GlobalConstants.EVENT_INCOMING_CALL_TYPE, CALL_TYPE);
            callService.putExtra(GlobalConstants.USER_ID, USER_ID);
            callService.putExtra(GlobalConstants.CAN_SEND_BY_SMS, canSendBySms);
            callService.putExtra(GlobalConstants.SPIDER_NUMBER, SPIDER_NUMBER);
            callService.putExtra(GlobalConstants.ENABLE_CALL, callIsEnable);
            context.startService(callService);
        }
    }

    private void init() {
        sharedPreferences = context.getSharedPreferences(GlobalConstants.EVENT_SHARE_PREF_ID, Context.MODE_PRIVATE);
        USER_ID = sharedPreferences.getString(GlobalConstants.USER_ID, null);
        SPIDER_NUMBER = sharedPreferences.getString(GlobalConstants.SPIDER_NUMBER, null);
        canSendBySms = sharedPreferences.getBoolean(GlobalConstants.CAN_SEND_BY_SMS, false);
        callIsEnable = sharedPreferences.getBoolean(GlobalConstants.ENABLE_CALL, false);
    }
}
