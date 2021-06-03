package com.example.gradutionthsis;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import com.example.gradutionthsis.dto.Injection;
import com.example.gradutionthsis.dto.Vaccine;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class InitializationActivity extends AppCompatActivity {
    private static final String TAG = InitializationActivity.class.getSimpleName();
    private final Gson gson = new Gson();
    DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);
        dbHelper = new DBHelper(this);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        //Đếm ngược 3s
        new CountDownTimer(7000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                insertAllVaccine();
                insertAllInjection();
            }

            @Override
            public void onFinish() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        }.start();
    }

    /**
     * @author: Nguyễn Thanh Tường
     * @date 21/05/2021 : 19h32p
     */
    //Thêm danh sách vaccine từ file json
    //[START insertAllVaccine]
    private void insertAllVaccine() {
        /*
        list = dbHelper.getAllVaccines();
        if (list.toArray().length != 0)
            Toast.makeText(getContext(), list.toString(), Toast.LENGTH_SHORT).show();*/

        if (dbHelper.getAllVaccines().size() == 0) {
            String json = readStringJson(InitializationActivity.this, R.raw.vaccines);
            if (json != null) {
                Vaccine[] vaccines = gson.fromJson(json, Vaccine[].class);
                for (Vaccine vaccine : vaccines)
                    if (dbHelper.insertVaccine(vaccine) > 0)
                        Log.d(TAG, "onCreateView: " + "success");
            }
        }
    }
    //[END insertAllVaccine]

    /**
     * @author Nguyễn Thanh Tường
     * date: 09/4/2021 : 19h13p
     */
    // Đọc file json trong res -> raw
    private String readStringJson(Context context, int fileJson) {
        String json = null;
        try {
            InputStream inputStream = context.getResources().openRawResource(fileJson);
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    /**
     * @author: Nguyễn Thanh Tường
     * @date 21/05/2021 : 19h32p
     */
    //Thêm danh sách mũi tiêm từ file json
    //[START insertAllInjection]
    private void insertAllInjection() {
        /*
        list = dbHelper.getAllVaccines();
        if (list.toArray().length != 0)
            Toast.makeText(getContext(), list.toString(), Toast.LENGTH_SHORT).show();*/

        if (dbHelper.getAllInjections().size() == 0) {
            String json = readStringJson(InitializationActivity.this, R.raw.injections);
            if (json != null) {
                Injection[] injections = gson.fromJson(json, Injection[].class);
                for (Injection injection : injections)
                    if (dbHelper.insertInjection(injection) > 0)
                        Log.d(TAG, "onCreateView: " + "success");
            }
        }
    }
    //[END insertAllInjection]
}