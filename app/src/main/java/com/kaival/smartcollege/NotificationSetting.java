package com.kaival.smartcollege;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationSetting extends AppCompatActivity {
Switch update,attendance,marks;
boolean updatenotif,attendancenotif,marksnotif;
DatabaseHelper mDatabaseHelper;
SharedPreferences mPrefs;
private static final String PREFS_NAME = "PrefFile";
ListView listView;
AdView madview;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);
        MobileAds.initialize(this, "ca-app-pub-9758630723010439/7855444453");
        madview = (AdView) (findViewById(R.id.adView));
        AdRequest adRequest = new AdRequest.Builder().build();
        madview.loadAd(adRequest);
        mDatabaseHelper=new DatabaseHelper(this);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setTitle("Notification Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        update=findViewById(R.id.update_notif);
        attendance=findViewById(R.id.attendancerem_notif);
        marks=findViewById(R.id.marksrem_notif);
        getPreferencesData();
        update.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                {
                    updatenotif=true;
                    SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("pref_updatenotif","true");
                    editor.apply();
                    FirebaseMessaging.getInstance().subscribeToTopic("Update_Notification");
                }
                else
                {
                    updatenotif=false;
                    SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("pref_updatenotif","false");
                    editor.apply();
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Update_Notification");

                }
            }
        });
        marks.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true)
                {
                    marksnotif=true;
                    SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("pref_marksnotif","true");
                    editor.apply();
                    FirebaseMessaging.getInstance().subscribeToTopic("Marks_Notification");
                }
                else
                {
                    marksnotif=false;
                    SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("pref_marksnotif","false");
                    editor.apply();
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Marks_Notification");

                }
            }
        });
        attendance.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true) {
                    attendancenotif = true;
                    SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("pref_attendancenotif","true");
                    editor.apply();
                    FirebaseMessaging.getInstance().subscribeToTopic("Attendance_Notification");
                }
                else
                {
                    attendancenotif=false;
                    SharedPreferences sharedPreferences = PreferenceManager
                            .getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("pref_attendancenotif","false");
                    editor.apply();
                    FirebaseMessaging.getInstance().unsubscribeFromTopic("Attendance_Notification");
                }
            }
        });

    }
    private void getPreferencesData() {
        SharedPreferences sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(this);
        if(sharedPreferences.contains("pref_updatenotif"))
        {
            String em=sharedPreferences.getString("pref_updatenotif","not found.");
            System.out.println("Update Notificaiton:"+em);
            if(em.toString().equals("true") && em!=null)
            {
                FirebaseMessaging.getInstance().subscribeToTopic("Update_Notification");
                update.setChecked(true);
            }
            else
            {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("Update_Notification");
                update.setChecked(false);
            }
        }
        if(sharedPreferences.contains("pref_attendancenotif"))
        {
            String em=sharedPreferences.getString("pref_attendancenotif","not found.");
            System.out.println("Attendance Notificaiton:"+em);
            if(em.toString().equals("true") && em!=null)
            {
                FirebaseMessaging.getInstance().subscribeToTopic("Attendance_Notification");
                attendance.setChecked(true);
            }
            else
            {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("Attendance_Notification");
                attendance.setChecked(false);
            }

        }
        if(sharedPreferences.contains("pref_marksnotif"))
        {
            String em=sharedPreferences.getString("pref_marksnotif","not found.");
            System.out.println("Marks Notificaiton:"+em);
            if(em.toString().equals("true") && em!=null)
            {
                FirebaseMessaging.getInstance().subscribeToTopic("Marks_Notification");
                marks.setChecked(true);
            }
            else
            {
                FirebaseMessaging.getInstance().unsubscribeFromTopic("Marks_Notification");
                marks.setChecked(false);
            }
        }

    }



}
