package com.example.gradutionthsis.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.example.gradutionthsis.R;
import com.example.gradutionthsis.adapter.CustomAdapterE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KnowledgeActivity extends AppCompatActivity {
//    private static  final String TAG = KnowledgeActivity.class.getSimpleName();

    List<String> listGroup;
    HashMap<String, List<String>> listItem;
    CustomAdapterE adapterE;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); //Hiển thị nút quay về trên thanh Action Bar
            actionBar.setTitle(getResources().getString(R.string.hint)); //Chỉnh sửa title trên ActionBar
        }

        ExpandableListView expandableListView = findViewById(R.id.eplChat);
        listGroup = new ArrayList<>();
        listItem = new HashMap<>();
        adapterE = new CustomAdapterE(this, listGroup, listItem);
        expandableListView.setAdapter(adapterE);

        initData();
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
     * @date 25/05/2021
     * */
    //Khởi tạo nội dung ban đầu
    //[START initData]
    private void initData() {
        listGroup.add("Phân loại vaccine");
        listGroup.add("Những hiểu lầm về tiêm chủng");
        listGroup.add("Tiêm chủng cùng lúc 2 loại vaccine");
        listGroup.add("Chăm sóc sau tiêm chủng");

        List<String> list = new ArrayList<>();
        list.add(getString(R.string.vaccine_classification));
        List<String> list1 = new ArrayList<>();
        list1.add(getString(R.string.misunderstandings));
        List<String> list2 = new ArrayList<>();
        list2.add(getString(R.string.get_two_vaccines));
        List<String> list3 = new ArrayList<>();
        list3.add(getString(R.string.post_injection_care));

        listItem.put(listGroup.get(0), list);
        listItem.put(listGroup.get(1), list1);
        listItem.put(listGroup.get(2), list2);
        listItem.put(listGroup.get(3), list3);



        adapterE.notifyDataSetChanged();
    }
    //[END initData]
}