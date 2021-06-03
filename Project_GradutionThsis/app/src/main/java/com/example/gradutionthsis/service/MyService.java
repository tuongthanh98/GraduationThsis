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

        NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_DEFAULT_IMPORTANCE, CHANNEL_NAME_SERVICE, NotificationManager.IMPORTANCE_NONE);
//        notificationChannel.enableVibration(false); //Thiêt lập chế độ rung khi nhận được notification
        notificationManager.createNotificationChannel(notificationChannel);
        // Notification ID cannot be 0.
        startForeground(ONGOING_NOTIFICATION_ID, notification);
    }
    //[END startFG]



    /**
     * @author: Nguyễn Thanh Tường
     * @date 24/05/2021 : 17h04p
     */
    //Thông báo chi tiết lịch tiêm phòng
    //[START notifyDetailSchedule]
    private void notifyDetailSchedule() {
        NotificationTask notificationTask = dbHelper.getAllTask().get(0);
        String title, message;
        int day = notificationTask.getDay(); // lấy ngày thông báo

//        List<DetailSchedule> detailSchedules = dbHelper.getDSchedulesByTime(getCurrentDay(day));   //Lấy danh sách có chung ngày thông báo

        List<DetailSchedule> detailSchedules = dbHelper.getDSchedulesByNotify();   //Lấy danh sách trạng thái thông báo của mũi tiêm là 0

        for (DetailSchedule detailSchedule : detailSchedules) {
            String date = detailSchedule.getInjectionTime().replace('/', '-'); // replace kí tự '/' thành kí tự '-'

            //Kiểm tra trạng thái của chi tiết lịch tiêm phòng
            //Nếu == 0 thì thông báo
            if (detailSchedule.getStatus() != DetailInjectionActivity.STATUS_CODE) {
                detailSchedule.setNotification(1);  //đặt lại notification

                //Kiểm tra giữa ngày tiêm phòng và ngày hiện hành
                //Ngày hiện hành = ngày hiện hành + day với day là thời gian thông báo.
                //Để tiến hành kiểm tra các mũi tiêm đã bỏ lỡ
                if (DetailInjectionActivity.compareDate(detailSchedule.getInjectionTime(), getCurrentDay(day)) > DetailInjectionActivity.CHECK_STATUS_CODE) {

                    //Cập nhật field notification của chi tiết lịch tiêm phòng trong db
                    if (dbHelper.updateDetailSchedule(detailSchedule) > 0) {
                        Relative relative = dbHelper.getRelativeById(detailSchedule.getIdRelative());
                        Injection injection = dbHelper.getInjectionById(detailSchedule.getIdInjection());

                        if (relative != null) {
                            title = relative.getFullName();
                            message = "Đã qua ngày tiêm! \nBạn hãy cập nhật " + injection.getInjectionName() + " của vaccine " + dbHelper.getVaccineById(injection.getIdVaccine()).getVaccination() + "!";
                            AlarmReceiver.ID_RELATIVE = relative.getIdRelative();
                            AlarmReceiver.ID_INJECTION = injection.getIdInjection();
                            isCheckedNotify(setFormat(date), day, notificationTask.getHour(), notificationTask.getMinute(), title, message);
                        }
                    }
                }

                if (DetailInjectionActivity.compareDate(detailSchedule.getInjectionTime(), getCurrentDay(day)) == DetailInjectionActivity.CHECK_STATUS_CODE){
                    //Cập nhật field notification của chi tiết lịch tiêm phòng trong db
                    if (dbHelper.updateDetailSchedule(detailSchedule) > 0) {
                        Relative relative = dbHelper.getRelativeById(detailSchedule.getIdRelative());
                        Injection injection = dbHelper.getInjectionById(detailSchedule.getIdInjection());

                        if (relative != null) {
                            title = relative.getFullName();
                            if (day == 3)
                                message = "Còn 3 ngày nữa là tới ngày tiêm " + injection.getInjectionName() + " vaccine " + dbHelper.getVaccineById(injection.getIdVaccine()).getVaccination() + " của " + ". \nBạn hãy cập nhật lại thời gian để được báo chính xác nhé!";
                            else
                                message = "Đã tới ngày tiêm! \nBạn hãy cập nhật " + injection.getInjectionName() + " của vaccine " + dbHelper.getVaccineById(injection.getIdVaccine()).getVaccination() + "!";
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
     * @param dayNotify Ngày  trong đối tương NotificationTask
     * @author: Nguyễn Thanh Tường
     * @date 24/05/2021 : 16h56p
     */
    //Lấy ngày hiện hành
    //[START getCurrentDay]
    private String getCurrentDay(int dayNotify) {
        Calendar c = Calendar.getInstance();
        c.getTime();//Lấy ngày hiện hành
        int dayOfMonth = c.get(Calendar.DAY_OF_MONTH) + dayNotify; //cộng ngày thông báo 0 hoặc 3 ngày
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        return ((dayOfMonth) + "/" + (month + 1) + "/" + year);
    }
    //[END getCurrentDay]


    /**
     * @author: Nguyễn Thanh Tường
     * @date 24/05/2021 16h55p
     */
    //Đặt lại định dạng cho ngày thành yyyy-MM-dd
    //[START setFormat]
    private String setFormat(String injectionTime) {
        String[] dateArray = injectionTime.split("-");
        //        Log.d(TAG, "DateFormat: " + dateFormat);
        return (dateArray[2] + "-" + dateArray[1] + "-" + dateArray[0]);
    }
    //[END setFormat]


    /**
     * @param message nội dung tin
     * @param date    ngày thông báo định dạng thao yyyy-MM-ddnhắn
     * @param hour    giờ thông báo
     * @param minute  phút thông báo
     * @author: Nguyễn Thanh Tường
     * @date 18/05/2021 : 9h52p
     */
    //Phương thức kiểm tra valid Notify
    //[START isCheckedNotify]
    private void isCheckedNotify(String date, int dayNotify, int hour, int minute, String title, String message) {
        String time = hour + ":" + minute;  // đặt format cho time (thời gian) theo định dạng HH:mm - hour:minute
        alarmReceiver.setOneAlarmTime(this, AlarmReceiver.TYPE_ONE_TIME, date, dayNotify, time, title, message);
    }
    //[END isCheckedNotify]
}
