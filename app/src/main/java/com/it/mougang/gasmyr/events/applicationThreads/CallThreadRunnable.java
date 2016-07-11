package com.it.mougang.gasmyr.events.applicationThreads;

import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.it.mougang.gasmyr.events.Utilities.Utils;
import com.it.mougang.gasmyr.events.database.CALL;

/**
 * Created by gamyr for project: Events on 6/24/16 22:09.
 */
public class CallThreadRunnable implements Runnable {

    private Context context;
    private String USER_ID;
    private CALL callToSend;
    public CallThreadRunnable(Context ctxt, String userid, CALL call){
        this.context=ctxt;
        this.USER_ID=userid;
        this.callToSend=call;
    }
    @Override
    public void run() {
        sendCALLToServer();
    }

    private void sendCALLToServer(){
        DatabaseReference databaseReference = Utils.getFireBaseCallReference
                (context, USER_ID);
        databaseReference.push().setValue(callToSend);
    }
}
