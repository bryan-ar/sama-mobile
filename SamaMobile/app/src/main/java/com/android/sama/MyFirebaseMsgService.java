package com.android.sama;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMsgService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
        Log.d("====>","NEW_TOKEN: "+token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        super.onMessageReceived(remoteMessage);
        Log.i("==========>", "De: " + remoteMessage.getFrom());
        Log.i("==========>", "Mensaje: " + remoteMessage.getNotification().getBody());

        /*if (remoteMessage.getData().size() > 0) {
            Log.i("======>", "Message data payload: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
        }*/
        if (remoteMessage.getNotification() != null) {
            Log.i("======>", "Message Notification Body: " + remoteMessage.getNotification().getBody());
            createNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
        }

    }

    private void createNotification(String title, String messageBody) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("my_channel_id_01", "my_channel_id_01", importance);
            channel.setDescription("my_channel_id_01");
            notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Intent intent = new Intent( this , PedidosActivity.class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent actionPendingIntent = PendingIntent.getActivity(this,1,intent,PendingIntent.FLAG_IMMUTABLE);

        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                //.addAction(R.drawable.common_google_signin_btn_icon_light, "ACEPTAR", actionPendingIntent)
                //.setWhen(System.currentTimeMillis())
                .setContentIntent(actionPendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_LARGE)
                .setContentTitle(title)
                //.setSound(Uri.parse("android.resource://"+getPackageName()+"/" + R.raw.beeep))
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentInfo("SMART");

        notificationManager.notify(/*notification id*/1, notificationBuilder.build());
    }
}