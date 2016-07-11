package com.it.mougang.gasmyr.events.Services;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.it.mougang.gasmyr.events.ContentObservers.OutGoingSmsContentObserver;
import com.it.mougang.gasmyr.events.Utilities.GlobalConstants;


public class OutGoingSmsService extends IntentService {
    private SharedPreferences sharedPreferences;


    public OutGoingSmsService() {
        super("OutGoingSmsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        sharedPreferences = getSharedPreferences(GlobalConstants.EVENT_SHARE_PREF_ID, MODE_PRIVATE);
        String user_id = sharedPreferences.getString(GlobalConstants.USER_ID, null);
        if (user_id != null) {
            try {
                OutGoingSmsContentObserver outGoingSmsContentObserver = new OutGoingSmsContentObserver(
                        new Handler(), getApplicationContext(), user_id);
                ContentResolver contentResolver = this.getApplicationContext().getContentResolver();
                contentResolver.registerContentObserver(Uri.parse("content://sms"), true, outGoingSmsContentObserver);
            } catch (Exception e) {
                Log.e(this.getClass().getName(),e.getMessage());
            }
        }

    }
}
