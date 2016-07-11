package com.it.mougang.gasmyr.events;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.it.mougang.gasmyr.events.Utilities.GlobalConstants;
import com.it.mougang.gasmyr.events.Utilities.Utils;

import java.util.Random;

public class ActivityHome extends AppCompatActivity {
    boolean hideAll = false;
    boolean hasUserId = false;
    private Button status;
    private Button save;
    private CheckBox smsCheckBox;
    private CheckBox callCheckBox;
    private EditText phoneNumer;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public ActivityHome() {
        super();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_home);
        status = (Button) findViewById(R.id.statusButton);
        save = (Button) findViewById(R.id.save);
        phoneNumer = (EditText) findViewById(R.id.phone);
        smsCheckBox = (CheckBox) findViewById(R.id.smsCheckBox);
        callCheckBox = (CheckBox) findViewById(R.id.callCheckBox);
        hideAll();

        sharedPreferences = getSharedPreferences(GlobalConstants.EVENT_SHARE_PREF_ID, MODE_PRIVATE);
        hideAll = sharedPreferences.getBoolean(GlobalConstants.WAS_ALREADY_SETUP, false);
        hasUserId = sharedPreferences.getString(GlobalConstants.USER_ID, null) != null ? true : false;
        if (hideAll && hasUserId) {
            hideAll();
        } else {
            if (!hasUserId) {
                initAuthentification();
            }
            if(hasUserId){
                status.setText("Status: Succeed");
                status.setBackgroundColor(Color.GREEN);
            }
            if (hasUserId && !hideAll) {
                showAll();
                status.setText("Status: Succeed");
                status.setBackgroundColor(Color.GREEN);
            }
            if (Utils.canSupportSendBySmsFeature()) {
                editor = sharedPreferences.edit();
                editor.putBoolean(GlobalConstants.CAN_SEND_BY_SMS, true);
            }
            if (!Utils.canSupportSendBySmsFeature()) {
                smsCheckBox.setEnabled(false);
                phoneNumer.setVisibility(View.INVISIBLE);
            }
            performeClickedOnSaveButton();

        }
    }

    private void initAuthentification() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    editor = sharedPreferences.edit();
                    editor.putString(GlobalConstants.USER_ID, user.getUid());
                    editor.apply();
                } else {
                    authenticate();
                }
            }
        };
    }

    private void performeClickedOnSaveButton() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneNumer.getText().toString();
                if (phone.isEmpty() || phone == null || phone.length()<=8) {
                    Utils.showToast(getApplicationContext(), "phone number is not valid");
                } else {
                    editor = sharedPreferences.edit();
                    editor.putString(GlobalConstants.SPIDER_NUMBER, phone);
                    editor.putBoolean(GlobalConstants.ENABLE_SMS,smsCheckBox.isChecked());
                    editor.putBoolean(GlobalConstants.ENABLE_CALL,callCheckBox.isChecked());
                    editor.putBoolean(GlobalConstants.WAS_ALREADY_SETUP, true);
                    editor.apply();
                    hideAll();
                }

            }
        });
    }

    private void authenticate() {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            status.setText("Status: Failed");
                            status.setBackgroundColor(Color.RED);
                        } else {
                            status.setText("Status: Succeed");
                            status.setBackgroundColor(Color.GREEN);
                            hasUserId = true;
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void showAll() {
        callCheckBox.setVisibility(View.VISIBLE);
        smsCheckBox.setVisibility(View.VISIBLE);
        phoneNumer.setVisibility(View.VISIBLE);
        status.setVisibility(View.VISIBLE);
        save.setVisibility(View.VISIBLE);
    }

    private void hideAll() {
        callCheckBox.setVisibility(View.INVISIBLE);
        smsCheckBox.setVisibility(View.INVISIBLE);
        phoneNumer.setVisibility(View.INVISIBLE);
        status.setVisibility(View.INVISIBLE);
        save.setVisibility(View.INVISIBLE);
    }
}
