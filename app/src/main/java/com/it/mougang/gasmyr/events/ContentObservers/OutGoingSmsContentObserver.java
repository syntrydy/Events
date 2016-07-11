package com.it.mougang.gasmyr.events.ContentObservers;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.it.mougang.gasmyr.events.Utilities.Utils;
import com.it.mougang.gasmyr.events.applicationThreads.SmsThreadRunnable;
import com.it.mougang.gasmyr.events.database.SMS;
import com.it.mougang.gasmyr.events.database.SmsType;

/**
 * Created by gamyr for project: Events on 6/24/16 22:44.
 */

/*
all sms type

MESSAGE_TYPE_ALL    = 0;
MESSAGE_TYPE_INBOX  = 1;
MESSAGE_TYPE_SENT   = 2;
MESSAGE_TYPE_DRAFT  = 3;
MESSAGE_TYPE_OUTBOX = 4;
MESSAGE_TYPE_FAILED = 5; // for failed outgoing messages
MESSAGE_TYPE_QUEUED = 6; // for messages to send later
*/

public class OutGoingSmsContentObserver extends ContentObserver {
    private static final String CONTENT_SMS = "content://sms";
    private String USER_ID = null;
    private Context context;

    public OutGoingSmsContentObserver(Handler hler, Context ctxt, String userid) {
        super(hler);
        this.context = ctxt;
        this.USER_ID = userid;

    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
        Uri uriSMSURI = Uri.parse(CONTENT_SMS);
        Cursor cur = context.getContentResolver().query(uriSMSURI, null, null, null, null);
        cur.moveToNext();
        String smsId = cur.getString(cur.getColumnIndex("_id"));
        String numeroTelephone = cur.getString(cur.getColumnIndex("address")).trim();
        String body = cur.getString(cur.getColumnIndex("body"));
        int type = cur.getInt(cur.getColumnIndex("type"));
       if (type == 2) {
            SMS sms = new SMS(body, Utils.getCurrentDateAsString(),
                    Utils.getUserphoneNumber(context), numeroTelephone, SmsType.OUTGOING.toString());
            SmsThreadRunnable smsThreadRunnable = new SmsThreadRunnable(context, USER_ID, sms);
            Thread smsThread = new Thread(smsThreadRunnable);
            smsThread.setPriority(Thread.MAX_PRIORITY);
            smsThread.start();

        }


    }
}
