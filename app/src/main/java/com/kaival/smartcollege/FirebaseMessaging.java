package com.kaival.smartcollege;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;

public class FirebaseMessaging extends FirebaseMessagingService {
    String message,title,click_action;
    private FirebaseRemoteConfig mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    OreoNotifications oreoNotifications;
    NotificationManager notificationManager;
    NotificationChannel notificationChannel;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getData().size()>0) {
            JSONObject data = new JSONObject(remoteMessage.getData());
            try {
                String jsonmessage = data.getString("extra_information");
                System.out.println("Extra information passed:" + jsonmessage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if(remoteMessage.getNotification()!=null)
        {
            title=remoteMessage.getNotification().getTitle();
            message=remoteMessage.getNotification().getBody();
            click_action=remoteMessage.getNotification().getClickAction();
            System.out.println("Title Passed:"+title);
            System.out.println("Message Passed:"+message);
            System.out.println("Click Action:"+click_action);
            for (Map.Entry<String, String> entry : remoteMessage.getData().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                System.out.println("KEY:"+key);
                System.out.println("VALUE:"+value);
            }
            try {
                sendNotification(message,title,click_action);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        oreoNotifications=new OreoNotifications(this);


    }
    private void sendNotification(String message, String title,String click_action) throws ClassNotFoundException {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        Intent intent = new Intent(this, welcome_splash_screen.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder builder=oreoNotifications.getchannelNotif(title,message,click_action);
            oreoNotifications.getManager().notify(new Random().nextInt(),builder.build());
            }

        else {

            NotificationCompat.Builder notification = new NotificationCompat.Builder(this);
            notification.setAutoCancel(true);
            notification.setSmallIcon(R.drawable.graduate);
            notification.setWhen(System.currentTimeMillis());
            notification.setContentTitle(title);
            notification.setContentText(message);
            if(click_action.equals("WELCOMEACTIVITY"))
            {   Intent i=new Intent(this,Facultyloggedin.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(pendingIntent);
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            notification.setContentIntent(pendingIntent);
            notificationManager.notify(0, notification.build());
        }

    }
}

