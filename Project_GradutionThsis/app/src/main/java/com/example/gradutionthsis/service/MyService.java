package com.example.gradutionthsis.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.MainActivity;
import com.example.gradutionthsis.R;
import com.example.gradutionthsis.activity.DetailInjectionActivity;
import com.example.gradutionthsis.dto.DetailSchedule;
import com.example.gradutionthsis.dto.Injection;
import com.example.gradutionthsis.dto.NotificationTask;
import com.example.gradutionthsis.dto.Relative;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyService extends Service {

    private static final String TAG = MyService.class.getSimpleName();
    private static final String CHANNEL_DEFAULT_IMPORTANCE = "channel_service";
    private static final String CHANNEL_NAME_SERVICE = "Channel Service";
    private static final int ONGOING_NOTIFICATION_ID = 1;

    private AlarmReceiver alarmReceiver;
    private DBHelper dbHelper;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d(TAG, "onStartCommand: Service is listeninggg!!!");



        startFG();
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Notification
                alarmReceiver = new AlarmReceiver();
                dbHelper = new DBHelper(getApplicationContext());

                notifyDetailSchedule();
                Log.d("LogService", "run: " + new Date().toString());
                mHandler.postDelayed(this, 1000);
            }
        }, 1000);

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
     * @author: Nguy???n Thanh T?????ng
     * @date 31/5/2021 : 16h47p
     */
    //Kh???i t???o Foreground
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

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_DEFAULT_IMPORTANCE, CHANNEL_NAME_SERVICE, NotificationManager.IMPORTANCE_NONE);
//        notificationChannel.enableVibration(false); //Thi??t l???p ch??? ????? rung khi nh???n ???????c notification
        notificationManager.createNotificationChannel(notificationChannel);
        // Notification ID cannot be 0.
        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }
    //[END startFG]



    /**
     * @author: Nguy???n Thanh T?????ng
     * @date 24/05/2021 : 17h04p
     */
    //Th??ng b??o chi ti???t l???ch ti??m ph??ng
    //[START notifyDetailSchedule]
    private void notifyDetailSchedule() {
        NotificationTask notificationTask = dbHelper.getAllTask().get(0);
        String title, message;
        int day = notificationTask.getDay(); // l???y ng??y th??ng b??o

//        List<DetailSchedule> detailSchedules = dbHelper.getDSchedulesByTime(getCurrentDay(day));   //L???y danh s??ch c?? chung ng??y th??ng b??o

        List<DetailSchedule> detailSchedules = dbHelper.getDSchedulesByNotify();   //L???y danh s??ch tr???ng th??i th??ng b??o c???a m??i ti??m l?? 0

        for (DetailSchedule detailSchedule : detailSchedules) {
            String date = detailSchedule.getInjectionTime().replace('/', '-'); // replace k?? t??? '/' th??nh k?? t??? '-'

            //Ki???m tra tr???ng th??i c???a chi ti???t l???ch ti??m ph??ng
            //N???u == 0 th?? th??ng b??o
            if (detailSchedule.getStatus() != DetailInjectionActivity.STATUS_CODE) {
                detailSchedule.setNotification(1);  //?????t l???i notification

                //Ki???m tra gi???a ng??y ti??m ph??ng v?? ng??y hi???n h??nh
                //Ng??y hi???n h??nh = ng??y hi???n h??nh + day v???i day l?? th???i gian th??ng b??o.
                //????? ti???n h??nh ki???m tra c??c m??i ti??m ???? b??? l???
                if (DetailInjectionActivity.compareDate(detailSchedule.getInjectionTime(), getCurrentDay(day)) > DetailInjectionActivity.CHECK_STATUS_CODE) {

                    //C???p nh???t field notification c???a chi ti???t l???ch ti??m ph??ng trong db
                    if (dbHelper.updateDetailSchedule(detailSchedule) > 0) {
                        Relative relative = dbHelper.getRelativeById(detailSchedule.getIdRelative());
                        Injection injection = dbHelper.getInjectionById(detailSchedule.getIdInjection());

                        if (relative != null) {
                            title = relative.getFullName();
                            message = "???? qua ng??y ti??m! \nB???n h??y c???p nh???t " + injection.getInjectionName() + " c???a vaccine " + dbHelper.getVaccineById(injection.getIdVaccine()).getVaccination() + "!";
                            AlarmReceiver.ID_RELATIVE = relative.getIdRelative();
                            AlarmReceiver.ID_INJECTION = injection.getIdInjection();
                            isCheckedNotify(setFormat(date), day, notificationTask.getHour(), notificationTask.getMinute(), title, message);
                        }
                    }
                }

                if (DetailInjectionActivity.compareDate(detailSchedule.getInjectionTime(), getCurrentDay(day)) == DetailInjectionActivity.CHECK_STATUS_CODE){
                    //C???p nh???t field notification c???a chi ti???t l???ch ti??m ph??ng trong db
                    if (dbHelper.updateDetailSchedule(detailSchedule) > 0) {
                        Relative relative = dbHelper.getRelativeById(detailSchedule.getIdRelative());
                        Injection injection = dbHelper.getInjectionById(detailSchedule.getIdInjection());

                        if (relative != null) {
                            title = relative.getFullName();
                            if (day == 3)
                                message = "C??n 3 ng??y n???a l?? t???i ng??y ti??m " + injection.getInjectionName() + " vaccine " + dbHelper.getVaccineById(injection.getIdVaccine()).getVaccination() + " c???a " + ". \nB???n h??y c???p nh???t l???i th???i gian ????? ???????c b??o ch??nh x??c nh??!";
                            else
                                message = "???? t???i ng??y ti??m! \nB???n h??y c???p nh???t " + injection.getInjectionName() + " c???a vaccine " + dbHelper.getVaccineById(injection.getIdVaccine()).getVaccination() + "!";
                            AlarmReceiver.ID_RELATIVE = relative.getIdRelative();
                            AlarmReceiver.ID_INJECTION = injection.getIdInjection();
                            isCheckedNotify(setFormat(date), day, notificationTask.getHour(), notificationTask.getMinute(), title, message);
                        }
                    }
                }

            }
        }
    }
    //[END notifyDetailSchedule]

    /**
     * @param dayNotify Ng??y  trong ?????i t????ng NotificationTask
     * @author: Nguy???n Thanh T?????ng
     * @date 24/05/2021 : 16h56p
     */
    //L???y ng??y hi???n h??nh
    //[START getCurrentDay]
    private String getCurrentDay(int dayNotify) {
        Calendar c = Calendar.getInstance();
        c.getTime();//L???y ng??y hi???n h??nh
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH) + dayNotify; //c???ng ng??y th??ng b??o 0 ho???c 3 ng??y
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        return ((dayOfMonth) + "/" + (month + 1) + "/" + year);
    }
    //[END getCurrentDay]


    /**
     * @author: Nguy???n Thanh T?????ng
     * @date 24/05/2021 16h55p
     */
    //?????t l???i ?????nh d???ng cho ng??y th??nh yyyy-MM-dd
    //[START setFormat]
    private String setFormat(String injectionTime) {
        String[] dateArray = injectionTime.split("-");
        //        Log.d(TAG, "DateFormat: " + dateFormat);
        return (dateArray[2] + "-" + dateArray[1] + "-" + dateArray[0]);
    }
    //[END setFormat]


    /**
     * @param message n???i dung tin
     * @param date    ng??y th??ng b??o ?????nh d???ng thao yyyy-MM-ddnh???n
     * @param hour    gi??? th??ng b??o
     * @param minute  ph??t th??ng b??o
     * @author: Nguy???n Thanh T?????ng
     * @date 18/05/2021 : 9h52p
     */
    //Ph????ng th???c ki???m tra valid Notify
    //[START isCheckedNotify]
    private void isCheckedNotify(String date, int dayNotify, int hour, int minute, String title, String message) {
        String time = hour + ":" + minute;  // ?????t format cho time (th???i gian) theo ?????nh d???ng HH:mm - hour:minute
        alarmReceiver.setOneAlarmTime(this, AlarmReceiver.TYPE_ONE_TIME, date, dayNotify, time, title, message);
    }
    //[END isCheckedNotify]
}
