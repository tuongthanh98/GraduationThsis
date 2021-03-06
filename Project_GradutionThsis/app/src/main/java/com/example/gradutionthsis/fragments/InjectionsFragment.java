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

import com.example.gradutionthsis.R;
import com.example.gradutionthsis.activity.DetailInjectionActivity;
import com.example.gradutionthsis.activity.TabRelativeActivity;
import com.example.gradutionthsis.adapter.CustomAdapterInjection;
import com.example.gradutionthsis.dto.DetailSchedule;
import com.example.gradutionthsis.dto.Injection;
import com.example.gradutionthsis.presenter.DetailSchedulePresenter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class InjectionsFragment extends Fragment {
    private static final String TAG = InjectionsFragment.class.getSimpleName();
    private final int idRelative;


    private List<Injection> injections;
    private final List<String> listHeader = new ArrayList<>();
    private final HashMap<String, List<Injection>> listHashMap = new HashMap<>();

    private Context mContext;
    private TextView txtUnfinished;
    private ExpandableListView expandableListView;

    DetailSchedulePresenter schedulePresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        setExpandList();
    }

    public InjectionsFragment(int idRelative) {
        this.idRelative = idRelative;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_injections, container, false);
        mContext = getActivity();
        schedulePresenter = new DetailSchedulePresenter(getContext());

        txtUnfinished = root.findViewById(R.id.textUnfinished);
        expandableListView = root.findViewById(R.id.expand_injected);

        setExpandList();
        return root;
    }

    /**
     * @author: Nguy???n Thanh T?????ng
     * @date 26/05/2021 : 12h54p
     * */
    //X??? l?? d??? li???u cho expand list
    private void setExpandList() {
        TabRelativeActivity tabRelativeActivity = new TabRelativeActivity();
        injections = tabRelativeActivity.xuLyData(getContext(), idRelative, TabRelativeActivity.COMPLETED);
        CustomAdapterInjection customAdapterInjection = new CustomAdapterInjection(getContext(), listHeader, listHashMap);

        txtUnfinished.setVisibility(View.INVISIBLE);//???n ??i textView
        expandableListView.setAdapter(customAdapterInjection);
        expandableListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            //L???y chi ti???t l???ch ti??m t???i v??? tr?? ???? ch???n
            DetailSchedule detailSchedule = schedulePresenter.getDetailSchedule(idRelative, Objects.requireNonNull(listHashMap.get(listHeader.get(groupPosition))).get(childPosition).getIdInjection());
            sendObject(detailSchedule);
            return false;
        });
        initData();

        for (int i = 0; i < listHeader.size(); i++) {
            expandableListView.expandGroup(i, false);
        }

        customAdapterInjection.notifyDataSetChanged();
        //Ki???m tra n???u listHeader r???ng hi???n text view
        if (listHeader.size() == 0) txtUnfinished.setVisibility(View.VISIBLE);//Hi???n textView
    }

    /**
     * @author: Nguy???n Thanh T?????ng
     * @date 25/05/2021
     */
    //Kh???i t???o n???i dung ban ?????u
    //[START initData]
    private void initData() {
        String[] array = getResources().getStringArray(R.array.schedule);
        listHeader.addAll(Arrays.asList(array));

        //L???y ng?????c danh s??ch listHeader
        for (int i = listHeader.size() - 1; i >= 0; i--) {
            int month = Integer.parseInt(listHeader.get(i).trim()) - 1; //Th??ng ti??m s??? b???ng v??? tr?? ph???n t??? trong danh s??ch list Header - 1 gi?? tr???
            List<Injection> newList = new ArrayList<>();    //M???ng m???i ????? c???p nh???t danh s??ch item con trong group
            for (int j = 0; j < injections.size(); j++) {   //L???y ng?????c danh s??ch injections
                Injection injection = injections.get(j);
                // so s??nh th??ng ti??m c???a injection v???i th??ng ti??m trong listHeader
                if (injection.getinjectionMonth() >= month) {
                    newList.add(injection);
                    injections.remove(injection);//Lo???i b??? ph???n t??? ????
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
     * @param object D??? li???u ?????i t?????ng ???????c g???i ??i - Data object to be sent
     * @author: Nguy???n Thanh T?????ng
     * @date 03/5/2021 : 15h06p
     */
    //Ph????ng th???c g???i object sang activity kh??c - Method of send the object to different activity
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