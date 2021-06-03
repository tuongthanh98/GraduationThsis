package com.example.gradutionthsis.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gradutionthsis.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomAdapterString extends BaseAdapter {

    private final Context context;
    private final String[] title;
    private final int[] icon;

    public CustomAdapterString(Context context, String[] title, int[] icon) {
        this.context = context;
        this.title = title;
        this.icon = icon;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting, parent, false);

        TextView textItem = convertView.findViewById(R.id.textItem);
        CircleImageView imageView = convertView.findViewById(R.id.imgIcon);

        textItem.setText(title[position]);
        imageView.setImageResource(icon[position]);

        return convertView;
    }
}
