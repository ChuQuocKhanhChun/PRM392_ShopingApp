package com.manager.glassshoping.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.manager.glassshoping.R;
import com.manager.glassshoping.activity.MainActivity;

public class FirebaseMessageReceiver extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

        String title = "";
        String body = "";

        if (message.getNotification() != null) {
            // Gửi từ Firebase Console (notification message)
            title = message.getNotification().getTitle();
            body = message.getNotification().getBody();
        } else if (message.getData().size() > 0) {
            // Gửi từ Postman/server (data message)
            title = message.getData().get("title");
            body = message.getData().get("body");
        }

        if (title != null && body != null) {
            showNotification(title, body);
        }
    }

    private void showNotification(String title, String body) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String chaneliD = "noti";
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),chaneliD)
                .setSmallIcon(R.drawable.img_2)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000,1000,1000,1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);
        builder = builder.setContent(customView(title,body));
        NotificationManager notificationManager=(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O ){
            NotificationChannel notificationChannel = new NotificationChannel(chaneliD,"ShopGlass", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        notificationManager.notify(0,builder.build());
    }

    private RemoteViews customView(String title, String body){
        RemoteViews remoteViews = new RemoteViews(getApplicationContext().getPackageName(), R.layout.notification);
        remoteViews.setTextViewText(R.id.title_notification, title);
        remoteViews.setTextViewText(R.id.txt_body_notification, body);
        remoteViews.setImageViewResource(R.id.img_notification, R.drawable.img_2);
        return remoteViews;
    }
}
