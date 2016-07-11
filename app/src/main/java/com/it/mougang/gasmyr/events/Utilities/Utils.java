package com.it.mougang.gasmyr.events.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by gamyr for project: Events on 5/22/16 06:56.
 */
public class Utils {
    private static FirebaseDatabase mDatabase;

    public static String getCurrentDateAsString() {
        return Calendar.getInstance(TimeZone.getDefault(), Locale.getDefault()).getTime().toString();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void sendNewSms(String message, String sendTo) {
        SmsManager smsManager = SmsManager.getDefault();
        if (message.length() > 140) {
            ArrayList<String> messageParts = smsManager.divideMessage(message);
            smsManager.sendMultipartTextMessage(sendTo, null, messageParts, null, null);
        } else {
            smsManager.sendTextMessage(sendTo, null, message, null, null);
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            return true;
        }
        return false;
    }

    public static String getUserphoneNumber(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = telephonyManager.getLine1Number();
        if (phoneNumber == null && phoneNumber.isEmpty()) {
            return telephonyManager.getSimSerialNumber() + telephonyManager.getNetworkOperator()
                    + telephonyManager.getNetworkCountryIso();
        } else {
            return phoneNumber;
        }
    }

    public static SimpleDateFormat getFormatter() {
        return new SimpleDateFormat("dd-mm-yyyy_hh.mm.ss");
    }

    public static Bitmap takeScreenShot(View view) {
        Bitmap screenShot = null;
        Date now = new Date();
        String folderPath = "GoogleScreen";
        createFolder(folderPath);
        String filePath = Environment.getExternalStorageDirectory().toString() +
                File.separator + folderPath + File.separator + "Capture_" + getFormatter().format(now) + ".jpeg";
        try {
            int witdh = view.getMeasuredWidth();
            int height = view.getMeasuredHeight();
            screenShot = Bitmap.createBitmap(witdh, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(screenShot);
            view.draw(canvas);

        } catch (Exception e) {
        }
        saveAsFile(screenShot, filePath);
        return screenShot;
    }

    private static void saveAsFile(Bitmap screenShot, String filePath) {
        ByteArrayOutputStream byteArrayOutputStream;
        File file;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            screenShot.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(byteArrayOutputStream.toByteArray());
            fos.close();

        } catch (Exception e) {

        }
    }

    public static boolean createFolder(String path) {
        File folder = new File(Environment.getExternalStorageDirectory() + File.separator + path);
        if (!folder.exists()) {
            boolean success = folder.mkdir();
            return success;
        }
        return false;

    }

    public static void removeDirectoty(File folder) {
        if (folder.isDirectory()) {
            File[] children = folder.listFiles();
            for (File child : children) {
                child.delete();
            }
            folder.delete();
        }
    }

    public static DatabaseReference getFireBaseSmsReference(Context context, String userId) {
        if(mDatabase==null){
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase.getReference("APPLICATION_SMS_" + userId + "_" + Utils.getUserphoneNumber(context));

    }

    public static DatabaseReference getFireBaseCallReference(Context context, String userId) {
        if(mDatabase==null){
            mDatabase = FirebaseDatabase.getInstance();
            mDatabase.setPersistenceEnabled(true);
        }
        return mDatabase.getReference("APPLICATION_CALL_" + userId + "_" + Utils.getUserphoneNumber(context));
    }
    public static boolean canSupportSendBySmsFeature(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return false;
        }
        else{
            return true;
        }
    }
}
