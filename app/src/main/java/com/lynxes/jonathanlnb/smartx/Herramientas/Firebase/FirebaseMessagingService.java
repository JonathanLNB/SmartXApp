package com.lynxes.jonathanlnb.smartx.Herramientas.Firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.lynxes.jonathanlnb.smartx.R;
import com.lynxes.jonathanlnb.smartx.SplashScreen;

/**
 * Created by user on 11/10/2017.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(""+remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle());
    }
    private void showNotification(String mensaje, String titulo) {
        Log.println(Log.ASSERT,"notificacion",""+mensaje);
        System.out.println(mensaje);
        Intent i = new Intent(this, SplashScreen.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent))
                .setVibrate(new long[]{0, 300, 200, 300, 200, 300, 500, 100, 500})
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(mensaje))
                .setSmallIcon(R.drawable.movil)
                .setSound(alarmSound)
                .setContentIntent(pendingIntent)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }
}