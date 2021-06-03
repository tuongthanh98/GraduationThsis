package com.example.gradutionthsis.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.MainActivity;
import com.example.gradutionthsis.R;
import com.example.gradutionthsis.adapter.ViewPagerAdapter;
import com.example.gradutionthsis.dto.DetailSchedule;
import com.example.gradutionthsis.dto.Injection;
import com.example.gradutionthsis.dto.Relative;
import com.example.gradutionthsis.fragments.InjectionsFragment;
import com.example.gradutionthsis.fragments.MissedInjectionFragment;
import com.example.gradutionthsis.fragments.UninjectedFragment;
import com.google.android.material.tabs.TabLayout;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TabRelativeActivity extends AppCompatActivity {
    private static final String TAG = TabRelativeActivity.class.getSimpleName();
    public static final int MISS = 0;//Danh sách các mũi đã qua - chưa tiêm
    public static final int UPCOMING = 1;//Danh sách các mũi sắp tiêm
    public static final int COMPLETED = 2;//Danh sách cái mũi đã tiêm

    Relative relative;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_relative);

        dbHelper = new DBHelper(this);

        ActionBar actionBar = getSupportActionBar();
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ViewPager viewPager = findViewById(R.id.viewpPage);

        relative = reciveObject();

        //Khởi tạo action bar
        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true); //Hiển thị nút quay về trên thanh Action Bar
            actionBar.setTitle(relative != null ? relative.getFullName() : null);
        }

        UninjectedFragment uninjectedFragment = new UninjectedFragment(Objects.requireNonNull(relative).getIdRelative());
        InjectionsFragment injectionsFragment = new InjectionsFragment(Objects.requireNonNull(relative).getIdRelative());
        MissedInjectionFragment missedInjectionFragment = new MissedInjectionFragment(Objects.requireNonNull(relative).getIdRelative());

        tabLayout.setupWithViewPager(viewPager);

        //Khởi tạo ViewPager Adapter
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(uninjectedFragment, getResources().getString(R.string.uninjected));
        viewPagerAdapter.addFragment(injectionsFragment, getResources().getString(R.string.injected));
        viewPagerAdapter.addFragment(missedInjectionFragment, getResources().getString(R.string.pass));

        viewPager.setAdapter(viewPagerAdapter);

        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.injection); // chèn icon vào item Tablayout
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.injection); // chèn icon vào item Tablayout
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.injection); // chèn icon vào item Tablayout

//            BadgeDrawable badgeDrawable = tabLayout.getTabAt(0).getOrCreateBadge();
//            badgeDrawable.setVisible(true);

//        xuLyData(COMPLETED);
    }

    /**
     * @author: Nguyễn Thanh Tường
     * date: 06/5/2021 : 23h41p
     * */
    //Phương thức chỉ định menu - Khỏi tạo layout - Method of specifying the menu
    // [START onCreateOptionsMenu]
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_tab_relative, menu);

        this.invalidateOptionsMenu();
        return super.onCreateOptionsMenu(menu);
    }
    // [END onCreateOptionsMenu]

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Xử lý sự kiện của nút back trên action bar
        switch (item.getItemId()) {
//            case android.R.id.home: //Xử lý sự kiện của nút back trên action bar
//                finish();
//                startActivity(new Intent(this, MainActivity.class));
//                return true;
            case R.id.iHealth:
                sendObject(relative);
                Log.i(TAG, "onContextItemSelected: Edit of menu context is selected!");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * @author: Nguyễn Thanh Tường
     * @date 26/05/2021 : 3h
     */
    //Xứ lý danh sách mũi tiêm
    public List<Injection> xuLyData(@NonNull DBHelper dbHelper, int idRelative, int type) {
        List<DetailSchedule> detailSchedules = dbHelper.getDetailSchedulesById(idRelative);
        List<Injection> injections = new ArrayList<>();

        // Xử lý mũi tiêm đã hoàn tất
        if (type == COMPLETED)
            for (DetailSchedule detailSchedule : detailSchedules)
                if (detailSchedule.getStatus() != 0) {
                    injections.add(dbHelper.getInjectionById(detailSchedule.getIdInjection()));
                }

        //Xử lý mũi tiêm chưa hoàn thành đã quá hạn
        if (type == MISS){
            for (DetailSchedule detailSchedule : detailSchedules)
                if (detailSchedule.getStatus() != 1 && compareDate(detailSchedule.getInjectionTime())) {
                    injections.add(dbHelper.getInjectionById(detailSchedule.getIdInjection()));
//                    Log.d(TAG, "PASS: " + dbHelper.getInjectionById(detailSchedule.getIdInjection()).toString());
//                    Log.d(TAG, "\nTime: " + detailSchedule.getInjectionTime());
                }
        }

        //Xử lý các mũi tiêm sắp tới
        if (type == UPCOMING)
            for (DetailSchedule detailSchedule : detailSchedules)
                if (detailSchedule.getStatus() != 1 && !compareDate(detailSchedule.getInjectionTime())) {
                    injections.add(dbHelper.getInjectionById(detailSchedule.getIdInjection()));
//                    Log.d(TAG, "UPCOMING: " + dbHelper.getInjectionById(detailSchedule.getIdInjection()).toString());
                }
        return injections;
    }


    /**
     * @author: Nguyễn Thanh Tường
     * @date 26/05/2021 15h33p
     * */
    private boolean compareDate(String dateTime){
        String pattern = "dd/MM/yyyy";
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            Date strDate =df.parse(dateTime);
            //So sánh ngày hiện hành với ngày tiêm
            if (new Date().after(strDate)){
                Log.d(TAG, "\ncompareDate: " + true);
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * @param object Dữ liệu đối tượng được gửi đi - Data object to be sent
     * @author Nguyễn Thanh Tường
     * date: 03/5/2021 : 15h06p
     */
    //Phương thức gửi object sang activity khác - Method of send the object to different activity
    // [START sendObject]
    @SuppressWarnings("uncheck")
    private void sendObject(Object object) {
        Intent intent = new Intent(getApplicationContext(), ListHealthActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("object", (Serializable) object);
        intent.putExtras(bundle);
        Log.i(TAG, "sendObject: Sender!");
        startActivity(intent);
    }


    /*
     * @author: Nguyễn Thanh Tường
     * date: 16/4/2021 : 16h34p
     * */
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
}