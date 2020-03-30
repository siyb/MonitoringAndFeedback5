package com.example.monitoringandfeedback5;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.Arrays;

public class PrimeService extends Service {
    public static final String COUNT_TO = "com.example.monitoringandfeedback.PrimeService.COUNT_TO";
    private static final String CHANNEL_ID = "com.example.monitoringandfeedback.PrimeService.CHANNEL_ID";
    private static final int NOTIFICATION_ID = 124123;
    private String actualChannelId;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // no need to return a binder,
        // we are implementing a started service
        return null;
    }

    public String createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel c = new NotificationChannel(
                    CHANNEL_ID,
                    "Prime Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager == null) throw new RuntimeException("notification manager not available");
            manager.createNotificationChannel(c);
            return CHANNEL_ID;
        } else {
            return "";
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        actualChannelId = createChannel();
    }

    /**
     * Taken and adapted from:
     * http://www.learn4master.com/interview-questions/leetcode/an-efficient-prime-number-generation-algorithm-java
     */
    void primeNumbers(int n) {
        if (n <= 1) {
            return;
        }
        boolean[] isPrime = new boolean[n + 1];
        Arrays.fill(isPrime, true);
        isPrime[2] = true;
        for (int i = 2; i <= n; i++) {
            if (isPrime[i]) {
                System.out.println(Thread.currentThread().getName() + " - PRIME " + i);
                for (int j = i; j <= n; j += i) {
                    isPrime[j] = false;
                }
            }
        }
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new NotificationCompat
                .Builder(this, actualChannelId)
                .setContentTitle(getString(R.string.content_title))
                .setContentText(getString(R.string.content_text))
                .setContentIntent(pendingIntent)
                .build();
        startForeground(NOTIFICATION_ID, notification);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                primeNumbers(intent.getIntExtra(COUNT_TO, 1000));
            }
        });
        // starting our calculation
        t.start();
        return START_NOT_STICKY;
    }
}
