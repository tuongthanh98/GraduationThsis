package com.example.gradutionthsis;

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
import androidx.core.content.ContextCompat;

import com.example.gradutionthsis.activity.DetailInjectionActivity;
import com.example.gradutionthsis.service.MyService;

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

    @Override
    public void onReceive(Context context, Intent intent) {
//        String type = intent.getStringExtra(EXTRA_TYPE);
        String message = intent.getStringExtra(EXTRA_MESSAGE);
        String title = intent.getStringExtra(EXTRA_TITLE);
        int idInjection = intent.getIntExtra(EXTRA_INJECTION_ID,0);

        Intent intent1 = new Intent(context.getApplicationContext(), MyService.class);
        intent1.putExtra("message", message);
        intent1.putExtra("title", title);
        intent1.putExtra("idInjection", idInjection);
        intent1.putExtra("notifId", getNotificationId());

//        context.startService(intent1);
        showAlarmNotification(context, title, message, getNotificationId(), idInjection);
    }

    /**
     * @author: Nguy???n Thanh T?????ng
     * @date 15/05/2021 : 18h59p
     */
    //L???y id th??ng b??o tr??nh b??? tr??ng l???p ???? l??n nhau m?? kh??ng hi???n th??? ???????c.
    // Vd: noti_1 th??ng b??o nh??ng noti_2 d??ng chung id n??n ???? l??n noti_1 v?? l??m m???t th??ng b??o v?? n?? ???? hi???n th??? noti_1 tr?????c ???? th??ng qua id d??ng chung
    //[START getNotificationId]
    private int getNotificationId() {
        return (int) new Date().getTime();
    }


    /**
     * @author: Nguy???n Thanh T?????ng
     * @date 15/05/2021 : 18h59p
     */
    //Hi???n th??? th??ng b??o
    //[START showAlarmNotification]
    @SuppressLint("ObsoleteSdkInt")
    public static void showAlarmNotification(Context context, String title, String message, int notifId, int idInjection) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION); //c??i ?????t nhac chu??ng

        Intent intent = new Intent(context, DetailInjectionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("idRelative", ID_RELATIVE);
        intent.putExtra("idInjection", idInjection);
        intent.putExtra("requestCode", TAG);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), intent, 0);

        //Kh???i t???o notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.outline_notifications_black_18) //Set small icon
                .setContentTitle(title)     //Ti??u ?????
                .setContentText(message)    //N???i dung tin nh???n
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))//Big text expandable notification
                .setContentIntent(pendingIntent) //m??? ra activity
                .setColor(ContextCompat.getColor(context, android.R.color.holo_red_dark)) //?????t m??u
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000}) //?????t ????? rung
                .setSound(uri)//?????t ??m thanh
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)// hi???n th??? c??c th??ng tin notification c?? b???n, nh?? icon, ti??u ?????, nh??ng che ??i n???i dung ??? m??n h??nh kh??a
                .setAutoCancel(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = builder.build();
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableVibration(true); //Thi??t l???p ch??? ????? rung khi nh???n ???????c notification
            notificationChannel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000}); //Thi???t l???p ki???u ch??? ????? rung ???????c s??? d???ng
            builder.setChannelId(CHANNEL_ID); // T???o notification y??u c???u m???t channel ID. ?????m b???o r???ng notification c???a ???ng d???ng thu???c v??? m???t channel ????? c?? th??? ???????c qu???n l?? b???i ng?????i d??ng t??? setting device

            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.notify(notifId, notification); //Hi???n th??? notify
        }
    }
    //[END showAlarmNotification]


    /**
     * @param date    ng??y th??ng b??o
     * @param time    th???i gian th??ng b??o (gi???, ph??t)
     * @param message n???i dung tin nh???n
     * @author: Nguy???n Thanh T?????ng
     * @date 15/05/2021 : 21h49p
     */
    //C??i ?????t th??ng b??o 1 l???n
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
//     * @author: Nguy???n Thanh T?????ng
//     * @date 15/05/2021 : 22h26p
//     */
//    //C??i ?????t th??ng b??o l???p
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
     * @author: Nguy???n Thanh T?????ng
     * @date 15/05/2021 : 19h22p
     */
    //H???y th??ng b??o
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
     * @author: Nguy???n Thanh T?????ng
     * @date 15/05/2021 : 19h19p
     */
    //Hi???n th??? th??ng b??o
    //[START isAlarmSet]
    public boolean isAlarmSet(Context context, String type) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        int requestCode = type.equalsIgnoreCase(TYPE_ONE_TIME) ? ID_ONE_TIME : ID_REPEATING;

        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_NO_CREATE) != null;
    }
    //[END isAlarmSet]


    /**
     * @param date   chu???i ng??y c???n ki???m tra
     * @param format ?????nh d???ng
     * @author: Nguy???n Thanh T?????ng
     * @date 15/05/2021 : 19h16p
     */
    //Ki???m tra ?????nh d???ng ng??y
    //[START isDateInvalid]
    public boolean isDateInvalid(String date, String format) {
        DateFormat df = new SimpleDateFormat(format, Locale.getDefault());  //Kh???i t???o dateformat
        df.setLenient(false); // B???n v??? l???i khi parse 1 date VD: 31/2 => Tr??? v??? flase

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


