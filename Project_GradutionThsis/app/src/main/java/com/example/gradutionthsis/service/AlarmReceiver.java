package com.example.gradutionthsis.service;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.core.content.ContextCompat;

import com.example.gradutionthsis.R;
import com.example.gradutionthsis.activity.DetailInjectionActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmReceiver.class.getSimpleName();
    public static final String TYPE_ONE_TIME = "OneTimeAlarm";
//    public static final String TYPE_REPEATING = "RepeatingAlarm";
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_INJECTION_ID = "idInjection";
    public static final String CHANNEL_ID = "channel_notif_alarm";
    public static int ID_RELATIVE = 1;
    public static int ID_INJECTION = 1;
    private static final CharSequence CHANNEL_NAME = "Alarm Channel";

    public static final int ID_ONE_TIME = 100;
    public static final int ID_REPEATING = 101;
    
    public AlarmReceiver() {

    }

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    @Override
    public void onReceive(Context context, Intent intent) {
//        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        String title = intent.getStringExtra(EXTRA_TITLE);
        int idInjection = intent.getIntExtra(EXTRA_INJECTION_ID,0);

        showAlarmNotification(context, title, message, getNotificationId(), idInjection);
    }

    /**
     * @author: Nguyễn Thanh Tường
     * @date 15/05/2021 : 18h59p
     */
    //Lấy id thông báo tránh bị trùng lặp đè lên nhau mà không hiển thị được.
    // Vd: noti_1 thông báo nhưng noti_2 dùng chung id nên đè lên noti_1 và làm mất thông báo ví nó đã hiển thị noti_1 trước đó thông qua id dùng chung
    //[START getNotificationId]
    private int getNotificationId() {
        return (int) new Date().getTime();
    }


    /**
     * @author: Nguyễn Thanh Tường
     * @date 15/05/2021 : 18h59p
     */
    //Hiển thị thông báo
    //[START showAlarmNotification]
    @SuppressLint("ObsoleteSdkInt")
    public static void showAlarmNotification(Context context, String title, String message, int notifId, int idInjection) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); //cài đặt nhac chuông

        Intent intent = new Intent(context, DetailInjectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("idRelative", ID_RELATIVE);
        intent.putExtra("idInjection", idInjection);
        intent.putExtra("requestCode", TAG);


        ///Khởi tạo StackBuilder
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);

//        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);
        PendingIntent pendingIntent = stackBuilder.getPendingIntent(notifId, PendingIntent.FLAG_UPDATE_CURRENT);



        //Khởi tạo notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.outline_notifications_black_18) //Set small icon
                .setContentTitle(title)     //Tiêu đề
                .setContentText(message)    //Nội dung tin nhắn
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))//Big text expandable notification
                .setContentIntent(pendingIntent) //mở ra activity
                .setColor(ContextCompat.getColor(context, android.R.color.holo_red_dark)) //Đặt màu
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}) //Đặt độ rung
                .setSound(uri)//Đặt âm thanh
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)// hiển thị các thông tin notification cơ bản, như icon, tiêu đề, nhưng che đi nội dung ở màn hình khóa
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = builder.build();
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableVibration(true); //Thiêt lập chế độ rung khi nhận được notification
            notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000}); //Thiết lập kiểu chế độ rung được sử dụng
            builder.setChannelId(CHANNEL_ID); // Tạo notification yêu cầu một channel ID. Đảm bảo rằng notification của ứng dụng thuộc về một channel để có thể được quản lý bởi người dùng từ setting device

            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.notify(notifId, notification); //Hiển thị notify
        }
    }
    //[END showAlarmNotification]


    /**
     * @param date    ngày thông báo
     * @param time    thời gian thông báo (giờ, phút)
     * @param message nội dung tin nhắn
     * @author: Nguyễn Thanh Tường
     * @date 15/05/2021 : 21h49p
     */
    //Cài đặt thông báo 1 lần
    //[START setOneAlarmTime]
    public void setOneAlarmTime(Context context, String type, String date, int dayNotify, String time, String title, String message) {
        if (isDateInvalid(date, "yyyy-MM-dd") || isDateInvalid(time, "HH:mm"))
            return;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(EXTRA_TITLE, title);
        intent.putExtra(EXTRA_TYPE, type);
        intent.putExtra(EXTRA_INJECTION_ID, ID_INJECTION);

        String[] dateArray = date.split("-");
        String[] timeArray = time.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, Integer.parseInt(dateArray[0]));
        calendar.set(Calendar.MONTH, Integer.parseInt(dateArray[1]) - 1);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateArray[2]) - dayNotify);
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
        calendar.set(Calendar.SECOND, 0);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, getNotificationId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        if (alarmManager != null)
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        Toast.makeText(context, "One time alarm is set", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "setOneAlarmTime: One time alarm is set");
    }
    //[END setOneAlarmTime]


//    /**
//     * @author: Nguyễn Thanh Tường
//     * @date 15/05/2021 : 22h26p
//     */
//    //Cài đặt thông báo lặp
//    //[START setRepeatingAlert]
//    public void setRepeatingAlert(Context context, String type, String time, String message) {
//        if (isDateInvalid(time, "HH:mm"))
//            return;
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, AlarmReceiver.class);
//        intent.putExtra(EXTRA_MESSAGE, message);
//        intent.putExtra(EXTRA_TYPE, type);
//
//        String[] timeArray = time.split(":");
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]));
//        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]));
//        calendar.set(Calendar.SECOND, 0);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        if (alarmManager != null)
//            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//        Toast.makeText(context, "Repeating alarm is set", Toast.LENGTH_SHORT).show();
//    }
//    //[END setRepeatingAlert]


    /**
     * @author: Nguyễn Thanh Tường
     * @date 15/05/2021 : 19h22p
     */
    //Hủy thông báo
    //[START cancelAlarm]
    public void cancelAlarm(Context context, String type) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);

        int requestCode = type.equalsIgnoreCase(TYPE_ONE_TIME) ? ID_ONE_TIME : ID_REPEATING;
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (alarmManager != null)
            alarmManager.cancel(pendingIntent);
    }
    //[END cancelAlarm]

    /**
     * @author: Nguyễn Thanh Tường
     * @date 15/05/2021 : 19h19p
     */
    //Hiển thị thông báo
    //[START isAlarmSet]
    public boolean isAlarmSet(Context context, String type) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_ONE_TIME) ? ID_ONE_TIME : ID_REPEATING;

        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }
    //[END isAlarmSet]


    /**
     * @param date   chuỗi ngày cần kiểm tra
     * @param format định dạng
     * @author: Nguyễn Thanh Tường
     * @date 15/05/2021 : 19h16p
     */
    //Kiểm tra định dạng ngày
    //[START isDateInvalid]
    public boolean isDateInvalid(String date, String format) {
        DateFormat df = new SimpleDateFormat(format, Locale.getDefault());  //Khởi tạo dateformat
        df.setLenient(false); // Bắn về lỗi khi parse 1 date VD: 31/2 => Trả về flase

        try {
            df.parse(date);
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return true;
        }
    }
    //[END isDateInvalid]


}


