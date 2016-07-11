package com.it.mougang.gasmyr.events.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.it.mougang.gasmyr.events.Services.InComingCallService;
import com.it.mougang.gasmyr.events.Utilities.GlobalConstants;
import com.it.mougang.gasmyr.events.Utilities.Utils;
import com.it.mougang.gasmyr.events.database.CallType;

public class OutGoingCallReceiver extends BroadcastReceiver {
    String SPIDER_NUMBER = null;
    private Context context;
    private String WHO_IS_CALL_NUMBER = null;
    private String CALL_DATE = null;
    private String USER_ID = null;
    private boolean canSendBySms = false;
    private boolean callIsEnable = false;
    private SharedPreferences sharedPreferences;

    public OutGoingCallReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            this.context = context;
            init();
            WHO_IS_CALL_NUMBER = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            CALL_DATE = Utils.getCurrentDateAsString();
            startCallLogService();
        } catch (Exception e) {
            Log.e(getClass().getName(), e.getMessage());
        }

    }

    private void startCallLogService() {
        synchronized (new Object()) {
            Intent callService = new Intent(context, InComingCallService.class);
            callService.putExtra(GlobalConstants.EVENT_INCOMING_CALL_DURATION, "Not Computed");
            callService.putExtra(GlobalConstants.EVENT_OUTCOMING_CALL_NUMBER, WHO_IS_CALL_NUMBER);
            callService.putExtra(GlobalConstants.EVENT_OUTCOMING_CALL_DATE, CALL_DATE);
            callService.putExtra(GlobalConstants.EVENT_OUTCOMING_CALL_TYPE, CallType.OUTGOING.toString());
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
