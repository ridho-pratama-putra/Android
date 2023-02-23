package com.example.android;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    public MyFirebaseMessagingService() {
        Log.d(TAG, "MyFirebaseMessagingService constructed");
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token);
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM registration token with any
     * server-side account maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "sendRegistrationToServer");
        // TODO: Implement this method to send token to your app server.
    }

    @Override
    public void onMessageReceived(RemoteMessage message) {
        super.onMessageReceived(message);

        // to filter am I is a group of people who's allowed to receive this notification or not
        List<String> authorizedGroupTopic = Arrays.asList("TOPIC-SAMPLE");

        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From ::: " + message.getFrom());
        String notificationMessage;

        // Check if message contains a notification payload.
        if (message.getNotification() != null) {
            notificationMessage = message.getNotification().getBody();
            Log.d(TAG, "Message Notification Body ::: " + message.getNotification().getBody());
        } else {
            Map<String, String> data = message.getData();
            notificationMessage = data.get("body");
            Log.d(TAG, "Message Notification from data Body ::: " + notificationMessage);
        }

        showNotification(notificationMessage);
    }

    private void showNotification(String message) {
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        String CHANNEL_ID_TOPIC_SAMPLE = "New Artist";
        Notification newArtistComing = new NotificationCompat.Builder(this, CHANNEL_ID_TOPIC_SAMPLE)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle("New Artist coming")
                .setDefaults(Notification.DEFAULT_VIBRATE) // to trigger vibrate
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_MAX) // to show notification when app open
                .build();
        managerCompat.notify(NotificationID.getID(), newArtistComing);
    }

    public static class NotificationID {
        private final static AtomicInteger c = new AtomicInteger(0);
        public static int getID() {
            return c.incrementAndGet();
        }
    }
}
