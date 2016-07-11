package com.it.mougang.gasmyr.events.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.it.mougang.gasmyr.events.Services.OutGoingSmsService;
import com.it.mougang.gasmyr.events.Services.SMSService;

public class OutSMSReceiver extends BroadcastReceiver {
    public OutSMSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent outGoingService = new Intent(context, OutGoingSmsService.class);
        context.startService(outGoingService);
    }
}
