package com.example.gradutionthsis.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import com.example.gradutionthsis.adapter.CustomAdapter;
import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.R;
import com.example.gradutionthsis.dto.Health;
import com.example.gradutionthsis.dto.Relative;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListHealthActivity extends AppCompatActivity {

    private static final String TAG = "ListHealthActivity";

    private Relative relative;
    private ListView listView;
    CustomAdapter<Health> customAdapter;

    private List<Health> healthList;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_health);

        //Khởi tạo action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); //Hiển thị nút quay về trên thanh Action Bar
            actionBar.setTitle(getResources().getString(R.string.monitor_health_at_home)); //Chỉnh sửa title trên ActionBar
        }

        dbHelper = new DBHelper(this);
        relative = reciveObject();//Gán data vào relative

        Button btnAdd = findViewById(R.id.buttonAdd);
        listView = findViewById(R.id.listHealth);

        btnAdd.setOnClickListener(v -> sendObject(relative, AddHealthActivity.class));
        setListView();
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
        menuInflater.inflate(R.menu.menu_relative, menu);

        this.invalidateOptionsMenu();
        return super.onCreateOptionsMenu(menu);
    }
    // [END onCreateOptionsMenu]



    @Override
    protected void onStart() {
        setListView();
        super.onStart();
    }


    /**
     * @author: Nguyễn Thanh Tường
     * date: 06/5/2021 : 23h43p
     * */
    //Phương thức xử lý sự kiện menu - Method of handling the menu event
    // [START onOptionsItemSelected]
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //Xử lý sự kiện của nút back trên action bar
        switch (item.getItemId()) {
            case android.R.id.home: //Xử lý sự kiện của nút back trên action bar
                finish();
                return true;
            case R.id.iAnalytis:
                sendObject(relative, GrowthChartActivity.class);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    // [END onOptionsItemSelected]


    private void setListView() {
        if ( getListHealth() !=null){
            customAdapter = new CustomAdapter<>(this, R.layout.item_health, getListHealth());

            listView.setAdapter(customAdapter);
            listView.setSelected(true);
            listView.setOnItemClickListener((parent, view, position, id) -> Log.i(TAG, "setListView: " + Objects.requireNonNull(getListHealth()).get(position).getIdHealth()));
        }
    }

    /**
     * @author Nguyễn Thanh Tường
     * date: 02/5/2021 : 14h43p
     */
    //Lấy danh sách thân nhân từ cơ sở dữ liệu - Get a list of relatives from database
    // [START getListHealth]
    private List<Health> getListHealth() {
        Log.d(TAG, "getListHealth: " + (relative != null ? relative.getIdRelative() : -1));
        if (relative == null) {
            Log.d(TAG, "getListHealth: The object Relative is null!");
        } else {
            healthList = dbHelper.getAllHealthsById(relative.getIdRelative());
            if (healthList.size() > 0) {
                ArrayList<String> list_string = new ArrayList<>();
                for (Health health : healthList) {
                    list_string.add(String.valueOf(health.getWeight()));
                }
            } else {
                Log.d(TAG, "getListHealth: null");
                return null;
            }
        }
        return healthList;
    }
    // [END getListHealth]

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
            Log.d(TAG, "reciveObject: Nhận được object");
            return (Relative) bundle.getSerializable("object");
        } else {
            Log.d(TAG, "reciveObject: null");
        }
        return null;
    }
    // [END reciveObject]


    /**
     * @param aClass lớp nhận đối tượng được truyền đến - The class that receives the object is passed on
     * @param object Dữ liệu đối tượng được gửi đi - Data object to be sent
     * @author Nguyễn Thanh Tường
     * date: 03/5/2021 : 15h06p
     */
    //Phương thức gửi object sang activity khác - Method of send the object to different activity
    // [START sendObject]
    @SuppressWarnings("uncheck")
    private void sendObject(Object object, Class aClass) {
        Intent intent = new Intent(getApplicationContext(), aClass);
        Bundle bundle = new Bundle();

        bundle.putSerializable("object", (Serializable) object);
        intent.putExtras(bundle);
        Log.i(TAG, "sendObject: Sender!");
        startActivity(intent);
    }
}