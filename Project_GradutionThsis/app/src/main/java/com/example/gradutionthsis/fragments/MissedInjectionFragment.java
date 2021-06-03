package com.example.gradutionthsis.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.R;
import com.example.gradutionthsis.activity.DetailInjectionActivity;
import com.example.gradutionthsis.activity.TabRelativeActivity;
import com.example.gradutionthsis.adapter.CustomAdapterInjection;
import com.example.gradutionthsis.dto.DetailSchedule;
import com.example.gradutionthsis.dto.Injection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MissedInjectionFragment extends Fragment {
    private static final String TAG = MissedInjectionFragment.class.getSimpleName();
    private final int idRelative;


    private List<Injection> injections;
    private final List<String> listHeader = new ArrayList<>();
    private final HashMap<String, List<Injection>> listHashMap = new HashMap<>();

    private Context mContext;
    private TextView txtMissInjection;
    private ExpandableListView expandableListView;
    private DBHelper dbHelper;

    public MissedInjectionFragment(int idRelative) {
        this.idRelative = idRelative;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_missed_injection, container, false);
        mContext = getActivity();
        txtMissInjection = root.findViewById(R.id.textMissInjection);
        dbHelper = new DBHelper(getContext());
        expandableListView = root.findViewById(R.id.expand_injected);

        setExpandList();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        setExpandList();
    }

    /**
     * @author: Nguyễn Thanh Tường
     * @date 26/05/2021 : 12h54p
     * */
    //Xử lý dữ liệu cho expand list
    private void setExpandList() {
        TabRelativeActivity tabRelativeActivity = new TabRelativeActivity();
        injections = tabRelativeActivity.xuLyData(dbHelper, idRelative, TabRelativeActivity.MISS);
//        Schedule schedule = dbHelper.getScheduleById(idSchedule);
        CustomAdapterInjection customAdapterInjection = new CustomAdapterInjection(getContext(), listHeader, listHashMap);

        txtMissInjection.setVisibility(View.INVISIBLE);//Ẩn đi textView
        expandableListView.setAdapter(customAdapterInjection);
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            //Lấy chi tiết lịch tiêm tại vị trí đã chọn
            DetailSchedule detailSchedule = dbHelper.getDetailScheduleById(idRelative, Objects.requireNonNull(listHashMap.get(listHeader.get(groupPosition))).get(childPosition).getIdInjection());
            sendObject(detailSchedule);
            return false;
        });
        initData();

        for (int i = 0; i < listHeader.size(); i++) {
            expandableListView.expandGroup(i, false);
        }

        customAdapterInjection.notifyDataSetChanged();
        //Kiểm tra nếu listHeader rỗng hiện text view
        if (listHeader.size() == 0) txtMissInjection.setVisibility(View.VISIBLE);//Hiện textView
    }

    /**
     * @author: Nguyễn Thanh Tường
     * @date 25/05/2021
     */
    //Khởi tạo nội dung ban đầu
    //[START initData]
    private void initData() {
        String[] array = getResources().getStringArray(R.array.schedule);
        listHeader.addAll(Arrays.asList(array));

        //Lấy ngược danh sách listHeader
        for (int i = listHeader.size() - 1; i >= 0; i--) {
            int month = Integer.parseInt(listHeader.get(i).trim()) - 1; //Tháng tiêm sẽ bằng vị trí phần tử trong danh sách list Header - 1 giá trị
            List<Injection> newList = new ArrayList<>();    //Mảng mới để cập nhật danh sách item con trong group
            for (int j = 0; j < injections.size(); j++) {   //Lấy ngược danh sách injections
                Injection injection = injections.get(j);
                // so sánh tháng tiêm của injection với tháng tiêm trong listHeader
                if (injection.getinjectionMonth() >= month) {
                    newList.add(injection);
                    injections.remove(injection);//Loại bỏ phần tử đó
                    j--;
                }
            }
            if (newList.size() != 0)
                listHashMap.put(listHeader.get(i), newList);
            else {
                listHeader.remove(i);
            }
        }
    }

    /**
     * @param object Dữ liệu đối tượng được gửi đi - Data object to be sent
     * @author: Nguyễn Thanh Tường
     * @date 03/5/2021 : 15h06p
     */
    //Phương thức gửi object sang activity khác - Method of send the object to different activity
    // [START sendObject]
    @SuppressWarnings("uncheck")
    private void sendObject(Object object) {
        Intent intent = new Intent(mContext.getApplicationContext(), DetailInjectionActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("object", (Serializable) object);
        intent.putExtras(bundle);
        Log.i(TAG, "sendObject: " + object.toString());
        startActivity(intent);
    }
}