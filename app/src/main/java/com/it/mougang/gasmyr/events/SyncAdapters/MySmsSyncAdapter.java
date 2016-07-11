package com.it.mougang.gasmyr.events.SyncAdapters;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * Created by gamyr for project: Events on 6/26/16 07:51.
 */
public class MySmsSyncAdapter extends AbstractThreadedSyncAdapter{
    private ContentResolver contentResolver;
    public MySmsSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        contentResolver=context.getContentResolver();
    }

    public MySmsSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        contentResolver=context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

    }
}
