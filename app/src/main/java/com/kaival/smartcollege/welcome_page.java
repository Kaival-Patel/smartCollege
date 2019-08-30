package com.kaival.smartcollege;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class welcome_page extends AppCompatActivity {
    Button signup,faculty,student;
    private FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    private HashMap<String, Object> firebaseDefaultMap;
    public static final String VERSION_CODE_KEY = "version";
    public String GET_URL;
    public String GET_VERSION;
    public static String GET_ACTIVITY;
    TextView tv;
    TextView choosetxt;
    AnimationDrawable animationDrawable;
    LinearLayout linearLayout;
    AdView madview;
    boolean connection=true;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        MobileAds.initialize(this, "ca-app-pub-9758630723010439/7855444453");
        madview = (AdView) (findViewById(R.id.adView));
        AdRequest adRequest = new AdRequest.Builder().build();
        madview.loadAd(adRequest);
        AlertDialog.Builder ab = new AlertDialog.Builder(welcome_page.this);
        Typeface robotobold = Typeface.createFromAsset(getAssets(), "Roboto-Bold.ttf");
        Typeface robotoreg = Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf");
        tv = (TextView) findViewById(R.id.welcomeText);
        linearLayout=findViewById(R.id.linearlay);
        animationDrawable=(AnimationDrawable)linearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(1500);
        animationDrawable.start();
        choosetxt=(TextView)findViewById(R.id.welcomeText1);
        tv.setTypeface(robotobold);
        if(isFirstTime())
        {
            tv.setText("Welcome,");
            Intent i =new Intent(welcome_page.this,sliding_activitymain.class);
            startActivity(i);
        }
        choosetxt.setTypeface(robotoreg);
        faculty = (Button) findViewById(R.id.signin_faculty);
        faculty.setTypeface(robotoreg);
        faculty.setTypeface(robotoreg);
        student = (Button) findViewById(R.id.signin_student);
        student.setTypeface(robotoreg);
        student.setTypeface(robotoreg);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            {
                ActivityCompat.requestPermissions(welcome_page.this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            }
        }
        startService(new Intent(this,MyService.class));
        firebaseDefaultMap = new HashMap<>();
        firebaseDefaultMap.put(VERSION_CODE_KEY, getCurrentVersionCode());
        mFirebaseRemoteConfig.setDefaults(firebaseDefaultMap);

        //Setting that default Map to Firebase Remote Config

        //Setting Developer Mode enabled to fast retrieve the values
        mFirebaseRemoteConfig.setConfigSettings(
                new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG)
                        .build());

        //Fetching the values here
        mFirebaseRemoteConfig.fetch(1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mFirebaseRemoteConfig.activateFetched();
                    //calling function to check if new version is available or not
                    GET_VERSION=mFirebaseRemoteConfig.getString(VERSION_CODE_KEY);
                    System.out.println("GET VERSION:"+GET_VERSION);
                    GET_URL=mFirebaseRemoteConfig.getString("update_url");
                    System.out.println("GET URL:"+GET_URL);
                    GET_ACTIVITY=mFirebaseRemoteConfig.getString("notification_activity");
                    System.out.println("GET_ACTIVITY:"+GET_ACTIVITY);
                    checkForUpdate();
                } else {
                    Toast.makeText(welcome_page.this, "Error while checking Updates",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        final ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED &&
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
            connection = false;
        }
        else{
            connection=true;
        }

        if (connection == false) {
            AlertDialog.Builder adp = new AlertDialog.Builder(this);
            adp.setTitle("App won't Work").setMessage("App won't Work without Internet Connection!Please Turn On your Network and Restart the application.").setIcon(R.drawable.interneticon).setPositiveButton("Turning it on", null).setCancelable(false).show();
        } else
        {

            student.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(welcome_page.this, whichsignup.class);
                    startActivity(i);
                }
            });

            faculty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(welcome_page.this, whichactivity.class);
                    startActivity(intent);
                }
            });
        }
        String instanceId=FirebaseInstanceId.getInstance().getToken();
        System.out.println("Instance ID:"+instanceId);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
        String currentDateandTime = sdf.format(Calendar.getInstance().getTime());
        System.out.println("Current Time:"+currentDateandTime);


    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder ad=new AlertDialog.Builder(this);
        ad.setTitle("Sayonara!").setCancelable(false).setMessage("Are You Sure To Exit the app?").setNegativeButton("NOPE",null).setPositiveButton("YEAH", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                System.exit(0);

            }
        }).show();
    }
    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }

    private void checkForUpdate() {
        float latestAppVersion = (float) mFirebaseRemoteConfig.getDouble(VERSION_CODE_KEY);
        float currentversionname=Float.parseFloat(getCurrentVersionCode());
        System.out.println("Latest Version:"+latestAppVersion);
        System.out.println("Current Version:"+currentversionname);
        if (latestAppVersion > currentversionname) {
            new AlertDialog.Builder(this).setTitle("New Update Available")
                    .setMessage("A new version of this app is available. Please update it!").setPositiveButton(
                    "UPDATE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                Uri uri = Uri.parse("http://" + GET_URL); // missing 'http://' will cause crashed
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(getApplicationContext(),e.getMessage().toString(),Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setCancelable(false).show();
        } else  {
        }
    }

    private String getCurrentVersionCode() {
        String results="";
        try {
            results=getPackageManager().getPackageInfo(getPackageName(),0).versionName;
            results=results.replaceAll("[a-zA-Z]|-","");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return results;
    }



}
