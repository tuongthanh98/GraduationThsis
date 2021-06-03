package com.example.gradutionthsis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.gradutionthsis.activity.AddRelativeActivity;
import com.example.gradutionthsis.activity.DetailInjectionActivity;
import com.example.gradutionthsis.activity.VaccineActivity;
import com.example.gradutionthsis.dto.DetailSchedule;
import com.example.gradutionthsis.dto.Injection;
import com.example.gradutionthsis.dto.NotificationTask;
import com.example.gradutionthsis.dto.Relative;
import com.example.gradutionthsis.fragments.VaccineFragment;
import com.example.gradutionthsis.fragments.RegulationFragment;
import com.example.gradutionthsis.fragments.ProfileFragment;
import com.example.gradutionthsis.fragments.SettingFragment;
import com.example.gradutionthsis.service.MyService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.play.core.internal.m;

import java.util.Calendar;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActionBar toolbar;
    private MenuItem menuItem;

    private DBHelper dbHelper;
    private AlarmReceiver alarmReceiver;
    private NotificationTask notificationTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = getSupportActionBar();
        BottomNavigationView navigation = findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        toolbar.setTitle(getResources().getText(R.string.immunization_book));           //Đặt tiêu đều cho thanh bar- set title actionbar
        loadFragment(new ProfileFragment());                                            //Hiển thị nội dung fragment trang chủ - load fragment home

        //Notification
        alarmReceiver = new AlarmReceiver();


        dbHelper = new DBHelper(this);

        if (isCheckedObjectNotifyTask())
            if (isCancelAlarm()) {
                notifyDetailSchedule();
            }

//        startService();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    protected void onStart() {
        super.onStart();
        if (isCancelAlarm()) {
            notifyDetailSchedule();
        }
    }
//
//    public void startService(){
//        Intent intent = new Intent(getApplicationContext(), MyService.class);
//        getApplicationContext().startService(intent);
//    }

    /**
     * @author: Nguyễn Thanh Tường
     * @date 09/4/2021 : 11h19p
     */
    //Sự kiện của bottomNavigationView khi chọn item bất kì - The bottomNavigationView event when selecting any item
    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                //Xử lý sự kiện item home trên thanh bottom navigation
                case R.id.navigation_home:
                    toolbar.setTitle(getResources().getText(R.string.regulation));
                    loadFragment(new RegulationFragment());   //load fragment home
                    menuItem.setVisible(false);
                    Log.i(TAG, "onNavigationItemSelected: Clicked item Home in navigation");
                    break;
                //Xử lý sự kiện item profile trên thanh bottom navigation
                case R.id.navigation_profile:
                    toolbar.setTitle(getResources().getText(R.string.immunization_book));
                    loadFragment(new ProfileFragment());   //load fragment home
                    menuItem.setVisible(true);
                    Log.i(TAG, "onNavigationItemSelected: Clicked item Profile in navigation");
                    break;
                //Xử lý sự kiện item vaccine trên thanh bottom navigation
                case R.id.navigation_vaccine:
                    toolbar.setTitle(getResources().getText(R.string.vaccine));
                    loadFragment(new VaccineFragment());   //load fragment Vaccine
                    menuItem.setVisible(false);
                    Log.i(TAG, "onNavigationItemSelected: Clicked item Vaccine in navigation");
                    break;
                //Xử lý sự kiện item schedule trên thanh bottom navigation
                case R.id.navigation_schedule:
                    toolbar.setTitle(getResources().getText(R.string.settings));
                    menuItem.setVisible(false);
                    loadFragment(new SettingFragment());   //load fragment Settings
                    Log.i(TAG, "onNavigationItemSelected: Clicked item Schedule in navigation");
                    break;
            }
            return true;
        }
    };


    /**
     * @author: Nguyễn Thanh Tường
     * @date 09/4/2021 : 11h19p
     */
    //load fragment
    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    /*
      @author: Nguyễn Thanh Tường
     * date: 07/4/2021 : 14h08p
     * */
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if (currentUser == null){
//            startActivity(new Intent(MainActivity.this, LoginActivity.class ));
//            finish();
//        }
//
//    }

    /**
     * @author: Nguyễn Thanh Tường
     * @date 04/4/2021 : 18h20p
     */
    //Phương thức chỉ định menu - Khỏi tạo layout - Method of specifying the menu
    // [START onCreateOptionsMenu]
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        menuItem = menu.findItem(R.id.iconAdd);   // Khởi tạo item menu icon_add
        menuItem.setVisible(true);             //Ẩn hiện item menu

        this.invalidateOptionsMenu();
        return true;
    }
    // [END onCreateOptionsMenu]


    /**
     * @author: Nguyễn Thanh Tường
     * @date 04/4/2021 : 18h20p
     */
    //Phương thức xử lý sự kiện menu - Method of handling the menu event
    // [START onOptionsItemSelected]
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        boolean result = true;
        if (item.getItemId() == R.id.iconAdd) {
            startActivity(new Intent(this, AddRelativeActivity.class));   // Mở activity_relatives thông qua class RelativesActivity
        } else if (item.getItemId() == R.id.iconNotify) {
            startActivity(new Intent(this, VaccineActivity.class));   // Mở activity_relatives thông qua class RelativesActivity
        } else {
            result = super.onOptionsItemSelected(item);
        }
        return result;
    }
    // [END onOptionsItemSelected]


    /**
     * @author: Nguyễn Thanh Tường
     * @date 24/05/2021 : 17h04p
     */
    //Thông báo chi tiết lịch tiêm phòng
    //[START notifyDetailSchedule]
    private void notifyDetailSchedule() {
        notificationTask = dbHelper.getAllTask().get(0);
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
                // Ngày hiện hành = ngày hiện hành + day với day là thời gian thông báo.
                // . Để tiến hành kiểm tra các mũi tiêm đã bỏ lỡ
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


    private void notifyMissInjected() {

    }

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
        String dateFormat = (dateArray[2] + "-" + dateArray[1] + "-" + dateArray[0]);
//        Log.d(TAG, "DateFormat: " + dateFormat);
        return dateFormat;
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
        alarmReceiver.setOneAlarmTime(MainActivity.this, AlarmReceiver.TYPE_ONE_TIME, date, dayNotify, time, title, message);
    }
    //[END isCheckedNotify]


    /**
     * @author: Nguyễn Thanh Tường
     * @date 18/05/2021 : 9h36p
     */
    //Phương thức kiểm tra xem đã có đối tượng NotificationTask chưa
    //[START isCheckedObjectNotify]
    private boolean isCheckedObjectNotifyTask() {
        //Đặt trạng thái thông báo mặc định
        //Kiểm tra xem đã có task trong SQLite chưa
        if (dbHelper.getAllTask().size() > 0) {
            notificationTask = dbHelper.getAllTask().get(0);    //Lấy task thông báo ra
            return true;
        }
        //Thêm task thông báo mới
        if (dbHelper.insertNotifyTask(new NotificationTask(1, 1, 0, 7, 0)) > 0) {
            notificationTask = dbHelper.getAllTask().get(0);
            Log.d(TAG, "isCheckedObjectNotifyTask: Tạo thông báo thành công!");
        }
        return false;
    }
    //[END isCheckedObjectNotify]

    /**
     * @author: Nguyễn Thanh Tường
     * @date 18/05/2021 : 9h42p
     */
    //Phương thức kiểm tra bật/tắt thông báo
    //[START isCancelAlarm]
    private boolean isCancelAlarm() {
        NotificationTask task = dbHelper.getAllTask().get(0);   //Lấy task thông báo từ DBhelper
        if (task.getStatus() != 0)  //Kiểm tra trạng thái cúa thông báo
            return true;

        //Nếu status false thực hiện hàm tắt notification
        if (alarmReceiver.isAlarmSet(MainActivity.this, AlarmReceiver.TYPE_ONE_TIME))
            alarmReceiver.cancelAlarm(MainActivity.this, AlarmReceiver.TYPE_ONE_TIME);
        return false;
    }
    //[END isCancelAlarm]

    //Cài đặt sự kiện nút quay lại của thiết bị áp dụng cho API 5 trở lên
    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
