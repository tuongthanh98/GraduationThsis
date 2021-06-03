package com.example.gradutionthsis.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.R;
import com.example.gradutionthsis.activity.VaccineActivity;
import com.example.gradutionthsis.adapter.CustomAdapterInjection;
import com.example.gradutionthsis.dto.Injection;
import com.example.gradutionthsis.dto.Vaccine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class VaccineFragment extends Fragment {
//    private static final String TAG = VaccineFragment.class.getSimpleName();

    private List<Vaccine> list = new ArrayList<>();
    DBHelper dbHelper;
    List<String> listHeader;
    HashMap<String, List<Injection>> listHashMap;
    List<Injection> injections;


    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_vaccine, container, false);

        dbHelper = new DBHelper(getContext());
        list = dbHelper.getAllVaccines();
        listHeader = new ArrayList<>();
        listHashMap = new HashMap<>();
        injections = dbHelper.getAllInjections();
        ExpandableListView expandableListView = root.findViewById(R.id.eplChat);
        CustomAdapterInjection customAdapterInjection = new CustomAdapterInjection(getContext(), listHeader, listHashMap);

        expandableListView.setAdapter(customAdapterInjection);
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            int idVaccine = Objects.requireNonNull(listHashMap.get(listHeader.get(groupPosition))).get(childPosition).getIdVaccine();
            for (Vaccine vaccine : list)
                if (vaccine.getIdVaccine() == idVaccine)
                    sendObject(vaccine);
            return false;
        });
        initData();

//        Bung mở expandableListView
        for (int i = 0; i < listHeader.size(); i++) {
            expandableListView.expandGroup(i, false);
        }
        customAdapterInjection.notifyDataSetChanged();
        return root;
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
    //[START initData]

    /**
     * @param object Dữ liệu đối tượng được gửi đi - Data object to be sent
     * @author Nguyễn Thanh Tường
     * @date 03/5/2021 : 15h06p
     */
    //Phương thức gửi object sang activity khác - Method of send the object to different activity
    // [START sendObject]
    @SuppressWarnings("uncheck")
    private void sendObject(Serializable object) {
        Intent intent = new Intent(Objects.requireNonNull(getContext(), "null").getApplicationContext(), VaccineActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("object", object);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}