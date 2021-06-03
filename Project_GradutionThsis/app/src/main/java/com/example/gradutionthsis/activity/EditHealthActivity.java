package com.example.gradutionthsis.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.R;
import com.example.gradutionthsis.dto.DetailSchedule;
import com.example.gradutionthsis.dto.Health;
import com.example.gradutionthsis.dto.Injection;
import com.example.gradutionthsis.dto.Relative;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditHealthActivity extends AppCompatActivity {
    private static final String TAG = "EditHealthActivity";
    private static final int CHECK_STATUS = 0;

    private TextView txtTime;
    private EditText edtWeight, edtHeight;
    private ProgressBar progressBar;
    Health health;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_health);//Khởi tạo action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); //Hiển thị nút quay về trên thanh Action Bar
            actionBar.setTitle(getResources().getString(R.string.edit)); //Chỉnh sửa title trên ActionBar
        }

        dbHelper = new DBHelper(this);
        health = reciveObject();  //Gán data vào relative

        txtTime = findViewById(R.id.textTime);
        edtWeight = findViewById(R.id.inputWeight);
        edtHeight = findViewById(R.id.inputHeight);
        progressBar = findViewById(R.id.progressBar);

        Button btnSave = findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(v -> {
            if (valid()) {
                Log.i(TAG, "Button save is clicked!");
                progressBar.setVisibility(View.VISIBLE);
                updateHealth();
            }
        });

        txtTime.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            Log.d(TAG, "Tháng hiện tại: " + c.get(Calendar.MONTH));

            @SuppressLint("SetTextI18n")
            DatePickerDialog.OnDateSetListener callBack = (DatePicker datePicker, int year, int month, int dayOfMonth) -> (txtTime = findViewById(R.id.textTime)).setText((dayOfMonth) + "/" + (month + 1) + "/" + year);
            DatePickerDialog pic = new DatePickerDialog(this, callBack, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
            pic.show();
        });

        setData();
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
     * @author Nguyễn Thanh Tường
     * date 29/04/2021
     */
    //Kiểm tra tính hợp lệ của dữ liệu nhập vào - Check the validity of the input data
    //[START valid]
    private boolean valid() {
        if (edtWeight.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Cân nặng không được để trống!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtHeight.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Chiều cao không được để trống!!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    //[START valid]

    /**
     * @return trả về đồi tượng Health được gửi từ CustomAdapter
     * @author: Nguyễn Thanh Tường
     * date: 01/5/2021 : 11h52p
     */
    //Nhận dữ liệu sức khỏe được gửi đến - reciveObject
    // [START reciveObject]
    private Health reciveObject() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            return (Health) bundle.getSerializable("object");
        } else {
            Toast.makeText(this, "Dữ liệu rỗng ", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    // [END reciveObject]

    /*
     * @author: Nguyễn Thanh Tường
     * date: 04/5/2021 : 19h48p
     * */
    //Đặt dữ liệu cho các ô thông tin
    // [START setData]
    private void setData() {
        edtWeight.setText(String.valueOf(health.getWeight()));
        edtHeight.setText(String.valueOf(health.getHeight()));
        txtTime.setText(health.getTime());

        Log.i(TAG, "getData: " + health.getIdHealth());
    }
    // [END setData]


    /**
     * @author Nguyễn Thanh Tường
     * date: 01/5/2021 : 11h59p
     */
    //Cập nhật sức khỏe trẻ em
    // [START updateHealth]
    private void updateHealth() {
        Log.i(TAG, "updateHealth: " + health.getIdHealth());
        Relative relative = dbHelper.getRelativeById(health.getIdRelative());

        if (health != null) {
            health.setWeight(Double.parseDouble(edtWeight.getText().toString().trim()));
            health.setHeight(Double.parseDouble(edtHeight.getText().toString().trim()));
            health.setTime(txtTime.getText().toString());

            if (compareDate(relative.getBirthDate(), txtTime.getText().toString().trim()) != DetailInjectionActivity.COMPARE_CODE)
                //Cập nhật health xuống SQLite
                if (dbHelper.updateHealth(health) > 0) {
                    Log.d(TAG, "updateHealth: Sửa thành công!!!");
                    Toast.makeText(this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    Log.d(TAG, "insertHealth: Sửa thất bại!!!");
                    return;
                }
        }
        Toast.makeText(this, "Ngày cập nhật phải bằng hoặc sau ngày sinh!", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
    }
    // [END updateHealth]

    /**
     * @param dayBefore Ngày trước đó
     * @param dayAfter  Ngày sau đó - ngày cập nhật
     * @author: Nguyễn Thanh Tường
     * @date 27/05/2021 15h3p
     */
    //So sánh 2 ngày
    //[START compareDate]
    private int compareDate(String dayBefore, String dayAfter) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date1 = df.parse(dayBefore);   //Ngày trước đó (Ngày lưu trong SQLite)
            Date date2 = df.parse(dayAfter);    //Ngày sau đó (Ngày chọn từ txtInjectionTime)
            if (date1 != null && date2 != null) {
                return date2.compareTo(date1);  //trả về -1 Nếu date2<date1
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return DetailInjectionActivity.CHECK_STATUS_CODE;
    }
    //[END compareDate
}