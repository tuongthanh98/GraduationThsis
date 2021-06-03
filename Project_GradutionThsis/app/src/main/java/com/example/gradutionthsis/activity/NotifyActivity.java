package com.example.gradutionthsis.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.R;
import com.example.gradutionthsis.dto.NotificationTask;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class NotifyActivity extends AppCompatActivity {
    private final static String TAG = "NotifyActivity";

    private SwitchMaterial switchMaterial;
    private RadioGroup radGroup;
    private RadioButton radSelect;
    private TimePicker timePicker;

    private NotificationTask notificationTask;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        //Khởi tạo title của dialog
        TextView title = new TextView(this);
        title.setText(R.string.reset);
        title.setPadding(20, 30, 20, 30);
        title.setTextSize(20F);
        title.setBackgroundColor(Color.CYAN);
        title.setTextColor(Color.WHITE);
        title.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_help_outline_black_18, 0, 0, 0); //Set icon for title


        //Khởi tạo action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); //Hiển thị nút quay về trên thanh Action Bar
            actionBar.setIcon(R.drawable.outline_notifications_black_18); //Chỉnh sửa icon trên ActionBar
            actionBar.setTitle(getResources().getString(R.string.setting_notification)); //Chỉnh sửa title trên ActionBar
        }
        dbHelper = new DBHelper(this);
        notificationTask = dbHelper.getAllTask().get(0);

        switchMaterial = findViewById(R.id.switchMaterial);
        radGroup = findViewById(R.id.radioGroup);
        timePicker = findViewById(R.id.timePicker);
        Button btnSave = findViewById(R.id.buttonSave);
        Button btnBack = findViewById(R.id.buttonBack);

        setData();


        switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                notificationTask.setStatus(1);
            } else {
                notificationTask.setStatus(0);
            }
            Log.d(TAG, "setAction: " + isChecked);
        });

        //Lấy giờ và phút
        timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            notificationTask.setHour(hourOfDay);
            notificationTask.setMinute(minute);
        });


        btnBack.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> setNotificationValues());

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Xử lý sự kiện của nút back trên action bar
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * @author: Nguyễn Thanh Tường
     * @date 14/05/2021 : 13h40p
     **/
    //Đặt các giá trị thông báo - set Notification Value
    // [START setNotificationValue]
    public void setNotificationValues() {
        radSelect = findViewById(radGroup.getCheckedRadioButtonId());

        //Lấy ngày thông báo
        if (radSelect.getId() == R.id.radCurrent)
            notificationTask.setDay(0);
        else
            notificationTask.setDay(3);

        if (dbHelper.updateTask(notificationTask) > 0) {
            Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "setNotificationValues: " + notificationTask.toString());
            finish();
        }
    }
    // [END setNotificationValue]


    /**
     * @author: Nguyễn Thanh Tường
     * @date 14/05/2021 : 13h40p
     **/
    //Đặt các giá trị thông báo - set Notification Value
    // [START setData]
    private void setData() {
        switchMaterial.setChecked(notificationTask.getStatus() == 1);
        if (notificationTask.getDay() == 0)
            radSelect = findViewById(R.id.radCurrent);
        else
            radSelect = findViewById(R.id.radBefore);
        radSelect.setChecked(true);
        timePicker.setHour(notificationTask.getHour());
        timePicker.setMinute(notificationTask.getMinute());
    }
    // [END setData]

    //Cài đặt sự kiện nút quay lại của thiết bị áp dụng cho API 5 trở lên
    @Override
    public void onBackPressed() {
        finish();//
        super.onBackPressed();
    }

//    //Cài đặt sự kiện nút của thiết bị áp dụng cho API 5 trở xuống
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        //Cài đặt sự kiện nút quay lại của thiết bị
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            finish();//
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}