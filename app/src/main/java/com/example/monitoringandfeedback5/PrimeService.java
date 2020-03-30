package com.example.monitoringandfeedback5;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class PrimeService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // no need to return a binder,
        // we are implementing a started service
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        //NotificationCompat notification = new NotificationCompat.Builder()
    }
}
