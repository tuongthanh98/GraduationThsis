package com.example.gradutionthsis.service;

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

import androidx.annotation.Nullable;

import com.example.gradutionthsis.AlarmReceiver;
import com.example.gradutionthsis.MainActivity;
import com.example.gradutionthsis.R;

public class MyService extends Service {

    private static final String TAG = MyService.class.getSimpleName();
    private static final String CHANNEL_DEFAULT_IMPORTANCE = "channel_service";
    private static final String CHANNEL_NAME_SERVICE = "Channel Service";
    private static final int ONGOING_NOTIFICATION_ID = 1;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        String title = intent.getStringExtra("title");
//        String message = intent.getStringExtra("message");
//        int idInjection = intent.getExtras().getInt("idInjection");
//        int notifId = intent.getExtras().getInt("notifId");
//        AlarmReceiver.showAlarmNotification(this, title, message, notifId, idInjection);


        startFG();
        Log.d(TAG, "onStartCommand: Service is listeninggg!!!");
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    /**
     * @author: Nguyễn Thanh Tường
     * @date 31/5/2021 : 16h47p
     */
    //Khởi tạo Foreground
    //[START startFG]
    public void startFG() {
        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification =
                new Notification.Builder(this, AlarmReceiver.CHANNEL_ID)
                        .setContentTitle(getText(R.string.app_name))
                        .setContentText(getText(R.string.starting))
                        .setSmallIcon(R.drawable.ic_app)
                        .setContentIntent(pendingIntent)
                        .setTicker(getText(R.string.ticker_text))
                        .setChannelId(CHANNEL_DEFAULT_IMPORTANCE).build();

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_DEFAULT_IMPORTANCE, CHANNEL_NAME_SERVICE, NotificationManager.IMPORTANCE_HIGH);
//        notificationChannel.enableVibration(false); //Thiêt lập chế độ rung khi nhận được notification
        notificationManager.createNotificationChannel(notificationChannel);
        // Notification ID cannot be 0.
        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }
    //[END startFG]


}
