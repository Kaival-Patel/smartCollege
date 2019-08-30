package com.kaival.smartcollege;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

public class OreoNotifications extends ContextWrapper {
    private final String CHANNEL_ID="com.kaival.smartcollege.channel";
    private final String CHANNEL_NAME="SmartCollege Channel";
    private NotificationManager notificationManager;
    public OreoNotifications(Context base) {
        super(base);
        createChannel();
    }

    public void createChannel() {
        NotificationChannel smartchannel= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            smartchannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            smartchannel.enableLights(true);
            smartchannel.enableVibration(true);
            smartchannel.setLightColor(Color.BLUE);
            smartchannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(smartchannel);
        }
    }
    public NotificationManager getManager()
    {
        if(notificationManager==null)
        {
            notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        }
        return notificationManager;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification.Builder getchannelNotif(String title, String body,String click_action)
    {
        Intent intent=new Intent(OreoNotifications.this,welcome_splash_screen.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        return new Notification.Builder(getApplicationContext(),CHANNEL_ID).setContentText(body).setContentTitle(title)
                .setSmallIcon(R.mipmap.smartass).setAutoCancel(true).setContentIntent(pendingIntent);

    }
}
