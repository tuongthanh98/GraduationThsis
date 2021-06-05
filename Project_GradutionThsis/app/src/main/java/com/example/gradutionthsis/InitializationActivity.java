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
import com.example.gradutionthsis.presenter.InjectionDAO;
import com.example.gradutionthsis.presenter.InjectionPresenter;
import com.example.gradutionthsis.presenter.VaccineDAO;
import com.example.gradutionthsis.presenter.VaccinePresenter;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class InitializationActivity extends AppCompatActivity implements VaccineDAO, InjectionDAO {
    private static final String TAG = InitializationActivity.class.getSimpleName();
    private final Gson gson = new Gson();

    VaccinePresenter vaccinePresenter;
    InjectionPresenter injectionPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);

        vaccinePresenter = new VaccinePresenter(this, this);
        injectionPresenter = new InjectionPresenter(this, this);

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

        if (vaccinePresenter.getAllVaccine()) {
            String json = readStringJson(InitializationActivity.this, R.raw.vaccines);
            if (json != null) {
                Vaccine[] vaccines = gson.fromJson(json, Vaccine[].class);
                for (Vaccine vaccine : vaccines)
                    vaccinePresenter.createVaccine(vaccine);
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

        if (injectionPresenter.getAllInjections()) {
            String json = readStringJson(InitializationActivity.this, R.raw.injections);
            if (json != null) {
                Injection[] injections = gson.fromJson(json, Injection[].class);
                for (Injection injection : injections)
                    injectionPresenter.createInjection(injection);
            }
        }
    }

    @Override
    public void createSuccess() {
        Log.d(TAG, "create success!");
    }

    @Override
    public void createFail() {
        Log.d(TAG, "create fail!");
    }
    //[END insertAllInjection]
}