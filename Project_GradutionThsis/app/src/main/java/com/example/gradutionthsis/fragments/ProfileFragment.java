package com.example.gradutionthsis.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.R;
import com.example.gradutionthsis.activity.TabRelativeActivity;
import com.example.gradutionthsis.adapter.CustomAdapter;
import com.example.gradutionthsis.dto.Relative;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";

    private Context mContext;
    private ListView listView;
    private DBHelper dbHelper;

    ArrayList<Relative> listRelatives;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = getActivity();
        dbHelper = new DBHelper(getContext());

        listView = root.findViewById(R.id.list_relatives);

//        recyclerView = root.findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        RecyclerViewAdapter viewAdapter = new RecyclerViewAdapter(getContext(), R.layout.activity_items_relative, listRelatives);
//        recyclerView.setAdapter(viewAdapter);

        //Set Action Bar for fragment
//        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        actionBar.setTitle("Hello");
        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListRelative();
    }

    //Khi mở rồi đóng activity khác làm mới lại nội dung. Làm tươi listView
    @Override
    public void onStart() {
        super.onStart();
        getListRelative();
    }

    /**
     * @author: Nguyễn Thanh Tường
     * @date 09/4/2021 : 11h19p
     */
    //Lấy danh sách thân nhân từ cơ sở dữ liệu - Get a list of relatives from database
    // [START getList]
    public void getListRelative() {
        listRelatives = dbHelper.getAllRelatives();
        if (listRelatives.size() > 0) {
            CustomAdapter customAdapter = new CustomAdapter<>(Objects.requireNonNull(getActivity(),"null").getApplicationContext(), R.layout.item_relative, listRelatives);
            listView.setAdapter(customAdapter);
            listView.setSelected(true);
            listView.setOnItemClickListener((adapterView, view, i, l) -> {
                Log.i(TAG, "idRelative: " + listRelatives.get(i).getIdRelative());
                sendObject(listRelatives.get(i));
                Log.i(TAG, "getListRelative: Success!");
            });
        }
    }
    // [END getList]


    /**
     * @param object Dữ liệu đối tượng được gửi đi - Data object to be sent
     * @author: Nguyễn Thanh Tường
     * @date 03/5/2021 : 15h06p
     */
    //Phương thức gửi object sang activity khác - Method of send the object to different activity
    // [START sendObject]
    @SuppressWarnings("uncheck")
    private void sendObject(Object object) {
        Intent intent = new Intent(mContext.getApplicationContext(), TabRelativeActivity.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("object", (Serializable) object);
        intent.putExtras(bundle);
        Log.i(TAG, "sendObject: " + object.toString());
        startActivity(intent);
    }

}