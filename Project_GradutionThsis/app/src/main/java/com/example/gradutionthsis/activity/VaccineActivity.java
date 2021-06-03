package com.example.gradutionthsis.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gradutionthsis.R;
import com.example.gradutionthsis.dto.Vaccine;

public class VaccineActivity extends AppCompatActivity {

    private TextView txtVaccination, txtVaccineName, txtDisease, txtDescribe, txtNote;
    private Vaccine vaccine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine);

        //Khởi tạo action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); //Hiển thị nút quay về trên thanh Action Bar
            actionBar.setTitle(getResources().getString(R.string.injections)); //Chỉnh sửa title trên ActionBar
        }

        txtVaccination = findViewById(R.id.textVaccination);
        txtVaccineName = findViewById(R.id.textVaccineName);
        txtDisease = findViewById(R.id.textDisease);
        txtDescribe = findViewById(R.id.textDescribe);
        txtNote = findViewById(R.id.textNote);

        vaccine = reciveObject();
        if (vaccine != null)
            setText();
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
     * @date 18/05/2021 : 17h34p
     */
    //Đặt nội dung text
    //[START setText]
    private void setText() {
        txtVaccination.setText(vaccine.getVaccination());
        txtVaccineName.setText(vaccine.getNameVaccine());
        txtDisease.setText(vaccine.getDisease());
        txtDescribe.setText(vaccine.getDescription());
        txtNote.setText(vaccine.getNote());
    }
    //[END setText]


    /*
     * @author: Nguyễn Thanh Tường
     * date: 16/4/2021 : 16h34p
     * */
    //Nhận dữ liệu thân nhân dược gửi đến - reciveObject
    // [START reciveObject]
    private Vaccine reciveObject() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            return (Vaccine) bundle.getSerializable("object");
        } else {
            Toast.makeText(this, "Dữ liệu rỗng ", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    // [END reciveObject]
}