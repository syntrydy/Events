package com.it.mougang.gasmyr.events.applicationThreads;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.it.mougang.gasmyr.events.Utilities.Utils;
import com.it.mougang.gasmyr.events.database.SMS;

/**
 * Created by gamyr for project: Events on 6/24/16 21:58.
 */
public class SmsThreadRunnable implements Runnable {
    private Context context;
    private String USER_ID;
    private SMS smsToSend;
    public SmsThreadRunnable(Context ctxt, String userid, SMS sms){
        this.context=ctxt;
        this.USER_ID=userid;
        this.smsToSend=sms;
    }
    @Override
    public void run() {
        sendSMSToServer();
    }
    private void sendSMSToServer(){
        DatabaseReference databaseReference = Utils.getFireBaseSmsReference
                (context, USER_ID);
        databaseReference.push().setValue(smsToSend);
    }
}
