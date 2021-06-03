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
import com.example.gradutionthsis.dto.Health;
import com.example.gradutionthsis.dto.Relative;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddHealthActivity extends AppCompatActivity {
    private static final String TAG = "HealthActivity";

    private TextView txtTime;
    private EditText edtWeight, edtHeight;
    private ProgressBar progressBar;
    Relative relative;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_health);

        //Khởi tạo action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); //Hiển thị nút quay về trên thanh Action Bar
            actionBar.setTitle(getResources().getString(R.string.add_new)); //Chỉnh sửa title trên ActionBar
        }

        dbHelper = new DBHelper(this);

        txtTime = findViewById(R.id.textTime);
        edtWeight = findViewById(R.id.inputWeight);
        edtHeight = findViewById(R.id.inputHeight);
        progressBar = findViewById(R.id.progressBar);

        String pattern = "dd/MM/yyyy";
        @SuppressLint("SimpleDateFormat")
        DateFormat df = new SimpleDateFormat(pattern);
        txtTime.setText(df.format(Calendar.getInstance().getTime()));

        Button btnSave = findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(v -> {
            if (valid()) {
                Log.i(TAG, "Button save is clicked!");
                progressBar.setVisibility(View.VISIBLE);
                insertHealth();
                finish();
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
     * @return trả về đồi tượng relative được gửi từ ProfileFragment
     * @author: Nguyễn Thanh Tường
     * date: 01/5/2021 : 11h52p
     */
    //Nhận dữ liệu thân nhân dược gửi đến - reciveObject
    // [START reciveObject]
    private Relative reciveObject() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            return (Relative) bundle.getSerializable("object");
        } else {
            Toast.makeText(this, "Dữ liệu rỗng ", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    // [END reciveObject]

    /**
     * @author Nguyễn Thanh Tường
     * date: 01/5/2021 : 11h59p
     */
    //Thêm sức khỏe trẻ em
    // [START insertHealth]
    private void insertHealth() {
        Health health = new Health();
        relative = reciveObject();  //Gán data vào relative

        if (relative != null) {
            health.setIdRelative(relative.getIdRelative());
            health.setWeight(Double.parseDouble(edtWeight.getText().toString().trim()));
            health.setHeight(Double.parseDouble(edtHeight.getText().toString().trim()));
            health.setTime(txtTime.getText().toString());

            //Lưu health xuống SQLite
            if (dbHelper.insertHealth(health) > 0) {
                Log.d(TAG, "id_relative: " + relative.getIdRelative());
                Log.i(TAG, "Health' object: " + health.toString());
                Log.d(TAG, "insertHealth: Success!!!");
                Toast.makeText(this, "Lưu thành công", Toast.LENGTH_SHORT).show();
            } else{
                progressBar.setVisibility(View.INVISIBLE);
                Log.d(TAG, "insertHealth: Lưu thất bại!!!");
            }
        }
    }
    // [END insertHealth]

}