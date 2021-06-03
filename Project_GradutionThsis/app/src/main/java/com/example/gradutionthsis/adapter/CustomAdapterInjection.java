package com.example.gradutionthsis.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.gradutionthsis.DBHelper;
import com.example.gradutionthsis.R;
import com.example.gradutionthsis.dto.Injection;
import com.example.gradutionthsis.dto.Vaccine;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CustomAdapterInjection extends BaseExpandableListAdapter {
//    private static final String TAG = CustomAdapterInjection.class.getSimpleName();

    private final Context context;
    private final List<String> listGroup;
    private final HashMap<String, List<Injection>> listItem;

    public CustomAdapterInjection(Context context, List<String> listGroup, HashMap<String, List<Injection>> listItem) {
        this.context = context;
        this.listGroup = listGroup;
        this.listItem = listItem;
    }

    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Objects.requireNonNull(this.listItem.get(this.listGroup.get(groupPosition))).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Objects.requireNonNull(this.listItem.get(this.listGroup.get(groupPosition))).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint({"SetTextI18n", "InflateParams"})
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String group = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_header, null);
        }

        TextView txtHeader = convertView.findViewById(R.id.list_item);
        txtHeader.setText(group);
        return convertView;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        DBHelper dbHelper = new DBHelper(context.getApplicationContext());
        Vaccine vaccine = dbHelper.getVaccineById(((Injection) getChild(groupPosition,childPosition)).getIdVaccine());

        String injectionName  = ((Injection) getChild(groupPosition,childPosition)).getInjectionName();
        String vaccineName  = vaccine.getVaccination();
        if (convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_schedule, null);
        }

        TextView txtInjectionName = convertView.findViewById(R.id.textInjectionName);
        TextView txtVaccineName = convertView.findViewById(R.id.vaccineName);

        txtInjectionName.setText(injectionName);
        txtVaccineName.setText(vaccineName);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
