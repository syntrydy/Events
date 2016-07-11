package com.it.mougang.gasmyr.events.SyncAdapters;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MySmsSyncService extends Service {
    private static MySmsSyncAdapter mySmsSyncAdapter=null;
    private static final Object mySmsSyncAdapterLock=new Object();
    public MySmsSyncService() {
    }

    @Override
    public void onCreate() {
        synchronized (mySmsSyncAdapterLock){
            if(mySmsSyncAdapterLock==null){
                mySmsSyncAdapter=new MySmsSyncAdapter(getApplicationContext(),true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
       return mySmsSyncAdapter.getSyncAdapterBinder();
    }
}
