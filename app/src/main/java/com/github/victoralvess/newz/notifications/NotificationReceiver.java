package com.github.victoralvess.newz.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.github.victoralvess.newz.R;
import com.github.victoralvess.newz.activities.MainActivity;
import com.github.victoralvess.newz.adapters.SharedPreferencesUserRepository;
import com.github.victoralvess.newz.usecases.ManageUserPreferenceUseCase;

import java.util.concurrent.atomic.AtomicInteger;

public class NotificationReceiver extends BroadcastReceiver {
    private final String CHANNEL_ID = "com.github.victoralvess.newsproject.notifications";
    AtomicInteger count = new AtomicInteger();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("NOTIFICATION", "SENDING");
        ManageUserPreferenceUseCase preferenceUseCase = new ManageUserPreferenceUseCase(
                new SharedPreferencesUserRepository(
                        context.getSharedPreferences(MainActivity.class.getName(), Context.MODE_PRIVATE),
                        context.getString(R.string.subject_key)
                )
        );

        if (preferenceUseCase.getSubject() == null) {
            Log.i("NOTIFICATION", "NO SUBJECT");
            return;
        }

        Log.i("NOTIFICATION", "BUILDING AND SENDING");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = CHANNEL_ID;
            String description = CHANNEL_ID;

            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


        Intent mainIntent = new Intent(context, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(mainIntent);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("\uD83D\uDC4B Ei, mantenha-se informado!")
                .setContentText("Temos novas notícias pra você \uD83D\uDCF0")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        notificationManager.notify(count.getAndIncrement(), builder.build());
    }
}
